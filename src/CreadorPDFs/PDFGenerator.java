/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package CreadorPDFs;

import java.io.FileOutputStream;
import java.io.IOException;

import java.sql.*;
import javax.swing.JOptionPane;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
/**
 *
 * @author breo
 */

public class PDFGenerator {
    String RESULT;
    String titulo = "-";
    String matric = "-";
    Short mes;
    int tipo_cons;
    

    public void setMes(Short imes){
        mes = imes;
    }

    public void setMatricula(String matricula){
        matric = matricula;
    }

    public void setFilename(String filename){
        RESULT = filename;
    }

    public void setTitulo(String titulo_informe){
        titulo = titulo_informe;
    }

    public void createPdf(
            String filename,
            String databasename,
            int tipo_consulta
        )
        throws DocumentException, IOException {
        this.tipo_cons = tipo_consulta;
        // step 1
        Document document = new Document(PageSize.A4.rotate());
        // step 2
        PdfWriter.getInstance(document, new FileOutputStream(filename+".pdf"));
        // step 3
        document.open();
        // step 4
        try{
            if(tipo_consulta == TIPO_CONSULTA.LINHAS){
                this.setTitulo("LISTA DE REXISTROS DE CONTROL (TODOS NA BASE "
                        + "DE DATOS ACTUAL, DO PRESENTE ANO)");
                document.add(xeraTableLinha(titulo,databasename));
            }
            else if(tipo_consulta == TIPO_CONSULTA.CONDUCTORES){
                this.setTitulo("LISTA DE CONDUCTORES (TODOS NA BASE "
                        + "DE DATOS ACTUAL, DO PRESENTE ANO)");
                document.add(xeraTableCond(titulo,databasename));
            }
            else if(tipo_consulta == TIPO_CONSULTA.VEHICULOS){
                this.setTitulo("LISTA DE VEHÍCULOS (TODOS NA BASE "
                        + "DE DATOS ACTUAL, DO PRESENTE ANO)");
                document.add(xeraTableVehic(titulo,databasename));
            }
            else if(tipo_consulta == TIPO_CONSULTA.LINHAS_MENSUAL){
                this.setTitulo("LISTA DE REXISTROS DE CONTROL PARA O MES "
                            + Short.toString(mes) + " (TODOS NA BASE "
                            + "DE DATOS ACTUAL, DO PRESENTE ANO)"
                            );
                document.add(xeraTableLinha(titulo,databasename));
                //document.add(xeraTableLinhaMensual(titulo,databasename,mes));
            }
            else if(tipo_consulta == TIPO_CONSULTA.CONSUMO_AOS_100)
            {
                this.setTitulo("CONSUMO AOS 100 MEDIO (ACTUAL ANO)");
                document.add(xeraGastoAos100MedioCombus(titulo,databasename));
            }
            else if(tipo_consulta == TIPO_CONSULTA.CONSUMO_AOS_100_MENSUAL){
                this.setTitulo("CONSUMO AOS 100 MEDIO MENSUAL");
                document.add(xeraGastoAos100MedioCombus(this.titulo,
                        databasename));
            }
            else if(tipo_consulta == TIPO_CONSULTA.GASTO_REPARA_MEDIO){
                this.setTitulo("GASTO REPARACIÓN MEDIO MENSUAL (TODOS NA BASE "
                            + "DE DATOS ACTUAL, DO PRESENTE ANO)");
                document.add(xeraGastoReparaMedio(this.titulo,databasename));
            }
            else if(tipo_consulta == TIPO_CONSULTA.GASTO_REPARA_MEDIO_MENSUAL)
            {
                this.setTitulo("GASTO TOTAL MEDIO MENSUAL");
                document.add(xeraGastoReparaMedio(this.titulo,databasename));
            }
            else if(tipo_consulta == TIPO_CONSULTA.ESTATÍSTICA_VEHICULO_MENSUAL)
            {
                this.setTitulo("LIÑAS DE CONTROL ALMACENADAS PARA"
                        + " O VEHÍCULO DE MATRÍCULA: "
                        +this.matric);
                document.add(xeraLinEstatisticaV(this.titulo,databasename));

                //Element e = "";
                //document.add((Element)"\n");
                String linha_en_branco = " ";
                Paragraph paragr = new Paragraph (new Phrase(linha_en_branco));
                document.add(paragr);

                this.setTitulo("\nRESUMO DE RESTATÍSTICAS PARA"
                        + " O VEHÍCULO DE MATRÍCULA: "
                        +this.matric);
                document.add(xeraTabEstatisticaV(this.titulo,databasename));
            }

        }
        catch( SQLException sqle){
            JOptionPane.showMessageDialog(null,
                    "Problema ó ler contido da base de datos. "
                    + sqle
                    );
        }
        catch( DocumentException de){
            JOptionPane.showMessageDialog(null,
                    "Problema ó procesar documento PDF. "
                    + de
                    );
        }
        catch( IOException ioe){
            JOptionPane.showMessageDialog(null,
                    "Problema ó gardar no disco. Comprobe non está cheo. "
                    + ioe
                    );
        }
        
        // step 5
        document.close();

    } // createPdf

