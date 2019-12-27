import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class TurretSprite extends ActiveSprite {

    private boolean isDead = false;
    private Image turretImage;
    private Image explosionFrames[];
    private int explosionFrame = -1;

    TurretSprite() {
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
    public void update(Screen level, KeyboardInput keyboard, long actual_delta_time) {

    }
}
