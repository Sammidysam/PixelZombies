package village;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class Road extends VillageObject {
	public float angle;
	public Road(float x, float y, float angle){
		super(x, y);
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
	public int getWidth(){
		return 64;
	}
	public int getHeight(){
		return 28;
	}
	public float getScale(){
		return 1;
	}
}
