package Executable;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileOutputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingWorker;
import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class Main {
	private static final String PLEASE_FETCH_RESEARCHER = "You have now completed part one.\n\nTimes you identifed the correct color: 1\nTimes you identifed the incorrect color: 1\nTotal rounds: 2\n\nPlease notify the research assistant that you have finished the first half of the experiment.\n"
			+ "They will provide you with new information to use during the second half of the experiment. ";
	private static final int[] practice = { 0,1,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,1,0,1,1,1,1,1,1};
	private static final int[] Bag1 = { 
		1, 0, 1, 1, 1, 0, 0, 1, 1, 1,
		0, 0, 1, 1, 1, 1, 1, 0, 1, 1,
		0, 1, 1, 1, 0, 1, 1, 0, 1, 1, 
		1 ,1, 1, 0, 1, 1, 1, 1, 0, 1, 
		1, 0, 1, 0, 1, 1, 0, 1, 0, 1, 
		1, 1, 0, 1, 1, 0, 1, 0, 1, 1};
	private static final int[] Bag2 = { 
		0, 0, 1, 0, 0, 1, 1, 0, 0, 0,
		1, 0, 0, 1, 0, 0, 0, 1, 0, 0, 
		1, 0, 1, 0, 1, 0, 0, 1, 0, 0,
		1, 0, 0, 0, 0, 0, 1, 0, 0, 1,
		0, 0, 0, 0, 0, 1, 0, 1, 0, 0,
		1, 0, 0, 1, 0, 1, 0, 0, 0, 0};
	private static final int[] Bag3 = { 
		1, 1, 0, 1, 1, 0, 0, 1, 1, 1,
		0, 1, 0, 1, 1, 1, 1, 0, 1, 1,
		1, 1, 1, 0, 1, 0, 1, 1, 1, 0,
		1, 0, 1, 1, 1, 1, 0, 1, 1, 1,
		1, 1, 1, 0, 0, 1, 0, 1, 0, 1,
		0, 1, 0, 1, 0, 1, 1, 1, 1, 1 };
	private static final int[] Bag4 = { 
		0, 1, 1, 1, 1, 0, 1, 0, 1, 1, 
		1, 1, 1, 0, 1, 1, 1, 0, 1, 1,
		0, 1, 0, 1, 1, 0, 1, 0, 1, 1,
		1, 1, 0, 1, 1, 1, 0, 1, 1, 0,
		1, 0, 1, 0, 1, 1, 1, 0, 1, 1,
		0, 1, 1, 1, 0, 1, 1, 1, 0, 1 };
	private static final ArrayList<int[]> bags = new ArrayList<int[]>();
	private static final ArrayList<String> bagNames = new ArrayList<String>();
	private static String curBagName = "Practice";
	private static final Random randomGenerator = new Random();
	private static int[] currentBag = practice;
	private static HSSFWorkbook book = new HSSFWorkbook();
	private static HSSFSheet sheet = book.createSheet();
	private static JFrame frame;
	private static JPanel pane;
	private static BoxLayout layout;
	private static int straws = 0;
	private static int datapoint = 1;
	private static String thisIsPractice = "This is a practice round";
	private static String practiceBeginning = "The practice round has now concluded. The experiment will now begin.";
	private static String nextRound = "Now you are given a new bag of straws.\n"
			+ "Again, 70% of the straws are one color and 30% are the other color.\n"
			 + "You will draw straws one by one to determine which color is 70%.\n"
			+"Draw as few straws as possible in order to make your decision.\nClick \"I'm finished drawing straws\" to give your final decision.";
	private static String question = "Based on the straws I have drawn so far, I believe 70% of the straws are:";
	private static String 
	instructionsPt1 = "A bag has two different colors of straws: red and blue. In this bag, 70% of the straws are one color and 30% are the other color.\n "
			+ "The computer will randomly choose which of the two colors is 70% and which is 30%. Your job is to figure out if it is 70% red or 70% blue.  \n\n"
			+ "You will draw straws one by one to determine which is correct. You will then be asked to rate how confident you are in your decision.\n\n "
			+ "Draw as few straws as possible in order to make your decision.\n\n";
	private static String instructionsPt2 = "Once you have made your decision, click \"I'm finished drawing straws\" to give your final answer.\n\n"
			+ "You will do this a total of five times, each time with a different bag. The first time will be a practice round, so that you can become familiar with the process. ";
	private static String
	doubtText = "Now you will do the task again, but this time with new information we did not tell you before. \n\n"
			+ " Feelings of certainty can be misleading. Research shows people often feel they are certain about a decision\n"
			+ " only to discover later on that their decision was incorrect. That study showed that their confidence\n"
			+ "about their own decision making had been false.\n\n"
			+ "Therefore, before you make your final decision, you should ask yourself whether you are really and genuinely sure\n"
			+ "about your decision and whether you are confident about your answer.";
	private static String
	doubtText2 = "Remember, feelings of certainty can be misleading. \n\n"
			+ "Before you make your final decision, you should ask yourself whether you are \n"
			+ "confident about your answer.";
	private static String 
	doubtConfirmation = "Are you sure you are ready to decide?";
	private static String confidenceRatingMessage = "After seeing this straw, I am _____% certain that I know the correct answer (please type an answer between 0 & 100):";
	private static String thankYou = "Thank you, please notify the research assistant that you have finished.";
	private static int round = 0;
	private static int end = 5;
	private static String color = "Not Asked For";
	private static int confidence;
	private static String ParticipantId;
	private static int control = 0;
	private static int blue = 0;
	private static boolean disappearingStraws = false;
	private static Dimension screenSize;
	private static int textHeight;

	public static void main(String[] args) {
		bags.add(Bag1);
		bags.add(Bag2);
		bags.add(Bag3);
		bags.add(Bag4);
		bagNames.add("Bag1, Blue Dominiant");
		bagNames.add("Bag2, Red Dominiant");
		bagNames.add("Bag3, Blue Dominiant");
		bagNames.add("Bag4, Blue Dominiant");
		reasearchAssistantGUI();
	}
	
	private static void reasearchAssistantGUI(){
		frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pane = new JPanel();
		pane.setOpaque(true);
		layout = new BoxLayout(pane, BoxLayout.Y_AXIS);
		pane.setLayout(layout);
		pane.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
		screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		screenSize.setSize(screenSize.getWidth(), screenSize.getHeight() - 35);
		textHeight = (int)screenSize.getHeight()/40;
		JButton disappearingStrawsToggle = new JButton("Change Counter Balance");
		disappearingStrawsToggle.setAlignmentX(Component.CENTER_ALIGNMENT);
		disappearingStrawsToggle.setAlignmentY(Component.CENTER_ALIGNMENT);
		Font font = new Font(disappearingStrawsToggle.getFont().getName(),
				Font.PLAIN, textHeight);
		disappearingStrawsToggle.setFont(font);
		JLabel disappearingLabel = new JLabel();
		disappearingLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		disappearingLabel.setAlignmentY(Component.CENTER_ALIGNMENT);
		disappearingLabel.setFont(font);
		disappearingLabel.setText("Counter Balance 1");
		disappearingStrawsToggle.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				disappearingStraws = !disappearingStraws;
				if(disappearingStraws){
					disappearingLabel.setText("Counter Balance 2");
				}else{
					disappearingLabel.setText("Counter Balance 1");
				}
				pane.repaint();
			}
			
		});
		JButton doneButton = new JButton("Done Configuring");
		doneButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		doneButton.setAlignmentY(Component.CENTER_ALIGNMENT);
		doneButton.setFont(font);
		javax.swing.UIManager.put("OptionPane.messageFont", font);
		javax.swing.UIManager.put("OptionPane.buttonFont", font);
		javax.swing.UIManager.put("OptionPane.componentFont", font);
		Component rigidArea = Box.createRigidArea(new Dimension(50,50));
		//The only way to get to the rest of the code
		doneButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				frame.dispose();
				setupGUI();
			}
		});
		pane.add(rigidArea);
		pane.add(disappearingStrawsToggle);
		pane.add(Box.createRigidArea(new Dimension(10,15)));
		pane.add(disappearingLabel);
		pane.add(Box.createRigidArea(new Dimension(10,15)));
		pane.add(doneButton);
		pane.setSize(screenSize);
		frame.setSize(screenSize);
		refresh();
	}

	private static void setupGUI() {
		frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pane = new JPanel();
		pane.setOpaque(true);
		layout = new BoxLayout(pane, BoxLayout.Y_AXIS);
		pane.setLayout(layout);
		pane.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		screenSize.setSize(screenSize.getWidth(), screenSize.getHeight() - 35);
		JButton startButton = new JButton("Start");
		startButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		startButton.setAlignmentY(Component.CENTER_ALIGNMENT);
		Font font = new Font(startButton.getFont().getName(),
				Font.PLAIN, textHeight);
		startButton.setFont(font);
		javax.swing.UIManager.put("OptionPane.messageFont", font);
		javax.swing.UIManager.put("OptionPane.buttonFont", font);
		javax.swing.UIManager.put("OptionPane.componentFont", font);
		Component rigidArea = Box.createRigidArea(new Dimension(50,50));
		//The only way to get to the rest of the code
		startButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String concattedInstructions = instructionsPt1;
				if(disappearingStraws){
					concattedInstructions+="Each straw you draw will appear for a few seconds before disappearing. Be sure to keep in mind how many red and blue straws you have seen so you can give an answer.\n\n";
				}
				concattedInstructions+=instructionsPt2;
				displayMessage(concattedInstructions, "Continue");
				layout.removeLayoutComponent(startButton);
				pane.remove(startButton);
				pane.remove(rigidArea);
				recursiveIdFetcher();
				pane.repaint();
			}

			private void recursiveIdFetcher() {
				JTextField tf = new JTextField(8);
				tf.setText("Input your ID");
                tf.setFont(tf.getFont().deriveFont(26f));
                int result = JOptionPane.showConfirmDialog(
                        frame, tf, "", 
                        JOptionPane.OK_CANCEL_OPTION);
                if (result==JOptionPane.OK_OPTION) {
                	ParticipantId = tf.getText();
                } else {
                	recursiveIdFetcher();
                }
			}

		});

		pane.add(rigidArea);
		pane.add(startButton);
		pane.setSize(screenSize);
		frame.setSize(screenSize);
		refresh();
	}

	//Used to redraw everything, probably excessively.
	private static void refresh() {
		frame.add(pane);
		frame.setVisible(true);
		frame.validate();
		frame.repaint();
	}

	//Used to log data
	private static void logData() {
		HSSFRow rowhead = sheet.createRow((short) 0);
		rowhead.createCell(0).setCellValue("Round");
		rowhead.createCell(1).setCellValue("Straw");
		rowhead.createCell(2).setCellValue("Color");
		rowhead.createCell(3).setCellValue("Confidence");
		rowhead.createCell(4).setCellValue("Bag");
		rowhead.createCell(5).setCellValue("Timestamp");
		HSSFRow row = sheet.createRow((short) datapoint);
		row.createCell(0).setCellValue(round);
		row.createCell(1).setCellValue(straws);
		row.createCell(2).setCellValue(color);
		row.createCell(3).setCellValue(confidence);
		row.createCell(4).setCellValue(curBagName);
		row.createCell(5).setCellValue(LocalDateTime.now().toString());
		datapoint++;
	}

	public static String convertToMultiline(String orig) {
		return "<html>" + orig.replaceAll("\n", "<br>");
	}

	protected static void displayMessage(String message, String Label) {
		JLabel instructions = new JLabel();
		instructions.setText(convertToMultiline(message));
		instructions.setHorizontalAlignment(JLabel.CENTER);
		instructions.setFont(new Font(instructions.getFont().getName(),
				Font.PLAIN, textHeight));
		instructions.setAlignmentX(Component.CENTER_ALIGNMENT);
		instructions.setBorder(BorderFactory.createLoweredSoftBevelBorder());
		pane.add(instructions);
		Component rA = Box.createRigidArea(new Dimension(10,15));
		if (round < end) {
			JButton cont = new JButton(Label);
			cont.setFont(new Font(cont.getFont().getName(),
					Font.PLAIN, textHeight));
			cont.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent arg0) {
					if(round == 0){
						JOptionPane.showMessageDialog(frame, thisIsPractice, "", JOptionPane.PLAIN_MESSAGE);
					}
					if(round == 3){
						JOptionPane.showMessageDialog(frame, doubtText, "", JOptionPane.PLAIN_MESSAGE);
					}
					showExperimentButtons();
					layout.removeLayoutComponent(cont);
					pane.remove(cont);
					layout.removeLayoutComponent(instructions);
					pane.remove(instructions);
					pane.remove(rA);
					pane.repaint();

				}

			});
			pane.add(rA);
			cont.setAlignmentX(Component.CENTER_ALIGNMENT);
			pane.add(cont);
		}
		refresh();
	}

	protected static void showExperimentButtons() {
		JButton drawStraw = new JButton("Draw a Straw");
		drawStraw.setAlignmentX(Component.CENTER_ALIGNMENT);
		drawStraw.setFont(new Font(drawStraw.getFont().getName(),
				Font.PLAIN, textHeight));
		JButton done = new JButton("I'm done drawing straws");
		done.setEnabled(false);
		done.setFont(new Font(done.getFont().getName(),
				Font.PLAIN, textHeight));
		done.setAlignmentX(Component.CENTER_ALIGNMENT);
		JPanel innerPane = new JPanel();
		innerPane.setLayout(new GridLayout(0, 15));
		pane.add(drawStraw);
		pane.add(Box.createRigidArea(new Dimension(10,15)));		
		pane.add(done);
		pane.add(Box.createRigidArea(new Dimension(10,15)));
		drawStraw.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				int picked = currentBag[straws];
				straws++;
				done.setEnabled(false);
				drawStraw.setEnabled(false);
				if (picked > blue) {
					add(new ImageIcon(Main.class.getResource("/resources/blue_straw.png")));
				} else {
					add(new ImageIcon(Main.class.getResource("/resources/red_straw.png")));
				}
			}

			private void add(ImageIcon image) {
				JLabel picLabel = new JLabel(image);
				picLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
				picLabel.setAlignmentY(Component.CENTER_ALIGNMENT);
				UIManager.put("TextField.font", new FontUIResource(
						new Font(done.getFont().getName(),
								Font.PLAIN, textHeight)));
				innerPane.add(picLabel);
				refresh();
				if(disappearingStraws){
					ImageKiller please = new ImageKiller(picLabel);
					please.execute();
				}else{
					recursiveConfidenceFetcher();
				}
			}
			
			class ImageKiller extends SwingWorker<String, Object> {
				JLabel picLabel;
				
				public ImageKiller(JLabel pic){
					picLabel = pic;
				}
				   @Override
				   public String doInBackground() {
					   try{
							TimeUnit.SECONDS.sleep(5);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						return null;
				   }

				   @Override
				   protected void done() {
				       try { 
				    	   picLabel.setIcon(null);
				    	   recursiveConfidenceFetcher();
				       } catch (Exception ignore) {
				       }
				   }
				}

			private void recursiveConfidenceFetcher() {
                String input = JOptionPane.showInputDialog(
                        frame, confidenceRatingMessage);
                	try{
                	confidence = Integer.valueOf(input);
                	}catch(Exception e){
                		JOptionPane.showMessageDialog(
                                frame, "Your confidence could not be understood. Please put your confidence in again."
                                );
                		recursiveConfidenceFetcher();
                	}
                	if(confidence < 0 || confidence > 100){
                		JOptionPane.showMessageDialog(
                                frame, "Your given confidence was not between 0 and 100. Please put your confidence in again."
                                );
                		recursiveConfidenceFetcher();
                	}
                	logData();
                	color = "Not Asked For";
                	done.setEnabled(true);
                	drawStraw.setEnabled(true);
			}

		});

		done.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				boolean progress = true;
				if(round > 2){
					Object[] options = { "Draw more Straws", "I'm sure" };
					int result = JOptionPane.showOptionDialog(frame, doubtConfirmation, "",
							JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE,
							null, options, options[1]);
					if (result == 1) {
						progress = true;
					} else {
						color = "Chose to draw more straws";
						logData();
						color = "Not Asked";
						progress = false;
					}
				}
				if(progress){
					progress = promptForColor(done);
				}
				if(progress){
					for(Component c: pane.getComponents()){
						pane.remove(c);
					}
					round++;
					datapoint++;
					if(round<end){
						int next = randomGenerator.nextInt(end-round);
						currentBag = bags.get(next);
						bags.remove(next);
						curBagName = bagNames.get(next);
						bagNames.remove(next);
					}
					if(round == 1){
						JOptionPane.showMessageDialog(frame, practiceBeginning, "", JOptionPane.PLAIN_MESSAGE);
					}
					if (round < end) {
						if(round == 3){
							displayMessage(PLEASE_FETCH_RESEARCHER,"I am the research assistant");
						}
						if(round == 4){
							JOptionPane.showMessageDialog(frame, doubtText2, "", JOptionPane.PLAIN_MESSAGE);
						}
						if(round!=3){
							JOptionPane.showMessageDialog(frame, nextRound, "", JOptionPane.PLAIN_MESSAGE);
							showExperimentButtons();
						}
					} else {
						displayMessage(thankYou, "Close Program");
						String filename = ParticipantId + "_data.xls";
						FileOutputStream fileOut;
						try {
							fileOut = new FileOutputStream(filename);
							book.write(fileOut);
							fileOut.close();
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					pane.repaint();
					straws = 0;
				}
			}

		});
		pane.add(innerPane);
		pane.add(Box.createRigidArea(new Dimension(10,15)));
		refresh();
	}

	private static boolean promptForColor(JButton done) {
		Object[] options = { "Red", "Blue", "Cancel"};
		int result = JOptionPane.showOptionDialog(frame, question, "",
				JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE,
				null, options, options[2]);
		if (result == control) {
			color = "Red";
		} else if(result > 1){
			color = "Cancelled";
			logData();
			color="Not Asked";
			return false;
		}
		else{
			color = "Blue";
		}
		logData();
		color="Not Asked";
		return true;
	}

}