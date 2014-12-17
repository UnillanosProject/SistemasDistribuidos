/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Cliente;

import InfoSistema.InfoSistema;
import MD5.Md5;
import Servidor.Servidor;
import java.awt.Color;
import java.awt.HeadlessException;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import org.hyperic.sigar.SigarException;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

/**
 *
 * @author Arango Abello
 */
public class Cliente extends javax.swing.JFrame {

    /**
     * Creates new form Cliente
     */
//    private Servidor servidor;
    private String dirIP;
    private int numPuerto;
    private Md5 md5;
    private HiloProcess hiloProcess;
    private HiloConex hiloConex;
    private HiloLabel hiloLabel;
    private int inI,inJ;
    private long processAnt,processNew;
    private boolean Inicio;
    private Contador contador; 
    private InfoSistema info;
    private long procesadosTotales;
    
    public Cliente() {
        initComponents();
        this.setLocationRelativeTo(null);
        numPuerto=9999;
        md5=new Md5(this);
        hiloConex=new HiloConex(this);
        processNew=0;
        Inicio = true;
        info=new InfoSistema();
//        hiloProcess=new HiloProcess(this);
        }

public class HiloConex extends Thread{   
    
    Cliente cliente;
    DataInputStream in;
    DataOutputStream out;
    public ArrayList<String> CPUs;
    public ArrayList<String> RAMs;
        public HiloConex(Cliente cliente) {
            this.cliente = cliente;
            hiloLabel = new HiloLabel(cliente);
            CPUs=new ArrayList<>();
            RAMs=new ArrayList<>();
        }
    

    @Override
    public void run()
    {
            String mensajeOut,mensajeIn;
        //System.out.println(b.getInetAddress()+" Conectado...");
        try {
//          dirIP=JOptionPane.showInputDialog("Digite la IP del Servidor:");
                        contador=new Contador(cliente);
                        contador.start();
            Socket b = new Socket(dirIP,numPuerto);
                        contador.stop();
                        estado.setForeground(Color.BLUE);
            estado.setText("Conectado");
            //System.out.println(b.getInetAddress()+" Conectado...");
                        in = new DataInputStream(b.getInputStream());
            out = new DataOutputStream(b.getOutputStream());
                        out.writeUTF("Esperando clave");
                        out.writeUTF(Double.parseDouble(info.InfoSO()[2])+"");
                        mensajeIn = in.readUTF();
                        md5.setClaveMd5(mensajeIn);
                        labelMd5.setText(mensajeIn);
                        
            do{
                mensajeIn = in.readUTF();
                //System.out.println(b.getInetAddress()+" Dice: "+mensajeIn);
                                if (mensajeIn.equals("Iniciar")) {
                                    mensajeIn = in.readUTF();
                                    //System.out.println(b.getInetAddress()+" Dice: "+mensajeIn);
                                    String[] asignados=mensajeIn.split(",");
                                    inI=Integer.valueOf(asignados[0]);
                                    inJ=Integer.valueOf(asignados[1]);
                                    if(Inicio){
                                        hiloLabel=new HiloLabel(cliente);
                                        hiloLabel.setOut(out);
                                        hiloLabel.start();
                                    }
                                    Inicio = false;
                                    hiloProcess=new HiloProcess(cliente);
                                    hiloProcess.setOut(out);
                                    hiloProcess.start();
                                }
//              out.writeUTF(mensajeOut);
                                
//              if(mensajeOut.equals("No Encontrado")) break;
//              System.out.println("Clave: "+mensajeOut);
            }while(!mensajeIn.equals("Finalizar"));
                        
             //System.out.println("Procesamiento Finalizado");
                         JOptionPane.showMessageDialog(cliente, "La clave se ha encontrado", "Clave encontrada", JOptionPane.INFORMATION_MESSAGE);
                         grafico1.series1.clear();
                         grafico1.series2.clear();
                         botonConectar.setText("Conectar");
                         ipServidor.setText("");
                         estado.setForeground(new java.awt.Color(148, 47, 47));
                         estado.setText("Desconectado");
                         hiloProcess.stop();
                         hiloLabel.stop();
        } catch (Exception e) { }
        
    }
    
