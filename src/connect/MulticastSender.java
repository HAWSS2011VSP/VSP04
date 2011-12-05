package connect;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.nio.ByteBuffer;

public class MulticastSender {

	
	private String iAddr;
	private int port;
	private MulticastSocket socket;
	private byte slotNr = 0;
	private long sendTime = 0;
	private ByteBuffer buffer33 = ByteBuffer.allocate(33);

	public MulticastSender(String iAddr, int port, Frame frame) {
		this.iAddr = iAddr;
		this.port = port;
		
		try {
			socket = new MulticastSocket();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void send(byte[] bytes) {
		
		if(bytes.length != 24){
			System.err.println("MsgLen != 24");
			return;
		}
		
		//TODO: waitForFreeSlot?!?
		
		sendTime = System.currentTimeMillis();
		
		buffer33.position(0);
		buffer33.put(bytes);
		buffer33.put(slotNr);
		buffer33.putLong(sendTime);
		
		byte[] buf = buffer33.array();
		
		try{
			InetAddress group = InetAddress.getByName(iAddr);
			DatagramPacket packet = new DatagramPacket(buf, buf.length, group, port);
			socket.send(packet);
		}catch (Exception e) {
			System.err.println("Sending failed!");
		}
	}

}
