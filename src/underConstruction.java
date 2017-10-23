//
///*
// * Create a non-rectangular shell to simulate transparency
// * 
// * For a list of all SWT example snippets see
// * http://dev.eclipse.org/viewcvs/index.cgi/%7Echeckout%7E/platform-swt-home/dev.html#snippets
// */
//import org.eclipse.swt.SWT;
//import org.eclipse.swt.events.PaintEvent;
//import org.eclipse.swt.events.PaintListener;
//import org.eclipse.swt.graphics.Image;
//import org.eclipse.swt.graphics.Point;
//import org.eclipse.swt.graphics.Rectangle;
//import org.eclipse.swt.graphics.Region;
//import org.eclipse.swt.widgets.Display;
//import org.eclipse.swt.widgets.Label;
//import org.eclipse.swt.widgets.Shell;
//
//public class underConstruction {
//
//  private static Label lblStatusIcon;
//
//public static void main(String[] args) {
//    Display display = new Display();
////    final Image image = display.getSystemImage(SWT.ICON_WARNING);
//    
////    final Image image = display.getSystemImage(SWT.ICON_WARNING);
//    final Image image = new Image(display,"dist/icons/Boue.png");
//    		
//    		
//    // Shell must be created with style SWT.NO_TRIM
//    final Shell shell = new Shell(display, SWT.NO_TRIM );
//    shell.setBackground(display.getSystemColor(SWT.COLOR_RED));
//    
//    
//	lblStatusIcon = new Label(shell,SWT.NONE);
//	lblStatusIcon.setBackground(display.getSystemColor(SWT.COLOR_WHITE));
//	lblStatusIcon.setText("Radi se!");
//    
//    // define a region
//    Region region = new Region();
//    Rectangle pixel = new Rectangle(0, 0, 1, 1);
//    for (int y = 0; y < 200; y += 2) {
//      for (int x = 0; x < 200; x += 2) {
//        pixel.x = x;
//        pixel.y = y;
//        region.add(pixel);
//      }
//    }
//    // define the shape of the shell using setRegion
//    shell.setRegion(region);
//    
////    Rectangle size = region.getBounds();
////    shell.setSize(size.width, size.height);
////    shell.addPaintListener(new PaintListener() {
////      public void paintControl(PaintEvent e) {
////        Rectangle bounds = image.getBounds();
////        Point size = shell.getSize();
////        e.gc.drawImage(image, 0, 0, bounds.width, bounds.height, 10, 10, size.x - 20, size.y - 20);
////      }
////    });
////    shell.open();
////    while (!shell.isDisposed()) {
////      if (!display.readAndDispatch())
////        display.sleep();
////    }
////    region.dispose();
////    display.dispose();
//    
//	lblStatusIcon = new Label(shell,SWT.NONE);
//	lblStatusIcon.setBackground(display.getSystemColor(SWT.COLOR_WHITE));
//	lblStatusIcon.setText("Radi se!");
//	
////	lblStatusIcon.setLayoutData("width 10"); //$NON-NLS-1$
////	lblStatusIcon.setImage(new Image(display,"dist/icons/Under-construction.ico")); //$NON-NLS-1$
//    
//    shell.open();
//    
//    while (!shell.isDisposed()) {
//	      if (!display.readAndDispatch())
//	        display.sleep();
//	    }
//    
//  }
//}

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import net.miginfocom.swt.MigLayout;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;

public class underConstruction {

	static private Shell shell;
	static private Display display;
	static private Shell s;
	static MigLayout layout = new MigLayout("", "15[]7[]", "20[]7[]7[]7[]7[]");
	private Label lblStatusIcon;

	public underConstruction(Shell parent) {
		shell = new Shell(parent, SWT.APPLICATION_MODAL | SWT.DIALOG_TRIM);
		display = shell.getDisplay();

		shell.setBackground(display.getSystemColor(SWT.COLOR_WHITE));
		shell.setSize(360, 450);
		DEF.CenterScreen(shell, display);
		shell.setLayout(layout);

		createWidgets();

		shell.addListener(SWT.Close, new Listener() {
			@Override
			public void handleEvent(Event event) {
				// int style = SWT.APPLICATION_MODAL | SWT.YES | SWT.NO;
				// MessageBox messageBox = new MessageBox(shell, style);
				// messageBox.setText("Information");
				// messageBox.setMessage("Close the shell?");
				// event.doit = messageBox.open() == SWT.YES;
				DEF.gBroj = 0;
				// shell.dispose();

			}
		});

		shell.open();

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}

	}

	public static void main(String[] args) {
		new underConstruction(shell);
	}

	private void createWidgets() {

		Label lblN = new Label(shell, SWT.NONE);
		lblN.setText("Sorry for the inconvenience!");
		lblN.setAlignment(SWT.CENTER);
		lblN.setFont(new Font(display, "Arial", 14, SWT.BOLD | SWT.ITALIC));
		lblN.setBackground(display.getSystemColor(SWT.COLOR_WHITE));
		lblN.setLayoutData("width 325,wrap");

		Canvas canvas = new Canvas(shell, SWT.NONE);
		canvas.setBackground(display.getSystemColor(SWT.COLOR_WHITE));
		canvas.setLayoutData("width 325, height 325,wrap");

		canvas.addPaintListener(new PaintListener() {
			public void paintControl(PaintEvent e) {
				Image image = null;
				image = new Image(display,
						"library/icons/Under-construction.jpg");

				e.gc.drawImage(image, 0, 0);

				image.dispose();
			}
		});

		// Sorry for the inconvenience!

		Label lbl = new Label(shell, SWT.NONE);
		lbl.setText("Under construction...");
		lbl.setAlignment(SWT.CENTER);
		lbl.setFont(new Font(display, "Arial", 14, SWT.BOLD | SWT.ITALIC));
		lbl.setBackground(display.getSystemColor(SWT.COLOR_WHITE));
		lbl.setLayoutData("width 325");

	}

}
