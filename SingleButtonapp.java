package utils;

import java.awt.AWTException; 
import java.awt.Color;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;


/**
 * @author Gurchet Singh The program provides the GUI for the Application having
 *         3 screens 1. To create New Document(OR New Session) 2. Dashboard. 3.
 *         To save Additional information with screen shot i.e. Comment,Test
 *         Data
 */

public class SingleButtonapp {

	// WatermarkTextField filename;
	MyDialog d;
	TakeScreenShot takeScreenShot;
	String document_name;
	final JTextField filename;
	// private static final long serialVersionUID = 1L;

	// Constructor from where the game begins

	public SingleButtonapp() {

		// createNewSession();

		// JDialog d = new JDialog();
		d = new MyDialog("Add File");
		d.setTitle("Add File");
		JPanel pnlButton = new JPanel();
		Box box = Box.createVerticalBox();
		filename = new JTextField("File Name");
		filename.setText("File Name");
		JButton Save = new JButton("Save");
		
		d.setSize(200, 100);

		box.add(filename);
		box.add(Save);
		pnlButton.add(box);
		d.add(pnlButton);
		d.setVisible(true);
		d.setLocationRelativeTo(null);
		d.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		
		Save.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				if (!filename.equals(null)) {
					document_name = filename.getText();
					
						try {
							takeScreenShot = new TakeScreenShot(document_name);
						} catch (InvalidFormatException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}

					
					d.dispose();
					screenController(takeScreenShot, document_name);
				}
			}

		});


	}

	/*
	 * 2. Dashboard. Function takes first argument as object of TakeScreenShot
	 * class to use its methods the another argument is the document name which
	 * would be shown on the title bar of the dashboard.
	 */
	public static void screenController(final TakeScreenShot takeScreenShot,
			final String document_name) {
		final JFrame frm_main = new JFrame();
		JPanel pnlButton = new JPanel();
		JButton NewButton;
		JButton Capture;
		JButton Done;
		JButton Comment;

		NewButton = new JButton("New");
		Capture = new JButton("Capture");
		Done = new JButton("Done");
		Comment = new JButton("Comment");

		pnlButton.add(NewButton);
		pnlButton.add(Capture);
		pnlButton.add(Comment);
		pnlButton.add(Done);

		frm_main.add(pnlButton);

		// JFrame properties
		frm_main.setSize(350, 70);
		frm_main.setBackground(Color.BLACK);
		frm_main.setTitle(document_name);
		frm_main.setLocationRelativeTo(null);
		frm_main.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frm_main.setVisible(true);

		NewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new SingleButtonapp();
//				SwingUtilities.invokeLater(new Runnable() {
//					public void run() {
//						// createNewSession(); // Let the constructor do the job
//						// From here the flow of the program would enroute . It
//						// is creating the object of the class again
//						// First object was created in Main function
//						new SingleButtonapp();
//					}
//				});
			}
		});

		Capture.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frm_main.setVisible(false);
				try {
					BufferedImage screenFullImage = takeScreenShot
							.captureScreen();
				} catch (AWTException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				} catch (IOException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
				frm_main.setVisible(true);
				// Printing screen shot
				try {
					takeScreenShot.printScreen();
				} catch (InvalidFormatException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

			}

		});

		Comment.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				frm_main.disable();

				// JDialog dlg_Save = new JDialog();
				final MyDialog dlg_Save = new MyDialog(document_name);
				dlg_Save.setTitle(document_name);
				dlg_Save.setBounds(0, 0, 300, 200);

				JPanel pnl_main = new JPanel();

				pnl_main.setLayout(new BoxLayout(pnl_main, BoxLayout.Y_AXIS));

				pnl_main.setBounds(30, 30, 250, 150);

				final JTextArea txt_Comment = new JTextArea("Comment");
				txt_Comment.setWrapStyleWord(true);
				txt_Comment.setLineWrap(true);
				txt_Comment.setEditable(true);
				txt_Comment.setFocusable(true);

				JScrollPane scroll_pane = new JScrollPane(txt_Comment);
				scroll_pane.setBounds(50, 0, 200, 90);
				pnl_main.add(scroll_pane);

				JPanel myPanel = new JPanel();
				myPanel.setLayout(new BoxLayout(myPanel, BoxLayout.X_AXIS));
				myPanel.setBounds(0, 100, 250, 50);

				JButton btn_save;
				btn_save = new JButton("Save");

