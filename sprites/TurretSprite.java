import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class TurretSprite extends ActiveSprite {

    private final static double SPEED = 200;
    private final static int WIDTH = 32;
    private final static int HEIGHT = 32;
    private final static int SHOOTING_COOLDOWN = 500;

    private AudioPlayer explosionSound = new AudioPlayer();

    private boolean isOnRightEdge = false;
    private boolean isOnLeftEdge = false;
    private boolean isDead = false;
    private Image turretImage;
    private Image[] explosionFrames;
    private int explosionFrame = -1;
    private long lastShotTime;

    TurretSprite(double centerX, double centerY) {
        super();
        setCenterX(centerX);
        setCenterY(centerY);
        setWidth(WIDTH);
        setHeight(HEIGHT);

        try {
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
    }

    @Override
    public Image getImage() {
        if (!isDead) {
            return turretImage;
        } else {
            if (explosionFrame != 6) {
                explosionFrame++;
            }
            return explosionFrames[explosionFrame];
        }
    }

    @Override
    public void update(Screen screen, KeyboardInput keyboard, long actual_delta_time) {
        collidedWithObject(screen);

        lastShotTime += actual_delta_time;
        if (lastShotTime >= SHOOTING_COOLDOWN && !isDead && keyboard.keyDownOnce(32)) {
            ((SpaceInvadersScreen) screen).shoot(getCenterX(), getCenterY(), 0);
            lastShotTime = 0;
        }

        if (!isOnRightEdge && keyboard.keyDown(39)) {
            isOnLeftEdge = false;
            setCenterX(getCenterX() + (SPEED * actual_delta_time * 0.001));
        } else if (!isOnLeftEdge && keyboard.keyDown(37)) {
            isOnRightEdge = false;
            setCenterX(getCenterX() + (-SPEED * actual_delta_time * 0.001));
        }

        if (explosionFrame == 6) {
            setDispose();
        }
    }

    private void collidedWithObject(Screen screen) {
        for (ActiveSprite activeSprite : screen.getActiveSprites()) {
            if (activeSprite instanceof ProjectileSprite) {
                isDead = CollisionDetection.overlaps(
                        getMinX(),
                        getMaxY(),
                        getMaxX(),
                        getMinY(),
                        activeSprite.getMinX() + activeSprite.getCenterX(),
                        activeSprite.getMaxY() + activeSprite.getCenterY(),
                        activeSprite.getMaxX() + activeSprite.getCenterX(),
                        activeSprite.getMinY() + activeSprite.getCenterY());


                if (isDead) {
                    explosionSound.playAsynchronous("res/explosion_1.wav");
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
}
