package connect;

public class SendReceive {

	public static void main(String[] args) {
		int udpListenerPort;
		int udpSenderPort;
		String mcAddress;
		int mcPort;
		
		if(args.length != 4){
			System.out.println("Benötigt: <udp-listener-port><udp-sender-port><mc-adresse><mc-port>");
			return;
		}
		
		try{
			udpListenerPort = Integer.parseInt(args[0]);
			udpSenderPort = Integer.parseInt(args[1]);
			mcAddress = args[2];
			mcPort = Integer.parseInt(args[3]);
		}catch (Exception e) {
			System.out.println("Benötigt: <udp-listener-port><udp-sender-port><mc-adresse><mc-port>");
			return;
		}
		
		final Frame frame = new Frame();
		final MessageQueue queue = new MessageQueue(512);
		final MulticastSender mcSend = new MulticastSender(mcAddress, mcPort, frame, queue);
		final MulticastReceiver mcListen = new MulticastReceiver(mcAddress, mcPort, frame);
		final UDP_Sender udpSend = new UDP_Sender(udpSenderPort);
		final UDP_Receiver udpListen = new UDP_Receiver(udpListenerPort);
		
		final MulticastSender mcSend2 = new MulticastSender(mcAddress, mcPort, frame, queue);
		final MulticastSender mcSend3 = new MulticastSender(mcAddress, mcPort, frame, queue);
		
		udpListen.setListenEvent(new ListenEvent() {
			@Override
			public void listen(byte[] msg) {
				queue.offer(msg);
			}
		});
		
		mcListen.setListenEvent(new ListenEvent() {
			@Override
			public void listen(byte[] msg) {
				udpSend.send(msg);
			}
		});
		
		mcListen.start();
		udpListen.start();
		mcSend.start();
		
		mcSend2.start();
		mcSend3.start();
		
		try {
			Thread.sleep(333);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
	}
	
}
