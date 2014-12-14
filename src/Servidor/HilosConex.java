package Servidor;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.*;


public class HilosConex extends Thread{
	private final Socket socketAlt;
	private DataInputStream in;
	private DataOutputStream out;
	static int numCliente;
        static String clave;
        private Asignacion asig;
        private Servidor servidor;
	
	public HilosConex(Servidor servidor,Socket socketAlt, int nC, String claveAux,Asignacion asig){
		this.servidor=servidor;
                this.socketAlt = socketAlt;
		numCliente = nC;
                clave=claveAux;
                this.asig=asig;
		try{
			in = new DataInputStream(socketAlt.getInputStream());
			out = new DataOutputStream(socketAlt.getOutputStream());
		}catch(IOException e){}
	}
	
        @Override
	public void run(){
		String mensaje="";
		try{
                    while(!mensaje.equals("fin")){
                        mensaje = in.readUTF();
                        System.out.println("Cliente ["+numCliente+"] dice: ["+mensaje+"]");
                        if (mensaje.equals("Esperando clave")) {
                            out.writeUTF(clave);
                            out.writeUTF("Iniciar");
                            asig.setAsignado(true);
                            out.writeUTF(asig.getInI()+","+asig.getInJ());
                        }
                        if (mensaje.equals("Terminado")) {
                            servidor.removerDeArray(asig);
                            asig=servidor.asignar();
                            asig.setAsignado(true);
                            out.writeUTF("Iniciar");
                            out.writeUTF(asig.getInI()+","+asig.getInJ());
                        }
                        
//                        Servidor servidor=new Servidor();
//                        servidor.setClave(mensaje);
//                        String clave = servidor.recorrer();
//                        out.writeUTF(clave);
                        
                    }
                    System.out.println("Cliente ["+numCliente+"] se Desconecto!!");
                    numCliente--;
                    System.out.println("Clientes "+numCliente);
                    socketAlt.close();
		}catch(IOException e){}
	}
}
