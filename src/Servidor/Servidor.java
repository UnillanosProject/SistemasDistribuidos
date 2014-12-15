/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

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
            JOptionPane.showMessageDialog(servidor, "La clave es: "+clave, "Se ha encontrado la clave", JOptionPane.INFORMATION_MESSAGE);
        }

public class Velocidad extends Thread{
    @Override
    public void run(){
        while(true){
            try {

                int vel=0;
                for (int i = 0; i < hilosConex.size(); i++) {
                    vel=vel+hilosConex.get(i).vel;
                }
                vel=vel*2;
                servidor.labelVelocidad.setText(vel+" / segundo");
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
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        labelConectados = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        graficoPrincipal = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        labelProcesados = new javax.swing.JLabel();
        labelRestantes = new javax.swing.JLabel();
        labelVelocidad = new javax.swing.JLabel();
        panelGraficos = new javax.swing.JPanel();
        selector1 = new javax.swing.JComboBox();
        jSeparator1 = new javax.swing.JSeparator();
        selector2 = new javax.swing.JComboBox();
        grafico1 = new javax.swing.JLabel();
        grafico2 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("MD5 Decrypter - Server");

        jPanel3.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel1.setText("MD5:");

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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(campoMd5, javax.swing.GroupLayout.DEFAULT_SIZE, 295, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(botonDescifrar)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(11, 11, 11)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(campoMd5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(botonDescifrar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Conectados:");

        labelConectados.setFont(new java.awt.Font("Calibri Light", 1, 24)); // NOI18N
        labelConectados.setForeground(new java.awt.Color(64, 75, 75));
        labelConectados.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelConectados.setText("0");
        labelConectados.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(labelConectados, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(labelConectados, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        graficoPrincipal.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        graficoPrincipal.setText("Gráfico principal");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(graficoPrincipal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(graficoPrincipal, javax.swing.GroupLayout.PREFERRED_SIZE, 411, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jPanel4.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel3.setText("Procesados:");

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel4.setText("Restantes:");

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel5.setText("Velocidad:");

        labelProcesados.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelProcesados.setText("0");
        labelProcesados.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        labelRestantes.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelRestantes.setText("2565726409");
        labelRestantes.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        labelVelocidad.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelVelocidad.setText("0 / segundo");
        labelVelocidad.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(labelProcesados, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 26, Short.MAX_VALUE)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(labelRestantes, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(35, 35, 35)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(labelVelocidad, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4)
                    .addComponent(jLabel5)
                    .addComponent(labelProcesados)
                    .addComponent(labelRestantes)
                    .addComponent(labelVelocidad))
                .addContainerGap())
        );

        panelGraficos.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        selector1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "192.168.10.106", "Item 2", "Item 3", "Item 4" }));

        jSeparator1.setForeground(new java.awt.Color(153, 153, 153));

        selector2.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "192.168.10.106", "Item 2", "Item 3", "Item 4" }));

        grafico1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        grafico1.setText("Gráfico 1");

        grafico2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        grafico2.setText("Gráfico 2");

        javax.swing.GroupLayout panelGraficosLayout = new javax.swing.GroupLayout(panelGraficos);
        panelGraficos.setLayout(panelGraficosLayout);
        panelGraficosLayout.setHorizontalGroup(
            panelGraficosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSeparator1)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelGraficosLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelGraficosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(grafico2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(grafico1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(selector1, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(selector2, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        panelGraficosLayout.setVerticalGroup(
            panelGraficosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelGraficosLayout.createSequentialGroup()
                .addComponent(selector1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(grafico1, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(selector2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(grafico2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 0, 0)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panelGraficos, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 0, 0)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panelGraficos, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 0, 0)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void botonDescifrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonDescifrarActionPerformed
        md5.setClaveMd5(campoMd5.getText());
        hiloSocket.start();
    }//GEN-LAST:event_botonDescifrarActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        String Key = JOptionPane.showInputDialog(this, "Ingrese Clave a Cifrar", "Clave", JOptionPane.INFORMATION_MESSAGE);
        GeneradorMD5 obj=new GeneradorMD5();
        String clave = obj.md5(Key);
        campoMd5.setText(clave);
    }//GEN-LAST:event_jButton1ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton botonDescifrar;
    private javax.swing.JTextField campoMd5;
    private javax.swing.JLabel grafico1;
    private javax.swing.JLabel grafico2;
    private javax.swing.JLabel graficoPrincipal;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JLabel labelConectados;
    private javax.swing.JLabel labelProcesados;
    private javax.swing.JLabel labelRestantes;
    private javax.swing.JLabel labelVelocidad;
    private javax.swing.JPanel panelGraficos;
    private javax.swing.JComboBox selector1;
    private javax.swing.JComboBox selector2;
    // End of variables declaration//GEN-END:variables
}
