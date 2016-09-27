package com.bit2016.network.test;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class LocalHost {

	public static void main(String[] args) {
		try {
			InetAddress inetAddress = InetAddress.getLocalHost();
			
			String hostName = inetAddress.getHostName();
			String hostAddress = inetAddress.getHostAddress();
			System.out.println( hostName + ":" + hostAddress);
			
			byte[] addresses = inetAddress.getAddress();
			for( byte address : addresses ){
				System.out.print( address );
				System.out.print( ".");
			}
		}catch ( UnknownHostException e){
			e.printStackTrace();
		}
		
		
		
	}

}
