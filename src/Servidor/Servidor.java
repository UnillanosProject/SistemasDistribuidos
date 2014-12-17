package Servidor;

import MD5.GeneradorMD5;
import MD5.Md5;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import java.awt.*;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.plot.dial.*;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.general.DefaultValueDataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.GradientPaintTransformType;
import org.jfree.ui.StandardGradientPaintTransformer;
/**
 *
 * @author Arango Abello
 */
public class Servidor extends javax.swing.JFrame {

    /**
     * Creates new form Servidor
     */
    private HiloSocket hiloSocket;
    private Md5 md5;
    private ArrayList<Asignacion> asignaciones;
    
    public Servidor() {
        initComponents();
        this.setLocationRelativeTo(null);
        hiloSocket=new HiloSocket(this);
        md5=new Md5();
        asignaciones=asignaciones();
//        panelGraficoPrincipal.add(new PanelGraficaPrincipal());
    }
    
    public void disminuirConectados(){
        labelConectados.setText((Integer.valueOf(labelConectados.getText())-1)+"");
    }
    
    public void removerDeArray(Asignacion asig){
        for (int i = 0; i < asignaciones.size(); i++) {
            if(asignaciones.get(i)==asig){
                asignaciones.remove(i);
            }
        }
    }
    
public ArrayList<Asignacion> asignaciones(){
    ArrayList<Asignacion> asignacionesAux=new ArrayList<>();
    for (int i = 0; i < md5.getCaracteres().length; i++) {
        for (int j = 0; j < md5.getCaracteres().length; j++) {
            asignacionesAux.add(new Asignacion(i,j));
        }
    }
    return asignacionesAux;
}
public Asignacion asignar(){
    for (int i = 0; i < asignaciones.size(); i++) {
//        System.out.println(asignaciones.get(i).isAsignado());
        if (!asignaciones.get(i).isAsignado()) {
            return asignaciones.get(i);
        }
    }
    return null;
}
    
public class HiloSocket extends Thread{
        
    Servidor servidor;
    int puerto;
    Velocidad velocidad;
    ArrayList<HilosConex> hilosConex;
        public HiloSocket(Servidor servidor) {
            this.servidor = servidor;
            puerto=9999;
            hilosConex=new ArrayList<>();
            velocidad=new Velocidad();
        }
        
        @Override
        public void run(){
            ServerSocket socketServ;
        //System.out.println("Esperando conexiones");
            JOptionPane.showMessageDialog(servidor, "Esperando Conexiones...", "Servidor Listo!!", JOptionPane.INFORMATION_MESSAGE);
        try{
            socketServ = new ServerSocket(puerto);
                        velocidad.start();
            while(true){
                Socket socket;
                socket = socketServ.accept();                               
                labelConectados.setText(""+(Integer.valueOf(labelConectados.getText())+1));
                HilosConex.numCliente++;
//              System.out.println("Se conectó cliente "+HilosConex.numCliente);
                HilosConex hilo=new HilosConex(servidor,socket,HilosConex.numCliente,md5.getClaveMd5(),asignar());
                                hilo.start();
                                hilosConex.add(hilo);                
            }
        }catch(IOException e){}
        }
        
        public void claveEncontrada(String clave){
//            servidor.mostrarClave(clave);
            for (int i = 0; i < hilosConex.size(); i++) {
                hilosConex.get(i).finalizar();
            }  
            labelClave.setText(clave);
            JOptionPane.showMessageDialog(servidor, "La clave es "+clave, "Clave encontrada", JOptionPane.INFORMATION_MESSAGE);
            botonDescifrar.setText("Descifrar");
            labelProcesados.setText("");
            labelRestantes.setText("");
            campoMd5.setText("");
            labelClave.setText("");
            try {
                hiloSocket.finalize();
                hilosConex.clear();
            } catch (Throwable ex) {
            }
        }

