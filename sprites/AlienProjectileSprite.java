import java.awt.Image;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class AlienProjectileSprite extends ProjectileSprite {
	private static int FRAME_CHANGE_PERIOD = 250;
	
	private static Image secondFrame;
	private static Image firstFrame;
	
	private boolean onFirstFrame = false;
	private double previousTime = 0;
	private double velocityY = 0;
	
	public AlienProjectileSprite(double centerX, double centerY, double velocityY) {
		super(centerX, centerY, velocityY, "res/alien_projectile.wav");
		
		try {
			firstFrame = ImageIO.read(new File ("res/alienProjectile/alienProjectile_0.png"));
			secondFrame = ImageIO.read(new File ("res/alienProjectile/alienProjectile_1.png"));
		} catch (IOException e) {
			System.err.println(e.toString());
		}
	}
	
	@Override
	public Image getImage() {
		return onFirstFrame ? secondFrame : firstFrame;
	}
	
	@Override
	public void update(Screen level, KeyboardInput keyboard, long actual_delta_time) {
	    previousTime += actual_delta_time;
	    
	    if (previousTime >= FRAME_CHANGE_PERIOD) {
	    	onFirstFrame ^= true;
	    	previousTime -= FRAME_CHANGE_PERIOD;
	    }
	    
	    double movement_y = (velocityY * actual_delta_time * 0.001);
	    
	    addCenterY(movement_y); 			
	}
}
