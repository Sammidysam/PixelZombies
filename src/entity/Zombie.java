package entity;

import org.newdawn.slick.Color;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Rectangle;

import essentials.Directions;
import essentials.TimeKeeper;

public class Zombie extends Entity {
	public float x;
	public float y;
	public float oldX;
	public float oldY;
	private Image image;
	public TimeKeeper timekeeper = new TimeKeeper();
	public boolean dead = false;
	public boolean reset = false;
	public boolean hasGun = false;
	public boolean hasKnife = false;
	public int direction = 8;
	private final float SPEED = generateSpeed();
	public int ENTITYID;
	public Zombie(float x, float y, int id){
		this.x = x;
		this.y = y;
		ENTITYID = id;
		image = getImage();
	}
	public void move(float x, float y){
		if(timekeeper.timeDifference() >= 500 && reset)
			reset = false;
		oldX = this.x;
		oldY = this.y;
		if(dead == false){
			if(!reset){
				double angle = Math.atan2(this.y - y, this.x - x);
				this.x -= Math.cos(angle) * SPEED;
				this.y -= Math.sin(angle) * SPEED;
			}
			if(reset){
				moveInDirection(direction);
			}
		}
	}
	public void draw(){
		float zero = 0;
		if(!dead)
			image.drawFlash(x, y, WIDTH * SCALE, HEIGHT * SCALE, new Color(0, 255 - zero, 255 - zero));
		if(dead)
			image.drawFlash(x, y, WIDTH * SCALE, HEIGHT * SCALE, new Color(255 - zero, 0, 0));
	}
	private void moveInDirection(int direction){
		if(direction == Directions.UP){
			y -= SPEED;
		}
		if(direction == Directions.RIGHT){
			x += SPEED;
		}
		if(direction == Directions.DOWN){
			y += SPEED;
		}
		if(direction == Directions.LEFT){
			x -= SPEED;
		}
		if(direction == Directions.UPRIGHT){
			y -= SPEED;
			x += SPEED;
		}
		if(direction == Directions.UPLEFT){
			y -= SPEED;
			x -= SPEED;
		}
		if(direction == Directions.DOWNLEFT){
			y += SPEED;
			x -= SPEED;
		}
		if(direction == Directions.DOWNRIGHT){
			y += SPEED;
			x += SPEED;
		}
	}
	public Rectangle getRectangle(){
		Rectangle rectangle = new Rectangle(x, y, WIDTH * SCALE, HEIGHT * SCALE);
		if(rectangle.getCenterX() == x){
			rectangle.setCenterX(x + ((WIDTH * SCALE) / 2));
			rectangle.setCenterX(y + ((HEIGHT * SCALE) / 2));
		}
		return rectangle;
	}
	public void returnToPrevious(){
		x = oldX;
		y = oldY;
	}
}