    public PdfPTable xeraTableLinha(
            String titulo,
            String databasename
            )
        throws 
            SQLException,
            DocumentException,
            IOException 
    {
        PruebaMySQL mysql_i = new PruebaMySQL(databasename);
        ResultSet rs = null;
        String cadena = "";
        int numLinhas = 0;

    	// create a database connection
        mysql_i.estableceConexion();
        if( tipo_cons == TIPO_CONSULTA.LINHAS ){
            rs = mysql_i.getLinhas();
        } else{
            rs = mysql_i.getLinhasMensual(mes);
        }
        

    	// Create a table with ? columns
        PdfPTable table = new PdfPTable(11); //PdfPTable table = new PdfPTable(11);
        table.setWidthPercentage(100f);
        table.getDefaultCell().setUseAscender(true);
        table.getDefaultCell().setUseDescender(true);
        // Add the first header row
        Font f = new Font();
        f.setColor(BaseColor.WHITE);
        PdfPCell cell = new PdfPCell(new Phrase(titulo));
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setColspan(11); // cell.setColspan(11);
        table.addCell(cell);
        // Add the second header row twice
        if( tipo_cons == TIPO_CONSULTA.LINHAS ){
            table.getDefaultCell().setBackgroundColor(BaseColor.YELLOW);
        } else{
            table.getDefaultCell().setBackgroundColor(BaseColor.GREEN);
        }
        for (int i = 0; i < 2; i++) {
            //table.addCell("Nº");
            table.addCell("Data (fecha)");
            table.addCell("Matrícula");
            table.addCell("Km. saída");
            table.addCell("Km. chegada");
            table.addCell("Km. percorridos");
            table.addCell("Hora saída");
            table.addCell("Hora chegada");
            table.addCell("Gastos reparación");
            table.addCell("Combustible");
            table.addCell("Gastos combustible");
            table.addCell("Conductor");
        }
        table.getDefaultCell().setBackgroundColor(null);
        // There are three special rows
        table.setHeaderRows(3);
        // One of them is a footer
        table.setFooterRows(1);
        // Now let's loop over the screenings
        try{
            while (rs.next())
            {
                //table.addCell(Integer.toString(++numLinhas));
                table.addCell(rs.getDate("fdata").toString());
                table.addCell(rs.getString(3));
                table.addCell(Integer.toString(rs.getInt(4)));
                table.addCell(Integer.toString(rs.getInt(5)));
                table.addCell(
                            (Integer.toString(rs.getInt(5) - rs.getInt(4)))
                        );
                table.addCell(rs.getTime(6).toString());
                table.addCell(rs.getTime(7).toString());
                table.addCell(Double.toString(rs.getDouble(8))+" €");
                table.addCell(Short.toString(rs.getShort(9))+" l.");
                table.addCell(Double.toString(rs.getDouble(10))+" €");
                table.addCell(rs.getString(11));

            }
            System.out.println(cadena);

        } catch(Exception e)
        {
            JOptionPane.showMessageDialog(null,
                    "Problema no xerador de PDFs co listado de rexistros: "+e);
        }

        mysql_i.pechaConexion();

        return table;
    } // getTableLinha


