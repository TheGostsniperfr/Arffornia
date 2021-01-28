package fr.thegostsniper.thegostlauncher;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import static fr.theshark34.swinger.Swinger.*;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import fr.theshark34.openauth.AuthenticationException;
import fr.theshark34.openlauncherlib.LaunchException;
import fr.theshark34.openlauncherlib.util.Saver;
import fr.theshark34.openlauncherlib.util.ramselector.RamSelector;
import fr.theshark34.swinger.Swinger;
import fr.theshark34.swinger.animation.Animator;
import fr.theshark34.swinger.colored.SColoredBar;

import fr.theshark34.swinger.event.SwingerEvent;
import fr.theshark34.swinger.event.SwingerEventListener;
import fr.theshark34.swinger.textured.STexturedButton;

@SuppressWarnings("serial")
public class LauncherPanel extends JPanel implements SwingerEventListener {
	
	private Image background = Swinger.getResource("frame2.png");
	private Saver saver = new Saver (new File(Launcher.TG_DIR, "launcher.properties"));
	private JTextField usernameField = new JTextField(this.saver.get("username"));
	private STexturedButton playButton = new STexturedButton (Swinger.getResource("play2.png"));
	private STexturedButton quitButton = new STexturedButton (Swinger.getResource("fermer.png"));
	private STexturedButton hideButton = new STexturedButton (Swinger.getResource("reduire.png"));
	private STexturedButton discordButton = new STexturedButton (Swinger.getResource("discord.png"));
	private STexturedButton  ramButton = new STexturedButton (Swinger.getResource("ram.png"));
	
	private SColoredBar progressBar = new SColoredBar (getTransparentWhite(100), getTransparentWhite(175));
	private JLabel infoLabel = new JLabel("Clique sur Jouer !", SwingConstants.CENTER);
	private RamSelector ramSelector = new RamSelector(new File(Launcher.TG_DIR, "ram.txt"));
	 

	
	public LauncherPanel() {
		this.setLayout(null);
		
		usernameField.setForeground(Color.WHITE);
		usernameField.setFont(usernameField.getFont().deriveFont(20F));
		usernameField.setOpaque(false);
		usernameField.setBorder(null);
		usernameField.setBounds(485, 240, 216, 58);
		this.add(usernameField);
		
		playButton.setBounds(360,357);
		playButton.addEventListener(this);
		this.add(playButton);
		
		quitButton.setBounds(906,0);
		quitButton.addEventListener(this);
		this.add(quitButton);
		
		hideButton.setBounds(862,0);
		hideButton.addEventListener(this);
		this.add(hideButton);
		
		discordButton.setBounds(19,19);
		discordButton.addEventListener(this);
		this.add(discordButton);
		
		progressBar.setBounds(0, 524, 950, 12);
		this.add(progressBar);
		
		infoLabel.setForeground(Color.WHITE);
		infoLabel.setFont(usernameField.getFont());
		infoLabel.setBounds(185, 500, 575, 20);
		this.add(infoLabel);
		
		this.ramButton.setBounds(819, 0, 43, 30);
		this.ramButton.addEventListener(this);
		this.add(ramButton);
		

		
		
		
		
	}

	@Override
	public void onEvent(SwingerEvent e) {
		if(e.getSource() == playButton) {
			setEnabled(false);
		
			if(usernameField.getText().replaceAll(" ", "").length() == 0) {
				JOptionPane.showMessageDialog(this, "Erreur, veuillez entrer un pseudo valide.", "Erreur",	JOptionPane.ERROR_MESSAGE);
				setEnabled(true);
				return;
				
			}
			
		
			Thread t = new Thread() {
				@Override
				public void run() {
					try {
						Launcher.auth(usernameField.getText());
					} catch (AuthenticationException e) {
						JOptionPane.showMessageDialog(LauncherPanel.this, "Erreur, impossible de connecter." + e.getErrorModel().getErrorMessage(), "Erreur",	JOptionPane.ERROR_MESSAGE);
						setFieldsEnabled(true);
						return;
								
					}
					saver.set("username", usernameField.getText());
					ramSelector.save();
					
					try {
						Launcher.update();
					} catch (Exception e) {
						Launcher.interruptThread();
						LauncherFrame.getCrashReporter().catchError(e, "Impossible de mettre à jour Arffornia! Le serveur peut être éteint.");
						
						
						}
					
					try {
						Launcher.launch();
					}
					catch (LaunchException e) {
						LauncherFrame.getCrashReporter().catchError(e, "Impossible de lancer le jeu !");
						}
					}
				};
				t.start();
		}
		else if (e.getSource() == quitButton)
			Animator.fadeOutFrame(LauncherFrame.getInstance(), 3, new Runnable() {
				@Override
				public void run() {
					System.exit(0);
				}
			});
		else if (e.getSource() == hideButton)
			LauncherFrame.getInstance().setState(JFrame.ICONIFIED);
		else if (e.getSource() == this.ramButton)
				ramSelector.display();
		else if (e.getSource() == discordButton)
			try {
				Desktop.getDesktop().browse(new URI("https://discord.gg/27rY3fu"));
			} catch (IOException | URISyntaxException el) {
				
				el.printStackTrace();
			}
			
	}

	@Override
	public void paintComponent(Graphics graphics) {
		super.paintComponent(graphics);
		drawFullsizedImage(graphics, this, background);

	}
	
	private void setFieldsEnabled(boolean enabled) {
		usernameField.setEnabled(enabled);
		playButton.setEnabled(enabled);
	}
	
	public SColoredBar getProgressBar() {
		return progressBar;
	}
	
	public void setInfoText(String text) {
		infoLabel.setText(text);
	}
	public RamSelector getRamSelector()
	{
		
		return ramSelector;
	}
	
	
}
