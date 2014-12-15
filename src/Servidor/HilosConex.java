package Servidor;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.*;
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
        int vel;
	
	public HilosConex(Servidor servidor,Socket socketAlt, int nC, String claveAux,Asignacion asig){
		this.servidor=servidor;
                this.socketAlt = socketAlt;
		numCliente = nC;
                clave=claveAux;
                this.asig=asig;
                contador=new Contador();
		try{
			in = new DataInputStream(socketAlt.getInputStream());
			out = new DataOutputStream(socketAlt.getOutputStream());
		}catch(IOException e){}
	}
	
        @Override
	public void run(){
		String mensaje="";
                System.out.println(socketAlt.getInetAddress()+" Conectado...");
		try{
                    while(!mensaje.equals("fin")){
                        contador=new Contador();
                        contador.start();
                        mensaje = in.readUTF();
                        contador.stop();
                        //System.out.println("Cliente ["+numCliente+"] dice: ["+mensaje+"]");
                        if (mensaje.equals("Esperando clave")) {
                            out.writeUTF(clave);
                            out.writeUTF("Iniciar");
                            asig.setAsignado(true);
                            out.writeUTF(asig.getInI()+","+asig.getInJ());
                        }
                        if(mensaje.equals("Procesando")){
                            mensaje = in.readUTF();
                            aumProcess(Integer.valueOf(mensaje));
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
        
        public void aumProcess(int sumando){
            vel=sumando;
            servidor.getLabelProcesados().setText((Long.valueOf(servidor.getLabelProcesados().getText())+sumando)+"");
            servidor.getLabelRestantes().setText((Long.valueOf(servidor.getLabelRestantes().getText())-(Long.valueOf(servidor.getLabelProcesados().getText())))+"");
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
            System.out.println(socketAlt.getInetAddress()+" Desconectado...");
            vel=0;
            asig.setAsignado(false);
            servidor.disminuirConectados();
        }
    }
}
}