    public void avisarClaveEncontrada(String clave){
        try {
            out.writeUTF("Clave Encontrada");
            out.writeUTF(clave);
        } catch (IOException ex) {
        }
    }
    
    }

public class HiloProcess extends Thread{   
    
    Cliente cliente;
    DataOutputStream out;

        public HiloProcess(Cliente cliente) {
            this.cliente = cliente;
        }
    
    @Override
    public void run(){
           //System.out.println("Asignados: "+inI+","+inJ);
           md5.recorrer(inI,inJ);
           procesadosTotales=Integer.valueOf(labelProcesados.getText());
            try {
//                cliente.setTerminado(true);
                out.writeUTF("Terminado");
                this.finalize();
            } catch (Throwable ex) {
            }
        }

        public void setOut(DataOutputStream out) {
            this.out = out;
        }
    
}

    public void setProcesados(long procesados, String actual){
        labelProcesados.setText(""+(procesadosTotales+procesados));
        labelActual.setText(""+actual);
//        labelProcesados.setText(""+procesados+", "+actual);
        labelRestantes.setText(""+(1727604-procesados));
    }

    public HiloConex getHiloConex() {
        return hiloConex;
    }

public class HiloLabel extends Thread{
    DataOutputStream out;
    Cliente cliente;
        public HiloLabel(Cliente cliente) {
            this.cliente=cliente;
        }
    
    @Override
    public void run(){
        while(true){
        try {
            out.writeUTF("Procesando");
            processAnt=processNew;
            processNew=Long.valueOf(cliente.labelProcesados.getText());
            out.writeUTF((processNew-processAnt)+"");
            
            out.writeUTF("Estado");
            try {
                String[] infoUSO=info.InfoSOalt();
                out.writeUTF(infoUSO[0]);
                out.writeUTF(infoUSO[1]);
                    grafico1.tiempoActual=grafico1.tiempoActual+0.5;
                    info.InfoSO();
                    Double Ghz=Double.parseDouble(info.infoSO[2])/1024;
                    grafico1.añadirASerie1(grafico1.tiempoActual,(Double.parseDouble(infoUSO[0])*Ghz/100));
                
                    grafico1.tiempoActual=grafico1.tiempoActual+0.5;
                    grafico1.añadirASerie2(grafico1.tiempoActual,(Double.parseDouble(infoUSO[1])/1024));
                
            } catch (SigarException ex) {
            }
        } catch (IOException ex) {
        }
            try {
                Thread.sleep(500);
            } catch (InterruptedException ex) {
            }
        }
    }

        public void setOut(DataOutputStream out) {
            this.out = out;
        }
    
}
public class Contador extends Thread{
    int tiempo=0;
    Cliente cliente;

        public Contador(Cliente cliente) {
            this.cliente = cliente;
        }
    
