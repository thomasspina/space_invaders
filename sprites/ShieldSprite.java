import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class ShieldSprite extends StaticSprite {
    private final static int HITS_MAX = 12;

    private Image[] shieldImages = new Image[6];
    private Image[] shieldExplosion = new Image[8];
    private int explosionFrame = -1;

    private boolean isDestroyed = false;
    private int hitCounter = 0;

    private AudioPlayer hitSound = new AudioPlayer();


    ShieldSprite(double centerX, double centerY) {
        super();
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

        } else {
            if (explosionFrame != 8) {
                explosionFrame++;
            }
            return shieldExplosion[explosionFrame];
        }
    }

    public void update(Screen screen, KeyboardInput keyboard, long actual_delta_time) {
        collidedWithObject(screen);
    }

    private void collidedWithObject(Screen screen) {
        for (ActiveSprite sprite : screen.getActiveSprites()) {
            if (sprite instanceof ProjectileSprite) {
                boolean isHit = CollisionDetection.overlaps(
                        getMinX(),
                        getMinY(),
                        getMaxX(),
                        getMaxY(),
                        sprite.getMinX(),
                        sprite.getMinY(),
                        sprite.getMaxX(),
                        sprite.getMaxY());

                if (isHit) {
                    if (((ProjectileSprite) sprite).getType() == ProjectileType.ALIEN) {
                        hitCounter++;
                        hitSound.playAsynchronous("res/projectileShieldExplosion.wav");
                        ((ProjectileSprite) sprite).hasHitShield();
                    } else {
                        sprite.setDispose();
                    }
                }
            }
        }
    }
}
