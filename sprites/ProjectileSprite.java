import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ProjectileSprite extends ActiveSprite {

	private static final int WIDTH = 3;
	private static final int HEIGHT = 10;

	private String imageFilePath;
	private String soundFilePath;

	private AudioPlayer projectileSound = null;
	private Image image;
	
	private double speed;
	private ProjectileType type;
	
	ProjectileSprite(double centerX, double centerY, ProjectileType projectileType) {
		super();
		this.setCenterX(centerX);
		this.setCenterY(centerY);
		this.setWidth(WIDTH);
		this.setHeight(HEIGHT);
		type = projectileType;

		if (projectileType == ProjectileType.TURRET) {
			speed = -6;
			soundFilePath = "res/turretProjectile.wav";
			imageFilePath = "res/turretProjectile/turretProjectile_0.png";
		} else {
			speed = 2;
			soundFilePath = "res/alienProjectile.wav";
			imageFilePath = "res/alienProjectile/alienProjectile_0.png";
		}
		
		try {
			image = ImageIO.read(new File(imageFilePath));
		}
		catch (IOException e) {
			System.err.println(e.toString());
		}
		
		if (projectileSound == null) {
			projectileSound = new AudioPlayer();
		}
		
		projectileSound.playAsynchronous(soundFilePath);
	}
	
	@Override
	public Image getImage() {
		return image;
	}
		
	@Override
	public void update(Screen screen, KeyboardInput keyboard, long actual_delta_time) {

	    setCenterY(getCenterY() + speed);

	    if (isOutOfBounds()) {
	    	setDispose();
		}
	}

	private boolean isOutOfBounds() {
		return getCenterY() < -300 || getCenterY() > 300;
	}

	ProjectileType getType() { return type; }
}

