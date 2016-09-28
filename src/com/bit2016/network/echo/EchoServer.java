package com.bit2016.network.echo;
  
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class EchoServer {
	private static final int PORT = 9000;
	
	public static void main(String[] args) {
		ServerSocket serverSocket = null;
		
		try {
			//1. Server Socket 생성
			String localHostAddress = InetAddress.getLocalHost().getHostAddress();
			serverSocket = new ServerSocket();
			
			//2. Binding
			serverSocket.bind( new InetSocketAddress( localHostAddress, PORT ) );
			log( "binding " + localHostAddress + ":" + PORT );
			
			while (true) {
				// 3. Accept
				Socket socket = serverSocket.accept();

				Thread thread = new EchoServerReceiveThread( socket );
				thread.start();
			}
			
		} catch( IOException ex ) {
			log( "error:" + ex );
		}
	}
	
	public static void log( String message ) {
		System.out.println( "[Echo Server]" + message );
	}

}