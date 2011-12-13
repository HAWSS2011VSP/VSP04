package connect;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class UDP_Sender {

	private DatagramSocket socket;
	private int port;
	
	public UDP_Sender(int port) {
		this.port = port;
		
		try {
			socket = new DatagramSocket();
		} catch (SocketException e) {
			e.printStackTrace();
		}
	}
	
	public void send(byte[] data) {
		
		DatagramPacket sendPacket;
		try {
			sendPacket = new DatagramPacket(data, data.length, InetAddress.getByName("localhost"),port);
			socket.send(sendPacket);
			System.out.println("DataSink --send--> "+new String(data) );
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
