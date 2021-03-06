import java.awt.Image;

public abstract class ActiveSprite extends Sprite {
	private double centerX = 0;
	private double centerY = 0;
	private double width = 50;
	private double height = 50;
	private boolean dispose = false;

	public abstract void update(Screen level, KeyboardInput keyboard, long actual_delta_time);
	public abstract Image getImage();
	
	ActiveSprite() { }
	
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

	double getCenterX() {
		return centerX;
	}

	double getCenterY() {
		return centerY;
	}
	
	final void setCenterX(double centerX) {
		this.centerX = centerX;
	}
	
	final void setCenterY(double centerY) {
		this.centerY = centerY;
	}

	void setWidth(double width) {
		this.width = width;
	}

	void setHeight(double height) {
		this.height = height;
	}
				
	boolean getDispose() {
		return dispose;
	}

	void setDispose() {
		dispose = true;
	}
}
