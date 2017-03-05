
package engadirlinhainforme;

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

    String jid_matricula,
           jconductor;
    String aux_jgasto_eur,
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
                    "Problema ó establecer a conexión coa base de datos " + e);
        }
    }

    public ResultSet getConductores()
    {
        ResultSet rs = null;
        Statement s = null;
        try
        {
            s = conexion.createStatement();
            rs = s.executeQuery("SELECT id_conductor FROM conductores");
        }catch (Exception e)
        {
            JOptionPane.showMessageDialog(null,
                    "Problema ó consultar á base de datos."
                    + " Os datos de conductores non foron lidos."
                    + " Intente arrancar manualmente o servidor de bases "
                    + "de datos e reinicie a aplicación"
                    );
        }
        return rs;
    } // getConductores
    
    public ResultSet getVehiculos()
    {
        ResultSet rs = null;
        Statement s = null;
        try
        {
            s = conexion.createStatement();
            rs = s.executeQuery("SELECT id_matricula FROM vehiculos");
        }catch (Exception e)
        {
            JOptionPane.showMessageDialog(null,
                    "Problema ó consultar á base de datos."
                    + " Os datos de vehículos non foron lidos."
                    + " Intente arrancar manualmente o servidor de bases "
                    + "de datos e reinicie a aplicación"
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
            rs = s.executeQuery("SELECT * FROM control_vehic ORDER BY fdata");
        }catch (Exception e)
        {
            JOptionPane.showMessageDialog(null,
                    "Problema ó consultar á base de datos "
                    + "Se fixo previamente un gardado de datos, "
                    + "probablemente estes foron gardados, pero"
                    + " recoméndase comprobalo revisando o un listado de"
                    + " liñas de control de vehículos."
                    );
        }
        return rs;
    }
    
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
    /*
    public static void main (String [] args) throws SQLException
    {
        PruebaMySQL x = new PruebaMySQL() ;
        ResultSet rs = null;
        String cadena="";
        int numLinhas = 0;

        x.insertaLinha();
        x.estableceConexion();
        rs = x.getLinhas();
        try{
        while (rs.next())
        {
            numLinhas++;
        }
        cadena += " ------------------------------------"
                + "------------------------------------------ \n"
                + " EXISTEN EN TOTAL: "
                + String.valueOf(numLinhas)
                + " liñas de control de vehículos almacenadas na BD. \n"
                + " Pode consultarlas sacando un listado en consultas"
                + "\n ------------------------------------"
                + "------------------------------------------ ";
        JOptionPane.showMessageDialog(null, cadena, 
                            "Resumo de Liñas de control existentes",1);
        } catch(Exception e)
        {
            JOptionPane.showMessageDialog(null,
                    "Problema ó imprimir en pantalla resumo da base de datos. "
                    + "Sen embargo, os datos foron gardados");
        }
        x.pechaConexion();
    } // main
    */
}
