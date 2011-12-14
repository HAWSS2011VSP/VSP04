package connect;

import java.util.concurrent.ArrayBlockingQueue;

public class MessageQueue extends ArrayBlockingQueue<byte[]> {
	
	private static final long serialVersionUID = 1L;
	private Object sync = new Object();
	
	public MessageQueue(int capacity) {
		super(capacity);
	}
	
	@Override
	public byte[] poll() {
		synchronized (sync) {
			return super.poll();
		}
	}
	
	@Override
	public boolean offer(byte[] e) {
		synchronized (sync) {
			return super.offer(e);
		}
	}
	
}
