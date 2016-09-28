package com.bit2016.network.echo;
  
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.util.Scanner;

public class EchoClient {
	private static final String SERVER_IP = "192.168.1.5";
	private static final int SERVER_PORT = 9000;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		Scanner scanner = null;
		Socket socket = null;
		
		try{
			//1. Scanner 생성(표준입력 연결)
			scanner = new Scanner( System.in );
			
			//2. Socket 생성
			socket = new Socket();
			
			//3. connect to server
			socket.connect( new InetSocketAddress( SERVER_IP, SERVER_PORT ) );
			
			//4. IOStream 생성(받아오기)
			BufferedReader br = new BufferedReader( new InputStreamReader( socket.getInputStream(), "UTF-8" ) );
			PrintWriter pw = new PrintWriter( new OutputStreamWriter( socket.getOutputStream(), "UTF-8" ), true );
			
			while( true ) {
				//5. 입력 받기
				System.out.print( ">>" );
				String line = scanner.nextLine();
				if( "quit".equals( line ) ) {
					break;
				}
				
				//6. 송신
				pw.println( line );
			
				//7. 수신
				String data = br.readLine();
				if( data == null ) {
					log( "closed by server" );
					break;
				}
				
				//8. 출력
				System.out.println( "<<" + data );
			}
		} catch( SocketException ex ){
			log( "abnormal closed by client" );
		} catch( IOException ex ) {
			log( "error" + ex );
		} finally {
			try {
				if( scanner != null ) {
					scanner.close();
				}
				if( socket != null && socket.isClosed() == false ) {
					socket.close();
				}
			} catch( IOException ex ) {
				ex.printStackTrace();
			}
		}
	}

	private static void log( String message ) {
		System.out.println( "[Echo Client]" + message );
	}
}