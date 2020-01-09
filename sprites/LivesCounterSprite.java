import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class LivesCounterSprite extends StaticSprite {
	LivesCounterSprite(double minX, double minY, double maxX, double maxY) {
		super();
		this.minX = minX;
		this.minY = minY;
		this.maxX = maxX;
		this.maxY = maxY;
	}
	
	@Override
	public Image getImage() {
		try {
			return ImageIO.read(new File("res/turret/turret_0.png"));
		} catch (IOException e) {
			System.err.println(e.toString());
		}
		
		return null;
	}

}
