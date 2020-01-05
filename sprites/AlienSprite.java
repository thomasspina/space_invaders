import java.awt.Image;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class AlienSprite extends ActiveSprite {
	
	private static final int HEIGHT = 32;
	private static final int WIDTH = 32;
	private static final int X_SHIFT_DISTANCE = 10;
	private static final int Y_SHIFT_DISTANCE = 15;
	private static final double SHOOTING_PROBABILITY = 0.2;
	
	private static Image[] explosionFrames;
	
	private AudioPlayer laserSound = new AudioPlayer();
	private AudioPlayer explosionSound = new AudioPlayer();
	
	final private int pointValue;
	final private File firstFrameFile;
	final private File secondFrameFile;
	private Image movementFirstFrame;
	private Image movementSecondFrame;

	private int direction = 1;
	private int explosionFrame = -1;
	private boolean onFirstFrame = true;
	private boolean isDead = false;
	private boolean shiftingY = false;
	private boolean justShiftedY = false;
	private boolean isShooting = false;
	
	// need to add random shooting
	
	private long previousTime = 0;
	private int shiftPeriod = 300;
	
	AlienSprite(int alienType, double centerX, double centerY) {
		super();
		setCenterX(centerX);
		setCenterY(centerY);
		setWidth(WIDTH);
		setHeight(HEIGHT);

		switch(alienType) {
			case 1:
				pointValue = 10;
				firstFrameFile = new File("res/alien1/alien1_0.png");
				secondFrameFile = new File("res/alien1/alien1_1.png");
				break;
			case 2:
				pointValue = 20;
				firstFrameFile = new File("res/alien2/alien2_0.png");
				secondFrameFile = new File("res/alien2/alien2_1.png");
				break;
			default:
				pointValue = 30;
				firstFrameFile = new File("res/alien3/alien3_0.png");
				secondFrameFile = new File("res/alien3/alien3_1.png");
				break;
		}
		
		try {
			movementFirstFrame = ImageIO.read(firstFrameFile);
			movementSecondFrame = ImageIO.read(secondFrameFile);
			explosionFrames = new Image[] {
					ImageIO.read(new File("res/alienExplosion/explosion_0.png")),
					ImageIO.read(new File("res/alienExplosion/explosion_1.png")),
					ImageIO.read(new File("res/alienExplosion/explosion_2.png")),
					ImageIO.read(new File("res/alienExplosion/explosion_3.png")),
					ImageIO.read(new File("res/alienExplosion/explosion_4.png")),
					ImageIO.read(new File("res/alienExplosion/explosion_5.png")),
					ImageIO.read(new File("res/alienExplosion/explosion_6.png"))
				};
		} catch (IOException e) {
			System.err.println(e.toString());
		}
	}
	
	@Override
	public Image getImage() {
		if (!isDead) {
			return onFirstFrame ? movementSecondFrame : movementFirstFrame;
		} else {
			if (explosionFrame != 6) {
				explosionFrame++;
			}
			return explosionFrames[explosionFrame];
		}
	}
	
	@Override
	public void update(Screen screen, KeyboardInput keyboard, long actual_delta_time) {
		previousTime += actual_delta_time;
		
		collidedWithObject(screen);
		
		if (!isDead && previousTime >= shiftPeriod) {
			isShooting = Math.random() < SHOOTING_PROBABILITY;
			onFirstFrame ^= true;
			previousTime -= shiftPeriod;
			
			if (shiftingY) {
				shiftingY = false;
				justShiftedY = true;
				setCenterY(getCenterY() + Y_SHIFT_DISTANCE); 
			} else {
				if (justShiftedY) {
					direction *= -1;
					justShiftedY = false;
				}
				setCenterX(getCenterX() + (X_SHIFT_DISTANCE * direction));
			}
			
			if (isShooting) {
				// create an alien projectile instance
			}
			
		} else if (explosionFrame == 6) {
			setDispose();
		}
	}
	
	private void collidedWithObject(Screen screen) {
		for (ActiveSprite activeSprite : screen.getActiveSprites()) {
			if (activeSprite instanceof ProjectileSprite) {
				isDead = CollisionDetection.overlaps(
						getMinX(),
						getMinY(),
						getMaxX(), 
						getMaxY(),
						activeSprite.getMinX(),
						activeSprite.getMinY(),
						activeSprite.getMaxX(),
						activeSprite.getMaxY());


				if (isDead) {
					// add the point value to the screen.score once it is created
					explosionSound.playAsynchronous("res/explosion_0.wav");
					activeSprite.setDispose();
					break;
				}
			}
		}
		
		for (StaticSprite staticSprite : screen.getStaticSprites()) {
			if (staticSprite instanceof BarrierSprite) {
				boolean onScreenEdge = CollisionDetection.overlaps(
						getMinX(),
						getMinY(), 
						getMaxX(), 
						getMaxY(), 
						staticSprite.getMinX(), 
						staticSprite.getMinY(),
						staticSprite.getMaxX(),
						staticSprite.getMaxY());
				
				if (!justShiftedY && !shiftingY && onScreenEdge) {
					for (ActiveSprite activeSprite : screen.getActiveSprites()) {
						if (activeSprite instanceof AlienSprite) {
							((AlienSprite) activeSprite).shiftY();
						}
					}
					break;
				}
			}
		}
	}
	
	private void shiftY() {
		shiftingY = true;
	}
}

