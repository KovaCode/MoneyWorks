import java.sql.ResultSet;
import java.sql.SQLException;

import kovaUtils.OpalDialog.kovaDialog;
import kovaUtils.OpalDialog.kovaDialog.OpalDialogType;

import net.miginfocom.swt.MigLayout;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.ColorDialog;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class CategoriesNew extends DEF {
	static Shell shell;
	static Shell s;
	static Display display;
	MigLayout layout = new MigLayout("", "5[]2[]5", "8[]5[]7[]1"); //$NON-NLS-2$ //$NON-NLS-3$
	MigLayout layout2 = new MigLayout("");
	MigLayout layout3 = new MigLayout("");
	final Image icona;

	static Text txtName;
	static Combo cmb2;

	private static int RGBred;
	private static int RGBgreen;
	private static int RGBblue;

	static Color red;
	static Color white;
	static Color black;
	private static ResultSet rs;
	private Color color;
	private Button cmdUnos;
	private Button cmdOdustani;
	private Label sep;
	static Button cmdColor;

	public CategoriesNew(Shell parent) {
		shell = new Shell(parent, SWT.APPLICATION_MODAL | SWT.DIALOG_TRIM);
		display = shell.getDisplay();

		shell.setSize(300, 140);
		shell.setText(getLabel("Categories"));

		icona = new Image(display, "library/icons/Boue.ico");
		shell.setImage(icona);

		red = display.getSystemColor(SWT.COLOR_RED);
		white = display.getSystemColor(SWT.COLOR_WHITE);
		black = display.getSystemColor(SWT.COLOR_BLACK);

		color = new Color(shell.getDisplay(), new RGB(0, 255, 0));

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

		createWidgets();

		System.out.println(gBroj);
		if (gBroj != 0) {
			fillForm();
		}

		// createControlButtons();

		shell.open();

		CenterScreen(shell, display);
		shell.setBackground(display.getSystemColor(SWT.COLOR_WHITE));

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}

	}

	private static void fillForm() {
		rs = RSet
				.openRS("select id,name,shortcutkey,r,g,b from categories where ID="
						+ gBroj);
		try {
			while (rs.next()) {
				txtName.setText(rs.getString("name"));
				cmdColor.setImage(createIcon(new Color(shell.getDisplay(),
						new RGB(rs.getInt("R"), rs.getInt("G"), rs.getInt("B"))))); //$NON-NLS-2$ //$NON-NLS-3$

			}
			rs.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.loggErr("CategoriesNew " + e.getMessage());
		}
	}

	private void createWidgets() {

		Label lblName = new Label(shell, SWT.NONE);
		lblName.setLayoutData("width 40px,height 15px,split2");
		lblName.setBackground(display.getSystemColor(SWT.COLOR_WHITE));
		lblName.setText(getLabel("colName"));

		txtName = new Text(shell, SWT.BORDER);
		txtName.setLayoutData("width 220px ,wrap");

		Label lblColor = new Label(shell, SWT.NONE);
		lblColor.setLayoutData("width 40px,height 15px,split4");
		lblColor.setBackground(display.getSystemColor(SWT.COLOR_WHITE));
		lblColor.setText(getLabel("Color"));

		Image image = new Image(display, 20, 20);
		GC gc = new GC(image);
		gc.setBackground(red);
		gc.fillRectangle(5, 5, 10, 10);
		gc.dispose();
		ImageData imageData = image.getImageData();

		PaletteData palette = new PaletteData(new RGB[] { new RGB(0, 0, 0),
				new RGB(0xFF, 0xFF, 0xFF), });
		ImageData maskData = new ImageData(20, 20, 1, palette);
		Image mask = new Image(display, maskData);
		gc = new GC(mask);
		gc.setBackground(black);
		gc.fillRectangle(2, 2, 50, 20);
		gc.setBackground(white);
		gc.fillRectangle(5, 5, 10, 10);
		gc.dispose();
		maskData = mask.getImageData();

		Image icon = new Image(display, imageData, maskData);

		cmdColor = new Button(shell, SWT.PUSH);
		cmdColor.setImage(icon);
		cmdColor.setLayoutData("width 40px,height 20px,gapright 20px");
		cmdColor.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent event) {
				ColorDialog dlg = new ColorDialog(shell);
				// dlg.setRGB(colorLabel.getBackground().getRGB());
				dlg.setText(getLabel("ColorSel"));
				RGB rgb = dlg.open();
				// System.out.println(rgb);
				if (rgb != null) {
					color.dispose();
					color = new Color(shell.getDisplay(), rgb);
					// colorLabel.setBackground(color);

					RGBblue = color.getBlue();
					RGBred = color.getRed();
					RGBgreen = color.getGreen();

					// System.out.println(RGBred);

					Image image = new Image(display, 20, 20);
					GC gc = new GC(image);
					gc.setBackground(color);
					gc.fillRectangle(5, 5, 10, 10);
					gc.dispose();
					ImageData imageData = image.getImageData();

					PaletteData palette = new PaletteData(new RGB[] {
							new RGB(0, 0, 0), new RGB(0xFF, 0xFF, 0xFF), });
					ImageData maskData = new ImageData(20, 20, 1, palette);
					Image mask = new Image(display, maskData);
					gc = new GC(mask);
					gc.setBackground(black);
					gc.fillRectangle(0, 0, 20, 20);
					gc.setBackground(white);
					gc.fillRectangle(5, 5, 10, 10);
					gc.dispose();
					maskData = mask.getImageData();

					final Image icon = new Image(display, imageData, maskData);
					cmdColor.setImage(icon);

				}
			}
		});

		Label lblShort = new Label(shell, SWT.NONE);
		lblShort.setLayoutData("width 50px,height 15px");
		lblShort.setBackground(display.getSystemColor(SWT.COLOR_WHITE));
		lblShort.setText(getLabel("ShortCut"));

		cmb2 = new Combo(shell, SWT.DROP_DOWN | SWT.READ_ONLY);
		cmb2.setLayoutData("width 90px,height 25px,wrap");
		cmb2.add("(None)");
		cmb2.add("(CTRL+F1)");
		cmb2.add("(CTRL+F2)");
		cmb2.add("(CTRL+F3)");
		cmb2.add("(CTRL+F4)");
		cmb2.add("(CTRL+F5)");
		cmb2.add("(CTRL+F6)");
		cmb2.add("(CTRL+F7)");
		cmb2.add("(CTRL+F8)");
		cmb2.add("(CTRL+F9)");
		cmb2.add("(CTRL+F10)");
		cmb2.select(0);

		sep = new Label(shell, SWT.SEPARATOR | SWT.SHADOW_IN | SWT.HORIZONTAL);
		sep.setLayoutData("width 300px,wrap");
		sep.setData("SEP");

		cmdUnos = new Button(shell, SWT.PUSH);
		cmdUnos.setText(getLabel("Ok"));
		cmdUnos.setLayoutData("gapleft 140px,width 70px,split2");
		cmdUnos.setEnabled(true);
		cmdUnos.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event e) {

				login.setFlgProfile(true);

				if (gBroj != 0) {
					int odg = ALERT.show(getLabel("mess43"), "?",
							getLabel("mess43txt") + txtName.getText() + "\" ("
									+ gBroj + ")", OpalDialogType.OK_CANCEL,
							"", "");

					if (odg == 0) {

						RSet.updateRS("update categories set name='"
								+ txtName.getText() + "', shortcutKey='"
								+ cmb2.getText() + "',R = " + RGBred + ",G = "
								+ RGBgreen + ",B = " + RGBblue + " where ID="
								+ gBroj);
						kovaDialog.inform(
								shell,
								getLabel("mess44"),
								getLabel("mess44txt") + "<br/><b>"
										+ txtName.getText() + "\" </b>");
						shell.dispose();
					}
				} else {

					rs = RSet
							.openRS("Select max (red)as maximum from categories");

					int red = 0;
					try {

						while (rs.next()) {
							red = rs.getInt(1);
							System.out.println(rs.getInt("maximum"));
						}

						rs.close();

					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
						logger.loggErr("CategoriesNew " + e1.getMessage());
					}
					System.out.println(red);

					// int odg=ALERT.open(shell,"?", "Spremanje kategorije?",
					// "Želite li spremiti novu kategoriju \""+txtName.getText()
					// +"\"", "Spremi|Odustani", 0);

					int odg = ALERT.show(getLabel("mess45"), "?",
							getLabel("mess45txt") + txtName.getText() + "\"",
							OpalDialogType.OK_CANCEL, "", "");
					if (odg == 0) {
						String col = "name,shortcutKey,R,G,B,red";
						String val = "'" + txtName.getText() + "','"
								+ cmb2.getText() + "'," + RGBred + ","
								+ RGBgreen + "," + RGBblue + "," + (red + 1);
						RSet.addRS2("categories", col, val);

						// ALERT.open(shell,"i", "Spremanje uspješno!",
						// "Nova kategorija \""+txtName.getText()
						// +"\" uspješno saèuvana!", "Nastavak", 0);
						kovaDialog.inform(shell, getLabel("mess46"),
								getLabel("mess46txt") + txtName.getText()
										+ "\" uspješno saèuvana!");
						shell.dispose();
					}
				}

			}

		});

		cmdOdustani = new Button(shell, SWT.PUSH);
		cmdOdustani.setText(getLabel("Cancel"));
		cmdOdustani.setLayoutData("width 70px");
		cmdOdustani.setEnabled(true);
		cmdOdustani.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event e) {
				System.runFinalizersOnExit(true);
				shell.dispose();

			}

		});

	}

	static Image createIcon(Color color) {
		Image image = new Image(display, 10, 10);
		GC gc = new GC(image);
		gc.setBackground(color);
		gc.fillRectangle(0, 0, 10, 10);
		gc.dispose();
		ImageData imageData = image.getImageData();

		PaletteData palette = new PaletteData(new RGB[] { new RGB(255, 0, 0),
				new RGB(0xFF, 0xFF, 0xFF), });
		ImageData maskData = new ImageData(10, 10, 1, palette);
		Image mask = new Image(display, maskData);
		gc = new GC(mask);
		// gc.setBackground(red);
		// gc.fillRectangle(0,0, 0, 0);
		// gc.fillOval(0,0,50,50);
		gc.fillRectangle(0, 0, 80, 80);
		gc.dispose();
		maskData = mask.getImageData();

		// Image icon = new Image(display,imageData);
		final Image icon = new Image(display, imageData, maskData);
		return icon;
	}

}