    public PdfPTable xeraLinEstatisticaV(
            String titulo,
            String databasename
            )
        throws
            SQLException,
            DocumentException,
            IOException
    {
        PruebaMySQL mysql_i = new PruebaMySQL(databasename);
        ResultSet rs = null;
        String cadena = "";
        int numLinhas = 0;

    	// create a database connection
        mysql_i.estableceConexion();
        if( tipo_cons == TIPO_CONSULTA.ESTATÍSTICA_VEHICULO_MENSUAL ){
            rs = mysql_i.getLinhasByVandMounth(mes,matric);
        } else{
            //rs = mysql_i.getLinhasMensual(mes);
        }


    	// Create a table with ? columns
        PdfPTable table = new PdfPTable(11); //PdfPTable table = new PdfPTable(11);
        table.setWidthPercentage(100f);
        table.getDefaultCell().setUseAscender(true);
        table.getDefaultCell().setUseDescender(true);
        // Add the first header row
        Font f = new Font();
        f.setColor(BaseColor.WHITE);
        PdfPCell cell = new PdfPCell(new Phrase(titulo));
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setColspan(11); // cell.setColspan(11);
        table.addCell(cell);
        // Add the second header row twice
        if( tipo_cons == TIPO_CONSULTA.ESTATÍSTICA_VEHICULO_MENSUAL ){
            table.getDefaultCell().setBackgroundColor(BaseColor.YELLOW);
        } else{
            table.getDefaultCell().setBackgroundColor(BaseColor.GREEN);
        }
        for (int i = 0; i < 2; i++) {
            //table.addCell("Nº");
            table.addCell("Data (fecha)");
            table.addCell("Matrícula");
            table.addCell("Km. saída");
            table.addCell("Km. chegada");
            table.addCell("Km. percorridos");
            table.addCell("Hora saída");
            table.addCell("Hora chegada");
            table.addCell("Gastos reparación");
            table.addCell("Combustible");
            table.addCell("Gastos combustible");
            table.addCell("Conductor");
        }
        table.getDefaultCell().setBackgroundColor(null);
        // There are three special rows
        table.setHeaderRows(3);
        // One of them is a footer
        table.setFooterRows(1);
        // Now let's loop over the screenings
        try{
            while (rs.next())
            {
                //table.addCell(Integer.toString(++numLinhas));
                table.addCell(rs.getDate("fdata").toString());
                table.addCell(rs.getString(3));
                table.addCell(Integer.toString(rs.getInt(4)));
                table.addCell(Integer.toString(rs.getInt(5)));
                table.addCell(
                            (Integer.toString(rs.getInt(5) - rs.getInt(4)))
                        );
                table.addCell(rs.getTime(6).toString());
                table.addCell(rs.getTime(7).toString());
                table.addCell(Double.toString(rs.getDouble(8))+" €");
                table.addCell(Short.toString(rs.getShort(9))+" l.");
                table.addCell(Double.toString(rs.getDouble(10))+" €");
                table.addCell(rs.getString(11));

            }
            System.out.println(cadena);

        } catch(Exception e)
        {
            JOptionPane.showMessageDialog(null,
                    "Problema no xerador de PDFs co listado de rexistros: "+e);
        }

        mysql_i.pechaConexion();

        return table;
    } // xeraLinEstatisticaV


    public PdfPTable xeraTabEstatisticaV(
            String titulo,
            String databasename
            )
        throws
            SQLException,
            DocumentException,
            IOException
    {
        PruebaMySQL mysql_i = new PruebaMySQL(databasename);
        ResultSet rs = null;
        String cadena = "";
       //int numLinhas = 0;

    	// create a database connection
        mysql_i.estableceConexion();
        rs = mysql_i.getEstatistVByMounth(mes,this.matric);
        

    	// Create a table with 5 columns
        PdfPTable table = new PdfPTable(6);
        table.setWidthPercentage(100f);
        table.getDefaultCell().setUseAscender(true);
        table.getDefaultCell().setUseDescender(true);
        // Add the first header row
        Font f = new Font();
        f.setColor(BaseColor.WHITE);
        PdfPCell cell = new PdfPCell(new Phrase(titulo));
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setColspan(7);
        table.addCell(cell);
        // Add the second header row twice
        if( tipo_cons == TIPO_CONSULTA.CONSUMO_AOS_100 ){
            table.getDefaultCell().setBackgroundColor(BaseColor.CYAN);
        } else{
            table.getDefaultCell().setBackgroundColor(BaseColor.ORANGE);
        }
        for (int i = 0; i < 2; i++) {
            table.addCell("Matrícula");
            table.addCell("Data para a que se calculou");
            table.addCell("Km. percorridos");
            table.addCell("Litros recargados");
            table.addCell("Consumo aos 100 (aproximadamente)");
            table.addCell("Gastos reparación totales");
        }
        table.getDefaultCell().setBackgroundColor(null);
        // There are three special rows
        table.setHeaderRows(3);
        // One of them is a footer
        table.setFooterRows(1);
        // Now let's loop over the screenings
        try{
            while (rs.next())
            {
                table.addCell(rs.getString("matricula"));
                table.addCell(rs.getString(2));
                table.addCell(rs.getString(3));
                table.addCell(rs.getString(4));
                table.addCell(rs.getString(5));
                table.addCell(rs.getString(6));

            }

        } catch(Exception e)
        {
            JOptionPane.showMessageDialog(null,
                    "Problema no xerador de PDFs cos documentos de "
                    + "consumos aos 100: "+e);
        }

        mysql_i.pechaConexion();

        return table;
    } // xeraTabEstatisticaV


