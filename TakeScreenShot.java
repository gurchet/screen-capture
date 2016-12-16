package utils;

import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.util.Units;
import org.apache.poi.xwpf.usermodel.Document;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFPicture;
import org.apache.poi.xwpf.usermodel.XWPFPictureData;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;

/**
 * This program is to save screen shots in document at run time with comments
 * and test data asking to user as an image which will be saved into a file.
 * 
 * @author Gurchet Singh
 *
 */

public class TakeScreenShot {

	XWPFDocument docx;
	String filename;
	FileOutputStream out;
	String screenshot_name;
	String image_format;
	String doc_format;
	File abc;
	String folder_name;
	int image_number;
	XWPFParagraph par;
	XWPFRun run;
	File file_doc;
	File theDir;

	TakeScreenShot(String file_name) throws IOException, InvalidFormatException {

		this.filename = file_name;
		this.doc_format = "docx";

		this.screenshot_name = "FullScreenshot";
		this.image_format = "JPEG";
		this.folder_name = file_name;
		this.image_number = 0;

		theDir = new File(this.filename);
		if (!theDir.exists()) {
			System.out.println("\n folder does not exists");
			try {
				theDir.mkdir();
			} catch (SecurityException se) {
				se.printStackTrace();
			}

		}
		file_doc = new File(this.folder_name + "\\" + this.filename + "."
				+ doc_format);
		if (!file_doc.exists()) {
			System.out.println("\n docx does not exists");
			file_doc.createNewFile();

			this.docx = new XWPFDocument();

			this.par = this.docx.createParagraph();
			this.run = par.createRun();
			this.par.setAlignment(ParagraphAlignment.CENTER);
			this.run.setBold(true);
			this.run.setFontFamily("Segoe UI");
			this.run.setFontSize(22);
			this.run.setText("Test Data");
			this.run.addBreak();
			this.run.addBreak();

		} else {
			this.docx = new XWPFDocument(OPCPackage.open(this.folder_name
					+ "\\" + this.filename + "." + doc_format));
			this.par = this.docx.createParagraph();
			this.run = par.createRun();
		}

		try {
			this.out = new FileOutputStream(this.folder_name + "\\"
					+ this.filename + "." + doc_format, true);
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}

	}

	/*
	 * PrintComment Method is for printing the user given comment for screen
	 * shot to word document
	 */

	public void PrintComment(String Comment) {
		
		run.setFontSize(13);
		run.setBold(false);
		par.setAlignment(ParagraphAlignment.LEFT);
        if (Comment.contains("\n")) {
            String[] lines = Comment.split("\n");
            run.setText(lines[0], 0); // set first line into XWPFRun
            for(int i=1;i<lines.length;i++){
                // add break and insert new text
                run.addBreak();
                run.setText(lines[i]);
            }
        } else {
            run.setText(Comment, 0);
        }
		System.out.println("\n In PrintComment Method");
		System.out.println("\n Comment : " + Comment);

		run.addBreak();
	}

	/*
	 * PrintComment Method is for capturing the current screen and saving it to
	 * local machine. It returns the Name of the Image being saved
	 */

	public BufferedImage captureScreen() throws AWTException, IOException {
		System.out.println("\n In captureScreen Method");
		// image_number++;
		image_number = docx.getAllPictures().size();

		Robot robot = new Robot();

		String fullname = screenshot_name + "_" + image_number + "."
				+ image_format;
		Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit()
				.getScreenSize());
		System.out.println("\n ScreenSize : " + " height : "
				+ screenRect.height + " width : " + screenRect.width + " x :"
				+ screenRect.x + " y : " + screenRect.y);
		BufferedImage screenFullImage = robot.createScreenCapture(screenRect);
		ImageIO.write(screenFullImage, image_format, new File(this.folder_name
				+ "\\" + fullname));
		return screenFullImage;

	}

	/*
	 * PrintComment Method is for printing the Image recently saved in local
	 * machine to word document It takes the Document being written and the name
	 * of the File recently saved in local machine
	 */

	public void printScreen() throws InvalidFormatException, IOException {

		String fullname = screenshot_name + "_" + image_number + "."
				+ image_format;
		System.out.println("Screen name Full : " + fullname);
		InputStream pic = new FileInputStream(folder_name + "\\" + fullname);
		if (!pic.equals(null)) {
			run.setFontSize(13);
			run.setBold(false);
			par.setAlignment(ParagraphAlignment.CENTER);
			//run.setText(fullname);
			run.addPicture(pic, Document.PICTURE_TYPE_JPEG,
					this.screenshot_name + "_" + image_number,
					Units.toEMU(480), Units.toEMU(320));
			run.addBreak();

			List<XWPFPicture> lst_pictures = run.getEmbeddedPictures();
			if (lst_pictures.isEmpty())
				System.out.println("couldn't add the picture in document!");
			else
				System.out.println("A full screenshot saved!");
		} else {
			System.out.println("\n Can't pick the image");
		}

		pic.close();
	}

	/*
	 * printTestData Method is for printing the user given test data to word
	 * document It takes the Document being written and the test data to be
	 * written in the form of table in the document. First Column of the table
	 * is for Module Name Second Column of the table is for Input data Third
	 * Column of the table is for output data First Row works as header of the
	 * table
	 */

	public void printTestData(String field, String expected, String actual) {

		String[] lst_fields = field.split(",");
		String[] lst_expected = expected.split(",");
		String[] lst_actual = actual.split(",");
		int maxer = Math.max(lst_fields.length, lst_expected.length);
		int maximum = Math.max(maxer, lst_actual.length);

		System.out.println("\n In printTestData Method");
		XWPFTable table = this.docx.createTable(maximum + 1, 3);
		List<XWPFTableRow> rows = table.getRows();
		if (!table.equals(null)) {
			System.out.println("\n Table has been created");
			XWPFTableRow tableOneRowOne = rows.get(0);
			tableOneRowOne.getCell(0).getParagraphs().get(0).createRun()
					.setText("Fields");
			tableOneRowOne.getCell(1).getParagraphs().get(0).createRun()
					.setText("Expected");
			tableOneRowOne.getCell(2).getParagraphs().get(0).createRun()
					.setText("Actual");
			int i = 0;
			while (i < maximum) {

				XWPFTableRow row = rows.get(i + 1);

				if (i < lst_fields.length) {
					if (!lst_fields[i].equals(null)) {
						row.getCell(0).getParagraphs().get(0).createRun()
								.setText(lst_fields[i]);
					}
				}
				if (i < lst_expected.length) {
					if (!lst_expected[i].equals(null)) {
						row.getCell(1).getParagraphs().get(0).createRun()
								.setText(lst_expected[i]);

					}
				}
				if (i < lst_actual.length) {
					if (!lst_actual[i].equals(null)) {
						row.getCell(2).getParagraphs().get(0).createRun()
								.setText(lst_actual[i]);

					}
				}

				i++;
			}
			table.setWidth(480);

		} else {
			System.out.println("\n Table has not been created");
		}
		List<XWPFPictureData> lst_pictures = docx.getAllPictures();
		if (lst_pictures.equals(null))
			System.out.println("couldn't add the picture in Docx!");

	}

	public void closeFile() {

		try {
			docx.write(out);
			out.close();
			docx.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block;
			System.out.println("\n table writting to doc failed");
			e.printStackTrace();
		}

	}

}
