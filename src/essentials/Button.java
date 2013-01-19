package essentials;

import org.lwjgl.opengl.GL11;

public class Button {
	int startX;
	int startY;
	int width;
	int height;
	int endX;
	int endY;
	float color = 0.6F;
	public Button(int x, int y, int width, int height){
		startX = x;
		startY = y;
		this.width = width;
		this.height = height;
		endX = startX + width;
		endY = startY + height;
	}
	public boolean isOver(int mouseX, int mouseY){
		boolean clicked = false;
		if(mouseX >= startX && mouseX <= endX && mouseY >= startY && mouseY <= endY)
			clicked = true;
		return clicked;
	}
	public void draw(){
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glBegin(GL11.GL_QUADS);
		GL11.glColor3f(color, color, color);
		GL11.glVertex2f(endX, startY);
		GL11.glVertex2f(endX, endY);
		GL11.glVertex2f(startX, endY);
		GL11.glVertex2f(startX, startY);
		GL11.glEnd();
		GL11.glEnable(GL11.GL_TEXTURE_2D);
	}
}
