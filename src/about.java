import net.miginfocom.swt.MigLayout;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;

public class about extends DEF {

	private static Shell shell;
	private Display display;
	private Image icona;
	MigLayout layout = new MigLayout();
	private Label lbl;
	static Composite c;

	public about(Shell parent) {
		shell = new Shell(parent, SWT.APPLICATION_MODAL | SWT.DIALOG_TRIM);
		display = shell.getDisplay();

		shell.setSize(320, 225);
		shell.setText(getLabel("about"));

		icona = new Image(display, "library/icons/Boue.ico");
		shell.setImage(icona);

		shell.setLayout(layout);

		createWidgets();

		// ALERT.openInput(shell);
		//
		//
		shell.addListener(SWT.Close, new Listener() {
			@Override
			public void handleEvent(Event event) {

				gBroj = 0;
			}
		});

		shell.addListener(SWT.Traverse, new Listener() {
			@Override
			public void handleEvent(Event e) {
				System.out.println(e.detail);
				if (e.detail == SWT.TRAVERSE_ESCAPE) {
					shell.dispose();
				}
			}
		});

		shell.setLayout(layout);

		// setLanguage(shell);

		// createControlButtons();

		shell.open();

		CenterScreen(shell, display);
		shell.setBackground(display.getSystemColor(SWT.COLOR_WHITE));

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}

	}

	private void createWidgets() {

		Canvas canvas = new Canvas(shell, SWT.NONE);
		canvas.setBackground(getColor(eColor.WHITE));
		canvas.setLayoutData("width 60,height 60,split 2");

		canvas.addPaintListener(new PaintListener() {
			public void paintControl(PaintEvent e) {
				Image image = new Image(display, "library/icons/boue48.ico");
				ImageData data = image.getImageData();
				// System.out.println(data.height);
				e.gc.drawImage(image, 10, 10);
				image.dispose();
			}
		});

		lbl = new Label(shell, SWT.NONE);
		lbl.setText(appName);
		lbl.setFont(getFont("Arial", 12, eFontType.BOLD_ITALIC));
		lbl.setLayoutData("width 150px,wrap");
		lbl.setBackground(display.getSystemColor(SWT.COLOR_WHITE));

		lbl = new Label(shell, SWT.WRAP | SWT.V_SCROLL);
		lbl.setText("Verzija aplikacije: " + appVer + "\n"
				+ "Copyrights by: Ivan Kovaèiæ");
		lbl.setFont(getFont("Arial", 9, eFontType.BOLD_ITALIC));
		lbl.setLayoutData("width 200px,height 150,gapleft 100");
		lbl.setBackground(display.getSystemColor(SWT.COLOR_WHITE));

	}

}
