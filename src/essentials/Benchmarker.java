package essentials;

public class Benchmarker {
	private TimeKeeper timekeeper = new TimeKeeper();
	public Benchmarker(){
		timekeeper.start();
	}
	public Long getElapsedTime(){
		return timekeeper.timeDifference();
	}
	public void reset(){
		timekeeper.start();
	}
	public long getMemoryUsage(){
		Runtime runtime = Runtime.getRuntime();
		return runtime.totalMemory() - runtime.freeMemory();
	}
}
