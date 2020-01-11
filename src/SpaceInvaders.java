import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.Timer;

public class SpaceInvaders extends JFrame {
	
	final public static int FRAMES_PER_SECOND = 60;
	final public static int SCREEN_HEIGHT = 600;
	final public static int SCREEN_WIDTH = 750;
	final private static String HIGH_SCORE_FILE = "score.txt";
	
	private int xpCenter = SCREEN_WIDTH / 2;
	private int ypCenter = SCREEN_HEIGHT / 2;
	private double scale = 1;
	private double xCenter = 0;		
	private double yCenter = 0;
	
	private JPanel panel;
	private JLabel lblScore;
	private FadeLabel lblFadeLabel;
	private JLabel lblMiddleLabel;
	private JLabel lblScoreText;
	BufferedImage livesCounterImage;

	Timer fadeTimer;
	private static Thread game;
	
	private long current_time = 0;
	private long next_refresh_time = 0;
	private long last_refresh_time = 0;
	private long minimum_delta_time = 1000 / FRAMES_PER_SECOND;
	private long actual_delta_time = 0;
	private boolean isPaused = false;
	private float fadeDirection = -0.07f;
	private String score;
	
	private boolean playing = false;
	private boolean isGameOver = false;
	
	private ArrayList<ActiveSprite> activeSprites = null;
	private ArrayList<StaticSprite> staticSprites = null;
	
	private KeyboardInput keyboard = new KeyboardInput();
	private Serializer serializer = new Serializer();
	private Screen screen = null;
	
