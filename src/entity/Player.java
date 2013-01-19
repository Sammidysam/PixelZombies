package entity;

import org.newdawn.slick.Color;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Rectangle;

public class Player extends Entity {
	public float x;
	public float y;
	public float oldX = -16;
	public float oldY = -16;
	public int bullets;
	public int gun;
	public boolean hasGun = false;
	public boolean hasKnife = false;
	private Image image;
	public boolean dead = false;
	public final float SPEED = generateSpeed();
	public Player(float x, float y){
		this.x = x;
		this.y = y;
		image = getImage();
	}
	public void draw(){
	    if(!dead)
	    	image.draw(x, y, WIDTH * SCALE, HEIGHT * SCALE);
	    if(dead)
	    	image.drawFlash(x, y, WIDTH * SCALE, HEIGHT * SCALE, new Color(255, 0, 0));
	}
	public void returnToPrevious(){
		x = oldX;
		y = oldY;
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