        public void cambiarGrafico1(String ip) {
            for (int i = 0; i < hilosConex.size(); i++) {
                if (hilosConex.get(i).ip.equals(ip)) {
                    ArrayList<String> CPUs = hilosConex.get(i).CPUs;
                    for (int j = 0; j < CPUs.size(); j++) {
                        graficoPequeño1.tiempoActual=graficoPequeño1.tiempoActual+0.5;
                        graficoPequeño1.añadirASerie(graficoPequeño1.tiempoActual,Double.parseDouble(CPUs.get(j)));
                    }
                }
            }
        }
        public void cambiarGrafico2(String ip) {
            for (int i = 0; i < hilosConex.size(); i++) {
                if (hilosConex.get(i).ip.equals(ip)) {
                    ArrayList<String> RAMs = hilosConex.get(i).RAMs;
                    for (int j = 0; j < RAMs.size(); j++) {
                        graficoPequeño2.tiempoActual=graficoPequeño2.tiempoActual+0.5;
                        graficoPequeño2.añadirASerie(graficoPequeño2.tiempoActual,Double.parseDouble(RAMs.get(j))/1024);
                    }
                }
            }
        }

public class Velocidad extends Thread{
    @Override
    public void run(){
        while(true){
            try {

                long vel=0;
                for (int i = 0; i < hilosConex.size(); i++) {
                    vel=vel+hilosConex.get(i).vel;
                }
                vel=vel*2;
                servidor.labelVelocidad.setText(vel+" / seg");
                
                double cpuTotal=0,ramTotal=0;
                for (int q = 0; q < hilosConex.size(); q++) {
                    if (hilosConex.get(q).ip.equals(selector1.getSelectedItem().toString())) {
//                        if (hilosConex.get(q).cpuTotal!=0) {
                        graficoPequeño1.tiempoActual=graficoPequeño1.tiempoActual+0.5;
                        graficoPequeño1.añadirASerie(graficoPequeño1.tiempoActual,Double.parseDouble(hilosConex.get(q).CPUactual)*(hilosConex.get(q).cpuTotal/1000)/100);
//                        }
                    }
                    if (hilosConex.get(q).ip.equals(selector2.getSelectedItem().toString())) {
                        graficoPequeño2.tiempoActual=graficoPequeño2.tiempoActual+0.5;
                        graficoPequeño2.añadirASerie(graficoPequeño2.tiempoActual,Double.parseDouble(hilosConex.get(q).RAMactual)/1024);
                    }
//                    System.out.println(Double.parseDouble(hilosConex.get(q).CPUactual)*(hilosConex.get(q).cpuTotal/1000)/100);
                    cpuTotal+=Double.parseDouble(hilosConex.get(q).CPUactual)*(hilosConex.get(q).cpuTotal/1000)/100;
                    ramTotal+=Double.parseDouble(hilosConex.get(q).RAMactual);
                }
                graficaPrincipal1.setValorCPU(cpuTotal);
                graficaPrincipal1.setValorRAM(ramTotal/1024);
                Thread.sleep(500);
            } catch (InterruptedException ex) {
            }
        }
    }
}        
        
}

    public HiloSocket getHiloSocket() {
        return hiloSocket;
    }

    public JLabel getLabelProcesados() {
        return labelProcesados;
    }
     
    public JLabel getLabelRestantes() {
        return labelRestantes;
    }
    
    public JTextField getCampoMd5() {
        return campoMd5;
    }
    
    public JLabel getLabelClave() {
        return labelClave;
    }

    public JComboBox getSelector1() {
        return selector1;
    }

