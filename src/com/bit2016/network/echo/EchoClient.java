package com.bit2016.network.echo;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Scanner;

public class EchoClient {
	private static final String SERVER_IP = "192.168.1.5";
	private static final int SERVER_PORT = 5000;

	public static void main(String[] args) {
		Socket socket = null;
		try {
			// 1. socket 생성
			socket = new Socket();			
			
			// 2. 서버연결
			socket.connect(new InetSocketAddress(SERVER_IP, SERVER_PORT));			

			// 3. IO Stream 받아오기
			InputStream inputStream = socket.getInputStream();
			OutputStream outputStream = socket.getOutputStream();
	
			while( true ){
				// 4. 쓰기
				Scanner scanner = new Scanner(System.in);
				System.out.print(">>");
				String str = scanner.nextLine();
				outputStream.write(str.getBytes("UTF-8"));
	
				// 5. 읽기
				byte[] buffer = new byte[256];
				int readByteCount = inputStream.read( buffer );
				if( readByteCount == -1 ){
					System.out.println("[Client] closed by server");
					return;
				}
				String data = new String(buffer, 0, readByteCount, "UTF-8");
				if( data.equals("exit") ) {
					break;
				}
				System.out.println("<<" + data);
			}

		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			try {
				if (socket != null & socket.isClosed() == false) {
					socket.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}
}