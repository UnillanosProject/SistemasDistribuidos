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
	
	public HilosConex(Socket socketAlt, int nC, String claveAux){
		this.socketAlt = socketAlt;
		numCliente = nC;
                clave=claveAux;
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
                        out.writeUTF(clave);
//                        mensaje = in.readUTF();
//                        System.out.println("Cliente ["+numCliente+"] dice: ["+mensaje+"]");
//                        Servidor servidor=new Servidor();
//                        servidor.setClave(mensaje);
//                        String clave = servidor.recorrer();
//                        out.writeUTF(clave);
                        out.writeUTF("Iniciar");
                        out.writeUTF("");
                    }
                    System.out.println("Cliente ["+numCliente+"] se Desconecto!!");
                    numCliente--;
                    System.out.println("Clientes "+numCliente);
                    socketAlt.close();
		}catch(IOException e){}
	}
}