	public SpaceInvaders() {
		super();
		setFocusable(true);
		setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBackground(Color.BLACK);
		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				this_windowClosing(e);
			}
		});
		
		addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent arg0) {
				keyboard.keyPressed(arg0);
			}
			@Override
			public void keyReleased(KeyEvent arg0) {
				keyboard.keyReleased(arg0);
			}
		});
		
		try {
			score = (String) serializer.deserialize(HIGH_SCORE_FILE);
		} catch (Exception e) {
			score = "0";
		}

		try {
			livesCounterImage = ImageIO.read(new File("res/turret/turret_0.png"));
		} catch (IOException e) {
			System.err.println(e.toString());
		}

		Container cp = getContentPane();
		cp.setBackground(Color.BLACK);
		cp.setLayout(null);

		panel = new DrawPanel();
		panel.setLayout(null);
		panel.setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
		getContentPane().add(panel, BorderLayout.CENTER);
		
		lblMiddleLabel = new JLabel("Space Invaders", SwingConstants.CENTER);
		lblMiddleLabel.setFont(new Font("Lucida Console", Font.BOLD, 70));
		lblMiddleLabel.setForeground(Color.WHITE);
		lblMiddleLabel.setBounds(10, 200, 714, 84);
		getContentPane().add(lblMiddleLabel);
		getContentPane().setComponentZOrder(lblMiddleLabel, 0);
		
		lblScoreText = new JLabel("High Score:");
		lblScoreText.setFont(new Font("Lucida Console", Font.PLAIN, 16));
		lblScoreText.setForeground(Color.WHITE);
		lblScoreText.setBounds(10, 11, 149, 30);
		getContentPane().add(lblScoreText);
		getContentPane().setComponentZOrder(lblScoreText, 0);
		
		lblScore = new JLabel(score, SwingConstants.RIGHT);
		lblScore.setForeground(Color.WHITE);
		lblScore.setFont(new Font("Lucida Console", Font.PLAIN, 16));
		lblScore.setBounds(100, 20, 100, 14);
		getContentPane().add(lblScore);
		getContentPane().setComponentZOrder(lblScore, 0);
		
		lblFadeLabel = new FadeLabel("press SPACE to play", SwingConstants.CENTER);
		lblFadeLabel.setFont(new Font("Lucida Console", Font.PLAIN, 29));
		lblFadeLabel.setForeground(Color.WHITE);
		lblFadeLabel.setBounds(10, 308, 714, 46);
		getContentPane().add(lblFadeLabel);
		getContentPane().setComponentZOrder(lblFadeLabel, 0);
		
		fadeTimer = new Timer(100, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                float alpha = lblFadeLabel.getAlpha();
                alpha += fadeDirection;
                if (alpha < 0) {
                    alpha = 0;
                    fadeDirection = 0.07f;
                } else if (alpha > 1) {
                    alpha = 1;
                    fadeDirection = -0.07f;
                }
                lblFadeLabel.setAlpha(alpha);
            }
        });
		
		fadeTimer.setRepeats(true);
		fadeTimer.setCoalesce(true);
		fadeTimer.start();
    }
	
	public static void main(String[] args) {
		SpaceInvaders spaceInvaders = new SpaceInvaders();
		spaceInvaders.setVisible(true);
		
		game = new Thread(() -> spaceInvaders.animationLoop());
		
		game.start();
	}
	
	private void animationLoop() {

		while(true) {
			last_refresh_time = System.currentTimeMillis();
			next_refresh_time = current_time + minimum_delta_time;

			while (current_time < next_refresh_time) {
				Thread.yield();

				try {
					Thread.sleep(1);
				}
				catch(Exception e) {
					System.err.println(e.toString());
				} 

				current_time = System.currentTimeMillis();
			}
			
			keyboard.poll();
			handleKeyboardInput();
			updateTime();
			if (playing) {
				int score = ((SpaceInvadersScreen) screen).getScore();
				if (((SpaceInvadersScreen) screen).isGameOver()) {
					lblFadeLabel.setText("press SPACE to return to the main menu");
					lblFadeLabel.setVisible(true);
					fadeTimer.start();
					lblMiddleLabel.setText("Game Over");
					lblMiddleLabel.setVisible(true);
					playing = false;
					
					if (score > Integer.parseInt(this.score)) {
						try {
							serializer.serialize(String.valueOf(score), HIGH_SCORE_FILE);
						} catch (Exception e) {
							System.err.println(e.toString());
						}
					}
					
					isGameOver = true;
				} else {
					screen.update(keyboard, actual_delta_time);
					
					if (Integer.parseInt(lblScore.getText()) != score) {
						lblScore.setText(String.valueOf(score));
					}
				}
			}
			repaint();
		}
	}
	
	private void handleKeyboardInput() {
		if (keyboard.keyDownOnce(32)) {
			if (!playing) {
				if (!isGameOver) {
					screen = new SpaceInvadersScreen();
					staticSprites = screen.getStaticSprites();
					activeSprites = screen.getActiveSprites();
					lblFadeLabel.setVisible(false);
					lblMiddleLabel.setVisible(false);
					lblScoreText.setText("Score:");
					fadeTimer.stop();
					lblScore.setText("0");
					playing = true;
				} else {
					try {
						score = (String) serializer.deserialize(HIGH_SCORE_FILE);
					} catch (Exception e) {
						score = "0";
					}
					
					lblFadeLabel.setText("press SPACE to play");
					lblMiddleLabel.setText("Space Invaders");
					screen = null;
					staticSprites = null;
					activeSprites = null;
					lblScoreText.setText("High Score:");
					lblScore.setText(score);
					isGameOver = false;
				}
			}
		}
	}
	
	private void updateTime() {
		current_time = System.currentTimeMillis();
		actual_delta_time = (isPaused ? 0 : current_time - last_refresh_time);
		last_refresh_time = current_time;
	}
	
	class DrawPanel extends JPanel {
		public void paintComponent(Graphics g) {	
			if (screen == null) {
				return;
			}
			
			for (StaticSprite staticSprite : staticSprites) {
				if (staticSprite.getShowImage()) {
					if (staticSprite.getImage() != null) {
						g.drawImage(staticSprite.getImage(), translateX(staticSprite.getMinX()), translateY(staticSprite.getMinY()), scaleX(staticSprite.getWidth()), scaleY(staticSprite.getHeight()), null);
					}
				}
			}


			int position = 700;
			for (int i = 0; i < ((SpaceInvadersScreen) screen).getLivesLeft(); i++) {
				g.drawImage(livesCounterImage, position, 10, null);
				position -= 50;
			}


			for (ActiveSprite activeSprite : activeSprites) {
				if (activeSprite.getImage() != null) {
					g.drawImage(activeSprite.getImage(), translateX(activeSprite.getMinX()), translateY(activeSprite.getMinY()), scaleX(activeSprite.getWidth()), scaleY(activeSprite.getHeight()), null);
				} else {
					g.setColor(Color.BLUE);
					g.fillRect(translateX(scale * (activeSprite.getMinX())), translateY(activeSprite.getMinY()), scaleX(activeSprite.getWidth()), scaleY(activeSprite.getHeight()));					
				}
			}

		}
		
		private int translateX(double x) {
			return xpCenter + scaleX(x - xCenter);
		}
		
		private int scaleX(double x) {
			return (int) Math.round(scale * x);
		}
		private int translateY(double y) {
			return ypCenter + scaleY(y - yCenter);
		}		
		private int scaleY(double y) {
			return (int) Math.round(scale * y);
		}
	}
	
	protected void this_windowClosing(WindowEvent e) {
		try {
			serializer.serialize(score, HIGH_SCORE_FILE);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		dispose();	
	}
}
