package connect;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class UDP_Receiver extends Thread {

	private ListenEvent listenEvent;
	private DatagramSocket socket;
	
	public UDP_Receiver(int port) {
		try {
			socket = new DatagramSocket(port);
		} catch (SocketException e) {
			e.printStackTrace();
		}
	}
	
	public void setListenEvent(ListenEvent listenEvent) {
		this.listenEvent = listenEvent;
	}
	
	@Override
	public void run() {
		if(socket == null){
			System.err.println("Socket is null!");
			return;
		}
		
		byte[] receiveData = new byte[24];
		
		while(!interrupted()){
			DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
	        try {
				socket.receive(receivePacket);
			} catch (IOException e) {
				e.printStackTrace();
			}
	        byte[] data = receivePacket.getData();
	        String sentence = new String( data );
	        System.out.println("RECEIVED-UDP: " + sentence);
	        listenEvent.listen(data);
		}
		
		socket.close();
	}

}
