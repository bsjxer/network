package com.bit2016.network.chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.SocketException;

public class ChatReceiveThread extends Thread {
	private Socket socket = null;
	
	public ChatReceiveThread ( Socket socket ) {
		this.socket = socket;
	}
	@Override
	public void run() {
		try {
			BufferedReader br = new BufferedReader( new InputStreamReader(socket.getInputStream(), "UTF-8"));
			
			while (true) {
				String content = br.readLine();
				System.out.println(content);
			}
			
		} catch (SocketException e) {
			ChatServerClient.consoleLog("error:" + e );
			System.exit( 0 );
		} catch (IOException e){
			ChatServerClient.consoleLog("error:" + e );
			System.exit( 0 );
		} 
	}
	
}
