package Server;

import java.io.*;
import java.net.*;
import java.util.Scanner;
import javax.swing.*;
import MD5.Servidor;

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
                                DataInputStream in = new DataInputStream(b.getInputStream());
				mensajeIn = in.readUTF();
				System.out.println(b.getInetAddress()+"Dice: "+mensajeIn);
                                
                                Servidor servidor=new Servidor();
                                servidor.setClave(mensajeIn);

				mensajeOut = servidor.recorrer();
				out.writeUTF(mensajeOut);
                                
				if(mensajeOut.equals("No Encontrado")) break;
				System.out.println("Clave: "+mensajeOut);
			}while(!mensajeOut.equals("No Encontrado"));
			 System.out.println("Procesamiento Finalizado");
		} catch (Exception e) {	}
		
	}
	
	public static void main(String[] args) {
		new Servidores().conexiones();

	}

}
