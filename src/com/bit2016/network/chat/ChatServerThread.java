package com.bit2016.network.chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.List;

public class ChatServerThread extends Thread {
	private Socket socket;
	private String name;
	private List<PrintWriter> listPrintWriter;
	
	public ChatServerThread( Socket socket, List<PrintWriter> listPrintWriter){
		this.socket = socket;
		this.listPrintWriter = listPrintWriter;
		
	}
	@Override
	public void run() {
		try {
			// 1. print remote socket address
			InetSocketAddress remoteSocketAddress = (InetSocketAddress)socket.getRemoteSocketAddress();
			ChatServer.consoleLog( "connected by client[" + remoteSocketAddress.getAddress().getHostAddress() + ":" + remoteSocketAddress.getPort() + "]");
			
			// 2. Create Stream ( from Basic Stream )
			BufferedReader br = new BufferedReader( new InputStreamReader(socket.getInputStream(), "UTF-8"));
			PrintWriter pw = new PrintWriter ( new OutputStreamWriter(socket.getOutputStream(), "UTF-8"), true);
			
			// 3. processing...
			while(true){
				String line = br.readLine();
				if( line == null ){
//					doQuitNoName();					//닉네임을 입력하시오 . 응답 해보자
					break;
				}
				
				String[] tokens = line.split( ":" );
				if( "JOIN".equals(tokens[0]) ){		//equals.. equalsIgnoreCase 대소문자 구분
					doJoin(tokens[1], pw);
				} else if( "MESSAGE".equals(tokens[0])) {
					doMessage(tokens[1]);
				} else if ( "QUIT".equals(tokens[0])) {
					doQuit(pw);
				}
			}
		
		} catch (UnsupportedEncodingException e) {
			ChatServer.consoleLog("error:" + e );
		} catch (IOException e) {
			ChatServer.consoleLog("error:" + e );
		} try {
			if( socket != null && socket.isClosed() == false ){
				socket.close();
			}
		} catch ( IOException e ){
			ChatServer.consoleLog("error:" + e );
		}
		
	}
	
	private void doJoin(String name, PrintWriter printWriter){
		// 1. save nickname
		this.name = name;
		
		// 2. broadcasting..
		String message = name + "님이 입장했습니다.";
		broadcastMessage( message );
		
		// 3. add PrintWriter
		addPrintWriter( printWriter );
		
		// 4. ack
//		printWriter.println( "JOIN:OK");
		
	}
	
	private void doMessage(String message){		//doMessage? 바로 broadcastMessage 쓰면 안되나?
		String content = this.name + " : " + message;
		broadcastMessage( content );
	}
	
	private void doQuit(PrintWriter printWriter ) {
		// 1. delete PrintWriter
		deletePrintWriter( printWriter );
		
		// 2. leave message
		String message = name + "님이 퇴장했습니다.";
		broadcastMessage( message );
	}
	
	private void doQuitNoName() {
	}
	
	private void addPrintWriter( PrintWriter printWriter ) {
		synchronized( listPrintWriter ) {			// 동기화 시간이 필요. 쓰레드이기 때문에 다른 곳에서 이 함수를 실행할때, 동기화가 진행중이면 잠을 잠(대기), 동기화가 끝나면 진입
			listPrintWriter.add( printWriter );
		}
	}
	
	private void deletePrintWriter( PrintWriter printWriter ) {
		synchronized( listPrintWriter ) {			
			listPrintWriter.remove( printWriter );
		}
	}
	
	private void broadcastMessage( String message ){
		synchronized( listPrintWriter ) {			
			for( PrintWriter printWriter : listPrintWriter ) {
				printWriter.println(message);
			}
		}
	}
	
	
}
