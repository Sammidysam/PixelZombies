package village;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class Road extends VillageObject {
	public static final int WIDTH = 64;
	public static final int HEIGHT = 28;
	private Image image;
	public float angle;
	private float x;
	private float y;
	public Road(float x, float y, float angle){
		this.x = x;
		this.y = y;
		this.angle = angle;
		try {
			image = new Image("res/road.png");
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}
	public void draw(){
		Image rotated = image.copy();
		if(angle % 90 != 0)
			angle = getNearest(angle, true);
		rotated.setRotation(angle);
		rotated.draw(x, y);
	}
}
