import java.util.ArrayList;

public class SpaceInvadersScreen extends Screen {

	private boolean isPaused = false;
	private boolean isGameOver = false;
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

		if (!isPaused && !isGameOver) {
			if (player.isDead()) {
				livesLeft -= 1;
				// remove one counter from the lives
			}
			
			for (ProjectileSprite sprite : projectileSprites) {
				getActiveSprites().add(sprite);
			}
			projectileSprites.clear();

			updateSprites(keyboard, actual_delta_time);
			disposeSprites();
		}
		
		if (livesLeft < 1) {
			isGameOver = true;
		}
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
