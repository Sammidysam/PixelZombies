package village;

import java.util.Random;

import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Rectangle;

public class VillageObject {
	protected float x;
	protected float y;
	protected Image image;
	protected Random rand = new Random();
	public VillageObject(float x, float y){
		this.x = x;
		this.y = y;
	}
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
	public int getWidth(){
		return 0;
	}
	public int getHeight(){
		return 0;
	}
	public float getScale(){
		return 0;
	}
	public float getX(){
		return x;
	}
	public float getY(){
		return y;
	}
	public Rectangle getRectangle(){
		Rectangle rectangle = new Rectangle(x, y, getWidth() * getScale(), getHeight() * getScale());
		if(rectangle.getCenterX() == x){
			rectangle.setCenterX(x + ((getWidth() * getScale()) / 2));
			rectangle.setCenterX(y + ((getHeight() * getScale()) / 2));
		}
		return rectangle;
	}
}
