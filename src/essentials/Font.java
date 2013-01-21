package essentials;

import org.newdawn.slick.Color;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

public class Font {
	Image[] letterImages = new Image[41];
	public Font(){
		loadLetters();
	}
	private void loadLetters(){
		SpriteSheet letterSheet = null;
		try {
			letterSheet = new SpriteSheet(new Image("res/font.png"), 19, 19, 1, 1);
		} catch (SlickException e) {
			e.printStackTrace();
		}
		for(int i = 0, addX = 0, addY = 0; i < 41; i++){
			letterImages[i] = letterSheet.getSubImage(addX, addY);
			try {
				letterImages[i].setFilter(Image.FILTER_NEAREST);
			} catch (SlickException e) {
				e.printStackTrace();
			}
			addX++;
			if(i == 25){
				addX = 0;
				addY++;
			}
		}
	}
	private int toFake(int letter){
		int fake = 40;
		if(letter >= 65 && letter <= 90)fake = letter - 65;
		if(letter >= 97 && letter <= 122)fake = letter - 97;
		if(letter == 48)fake = 35;
		if(letter >= 49 && letter <= 57)fake = letter - 23;
		if(letter == 33)fake = 36;
		if(letter == 63)fake = 37;
		if(letter == 46)fake = 38;
		if(letter == 58)fake = 39;
		if(letter == 32)fake = 40;
		if(fake == 41){
			System.out.println(letter + " was unexpected");
			fake = 40;
		}
		return fake;
	}
	public void drawString(String string, float x, float y, Color color, float size){
		char[] characters = new char[string.length()];
		for(int i = 0; i < string.length(); i++)
			characters[i] = string.charAt(i);
		for(int i = 0, addX = 0; i < string.length(); i++){
			Image real = letterImages[toFake(characters[i])].getSubImage(1, 1, letterImages[toFake(characters[i])].getWidth() - 2, letterImages[toFake(characters[i])].getHeight() - 2);
			real = real.getScaledCopy(size);
			real.drawFlash(x + addX, y, real.getWidth(), real.getHeight(), color);
			addX += real.getWidth() / 1.3;
		}
	}
	public int getLength(String string, float size){
		int length = 0;
		char[] characters = new char[string.length()];
		for(int i = 0; i < string.length(); i++)
			characters[i] = string.charAt(i);
		for(int i = 0; i < string.length(); i++){
			Image real = letterImages[toFake(characters[i])].getSubImage(1, 1, letterImages[toFake(characters[i])].getWidth() - 2, letterImages[toFake(characters[i])].getHeight() - 2);
			real = real.getScaledCopy(size);
			length += real.getWidth() / 1.3;
		}
		return length;
	}
}
