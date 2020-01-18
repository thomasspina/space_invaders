import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class FlyingSaucerSprite extends ActiveSprite {
    final private static int WIDTH = 32;
    final private static int HEIGHT = 32;
    final private static double SPEED = -1.75;
    final private static int POINT_VALUE = 250;

    private AudioPlayer explosionSound = new AudioPlayer();

    private Image image;
    private Image[] explosionFrames;
    private int explosionFrame = -1;

    private boolean isDead = false;

    FlyingSaucerSprite(double CenterX, double CenterY) {
        super();
        setWidth(WIDTH);
        setHeight(HEIGHT);
        setCenterX(CenterX);
        setCenterY(CenterY);

        try {
            image = ImageIO.read(new File("res/saucer/saucer_0.png"));
            explosionFrames = new Image[] {
                    ImageIO.read(new File("res/saucerExplosion/saucerExplosion_00.png")),
                    ImageIO.read(new File("res/saucerExplosion/saucerExplosion_01.png")),
                    ImageIO.read(new File("res/saucerExplosion/saucerExplosion_02.png")),
                    ImageIO.read(new File("res/saucerExplosion/saucerExplosion_03.png")),
                    ImageIO.read(new File("res/saucerExplosion/saucerExplosion_04.png")),
                    ImageIO.read(new File("res/saucerExplosion/saucerExplosion_05.png")),
                    ImageIO.read(new File("res/saucerExplosion/saucerExplosion_06.png")),
                    ImageIO.read(new File("res/saucerExplosion/saucerExplosion_07.png")),
                    ImageIO.read(new File("res/saucerExplosion/saucerExplosion_08.png")),
                    ImageIO.read(new File("res/saucerExplosion/saucerExplosion_09.png")),
                    ImageIO.read(new File("res/saucerExplosion/saucerExplosion_10.png")),
                    ImageIO.read(new File("res/saucerExplosion/saucerExplosion_11.png")),
                    ImageIO.read(new File("res/saucerExplosion/saucerExplosion_12.png")),
                    ImageIO.read(new File("res/saucerExplosion/saucerExplosion_13.png")),
                    ImageIO.read(new File("res/saucerExplosion/saucerExplosion_14.png")),
                    ImageIO.read(new File("res/saucerExplosion/saucerExplosion_15.png"))
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
            if (explosionFrame != 15) {
                explosionFrame++;
            }
            return explosionFrames[explosionFrame];
        }
    }

    @Override
    public void update(Screen screen, KeyboardInput keyboard, long actual_delta_time) {
        collidedWithObject(screen);
        setCenterX(getCenterX() + SPEED);

        if (explosionFrame == 15 || isOutOfBounds()) {
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