    public PdfPTable xeraTableVehic(
            String titulo,
            String databasename
            )
        throws
            SQLException,
            DocumentException,
            IOException
    {
        PruebaMySQL mysql_i = new PruebaMySQL(databasename);
        ResultSet rs = null;
        String cadena = "";
        int numLinhas = 0;

    	// create a database connection
        mysql_i.estableceConexion();
        rs = mysql_i.getAllInVehiculos();

    	// Create a table with 7 columns
        PdfPTable table = new PdfPTable(6);
        table.setWidthPercentage(100f);
        table.getDefaultCell().setUseAscender(true);
        table.getDefaultCell().setUseDescender(true);
        // Add the first header row
        Font f = new Font();
        f.setColor(BaseColor.WHITE);
        PdfPCell cell = new PdfPCell(new Phrase(titulo));
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setColspan(6);
        table.addCell(cell);
        // Add the second header row twice
        table.getDefaultCell().setBackgroundColor(BaseColor.BLUE);
        for (int i = 0; i < 2; i++) {
            table.addCell("Nº");
            table.addCell("Matrícula");
            table.addCell("Marca");
            table.addCell("Modelo");
            table.addCell("Tipo");
            table.addCell("Descripción adicional");
        }
        table.getDefaultCell().setBackgroundColor(null);
        // There are three special rows
        table.setHeaderRows(3);
        // One of them is a footer
        table.setFooterRows(1);
        // Now let's loop over the screenings
        try{
            while (rs.next())
            {
                table.addCell(Integer.toString(++numLinhas));
                table.addCell(rs.getString("id_matricula"));
                table.addCell(rs.getString(2));
                table.addCell(rs.getString(3));
                table.addCell(rs.getString(4));
                table.addCell(rs.getString(5));

            }

        } catch(Exception e)
        {
            JOptionPane.showMessageDialog(null,
                    "Problema no xerador de PDFs co listado de vehículos: "+e);
        }

        mysql_i.pechaConexion();

        return table;
    } // getTableVehic


    public PdfPTable xeraTableCond(
            String titulo,
            String databasename
            )
        throws
            SQLException,
            DocumentException,
            IOException
    {
        PruebaMySQL mysql_i = new PruebaMySQL(databasename);
        ResultSet rs = null;
        String cadena = "";
        int numLinhas = 0;

    	// create a database connection
        mysql_i.estableceConexion();
        rs = mysql_i.getAllInConductores();

    	// Create a table with 7 columns
        PdfPTable table = new PdfPTable(7);
        table.setWidthPercentage(100f);
        table.getDefaultCell().setUseAscender(true);
        table.getDefaultCell().setUseDescender(true);
        // Add the first header row
        Font f = new Font();
        f.setColor(BaseColor.WHITE);
        PdfPCell cell = new PdfPCell(new Phrase(titulo));
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setColspan(7);
        table.addCell(cell);
        // Add the second header row twice
        table.getDefaultCell().setBackgroundColor(BaseColor.RED);
        for (int i = 0; i < 2; i++) {
            table.addCell("Nº");
            table.addCell("Identificador de Conductor");
            table.addCell("DNI");
            table.addCell("Nome");
            table.addCell("Apelidos");
            table.addCell("Teléfono");
            table.addCell("Descripción adicional");
        }
        table.getDefaultCell().setBackgroundColor(null);
        // There are three special rows
        table.setHeaderRows(3);
        // One of them is a footer
        table.setFooterRows(1);
        // Now let's loop over the screenings
        try{
            while (rs.next())
            {
                table.addCell(Integer.toString(++numLinhas));
                table.addCell(rs.getString("id_conductor"));
                table.addCell(rs.getString(2));
                table.addCell(rs.getString(3));
                table.addCell(rs.getString(4));
                table.addCell(rs.getString(5));
                table.addCell(rs.getString(6));

            }

        } catch(Exception e)
        {
            JOptionPane.showMessageDialog(null,
                    "Problema no xerador de PDFs co listado de conductores: "+e);
        }

        mysql_i.pechaConexion();

        return table;
    } // getTableCond

