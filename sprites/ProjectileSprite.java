import java.awt.Image;
import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class ProjectileSprite extends ActiveSprite {

	private static Image image;
	private static final int WIDTH = 20;
	private static final int HEIGHT = 20;

	private AudioPlayer projectileSound = null;
	
	private double velocityY = 0;
	
	public ProjectileSprite(double centerX, double centerY, double velocityY, String audioFile) {

		super();
		this.setCenterX(centerX);
		this.setCenterY(centerY);
		this.setWidth(WIDTH);
		this.setHeight(HEIGHT);

		this.velocityY = velocityY;
		
		if (image == null) {
			try {
				image = ImageIO.read(new File("res/turretProjectile/turretProjectile_0.png"));
			}
			catch (IOException e) {
				System.err.println(e.toString());
			}
		}
		
		if (projectileSound == null) {
			projectileSound = new AudioPlayer();
		}
		
		projectileSound.playAsynchronous(audioFile);
	}
	
	@Override
	public Image getImage() {
		return image;
	}
		
	@Override
	public void update(Screen level, KeyboardInput keyboard, long actual_delta_time) {
	    double movement_y = (velocityY * actual_delta_time * 0.001);
	    
	    this.addCenterY(movement_y);    			
	}			
	
}

