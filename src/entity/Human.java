package entity;

import org.newdawn.slick.Color;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Rectangle;

import essentials.Directions;
import essentials.TimeKeeper;

public class Human extends Entity {
	public float x;
	public float y;
	private float oldX;
	private float oldY;
	public float movementX;
	public float movementY;
	private Image image;
	public boolean show = true;
	public boolean dead = false;
	public TimeKeeper timekeeper = new TimeKeeper();
	private boolean started = false;
	public boolean zombieNear = false;
	public boolean gunNear = false;
	public boolean knifeNear = false;
	public int direction = 8;
	public boolean hasGun = false;
	public boolean hasKnife = false;
	private int wait1 = (rand.nextInt(4) + 1) * 1000;
	private int wait2 = (rand.nextInt(10) + 1) * 1000;
	public int nearestZombie;
	public int nearestGun;
	public int nearestKnife;
	public final float SPEED = generateSpeed();
	public int ENTITYID;
	public Human(float x, float y, int id){
		this.x = x;
		this.y = y;
		ENTITYID = id;
		image = getImage();
		if(rand.nextBoolean()){
			timekeeper.start();
			started = false;
		}
	}
	public void move(){
		if(show && !dead){
			oldX = x;
			oldY = y;
			if(!zombieNear && !gunNear && !knifeNear){
				if(started == true){
					moveInDirection(direction);
					if(timekeeper.timeDifference() >= wait1){
						timekeeper.start();
						started = false;
						wait1 = (rand.nextInt(4) + 1) * 1000;
					}
				}
				if((started == false && timekeeper.timeDifference() == 0) || (started == false && timekeeper.timeDifference() >= wait2)){
					timekeeper.start();
					started = true;
					direction = rand.nextInt(8);
					wait2 = (rand.nextInt(10) + 1) * 1000;
				}
			}
			if(zombieNear){
				x += movementX;
				y += movementY;
			}
			if(gunNear || knifeNear){
				x -= movementX;
				y -= movementY;
			}
		}
	}
	public void draw(){
		if(show){
			float zero = 0;
			if(!dead){
				Color color = new Color(230 - zero, 230 - zero, 235 - zero);
				image.drawFlash(x, y, WIDTH * SCALE, HEIGHT * SCALE, color);
			}
			if(dead){
				Color color = new Color(230 - zero, 0, 0);
				image.drawFlash(x, y, WIDTH * SCALE, HEIGHT * SCALE, color);
			}
		}
	}
	public void returnToPrevious(){
		x = oldX;
		y = oldY;
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
}
