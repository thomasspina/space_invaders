import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class SpaceInvadersScreen extends Screen {
	final private static double SAUCER_SPAWN_PROB = 0.00001;
	private boolean isPaused = false;
	private boolean isGameOver = false;
	private boolean isDeathAnimation = false;
	private ArrayList<ProjectileSprite> projectileSprites = new ArrayList<>();
	private int score = 0;
	private int livesLeft = 3;
	private int aliensCount = 55;
	private boolean aliensCleared = false;
	private boolean isSaucerSpawned = false;
	private TurretSprite player;
	
	SpaceInvadersScreen() {
		super();
		
		BarrierSprite rightBarrier = new BarrierSprite(345, -250, 335, 270);
		BarrierSprite leftBarrier = new BarrierSprite(-363, -250, -353, 270);

		spawnAliens();
		int yPosition = 200;
		int xPosition = -300;

		for (int i = 0; i < 8; i++) {
			ShieldSprite shield = new ShieldSprite(xPosition, yPosition);
			activeSprites.add(shield);
			xPosition += 80;
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
			if (aliensCleared) {
				try {
					TimeUnit.MILLISECONDS.sleep(2000);
				} catch (InterruptedException e) {
					System.err.println(e.toString());
				}
				aliensCount = 55;
				spawnAliens();

				for (int i = 0; i < getStaticSprites().size(); i++) {
					if (getStaticSprites().get(i).getImage() != null) {
						getStaticSprites().remove(i);
						break;
					}
				}

				aliensCleared = false;
			}

			if (player.isDead() && !player.isAlienLanding()) {
				livesLeft -= 1;
				if (livesLeft > -1) {
					isPaused = true;
					isDeathAnimation = true;
				}
			}

			if (!isSaucerSpawned) {
				if (Math.random() <= SAUCER_SPAWN_PROB) {
					getActiveSprites().add(new FlyingSaucerSprite(450, -250));
					isSaucerSpawned = true;
				}
			}

			for (ProjectileSprite sprite : projectileSprites) {
				getActiveSprites().add(sprite);
				sprite.playSound();
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
					TimeUnit.MILLISECONDS.sleep(1000);
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

	private boolean updatePlayer(KeyboardInput keyboard, long actual_delta_time) {
		boolean deathComplete = true;
		for (ActiveSprite activeSprite : activeSprites) {
			if (activeSprite instanceof TurretSprite) {
				deathComplete = false;
				activeSprite.update(this, keyboard, actual_delta_time);
			}
    	}  
		return deathComplete;
	}
	
	void addScore(int score) {
		this.score += score;
	}

	int getScore() {
		return score;
	}
	
	boolean isGameOver() {
		return isGameOver;
	}

	void alienHasLanded() {
		isGameOver = true;
		livesLeft = 0;
	}

	int getLivesLeft() {
		return livesLeft;
	}

	void flyingSaucerExploded() {
		isSaucerSpawned = false;
	}

	void removeAlien() {
		aliensCount--;
		switch (aliensCount) {
			case 50:
				setAllAliensIncreasedStats(480, 0.007);
				break;
			case 45:
				setAllAliensIncreasedStats(430, 0.007);
				break;
			case 40:
				setAllAliensIncreasedStats(380, 0.007);
				break;
			case 35:
				setAllAliensIncreasedStats(330, 0.007);
				break;
			case 30:
				setAllAliensIncreasedStats(280, 0.007);
				break;
			case 25:
				setAllAliensIncreasedStats(240, 0.007);
				break;
			case 20:
				setAllAliensIncreasedStats(200, 0.008);
				break;
			case 15:
				setAllAliensIncreasedStats(170, 0.009);
				break;
			case 10:
				setAllAliensIncreasedStats(140, 0.01);
				break;
			case 5:
				setAllAliensIncreasedStats(100, 0.02);
				break;
			case 1:
				setAllAliensIncreasedStats(75, 0.05);
				break;
			case 0:
				aliensCleared = true;
				BarrierSprite panel = new BarrierSprite(-75, -178,53,-50);
				try {
					panel.setImage(ImageIO.read(new File("res/roundClear/roundClear_0.png")));
				} catch (IOException e) {
					System.err.println(e.toString());
				}
				getStaticSprites().add(panel);
				break;
		}
	}

	private void setAllAliensIncreasedStats(int newSpeed, double newShootingProbability) {
		for (ActiveSprite sprite : getActiveSprites()) {
			if (sprite instanceof AlienSprite) {
				((AlienSprite) sprite).setIncreasedSpeed(newSpeed);
				((AlienSprite) sprite).setIncreasedShootingFrequency(newShootingProbability);
			}
		}
	}

	void shiftAliensDown() {
		for (ActiveSprite sprite : getActiveSprites()) {
			if (sprite instanceof AlienSprite) {
				((AlienSprite) sprite).shiftY();
			}
		}
	}

	private void spawnAliens() {
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
	}
}
