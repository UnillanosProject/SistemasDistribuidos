/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Servidor;

import Graficas.PanelGraficaPrincipal;
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
        panelGraficoPrincipal.add(new PanelGraficaPrincipal());
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
		System.out.println("Esperando conexiones");
		try{
			socketServ = new ServerSocket(puerto);
                        velocidad.start();
			while(true){
				Socket socket;
				socket = socketServ.accept();                               
                                labelConectados.setText(""+(Integer.valueOf(labelConectados.getText())+1));
				HilosConex.numCliente++;
//				System.out.println("Se conectó cliente "+HilosConex.numCliente);
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
    
    public JLabel getLabelClave() {
        return labelClave;
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
        jLabel2 = new javax.swing.JLabel();
        labelConectados = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        panelGraficoPrincipal = new javax.swing.JPanel();
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

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Conectados");

        labelConectados.setFont(new java.awt.Font("Calibri Light", 1, 24)); // NOI18N
        labelConectados.setForeground(new java.awt.Color(64, 75, 75));
        labelConectados.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelConectados.setText("0");
        labelConectados.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jSeparator2.setOrientation(javax.swing.SwingConstants.VERTICAL);

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
                .addComponent(campoMd5, javax.swing.GroupLayout.PREFERRED_SIZE, 338, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(botonDescifrar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(labelConectados, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(labelConectados, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(botonDescifrar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(campoMd5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(jSeparator2)
        );

        panelGraficoPrincipal.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        javax.swing.GroupLayout panelGraficoPrincipalLayout = new javax.swing.GroupLayout(panelGraficoPrincipal);
        panelGraficoPrincipal.setLayout(panelGraficoPrincipalLayout);
        panelGraficoPrincipalLayout.setHorizontalGroup(
            panelGraficoPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 353, Short.MAX_VALUE)
        );
        panelGraficoPrincipalLayout.setVerticalGroup(
            panelGraficoPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 424, Short.MAX_VALUE)
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
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(labelClave, javax.swing.GroupLayout.DEFAULT_SIZE, 19, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel3)
                        .addComponent(jLabel4)
                        .addComponent(jLabel5)
                        .addComponent(labelProcesados)
                        .addComponent(labelRestantes)
                        .addComponent(labelVelocidad)
                        .addComponent(jLabel6)))
                .addContainerGap())
        );

        panelGraficos.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        selector1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "192.168.10.106", "Item 2", "Item 3", "Item 4" }));

        jSeparator1.setForeground(new java.awt.Color(153, 153, 153));

        selector2.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "192.168.10.106", "Item 2", "Item 3", "Item 4" }));

        javax.swing.GroupLayout panelGraficosLayout = new javax.swing.GroupLayout(panelGraficos);
        panelGraficos.setLayout(panelGraficosLayout);
        panelGraficosLayout.setHorizontalGroup(
            panelGraficosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSeparator1)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelGraficosLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelGraficosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(selector1, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(selector2, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        panelGraficosLayout.setVerticalGroup(
            panelGraficosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelGraficosLayout.createSequentialGroup()
                .addComponent(selector1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(175, 175, 175)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(selector2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(189, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(panelGraficoPrincipal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(panelGraficos, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(panelGraficoPrincipal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panelGraficos, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 0, 0)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        String Key = JOptionPane.showInputDialog(this, "Ingrese clave a cifrar", "Clave", JOptionPane.INFORMATION_MESSAGE);
        GeneradorMD5 obj=new GeneradorMD5();
        String clave = obj.md5(Key);
        campoMd5.setText(clave);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void botonDescifrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonDescifrarActionPerformed
        if(botonDescifrar.getText().equals("Descifrar")){
            md5.setClaveMd5(campoMd5.getText());
            hiloSocket.start();
            botonDescifrar.setText("Cancelar");
            return;
        }
        if(botonDescifrar.getText().equals("Cancelar")){
            hiloSocket.stop();
            botonDescifrar.setText("Descifrar");
            return;
        }
    }//GEN-LAST:event_botonDescifrarActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton botonDescifrar;
    private javax.swing.JTextField campoMd5;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JLabel labelClave;
    private javax.swing.JLabel labelConectados;
    private javax.swing.JLabel labelProcesados;
    private javax.swing.JLabel labelRestantes;
    private javax.swing.JLabel labelVelocidad;
    private javax.swing.JPanel panelGraficoPrincipal;
    private javax.swing.JPanel panelGraficos;
    private javax.swing.JComboBox selector1;
    private javax.swing.JComboBox selector2;
    // End of variables declaration//GEN-END:variables
}
