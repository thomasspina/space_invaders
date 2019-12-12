import java.awt.Image;
import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public abstract class ActiveSprite extends Sprite {
	private double centerX = 0;
	private double centerY = 0;
	private double width = 50;
	private double height = 50;
	private boolean dispose = false;

	public abstract void update(Screen level, KeyboardInput keyboard, long actual_delta_time);
	public abstract Image getImage();
	
	public ActiveSprite() {
	}
	
	public final double getMinX() {
		return centerX - (width / 2);
	}

	public final double getMaxX() {
		return centerX + (width / 2);
	}

	public final double getMinY() {
		return centerY - (height / 2);
	}

	public final double getMaxY() {
		return centerY + (height / 2);
	}

	public final double getHeight() {
		return height;
	}

	public final double getWidth() {
		return width;
	}

	public double getCenterX() {
		return centerX;
	}

	public double getCenterY() {
		return centerY;
	}
	
	public final void setCenterX(double centerX) {
		this.centerX = centerX;
	}
	
	public final void setCenterY(double centerY) {
		this.centerY = centerY;
	}

	public void setWidth(double width) {
		this.width = width;
	}

	public void setHeight(double height) {
		this.height = height;
	}
	
	public final void addCenterX(double delta) {
		centerX += delta;		
	}

	public final void addCenterY(double delta) {
		centerY += delta;		
	}
	
	public final void setMinX(double minX) {
		centerX =  minX + (width / 2);
	}

	public final void setMinY(double minY) {
		centerY = minY + (height / 2);
	}
				
	public boolean getDispose() {
		return dispose;
	}

	public void setDispose() {
		dispose = true;
	}
}
