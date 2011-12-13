package connect;

import java.util.concurrent.ArrayBlockingQueue;

public class MessageQueue extends ArrayBlockingQueue<byte[]> {
	
	private static final long serialVersionUID = 1L;

	
	public MessageQueue(int capacity) {
		super(capacity);
	}
	
}