    @Override
    public void run(){
        for (int i = 0; i < 6; i++) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
            }
            tiempo=tiempo+1000;
            //System.out.println(tiempo);
        }
        if (tiempo>=5000) {
            hiloConex.stop();
            hiloConex=new HiloConex(cliente);
            JOptionPane.showMessageDialog(cliente, "No se ha podido conectar al servidor", "Error de conexión", JOptionPane.ERROR_MESSAGE);
            botonConectar.setText("Conectar");
        }
    }
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel4 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        labelProcesados = new javax.swing.JLabel();
        labelRestantes = new javax.swing.JLabel();
        labelActual = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        labelClave = new javax.swing.JLabel();
        ipServidor = new javax.swing.JTextField();
        botonConectar = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        estado = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jPanel2 = new javax.swing.JPanel();
        grafico1 = new Cliente.Grafico();
        jPanel5 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        labelMd5 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("MD5 Decrypter - Client");

        jPanel4.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel3.setText("Procesados");

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel4.setText("Restantes Asignados");

        labelProcesados.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelProcesados.setText("0");
        labelProcesados.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        labelRestantes.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelRestantes.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        labelActual.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelActual.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        labelActual.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(labelProcesados, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(labelActual, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(labelRestantes, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(labelRestantes, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(labelProcesados)
                    .addComponent(labelActual, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel3)
                        .addComponent(jLabel4)))
                .addContainerGap())
        );

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        labelClave.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        labelClave.setText("IP Servidor");

        ipServidor.setToolTipText("Clave");

        botonConectar.setText("Conectar");
        botonConectar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonConectarActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel1.setText("Estado");

        estado.setForeground(new java.awt.Color(148, 47, 47));
        estado.setText("Desconectado");

        jSeparator1.setOrientation(javax.swing.SwingConstants.VERTICAL);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(labelClave)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(ipServidor)
                .addGap(18, 18, 18)
                .addComponent(botonConectar)
                .addGap(18, 18, 18)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(estado, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelClave)
                    .addComponent(ipServidor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(botonConectar)
                    .addComponent(jLabel1)
                    .addComponent(estado))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(jSeparator1, javax.swing.GroupLayout.Alignment.TRAILING)
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(grafico1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(grafico1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jPanel5.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel5.setText("Clave MD5");

        labelMd5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelMd5.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel5)
                .addGap(31, 31, 31)
                .addComponent(labelMd5, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(108, 108, 108))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(labelMd5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void botonConectarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonConectarActionPerformed
        if(botonConectar.getText().equals("Conectar")){
            dirIP=ipServidor.getText();
            hiloConex=new HiloConex(this);  
            hiloConex.start();
            botonConectar.setText("Desconectar");  
            return;
        }
        if(botonConectar.getText().equals("Desconectar")){
            estado.setForeground(Color.RED);
            estado.setText("Desconectado");
            hiloConex.stop();
            if (hiloLabel!=null) {
                hiloLabel.stop();
            }
            if (hiloProcess!=null) {
                hiloProcess.stop();
            }
            grafico1.series1.clear();
            grafico1.series2.clear();
//            ipServidor.setText("");
            Inicio=true;
            botonConectar.setText("Conectar");  
            return;
        }
    }//GEN-LAST:event_botonConectarActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton botonConectar;
    private javax.swing.JLabel estado;
    private Cliente.Grafico grafico1;
    private javax.swing.JTextField ipServidor;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JLabel labelActual;
    private javax.swing.JLabel labelClave;
    private javax.swing.JLabel labelMd5;
    private javax.swing.JLabel labelProcesados;
    private javax.swing.JLabel labelRestantes;
    // End of variables declaration//GEN-END:variables

static class Grafico extends JPanel {

    /**
     * 
     */
    private static final long serialVersionUID = -2318973151598624669L;
        XYSeries series1;
        XYSeries series2;
        double tiempoActual=0;
    /**
     * Creates a new demo.
     *
     * @param title  the frame title.
     */
    public Grafico() {

//        super(title);

        final XYDataset dataset = createDataset();
        final JFreeChart chart = createChart(dataset);
        final ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(560, 290));
//        setContentPane(chartPanel);
        add(chartPanel);
    }
    
    public void añadirASerie1(double x,double y){
        series1.add(x ,y );
    }
    public void añadirASerie2(double x,double y){
        series2.add(x ,y );
    }
    /**
     * Creates a sample dataset.
     * 
     * @return a sample dataset.
     */
    private XYDataset createDataset() {
        
        series1 = new XYSeries("CPU");

        series2 = new XYSeries("RAM");

        final XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(series1);
        dataset.addSeries(series2);
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
            "GHz - GB",                      // y axis label
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

//    public static void main(final String[] args) {
//
//        final LineChartDemo6 demo = new LineChartDemo6();
////        demo.pack();
////        RefineryUtilities.centerFrameOnScreen(demo);
//        demo.setVisible(true);
//
//    }

}
    
}
