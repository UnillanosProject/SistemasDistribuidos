package Server;

import java.io.*;
import java.net.*;

public class Proyecto {
	public static void main(String[] args){
		ServerSocket socketServ;
		System.out.println("Esperando conexiones");
		try{
			socketServ = new ServerSocket(9999);
			while(HilosConex.numCliente < 10){
				Socket socket;
				socket = socketServ.accept();
				HilosConex.numCliente++;
				System.out.println("Se conecto cliente "+HilosConex.numCliente);
				((HilosConex)new HilosConex(socket,HilosConex.numCliente)).start();
			}
		}catch(Exception e){}
	}
}