package essentials;

public class DayNightCycle {
	private static final float green = 1.00F;
	private float subtraction = 0.00F;
	public int days = 0;
	public boolean pause = false;
	TimeKeeper timekeeper = new TimeKeeper();
	public DayNightCycle(){
		timekeeper.start();
	}
	public float getGreen(){
		if(timekeeper.timeDifference() >= 200000){
			days++;
			timekeeper.start();
		}
		subtraction = getSubtraction();
		return Math.abs(green - subtraction);
	}
	private float getSubtraction(){
		String start = "0.";
		String sub = Long.toString(timekeeper.timeDifference());
		if(!pause){
			if(timekeeper.timeDifference() >= 100000){
				start = "1.";
				sub = sub.substring(1, sub.length());
			}
			if(timekeeper.timeDifference() >= 0 && timekeeper.timeDifference() < 10000)
				start += "0";
			if(!sub.substring(0, 1).equalsIgnoreCase("-"))
				start += sub;
		}
		if(start != "0.")
			return Float.parseFloat(start);
		else{
			return subtraction;
		}
	}
	public void pause(){
		timekeeper.pause();
		pause = true;
	}
	public void unPause(){
		timekeeper.unPause();
		pause = false;
	}
}
