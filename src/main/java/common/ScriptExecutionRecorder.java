package common;

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;
import org.monte.media.Format;
import org.monte.media.FormatKeys.MediaType;
import org.monte.media.Registry;
import org.monte.media.math.Rational;
import org.monte.screenrecorder.ScreenRecorder;
import static org.monte.media.AudioFormatKeys.*;
import static org.monte.media.VideoFormatKeys.*;

/**
 * This class will contain all the methods for recording the screen.
 * @author spandit
 * @lastmodifiedby spandit
 */
public class ScriptExecutionRecorder extends ScreenRecorder {

	private static ScreenRecorder screenRecorder;
	private static String name;
	private static boolean isRecorderStopped = false;

	public ScriptExecutionRecorder(GraphicsConfiguration cfg, Rectangle captureArea, Format fileFormat, Format screenFormat,
			Format mouseFormat, Format audioFormat, File movieFolder) throws IOException, AWTException {
		super(cfg, captureArea, fileFormat, screenFormat, mouseFormat, audioFormat, movieFolder);
	}

	/**
	 * This method is used to Create a file for recording the screen.
	 * @param fileFormat File Format
	 * @return File
	 * @author spandit
	 * @lastmodifiedby spandit
	 */
	@Override
	protected File createMovieFile(Format fileFormat) throws IOException {
		if (!movieFolder.exists()) {
			movieFolder.mkdirs();
		} else if (!movieFolder.isDirectory()) {
			throw new IOException("\"" + movieFolder + "\" is not a directory.");
		}
		return new File(movieFolder,
				name +  "." + Registry.getInstance().getExtension(fileFormat));
	}

	/**
	 * This method is used to start the screen recording.
	 * @param methodName Method Name
	 * @author spandit
	 * @lastmodifiedby spandit
	 */
	public static void startRecord(String methodName) throws IOException, AWTException {
		File file = new File(System.getProperty("user.dir") + File.separator + "screenshotsAndRecordings");
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int width = screenSize.width;
		int height = screenSize.height;

		Rectangle captureSize = new Rectangle(0, 0, width, height);

		GraphicsConfiguration gc = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice()
				.getDefaultConfiguration();
		screenRecorder = new ScriptExecutionRecorder(gc, captureSize,
				new Format(MediaTypeKey, MediaType.FILE, MimeTypeKey, MIME_AVI),
				new Format(MediaTypeKey, MediaType.VIDEO, EncodingKey, ENCODING_AVI_TECHSMITH_SCREEN_CAPTURE,
						CompressorNameKey, ENCODING_AVI_TECHSMITH_SCREEN_CAPTURE, DepthKey, 24, FrameRateKey,
						Rational.valueOf(15), QualityKey, 1.0f, KeyFrameIntervalKey, 15 * 60),
				new Format(MediaTypeKey, MediaType.VIDEO, EncodingKey, "black", FrameRateKey, Rational.valueOf(30)),
				null, file);
		name = methodName;
		screenRecorder.start();
		setScreenRecorderStatus(false);
	}

	/**
	 * This method is used to stop the screen recording.
	 * @author spandit
	 * @lastmodifiedby spandit
	 */
	public static void stopRecord() throws IOException {
		screenRecorder.stop();
		isRecorderStopped = true;
		screenRecorder = null;
	}

	/**
	 * This method is used to verify whether the screen recording is completed or not.
	 * @return boolean
	 * @author spandit
	 * @lastmodifiedby spandit
	 */
	public static boolean isRecordingCompleted() {
		return isRecorderStopped;
	}
	
	/**
	 * This method is used to set the Screen recorder Status.
	 * @param status Status
	 * @author spandit
	 * @lastmodifiedby spandit
	 */
	public static void setScreenRecorderStatus(boolean status) {
		isRecorderStopped = status;
	}
	
	/**
	 * This method is used to get the Screen Recorder Object.
	 * @return Screen Recorder Object
	 * @author spandit
	 * @lastmodifiedby spandit
	 */
	public static ScreenRecorder getScreenRecorder() {
		return screenRecorder;
	}
}
