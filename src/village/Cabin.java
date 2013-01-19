package village;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.geom.Rectangle;

public class Cabin extends VillageObject {
	public static final int WIDTH = 24;
	public static final int HEIGHT = 23;
	public static final float SCALE = 3.1F;
	public Image closed;
	public Image open;
	private Image human;
	public float x;
	public float y;
	private int inhabitants = rand.nextInt(3);
	public Cabin(float x, float y){
		this.x = x;
		this.y = y;
		Image sheet = null;
		try {
			sheet = new Image("res/cabin.png");
			sheet.setFilter(Image.FILTER_NEAREST);
			human = new Image("res/pixel.png");
			human.setFilter(Image.FILTER_NEAREST);
		} catch (SlickException e) {
			e.printStackTrace();
		}
		SpriteSheet cabinsheet = new SpriteSheet(sheet, WIDTH, HEIGHT);
		closed = cabinsheet.getSubImage(0, 0);
		open = cabinsheet.getSubImage(1, 0);
	}
	public void draw(float humanx, float humany){
		float doorX = x + ((WIDTH * SCALE) / 2.2F);
		if(Math.sqrt(Math.pow(doorX - humanx, 2) + Math.pow((y + (HEIGHT * SCALE)) - humany, 2)) <= 9.5)
			open.draw(x, y, SCALE);
		else {
			closed.draw(x, y, SCALE);
		}
		for(int i = 0; i < inhabitants; i++){
			if(i == 0 && Math.sqrt(Math.pow(doorX - humanx, 2) + Math.pow((y + (HEIGHT * SCALE)) - humany, 2)) > 9.5){
				human.draw(x + 9.2F, y + (HEIGHT * SCALE - 17.9F), 3 * SCALE, 3 * SCALE);
			}
			if(i == 1){
				human.draw(x + 55.7F, y + (HEIGHT * SCALE - 17.9F), 3 * SCALE, 3 * SCALE);
			}
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
