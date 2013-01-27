package village;

import java.util.Random;

public class VillageGenerator {
	private Random rand = new Random();
	public Cabin[] cabin = new Cabin[100];
	public Road[] road = new Road[100];
	public int cabinNumber = 0;
	public int roadNumber = 0;
	public void loop(float humanx, float humany){
		for(int i = 0; i < cabinNumber; i++)
			cabin[i].draw(humanx, humany);
		for(int i = 0; i < roadNumber; i++)
			road[i].draw();
	}
	public void createStreet(float x, float y, int length, float angle){
		if(angle % 90 != 0)
			angle = getNearest(angle, true);
		if(angle == 0){
			for(int i = 0; i < length; i++){
				createCabin(x + (i * (Cabin.WIDTH * Cabin.SCALE + (Cabin.WIDTH * Cabin.SCALE / 2))), y);
				for(int u = 0; u < 2; u++)
					createRoad((x + (i * (Cabin.WIDTH * Cabin.SCALE + (Cabin.WIDTH * Cabin.SCALE / 2)))) + (u * (Road.WIDTH)), y + (Cabin.HEIGHT * Cabin.SCALE) + 15, 0);
			}
		}
		if(angle == 180){
			for(int i = 0; i < length; i++){
				createCabin(x + (i * (Cabin.WIDTH * Cabin.SCALE + (Cabin.WIDTH * Cabin.SCALE / 2))), y);
				for(int u = 0; u < 2; u++)
					createRoad((x + (i * (Cabin.WIDTH * Cabin.SCALE + (Cabin.WIDTH * Cabin.SCALE / 2)))) + (u * (Road.WIDTH)), y - (Cabin.HEIGHT * Cabin.SCALE) + 15, 0);
			}
		}
		if(angle == 90){
//			left
		}
		if(angle == 270){
//			right
		}
	}
	public float getStreetDimension(float angle){
		if(angle % 90 != 0)
			angle = getNearest(angle, true);
		float distance = 0;
		switch ((int) angle){
			case 0:
				distance = (Cabin.HEIGHT * Cabin.SCALE) + (Road.HEIGHT) + 45;
				break;
			case 90:
	//			put in distance
				break;
			case 180:
				distance = (Cabin.HEIGHT * Cabin.SCALE) + (Road.HEIGHT) + 45;
				break;
			case 270:
	//			put in distance
				break;
		}
		return distance;
	}
	public void createTownSquare(){
		int x = rand.nextInt(1000) - 500;
		int y = rand.nextInt(1000) - 500;
		for(double angle = 0, radius = 150; angle < 360; angle += 45){
			if(angle != 0 && angle != 180){
				cabin[cabinNumber] = new Cabin((float)(Math.cos(Math.toRadians(angle)) * radius) + x, (float)(Math.sin(Math.toRadians(angle)) * radius) + y);
				cabinNumber++;
			}
		}
	}
	public void createCabin(float x, float y){
		cabin[cabinNumber] = new Cabin(x, y);
		cabinNumber++;
	}
	public void createRoad(float x, float y, float angle){
		road[roadNumber] = new Road(x, y, angle);
		roadNumber++;
	}
	private float getNearest(float angle, boolean usingDegrees){
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
