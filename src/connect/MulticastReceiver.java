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
	private MulticastSocket socket;
	private InetAddress group;
	
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
		connect();

		try{
			while(!interrupted()) {
			    receivePacket();
			}
		}catch (IOException e) {
			System.err.println("Receive Error!");
		}
		
		disconnect();
	}
	
	private void receivePacket() throws IOException {
		byte[] buf = new byte[33];
		DatagramPacket packet = new DatagramPacket(buf, buf.length);
	    socket.receive(packet);

	    byte[] packetData = packet.getData();
	    String received = new String(packetData);
	    
	    buffer33.position(0);
	    buffer33.put(packetData);
	    
	    buffer33.position(24);
	    byte nextSlot = buffer33.get();
	    frame.reserveSlot(nextSlot);
	    
	    System.out.println("MulticastReceiver: " + received + " --> "+frame.actualSlot()+" nextIs "+nextSlot);
	    
	    listenEvent.listen(packetData);
	}

	private void connect(){
		try {
			socket = new MulticastSocket(port);
		} catch (IOException e) {
			System.err.println("Port is in use!");
			return;
		}
		
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
	}
	
	private void disconnect(){
		try {
			socket.leaveGroup(group);
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
