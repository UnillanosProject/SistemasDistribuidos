import java.io.*;
import java.net.*;
import java.util.Scanner;
import javax.swing.*;

public class servertcp {
	int numPuerto=1710;
	String mensajeIn;
	Scanner sDato = new Scanner(System.in);
    public void conexiones(){
    	
		System.out.println("Iniciando el Servidor .... Abriendo puerto");
		try{	
			ServerSocket ss = new ServerSocket(numPuerto);
			System.out.println("Esperando conexiones");
			Socket a = ss.accept();
			System.out.println("Se conecto un cliente ");
			DataInputStream in = new DataInputStream(a.getInputStream());
			DataOutputStream out = new DataOutputStream(a.getOutputStream());
			do {
		        mensajeIn = in.readUTF();		
				System.out.println(a.getInetAddress()+" dice: "+ mensajeIn);
				if(mensajeIn.equals("bye")) break;
				System.out.println("Digite el mensaje a enviar: ");
				String mensajeOut = sDato.nextLine();
	     		out.writeUTF(mensajeOut);
		    	System.out.println("mensaje ["+mensajeOut+"]Enviado!");
			}while (!mensajeIn.equals("bye"));
			a.close();
			System.out.println("Se Desconecto el Cliente");
		}catch (Exception e){}
		
	}
    public static void main(String [] args){
    	new servertcp().conexiones();
    }
}
