package Servidor;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;


public class HilosConex extends Thread{
    private final Socket socketAlt;
    private DataInputStream in;
    private DataOutputStream out;
    static int numCliente;
        static String clave;
        private Asignacion asig;
        private Servidor servidor;
        private Contador contador;
        public ArrayList<String> CPUs;
        public ArrayList<String> RAMs;
        public String CPUactual="0";
        public String RAMactual="0";
        public String ip;
        public double cpuTotal=0;
        long vel;
    
    public HilosConex(Servidor servidor,Socket socketAlt, int nC, String claveAux,Asignacion asig){
        this.servidor=servidor;
                this.socketAlt = socketAlt;
        numCliente = nC;
                clave=claveAux;
                this.asig=asig;
                CPUs=new ArrayList<>();
                RAMs=new ArrayList<>();
                contador=new Contador();
        try{
            in = new DataInputStream(socketAlt.getInputStream());
            out = new DataOutputStream(socketAlt.getOutputStream());
        }catch(IOException e){}
    }
    
        @Override
    public void run(){
        String mensaje="";
                ip=socketAlt.getInetAddress().toString().substring(1);
                servidor.getSelector1().addItem(ip);
                servidor.getSelector2().addItem(ip);
                //System.out.println(ip+" Conectado...");
        try{
                    while(!mensaje.equals("fin")){
                        contador=new Contador();
                        contador.start();
                        mensaje = in.readUTF();
                        contador.stop();
                        //System.out.println("Cliente ["+numCliente+"] dice: ["+mensaje+"]");
                        if (mensaje.equals("Esperando clave")) {
                            mensaje = in.readUTF();
                            cpuTotal=Double.parseDouble(mensaje);
                            out.writeUTF(clave);
                            out.writeUTF("Iniciar");
                            asig.setAsignado(true);
                            out.writeUTF(asig.getInI()+","+asig.getInJ());
                        }
                        if(mensaje.equals("Procesando")){
                            mensaje = in.readUTF();
                            aumProcess(Long.valueOf(mensaje));
                        }  
                        if(mensaje.equals("Estado")){
                            CPUactual = in.readUTF();
                            RAMactual = in.readUTF();
                            actInfo();
                        } 
                        if (mensaje.equals("Terminado")) {
                            servidor.removerDeArray(asig);
                            asig=servidor.asignar();
                            asig.setAsignado(true);
                            out.writeUTF("Iniciar");
                            out.writeUTF(asig.getInI()+","+asig.getInJ());
                        }
                        if (mensaje.equals("Clave Encontrada")) {
                            mensaje = in.readUTF();
                            servidor.getHiloSocket().claveEncontrada(mensaje);
                        } 
//                        Servidor servidor=new Servidor();
//                        servidor.setClave(mensaje);
//                        String clave = servidor.recorrer();
//                        out.writeUTF(clave);
                        
                    }
                    //System.out.println("Cliente ["+numCliente+"] se Desconecto!!");
                    numCliente--;
                    //System.out.println("Clientes "+numCliente);
                    socketAlt.close();
        }catch(IOException e){}
    }
        
        public void aumProcess(long sumando){
            vel=sumando;
            long nuevoValor=(Long.valueOf(servidor.getLabelProcesados().getText())+sumando);
            servidor.getLabelProcesados().setText(nuevoValor+"");            
            servidor.getLabelRestantes().setText(""+(Long.valueOf(servidor.getLabelRestantes().getText())-sumando));
        }
        
        public void actInfo(){
//            System.out.println("CPU:"+CPUactual+" RAM:"+RAMactual);
            CPUs.add(this.CPUactual);
            RAMs.add(this.RAMactual);
        }
        
        public void finalizar(){
            try {
                out.writeUTF("Finalizar");
//                try {
//                    this.finalize();
//                } catch (Throwable ex) {
//                }
            } catch (IOException ex) {
            }
        }

public class Contador extends Thread{
    int tiempo=0;
    @Override
    public void run(){
        for (int i = 0; i < 30; i++) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
            }
            tiempo=tiempo+100;
            //System.out.println(tiempo);
        }
        if (tiempo>=3000) {
            //System.out.println(ip+" Desconectado...");
            vel=0;
            CPUactual="0";
            RAMactual="0";
            asig.setAsignado(false);
            servidor.disminuirConectados();
//            Object[] objetosSelector1=
            for (int i = 0; i < servidor.getSelector1().getItemCount(); i++) {
                if (servidor.getSelector1().getItemAt(i).toString().equals(ip)) {
                    servidor.getSelector1().removeItemAt(i);
                    servidor.graficoPequeño1.series1.clear();
                }
                if (servidor.getSelector2().getItemAt(i).toString().equals(ip)) {
                    servidor.getSelector2().removeItemAt(i);
                    servidor.graficoPequeño2.series1.clear();
                }
            }
        }
    }
}
}
