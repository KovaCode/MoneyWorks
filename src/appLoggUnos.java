import java.sql.ResultSet;
import java.sql.SQLException;

import kovaUtils.OpalDialog.kovaDialog;
import kovaUtils.OpalDialog.kovaDialog.OpalDialogType;
import net.miginfocom.swt.MigLayout;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.TreeItem;

public class appLoggUnos extends DEF {

	static int IDbroj = 0;
	static Shell shell;
	static Display display;
	static Image icona;
	MigLayout layout = new MigLayout("");
	MigLayout layoutU = new MigLayout("");

	static Boolean flgEdit = false;
	private Composite compUnos;
	private Label lbl;
	private Label lblOper;
	private Label lblVer;
	private Label sep;
	private Text txtNaslov;
	private Spinner spin;
	private StyledText txtOpis;
	private Button cmdSpremi;
	private Button cmdVerzija;
	private ResultSet rs;

	public appLoggUnos(Shell parent) {
		shell = new Shell(parent, SWT.APPLICATION_MODAL | SWT.DIALOG_TRIM);
		display = shell.getDisplay();

		shell.setSize(500, 450);
		shell.setText(getLabel("about"));

		icona = new Image(display, "library/icons/Boue.ico");
		shell.setImage(icona);

		// ALERT.openInput(shell);

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

		if (gBroj > 0) {
			IDbroj = gBroj;
			fillForm(IDbroj);
			flgEdit = true;
			gBroj = 0;
		}

		shell.open();

		CenterScreen(shell, display);
		shell.setBackground(display.getSystemColor(SWT.COLOR_WHITE));

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}

	}

	private void fillForm(int id) {
		rs = RSet
				.openRS("select id,naziv,opis,postotak,datum,ver,(select ime+' '+prezime from operateri where id = appLogg.oper)as sOper from appLogg where id= "
						+ id);
		try {
			while (rs.next()) {
				txtNaslov.setText(rs.getString("naziv"));
				lblVer.setText(rs.getString("ver"));
				lblOper.setText(rs.getString("sOper"));
				txtOpis.setText(rs.getString("opis"));
				spin.setSelection(rs.getInt("postotak"));
			}
			rs.close();

		} catch (SQLException e) {
			kovaDialog.showException(e);
			e.printStackTrace();
		}

	}

	private void createWidgets() {

		compUnos = new Composite(shell, SWT.NONE);
		compUnos.setBackground(display.getSystemColor(SWT.COLOR_WHITE));
		compUnos.setLayout(layoutU);
		compUnos.setLayoutData("wrap");

		lbl = new Label(compUnos, SWT.NONE);
		lbl.setText(getLabel("lblUser"));
		lbl.setLayoutData("width 50px,split 4");
		lbl.setBackground(display.getSystemColor(SWT.COLOR_WHITE));

		lblOper = new Label(compUnos, SWT.NONE);
		lblOper.setText(RSet.getOsoba(getgUserID()));
		lblOper.setLayoutData("width 120px");
		lblOper.setForeground(getColor(eColor.RED));
		lblOper.setBackground(display.getSystemColor(SWT.COLOR_WHITE));

		lbl = new Label(compUnos, SWT.NONE);
		lbl.setText(getLabel("lblVerzija"));
		lbl.setLayoutData("width 60px");
		lbl.setBackground(display.getSystemColor(SWT.COLOR_WHITE));

		lblVer = new Label(compUnos, SWT.NONE);
		lblVer.setText(appVer);
		lblVer.setLayoutData("width 60px,wrap");
		lblVer.setBackground(display.getSystemColor(SWT.COLOR_WHITE));

		// lblTime = new Label(compUnos,SWT.NONE);
		// lblTime.setBackground(display.getSystemColor(SWT.COLOR_WHITE));
		// lblTime.setLayoutData("width 100,wrap,gapleft 30");
		// // lblStatus3.setText(RSet.getOsoba(getgUserID()));
		// lblTime.setText(sdf2.format(new Date(System.currentTimeMillis())));
		//
		// final int time = 1000;
		// Runnable timer2 = new Runnable() {
		// public void run() {
		// lblTime.setText(sdf2.format(new Date(System.currentTimeMillis())));
		// display2.timerExec(time, this);
		// }
		// };
		// display2.timerExec(time, timer2);

		sep = new Label(compUnos, SWT.SEPARATOR | SWT.SHADOW_IN
				| SWT.HORIZONTAL);
		sep.setData("sep");
		sep.setLayoutData("width 470,wrap");

		lbl = new Label(compUnos, SWT.NONE);
		lbl.setText(getLabel("lblNaziv"));
		lbl.setLayoutData("width 50px,split 2");
		lbl.setBackground(display.getSystemColor(SWT.COLOR_WHITE));

		txtNaslov = new Text(compUnos, SWT.SINGLE | SWT.BORDER);
		txtNaslov.setTextLimit(50);
		txtNaslov.setLayoutData("width 390px,wrap");
		txtNaslov.addVerifyListener(new VerifyListener() {
			public void verifyText(VerifyEvent e) {
				e.text = e.text.toUpperCase();
			}
		});

		lbl = new Label(compUnos, SWT.NONE);
		// lbl.setText (getLabel("lblPostoZavr"));
		lbl.setText("Završeno:");
		lbl.setLayoutData("width 50px,split 3");
		lbl.setBackground(display.getSystemColor(SWT.COLOR_WHITE));

		spin = new Spinner(compUnos, SWT.BORDER);
		spin.setLayoutData("width 60px");
		spin.setMinimum(0);
		spin.setMaximum(100);
		spin.setSelection(0);
		spin.setIncrement(5);
		spin.setPageIncrement(5);

		lbl = new Label(compUnos, SWT.NONE);
		lbl.setText("(%)");
		lbl.setLayoutData("width 20px,wrap");
		lbl.setBackground(display.getSystemColor(SWT.COLOR_WHITE));

		lbl = new Label(compUnos, SWT.NONE);
		lbl.setText(getLabel("lblLogg"));
		lbl.setLayoutData("width 50px,split 2");
		lbl.setBackground(display.getSystemColor(SWT.COLOR_WHITE));

		txtOpis = new StyledText(compUnos, SWT.BORDER | SWT.V_SCROLL);
		txtOpis.setTextLimit(500);
		txtOpis.setLayoutData("width 390px,wrap,height 150,wrap");
		txtOpis.setWordWrap(true);

		sep = new Label(compUnos, SWT.SEPARATOR | SWT.SHADOW_IN
				| SWT.HORIZONTAL);
		sep.setData("sep");
		sep.setLayoutData("width 470,wrap");

		cmdSpremi = new Button(compUnos, SWT.PUSH);
		cmdSpremi.setLayoutData("gapleft 350,width 100px,height 20px,split2");
		cmdSpremi.setText("Spremi");
		cmdSpremi.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				switch (e.type) {
				case SWT.Selection:
					if (checkValues() == true) {
						Spremi();
						shell.dispose();
					}

					break;
				}
			}
		});

	}

	private boolean checkValues() {
		Boolean flgChk = false;

		if (txtNaslov.getText().length() > 10) {
			flgChk = true;

			if (txtOpis.getText().length() > 10) {
				flgChk = true;
			} else {
				kovaDialog.inform("Greška unosa!",
						"Potrebno je unijeti više od 10 znakova za opis!");
				logger.loggErr("Potrebno je unijeti više od 10 znakova za opis!");
				flgChk = false;
			}
		} else {
			kovaDialog.inform("Greška unosa!",
					"Potrebno je unijeti više od 10 znakova za naslov!");
			logger.loggErr("Potrebno je unijeti više od 10 znakova za naslov!");
			flgChk = false;
		}

		return flgChk;

	}

	private void Spremi() {
		String sqlDatum = getDateSQL();

		int odg = ALERT.show("Spremanje logg-a aplikacije", "?",
				"Spremiti logg aplikacije?", OpalDialogType.SAVE, "", "");

		if (odg == 0) {
			if (flgEdit == false) {
				System.out.println(flgEdit);
				String col = "naziv,ver,opis,datum,oper,postotak";
				String val = "'" + txtNaslov.getText() + "','"
						+ lblVer.getText() + "','" + txtOpis.getText() + "','"
						+ sqlDatum + "'," + getgUserID() + "," + spin.getText();
				RSet.addRS2("appLogg", col, val);
				logger.logg(getLabel("logg8") + "appLogg");
				kovaDialog.inform("Spremanje uspješno završeno!", "");

			} else {
				RSet.updateRS("update appLogg set naziv ='"
						+ txtNaslov.getText() + "',ver='" + lblVer.getText()
						+ "',opis='" + txtOpis.getText() + "',datum='"
						+ sqlDatum + "',oper=" + getgUserID() + ",postotak="
						+ spin.getText() + " where id=" + IDbroj);
				logger.logg(getLabel("logg8") + "appLogg");
				kovaDialog.inform("Ažuriranje uspješno završeno!", "");
			}

			txtNaslov.setText("");
			txtOpis.setText("");
			spin.setDigits(0);
		}

	}

}
