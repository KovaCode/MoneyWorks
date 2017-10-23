import java.sql.ResultSet;

import kovaUtils.OpalDialog.kovaDialog;
import kovaUtils.OpalDialog.kovaDialog.OpalDialogType;

import net.miginfocom.swt.MigLayout;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

public class Categories extends DEF implements SelectionListener,
		MouseListener, KeyListener {

	static Shell shell;
	static Shell s;
	static Display display;
	MigLayout layout = new MigLayout("", "5[]2[]5", "15[]3[]7[]1"); //$NON-NLS-2$ //$NON-NLS-3$
	MigLayout layout2 = new MigLayout("");
	MigLayout layout3 = new MigLayout("");
	static MigLayout layout4 = new MigLayout("", "", "0[]7[]7[]1"); //$NON-NLS-2$ //$NON-NLS-3$
	static MigLayout layout5 = new MigLayout(
			"", "", "0[]3[]3[]15[]1[]3[]3[]3[]0"); //$NON-NLS-2$ //$NON-NLS-3$
	MigLayout layoutComp = new MigLayout("");
	Image icona;

	static Button cmdBrowse;
	static Button chkLogin;
	static Button cmdSpremi;
	static Combo cmbJezik;
	static Label lbl;
	static Text txtOpis;
	static Text txtPath;
	private Label sep;
	private static String SQLquery;
	private static ResultSet rs;
	static Label lblOpis;
	static Button btnDelete;
	static Button btnEdit;

	static Composite comp;
	static Composite comp2;
	static Label colorLabel;
	static Group group;
	static Group group2;
	static Group group3;
	static int IDbroj = 0;
	static int iRed = 0;
	private Color color;
	private Label colorLabel2;
	private Image tblIco;
	private static TableColumn colSort;
	private static Image icon;

	static kovaTable table;
	private static String val[][];
	private static Boolean flgSort = false;

	public Categories(Shell parent) {
		shell = new Shell(parent, SWT.APPLICATION_MODAL | SWT.DIALOG_TRIM);
		display = shell.getDisplay();

		shell.setSize(470, 400);
		shell.setText(DEF.getLabel("Categories"));

		icona = new Image(display, "library/icons/Boue.ico");
		shell.setImage(icona);

		color = new Color(shell.getDisplay(), new RGB(0, 255, 0));

		shell.addListener(SWT.Close, new Listener() {
			@Override
			public void handleEvent(Event event) {

				DEF.gBroj = 0;
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

		// createControlButtons();

		shell.open();

		DEF.CenterScreen(shell, display);
		shell.setBackground(display.getSystemColor(SWT.COLOR_WHITE));

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}

	}

	public static void main(String[] args) {
		new Categories(shell);
	}

	private void createWidgets() {
		Label lbl = new Label(shell, SWT.READ_ONLY | SWT.LEFT);
		// lbl.setText("To assign Color categories to the currently selected items, use the checkboxes next\n to each category. To edit category, select the category name and use the commands\n to the right.");
		lbl.setText(DEF.getLabel("mess47"));
		lbl.setLayoutData("width 370px,height 55px,wrap");
		lbl.setBackground(display.getSystemColor(SWT.COLOR_WHITE));

		comp = new Composite(shell, SWT.NONE);
		comp.setBackground(display.getSystemColor(SWT.COLOR_WHITE));
		comp.setLayoutData("width 150px,height 50px,split 2");
		comp.setLayout(layout4);

		comp2 = new Composite(shell, SWT.NONE);
		comp2.setBackground(display.getSystemColor(SWT.COLOR_WHITE));
		comp2.setLayoutData("width 150px,height 300px,wrap");
		comp2.setLayout(layout5);

		Button btn = new Button(comp2, SWT.PUSH);
		btn.setLayoutData("width 90px,height 25px,wrap");
		btn.setText(DEF.getLabel("New"));
		btn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent event) {
				DEF.gBroj = 0;
				new CategoriesNew(shell);
				freshTable();
			}
		});

		btnEdit = new Button(comp2, SWT.PUSH);
		btnEdit.setLayoutData("width 90px,height 25px,wrap");
		btnEdit.setText(DEF.getLabel("Edit"));
		btnEdit.setEnabled(false);
		btnEdit.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent event) {
				DEF.gBroj = IDbroj;
				new CategoriesNew(shell);
				freshTable();
				DEF.gBroj = 0;
			}
		});

		btnDelete = new Button(comp2, SWT.PUSH);
		btnDelete.setLayoutData("width 90px,height 25px,wrap");
		btnDelete.setText(DEF.getLabel("Delete"));
		btnDelete.setEnabled(false);
		btnDelete.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent event) {
				int odg = ALERT.show(
						DEF.getLabel("mess48"), "?", DEF.getLabel("mess48txt") + getColVal(1) + "\"", OpalDialogType.YES_NO, "", DEF.getLabel("mess48detail")); //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$

				if (odg == 0) {
					RSet.updateRS("update racuni set kategorija =0 where kategorija ="
							+ IDbroj);
					RSet.deleteRSid(IDbroj, "categories");
					kovaDialog.inform(
							DEF.getLabel("mess49"), DEF.getLabel("mess49txt")); //$NON-NLS-2$

					freshTable();
				}
			}
		});

		SQLquery = "select id,name as Naziv,R,G,B,shortcutkey as Kratica from categories";
		rs = RSet.openRS(SQLquery);

		table = fillKovaTable(comp);
		table.setLayoutData("width 330px,height 300px");
		table.contentTable.setLinesVisible(true);
		table.createSearchField(shell, SWT.BORDER).setLayoutData("growx,wrap");
		table.contentTable.addMouseListener(this);

		final TableColumn colID = table.getColumn(0);
		colID.setData("COL1");
		colID.setWidth(10);
		colID.setText(DEF.getLabel("IDCol"));
		colID.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				colSort = table.getColumn(0);
				setSort();
				table.applyFilter();
			}
		});

		TableColumn colNaziv = table.getColumn(1);
		colNaziv.setData("COL2");
		colNaziv.setWidth(170);
		colNaziv.setText(DEF.getLabel("colName"));
		colNaziv.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				colSort = table.getColumn(1);
				setSort();
				table.applyFilter();
			}
		});

		TableColumn colR = table.getColumn(2);
		colR.setWidth(0);
		colSort = table.getColumn(2);

		TableColumn colG = table.getColumn(3);
		colG.setWidth(0);
		colSort = table.getColumn(3);

		TableColumn colB = table.getColumn(4);
		colB.setWidth(0);
		colB.setResizable(false);
		colSort = table.getColumn(4);

		TableColumn colshortKey = table.getColumn(5);
		colshortKey.setData("COL3");
		colshortKey.setText(DEF.getLabel("ShortCut"));
		colshortKey.setWidth(50);
		colshortKey.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				colSort = table.getColumn(5);
				setSort();
				table.applyFilter();
			}
		});

		table.contentTable.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event e) {
				System.out.println(table.contentTable.getSelectionIndex());

				if (table.contentTable.getSelectionIndex() != -1) {
					IDbroj = Integer.parseInt(DEF.iNull0(table.getItem(
							table.contentTable.getSelectionIndex()).getText()));
					btnEdit.setEnabled(true);
					btnDelete.setEnabled(true);
				} else {
					IDbroj = 0;
					btnEdit.setEnabled(false);
					btnDelete.setEnabled(false);
				}
				// System.out.println(IDbroj);
			}
		});

		for (TableItem item : table.getItems()) {
			icon = createIcon(new Color(shell.getDisplay(), new RGB(
					Integer.parseInt(item.getText(2)), Integer.parseInt(item
							.getText(3)), Integer.parseInt(item.getText(4)))));
			item.setImage(1, icon);
		}

	}

	static void applyColors(final TableItem target) {
		if (iRed == 0) {
			if (target.getText(2) != "") {
				icon = createIcon(new Color(shell.getDisplay(), new RGB(
						Integer.parseInt(target.getText(2)),
						Integer.parseInt(target.getText(3)),
						Integer.parseInt(target.getText(4)))));
				target.setImage(1, icon);
			}

			target.setBackground(getColor(eColor.WHITE));
			iRed = 1;
		} else {
			if (target.getText(2) != "") {
				icon = createIcon(new Color(shell.getDisplay(), new RGB(
						Integer.parseInt(target.getText(2)),
						Integer.parseInt(target.getText(3)),
						Integer.parseInt(target.getText(4)))));
				target.setImage(1, icon);
			}
			target.setBackground(getColorRGB(191, 216, 255));
			iRed = 0;
		}

	}

	static Image createIcon(Color color) {
		Image image = new Image(display, 20, 20);
		GC gc = new GC(image);
		gc.setBackground(color);
		gc.fillRectangle(5, 5, 10, 10);
		gc.dispose();
		ImageData imageData = image.getImageData();

		PaletteData palette = new PaletteData(new RGB[] { new RGB(0, 0, 0),
				new RGB(0xFF, 0xFF, 0xFF), });
		ImageData maskData = new ImageData(20, 20, 1, palette);
		Image mask = new Image(display, maskData);
		gc = new GC(mask);
		gc.setBackground(getColor(eColor.WHITE));
		gc.fillRectangle(0, 0, 20, 20);
		gc.setBackground(getColor(eColor.WHITE));
		gc.fillRectangle(5, 5, 10, 10);
		gc.dispose();
		maskData = mask.getImageData();

		final Image icon = new Image(display, imageData, maskData);
		return icon;
	}

	private void freshTable() {

		SQLquery = "select id,name as Name,R,G,B,shortcutkey as Shortcut_key from categories";
		rs = RSet.openRS(SQLquery);

		table.resetValues(kovaTable.fillVal(rs));

		for (TableItem item : table.getItems()) {
			// System.out.println("trr");
			icon = createIcon(new Color(shell.getDisplay(), new RGB(
					Integer.parseInt(item.getText(2)), Integer.parseInt(item
							.getText(3)), Integer.parseInt(item.getText(4)))));
			item.setImage(1, icon);
		}
		RSet.closeRS();
		btnEdit.setEnabled(false);
		btnDelete.setEnabled(false);

		table.sortTable(colSort);
		table.applyFilter();

		for (TableItem singleItem : table.getItems()) {
			applyColors(singleItem);
		}

		if (flgSort == true) {
			table.lastDescendingColumn = null;
		} else {
			table.lastDescendingColumn = colSort;
		}

	}

	@Override
	public void widgetDefaultSelected(SelectionEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void widgetSelected(SelectionEvent arg0) {
		// TODO Auto-generated method stub

	}

	private static String getColVal(int kolona) {
		String val = "";
		if (table.contentTable.getSelectionCount() > 0) {
			val = table.getItem(table.contentTable.getSelectionIndex())
					.getText(kolona);
		}
		return val;
	}

	private static kovaTable fillKovaTable(final Composite parent) {

		// može ovako, a može se i iz metaData-e izèitati
		final String[] header = { "ID", "Naziv", "R", "G", "B", "Kratica" }; //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$

		SQLquery = "select id,name as Name,R,G,B,shortcutkey as Shortcut_key from categories";
		rs = RSet.openRS(SQLquery);

		val = kovaTable.fillVal(rs);

		// create the table itself
		final kovaTable table = kovaTable.createKovaTable(parent,
				SWT.FULL_SELECTION | SWT.MULTI, header, val);
		table.setLayoutData("width 470px,height 345px,span");

		for (TableColumn singleColumn : table.getColumns()) {
			singleColumn.pack();
		}

		kovaTable.setColumnsMeta(rs, table);
		table.setEditable(false, 1, 2, 3);
		/*
		 * highlight first column items
		 */
		table.addColorListener(new Listener() {
			@Override
			public void handleEvent(final Event event) {
				for (TableItem singleItem : table.getItems()) {
					applyColors(singleItem);
				}
			}
		});
		// initial coloring
		for (TableItem singleItem : table.getItems()) {
			applyColors(singleItem);
		}

		table.sortTable(table.getColumn(1));

		return table;
	}

	@Override
	public void mouseDown(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseUp(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyPressed(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseDoubleClick(MouseEvent arg0) {
		DEF.gBroj = IDbroj;
		new CategoriesNew(shell);
		freshTable();

	}

	private static void setSort() {
		flgSort = (flgSort) ? false : true;
		table.lastDescendingColumn = (flgSort) ? colSort : null;

	}

}