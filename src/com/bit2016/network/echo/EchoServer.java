package com.bit2016.network.echo;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class EchoServer {
	private static final int PORT = 5000;

	public static void main(String[] args) {
		// 1. 서버소켓 생성
		ServerSocket serverSocket = null;

		try {
			// 2. binding( 소켓에 소켓주소(IP + Port)를 바인딩한다.)
			serverSocket = new ServerSocket();
			InetAddress inetaddress = InetAddress.getLocalHost();
			String hostaddress = inetaddress.getHostAddress();

			// serverSocket.bind(hostaddress, PORT);
			serverSocket.bind(new InetSocketAddress(hostaddress, PORT));
			// System.out.println("[server] binding " + hostaddress + ":" +
			// PORT);

			// 3. accept( 클라이언트로 부터 연결요청을 기다린다. )
			System.out.println("[서버] 연결 기다림");
			Socket socket = serverSocket.accept();
			System.out.println("[서버] 연결됨 from " + hostaddress + ":" + PORT);

			try {
				// 4. IOStream 받아오기
				InputStream inputStream = socket.getInputStream();
				OutputStream outputStream = socket.getOutputStream();

				while (true) {
					// 5. 데이터 읽기
					byte[] buffer = new byte[256];
					int readByteCount = inputStream.read(buffer);
					if (readByteCount == -1) {
						System.out.println("[서버] 클라이언트로 부터 연결끊김");
						break;
					}

					String data = new String(buffer, 0, readByteCount, "UTF-8");
					System.out.println("[서버] 데이터 수신 : " + data);

					// 6. 쓰기
					outputStream.write(data.getBytes("UTF-8"));
				}

				// 7. 자원정리( 소켓 닫기 )
			} catch (SocketException ex) {
				System.out.println("[server] abnormal closed by client");
			} catch (IOException ex) {
				ex.printStackTrace();
			} finally {
				try {
					socket.close();
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (serverSocket != null && serverSocket.isClosed() == false) {
					serverSocket.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}