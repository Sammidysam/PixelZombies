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
	public void createStreet(){
//		float width = 100;
//		float height = 150;
//		for(int i = 0, x = rand.nextInt(1000) - 500; i < 3; i++, x += 240){
//			Rectangle rectangle = new Rectangle(x, 0, width, height);
//			cabin[cabinNumber] = new Cabin(rectangle.getMinX(), rectangle.getMaxY());
//			cabinNumber++;
//			cabin[cabinNumber] = new Cabin(rectangle.getMaxX(), rectangle.getMaxY());
//			cabinNumber++;
//			cabin[cabinNumber] = new Cabin(rectangle.getMinX(), rectangle.getMinY());
//			cabinNumber++;
//			cabin[cabinNumber] = new Cabin(rectangle.getMaxX(), rectangle.getMinY());
//			cabinNumber++;
//		}
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
}
