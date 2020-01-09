import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class SpaceInvadersScreen extends Screen {

	private boolean isPaused = false;
	private boolean isGameOver = false;
	private boolean isDeathAnimation = false;
	private ArrayList<ProjectileSprite> projectileSprites = new ArrayList<>();
	private int score = 0;
	private int livesLeft = 3;
	private TurretSprite player;
	
	SpaceInvadersScreen() {
		super();
		
		BarrierSprite rightBarrier = new BarrierSprite(345, -250, 335, 270);
		BarrierSprite leftBarrier = new BarrierSprite(-363, -250, -353, 270);
		
		int alienType = 3;
		int yPosition = 0;
		for (int i = 0; i < 5; i++) {
			int xPosition = -300;
			for (int j = 0; j < 11; j++) {
				AlienSprite alien = new AlienSprite(alienType, xPosition, yPosition);
				activeSprites.add(alien);
				xPosition += 40;
			}
			yPosition -= 40;
			if (i == 1 || i == 3) {
				alienType--;
			}
		}
		
		player = new TurretSprite(0, 257);
		activeSprites.add(player);

		staticSprites.add(rightBarrier);
		staticSprites.add(leftBarrier);
	}

	void shoot(double centerX, double centerY, ProjectileType type) {
		projectileSprites.add(new ProjectileSprite(centerX, centerY, type));
	}

	@Override
	public void update(KeyboardInput keyboard, long actual_delta_time) {
		if (keyboard.keyDownOnce(80)) {
			isPaused ^= true;
		}

		if (livesLeft < 0) {
			isGameOver = true;
		}
		
		if (!isPaused && !isGameOver) {
			if (player.isDead()) {
				livesLeft -= 1;
				if (livesLeft > -1) {
					isPaused = true;
					isDeathAnimation = true;
				}
			}
			
			for (ProjectileSprite sprite : projectileSprites) {
				getActiveSprites().add(sprite);
			}
			projectileSprites.clear();

			updateSprites(keyboard, actual_delta_time);
			disposeSprites();
		}
		
		if (isDeathAnimation && isPaused) {
			boolean deathAnimationComplete = updatePlayer(keyboard, actual_delta_time);
			disposeSprites();
			if (deathAnimationComplete) {
				try {
					TimeUnit.MILLISECONDS.sleep(500);
				} catch (InterruptedException e) {
					System.err.println(e.toString());
				}
				
				player = new TurretSprite(0, 257);
				getActiveSprites().add(player);
				isDeathAnimation = false;
				isPaused = false;
			}
		}
	}

	boolean updatePlayer(KeyboardInput keyboard, long actual_delta_time) {
		boolean deathComplete = true;
		for (ActiveSprite activeSprite : activeSprites) {
			if (activeSprite instanceof TurretSprite) {
				deathComplete = false;
				activeSprite.update(this, keyboard, actual_delta_time);
			}
    	}  
		return deathComplete;
	}
	
	public void addScore(int score) {
		this.score += score;
	}

	public int getScore() {
		return score;
	}
	
	public boolean isGameOver() {
		return isGameOver;
	}
}
