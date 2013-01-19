package weapons;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;

public class Bullet {
	public static final float SCALE = 1.0F;
	public static final int WIDTH = 16;
	public static final int HEIGHT = 8;
	double angle;
	public float x;
	public float y;
	float originX;
	float originY;
	float upgrade = 1;
	public boolean done;
	public boolean moving;
	public int ENTITYID;
	public static final float SPEED = 8.0F;
	Image image;
	public Bullet(float x, float y, float manx, float many, int id, boolean moving){
		angle = Math.atan2(y - many, x - manx);
		this.moving = moving;
		if(this.moving){
			this.x = manx;
			this.y = many;
		}
		if(!this.moving){
			this.x = x;
			this.y = y;
		}
		originX = manx;
		originY = many;
		this.ENTITYID = id;
		try {
			image = new Image("res/bullet.png");
			image.setFilter(Image.FILTER_NEAREST);
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}
	public void move(){
		if(moving){
			if(Math.sqrt(Math.pow(originX - x, 2) + Math.pow(originY - y, 2)) >= 300 * upgrade)
				done = true;
			x += Math.cos(angle) * SPEED;
			y += Math.sin(angle) * SPEED;
		}
	}
	public void draw(){
		Image rotated = image.copy();
		if(moving)
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
}
