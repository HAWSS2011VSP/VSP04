package connect;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;

public class MulticastReceiver extends Thread{

	private ListenEvent listenEvent;
	private String iAddr;
	private int port;
	private ByteBuffer buffer33 = ByteBuffer.allocate(33);
	private Frame frame;
	
	public MulticastReceiver(String iAddr, int port, Frame frame) {
		this.iAddr = iAddr;
		this.port = port;
		this.frame = frame;
	}
	
	public void setListenEvent(ListenEvent listenEvent) {
		this.listenEvent = listenEvent;
	}
	
	@Override
	public void run() {
		MulticastSocket socket;
		try {
			socket = new MulticastSocket(port);
		} catch (IOException e) {
			System.err.println("Port is in use!");
			return;
		}
		
		InetAddress group;
		try {
			group = InetAddress.getByName(iAddr);
			socket.joinGroup(group);
		} catch (UnknownHostException e) {
			System.err.println("Unknown Host!");
			socket.close();
			return;
		} catch (IOException e) {
			System.err.println("Counldn't join group!");
			socket.close();
			return;
		}
		

		try{
			DatagramPacket packet;
			while(!interrupted()) {
			    byte[] buf = new byte[33];
			    packet = new DatagramPacket(buf, buf.length);
			    socket.receive(packet);
	
			    byte[] packetData = packet.getData();
			    String received = new String(packetData);
			    System.out.println("MulticastReceiver: " + received);
			    
			    buffer33.position(0);
			    buffer33.put(packetData);
			    
			    buffer33.position(23);
			    frame.setBlockAndTime(buffer33.get(), buffer33.getLong());
			    
			    listenEvent.listen(packetData);
			}
			socket.leaveGroup(group);
		}catch (IOException e) {
			System.err.println("Receive Error!");
		}
		
		socket.close();
	}

}