    public JComboBox getSelector2() {
        return selector2;
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel3 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        campoMd5 = new javax.swing.JTextField();
        botonDescifrar = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        labelProcesados = new javax.swing.JLabel();
        labelRestantes = new javax.swing.JLabel();
        labelVelocidad = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        labelClave = new javax.swing.JLabel();
        panelGraficos = new javax.swing.JPanel();
        selector1 = new javax.swing.JComboBox();
        jSeparator1 = new javax.swing.JSeparator();
        selector2 = new javax.swing.JComboBox();
        graficoPequeño2 = new Servidor.GraficoPequeño2();
        graficoPequeño1 = new Servidor.GraficoPequeño();
        jPanel1 = new javax.swing.JPanel();
        labelConectados = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        graficaPrincipal1 = new Servidor.GraficaPrincipal();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("MD5 Decrypter - Server");

        jPanel3.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel1.setText("MD5");

        botonDescifrar.setText("Descifrar");
        botonDescifrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonDescifrarActionPerformed(evt);
            }
        });

        jButton1.setText("...");
        jButton1.setActionCommand("Key");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(campoMd5, javax.swing.GroupLayout.PREFERRED_SIZE, 259, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(botonDescifrar)
                .addContainerGap(15, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(campoMd5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(botonDescifrar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel4.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel3.setText("Procesados");

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel4.setText("Restantes");

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel5.setText("Velocidad");

        labelProcesados.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelProcesados.setText("0");
        labelProcesados.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        labelRestantes.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelRestantes.setText("2565726409");
        labelRestantes.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        labelVelocidad.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelVelocidad.setText("0 / seg");
        labelVelocidad.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel6.setText("Clave");

        labelClave.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        labelClave.setForeground(new java.awt.Color(53, 155, 35));
        labelClave.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelClave.setToolTipText("");
        labelClave.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(1, 1, 1)));

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(labelClave, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 22, Short.MAX_VALUE)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(labelProcesados, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(labelRestantes, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(28, 28, 28)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(labelVelocidad, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(14, 14, 14))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel6)
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(labelClave, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4)
                            .addComponent(jLabel5)
                            .addComponent(labelProcesados)
                            .addComponent(labelRestantes)
                            .addComponent(labelVelocidad))))
                .addContainerGap())
        );

        panelGraficos.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        selector1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "..." }));
        selector1.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                selector1ItemStateChanged(evt);
            }
        });

        jSeparator1.setForeground(new java.awt.Color(153, 153, 153));

        selector2.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "..." }));
        selector2.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                selector2ItemStateChanged(evt);
            }
        });

        graficoPequeño2.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        graficoPequeño1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        javax.swing.GroupLayout panelGraficosLayout = new javax.swing.GroupLayout(panelGraficos);
        panelGraficos.setLayout(panelGraficosLayout);
        panelGraficosLayout.setHorizontalGroup(
            panelGraficosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSeparator1)
            .addGroup(panelGraficosLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelGraficosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(selector1, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(selector2, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(panelGraficosLayout.createSequentialGroup()
                        .addGroup(panelGraficosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(graficoPequeño1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(graficoPequeño2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        panelGraficosLayout.setVerticalGroup(
            panelGraficosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelGraficosLayout.createSequentialGroup()
                .addComponent(selector1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(graficoPequeño1, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(7, 7, 7)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(7, 7, 7)
                .addComponent(selector2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(graficoPequeño2, javax.swing.GroupLayout.DEFAULT_SIZE, 183, Short.MAX_VALUE)
                .addGap(7, 7, 7))
        );

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        labelConectados.setFont(new java.awt.Font("Calibri Light", 1, 24)); // NOI18N
        labelConectados.setForeground(new java.awt.Color(64, 75, 75));
        labelConectados.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelConectados.setText("0");
        labelConectados.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Conectados:");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(labelConectados, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(labelConectados, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(graficaPrincipal1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 0, 0)
                        .addComponent(panelGraficos, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(panelGraficos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 0, 0)
                        .addComponent(graficaPrincipal1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        String Key = JOptionPane.showInputDialog(this, "Ingrese clave a cifrar", "Clave", JOptionPane.INFORMATION_MESSAGE);
        GeneradorMD5 obj=new GeneradorMD5();
        String clave="";
        if(Key!=null){
        clave = obj.md5(Key);
        }
        campoMd5.setText(clave);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void botonDescifrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonDescifrarActionPerformed
        if(botonDescifrar.getText().equals("Descifrar")){
            md5.setClaveMd5(campoMd5.getText());
            hiloSocket=new HiloSocket(this);
            hiloSocket.start();
            botonDescifrar.setText("Cancelar");
            return; 
        }
        if(botonDescifrar.getText().equals("Cancelar")){
            getHiloSocket().hilosConex.clear();
            for (int i = 0; i < getHiloSocket().hilosConex.size(); i++) {
                getHiloSocket().hilosConex.get(i).finalizar();
            }
            hiloSocket.stop();
            botonDescifrar.setText("Descifrar");
            return;
        }
    }//GEN-LAST:event_botonDescifrarActionPerformed

    private void selector1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_selector1ItemStateChanged
        if (selector1.getSelectedItem().toString().equals("...")) {
            return;
        }
        graficoPequeño1.series1.clear();
        graficoPequeño1.tiempoActual=0;
        hiloSocket.cambiarGrafico1(selector1.getSelectedItem().toString());
    }//GEN-LAST:event_selector1ItemStateChanged

    private void selector2ItemStateChanged(java.awt.event.ItemEvent evt) {                                           
        if (selector2.getSelectedItem().toString().equals("...")) {
            return;
        }
        graficoPequeño2.series1.clear();
        graficoPequeño2.tiempoActual=0;
        hiloSocket.cambiarGrafico2(selector2.getSelectedItem().toString());
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton botonDescifrar;
    private javax.swing.JTextField campoMd5;
    private Servidor.GraficaPrincipal graficaPrincipal1;
    public Servidor.GraficoPequeño graficoPequeño1;
    public Servidor.GraficoPequeño2 graficoPequeño2;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JLabel labelClave;
    private javax.swing.JLabel labelConectados;
    private javax.swing.JLabel labelProcesados;
    private javax.swing.JLabel labelRestantes;
    private javax.swing.JLabel labelVelocidad;
    private javax.swing.JPanel panelGraficos;
    private javax.swing.JComboBox selector1;
    private javax.swing.JComboBox selector2;
    // End of variables declaration//GEN-END:variables

static class GraficaPrincipal extends JPanel
//                implements ChangeListener
        {

                DefaultValueDataset dataset1;
                DefaultValueDataset dataset2;
//                JSlider slider1;
//                JSlider slider2;

//                public void stateChanged(ChangeEvent changeevent)
//                {
//                        dataset1.setValue(new Integer(slider1.getValue()));
//                        dataset2.setValue(new Integer(slider2.getValue()));
//                }
                
                public void setValorCPU(double valor){
                    dataset1.setValue(valor);
                }
                public void setValorRAM(double valor){
                    dataset2.setValue(valor);
                }
                
                public GraficaPrincipal()
                {
                        super(new BorderLayout());
                        setBorder(javax.swing.BorderFactory.createEtchedBorder());
                        dataset1 = new DefaultValueDataset(0D);
                        dataset2 = new DefaultValueDataset(0D);
                        
                        DialPlot dialplot = new DialPlot();
                        
                        dialplot.setView(0.0D, 0.0D, 1.0D, 1.0D);
                        dialplot.setDataset(0, dataset1);
                        dialplot.setDataset(1, dataset2);
                        
                        StandardDialFrame standarddialframe = new StandardDialFrame();
                        standarddialframe.setBackgroundPaint(Color.lightGray);
                        standarddialframe.setForegroundPaint(Color.darkGray);
                        dialplot.setDialFrame(standarddialframe);
                        
                        GradientPaint gradientpaint = new GradientPaint(new Point(), new Color(255, 255, 255), new Point(), new Color(170, 170, 220));
                        DialBackground dialbackground = new DialBackground(gradientpaint);
                        
                        dialbackground.setGradientPaintTransformer(new StandardGradientPaintTransformer(GradientPaintTransformType.VERTICAL));
                        dialplot.setBackground(dialbackground);
                        
                        DialTextAnnotation dialtextannotation = new DialTextAnnotation("CPU(GHz) - Memoria(GB)");
                        dialtextannotation.setFont(new Font("Dialog", 1, 14));
                        dialtextannotation.setRadius(0.69999999999999996D);
                        dialplot.addLayer(dialtextannotation);
                                               
                        DialValueIndicator dialvalueindicator = new DialValueIndicator(0);
                        dialvalueindicator.setFont(new Font("Dialog", 0, 10));
                        dialvalueindicator.setOutlinePaint(Color.darkGray);
                        dialvalueindicator.setRadius(0.59999999999999998D);
                        dialvalueindicator.setAngle(-103D);
                        dialplot.addLayer(dialvalueindicator);
                        
                        DialValueIndicator dialvalueindicator1 = new DialValueIndicator(1);
                        dialvalueindicator1.setFont(new Font("Dialog", 0, 10));
                        dialvalueindicator1.setOutlinePaint(Color.red);
                        dialvalueindicator1.setRadius(0.59999999999999998D);
                        dialvalueindicator1.setAngle(-77D);
                        dialplot.addLayer(dialvalueindicator1);
                                                
                        StandardDialScale standarddialscale = new StandardDialScale(0D, 100D, -120D, -300D, 10D, 4);
                        standarddialscale.setTickRadius(0.88D);
                        standarddialscale.setTickLabelOffset(0.14999999999999999D);
                        standarddialscale.setTickLabelFont(new Font("Dialog", 0, 14));
                        dialplot.addScale(0, standarddialscale);
                        
                        StandardDialScale standarddialscale1 = new StandardDialScale(0.0D, 100D, -120D, -300D, 10D, 4);
                        standarddialscale1.setTickRadius(0.5D);
                        standarddialscale1.setTickLabelOffset(0.14999999999999999D);
                        standarddialscale1.setTickLabelFont(new Font("Dialog", 0, 10));
                        standarddialscale1.setMajorTickPaint(Color.red);
                        standarddialscale1.setMinorTickPaint(Color.red);
                        dialplot.addScale(1, standarddialscale1);
                        
                        dialplot.mapDatasetToScale(1, 1);
                        
                        StandardDialRange standarddialrange = new StandardDialRange(90D, 100D, Color.blue);
                        standarddialrange.setScaleIndex(1);
                        standarddialrange.setInnerRadius(0.58999999999999997D);
                        standarddialrange.setOuterRadius(0.58999999999999997D);
                        dialplot.addLayer(standarddialrange);
                        
                        org.jfree.chart.plot.dial.DialPointer.Pin pin = new org.jfree.chart.plot.dial.DialPointer.Pin(1);
                        pin.setRadius(0.55000000000000004D);
                        dialplot.addPointer(pin);
                        
                        org.jfree.chart.plot.dial.DialPointer.Pointer pointer = new org.jfree.chart.plot.dial.DialPointer.Pointer(0);
                        dialplot.addPointer(pointer);
                        
                        DialCap dialcap = new DialCap();
                        dialcap.setRadius(0.10000000000000001D);
                        dialplot.setCap(dialcap);
                        
                        //ChartFactory.create
                        
                        JFreeChart jfreechart = new JFreeChart(dialplot);
                        jfreechart.setTitle("CPU - Memory");
                        ChartPanel chartpanel = new ChartPanel(jfreechart);
                        chartpanel.setPreferredSize(new Dimension(400, 400));
                        JPanel jpanel = new JPanel(new GridLayout(2, 2));
                        add(chartpanel);
                        add(jpanel, "South");
                }
        }

static class GraficoPequeño extends JPanel {

    XYSeries series1;
    double tiempoActual=0;
public GraficoPequeño() {

//        super(title);

        final XYDataset dataset = createDataset();
        final JFreeChart chart = createChart(dataset);
        final ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(240, 170));
//        setContentPane(chartPanel);
        add(chartPanel);
    }
    

    public void añadirASerie(double x,double y){
        series1.add(x,y);
    }

    /**
     * Creates a sample dataset.
     * 
     * @return a sample dataset.
     */
    private XYDataset createDataset() {
        
        series1 = new XYSeries("CPU");
//        series1.add(1.0, 1.0);
//        series1.add(2.0, 4.0);
//        series1.add(3.0, 3.0);
//        series1.add(4.0, 5.0);
//        series1.add(5.0, 5.0);
//        series1.add(6.0, 7.0);
//        series1.add(7.0, 7.0);
//        series1.add(8.0, 8.0);

//        final XYSeries series2 = new XYSeries("Memoria");
//        series2.add(1.0, 5.0);
//        series2.add(2.0, 7.0);
//        series2.add(3.0, 6.0);
//        series2.add(4.0, 8.0);
//        series2.add(5.0, 4.0);
//        series2.add(6.0, 4.0);
//        series2.add(7.0, 2.0);
//        series2.add(8.0, 1.0);

        final XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(series1);
//        dataset.addSeries(series3);
                
        return dataset;
        
    }
    
    /**
     * Creates a chart.
     * 
     * @param dataset  the data for the chart.
     * 
     * @return a chart.
     */
    private JFreeChart createChart(final XYDataset dataset) {
        
        // create the chart...
        final JFreeChart chart = ChartFactory.createXYLineChart(
            "",      // chart title
            "Tiempo",                      // x axis label
            "GHz",                      // y axis label
            dataset,                  // data
            PlotOrientation.VERTICAL,
            true,                     // include legend
            true,                     // tooltips
            true                     // urls
        );

        // NOW DO SOME OPTIONAL CUSTOMISATION OF THE CHART...
        chart.setBackgroundPaint(Color.white);

//        final StandardLegend legend = (StandardLegend) chart.getLegend();
  //      legend.setDisplaySeriesShapes(true);
        
        // get a reference to the plot for further customisation...
        final XYPlot plot = chart.getXYPlot();
        plot.setBackgroundPaint(Color.lightGray);
    //    plot.setAxisOffset(new Spacer(Spacer.ABSOLUTE, 5.0, 5.0, 5.0, 5.0));
        plot.setDomainGridlinePaint(Color.white);
        plot.setRangeGridlinePaint(Color.white);
        
        
        final XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        //renderer.setSeriesLinesVisible(0, false);
        //renderer.setSeriesShapesVisible(1, false);
        //plot.setRenderer(renderer);

        // change the auto tick unit selection to integer units only...
        final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        // OPTIONAL CUSTOMISATION COMPLETED.
                
        return chart;
        
    }
}

static class GraficoPequeño2 extends JPanel {

    XYSeries series1;
    double tiempoActual=0;
public GraficoPequeño2() {

//        super(title);

        final XYDataset dataset = createDataset();
        final JFreeChart chart = createChart(dataset);
        final ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(240, 170));
//        setContentPane(chartPanel);
        add(chartPanel);
    }
    

    public void añadirASerie(double x,double y){
        series1.add(x,y);
    }

    /**
     * Creates a sample dataset.
     * 
     * @return a sample dataset.
     */
    private XYDataset createDataset() {
        
        series1 = new XYSeries("RAM");
//        series1.add(1.0, 1.0);
//        series1.add(2.0, 4.0);
//        series1.add(3.0, 3.0);
//        series1.add(4.0, 5.0);
//        series1.add(5.0, 5.0);
//        series1.add(6.0, 7.0);
//        series1.add(7.0, 7.0);
//        series1.add(8.0, 8.0);

//        final XYSeries series2 = new XYSeries("Memoria");
//        series2.add(1.0, 5.0);
//        series2.add(2.0, 7.0);
//        series2.add(3.0, 6.0);
//        series2.add(4.0, 8.0);
//        series2.add(5.0, 4.0);
//        series2.add(6.0, 4.0);
//        series2.add(7.0, 2.0);
//        series2.add(8.0, 1.0);

        final XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(series1);
//        dataset.addSeries(series3);
                
        return dataset;
        
    }
    
    /**
     * Creates a chart.
     *  
     * @param dataset  the data for the chart.
     * 
     * @return a chart.
     */
    private JFreeChart createChart(final XYDataset dataset) {
        
        // create the chart...
        final JFreeChart chart = ChartFactory.createXYLineChart(
            "",      // chart title
            "Tiempo",                      // x axis label
            "GB",                      // y axis label
            dataset,                  // data
            PlotOrientation.VERTICAL,
            true,                     // include legend
            true,                     // tooltips
            true                     // urls
        );

        // NOW DO SOME OPTIONAL CUSTOMISATION OF THE CHART...
        chart.setBackgroundPaint(Color.white);

//        final StandardLegend legend = (StandardLegend) chart.getLegend();
  //      legend.setDisplaySeriesShapes(true);
        
        // get a reference to the plot for further customisation...
        final XYPlot plot = chart.getXYPlot();
        plot.setBackgroundPaint(Color.lightGray);
    //    plot.setAxisOffset(new Spacer(Spacer.ABSOLUTE, 5.0, 5.0, 5.0, 5.0));
        plot.setDomainGridlinePaint(Color.white);
        plot.setRangeGridlinePaint(Color.white);
        
        
        final XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        //renderer.setSeriesLinesVisible(0, false);
        //renderer.setSeriesShapesVisible(1, false);
        //plot.setRenderer(renderer);

        // change the auto tick unit selection to integer units only...
        final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        // OPTIONAL CUSTOMISATION COMPLETED.
                
        return chart;
        
    }
}
}
