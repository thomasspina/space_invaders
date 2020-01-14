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
	private Image[] projectileExplosion = new Image[5];
	private int explosionFrame = -1;
	
	private double speed;
	private ProjectileType type;

	private boolean hasHitShield = false;
	
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
			projectileExplosion[0] = ImageIO.read(new File("res/alienProjectileExplosion/alienProjectileExplosion_0.png"));
			projectileExplosion[1] = ImageIO.read(new File("res/alienProjectileExplosion/alienProjectileExplosion_1.png"));
			projectileExplosion[2] = ImageIO.read(new File("res/alienProjectileExplosion/alienProjectileExplosion_2.png"));
			projectileExplosion[3] = ImageIO.read(new File("res/alienProjectileExplosion/alienProjectileExplosion_3.png"));
			projectileExplosion[4] = ImageIO.read(new File("res/alienProjectileExplosion/alienProjectileExplosion_4.png"));
		} catch (IOException e) {
			System.err.println(e.toString());
		}
		
		if (projectileSound == null) {
			projectileSound = new AudioPlayer();
		}
		
		projectileSound.playAsynchronous(soundFilePath);
	}
	
	@Override
	public Image getImage() {
		if (hasHitShield) {
			setWidth(40);
			setHeight(40);
			if (explosionFrame != 4) {
				explosionFrame++;
			}
			return projectileExplosion[explosionFrame];
		}

		return image;
	}
		
	@Override
	public void update(Screen screen, KeyboardInput keyboard, long actual_delta_time) {
		if (!hasHitShield) {
			setCenterY(getCenterY() + speed);
		}

	    if (isOutOfBounds() || explosionFrame == 4) {
	    	setDispose();
		}
	}

	private boolean isOutOfBounds() {
		return getCenterY() < -300 || getCenterY() > 300;
	}

	ProjectileType getType() { return type; }

	void setHasHitShield() {
		hasHitShield = true;
	}

	boolean hasHitShield() {
		return hasHitShield;
	}
}

