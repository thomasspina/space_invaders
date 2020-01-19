import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class TurretSprite extends ActiveSprite {

    private final static double SPEED = 200;
    private final static int WIDTH = 32;
    private final static int HEIGHT = 32;
    private final static int RESPAWN_SHOOT_DELAY = 500;
    private final static int BLINK_FREQUENCY = 200;
    private AudioPlayer explosionSound = new AudioPlayer();
    private AudioPlayer respawnSound = new AudioPlayer();
    private boolean alienIsLanding = false;
    private boolean isSpawning = true;
    private boolean isOnRightEdge = false;
    private boolean isOnLeftEdge = false;
    private boolean isDead = false;
    private Image turretImage;
    private Image blinkTurretImage;
    private Image[] explosionFrames;
    private int explosionFrame = -1;
    private int spawnBlinkCount = 0;
    private long previousBlink = 0;
    private long currentTime = 0;
    private boolean onFirstRespawnFrame = true;

    TurretSprite(double centerX, double centerY) {
        super();
        setCenterX(centerX);
        setCenterY(centerY);
        setWidth(WIDTH);
        setHeight(HEIGHT);

        try {
        	blinkTurretImage = ImageIO.read(new File("res/turret/turretSpawn_0.png"));
            turretImage = ImageIO.read(new File("res/turret/turret_0.png"));
            explosionFrames = new Image[] {
                    ImageIO.read(new File("res/turretExplosion/explosion_0.png")),
                    ImageIO.read(new File("res/turretExplosion/explosion_1.png")),
                    ImageIO.read(new File("res/turretExplosion/explosion_2.png")),
                    ImageIO.read(new File("res/turretExplosion/explosion_3.png")),
                    ImageIO.read(new File("res/turretExplosion/explosion_4.png")),
                    ImageIO.read(new File("res/turretExplosion/explosion_5.png")),
                    ImageIO.read(new File("res/turretExplosion/explosion_6.png"))
            };
        } catch (IOException e) {
            System.err.println(e.toString());
        }

        respawnSound.playAsynchronous("res/respawn.wav");
    }

    @Override
    public Image getImage() {
    	if (isSpawning) {
    		if (spawnBlinkCount > 14) {
    			isSpawning = false;
    		}
    		
    		if (currentTime - BLINK_FREQUENCY > previousBlink) {
    			spawnBlinkCount++;
    			previousBlink = currentTime;
    			onFirstRespawnFrame ^= true;
    		}
    		
    		return onFirstRespawnFrame ? blinkTurretImage : turretImage;
    		
    	} else {
    		if (!isDead) {
                return turretImage;
            } else {
                if (explosionFrame != 6) {
                    explosionFrame++;
                }
                return explosionFrames[explosionFrame];
            }
    	}
    }

    @Override
    public void update(Screen screen, KeyboardInput keyboard, long actual_delta_time) {
    	currentTime += actual_delta_time;
    	
    	if (!isDead) {
    		collidedWithObject(screen);
    		
    		boolean canShoot = true;
            for (ActiveSprite sprite : screen.getActiveSprites()) {
                if (sprite instanceof ProjectileSprite && ((ProjectileSprite) sprite).getType() == ProjectileType.TURRET) {
                    canShoot = false;
                    break;
                }
            }

            if (canShoot && keyboard.keyDownOnce(32) && currentTime >= RESPAWN_SHOOT_DELAY) {
                ((SpaceInvadersScreen) screen).shoot(getCenterX(), getCenterY(), ProjectileType.TURRET);
            }

            if (!isOnRightEdge && keyboard.keyDown(39)) {
                isOnLeftEdge = false;
                setCenterX(getCenterX() + (SPEED * actual_delta_time * 0.001));
            } else if (!isOnLeftEdge && keyboard.keyDown(37)) {
                isOnRightEdge = false;
                setCenterX(getCenterX() + (-SPEED * actual_delta_time * 0.001));
            }
    	}
        

        if (explosionFrame == 6) {
            setDispose();
        }
    }

    private void collidedWithObject(Screen screen) {
        for (ActiveSprite sprite : screen.getActiveSprites()) {
            if (sprite instanceof ProjectileSprite && ((ProjectileSprite) sprite).getType() == ProjectileType.ALIEN) {
            	isDead = CollisionDetection.overlaps(getMinX(), getMinY(), getMaxX(), getMaxY(),
                        sprite.getMinX(), sprite.getMinY(), sprite.getMaxX(), sprite.getMaxY());

            	if (isDead && isSpawning) {
            	    if (CollisionDetection.covers(getMinX(), getMinY(), getMaxX(), getMaxY(),
                            sprite.getMinX(), sprite.getMinY(), sprite.getMaxX(), sprite.getMaxY())) {
                        sprite.setDispose();
                    }
            		isDead = false;
            	}
            	
                if (isDead) {
                    explosionSound.playAsynchronous("res/turretExplosion.wav");
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

                if (onScreenEdge) {
                    if (getCenterX() > 0) {
                        isOnRightEdge = true;
                    } else {
                        isOnLeftEdge = true;
                    }
                }
            }
        }
    }

    void explode() {
        isDead = true;
        alienIsLanding = true;
        explosionSound.playAsynchronous("res/turretExplosion.wav");
    }

    boolean isDead() { return isDead; }
	boolean isAlienLanding() { return alienIsLanding; }
}
