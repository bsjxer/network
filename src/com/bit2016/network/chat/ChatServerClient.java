package com.bit2016.network.chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.util.Scanner;

public class ChatServerClient {
	private static final int SERVER_PORT = 5004;
	private static final String SERVER_IP = "192.168.1.4";
	
	public static void main(String[] args){
		Socket socket = null;
		Scanner scanner = null;
		
		try {
			socket = new Socket();
			scanner = new Scanner(System.in);
			
			// 클라이언스와 서버 연결
			socket.connect(new InetSocketAddress(SERVER_IP, SERVER_PORT));
			
			PrintWriter pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"), true);
			
			
			// 클라이언트 닉네임 설정			
			System.out.print("닉네임을 입력하세요:");
			String line = scanner.nextLine();
			pw.println( "JOIN:" + line);
			consoleLog("사용하실 닉네임은 " + line + "입니다.");
			
			// 클라이언트 메시지 수신
			Thread thread = new ChatReceiveThread(socket);
			thread.start();
			
			while( true ) {
				// 클라이언트 메시지 송신
				String content = scanner.nextLine();
				pw.println( "MESSAGE:" + content);
				
				if( "QUIT".equals(content)) {
					consoleLog("LEAVE:OK");
					break;
				}
			}
			

		} catch( SocketException ex ){
			consoleLog( "abnormal closed by client" );
		} catch( IOException ex ) {
			consoleLog( "error" + ex );
		} finally {
			try {
				if( scanner != null) {
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
	
	public static void consoleLog( String message ){
		System.out.println("[client]" + message);
	}
}
