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
		final MulticastSender mcSend = new MulticastSender(mcAddress, mcPort, frame);
		final MulticastReceiver mcListen = new MulticastReceiver(mcAddress, mcPort, frame);
		final UDP_Sender udpSend = new UDP_Sender(udpSenderPort);
		final UDP_Receiver udpListen = new UDP_Receiver(udpListenerPort);
		
		udpListen.setListenEvent(new ListenEvent() {
			@Override
			public void listen(byte[] msg) {
				mcSend.send(msg);
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
		
		try {
			Thread.sleep(333);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		mcSend.send(("22-X"+" --> "+"beliebiger Text").getBytes());
	}
	
}
