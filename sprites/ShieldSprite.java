import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class ShieldSprite extends ActiveSprite {
    private final static int WIDTH = 40;
    private final static int HEIGHT = 40;

    private Image[] shieldImages = new Image[6];
    private Image[] shieldExplosion = new Image[8];
    private int explosionFrame = -1;

    private boolean isDestroyed = false;
    private int hitCounter = 0;

    private AudioPlayer hitSound = new AudioPlayer();
    private AudioPlayer explosionSound = new AudioPlayer();

    ShieldSprite(double centerX, double centerY) {
        super();
        setCenterX(centerX);
        setCenterY(centerY);
        setHeight(HEIGHT);
        setWidth(WIDTH);

        try {
            shieldImages[0] = ImageIO.read(new File("res/shield/shield_0.png"));
            shieldImages[1] = ImageIO.read(new File("res/shield/shield_1.png"));
            shieldImages[2] = ImageIO.read(new File("res/shield/shield_2.png"));
            shieldImages[3] = ImageIO.read(new File("res/shield/shield_3.png"));
            shieldImages[4] = ImageIO.read(new File("res/shield/shield_4.png"));
            shieldImages[5] = ImageIO.read(new File("res/shield/shield_5.png"));

            shieldExplosion[0] = ImageIO.read(new File("res/shieldExplosion/shieldExplosion_0.png"));
            shieldExplosion[1] = ImageIO.read(new File("res/shieldExplosion/shieldExplosion_1.png"));
            shieldExplosion[2] = ImageIO.read(new File("res/shieldExplosion/shieldExplosion_2.png"));
            shieldExplosion[3] = ImageIO.read(new File("res/shieldExplosion/shieldExplosion_3.png"));
            shieldExplosion[4] = ImageIO.read(new File("res/shieldExplosion/shieldExplosion_4.png"));
            shieldExplosion[5] = ImageIO.read(new File("res/shieldExplosion/shieldExplosion_5.png"));
            shieldExplosion[6] = ImageIO.read(new File("res/shieldExplosion/shieldExplosion_6.png"));
            shieldExplosion[7] = ImageIO.read(new File("res/shieldExplosion/shieldExplosion_7.png"));
        } catch (IOException e) {
            System.err.println(e.toString());
        }
    }

    @Override
    public Image getImage() {
        if (!isDestroyed) {
            return shieldImages[hitCounter];
        } else {
            if (explosionFrame != 7) {
                explosionFrame++;
            }
            return shieldExplosion[explosionFrame];
        }
    }

    @Override
    public void update(Screen screen, KeyboardInput keyboard, long actual_delta_time) {
        collidedWithObject(screen);

        if (explosionFrame == 7) {
            setDispose();
        }
    }

    private void collidedWithObject(Screen screen) {
        for (ActiveSprite sprite : screen.getActiveSprites()) {
            if (sprite instanceof AlienSprite) {
                boolean isHit = CollisionDetection.pixelBasedOverlaps(this, sprite);

                if (isHit) {
                    for (ActiveSprite activeSprite : screen.getActiveSprites()) {
                        if (activeSprite instanceof ShieldSprite) {
                            ((ShieldSprite) activeSprite).explode();
                        } else if (activeSprite instanceof TurretSprite) {
                            ((TurretSprite) activeSprite).setAlienIsLanding();
                        }
                    }

                    ((AlienSprite) sprite).land();
                    explosionSound.playAsynchronous("res/shieldExplosion.wav");
                    break;
                }

            } else if (sprite instanceof ProjectileSprite) {
                boolean isHit = CollisionDetection.pixelBasedOverlaps(this, sprite);

                if (isHit) {
                    if (((ProjectileSprite) sprite).getType() == ProjectileType.ALIEN) {
                        hitSound.playAsynchronous("res/projectileShieldExplosion.wav");
                        sprite.setDispose();

                        if (hitCounter == 5) {
                            isDestroyed = true;
                            explosionSound.playAsynchronous("res/shieldExplosion.wav");
                        } else {
                            hitCounter++;
                        }
                    } else {
                        sprite.setDispose();
                    }
                }
            }
        }
    }

    private void explode() {
        isDestroyed = true;
    }
}
