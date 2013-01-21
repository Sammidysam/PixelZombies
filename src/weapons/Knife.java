package weapons;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;

public class Knife {
	public static final float SCALE = 1.0F;
	public static final int WIDTH = 8;
	public static final int HEIGHT = 4;
	public boolean equipped = false;
	public float x;
	public float y;
	private double angle;
	public int equippedBy = 50;
	public int equippedNumber = 101;
	public int ENTITYID;
	public static final int RADIUS = 8;
	private Image image;
	public Knife(float x, float y, int id, boolean bound){
		equipped = bound;
		ENTITYID = id;
		this.x = x;
		this.y = y;
		try {
			image = new Image("res/knife.png");
			image.setFilter(Image.FILTER_NEAREST);
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}
	public void move(float manx, float many, float mouseX, float mouseY, boolean hasGun){
		manx += 8;
		many += 8;
		int radius = RADIUS;
		if(hasGun)
			radius += 8;
		if(equipped){
			angle = Math.atan2(mouseY - many, mouseX - manx);
			if(Math.toDegrees(angle) > 135 || Math.toDegrees(angle) < -45)
				radius += 8;
			x = (float) (Math.cos(angle) * radius + manx);
			y = (float) (Math.sin(angle) * radius + many);
		}
	}
	public void draw(){
		Image rotated = image.copy();
		rotated.setRotation((float) Math.toDegrees(angle));
		rotated.draw(x, y);
	}
	public Rectangle getRectangle(){
		Rectangle rectangle = new Rectangle(x, y, WIDTH * SCALE, HEIGHT * SCALE);
		if(rectangle.getCenterX() == x){
			rectangle.setCenterX(x + ((WIDTH * SCALE) / 2));
			rectangle.setCenterX(y + ((HEIGHT * SCALE) / 2));
		}
		return rectangle;
	}
	public void reset(){
		equipped = false;
		equippedBy = 50;
		equippedNumber = 101;
	}
}
