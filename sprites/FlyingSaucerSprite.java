import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class FlyingSaucerSprite extends ActiveSprite {
    final private static int WIDTH = 32;
    final private static int HEIGHT = 32;
    final private static double SPEED = 1.75;
    final private static int POINT_VALUE = 250;

    private AudioPlayer explosionSound = new AudioPlayer();

    private Image image;
    private Image[] explosionFrames;
    private int explosionFrame = -1;

    private boolean isDead = false;
    private int direction = 1;

    FlyingSaucerSprite(double CenterX, double CenterY) {
        super();
        setWidth(WIDTH);
        setHeight(HEIGHT);
        setCenterX(CenterX);
        setCenterY(CenterY);

        try {
            image = ImageIO.read(new File("res/saucer/saucer_0.png"));
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
            return image;
        } else {
            if (explosionFrame != 6) {
                explosionFrame++;
            }
            return explosionFrames[explosionFrame];
        }
    }

    @Override
    public void update(Screen screen, KeyboardInput keyboard, long actual_delta_time) {

        if (isOutOfBounds()) {
            direction *= -1;
        }

        collidedWithObject(screen);
        setCenterX(getCenterX() + (SPEED * direction));

        if (explosionFrame == 6) {
            ((SpaceInvadersScreen) screen).flyingSaucerExploded();
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
                    ((SpaceInvadersScreen) screen).addScore(POINT_VALUE);
                    explosionSound.playAsynchronous("res/saucerExplosion.wav");
                    sprite.setDispose();
                    break;
                }
            }
        }
    }

    private boolean isOutOfBounds() {
        return getCenterX() <= -500 || getCenterX() >= 500;
    }
}