//				JButton btn_cancel;
//				btn_cancel = new JButton("Cancel");

				myPanel.add(btn_save);
//				myPanel.add(btn_cancel);

				pnl_main.add(myPanel);

				dlg_Save.add(pnl_main);

				dlg_Save.setLocationRelativeTo(frm_main);
				dlg_Save.setVisible(true);
//				dlg_Save.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

				btn_save.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {

						String comment = txt_Comment.getText();
						takeScreenShot.PrintComment(comment);
						dlg_Save.setVisible(false);
						frm_main.setVisible(true);
						frm_main.enable();
					}
				});

//				btn_cancel.addActionListener(new ActionListener() {
//					public void actionPerformed(ActionEvent e) {
//						dlg_Save.setVisible(false);
//						frm_main.setVisible(true);
//						frm_main.enable();
//					}
//				});

				dlg_Save.addWindowListener(new WindowAdapter() {

					@Override
					public void windowClosing(WindowEvent e) {

						//dlg_Save.setVisible(false);
						dlg_Save.dispose();
						frm_main.setVisible(true);
						frm_main.enable();

					}

				});

			}

		});

		// Capture.addActionListener(new ActionListener() {
		// public void actionPerformed(ActionEvent e) {
		//
		// BufferedImage screenFullImage = takeScreenShot.captureScreen();
		//
		// // JFrame frm_capture_testdata = new JFrame(document_name);
		// JDialog dlg_Save = new JDialog();
		// dlg_Save.setTitle(document_name);
		// dlg_Save.setBounds(0, 0, 500, 500);
		//
		// ImageIcon img_icon = new ImageIcon(screenFullImage);
		// img_icon.setDescription("Screen Shot");
		// JLabel lbl_icon = new JLabel("Logging in ... ", img_icon,
		// JLabel.CENTER);
		// lbl_icon.setBounds(0, 0, 500, 500);
		// dlg_Save.setContentPane(lbl_icon);
		//
		// JPanel myPanel = new JPanel();
		// myPanel.setLayout(new BoxLayout(myPanel, BoxLayout.Y_AXIS));
		// myPanel.setBounds(50, 50, 400, 200);
		// // myPanel.setBackground(Color.BLUE);
		// JLabel lbl_Comment = new JLabel("Comment");
		// lbl_Comment.setText("Comment");
		// JTextField txt_Comment;
		// txt_Comment = new JTextField("Comment");
		// txt_Comment.setText("Give Comment");
		// txt_Comment.setSize(100, 50);
		//
		// JLabel lbl_field = new JLabel("Field");
		// lbl_field.setText("Field");
		// JTextField txt_field;
		// txt_field = new JTextField("field");
		// txt_field.setText("field");
		// txt_field.setSize(100, 50);
		//
		// JLabel lbl_input = new JLabel("Expected");
		// lbl_input.setText("Input");
		// JTextField txt_input;
		// txt_input = new JTextField("input");
		// txt_input.setText("input");
		// txt_input.setSize(100, 50);
		//
		// JLabel lbl_output = new JLabel("Actual");
		// lbl_output.setText("Output");
		// JTextField txt_output;
		// txt_output = new JTextField("output");
		// txt_output.setText("output");
		// txt_output.setSize(100, 50);
		//
		// JPanel pnl_buttons = new JPanel();
		// pnl_buttons.setLayout(new BoxLayout(pnl_buttons,
		// BoxLayout.X_AXIS));
		//
		// JButton btn_save;
		// btn_save = new JButton("Save");
		// btn_save.setBounds(10, 10, 50, 30);
		//
		// JButton btn_cancel;
		// btn_cancel = new JButton("Cancel");
		// btn_cancel.setBounds(30, 10, 50, 30);
		//
		// myPanel.add(lbl_Comment);
		// myPanel.add(txt_Comment);
		// myPanel.add(lbl_field);
		// myPanel.add(txt_field);
		// myPanel.add(lbl_input);
		// myPanel.add(txt_input);
		// myPanel.add(lbl_output);
		// myPanel.add(txt_output);
		// pnl_buttons.add(btn_save);
		// pnl_buttons.add(btn_cancel);
		//
		// // Adding pnl_buttons panel to Jpanel "myPanel"
		// myPanel.add(pnl_buttons);
		// // Adding myPanel to the Jdialog "dlg_Save"
		// dlg_Save.add(myPanel);
		// // dlg_Save.add(pnl_buttons);
		//
		// dlg_Save.setLocationRelativeTo(frm_main);
		// dlg_Save.setVisible(true);
		// dlg_Save.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		// frm_main.disable();
		//
		// btn_save.addActionListener(new ActionListener() {
		// public void actionPerformed(ActionEvent e) {
		//
		// String comment = txt_Comment.getText();
		// String field = txt_field.getText();
		// String expected = txt_input.getText();
		// String actual = txt_output.getText();
		//
		// // Printing comment
		// takeScreenShot.PrintComment(comment);
		// takeScreenShot.printTestData(field, expected, actual);
		// try {
		// // Printing screen shot
		// takeScreenShot.printScreen();
		//
		// } catch (InvalidFormatException | IOException e1) {
		// // TODO Auto-generated catch block
		// e1.printStackTrace();
		// }
		//
		// //takeScreenShot.printTestData(field, expected, actual);
		// dlg_Save.setVisible(false);
		// frm_main.setVisible(true);
		// frm_main.enable();
		// }
		//
		// });
		// btn_cancel.addActionListener(new ActionListener() {
		// public void actionPerformed(ActionEvent e) {
		// dlg_Save.setVisible(false);
		// frm_main.setVisible(true);
		// frm_main.enable();
		// }
		// });
		// }
		// });

		Done.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				takeScreenShot.closeFile();
				Thread.currentThread().interrupt();
				//frm_main.setVisible(false);
				frm_main.dispose();
			}
		});

		frm_main.addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {

				takeScreenShot.closeFile();
				Thread.currentThread().interrupt();
				//frm_main.setVisible(false);
				frm_main.dispose();
			}

		});

	}

	public static void main(String[] args) throws IOException, AWTException {
		new SingleButtonapp();
		
	}
}

