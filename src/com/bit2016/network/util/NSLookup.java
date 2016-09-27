package com.bit2016.network.util;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class NSLookup {

	public static void main(String[] args) {
		try {
			String hostname = "www.naver.com";
			InetAddress[] inerAddresses = InetAddress.getAllByName(hostname);
			
			for( InetAddress inetAddress : inerAddresses) {
				System.out.println( inetAddress.getHostAddress() );
			}
		} catch (UnknownHostException e){
			e.printStackTrace();
		}
		
		
	}

}
