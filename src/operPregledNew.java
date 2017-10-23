import java.sql.ResultSet;
import java.sql.SQLException;

import kovaUtils.OpalDialog.kovaDialog.OpalDialogType;

import net.miginfocom.swt.MigLayout;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

public class operPregledNew extends DEF implements FocusListener, KeyListener,
		MouseListener {

	private static Display display;
	private static Shell shell;
	static MigLayout layout = new MigLayout();
	static MigLayout layout2 = new MigLayout();

	private static Group group;
	private static int iRed;
	private ResultSet rs2;

	static Button cmdNovi;
	static Button cmdObrisi;
	static Button cmdUredi;
	private static String SQLquery;
	private static ResultSet rs;
	private static String val[][];

	private Label lblUkIznos;
	private Label lbl;
	protected int IDbroj;
	protected String sTablica = "operateri";

	static kovaTable table;

	public operPregledNew(Shell parent) {
		shell = new Shell(parent, SWT.APPLICATION_MODAL | SWT.DIALOG_TRIM);
		display = shell.getDisplay();

		Image icona = new Image(display, "library/icons/Boue.ico");
		shell.setImage(icona);

		CenterScreen(shell, shell.getDisplay());

		shell.setText(appName + " " + getLabel("GR4")); //$NON-NLS-2$
		shell.setBackground(display.getSystemColor(SWT.COLOR_WHITE));
		shell.setSize(350, 500);
		shell.setLayout(layout);

		createWidgets();

		shell.open();

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}

	}

	public static void main(String[] args) {
		new operPregledNew(shell);
	}

	private void createWidgets() {
		// *****************************TABLICA******************************//

		group = new Group(shell, SWT.NONE);
		group.setText(getLabel("GR4"));
		group.setBackground(display.getSystemColor(SWT.COLOR_WHITE));
		group.setForeground(display.getSystemColor(SWT.COLOR_BLUE));
		group.setLayoutData("width 220px,wrap");
		group.setLayout(layout2);

		// ************************************kreiranje
		// tablice********************************************//

		// RSet.setSystemBase();
		// rs2 = RSet.openRS("select id,ime,prezime,korisnik from operateri");

		table = fillKovaTable(group);

		// tableOper=KTable.createKTable(group,rs2);
		// tableOper.addFocusListener(this);
		// tableOper.addKeyListener(this);
		// tableOper.setData("TBL");

		lblUkIznos = new Label(group, SWT.RIGHT);
		lblUkIznos.setLayoutData("width 120px,span,gapleft 340");
		lblUkIznos.setBackground(display.getSystemColor(SWT.COLOR_WHITE));

		lbl = new Label(group, SWT.NONE);
		lbl.setText(getLabel("lblFast"));
		lbl.setBackground(display.getSystemColor(SWT.COLOR_WHITE));

		table.createSearchField(group, SWT.BORDER);
		table.contentTable.addMouseListener(this);

		table.contentTable.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event e) {
				IDbroj = Integer.parseInt(table.getItem(
						table.contentTable.getSelectionIndex()).getText());

			}
		});

		final TableColumn IDCol = table.getColumn(0);
		IDCol.setData("IDCol");
		IDCol.setText(getLabel("IDCol"));
		IDCol.setWidth(30);

		final TableColumn imeCol = table.getColumn(1);
		imeCol.setData("colName");
		imeCol.setText(getLabel("colName"));
		imeCol.setWidth(100);

		TableColumn prezimeCol = table.getColumn(2);
		prezimeCol.setText(getLabel("Surname"));
		prezimeCol.setWidth(100);

		TableColumn userCol = table.getColumn(3);
		userCol.setText(getLabel("colUser"));
		userCol.setData("COL3");
		userCol.setWidth(60);

		table.setLayoutData("width 300px,height 345px,span");

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

		cmdNovi = new Button(shell, SWT.PUSH);
		cmdNovi.setText(getLabel("NewOper"));
		cmdNovi.setLayoutData("width 100px,split 3");
		cmdNovi.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event e) {
				gBroj = 0;
				new operUnos(shell);
				freshTable();
			}

		});

		cmdUredi = new Button(shell, SWT.PUSH);
		cmdUredi.setText(getLabel("Edit"));
		cmdUredi.setLayoutData("width 100px,split 3");
		cmdUredi.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event e) {
				gBroj = IDbroj;
				new operUnos(shell);
				freshTable();
				gBroj = 0;

			}

		});

		cmdObrisi = new Button(shell, SWT.PUSH);
		cmdObrisi.setText(getLabel("Delete"));
		cmdObrisi.setLayoutData("width 100px,split 3");
		cmdObrisi.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event e) {
				// int odg = ALERT.open(shell, "?", "Brisanje operatera?",
				// "Želite li obrisati operatera: "+ getColVal(1)+ " " +
				// getColVal(2)+"", "Obriši|Odustani",0);
				int odg = ALERT
						.show(getLabel("mess40"), "?", getLabel("mess40txt") + getColVal(1) + " " + getColVal(2) + "?", OpalDialogType.OK_CANCEL, "", ""); //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$

				if (odg == 0) {
					logger.logg(getLabel("logg11") + " " + sTablica + " (ID="
							+ IDbroj + ")");
					RSet.deleteRSid(IDbroj, sTablica);
					freshTable();
				} else {

				}

			}

		});

	}

	private void freshTable() {
		rs2 = RSet.openRS("select id,ime,prezime,korisnik from operateri");
		;
		table.resetValues(kovaTable.fillVal(rs2));
		try {
			rs2.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.loggErr("operPregledNew " + e.getMessage());
		}

	}

	static void applyColors(final TableItem target) {
		if (iRed == 0) {
			target.setBackground(target.getDisplay().getSystemColor(
					SWT.COLOR_WIDGET_LIGHT_SHADOW));
			iRed = 1;
		} else {
			target.setBackground(target.getDisplay().getSystemColor(
					SWT.COLOR_WHITE));
			iRed = 0;
		}

	}

	@Override
	public void focusGained(FocusEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void focusLost(FocusEvent arg0) {
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
		gBroj = IDbroj;
		new operUnos(shell);
		freshTable();

	}

	@Override
	public void mouseDown(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseUp(MouseEvent arg0) {
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
		final String[] header = { "ID", "Ime", "Prezime", "Korisnik" };

		SQLquery = "select id,ime,prezime,korisnik from operateri";
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
		return table;
	}

}