import java.sql.ResultSet;
import java.sql.SQLException;

import kovaUtils.OpalDialog.kovaDialog;
import kovaUtils.OpalDialog.kovaDialog.OpalDialogType;

import net.miginfocom.swt.MigLayout;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

//napraviti da se upisuje svaki red!//

public class balanceForm extends DEF implements FocusListener, KeyListener,
		VerifyListener, SelectionListener {

	static Display display;
	static Shell shell;
	static Image icona;
	static MigLayout layout = new MigLayout();
	static MigLayout layout2 = new MigLayout();
	static MigLayout layout3 = new MigLayout();
	static Composite comp;

	static ResultSet rs;

	static int optIndex = 0;
	static String optVal = "Uplata";
	static String datum = getDateSQL();
	static double ukupno;

	static Text txtIznos;

	static Button cmdSpremi;
	static Button cmdOdustani;
	static Button[] opt;
	static Button cmdDodUkRN;
	static Button cmdDodUkMJ;
	static Button cmdDodUkOST;

	static Listener listener;

	static DateTime dtp;

	static Label sep;
	static Label lbl;
	static Label lblDatum;
	static Label lblOper;
	static Label lblIznosUkRN;
	static Label lblIznosUkMJ;
	static Label lblIznosUkOST;
	static Label lblIznos;
	static Label lblIznosUk;
	static Label lblUkupno;

	static double ukupnoMJ;
	static double ukupnoRN;
	static double ukupnoOST;

	private int year;
	private int month;

	public balanceForm(Shell parent, int month, int year) {
		shell = new Shell(parent, SWT.APPLICATION_MODAL | SWT.DIALOG_TRIM);
		display = shell.getDisplay();
		// shell = new Shell(display, SWT.APPLICATION_MODAL|SWT.DIALOG_TRIM);

		shell.setSize(250, 290);

		this.month = month;
		this.year = year;

		icona = new Image(display, "library/icons/Boue.ico");
		shell.setText("Pregled i ureðivanje stanja raèuna");
		shell.setImage(icona);

		shell.addListener(SWT.Close, new Listener() {
			public void handleEvent(Event event) {

				gBroj = 0;
			}
		});

		shell.setLayout(layout);

		System.out.println(getColor(eColor.WHITE));

		createWidgets();
		createButtons();

		shell.open();

		CenterScreen(shell, display);
		shell.setBackground(display.getSystemColor(SWT.COLOR_WHITE));

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
	}

	private void createButtons() {

	}

	private void createWidgets() {
		comp = new Composite(shell, SWT.NONE);
		comp.setLayoutData("width 310px,wrap");
		comp.setBackground(display.getSystemColor(SWT.COLOR_WHITE));
		comp.setLayout(layout2);

		listener = new Listener() {

			public void handleEvent(Event e) {
				Button bt = (Button) e.widget;

				for (int i = 0; i < 4; i++) {
					if (bt.equals(opt[i])) {
						optIndex = i;
						break;
					}

				}

				switch (optIndex) {
				case 0: // uplata
					optVal = "Uplata";
					break;

				case 1: // isplata
					optVal = "Isplata";
					break;

				}

			}

		};

		opt = new Button[2];
		for (int i = 0; i < opt.length; i++) {
			opt[i] = new Button(comp, SWT.RADIO);
			opt[i].setText(getLabel("opt" + (4 + i)));
			opt[i].setLayoutData("width 30px");
			opt[i].setBackground(display.getSystemColor(SWT.COLOR_WHITE));
			opt[i].addListener(SWT.Selection, listener);
		}

		opt[0].setLayoutData("split 2");
		opt[1].setLayoutData("wrap");
		opt[0].setSelection(true);

		sep = new Label(comp, SWT.SEPARATOR | SWT.SHADOW_IN | SWT.HORIZONTAL);
		sep.setLayoutData("width 230px,wrap");
		sep.setData("SEP");

		lblDatum = new Label(comp, SWT.NONE);
		lblDatum.setText(getLabel("lblDatum"));
		lblDatum.setData("L3");
		lblDatum.setLayoutData("width 70px,split2,gapright 6px,gapleft 2px");
		lblDatum.setBackground(getColor(eColor.WHITE));

		dtp = new DateTime(comp, SWT.DATE | SWT.DROP_DOWN);
		dtp.setLayoutData("width 100px,wrap");
		dtp.setDate(month, year, getToday().getDay());
		dtp.setMonth(getToday().getMonth());
		dtp.setYear(getToday().getYear());
		dtp.addSelectionListener(this);

		dtp.addListener(SWT.Selection, new Listener() {

			public void handleEvent(Event e) {
				switch (e.type) {
				case SWT.Selection:
					dtp.setDate(dtp.getYear(), dtp.getMonth(), dtp.getDay());
					datum = dtp.getYear()
							+ "-" + dtp.getMonth() + "-" + dtp.getDay(); //$NON-NLS-2$
					break;
				}
			}

		});

		// lblIznos = new Label (comp, SWT.NONE);
		// lblIznos.setText (getLabel("lblIznos"));
		// lblIznos.setLayoutData
		// ("width 61px,split2,gapright 15px,gapleft 2px");
		// lblIznos.setBackground(getColor(eColor.WHITE ));
		//
		// txtIznos = new Text (comp, SWT.BORDER|SWT.RIGHT);
		// txtIznos.setText("0,00");
		// txtIznos.setTextLimit(15);
		// txtIznos.setLayoutData ("width 85px,wrap");
		// txtIznos.addFocusListener(this);
		// txtIznos.addKeyListener(this);
		// txtIznos.addVerifyListener(this);

		sep = new Label(comp, SWT.SEPARATOR | SWT.SHADOW_IN | SWT.HORIZONTAL);
		sep.setLayoutData("width 230px,wrap");
		sep.setData("SEP");

		lbl = new Label(comp, SWT.NONE);
		lbl.setText(DEF.getLabel("lblTotalMJ"));
		lbl.setLayoutData("width 61px,split3,gapright 5,gapleft 2px");
		lbl.setBackground(getColor(eColor.WHITE));

		lblIznosUkMJ = new Label(comp, SWT.NONE);
		lblIznosUkMJ.setLayoutData("width 80px");
		lblIznosUkMJ.setAlignment(SWT.RIGHT);
		lblIznosUkMJ.setBackground(getColor(eColor.WHITE));

		cmdDodUkMJ = new Button(comp, SWT.PUSH);
		cmdDodUkMJ.setText(getLabel("etc"));
		cmdDodUkMJ.setLayoutData("width 30px,wrap");
		cmdDodUkMJ.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				Double iznos = 0.0;
				String izn = ALERT.input(shell, optVal, "i",
						"Unesite željeni iznos (mjescni unos/razlika):",
						"0,00", OpalDialogType.SAVE, "", "");

				iznos = isNumeric(izn);

				if (iznos != 0) {
					if (optIndex == 0) { // uplata
						RSet.updateRS("insert into racuni (datum,iznos,svrha,mjesto,tip) values ('"
								+ datum
								+ "',"
								+ iznos
								+ ",'MJESEÈNA RAZLIKA','-',99)");
					} else { // isplata
						RSet.updateRS("insert into racuni (datum,iznos,svrha,mjesto,tip) values ('"
								+ datum
								+ "',"
								+ (iznos * -1)
								+ ",'MJESEÈNA RAZLIKA','-',99)");
					}
					freshValues();
				}
			}
		});

		lbl = new Label(comp, SWT.NONE);
		lbl.setText(DEF.getLabel("lblTotalRN"));
		lbl.setLayoutData("width 61px,split3,gapright 12,gapleft 2px");
		lbl.setBackground(getColor(eColor.WHITE));

		lblIznosUkRN = new Label(comp, SWT.NONE);
		lblIznosUkRN.setLayoutData("width 80px");
		lblIznosUkRN.setAlignment(SWT.RIGHT);
		lblIznosUkRN.setBackground(getColor(eColor.WHITE));

		cmdDodUkRN = new Button(comp, SWT.PUSH);
		cmdDodUkRN.setText(getLabel("etc"));
		cmdDodUkRN.setLayoutData("width 30px,wrap");
		cmdDodUkRN.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				Double iznos = 0.0;
				String izn = ALERT.input(shell, optVal, "i",
						"Unesite željeni iznos (na raèunu):", "0,00",
						OpalDialogType.SAVE, "", "");

				iznos = isNumeric(izn);
				if (iznos != 0) {
					if (optIndex == 0) { // uplata
						// RSet.updateRS("update totalsum set oper="+
						// getgUserID() +",datum='"+ datum +"',iznosRN="+
						// (ukupnoRN+iznos));
						RSet.updateRS("insert into totalsum (datum,iznosRN,iznosOST,oper) values ('"
								+ datum
								+ "',"
								+ (iznos)
								+ ",0,"
								+ getgUserID()
								+ ")");
						kovaDialog.inform("Uplata", "Raèun uveæan za: "
								+ FormatCur(String.valueOf(iznos)));
					} else { // isplata
						// RSet.updateRS("update totalsum set oper="+
						// getgUserID() +",datum='"+ datum +"',iznosRN="+
						// ((ukupnoRN-iznos)));
						RSet.updateRS("insert into totalsum (datum,iznosRN,iznosOST,oper) values ('"
								+ datum
								+ "',"
								+ ((iznos) * -1)
								+ ",0,"
								+ getgUserID() + ")");
						kovaDialog.inform("Isplata", "Raèun umanjen za: "
								+ FormatCur(String.valueOf(iznos)));
					}
					freshValues();
				}
			}
		});

		lbl = new Label(comp, SWT.NONE);
		lbl.setText(DEF.getLabel("lblTotalOST"));
		lbl.setLayoutData("width 61px,split3,gapright 10,gapleft 2px");
		lbl.setBackground(getColor(eColor.WHITE));

		lblIznosUkOST = new Label(comp, SWT.NONE);
		lblIznosUkOST.setLayoutData("width 80px");
		lblIznosUkOST.setBackground(getColor(eColor.WHITE));
		lblIznosUkOST.setAlignment(SWT.RIGHT);

		cmdDodUkOST = new Button(comp, SWT.PUSH);
		cmdDodUkOST.setText(getLabel("etc"));
		cmdDodUkOST.setLayoutData("width 30px,wrap");
		cmdDodUkOST.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				Double iznos = 0.0;
				String izn = ALERT.input(shell, optVal, "i",
						"Unesite željeni iznos (na ostalo):", "0,00",
						OpalDialogType.SAVE, "", "");

				iznos = isNumeric(izn);
				// kovaDialog.inform("Iznos", FormatCur(String.valueOf(iznos)));

				if (iznos != 0) {
					if (optIndex == 0) { // uplata
						// RSet.updateRS("update totalsum set oper="+
						// getgUserID() +",datum='"+ datum +"',iznosOST="+
						// (ukupnoOST+iznos));
						RSet.updateRS("insert into totalsum (datum,iznosRN,iznosOST,oper) values ('"
								+ datum
								+ "',0,"
								+ (iznos)
								+ ","
								+ getgUserID()
								+ ")");
						kovaDialog.inform("Uplata",
								"Ostale financije uveæane za: "
										+ FormatCur(String.valueOf(iznos)));
					} else { // isplata
						// RSet.updateRS("update totalsum set oper="+
						// getgUserID() +",datum='"+ datum +"',iznosOST="+
						// ((ukupnoOST-iznos)));
						RSet.updateRS("insert into totalsum (datum,iznosRN,iznosOST,oper) values ('"
								+ datum
								+ "',0,"
								+ (iznos * -1)
								+ ","
								+ getgUserID() + ")");
						kovaDialog.inform("Isplata",
								"Ostale financije umanjene za: "
										+ FormatCur(String.valueOf(iznos)));
					}
					freshValues();
				}
			}
		});

		sep = new Label(comp, SWT.SEPARATOR | SWT.SHADOW_IN | SWT.HORIZONTAL);
		sep.setLayoutData("width 230px,wrap");
		sep.setData("SEP");

		lbl = new Label(comp, SWT.NONE);
		lbl.setText(DEF.getLabel("lblTotal"));
		lbl.setLayoutData("width 61px,split3,gapright 27,gapleft 2px");
		lbl.setBackground(getColor(eColor.WHITE));

		lblIznosUk = new Label(comp, SWT.NONE);
		lblIznosUk.setLayoutData("width 80px,wrap");
		lblIznosUk.setBackground(getColor(eColor.WHITE));
		lblIznosUk.setAlignment(SWT.RIGHT);
		lblIznosUk.setFont(new Font(display, "", 10, SWT.BOLD));

		sep = new Label(comp, SWT.SEPARATOR | SWT.SHADOW_IN | SWT.HORIZONTAL);
		sep.setLayoutData("width 230px,wrap");
		sep.setData("SEP");

		lblOper = new Label(comp, SWT.NONE);
		lblOper.setText("Operater: " + RSet.getOsoba(getgUserID()));
		lblOper.setBackground(getColor(eColor.WHITE));

		lblOper.setLayoutData("south,width " + (shell.getBounds().width)
				+ ",top " + shell.getBounds().height + ",split 4");

		freshValues();

	}

	private void freshValues() {
		// String sql=
		// "select SUM(iznosRN)as iznosRN,SUM(iznosOST)as iznosOST,(select SUM(iznos) from racuni where Month(Datum)="+
		// month +" and YEAR(datum)="+ year +")as iznosMJ," +
		// "(iznosRN + iznosOST )as ukupRNMJ from totalSum where Month(Datum)="+
		// month +" and YEAR(datum)="+ year +"";

		String SQL = "select "
				+ "(select isnull(SUM(iznosRN),0) from totalsum where Month(Datum)=8 and YEAR(datum)=2011)as iznosRN,"
				+ "(select isnull(SUM(iznosOST),0) from totalsum where Month(Datum)=8 and YEAR(datum)=2011)as iznosOST,"
				+ "(select isnull(SUM(iznos),0) from racuni where Month(Datum)=8 and YEAR(datum)=2011)as iznosMJ";

		// String SQL = "select " +
		// "(select SUM(iznosRN) from totalsum)as iznosRN, " +
		// "(select SUM(iznosOST) from totalsum)as iznosOST, " +
		// "(select SUM(iznos) from racuni where Month(Datum)=8 and YEAR(datum)=2011)as iznosMJ "
		// +
		// "from totalSum " +
		// "where Month(Datum)="+ month +" and YEAR(datum)="+ year +" " +
		// "group by Month(Datum),YEAR(datum) ";

		rs = RSet.openRS(SQL);

		System.out.println(SQL);

		try {
			while (rs.next()) {
				ukupnoMJ = rs.getDouble("iznosMJ");
				ukupnoRN = rs.getDouble("iznosRN");
				ukupnoOST = rs.getDouble("iznosOST");
				ukupno = (ukupnoRN + ukupnoOST + ukupnoMJ);
			}

			lblIznosUkMJ.setText(FormatCur(String.valueOf(ukupnoMJ)));
			lblIznosUkOST.setText(FormatCur(String.valueOf(ukupnoOST)));
			lblIznosUkRN.setText(FormatCur(String.valueOf(ukupnoRN)));
			lblIznosUk.setText(FormatCur(String.valueOf(ukupno)));

			if (ukupnoMJ < 0) {
				lblIznosUkMJ.setForeground(getColor(eColor.RED));
			} else {
				lblIznosUkMJ.setForeground(getColor(eColor.DARK_GREEN));
			}

			if (ukupnoOST < 0) {
				lblIznosUkOST.setForeground(getColor(eColor.RED));
			} else {
				lblIznosUkOST.setForeground(getColor(eColor.DARK_GREEN));
			}

			if (ukupnoRN < 0) {
				lblIznosUkRN.setForeground(getColor(eColor.RED));
			} else {
				lblIznosUkRN.setForeground(getColor(eColor.DARK_GREEN));
			}

			if (ukupno < 0) {
				lblIznosUk.setForeground(getColor(eColor.RED));
			} else {
				lblIznosUk.setForeground(getColor(eColor.DARK_GREEN));
			}

		} catch (SQLException e) {
			kovaDialog.showException(e);
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
	}

	@Override
	public void verifyText(VerifyEvent e) {
		if (e.widget == txtIznos) {
			String string = e.text;

			char[] chars = new char[string.length()];
			string.getChars(0, chars.length, chars, 0);

			for (int i = 0; i < chars.length; i++) {
				if (!('0' <= chars[i] && chars[i] <= '9' || '.' == chars[i]
						|| '-' == chars[i] || ',' == chars[i]))
					e.doit = false;
				return;

			}
		}

	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void focusGained(FocusEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void focusLost(FocusEvent e) {
		if (e.widget instanceof Text) {
			Text t = (Text) e.widget;
			t.setBackground(display.getSystemColor(SWT.COLOR_WHITE));

			txtIznos.setText(FormatCur(txtIznos.getText()));
		}

	}

	@Override
	public void widgetDefaultSelected(SelectionEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void widgetSelected(SelectionEvent e) {
		// TODO Auto-generated method stub

	}

}
