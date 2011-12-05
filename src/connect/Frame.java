package connect;

public class Frame {

	private static final int NUM_SLOTS = 20;
	private static final int INTERVALL = 50;
	private static final int INTERVALL_HALF = INTERVALL/2;
	
	private boolean[] freelots = new boolean[NUM_SLOTS];
	private long frameStart = 0;
	
	public Frame() {
		for(int i=0; i<NUM_SLOTS; ++i){
			freelots[i] = true;
		}
	}

	public void setBlockAndTime(byte b, long long1) {
		// TODO Auto-generated method stub
		
	}
	
	
	
}
