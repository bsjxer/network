package com.bit2016.network.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UDPTimeServer {
	public static final int PORT = 8880;
	private static final int BUFFER_SIZE = 1024;

	public static void main(String[] args) {
		DatagramSocket socket = null;
		
		try{
			// 1. 소켓 생성
			socket = new DatagramSocket(PORT);
			String date = "";
		
			while (true) {
				// 2. 데이터 수신
				DatagramPacket receivePacket = new DatagramPacket(new byte[BUFFER_SIZE], BUFFER_SIZE);
				socket.receive(receivePacket); // blocking
				
				String message = new String(receivePacket.getData(), 0, receivePacket.getLength(), "UTF-8");
				System.out.println("[server] received:" + message);

				// 3. 날짜 생성
				Date now = new Date();
				String dateMessage = saveDate(now) + message;
				System.out.println(dateMessage);
				
				// 4. 데이터 송신
				byte[] sendData = dateMessage.getBytes();
				DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, receivePacket.getAddress(),
						receivePacket.getPort());
				socket.send(sendPacket);
			}
		
		} catch( SocketException e ){
			e.printStackTrace();
		} catch ( IOException e ){
			e.printStackTrace();
		} finally {
			if (socket != null && socket.isClosed() == false) {
				socket.close();
			}
		}
	}
	
	public static String saveDate( Date now ){
		SimpleDateFormat sdf = new SimpleDateFormat( "[yyyy년 MM월 dd일 hh시 mm분 ss초]:" );
		return sdf.format( now );
	}

}