class MyDialog extends JDialog {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final List<Image> icon = Arrays.asList(new ImageIcon(
			"icon_16.png").getImage(), new ImageIcon("icon_32.png").getImage(),
			new ImageIcon("icon_64.png").getImage());

	MyDialog(String name) {
		super(new DummyFrame(name, icon));
		// super(null, java.awt.Dialog.ModalityType.TOOLKIT_MODAL);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);

	}

	public void setVisible(boolean visible) {
		super.setVisible(visible);
		if (!visible) {
			((DummyFrame) getParent()).dispose();
		}
	}
}

class DummyFrame extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	DummyFrame(String title, List<? extends Image> iconImages) {
		super(title);
		setUndecorated(true);
		setVisible(true);
		setLocationRelativeTo(null);
		setIconImages(iconImages);
	}
}

/*
 * This class creates a textbox with watermark
 */
//class WatermarkTextField extends JTextField {
//
//	BufferedImage img;
//
//	TexturePaint texture;
//
//	public WatermarkTextField(File file) {
//		super();
//		try {
//			img = ImageIO.read(file);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		Rectangle rect = new Rectangle(0, 0, img.getWidth(null),
//				img.getHeight(null));
//		texture = new TexturePaint(img, rect);
//		setOpaque(false);
//	}
//
//	public void paintComponent(Graphics g) {
//		Graphics2D g2 = (Graphics2D) g;
//		g2.setPaint(texture);
//		g.fillRect(0, 0, getWidth(), getHeight());
//		super.paintComponent((java.awt.Graphics) g);
//	}
//}