    public PdfPTable xeraGastoAos100MedioCombus(
            String titulo,
            String databasename
            )
        throws
            SQLException,
            DocumentException,
            IOException
    {
        PruebaMySQL mysql_i = new PruebaMySQL(databasename);
        ResultSet rs = null;
        String cadena = "";
       //int numLinhas = 0;

    	// create a database connection
        mysql_i.estableceConexion();
        if( tipo_cons == TIPO_CONSULTA.CONSUMO_AOS_100 ){
            rs = mysql_i.getGastoCombusAos100Medio();
        } else{
            rs = mysql_i.getGastoCombusAos100MedioMensual(mes);
        }

    	// Create a table with 5 columns
        PdfPTable table = new PdfPTable(6);
        table.setWidthPercentage(100f);
        table.getDefaultCell().setUseAscender(true);
        table.getDefaultCell().setUseDescender(true);
        // Add the first header row
        Font f = new Font();
        f.setColor(BaseColor.WHITE);
        PdfPCell cell = new PdfPCell(new Phrase(titulo));
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setColspan(7);
        table.addCell(cell);
        // Add the second header row twice
        if( tipo_cons == TIPO_CONSULTA.CONSUMO_AOS_100 ){
            table.getDefaultCell().setBackgroundColor(BaseColor.CYAN);
        } else{
            table.getDefaultCell().setBackgroundColor(BaseColor.ORANGE);
        }
        for (int i = 0; i < 2; i++) {
            table.addCell("Matrícula");
            table.addCell("Data para a que se calculou");
            table.addCell("Km. percorridos");
            table.addCell("Litros recargados");
            table.addCell("Coste total combustible");
            table.addCell("Consumo medio aos 100 (aproximadamente)");
        }
        table.getDefaultCell().setBackgroundColor(null);
        // There are three special rows
        table.setHeaderRows(3);
        // One of them is a footer
        table.setFooterRows(1);
        // Now let's loop over the screenings
        try{
            while (rs.next())
            {
                table.addCell(rs.getString("matricula"));
                table.addCell(rs.getString(2));
                table.addCell(rs.getString(3));
                table.addCell(rs.getString(4) + " l.");
                table.addCell(rs.getString(5)+ " €");
                table.addCell(rs.getString(6) + " l./100 km.");

            }

        } catch(Exception e)
        {
            JOptionPane.showMessageDialog(null,
                    "Problema no xerador de PDFs cos documentos de "
                    + "consumos aos 100: "+e);
        }

        mysql_i.pechaConexion();

        return table;

    } // xeraGastoAos100MedioCombus


    public PdfPTable xeraGastoReparaMedio(
            String titulo,
            String databasename
            )
        throws
            SQLException,
            DocumentException,
            IOException
    {
        PruebaMySQL mysql_i = new PruebaMySQL(databasename);
        ResultSet rs = null;
        String cadena = "";
       //int numLinhas = 0;

    	// create a database connection
        mysql_i.estableceConexion();
        if( tipo_cons == TIPO_CONSULTA.GASTO_REPARA_MEDIO ){
            rs = mysql_i.getGastoMedioRepara();
        } else{
            rs = mysql_i.getGastoMedioReparaMensual(mes);
        }

    	// Create a table with 5 columns
        PdfPTable table = new PdfPTable(4);
        table.setWidthPercentage(100f);
        table.getDefaultCell().setUseAscender(true);
        table.getDefaultCell().setUseDescender(true);
        // Add the first header row
        Font f = new Font();
        f.setColor(BaseColor.WHITE);
        PdfPCell cell = new PdfPCell(new Phrase(titulo));
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setColspan(7);
        table.addCell(cell);
        // Add the second header row twice
        if( tipo_cons == TIPO_CONSULTA.GASTO_REPARA_MEDIO ){
            table.getDefaultCell().setBackgroundColor(BaseColor.CYAN);
        } else{
            table.getDefaultCell().setBackgroundColor(BaseColor.ORANGE);
        }
        for (int i = 0; i < 2; i++) {
            table.addCell("Matrícula");
            table.addCell("Data para a que se calculou");
            table.addCell("Gastos de reparación medios (por visita a taller)");
            table.addCell("Gastos acumulados (sumatorio)");
        }
        table.getDefaultCell().setBackgroundColor(null);
        // There are three special rows
        table.setHeaderRows(3);
        // One of them is a footer
        table.setFooterRows(1);
        // Now let's loop over the screenings
        try{
            while (rs.next())
            {
                table.addCell(rs.getString("matricula"));
                table.addCell(rs.getString(2));
                table.addCell(rs.getString(3)+" € (medios por reparación)");
                table.addCell(rs.getString(4)+" € (acumulados en total)");

            }

        } catch(Exception e)
        {
            JOptionPane.showMessageDialog(null,
                    "Problema no xerador de PDFs cos documentos de "
                    + "consumos aos 100: "+e);
        }

        mysql_i.pechaConexion();

        return table;

    } // xeraGastoReparaMedio

}
