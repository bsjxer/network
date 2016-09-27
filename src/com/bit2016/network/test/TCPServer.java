package com.bit2016.network.test;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class TCPServer {
	private static final int PORT = 5000;

	public static void main(String[] args) {
		ServerSocket serverSocket = null;
		try {
			// 1. 서버소켓 생성
			serverSocket = new ServerSocket();								

			// 2. binding( 소켓에 소켓주소(IP + Port)를 바인딩한다.)
			InetAddress inetAddress = InetAddress.getLocalHost();			// 호스트의 정보(IP등등)들을 inetAddress에 저장
			String hostAddress = inetAddress.getHostAddress();				// 호스트 IP주소만 따로 저장 (바인딩용)
																					// 서버소켓의 메소드 바인드 이용. 새로운 소켓생성( IP, 포트 사용)
			serverSocket.bind( new InetSocketAddress( hostAddress, PORT) );			//IP와 포트가 포함된 소케 address/인터넷상에 소켓 Address를 등록하는 과정. 그래야 connect찌르는 사람이 확인 가능
			System.out.println( "[server] binding " + hostAddress + ":" + PORT);	// 바인드가 완료됨으로써, 내용 출력
			
			// 3. accept( 클라이언트로 부터 연결요청을 기다린다. )
			Socket socket = serverSocket.accept(); // block
			
			InetSocketAddress inetSocketAddress = (InetSocketAddress)socket.getRemoteSocketAddress();
			InetAddress inetRemoteHostAddress = inetSocketAddress.getAddress();
			String remoteHostAddress = inetRemoteHostAddress.getHostAddress();
//			String remoteHostAddress = inetSocketAddress.getAddress().getHostAddress();
			int remoteHostPort = inetSocketAddress.getPort();
			
			System.out.println( "[server] conneted by client[" + remoteHostAddress + ":" + remoteHostPort + "]");
			
			try {
				// 4. IOStream 받아오기
				InputStream inputStream = socket.getInputStream();
				OutputStream outputStream = socket.getOutputStream();

				while (true) {
					// 5. 데이터 읽기
					byte[] buffer = new byte[256];
					int readByteCount = inputStream.read(buffer);	//block		// 조금만 씌여도 return해줘야 하기때문에 사이즈를 알아야함
					if (readByteCount == -1) {
						// 정상종료( remote socket close() 불러서 정상적으로 소켓을 닫았다 )
						System.out.println("[server] closed by client");
						break;
					}
					String data = new String(buffer, 0, readByteCount, "UTF-8");
					System.out.println("[server] received:" + data);

					// 6. 쓰기
					outputStream.write(data.getBytes("UTF-8"));
				}
			} catch ( SocketException ex ) {
				System.out.println( "[server] abnormal closed by client");
			} catch (IOException ex) {
				ex.printStackTrace();
			} finally {
				try {
					// 7. 자원정리( 소켓 닫기 )
					socket.close();
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if( serverSocket != null && serverSocket.isClosed() == false) {
					serverSocket.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
