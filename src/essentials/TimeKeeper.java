package essentials;

import org.lwjgl.Sys;

public class TimeKeeper {
	private long startTime;
	private long pauseTime;
	private long subtraction;
	private boolean paused;
	public void start(){
		startTime = getTime();
		subtraction = 0;
	}
	public long timeDifference(){
		return getTime() - subtraction - startTime;
	}
	public long getTime(){
		return (Sys.getTime() * 1000) / Sys.getTimerResolution();
	}
	public boolean isPaused(){
		return paused;
	}
	public void pause(){
		pauseTime = getTime();
		paused = true;
	}
	public void unPause(){
		subtraction += getSubtraction();
		paused = false;
	}
	public long getSubtraction(){
		return getTime() - pauseTime;
	}
}
