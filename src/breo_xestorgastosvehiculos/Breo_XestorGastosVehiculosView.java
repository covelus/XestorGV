/*
 * Breo_XestorGastosVehiculosView.java
 */

package breo_xestorgastosvehiculos;

import org.jdesktop.application.Action;
import org.jdesktop.application.ResourceMap;
import org.jdesktop.application.SingleFrameApplication;
import org.jdesktop.application.FrameView;
import org.jdesktop.application.TaskMonitor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;
import javax.swing.Icon;
import javax.swing.JDialog;
import javax.swing.JFrame;

import java.util.Calendar;
import javax.swing.JOptionPane;
import java.sql.*;

import java.awt.Desktop;
import java.io.File;

import java.io.IOException;
import com.itextpdf.text.DocumentException;

import CreadorPDFs.*;

/**
 * The application's main frame.
 */
public class Breo_XestorGastosVehiculosView extends FrameView {
   

    public Breo_XestorGastosVehiculosView(SingleFrameApplication app) {
        super(app);

        initComponents();
        this.startDatabaseServer();
        setCurrentYearAndDBName();
        this.mysql_i = new PruebaMySQL(this.database_name);
        try{
            this.mysql_i.estableceConexion();
        }
        catch( interfaceBDException idbe ){
            if( idbe.toString().startsWith("1049")){
                this.mysql_i = new PruebaMySQL("test");
                try{
                    this.mysql_i.estableceConexion();
                }
                catch( interfaceBDException idbe2 ){
                    JOptionPane.showMessageDialog(null,
                            "Imposible crear nova BD. Erro indeterminado:\n"
                            +idbe2
                            );
                }
                JOptionPane.showMessageDialog(null, idbe.toString()
                    + "... procederase a creala. ENTRAMOS EN ANO NOVO? "
                    + "(Se é así, feliz ano :P )\n"
                    + "\nSe non estamos en ano novo, revise a data do"
                    + " ordenador, pois posiblemente estea desaxustada: \n"
                    + "- faga click co botón dereito enriba do reloxo da"
                    + " barra de tarefas e seleccione a opción similar a"
                    + " \"axustar data e hora\" (esta aparecerá no idioma que"
                    + " \nteña configurado o ordenador e pode cambiar"
                    + " dunhas versións do sistema a outras)."
                    + "\n\nSe data e hora se perden cada vez que "
                    + "reinicia o ordenador, "
                    + "é moi posible que un técnico teña que cambiar a pila "
                    + "da placa base, pois posiblemente esta estea esgotada."
                    );
                this.mysql_i.creaBD(this.database_name);
            } else{
                JOptionPane.showMessageDialog(null, 
                        "ERRO NO INTERFACE Á BASE DE DATOS\n"
                        + idbe.toString());
            }
        }
        catch( Exception e ){
            JOptionPane.showMessageDialog(null,
                        "Erro non previsto no interface á base de datos\n"
                        + e);
        }
        this.readDBAndReloadCombos();

        // status bar initialization - message timeout, idle icon and busy animation, etc
        ResourceMap resourceMap = getResourceMap();
        int messageTimeout = resourceMap.getInteger("StatusBar.messageTimeout");
        messageTimer = new Timer(messageTimeout, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                statusMessageLabel.setText("");
            }
        });
        messageTimer.setRepeats(false);
        int busyAnimationRate = resourceMap.getInteger("StatusBar.busyAnimationRate");
        for (int i = 0; i < busyIcons.length; i++) {
            busyIcons[i] = resourceMap.getIcon("StatusBar.busyIcons[" + i + "]");
        }
        busyIconTimer = new Timer(busyAnimationRate, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                busyIconIndex = (busyIconIndex + 1) % busyIcons.length;
                statusAnimationLabel.setIcon(busyIcons[busyIconIndex]);
            }
        });
        idleIcon = resourceMap.getIcon("StatusBar.idleIcon");
        statusAnimationLabel.setIcon(idleIcon);
        progressBar.setVisible(false);

        // connecting action tasks to status bar via TaskMonitor
        TaskMonitor taskMonitor = new TaskMonitor(getApplication().getContext());
        taskMonitor.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                String propertyName = evt.getPropertyName();
                if ("started".equals(propertyName)) {
                    if (!busyIconTimer.isRunning()) {
                        statusAnimationLabel.setIcon(busyIcons[0]);
                        busyIconIndex = 0;
                        busyIconTimer.start();
                    }
                    progressBar.setVisible(true);
                    progressBar.setIndeterminate(true);
                } else if ("done".equals(propertyName)) {
                    busyIconTimer.stop();
                    statusAnimationLabel.setIcon(idleIcon);
                    progressBar.setVisible(false);
                    progressBar.setValue(0);
                } else if ("message".equals(propertyName)) {
                    String text = (String)(evt.getNewValue());
                    statusMessageLabel.setText((text == null) ? "" : text);
                    messageTimer.restart();
                } else if ("progress".equals(propertyName)) {
                    int value = (Integer)(evt.getNewValue());
                    progressBar.setVisible(true);
                    progressBar.setIndeterminate(false);
                    progressBar.setValue(value);
                }
            }
        });
    }

    @Action
    public void showAboutBox() {
        if (aboutBox == null) {
            JFrame mainFrame = Breo_XestorGastosVehiculosApp.getApplication().getMainFrame();
            aboutBox = new Breo_XestorGastosVehiculosAboutBox(mainFrame);
            aboutBox.setLocationRelativeTo(mainFrame);
        }
        Breo_XestorGastosVehiculosApp.getApplication().show(aboutBox);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mainPanel = new javax.swing.JPanel();
        jPanelVersiónDB = new javax.swing.JPanel();
        jLabelDBAnoTag = new javax.swing.JLabel();
        jLabelDBAno = new javax.swing.JLabel();
        jButtonCambiarVBD = new javax.swing.JButton();
        jLabelAVISO = new javax.swing.JLabel();
        jPanelXestión = new javax.swing.JPanel();
        jButtonNovosRexControl = new javax.swing.JButton();
        jButtonNovosConduct = new javax.swing.JButton();
        jButtonNovosVehic = new javax.swing.JButton();
        jPanelEstatísticas = new javax.swing.JPanel();
        jButtonListRControl = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jButtonListVehic = new javax.swing.JButton();
        jButtonListConduct = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jButtonGastosConsAnuais = new javax.swing.JButton();
        jComboBoxConsumoMes = new javax.swing.JComboBox();
        jLabel3 = new javax.swing.JLabel();
        jButtonGastosRepAnuais = new javax.swing.JButton();
        jComboBoxGastosRepMes = new javax.swing.JComboBox();
        jComboBoxMatriculas = new javax.swing.JComboBox();
        jLabel4 = new javax.swing.JLabel();
        jButtonClose = new javax.swing.JButton();
        menuBar = new javax.swing.JMenuBar();
        javax.swing.JMenu fileMenu = new javax.swing.JMenu();
        javax.swing.JMenuItem exitMenuItem = new javax.swing.JMenuItem();
        javax.swing.JMenu helpMenu = new javax.swing.JMenu();
        javax.swing.JMenuItem aboutMenuItem = new javax.swing.JMenuItem();
        jMenuItem1 = new javax.swing.JMenuItem();
        statusPanel = new javax.swing.JPanel();
        javax.swing.JSeparator statusPanelSeparator = new javax.swing.JSeparator();
        statusMessageLabel = new javax.swing.JLabel();
        statusAnimationLabel = new javax.swing.JLabel();
        progressBar = new javax.swing.JProgressBar();

        mainPanel.setName("mainPanel"); // NOI18N

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(breo_xestorgastosvehiculos.Breo_XestorGastosVehiculosApp.class).getContext().getResourceMap(Breo_XestorGastosVehiculosView.class);
        jPanelVersiónDB.setBorder(javax.swing.BorderFactory.createTitledBorder(resourceMap.getString("jPanelVersiónDB.border.title"))); // NOI18N
        jPanelVersiónDB.setName("jPanelVersiónDB"); // NOI18N

        jLabelDBAnoTag.setText(resourceMap.getString("jLabelDBAnoTag.text")); // NOI18N
        jLabelDBAnoTag.setName("jLabelDBAnoTag"); // NOI18N

        jLabelDBAno.setFont(resourceMap.getFont("jLabelDBAno.font")); // NOI18N
        jLabelDBAno.setForeground(resourceMap.getColor("jLabelDBAno.foreground")); // NOI18N
        jLabelDBAno.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabelDBAno.setText(resourceMap.getString("jLabelDBAno.text")); // NOI18N
        jLabelDBAno.setName("jLabelDBAno"); // NOI18N

        jButtonCambiarVBD.setText(resourceMap.getString("jButtonCambiarVBD.text")); // NOI18N
        jButtonCambiarVBD.setName("jButtonCambiarVBD"); // NOI18N
        jButtonCambiarVBD.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButtonCambiarVBDMouseClicked(evt);
            }
        });

        jLabelAVISO.setForeground(resourceMap.getColor("jLabelAVISO.foreground")); // NOI18N
        jLabelAVISO.setText(resourceMap.getString("jLabelAVISO.text")); // NOI18N
        jLabelAVISO.setName("jLabelAVISO"); // NOI18N

        javax.swing.GroupLayout jPanelVersiónDBLayout = new javax.swing.GroupLayout(jPanelVersiónDB);
        jPanelVersiónDB.setLayout(jPanelVersiónDBLayout);
        jPanelVersiónDBLayout.setHorizontalGroup(
            jPanelVersiónDBLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelVersiónDBLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabelDBAnoTag, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabelDBAno, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(58, 58, 58)
                .addComponent(jButtonCambiarVBD)
                .addGap(132, 132, 132)
                .addComponent(jLabelAVISO, javax.swing.GroupLayout.PREFERRED_SIZE, 228, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(38, Short.MAX_VALUE))
        );
        jPanelVersiónDBLayout.setVerticalGroup(
            jPanelVersiónDBLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelVersiónDBLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelVersiónDBLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabelAVISO, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 23, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanelVersiónDBLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabelDBAnoTag)
                        .addComponent(jButtonCambiarVBD)
                        .addComponent(jLabelDBAno)))
                .addContainerGap())
        );

        jButtonCambiarVBD.getAccessibleContext().setAccessibleDescription(resourceMap.getString("jButton4.AccessibleContext.accessibleDescription")); // NOI18N

        jPanelXestión.setBorder(javax.swing.BorderFactory.createTitledBorder(resourceMap.getString("jPanelXestión.border.title"))); // NOI18N
        jPanelXestión.setName("jPanelXestión"); // NOI18N

        jButtonNovosRexControl.setText(resourceMap.getString("jButtonNovosRexControl.text")); // NOI18N
        jButtonNovosRexControl.setName("jButtonNovosRexControl"); // NOI18N
        jButtonNovosRexControl.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButtonNovosRexControlMouseClicked(evt);
            }
        });

        jButtonNovosConduct.setText(resourceMap.getString("jButtonNovosConduct.text")); // NOI18N
        jButtonNovosConduct.setName("jButtonNovosConduct"); // NOI18N
        jButtonNovosConduct.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButtonNovosConductMouseClicked(evt);
            }
        });

        jButtonNovosVehic.setText(resourceMap.getString("jButtonNovosVehic.text")); // NOI18N
        jButtonNovosVehic.setName("jButtonNovosVehic"); // NOI18N
        jButtonNovosVehic.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButtonNovosVehicMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanelXestiónLayout = new javax.swing.GroupLayout(jPanelXestión);
        jPanelXestión.setLayout(jPanelXestiónLayout);
        jPanelXestiónLayout.setHorizontalGroup(
            jPanelXestiónLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelXestiónLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButtonNovosRexControl)
                .addGap(18, 18, 18)
                .addComponent(jButtonNovosVehic)
                .addGap(18, 18, 18)
                .addComponent(jButtonNovosConduct)
                .addContainerGap(470, Short.MAX_VALUE))
        );
        jPanelXestiónLayout.setVerticalGroup(
            jPanelXestiónLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelXestiónLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelXestiónLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonNovosRexControl)
                    .addComponent(jButtonNovosVehic)
                    .addComponent(jButtonNovosConduct))
                .addContainerGap(75, Short.MAX_VALUE))
        );

        jPanelEstatísticas.setBorder(javax.swing.BorderFactory.createTitledBorder(resourceMap.getString("jPanelEstatísticas.border.title"))); // NOI18N
        jPanelEstatísticas.setName("jPanelEstatísticas"); // NOI18N

        jButtonListRControl.setText(resourceMap.getString("jButtonListRControl.text")); // NOI18N
        jButtonListRControl.setName("jButtonListRControl"); // NOI18N
        jButtonListRControl.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButtonListRControlMouseClicked(evt);
            }
        });

        jLabel1.setText(resourceMap.getString("jLabel1.text")); // NOI18N
        jLabel1.setName("jLabel1"); // NOI18N

        jButtonListVehic.setText(resourceMap.getString("jButtonListVehic.text")); // NOI18N
        jButtonListVehic.setName("jButtonListVehic"); // NOI18N
        jButtonListVehic.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButtonListVehicMouseClicked(evt);
            }
        });

        jButtonListConduct.setText(resourceMap.getString("jButtonListConduct.text")); // NOI18N
        jButtonListConduct.setName("jButtonListConduct"); // NOI18N
        jButtonListConduct.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButtonListConductMouseClicked(evt);
            }
        });

        jLabel2.setText(resourceMap.getString("jLabel2.text")); // NOI18N
        jLabel2.setName("jLabel2"); // NOI18N

        jButtonGastosConsAnuais.setText(resourceMap.getString("jButtonGastosConsAnuais.text")); // NOI18N
        jButtonGastosConsAnuais.setName("jButtonGastosConsAnuais"); // NOI18N
        jButtonGastosConsAnuais.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButtonGastosConsAnuaisMouseClicked(evt);
            }
        });

        jComboBoxConsumoMes.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "01: Consumo medio e total e total en Xaneiro", "02: Consumo medio e total en Febreiro", "03: Consumo medio e total en Marzo", "04: Consumo medio e total en Abril", "05: Consumo medio e total en Maio", "06: Consumo medio e total en Xuño", "07: Consumo medio e total en Xullo", "08: Consumo medio e total en Agosto", "09: Consumo medio e total en Setembro", "10: Consumo medio e total en Outubro", "11: Consumo medio e total en Novembro", "12: Consumo medio e total en Decembro" }));
        jComboBoxConsumoMes.setName("jComboBoxConsumoMes"); // NOI18N
        jComboBoxConsumoMes.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBoxConsumoMesItemStateChanged(evt);
            }
        });

        jLabel3.setText(resourceMap.getString("jLabel3.text")); // NOI18N
        jLabel3.setName("jLabel3"); // NOI18N

        jButtonGastosRepAnuais.setText(resourceMap.getString("jButtonGastosRepAnuais.text")); // NOI18N
        jButtonGastosRepAnuais.setName("jButtonGastosRepAnuais"); // NOI18N
        jButtonGastosRepAnuais.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButtonGastosRepAnuaisMouseClicked(evt);
            }
        });

        jComboBoxGastosRepMes.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "01: Gasto medio e total e total en Xaneiro", "02: Gasto medio e total en Febreiro", "03: Gasto medio e total en Marzo", "04: Gasto medio e total en Abril", "05: Gasto medio e total en Maio", "06: Gasto medio e total en Xuño", "07: Gasto medio e total en Xullo", "08: Gasto medio e total en Agosto", "09: Gasto medio e total en Setembro", "10: Gasto medio e total en Outubro", "11: Gasto medio e total en Novembro", "12: Gasto medio e total en Decembro" }));
        jComboBoxGastosRepMes.setName("jComboBoxGastosRepMes"); // NOI18N
        jComboBoxGastosRepMes.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBoxGastosRepMesItemStateChanged(evt);
            }
        });

        jComboBoxMatriculas.setName("jComboBoxMatriculas"); // NOI18N
        jComboBoxMatriculas.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBoxMatriculasItemStateChanged(evt);
            }
        });

        jLabel4.setText(resourceMap.getString("jLabel4.text")); // NOI18N
        jLabel4.setName("jLabel4"); // NOI18N

        javax.swing.GroupLayout jPanelEstatísticasLayout = new javax.swing.GroupLayout(jPanelEstatísticas);
        jPanelEstatísticas.setLayout(jPanelEstatísticasLayout);
        jPanelEstatísticasLayout.setHorizontalGroup(
            jPanelEstatísticasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelEstatísticasLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelEstatísticasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelEstatísticasLayout.createSequentialGroup()
                        .addGroup(jPanelEstatísticasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2))
                        .addGroup(jPanelEstatísticasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanelEstatísticasLayout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButtonListRControl)
                                .addGap(18, 18, 18)
                                .addComponent(jButtonListVehic)
                                .addGap(18, 18, 18)
                                .addComponent(jButtonListConduct))
                            .addGroup(jPanelEstatísticasLayout.createSequentialGroup()
                                .addGap(8, 8, 8)
                                .addGroup(jPanelEstatísticasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jButtonGastosConsAnuais)
                                    .addComponent(jButtonGastosRepAnuais))
                                .addGap(22, 22, 22)
                                .addGroup(jPanelEstatísticasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jComboBoxGastosRepMes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jComboBoxConsumoMes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jComboBoxMatriculas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addComponent(jLabel3))
                .addContainerGap(41, Short.MAX_VALUE))
        );
        jPanelEstatísticasLayout.setVerticalGroup(
            jPanelEstatísticasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelEstatísticasLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelEstatísticasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jButtonListConduct)
                    .addComponent(jButtonListVehic)
                    .addComponent(jButtonListRControl))
                .addGap(31, 31, 31)
                .addGroup(jPanelEstatísticasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jComboBoxMatriculas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4)
                    .addComponent(jButtonGastosConsAnuais)
                    .addComponent(jComboBoxConsumoMes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(28, 28, 28)
                .addGroup(jPanelEstatísticasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jButtonGastosRepAnuais)
                    .addComponent(jComboBoxGastosRepMes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jButtonClose.setText(resourceMap.getString("jButtonClose.text")); // NOI18N
        jButtonClose.setName("jButtonClose"); // NOI18N
        jButtonClose.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButtonCloseMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanelVersiónDB, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanelXestión, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanelEstatísticas, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, mainPanelLayout.createSequentialGroup()
                .addContainerGap(769, Short.MAX_VALUE)
                .addComponent(jButtonClose)
                .addGap(21, 21, 21))
        );
        mainPanelLayout.setVerticalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addComponent(jPanelVersiónDB, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanelXestión, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanelEstatísticas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonClose)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanelEstatísticas.getAccessibleContext().setAccessibleName(resourceMap.getString("jPanelEstatísticas.AccessibleContext.accessibleName")); // NOI18N

        menuBar.setName("menuBar"); // NOI18N

        fileMenu.setText(resourceMap.getString("fileMenu.text")); // NOI18N
        fileMenu.setName("fileMenu"); // NOI18N

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(breo_xestorgastosvehiculos.Breo_XestorGastosVehiculosApp.class).getContext().getActionMap(Breo_XestorGastosVehiculosView.class, this);
        exitMenuItem.setAction(actionMap.get("quit")); // NOI18N
        exitMenuItem.setText(resourceMap.getString("exitMenuItem.text")); // NOI18N
        exitMenuItem.setName("exitMenuItem"); // NOI18N
        fileMenu.add(exitMenuItem);

        menuBar.add(fileMenu);

        helpMenu.setText(resourceMap.getString("helpMenu.text")); // NOI18N
        helpMenu.setName("helpMenu"); // NOI18N

        aboutMenuItem.setAction(actionMap.get("showAboutBox")); // NOI18N
        aboutMenuItem.setText(resourceMap.getString("aboutMenuItem.text")); // NOI18N
        aboutMenuItem.setName("aboutMenuItem"); // NOI18N
        helpMenu.add(aboutMenuItem);

        jMenuItem1.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_H, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem1.setText(resourceMap.getString("jMenuItem1.text")); // NOI18N
        jMenuItem1.setName("jMenuItem1"); // NOI18N
        helpMenu.add(jMenuItem1);

        menuBar.add(helpMenu);

        statusPanel.setName("statusPanel"); // NOI18N

        statusPanelSeparator.setName("statusPanelSeparator"); // NOI18N

        statusMessageLabel.setName("statusMessageLabel"); // NOI18N

        statusAnimationLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        statusAnimationLabel.setName("statusAnimationLabel"); // NOI18N

        progressBar.setName("progressBar"); // NOI18N

        javax.swing.GroupLayout statusPanelLayout = new javax.swing.GroupLayout(statusPanel);
        statusPanel.setLayout(statusPanelLayout);
        statusPanelLayout.setHorizontalGroup(
            statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(statusPanelSeparator, javax.swing.GroupLayout.DEFAULT_SIZE, 905, Short.MAX_VALUE)
            .addGroup(statusPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(statusMessageLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 735, Short.MAX_VALUE)
                .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(statusAnimationLabel)
                .addContainerGap())
        );
        statusPanelLayout.setVerticalGroup(
            statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(statusPanelLayout.createSequentialGroup()
                .addComponent(statusPanelSeparator, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(statusMessageLabel)
                    .addComponent(statusAnimationLabel)
                    .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(3, 3, 3))
        );

        setComponent(mainPanel);
        setMenuBar(menuBar);
        setStatusBar(statusPanel);
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonCloseMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonCloseMouseClicked
        mysql_i.pechaConexion();
        this.stopDatabaseServer();
        System.exit(0);
    }//GEN-LAST:event_jButtonCloseMouseClicked

    private void jButtonGastosConsAnuaisMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonGastosConsAnuaisMouseClicked
        try{
            pdfx.createPdf(
                "CONSUMO_medio_"
                +this.ano_actual
                +"_transcurrido",
                this.database_name,
                TIPO_CONSULTA.CONSUMO_AOS_100);
        }
        catch( DocumentException de){
            JOptionPane.showMessageDialog(null,
                    "ERRO: Problema xerando documento:\n"
                    + de);
        }
        catch( IOException ioe){
            JOptionPane.showMessageDialog(null,
                    "ERRO: Problema na lectura/escritura no disco duro:\n"
                    + ioe);
        }
        try {
            Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler "
                    + "CONSUMO_medio_"
                    + this.ano_actual
                    + "_transcurrido"
                    + ".pdf");
        } catch (IOException e) { }
    }//GEN-LAST:event_jButtonGastosConsAnuaisMouseClicked

    private void jButtonNovosRexControlMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonNovosRexControlMouseClicked
        try {
            //Call EngadirLinhaInforme.jar
            Desktop.getDesktop().open(new File("EngadirLinhaInforme.jar"));
        }
        catch( IllegalArgumentException iae ){
            JOptionPane.showMessageDialog(null,
                    "ERRO: Problema ó invocar o programa (non se atopa na "
                    + "carpeta de instalación):\n"
                    + iae
                    );
        }
        catch( Exception exception ){
            JOptionPane.showMessageDialog(null,
                    "ERRO: Problema indeterminado:\n"
                    + exception);
        }
    }//GEN-LAST:event_jButtonNovosRexControlMouseClicked

    private void jButtonNovosVehicMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonNovosVehicMouseClicked
        try{
            mysql_i.insertaVehiculo();
        }
        catch( SQLException sqle){
            JOptionPane.showMessageDialog(null, "erro insertando datos de "
                    + "vehículo:\n" + sqle);
        }
        catch( Exception e){
            JOptionPane.showMessageDialog(null, "erro descoñecido insertando "
                    + "datos de vehículo:\n" + e);
        }
        try{
            this.jComboBoxMatriculas.removeAllItems();
            this.readDBAndReloadCombos();
        }
        catch( Exception xe ){
            JOptionPane.showMessageDialog(null, 
                    "erro descoñecido recargando datos datos de matrículas "
                    + "(consulte no desplegable se foron recargadas, se non o "
                    + "foron, por favor, peche o programa e volva abrilo):\n"
                    + xe);
        }
    }//GEN-LAST:event_jButtonNovosVehicMouseClicked

    private void jButtonNovosConductMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonNovosConductMouseClicked
        try{
            mysql_i.insertaConductor();
        }
        catch( SQLException sqle){
            JOptionPane.showMessageDialog(null, "erro insertando datos do "
                    + "conductor:\n" + sqle);
        }
        catch( Exception e){
            JOptionPane.showMessageDialog(null, "erro descoñecido insertando "
                    + "datos do conductor:\n" + e);
        }
    }//GEN-LAST:event_jButtonNovosConductMouseClicked

    private void jButtonListRControlMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonListRControlMouseClicked
        try{
            pdfx.createPdf(
                "lista_rexistros_CONTROL_vehículos_BD_actual_"+this.ano_actual,
                this.database_name,
                TIPO_CONSULTA.LINHAS);
        }
        catch( DocumentException de){ 
            JOptionPane.showMessageDialog(null,
                    "ERRO: Problema xerando documento:\n"
                    + de);
        }
        catch( IOException ioe){ 
            JOptionPane.showMessageDialog(null,
                    "ERRO: Problema na lectura/escritura no disco duro:\n"
                    + ioe);
        }
        try {
            Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler "
                    + "lista_rexistros_CONTROL_vehículos_BD_actual_"
                    + this.ano_actual
                    + ".pdf");
        } catch (IOException e) { }
    }//GEN-LAST:event_jButtonListRControlMouseClicked

    private void jButtonListVehicMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonListVehicMouseClicked
        try{
            pdfx.createPdf(
                "lista_VEHÍCULOS_BD_actual_"+this.ano_actual,
                this.database_name,
                TIPO_CONSULTA.VEHICULOS);
        }
        catch( DocumentException de){
            JOptionPane.showMessageDialog(null,
                    "ERRO: Problema xerando documento:\n"
                    + de);
        }
        catch( IOException ioe){
            JOptionPane.showMessageDialog(null,
                    "ERRO: Problema na lectura/escritura no disco duro:\n"
                    + ioe);
        }
        try {
            Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler "
                    + "lista_VEHÍCULOS_BD_actual_"
                    + this.ano_actual
                    + ".pdf");
        } catch (IOException e) { }
    }//GEN-LAST:event_jButtonListVehicMouseClicked

    private void jButtonListConductMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonListConductMouseClicked
        try{
            pdfx.createPdf(
                "lista_CONDUCTORES_BD_actual_"+this.ano_actual,
                this.database_name,
                TIPO_CONSULTA.CONDUCTORES);
        }
        catch( DocumentException de){
            JOptionPane.showMessageDialog(null,
                    "ERRO: Problema xerando documento:\n"
                    + de);
        }
        catch( IOException ioe){
            JOptionPane.showMessageDialog(null,
                    "ERRO: Problema na lectura/escritura no disco duro:\n"
                    + ioe);
        }
        try {
            Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler "
                    + "lista_CONDUCTORES_BD_actual_"
                    + this.ano_actual
                    + ".pdf");
        } catch (IOException e) { }
    }//GEN-LAST:event_jButtonListConductMouseClicked

    private void jButtonGastosRepAnuaisMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonGastosRepAnuaisMouseClicked
                try{
            pdfx.createPdf(
                "gastos_REPARACIÓN_"
                +this.ano_actual
                +"_transcurrido",
                this.database_name,
                TIPO_CONSULTA.GASTO_REPARA_MEDIO);
        }
        catch( DocumentException de){
            JOptionPane.showMessageDialog(null,
                    "ERRO: Problema xerando documento:\n"
                    + de);
        }
        catch( IOException ioe){
            JOptionPane.showMessageDialog(null,
                    "ERRO: Problema na lectura/escritura no disco duro:\n"
                    + ioe);
        }
        try {
            Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler "
                    + "gastos_REPARACIÓN_"
                    + this.ano_actual
                    + "_transcurrido"
                    + ".pdf");
        } catch (IOException e) { }
    }//GEN-LAST:event_jButtonGastosRepAnuaisMouseClicked

    private void jButtonCambiarVBDMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonCambiarVBDMouseClicked
        String novo_ano;
        novo_ano = JOptionPane.showInputDialog(null, "Ano?");
        if( novo_ano != null){
            setCurrentYearAndDBName(novo_ano);
        }
        try{
            this.jComboBoxMatriculas.removeAllItems();
            this.readDBAndReloadCombos();
        }
        catch( Exception xe ){
            JOptionPane.showMessageDialog(null,
                    "erro descoñecido recargando datos datos de matrículas "
                    + "(consulte no desplegable se foron recargadas, se non o "
                    + "foron, por favor, peche o programa e volva abrilo):\n"
                    + xe);
        }
    }//GEN-LAST:event_jButtonCambiarVBDMouseClicked

    private void jComboBoxConsumoMesItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBoxConsumoMesItemStateChanged
        this. selected_mes_consumo_str =
                this.jComboBoxConsumoMes.getSelectedItem()
                    .toString().substring(0, 2);
        //JOptionPane.showMessageDialog(null,selected_mes_consumo_str+"\n"+selected_matricula_str);
    }//GEN-LAST:event_jComboBoxConsumoMesItemStateChanged

    private void jComboBoxMatriculasItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBoxMatriculasItemStateChanged
        String filename = "__erro__xerando_pdf";
        this.selected_matricula_str =
                this.jComboBoxMatriculas.getSelectedItem().toString();
        //JOptionPane.showMessageDialog(null,selected_matricula_str);
        try{
            filename = "CONSUMO_medio_MES_"
                    +this.selected_mes_consumo_str
                    +"_para_"
                    +this.selected_matricula_str;
        }
        catch( Exception xe){
            JOptionPane.showMessageDialog(null,
                    "ERRO: Problema co nome do arquivo:\n"
                    + xe);
        }
        try{
            pdfx.setMes(Short.valueOf(this.selected_mes_consumo_str));
            pdfx.setMatricula(this.selected_matricula_str);
            pdfx.createPdf(
                filename,
                this.database_name,
                TIPO_CONSULTA.ESTATÍSTICA_VEHICULO_MENSUAL);
        }
        catch( DocumentException de){
            JOptionPane.showMessageDialog(null,
                    "ERRO: Problema xerando documento:\n"
                    + de);
        }
        catch( IOException ioe){
            JOptionPane.showMessageDialog(null,
                    "ERRO: Problema na lectura/escritura no disco duro:\n"
                    + ioe);
        }
        catch( Exception xe){
            JOptionPane.showMessageDialog(null,
                    "ERRO: problema non previsto calculando consumo mensual:\n"
                    + xe);
        }
        try {
            Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler "
                    +filename
                    + ".pdf");
        } catch (IOException e) { }
    }//GEN-LAST:event_jComboBoxMatriculasItemStateChanged

    private void jComboBoxGastosRepMesItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBoxGastosRepMesItemStateChanged
        String filename = "__erro__xerando_pdf";
        this. selected_mes_gastos_str =
                this.jComboBoxGastosRepMes.getSelectedItem()
                    .toString().substring(0, 2);
        //JOptionPane.showMessageDialog(null,selected_mes_gastos_str);
        //JOptionPane.showMessageDialog(null,selected_matricula_str);
        try{
            filename = "Gasto_REPARACIÓN_medio_MES_"
                    +this.selected_mes_gastos_str;
        }
        catch( Exception xe){
            JOptionPane.showMessageDialog(null,
                    "ERRO: Problema co nome do arquivo:\n"
                    + xe);
        }
        try{
            pdfx.setMes(Short.valueOf(this.selected_mes_gastos_str));
            pdfx.createPdf(
                filename,
                this.database_name,
                TIPO_CONSULTA.GASTO_REPARA_MEDIO_MENSUAL);
        }
        catch( DocumentException de){
            JOptionPane.showMessageDialog(null,
                    "ERRO: Problema xerando documento:\n"
                    + de);
        }
        catch( IOException ioe){
            JOptionPane.showMessageDialog(null,
                    "ERRO: Problema na lectura/escritura no disco duro:\n"
                    + ioe);
        }
        catch( Exception xe){
            JOptionPane.showMessageDialog(null,
                    "ERRO: problema non previsto calculando gasto mensual:\n"
                    + xe);
        }
        try {
            Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler "
                    +filename
                    + ".pdf");
        } catch (IOException e) { }
    }//GEN-LAST:event_jComboBoxGastosRepMesItemStateChanged

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonCambiarVBD;
    private javax.swing.JButton jButtonClose;
    private javax.swing.JButton jButtonGastosConsAnuais;
    private javax.swing.JButton jButtonGastosRepAnuais;
    private javax.swing.JButton jButtonListConduct;
    private javax.swing.JButton jButtonListRControl;
    private javax.swing.JButton jButtonListVehic;
    private javax.swing.JButton jButtonNovosConduct;
    private javax.swing.JButton jButtonNovosRexControl;
    private javax.swing.JButton jButtonNovosVehic;
    private javax.swing.JComboBox jComboBoxConsumoMes;
    private javax.swing.JComboBox jComboBoxGastosRepMes;
    private javax.swing.JComboBox jComboBoxMatriculas;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabelAVISO;
    private javax.swing.JLabel jLabelDBAno;
    private javax.swing.JLabel jLabelDBAnoTag;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JPanel jPanelEstatísticas;
    private javax.swing.JPanel jPanelVersiónDB;
    private javax.swing.JPanel jPanelXestión;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JProgressBar progressBar;
    private javax.swing.JLabel statusAnimationLabel;
    private javax.swing.JLabel statusMessageLabel;
    private javax.swing.JPanel statusPanel;
    // End of variables declaration//GEN-END:variables

    // VARIABLES ENGADIDAS POLO USUSARIO
    private int ano_actual;
    private String database_name;
    private PruebaMySQL mysql_i;
    private String selected_mes_consumo_str = "01";
    private String selected_mes_gastos_str  = "01";
    private String selected_matricula_str = "";

    PDFGenerator pdfx = new PDFGenerator();
    
    private final Timer messageTimer;
    private final Timer busyIconTimer;
    private final Icon idleIcon;
    private final Icon[] busyIcons = new Icon[15];
    private int busyIconIndex = 0;

    private JDialog aboutBox;


    private void readDBAndReloadCombos(){

        {
            int i = 0;
            ResultSet rs = null;
            try{
                rs = mysql_i.getAllInVehiculos();
                /*
                try{
                    this.jComboBoxMatriculas.removeAllItems();
                }
                catch( Exception ei){
                    JOptionPane.showMessageDialog(null,
                            "Problema con removeAllItems:\n" + ei
                          );
                }
                */
                while( rs.next() ){
                    String cadea = rs.getString("id_matricula");
                    try{
                        this.jComboBoxMatriculas.insertItemAt(cadea,i);
                        //if( i == 0 ){ selected_matricula_str = cadea; }
                    }
                    catch( Exception e1 ){
                        JOptionPane.showMessageDialog(null,
                            "Problema ó procesar valores de matrículas:\n" + e1
                          );
                    }
                    i++;

                }// while
            }  // try 1
            catch( Exception e2 ){
                JOptionPane.showMessageDialog(null,
                    "Problema ó solicitar datos de matrículas ó interface "
                    + "coa Base de Datos:\n"
                    + e2
                    );
            } // catch e2
            //mysql_i.pechaConexion();
        } // bloque vehículos

    } // readDBAndReloadCombos

    private void startDatabaseServer(){
        jLabelAVISO.setText("ARRANCANDO SERVIDOR DE BASE DE DATOS."
                + " Espere, por favor.");
        try {
            
            Process p = Runtime.getRuntime().exec("startServer.bat");
            p.waitFor();
        }
        catch (Exception err) {
            JOptionPane.showMessageDialog(null,
                    "ERRO arrancando o servidor:\n"
                    + err);
        }
        jLabelAVISO.setText("");
    }
    private void stopDatabaseServer(){
        jLabelAVISO.setText("APAGANDO O SERVIDOR DE BASE DE DATOS. "
                + "Espere, por favor.");
        try {
            
            Process p = Runtime.getRuntime().exec("stopServer.bat");
            p.waitFor();
        }
        catch (Exception err) {
            JOptionPane.showMessageDialog(null,
                    "ERRO arrancando o servidor:\n"
                    + err);
        }
        jLabelAVISO.setText("");
    }

    private void setCurrentYearAndDBName(){
        Calendar cal = Calendar.getInstance();
        this.ano_actual = cal.get(cal.YEAR);
        jLabelDBAno.setText(Integer.toString(this.ano_actual));
        database_name = "contrlvehicdb_"+this.ano_actual;
    }

    private void setCurrentYearAndDBName(String bd_ano){
        mysql_i.pechaConexion();
        jLabelDBAno.setText(bd_ano);
        this.ano_actual = Integer.parseInt(bd_ano);
        database_name = "contrlvehicdb_"+bd_ano;
        this.mysql_i = new PruebaMySQL(this.database_name) ;
        try{
            this.mysql_i.estableceConexion();
        }
        catch( Exception e2 ){
            JOptionPane.showMessageDialog(null,
                    "Erro abrindo a nova base de datos Base de Datos:\n"
                    + e2
                    );
        }
        this.readDBAndReloadCombos();
    }
}
