
package CreadorPDFs;

/**
 * @author Breo
 */

import java.sql.*;
import javax.swing.JOptionPane;
import java.sql.Time.*;
import java.sql.Date.*;

public class PruebaMySQL {
    private Connection conexion = null;
    private String databasename;

    private String jid_matricula,
                   jconductor;
    private String aux_jgasto_eur,
                   aux_jkm_saida,
                   aux_jkm_chegada,
                   aux_jcombustible_litros,
                   aux_jfdata,
                   aux_jhora_saida,
                   aux_jhora_chegada,
                   aux_jgastos_repara;
    
    public PruebaMySQL(String dbname){
        databasename = dbname;
    }

    public void set_jid_matricula(String new_val){
        jid_matricula = new_val;
    }
    public void set_jconductor(String new_val){
        jconductor = new_val;
    }
    public void set_aux_jgasto_eur(String new_val){
        aux_jgasto_eur = new_val;
    }
    public void set_aux_jkm_saida(String new_val){
        aux_jkm_saida = new_val;
    }
    public void set_aux_jkm_chegada(String new_val){
        aux_jkm_chegada = new_val;
    }
    public void set_aux_jcombustible_litros(String new_val){
        aux_jcombustible_litros = new_val;
    }
    public void set_aux_jfdata(String new_val){
        aux_jfdata = new_val;
    }
    public void set_aux_jhora_saida(String new_val){
        aux_jhora_saida = new_val;
    }
    public void set_aux_jhora_chegada(String new_val){
        aux_jhora_chegada = new_val;
    }
    public void set_aux_jgastos_repara(String new_val){
        aux_jgastos_repara = new_val;
    }

    public void set_Databasename(String new_val){
        databasename = new_val;
    }

