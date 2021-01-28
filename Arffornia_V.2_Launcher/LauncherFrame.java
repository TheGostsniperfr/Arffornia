package fr.thegostsniper.thegostlauncher;

import javax.swing.JFrame;

import com.sun.awt.AWTUtilities;

import fr.theshark34.openlauncherlib.util.CrashReporter;
import fr.theshark34.swinger.Swinger;
import fr.theshark34.swinger.animation.Animator;
import fr.theshark34.swinger.util.WindowMover;

@SuppressWarnings("serial")
public class LauncherFrame extends JFrame {
	
	private static LauncherFrame instance;
	private LauncherPanel launcherPanel;
	private static CrashReporter crashReporter;
	
	public LauncherFrame() {
		this.setTitle("Arffornia Launcher");
		this.setSize(950, 534);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		this.setUndecorated(true);
		this.setIconImage(Swinger.getResource("icon.png"));
		this.setContentPane(launcherPanel = new LauncherPanel());
		AWTUtilities.setWindowOpacity(this, 0.0F);

		
		WindowMover mover = new WindowMover(this);
		this.addMouseListener(mover);
		this.addMouseMotionListener(mover);
		
		
		this.setVisible(true);
		
		Animator.fadeInFrame(this, 3);
	}



	public static void main(String[] args) {
		Swinger.setSystemLookNFeel();
		Swinger.setResourcePath("/fr/thegostsniper/thegostlauncher/resources/");
		Launcher.TG_CRASH_FOLDER.mkdirs();
		crashReporter = new CrashReporter("Arffornia Launcher", Launcher.TG_CRASH_FOLDER);
		
		instance = new LauncherFrame();
	}	
	
	public static LauncherFrame getInstance() {
		return instance;
	}
	
	public static CrashReporter getCrashReporter() {
		return crashReporter;
	}
	
	public LauncherPanel getLauncherPanel() {
		return this.launcherPanel;
	}
}
