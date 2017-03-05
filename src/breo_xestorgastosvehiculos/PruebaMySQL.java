
package breo_xestorgastosvehiculos;

/**
 * @author Breo
 */

import java.sql.*;
import javax.swing.JOptionPane;
import java.sql.Time.*;
import java.sql.Date.*;
import java.util.Calendar;

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

    public void estableceConexion() throws interfaceBDException
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
        } catch (SQLException sqle) {

            throw new interfaceBDException(databasename, sqle.getErrorCode());
            /*
            JOptionPane.showMessageDialog(null,
                    "INTERFACE COA BASE DE DATOS. "
                    + "Problema ó establecer a conexión coa base de datos:\n"
                    + e);
             */
        }
        catch (Exception e) {
            /*
            JOptionPane.showMessageDialog(null,
                    "INTERFACE COA BASE DE DATOS. "
                    + "Problema ó establecer a conexión coa base de datos:\n"
                    + e);
             */
        }
    }

    public void creaBD(String db_name)
    {
        this.databasename = db_name;
        Statement st = null;
        try{
            st = conexion.createStatement();
        }
        catch( SQLException sqle){
            JOptionPane.showMessageDialog(null,
                    "ERRO antes de crear a nova base de datos:\n"+sqle);
        }
        catch( NullPointerException npe){
            JOptionPane.showMessageDialog(null,
                    "ERRO antes de crear a nova base de datos:\n"+npe);
        }
        try{
            st.executeUpdate("CREATE DATABASE "+db_name);
        }
        catch( SQLException sqle){
            JOptionPane.showMessageDialog(null,
                    "ERRO creando a nova base de datos:\n"+sqle);
        }
        this.creaEstructuraBD(db_name);
        this.importaConductoresAnteriorDB(this.getLastYearAndDBName());
    }

    public void creaEstructuraBD(String db_name)
    {
        Statement st = null;
        try{
            st = conexion.createStatement();
        }
        catch( SQLException sqle){
            JOptionPane.showMessageDialog(null,
                    "ERRO antes de crear a estructura da nova BD anual:\n"
                    +sqle);
        }
        catch( NullPointerException npe){
            JOptionPane.showMessageDialog(null,
                    "ERRO antes de crear a estructura da nova BD anual:\n"
                    +npe);
        }
        try{
            st.executeUpdate("USE "+databasename);
        }
        catch( SQLException sqle){
            JOptionPane.showMessageDialog(null,
                    "ERRO cambiando á nova BD anual para crear a súa "
                    + "estructura:\n"
                    +sqle);
        }
        try{
            st.executeUpdate(
                    "CREATE TABLE conductores("
                    + "id_conductor VARCHAR(50) NOT NULL PRIMARY KEY, "
                    + "dni VARCHAR(50), "
                    + "nome VARCHAR(50), "
                    + "apelidos VARCHAR(50), "
                    + "telefono VARCHAR(50), "
                    + "outros_datos VARCHAR(50) "
                    + ") ENGINE=INNODB;"
                    );
        }
        catch( SQLException sqle){
            JOptionPane.showMessageDialog(null,
                    "ERRO creando a estructura da táboa conductores na "
                    + "nova BD anual:\n"
                    +sqle);
        }
        try{
            st.executeUpdate(
                    "CREATE TABLE vehiculos("
                    + "id_matricula VARCHAR(15) NOT NULL PRIMARY KEY, "
                    + "marca VARCHAR(50), "
                    + "modelo VARCHAR(50), "
                    + "tipo VARCHAR(50), "
                    + "descrip VARCHAR(50)"
                    + ") ENGINE=INNODB;"
                    );
        }
        catch( SQLException sqle){
            JOptionPane.showMessageDialog(null,
                    "ERRO creando a estructura da táboa vehículos na "
                    + "nova BD anual:\n"
                    +sqle);
        }
        try{
            st.executeUpdate(
                    "CREATE TABLE control_vehic("
                    + "idcontrol INTEGER NOT NULL AUTO_INCREMENT, "
                    + "fdata DATE NOT NULL, "
                    + "matricula VARCHAR(15) NOT NULL, "
                    + "km_saida INTEGER NOT NULL, "
                    + "km_chegada INTEGER NOT NULL, "
                    + "hora_saida TIME NOT NULL, "
                    + "hora_chegada TIME NOT NULL, "
                    + "gastos_repara VARCHAR(50), "
                    + "combustible_litros SMALLINT NOT NULL, "
                    + "gasto_eur REAL NOT NULL, "
                    + "conductor VARCHAR(50) NOT NULL, "
                    + "PRIMARY KEY(idcontrol) "
                    + ") ENGINE=INNODB;"
                    );
        }
        catch( SQLException sqle){
            JOptionPane.showMessageDialog(null,
                    "ERRO creando a estructura da táboa liñas de control na "
                    + "nova BD anual:\n"
                    +sqle);
        }
        try{
            st.executeUpdate(
                    "CREATE USER 'juanc'@'localhost' IDENTIFIED BY 'taebek_10';"
                    );
        }
        catch( SQLException sqle){
        }
        try{
            st.executeUpdate(
                    "GRANT SELECT,INSERT,UPDATE,DELETE "
                    + "ON "+this.databasename+".* "
                    + "TO 'juanc'@'localhost';"
                    );
        }
        catch( SQLException sqle){
            JOptionPane.showMessageDialog(null,
                    "ERRO dando permisos ao usuario na nova BD anual:\n"
                    +sqle);
        }
    }

    public void importaConductoresAnteriorDB(String anterior_db){
        PruebaMySQL p2 = new PruebaMySQL(anterior_db);
        try{
            p2.estableceConexion();
        }
        catch( interfaceBDException idbe ){
            JOptionPane.showMessageDialog(null,
                        "Problema ó conectar coa antiga B.D: o nome usado"
                        + " non se corresponde con ningunha BD existente "
                        + "(quizais sexa esta a primeira vez que se usa "
                        + "o programa):\n"
                    );
        }
        { // bloque vehículos
            String jid_matriculal="", jmarca="", jmodelo="", jtipo="", 
                    jdescrp="";
            int i = 0;
            ResultSet rs = null;
            try{
                rs = p2.getAllInVehiculos();
                while( rs.next() ){
                    Boolean fail = false;
                    String cadea = rs.getString("id_matricula");
                    try{
                        jid_matriculal = cadea;
                    }
                    catch( Exception e1 ){
                        fail = true;
                    }
                    cadea = rs.getString("marca");
                    try{
                        jmarca = cadea;
                    }
                    catch( Exception e1 ){
                        fail = true;
                    }
                    cadea = rs.getString("modelo");
                    try{
                        jmodelo = cadea;
                    }
                    catch( Exception e1 ){
                        fail = true;
                    }
                    cadea = rs.getString("tipo");
                    try{
                        jtipo = cadea;
                    }
                    catch( Exception e1 ){
                        fail = true;
                    }
                    cadea = rs.getString("descrip");
                    try{
                        jdescrp = cadea;
                    }
                    catch( Exception e1 ){
                        fail = true;
                    }
                    i++;
                    if( fail == true){
                        JOptionPane.showMessageDialog(null,
                            "Problema ó importar un valor de vehículos da "
                            + "BD anterior:\n"
                          );
                    }
                    try{
                        /*
                        JOptionPane.showMessageDialog(null,
                            "Vaise insertar:\n" + jid_matriculal
                            + ", " + jmarca
                            + ", " + jmodelo
                            + ", " + jtipo
                            + ", " + jdescrp);*/
                        this.insertaVehiculo(jid_matriculal, jmarca, jmodelo,
                                jtipo, jdescrp);
                    }
                    catch( SQLException sqle ){
                        JOptionPane.showMessageDialog(null,
                            "Problema ó insertar datos do vehículo con "
                            + "matrícula " + jid_matricula + " na nova BD:\n"
                            + sqle);
                    }
                    catch( Exception xe){
                        JOptionPane.showMessageDialog(null,
                            "Problema non previsto ó insertar datos do "
                            + "vehículo con matrícula " + jid_matricula 
                            + " na nova BD:\n"
                            + xe);
                    }
                }// while
            }  // try 1
            catch( Exception e2 ){
                JOptionPane.showMessageDialog(null,
                    "Problema ó solicitar datos de vehículos ó interface "
                    + "coa Base de Datos:\n"
                    + e2
                    );
            } // catch e2
            JOptionPane.showMessageDialog(null,"Importados " + i + " vehículos "
                    + "da base de datos do ano anterior");
        } // bloque vehículos
        { // bloque conductores
            String jid_conductor = "", jdni = "", jnome = "", japelidos = "",
                    jtelefono = "", joutros_datos = "";
            int i = 0;
            ResultSet rs2 = null;
            try{
                rs2 = p2.getAllInConductores();
                while( rs2.next() ){
                    Boolean fail = false;
                    String cadea = rs2.getString("id_conductor");
                    try{
                        jid_conductor = cadea;
                    }
                    catch( Exception e1 ){
                        fail = true;
                    }
                    cadea = rs2.getString("dni");
                    try{
                        jdni = cadea;
                    }
                    catch( Exception e1 ){
                        fail = true;
                    }
                    cadea = rs2.getString("nome");
                    try{
                        jnome = cadea;
                    }
                    catch( Exception e1 ){
                        fail = true;
                    }
                    cadea = rs2.getString("apelidos");
                    try{
                        japelidos = cadea;
                    }
                    catch( Exception e1 ){
                        fail = true;
                    }
                    cadea = rs2.getString("telefono");
                    try{
                        jtelefono = cadea;
                    }
                    catch( Exception e1 ){
                        fail = true;
                    }
                    cadea = rs2.getString("outros_datos");
                    try{
                        joutros_datos = cadea;

                    }
                    catch( Exception e1 ){
                        fail = true;
                    }
                    i++;
                    /*
                    JOptionPane.showMessageDialog(null,
                        "Lido:\n" + jid_conductor
                        + ", " + jdni
                        + ", " + jnome
                        + ", " + japelidos
                        + ", " + jtelefono
                        + ", " + joutros_datos);*/
                    if( fail == true){
                        JOptionPane.showMessageDialog(null,
                            "Problema ó importar un valor de conductores da "
                            + "BD anterior:\n"
                          );
                    }
                    try{
                        this.insertaConductor(jid_conductor, jdni, jnome,
                                japelidos, jtelefono, joutros_datos);
                    }
                    catch( SQLException sqle ){
                        JOptionPane.showMessageDialog(null,
                            "Problema ó insertar datos do conductor con "
                            + "identificador " + jid_conductor + " na nova BD:\n"
                            + sqle);
                    }
                    catch( Exception xe){
                        JOptionPane.showMessageDialog(null,
                            "Problema non previsto ó insertar datos do "
                            + "conductor con ID " + jid_conductor
                            + " na nova BD:\n"
                            + xe);
                    }
                }// while
            }  // try 1
            catch( Exception e2 ){
                JOptionPane.showMessageDialog(null,
                    "Problema ó solicitar datos de conductores ó interface "
                    + "coa Base de Datos:\n"
                    + e2
                    );
            } // catch e2
            JOptionPane.showMessageDialog(null,"Importados " + i + " "
                    + "conductores da base de datos do ano anterior");
        } // bloque conductores
    } // importaConductoresAnteriorDB
    
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
                        + "SUM(gastos_repara)"
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
        try{
            estableceConexion();
        }
        catch( interfaceBDException idbe ){
            JOptionPane.showMessageDialog(null,
                        "Problema ó conectar coa B.D: o nome usado"
                        + " non se corresponde con ningunha BD existente:\n"
                    );
        }
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
    public void insertaVehiculo() throws SQLException
    {
        String jid_matriculal, jmarca, jmodelo, jtipo, jdescrp;
        int lineas_modi=0;
        PreparedStatement consulta;

        jid_matriculal = JOptionPane.showInputDialog(null, "Matricula");
        jmarca = JOptionPane.showInputDialog(null, "Marca (opcional)");
        jmodelo = JOptionPane.showInputDialog(null, "Modelo (opcional)");
        jtipo = JOptionPane.showInputDialog(null, "Tipo (opcional)");
        jdescrp = JOptionPane.showInputDialog(null, "Breve descripción"
                + " (opcional)");

        //estableceConexion();
        consulta =  conexion.prepareStatement(
                "INSERT INTO vehiculos "
                + "(id_matricula, marca, modelo, tipo, descrip) "
                + "VALUES "
                + "(?, ?, ?, ?, ?)"
                );

        try
        {
            consulta.setString(1, jid_matriculal);
            consulta.setString(2, jmarca);
            consulta.setString(3, jmodelo);
            consulta.setString(4, jtipo);
            consulta.setString(5, jdescrp);
            lineas_modi = consulta.executeUpdate();
            JOptionPane.showMessageDialog(null, lineas_modi
                    + " rexistros agregados");
        }catch (Exception e)
        {
            JOptionPane.showMessageDialog(null,
                        "Problema ó actualizar a base de datos (vehículos). "
                        + "Datos NON gardados"
                    );
        }

    }

    public void insertaVehiculo(
            String jid_matriculal,
            String jmarca,
            String jmodelo,
            String jtipo,
            String jdescrp) throws SQLException
    {

        int lineas_modi=0;
        PreparedStatement consulta;
        /*
        JOptionPane.showMessageDialog(null,
            "Recibido:\n" + jid_matriculal
            + ", " + jmarca
            + ", " + jmodelo
            + ", " + jtipo
            + ", " + jdescrp);*/
        //estableceConexion();
        consulta =  conexion.prepareStatement(
                "INSERT INTO vehiculos "
                + "(id_matricula, marca, modelo, tipo, descrip) "
                + "VALUES "
                + "(?, ?, ?, ?, ?)"
                );

        try
        {
            consulta.setString(1, jid_matriculal);
            consulta.setString(2, jmarca);
            consulta.setString(3, jmodelo);
            consulta.setString(4, jtipo);
            consulta.setString(5, jdescrp);
            lineas_modi = consulta.executeUpdate();
        }catch (Exception e)
        {
            JOptionPane.showMessageDialog(null,
                        "Problema ó actualizar a base de datos (vehículos). "
                        + "Datos NON gardados\n"
                        + e
                    );
        }

    }

    public void insertaConductor() throws SQLException
    {
        String jid_conductor, jdni, jnome, japelidos, jtelefono, joutros_datos;
        int lineas_modi=0;
        PreparedStatement consulta;

        jid_conductor = JOptionPane.showInputDialog(null,
                "Identificador de conductor "
                + "(obrigatorio e debe ser único, mostrarase no formulario)");
        jdni = JOptionPane.showInputDialog(null, "DNI (opcional)");
        jnome = JOptionPane.showInputDialog(null, "Nome (opcional)");
        japelidos = JOptionPane.showInputDialog(null, "Apelido (opcional)");
        jtelefono = JOptionPane.showInputDialog(null, "Teléfono (opcional)");
        joutros_datos = JOptionPane.showInputDialog(null,
                                                    "Outros datos (opcional)");
        //estableceConexion();
        consulta =  conexion.prepareStatement(
                "INSERT INTO conductores "
                + "(id_conductor, dni, nome, apelidos, telefono, outros_datos) "
                + "VALUES"
                + " (?, ?, ?, ?, ?, ?)"
                );

        try
        {
            consulta.setString(1, jid_conductor);
            consulta.setString(2, jdni);
            consulta.setString(3, jnome);
            consulta.setString(4, japelidos);
            consulta.setString(5, jtelefono);
            consulta.setString(6, joutros_datos);
            lineas_modi = consulta.executeUpdate();
            JOptionPane.showMessageDialog(null, lineas_modi
                    + " rexistros agregados");
        }catch (Exception e)
        {
            JOptionPane.showMessageDialog(null,
                        "Problema ó actualizar a base de datos (conductores). "
                        + "Datos NON gardados"
                    );
        }

    }

    public void insertaConductor(
            String jid_conductor,
            String jdni,
            String jnome,
            String japelidos,
            String jtelefono,
            String joutros_datos) throws SQLException
    {

        int lineas_modi=0;
        PreparedStatement consulta;
        /*
        JOptionPane.showMessageDialog(null,
            "Recibido:\n" + jid_conductor
            + ", " + jdni
            + ", " + jnome
            + ", " + japelidos
            + ", " + jtelefono
            + ", " + joutros_datos);*/
        //estableceConexion();
        consulta =  conexion.prepareStatement(
                "INSERT INTO conductores "
                + "(id_conductor, dni, nome, apelidos, telefono, outros_datos) "
                + "VALUES"
                + " (?, ?, ?, ?, ?, ?)"
                );

        try
        {
            consulta.setString(1, jid_conductor);
            consulta.setString(2, jdni);
            consulta.setString(3, jnome);
            consulta.setString(4, japelidos);
            consulta.setString(5, jtelefono);
            consulta.setString(6, joutros_datos);
            lineas_modi = consulta.executeUpdate();
            
        }catch (Exception e)
        {
            JOptionPane.showMessageDialog(null,
                        "Problema ó actualizar a base de datos (conductores). "
                        + "Datos NON gardados"
                    );
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
                        "Problema para pechar a conexión coa B.D.\n"
                        + "Este problema non debería afectar negativamente "
                        + "no funcionamento do programa, pero recoméndase que "
                        + "revise nos informes que todo funciona correctamente."
                    );
        }
    }
    
    private String getLastYearAndDBName(){
        Calendar cal = Calendar.getInstance();
        int ano_pasado = cal.get(cal.YEAR)-1;
        String last_db_name = "contrlvehicdb_" + ano_pasado;
        //JOptionPane.showMessageDialog(null,"vou devolver " + last_db_name);
        return last_db_name;
    }
}

class interfaceBDException extends Exception{
    String dbname;
    int error_code;
    interfaceBDException(String db_name, int sql_excep){
        dbname = db_name;
        error_code = sql_excep;
    }
    public String toString(){
        if(this.error_code == 0){
            return Integer.toString(error_code)
                    + ": interfaceBDException "
                    + "[SERVIDOR APAGADO]";
        } else if(this.error_code == 1049){
            return Integer.toString(error_code)
                    + ": interfaceBDException "
                    + "[base de datos " + dbname + " NON EXISTE]\n";
        } else if(this.error_code == 1044){
            return Integer.toString(error_code)
                    + ": interfaceBDException "
                    + "[CONTRASINAL DE USUARIO INCORRECTA] Cambiouna alguén?\n";
        }
        return Integer.toString(error_code)
                    + ": interfaceBDException "
                    + "[ERRO NON PREVISTO]";
    }


}
