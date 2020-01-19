import java.awt.Image;

public abstract class StaticSprite extends Sprite {

	protected static Image image;
	protected boolean showImage = true;
	protected double minX = 0;
	protected double minY = 0;
	protected double maxX = 0;
	protected double maxY = 0;

	public abstract Image getImage();
	
	StaticSprite() {}

	boolean getShowImage() {
		return showImage;
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

