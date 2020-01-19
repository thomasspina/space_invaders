import java.awt.Image;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class AlienSprite extends ActiveSprite {
	
	private static final int HEIGHT = 32;
	private static final int WIDTH = 32;
	private static final int X_SHIFT_DISTANCE = 10;
	private static final int Y_SHIFT_DISTANCE = 15;
	private static final double INITIAL_SHOOTING_PROBABILITY = 0.007;
	private static final int INITIAL_SHIFT_PERIOD = 500;
	private static Image[] explosionFrames;
	private AudioPlayer explosionSound = new AudioPlayer();
	final private int pointValue;
	private Image movementFirstFrame;
	private Image movementSecondFrame;
	private int direction = 1;
	private int explosionFrame = -1;
	private boolean onFirstFrame = true;
	private boolean isDead = false;
	private boolean shiftY = false;
	private boolean changedDirection = false;
	private boolean isLanding = false;
	private long previousTime = 0;
	private int shiftPeriod = INITIAL_SHIFT_PERIOD;
	private double shootingProbability = INITIAL_SHOOTING_PROBABILITY;
	
	AlienSprite(int alienType, double centerX, double centerY) {
		super();
		setCenterX(centerX);
		setCenterY(centerY);
		setWidth(WIDTH);
		setHeight(HEIGHT);

		File firstFrameFile;
		File secondFrameFile;

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
		boolean isShooting;
		previousTime += actual_delta_time;



		if (!isDead) {
			collidedWithObject(screen);

			if (previousTime >= shiftPeriod) {
				isShooting = Math.random() < shootingProbability;
				onFirstFrame ^= true;
				previousTime -= shiftPeriod;

				if (isLanding) {
					if (getCenterY() >= 242) {
						((SpaceInvadersScreen) screen).alienHasLanded();
					}

					setCenterY(getCenterY() + Y_SHIFT_DISTANCE);
				} else if (shiftY) {
					shiftY = false;
					changedDirection = true;
					direction *= -1;
					setCenterY(getCenterY() + Y_SHIFT_DISTANCE);
				} else {
					if (changedDirection) {
						changedDirection = false;
					}
					setCenterX(getCenterX() + (X_SHIFT_DISTANCE * direction));
				}
				
				if (isShooting && !isLanding) {
					((SpaceInvadersScreen) screen).shoot(getCenterX(), getCenterY(), ProjectileType.ALIEN);
				}
				
			} 
		}

		if (explosionFrame == 6) {
			((SpaceInvadersScreen) screen).removeAlien();
			setDispose();
		}
	}
	
	private void collidedWithObject(Screen screen) {
		for (ActiveSprite sprite : screen.getActiveSprites()) {
			if (sprite instanceof ProjectileSprite && ((ProjectileSprite) sprite).getType() == ProjectileType.TURRET) {
				isDead = CollisionDetection.overlaps(
						getMinX(),
						getMinY(),
						getMaxX(), 
						getMaxY(),
						sprite.getMinX(),
						sprite.getMinY(),
						sprite.getMaxX(),
						sprite.getMaxY());


				if (isDead) {
					((SpaceInvadersScreen) screen).addScore(pointValue);
					explosionSound.playAsynchronous("res/alienExplosion.wav");
					sprite.setDispose();
					break;
				}
			}
		}
		
		for (StaticSprite sprite : screen.getStaticSprites()) {
			if (sprite instanceof BarrierSprite) {
				boolean onScreenEdge = CollisionDetection.overlaps(
						getMinX(),
						getMinY(), 
						getMaxX(), 
						getMaxY(),
						sprite.getMinX(),
						sprite.getMinY(),
						sprite.getMaxX(),
						sprite.getMaxY());
				
				if (!shiftY && !changedDirection && onScreenEdge) {
					((SpaceInvadersScreen) screen).shiftAliensDown();
					break;
				}
			}
		}
	}

	void shiftY() {
		shiftY = true;
	}

	void setIncreasedSpeed(int speed) {
		shiftPeriod = speed;
	}

	void setIncreasedShootingFrequency(double probability) {
		shootingProbability = probability;
	}

	void land() {
		isLanding = true;
		shiftPeriod = INITIAL_SHIFT_PERIOD;
	}
}


