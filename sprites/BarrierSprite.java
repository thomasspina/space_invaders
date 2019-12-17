import java.awt.Image;

public class BarrierSprite extends StaticSprite {
	public BarrierSprite(double minX, double minY, double maxX, double maxY) {
		this.minX = minX;
		this.minY = minY;
		this.maxX = maxX;
		this.maxY = maxY;
	}
	
	@Override
	public Image getImage() {
		return null;
	}
}
