package com.bit2016.network.http;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.file.Files;

public class RequestHandler extends Thread {
	private Socket socket;
	
	public RequestHandler( Socket socket ) {
		this.socket = socket;
	}
	
	@Override
	public void run() {
		try {
			// logging Remote Host IP Address & Port
			InetSocketAddress inetSocketAddress = ( InetSocketAddress )socket.getRemoteSocketAddress();
			consoleLog( "connected from " + inetSocketAddress.getAddress().getHostAddress() + ":" + inetSocketAddress.getPort() );

			// get IOStream
			OutputStream outputStream = socket.getOutputStream();
			BufferedReader br = new BufferedReader( new InputStreamReader( socket.getInputStream(), "UTF-8" ) );
			
			String request = null;				// 첫번째 라인 분리하는 작업. GET 부분
			while( true ) {
				String line = br.readLine();
				// 브라우저가 연결을 끊으면
				if( line == null ){
					break;
				}
				
				// Header 만 읽음
				if("".equals( line ) ) {
					break;
				}
				
				if( request == null ){
					request = line;
				}
			}	
			
			consoleLog( request );
			String[] tokens = request.split( " " );
			if( "GET".equals(tokens[0]) == true ) {
				responseStaticResources( outputStream, tokens[1], tokens[2]);  
			} else if ( "POST".equals(tokens[0]) == true || "DELETE".equals(tokens[0]) == true ||
					"PUT".equals(tokens[0]) == true || "Etc".equals(tokens[0]) == true) { // POST, DELETE, PUT, Etc 명령들은 400 Bad Request
				response400Error ( outputStream, tokens[2] );
			}
				
			
			// 예제 응답입니다.
			// 서버 시작과 테스트를 마친 후, 주석 처리 합니다.
			//outputStream.write( "HTTP/1.1 200 OK\r\n".getBytes( "UTF-8" ) );
			//outputStream.write( "Content-Type:text/html; charset=utf-8\r\n".getBytes( "UTF-8" ) );
			//outputStream.write( "\r\n".getBytes() );
			//outputStream.write( "<h1>이 페이지가 잘 보이면 실습과제 SimpleHttpServer를 시작할 준비가 된 것입니다.</h1>".getBytes( "UTF-8" ) );
			
		} catch( Exception ex ) {
			consoleLog( "error:" + ex );
		} finally {
			// clean-up
			try{
				if( socket != null && socket.isClosed() == false ) {
					socket.close();
				}
				
			} catch( IOException ex ) {
				consoleLog( "error:" + ex );
			}
		}			
	}
	
	public void responseStaticResources( OutputStream outputStream, String url, String protocol ) throws IOException {
		System.out.println(url + ":" + protocol);
		if( "/".equals( url ) == true ) {
			// Welcome(defualt) File
			url = "/index.html";
		}
		
		File file = new File( "./webapp" + url );
		if( file.exists() == false ) {
			response404Error( outputStream, protocol );
			return;
		}
		
		//nio
		byte[] body = Files.readAllBytes( file.toPath() );
		String contentType = Files.probeContentType( file.toPath() );
		
		//	응답(response)
		outputStream.write( (protocol +" 200 OK\r\n").getBytes( "UTF-8" ) );
		outputStream.write( ("Content-Type:" + contentType + "; charset=utf-8\r\n").getBytes( "UTF-8" ) );
		outputStream.write( "\r\n".getBytes() );
		outputStream.write( body );
	}
	
	public void response404Error( OutputStream outputStream, String protocol ) throws IOException {
		
		File file = new File( "./webapp/error/404.html");
		
		byte[] body = Files.readAllBytes( file.toPath() );
		String contentType = Files.probeContentType( file.toPath() );
		// HTTP/1.1 404 File Not Found
		// Content-Type:text/html; charset=utf-8
		//
		// html(./webapp/error/404.html)
		outputStream.write( (protocol + " 200 OK\r\n").getBytes( "UTF-8") );
		outputStream.write( ("Content-Type:" + contentType + "; charset=utf-8\r\n").getBytes("UTF-8"));
		outputStream.write( "\r\n".getBytes("UTF-8") );
		outputStream.write( body );
		
		System.out.println( "404 error 응답할 것 [과제]");
	}
	
	public void response400Error ( OutputStream outputStream, String protocol ) throws IOException {

		File file = new File( "./webapp/error/400.html");
		
		byte[] body = Files.readAllBytes( file.toPath() );
		String contentType = Files.probeContentType( file.toPath() );
		// HTTP/1.1 400 File Not Found
		// Content-Type:text/html; charset=utf-8
		//
		// html(./webapp/error/400.html)
		outputStream.write( (protocol + " 200 OK\r\n").getBytes( "UTF-8") );
		outputStream.write( ("Content-Type:" + contentType + "; charset=utf-8\r\n").getBytes("UTF-8"));
		outputStream.write( "\r\n".getBytes("UTF-8") );
		outputStream.write( body );
		
		System.out.println( "400 error 응답할 것 [과제]");
	}
	
	public void consoleLog( String message ) {
		System.out.println( "[RequestHandler#" + getId() + "] " + message );
	}
}
