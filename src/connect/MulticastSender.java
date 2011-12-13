package connect;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.nio.ByteBuffer;

public class MulticastSender extends Thread {

	
	private String iAddr;
	private int port;
	private MulticastSocket socket;
	private byte slotNr = 0;
	private long sendTime = 0;
	private ByteBuffer buffer33 = ByteBuffer.allocate(33);
	private Frame frame;
	private MessageQueue queue;

	public MulticastSender(String iAddr, int port, Frame frame, MessageQueue queue) {
		this.iAddr = iAddr;
		this.port = port;
		this.frame = frame;
		this.queue = queue;
		
		try {
			socket = new MulticastSocket();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void run() {
		byte slot;
		while(!interrupted()){ //wait for free slot
			if((slot = frame.getFreeSlot()) != -1){ //enter if there is a free slot
				send(slot);
			}
		}
	}
	
	private void send(byte slot) {
		if(!queue.isEmpty()){
			
			frame.waitForSlot(slot);
			
			byte nextSlot;
			while((nextSlot = send(queue.poll())) != -1 && !queue.isEmpty() && !interrupted()){
				System.out.println("NextSlot[init="+slot+"]:: "+nextSlot);
				frame.waitForNextSlot(nextSlot);
			}
			System.out.println("MulticastSender[initSlot="+slot+"] --nextSlot-> "+nextSlot);
		}
		System.out.println("---------------there-are-no-messages-in-the-queue--------------");
	}

	public byte send(byte[] bytes) {
		
		if(bytes.length != 24){
			System.err.println("MsgLen != 24");
			return -1;
		}
		
		slotNr = frame.actualSlot(); //isNextSlot
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
			return slotNr;
		}catch (Exception e) {
			System.err.println("Sending failed!");
		}
		
		return -1;
	}

}
