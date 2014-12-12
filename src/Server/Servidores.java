package Server;

import java.io.*;
import java.net.*;
import java.util.Scanner;
import javax.swing.*;

public class Servidores {
	String dirIP;
	int numPuerto=9999;
	Scanner sDato = new Scanner(System.in);
	
	public void conexiones()
	{	String mensajeOut,mensajeIn;
		System.out.println("Conectandose al Servidor.... Abriendo Puerto"+numPuerto);
		try {
			dirIP=JOptionPane.showInputDialog("Digite la IP del Servidor:");
			Socket b = new Socket(dirIP,numPuerto);
			System.out.println("Conectado!");
			
			DataOutputStream out = new DataOutputStream(b.getOutputStream());
			do{
				System.out.println("Digite el mensaje a enviar:");
				mensajeOut = sDato.nextLine();
				out.writeUTF(mensajeOut);
				if(mensajeOut.equals("fin")) break;
				System.out.println("Mensaje ["+mensajeOut+"] Enviado!");
				System.out.println("Esperando Respuesta!...");
				DataInputStream in = new DataInputStream(b.getInputStream());
				mensajeIn = in.readUTF();
				System.out.println(b.getInetAddress()+"Dice: "+mensajeIn);
				
			}while(!mensajeOut.equals("fin"));
			 System.out.println("Cerro sesion");
		} catch (Exception e) {	}
		
	}
	
	public static void main(String[] args) {
		new Servidores().conexiones();

	}

}
