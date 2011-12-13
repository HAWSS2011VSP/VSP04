package connect;

import java.util.ArrayList;
import java.util.HashSet;

public class Frame {

	private static final int NUM_SLOTS = 20;
	private static final int INTERVALL = 50;
	private static final int INTERVALL_HALF = INTERVALL/2;
	
	private boolean[] freelots = new boolean[NUM_SLOTS];
	private Object sync = new Object();
	
	public int timeToNextFrame(){
		return (int) (1000 - (System.currentTimeMillis()%1000));
	}
	
	public byte actualSlot(){
		return (byte) ((System.currentTimeMillis()%1000)/INTERVALL);
	}
	
	public int timeToNextSlot(int slot){ //Fetch send time of next slot
		return timeToNextFrame() + slot * INTERVALL + INTERVALL_HALF;
	}
	
	public Frame() {
		for(int i=0; i<NUM_SLOTS; ++i){
			freelots[i] = true;
		}
	}

	public void reserveSlot(int nextSlot) {
		if(nextSlot < 0 || nextSlot >= 20) return; //ignore slotReservation
		
		synchronized (sync) {
			freelots[nextSlot] = false;
		}
	}

	public byte getFreeSlot() {
		try {
			ArrayList<Integer> freeSlotsToChoose = new ArrayList<Integer>(20);
			
			Thread.sleep(timeToNextFrame());
			
			synchronized (sync) {
				for(int i=0; i<NUM_SLOTS; ++i){
					if(freelots[i]){
						freeSlotsToChoose.add(i);
					}else{
						freelots[i] = true;
					}
				}
			}
			if(!freeSlotsToChoose.isEmpty()) return (byte)(int)freeSlotsToChoose.get((int) (Math.random()*freeSlotsToChoose.size()));
		} catch (Exception e) {e.printStackTrace();}
		
		return -1;
	}

	public void waitForSlot(byte slot) { //in actualFrame if possible
		int i = timeToNextSlot(slot);
		if(i > 1000+INTERVALL_HALF) i-= 1000;
		
		try {
			Thread.sleep(i);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void waitForNextSlot(byte slot) { //in actualFrame if possible
		int i = timeToNextSlot(slot);
		
		try {
			Thread.sleep(i);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	
	
}
