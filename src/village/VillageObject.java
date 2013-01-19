package village;

import java.util.Random;

import org.newdawn.slick.Image;

public class VillageObject {
	protected Random rand = new Random();
	public void draw(float x, float y, Image image, float scale){
		image.draw(x, y, scale);
	}
	protected float getNearest(float angle, boolean usingDegrees){
		if(!usingDegrees)
			angle = (float) Math.toDegrees(angle);
		float[] distance = new float[5];
		int[] to = new int[5];
		for(int i = 0; i < 5; i++)
			to[i] = 90 * i;
		for(int i = 0; i < 5; i++)
			distance[i] = Math.abs(angle - to[i]);
		float leastDistance = 90;
		int choice = 5;
		for(int i = 0; i < 5; i++)
			if(distance[i] < leastDistance){
				leastDistance = distance[i];
				choice = i;
			}
		return choice * 90;
	}
}
