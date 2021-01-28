package fr.thegostsniper.thegostlauncher;

import java.io.File;
import java.util.Arrays;

import fr.theshark34.openauth.AuthenticationException;
import fr.theshark34.openlauncherlib.LaunchException;
import fr.theshark34.openlauncherlib.external.ExternalLaunchProfile;
import fr.theshark34.openlauncherlib.external.ExternalLauncher;
import fr.theshark34.openlauncherlib.minecraft.AuthInfos;
import fr.theshark34.openlauncherlib.minecraft.GameFolder;
import fr.theshark34.openlauncherlib.minecraft.GameInfos;
import fr.theshark34.openlauncherlib.minecraft.GameTweak;
import fr.theshark34.openlauncherlib.minecraft.GameType;
import fr.theshark34.openlauncherlib.minecraft.GameVersion;
import fr.theshark34.openlauncherlib.minecraft.MinecraftLauncher;
import fr.theshark34.openlauncherlib.util.CrashReporter;
import fr.theshark34.openlauncherlib.util.ProcessLogManager;
import fr.theshark34.supdate.BarAPI;
import fr.theshark34.supdate.SUpdate;
import fr.theshark34.supdate.application.integrated.FileDeleter;
import fr.theshark34.swinger.Swinger;


public class Launcher {

	public static final GameVersion TG_VERSION = new GameVersion("1.7.10", GameType.V1_7_10);
	public static final GameInfos TG_INFOS = new GameInfos("Hello Neighbor on Minecraft", TG_VERSION, new GameTweak[] {GameTweak.FORGE});
	public static final File TG_DIR = TG_INFOS.getGameDir();
	public static final File TG_CRASH_FOLDER = new File(TG_DIR, "crashes");

	
	private static AuthInfos authInfos;
	private static Thread updateThread;
	
	
	public static void auth(String username) throws AuthenticationException {
	authInfos = new AuthInfos(username, "sry", "nope");

	}
	
	public static void update() throws Exception {
		SUpdate su = new SUpdate("http://arffornia.ddns.net:25560", TG_DIR);
		su.addApplication(new FileDeleter());
		
		updateThread = new Thread() {
			private int val;
			private int max;
			
			@Override
			public void run() {
				while(!this.isInterrupted()) {
					if(BarAPI.getNumberOfFileToDownload() == 0) {
						LauncherFrame.getInstance().getLauncherPanel().setInfoText("Verification des fichiers");
							continue;
				}

					
					val = (int) (BarAPI.getNumberOfTotalDownloadedBytes() / 1000);
					max = (int) (BarAPI.getNumberOfTotalBytesToDownload() / 1000);
					
					LauncherFrame.getInstance().getLauncherPanel().getProgressBar().setMaximum(max);
					LauncherFrame.getInstance().getLauncherPanel().getProgressBar().setValue(val);
					
					LauncherFrame.getInstance().getLauncherPanel().setInfoText("Telechargement des fichiers. " +
							BarAPI.getNumberOfDownloadedFiles() + "/" + BarAPI.getNumberOfFileToDownload() + " " + 
								Swinger.percentage(val, max) + "%");

				}
			}
		};
		updateThread.start();
		
		su.start();
		updateThread.interrupt();
	}
	public static void launch() throws LaunchException
		{
		
		
			ExternalLaunchProfile profile = MinecraftLauncher.createExternalProfile(TG_INFOS, GameFolder.BASIC, authInfos);
			profile.getVmArgs().addAll(Arrays.asList(LauncherFrame.getInstance().getLauncherPanel().getRamSelector().getRamArguments()));
			ExternalLauncher launcher = new ExternalLauncher(profile);
			
			
			Process p = launcher.launch();
			
			ProcessLogManager manager = new ProcessLogManager(p.getInputStream(), new File(TG_DIR, "logs.txt"));
		
			
			
			try
			{
				Thread.sleep(5000L);
				LauncherFrame.getInstance().setVisible(false);
				p.waitFor();
		}
		catch (InterruptedException e)
			{
			e.printStackTrace();
		}
			System.exit(0);
	}
	
	public static void interruptThread() {
		updateThread.interrupt(); }
	
	public static CrashReporter getErrorUtil() {
		return getErrorUtil();
	}
	
}


