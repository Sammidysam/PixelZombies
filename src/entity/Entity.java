package entity;

import java.util.Random;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class Entity {
	public static final float SCALE = 2.0F;
	public static final int WIDTH = 8;
	public static final int HEIGHT = 8;
	public boolean reset = false;
	public int direction;
	protected Random rand = new Random();
	protected float generateSpeed(){
		String speed = "0.";
		speed += (rand.nextInt(20) + 70) + "F";
		return Float.parseFloat(speed) * 2.9F;
	}
	protected Image getImage(){
		Image image = null;
		try {
			image = new Image("res/pixel.png");
			image.setFilter(Image.FILTER_NEAREST);
		} catch (SlickException e) {
			e.printStackTrace();
		}
		return image;
	}
}
