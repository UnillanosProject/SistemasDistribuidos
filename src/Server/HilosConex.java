package Server;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.*;

import MD5.Servidor;

public class HilosConex extends Thread{
	private Socket socketAlt;
	private DataInputStream in;
	private DataOutputStream out;
	static int numCliente;
	
	public HilosConex(Socket socketAlt, int nC){
		this.socketAlt = socketAlt;
		numCliente = nC;
		try{
			in = new DataInputStream(socketAlt.getInputStream());
			out = new DataOutputStream(socketAlt.getOutputStream());
		}catch(Exception e){}
	}
	
	public void run(){
		String mensaje="";
		try{
                    while(!mensaje.equals("fin")){
                        out.writeUTF("3c1ae9e7d34027464bf6396a888c7de4");
                        mensaje = in.readUTF();
                        System.out.println("Cliente ["+numCliente+"] dice: ["+mensaje+"]");
//                        Servidor servidor=new Servidor();
//                        servidor.setClave(mensaje);
//                        String clave = servidor.recorrer();
//                        out.writeUTF(clave);
                        out.writeUTF("El servidor confirma la recepcion");
                    }
                    System.out.println("Cliente ["+numCliente+"] se Desconecto!!");
                    numCliente--;
                    System.out.println("Clientes "+numCliente);
                    socketAlt.close();
		}catch(Exception e){}
	}
}