    public void estableceConexion()
    {
        if (conexion != null)
            return;
        try
        {

           Class.forName("com.mysql.jdbc.Driver");
           conexion = DriverManager.getConnection(
                   "jdbc:mysql://localhost/" + databasename,"root","");
           /*
            if (conexion !=null){
               JOptionPane.showMessageDialog(null,
                    "Conexión a base de datos ... Ok");
           }*/
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "INTERFACE COA BASE DE DATOS. "
                    + "Problema ó establecer a conexión coa base de datos:\n"
                    + e);
        }
    }

    public ResultSet getAllInConductores()
    {
        ResultSet rs = null;
        Statement s = null;
        try
        {
            s = conexion.createStatement();
            rs = s.executeQuery("SELECT * FROM conductores");
        }catch (Exception e)
        {
            JOptionPane.showMessageDialog(null,
                    "Problema ó consultar á base de datos.\n"
                    + " Os datos de conductores non foron lidos."
                    + " Intente arrancar manualmente o servidor de bases "
                    + "de datos e reinicie a aplicación\n"
                    +e
                    );
        }
        return rs;
    } // getConductores
    
    public ResultSet getAllInVehiculos()
    {
        ResultSet rs = null;
        Statement s = null;
        try
        {
            s = conexion.createStatement();
            rs = s.executeQuery("SELECT * FROM vehiculos");
        }
        catch (SQLException sqle)
        {
            JOptionPane.showMessageDialog(null,
                    "Problema ó consultar á base de datos."
                    + " Os datos de vehículos non foron lidos."
                    + " Intente arrancar manualmente o servidor de bases "
                    + "de datos e reinicie a aplicación.\n"
                    + sqle
                    );
        }
        catch (Exception e)
        {
            JOptionPane.showMessageDialog(null,
                    "Problema ó consultar á base de datos."
                    + " Os datos de vehículos non foron lidos."
                    + " Intente arrancar manualmente o servidor de bases "
                    + "de datos e reinicie a aplicación.\n"
                    + e
                    );
        }
        return rs;
    } //     getVehiculos()


    public ResultSet getLinhas()
    {
        ResultSet rs = null;
        Statement s = null;
        try
        {
            s = conexion.createStatement();
            rs = s.executeQuery(
                    "SELECT * FROM control_vehic ORDER BY fdata, matricula"
                    );
        }catch (Exception e)
        {
            JOptionPane.showMessageDialog(null,
                    "Problema ó consultar á base de datos para obter rexistros"
                    + " de control."
                    + "Se é posible, revise a fonte de datos ou o sistema"
                    + " xestor de bases de datos."
                    );
        }
        return rs;
    } // getLinhas


    public ResultSet getLinhasMensual(Short mes)
    {
        ResultSet rs = null;
        Statement s = null;
        String str_mes = "";
        try{
           str_mes  = Short.toString(mes);
        }
        catch( NullPointerException npe){
            JOptionPane.showMessageDialog(null,
                    "Problema ó converter mes en formato adecuado para consulta"
                    + " na base de datos de rexistros de control por mes"
                    + npe
                    );
        }
        try
        {
            s = conexion.createStatement();
            rs = s.executeQuery(
                    "SELECT * "
                    + "FROM control_vehic "
                    + "WHERE MONTH(fdata) = '"+str_mes+"'"
                    + "order by matricula"
                   );
        }catch(Exception e)
        {
            JOptionPane.showMessageDialog(null,
                    "Problema ó consultar á base de datos para obter rexistros"
                    + " de control\n "
                    + "Se é posible, revise a fonte de datos ou o sistema"
                    + " xestor de bases de datos."
                    );
        }
        return rs;
    } // getLinhasByMounth


    public ResultSet getLinhasByVandMounth(Short mes, String matric)
    {
        ResultSet rs = null;
        Statement s = null;
        String str_mes = "";
        try{
           str_mes  = Short.toString(mes);
        }
        catch( NullPointerException npe){
            JOptionPane.showMessageDialog(null,
                    "Problema ó converter mes en formato adecuado para consulta"
                    + " na base de datos de rexistros de control por mes"
                    + npe
                    );
        }
        try
        {
            s = conexion.createStatement();
            rs = s.executeQuery(
                    "SELECT * "
                    + "FROM control_vehic "
                    + "WHERE MONTH(fdata) = '"+str_mes+"' "
                    + "AND matricula = '"+matric+"' "
                    + "order by fdata"
                   );
        }catch(Exception e)
        {
            JOptionPane.showMessageDialog(null,
                    "Problema ó consultar á base de datos para obter rexistros"
                    + " de control\n "
                    + "Se é posible, revise a fonte de datos ou o sistema"
                    + " xestor de bases de datos."
                    );
        }
        return rs;
    } // getLinhasByVandMounth


    public ResultSet getEstatistVByMounth(Short mes, String matric)
    {
        ResultSet rs = null;
        Statement s = null;
        try
        {
            s = conexion.createStatement();
            rs = s.executeQuery(
                    "SELECT "
                        + "matricula, "
                        + "CONCAT(YEAR(fdata),\"-\",MONTH(fdata)), "
                        + "SUM(km_chegada - km_saida), "
                        + "SUM(combustible_litros), "
                        + "CAST(SUM(combustible_litros)/"
                        + "SUM(km_chegada - km_saida)*100 AS DECIMAL(9,2)), "
                        + "CAST(SUM(gastos_repara) as Decimal(9,2)) "
                    + "FROM control_vehic "
                    + "WHERE MONTH(fdata) = '"+mes+"' "
                    + "AND (matricula = '"+matric+"')"
                );
        }catch (Exception e)
        {
            JOptionPane.showMessageDialog(null,
                    "Problema ó consultar o gasto medio por vehículo na "
                    + "base de datos.\n"
                    + e
                    );
        }
        return rs;
    } // getEstatistVByMounth


    public ResultSet getGastoCombusAos100Medio()
    {
        ResultSet rs = null;
        Statement s = null;
        try
        {
            s = conexion.createStatement();
            rs = s.executeQuery(
                    "SELECT "
                        + "matricula, "
                        + "YEAR(fdata), "
                        + "SUM(km_chegada - km_saida), "
                        + "SUM(combustible_litros), "
                        + "SUM(gasto_eur), "
                        + "CAST(SUM(combustible_litros)/"
                        + "SUM(km_chegada - km_saida)*100 AS DECIMAL(9,2)) "
                    + "FROM control_vehic "
                    + "GROUP BY matricula "
                    + "ORDER BY matricula");
        }catch (Exception e)
        {
            JOptionPane.showMessageDialog(null,
                    "Problema ó consultar o gasto medio na base de datos.\n"
                    + e
                    );
        }
        return rs;
    } // getGastoMedioCombus


    public ResultSet getGastoCombusAos100MedioMensual(Short mes)
    {
        ResultSet rs = null;
        Statement s = null;
        String str_mes = "";
        try{
           str_mes  = Short.toString(mes);
        }
        catch( Exception npe){
            JOptionPane.showMessageDialog(null,
                    "Problema ó converter mes en formato adecuado para consulta"
                    + " na base de datos de rexistros de control por mes.\n"
                    + npe
                    );
        }

        try
        {
            s = conexion.createStatement();
            rs = s.executeQuery(
                    "SELECT "
                        + "matricula, "
                        + "CONCAT(MONTH(fdata),\"-\", YEAR(fdata)), "
                        + "SUM(km_chegada - km_saida), "
                        + "SUM(combustible_litros), "
                        + "SUM(gasto_eur), "
                        + "CAST(SUM(combustible_litros)/"
                        + "SUM(km_chegada - km_saida)*100 as Decimal(9,2)) "
                    + "FROM control_vehic "
                    + "WHERE MONTH(fdata) = '"+str_mes+"' "
                    + "GROUP BY matricula "
                    + "ORDER BY matricula"
                    );
        }catch (Exception e)
        {
            JOptionPane.showMessageDialog(null,
                    "Problema ó consultar o gasto medio mensual "
                    + "na base de datos.\n"
                    + e
                    );
        }
        return rs;
    } // getGastoMedioCombusMensual


    public ResultSet getGastoMedioRepara()
    {
        ResultSet rs = null;
        Statement s = null;
        try
        {
            s = conexion.createStatement();
            rs = s.executeQuery(
                    "SELECT "
                        + "matricula, "
                        + "YEAR(fdata), "
                        + "CAST(avg(gastos_repara) as Decimal(9,2)), "
                        + "CAST(SUM(gastos_repara) as Decimal(9,2))"
                    + "FROM control_vehic "
                    + "WHERE (gastos_repara > 0) "
                    + "GROUP BY matricula "
                    + "ORDER BY matricula"
                    );
        }catch (Exception e)
        {
            JOptionPane.showMessageDialog(null,
                    "Problema ó consultar á base de datos calculando o gasto"
                    + " medio de reparacións."
                    +e
                    );
        }
        return rs;
    } // getGastoMedioReparaAnual

    public ResultSet getGastoMedioReparaMensual(Short mes)
    {
        ResultSet rs = null;
        Statement s = null;
        String str_mes = "";
        try{
           str_mes  = Short.toString(mes);
        }
        catch( Exception npe){
            JOptionPane.showMessageDialog(null,
                    "Problema ó converter mes en formato adecuado para consulta"
                    + " na base de datos de gasto medio mensual.\n"
                    + npe
                    );
        }
        try
        {
            s = conexion.createStatement();
            rs = s.executeQuery(
                    "SELECT "
                        + "matricula, "
                        + "CONCAT(YEAR(fdata),\"-\",MONTH(fdata)), "
                        + "CAST(avg(gastos_repara) as Decimal(9,2)), "
                    + "SUM(gastos_repara)"
                    + "FROM control_vehic "
                    + "WHERE MONTH(fdata) = '"+str_mes+"' AND (gastos_repara > 0) "
                    + "GROUP BY matricula "
                    + "ORDER BY matricula"
                    );
        }catch (Exception e)
        {
            JOptionPane.showMessageDialog(null,
                    "Problema ó consultar á base de datos calculando o gasto"
                    + " medio de reparacións mensual."
                    +e
                    );
        }
        return rs;
    } // getGastoMedioReparaMensual

    public void insertaLinha() throws SQLException
    {
        String erro = " ";
        int n_erros = 0;
        Date jfdata = new Date(0); // public static Date valueOf(String s)
        int jkm_saida = 0,
            jkm_chegada = 0;
        short jcombustible_litros = 0;
        float jgastos_repara = 0,
              jgasto_eur = 0;
        Time jhora_saida = new Time(0); // public static Time valueOf(String s)
        Time jhora_chegada = new Time(0);

        
        int lineas_modi=0;
        PreparedStatement consulta;
        /*
        aux_jfdata = JOptionPane.showInputDialog(null,
                                            "Data (en formato aaaa-mm-dd)?");
        jid_matricula = JOptionPane.showInputDialog(null, "Matricula?");
        aux_jkm_saida = JOptionPane.showInputDialog(null, "km. saída?");
        aux_jkm_chegada = JOptionPane.showInputDialog(null, "km. chegada?");
        aux_jhora_saida = JOptionPane.showInputDialog(null, "hora saída?");
        aux_jhora_chegada = JOptionPane.showInputDialog(null,
                "hora chegada?");
        aux_jgastos_repara = JOptionPane.showInputDialog(null,
                "gastos repara?");
        aux_jcombustible_litros = JOptionPane.showInputDialog(null,
                "litros de combustible (so nº, sen litros, l. etc)?");
        aux_aux_jgasto_eur = JOptionPane.showInputDialog(null,
                "custo en € do combustible (so nº, sen €, Eur, etc)?");
        jconductor = JOptionPane.showInputDialog(null, "Id do conductor?");
        */

        try{
            aux_jhora_saida = aux_jhora_saida + ":00";
            aux_jhora_chegada = aux_jhora_chegada + ":00";
            jkm_saida = Integer.parseInt(aux_jkm_saida);
            jkm_chegada = Integer.parseInt(aux_jkm_chegada);
            jcombustible_litros = Short.parseShort(aux_jcombustible_litros);
            jgastos_repara = Float.parseFloat(aux_jgastos_repara);
            jgasto_eur = Float.parseFloat(aux_jgasto_eur);
        }
        catch (NumberFormatException e){
            JOptionPane.showMessageDialog(null,
                        "Problema ó converter valores (km saída/chegada, "
                        + "gastos de reparación ou gastos de combustible."
                        + "Datos NON gardados. Comprobe"
                        + " que os valores introducidos foron numéricos"
                    );
        }
        catch (Exception e){
            erro = erro + "Erro tratando valores."
                    + " Teñen valores tódolos campos?";
            n_erros++;
        }
        estableceConexion();
        consulta =  conexion.prepareStatement(
                "INSERT INTO control_vehic "
                    + "(fdata,"
                    + " matricula,"
                    + " km_saida,"
                    + " km_chegada,"
                    + " hora_saida,"
                    + " hora_chegada,"
                    + " gastos_repara,"
                    + " combustible_litros,"
                    + " gasto_eur,"
                    + " conductor)"
                + "VALUES "
                    + "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"
                );

        try
        {
            consulta.setDate(1, jfdata.valueOf(aux_jfdata)); // DA ERRO
        }
        catch (IllegalArgumentException e){
            n_erros++;
            erro = erro +" ó converter a data (fecha), ";
        }
        try
        {
            consulta.setString(2, jid_matricula);
            consulta.setInt(3, jkm_saida);
            consulta.setInt(4, jkm_chegada);
        }
        catch (IllegalArgumentException e){
            n_erros++;
            erro = erro +" ó converter matrícula ou kilómetros, ";
        }
        try
        {
            consulta.setTime(5, jhora_saida.valueOf(aux_jhora_saida));
            consulta.setTime(6, jhora_chegada.valueOf(aux_jhora_chegada));
        }
        catch (IllegalArgumentException e){
            n_erros++;
            erro = erro +" ó converter a hora, ";
        }
        
        try
        {
            consulta.setFloat(7, jgastos_repara);
            consulta.setShort(8, jcombustible_litros);
            consulta.setFloat(9, jgasto_eur);
            consulta.setString(10, jconductor);
        }
        catch (IllegalArgumentException e){
            n_erros++;
            erro = erro +" ó converter os gastos de reparación, litros"
                        + " de combustible, gastos de conbustible ou ID do "
                        + "conductor, ";
        }

        try{
            lineas_modi = consulta.executeUpdate();
            /*
            JOptionPane.showMessageDialog(null, lineas_modi
                    + " rexistros agregados");
             */
            erro = erro + Integer.toString(lineas_modi)
                    + " rexistros agregados";
        }
        catch (SQLException e)
        {
            n_erros++;
            erro = erro +" ó realizar a actualización (SQLException)" + e ;
        }
        catch (Exception e)
        {
            n_erros++;
            erro = erro +" indefinido ó realizar a actualización (Exception)";
        }/*
        try{
            System.out.println("Data (fecha) almacenada: " +
                        jfdata.valueOf(aux_jfdata).toString()
                    );
        }
        catch(IllegalArgumentException iae){
            System.out.println("IAE imprimindo "
                    + "jfdata.valueOf(aux_jfdata).toString()");

        }*/
        JOptionPane.showMessageDialog(null, n_erros + " erros: "
                    + erro);
        if( n_erros > 0){
            System.out.println(n_erros + " erros: " + erro);
        }
    }

    public void pechaConexion()
    {
        try
        {
            conexion.close();
        }catch(Exception e)
        {
            JOptionPane.showMessageDialog(null,
                        "Problema para pechar a conexión coa B.D. "
                        + "Probablemente os datos foron gardados, pero"
                        + "recoméndase comprobalo revisando o listado de"
                        + "liñas de control (saque un listado)."
                    );
        }
    }
}
