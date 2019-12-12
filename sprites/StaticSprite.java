import java.awt.Image;
import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public abstract class StaticSprite extends Sprite {

	private static Image image;
	protected boolean showImage = true;
	protected double minX = 0;
	protected double minY = 0;
	protected double maxX = 0;
	protected double maxY = 0;

	public abstract Image getImage();
	
	public StaticSprite() {}

	public boolean getShowImage() {
		return showImage;
	}

	public void setShowImage(boolean showImage) {
		this.showImage = showImage;
	}

	public final void setMinX(double minX) {
		this.minX = minX;
	}
	
	public final void setMinY(double minY) {
		this.minY = minY;
	}
	
	public final double getMinX() {
		return minX;
	}

	public final double getMaxX() {
		return maxX;
	}

	public final double getMinY() {
		return minY;
	}

	public final double getMaxY() {
		return maxY;
	}

	public final double getHeight() {
		return (maxY - minY);
	}

	public final double getWidth() {
		return (maxX - minX);
	}
}

