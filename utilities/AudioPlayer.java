import java.io.File;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class AudioPlayer implements LineListener {
	private boolean playCompleted = true;
	private boolean stop = false;

	@Override
	public void update(LineEvent event) {
		LineEvent.Type type = event.getType();

		if (type == LineEvent.Type.STOP) {
			playCompleted = true;
		}

	}

	void playAsynchronous(String file)
	{
		Thread audioThread;

		stop = false;
		audioThread = new Thread(() -> {
			playCompleted = false;
			play(file);
			playCompleted = true;
		});

		audioThread.start();
	}	 

	private void play(String audioFilePath) {
		File audioFile = new File(audioFilePath);

		try {
			AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);

			AudioFormat format = audioStream.getFormat();

			DataLine.Info info = new DataLine.Info(Clip.class, format);

			Clip audioClip = (Clip) AudioSystem.getLine(info);

			audioClip.addLineListener(this);

			audioClip.open(audioStream);

			audioClip.start();

			playCompleted = false;

			while (!playCompleted) {
				try {
					Thread.sleep(100);
				} catch (InterruptedException ex) {
					ex.printStackTrace();
				}
				if (stop) {
					audioClip.stop();
					playCompleted = true;
				}

			}

			audioClip.close();

		} catch (UnsupportedAudioFileException ex) {
			System.out.println("The specified audio file is not supported.");
			playCompleted = true;
		} catch (LineUnavailableException ex) {
			System.out.println("Audio line for playing back is unavailable.");
			playCompleted = true;
		} catch (Exception ex) {
			System.out.println("Error playing the audio file.");
			playCompleted = false;
		}

	}
}
