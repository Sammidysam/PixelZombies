package village;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

public class Cabin extends VillageObject {
	public Image closed;
	public Image open;
	private Image human;
	private int inhabitants = rand.nextInt(3);
	public Cabin(float x, float y){
		super(x, y);
		Image sheet = null;
		try {
			sheet = new Image("res/cabin.png");
			sheet.setFilter(Image.FILTER_NEAREST);
			human = new Image("res/pixel.png");
			human.setFilter(Image.FILTER_NEAREST);
		} catch (SlickException e) {
			e.printStackTrace();
		}
		SpriteSheet cabinsheet = new SpriteSheet(sheet, getWidth(), getHeight());
		closed = cabinsheet.getSubImage(0, 0);
		open = cabinsheet.getSubImage(1, 0);
	}
	public void draw(float humanx, float humany){
		float doorX = x + ((getWidth() * getScale()) / 2.2F);
		if(Math.sqrt(Math.pow(doorX - humanx, 2) + Math.pow((y + (getHeight() * getScale())) - humany, 2)) <= 9.5)
			open.draw(x, y, getScale());
		else {
			closed.draw(x, y, getScale());
		}
		for(int i = 0; i < inhabitants; i++){
			if(i == 0 && Math.sqrt(Math.pow(doorX - humanx, 2) + Math.pow((y + (getHeight() * getScale())) - humany, 2)) > 9.5){
				human.draw(x + 9.2F, y + (getHeight() * getScale() - 17.9F), 3 * getScale(), 3 * getScale());
			}
			if(i == 1){
				human.draw(x + 55.7F, y + (getHeight() * getScale() - 17.9F), 3 * getScale(), 3 * getScale());
			}
		}
	}
	public int getWidth(){
		return 24;
	}
	public int getHeight(){
		return 23;
	}
	public float getScale(){
		return 3.1f;
	}
}
