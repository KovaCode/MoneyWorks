import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.xml.bind.ParseConversionEvent;

import kovaUtils.TextAssist.TextAssist;
import kovaUtils.TextAssist.TextAssistContentProvider;

import net.miginfocom.swt.MigLayout;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolTip;

public class editForm implements FocusListener, KeyListener, VerifyListener {
	static Group group;
	static Label lblCateg;
	static Label lblDatum;
	static DateTime dtp;
	static DateTime dtp2;
	static Label lblBrRata;
	static Label lblIznosU;
	static Text txtBrRata;
	static Text txtUkupRata;
	static String datum;
	static TextAssist txtMjesto2;
	static TextAssist txtSvrha;
	static Text txtIznos;
	Label lblTrack;
	static Label lblNet;
	static Text txtNet;
	Label sep;
	static int optIndex;
	static ToolTip tip;
	static Label lbl;
	static Label lblMess;
	static Button[] opt;
	static Button cmdSpremi;
	static Button cmdExit;

	static Listener listener;

	double ukIznos = 0;
	static double iznNeg = 0;

	static int IDbroj = 0;
	static String sTablica = "racuni";
	Image icona;

	static ResultSet rs;
	static Boolean flgAdmin = false;

	static Shell shell;
	static Composite comp;
	static Shell s;
	static Display display;
	private static String SQLquery;
	MigLayout layout = new MigLayout("");
	MigLayout layout2 = new MigLayout("");
	MigLayout layoutComp = new MigLayout("");
	private TextAssistContentProvider contentProvider2;
	private ArrayList<String> lista;
	private ArrayList<String> lista2;
	private static Color gray;

	public editForm(Shell parent) {
		shell = new Shell(parent, SWT.APPLICATION_MODAL | SWT.DIALOG_TRIM);
		display = shell.getDisplay();

		shell.setSize(360, 250);

		icona = new Image(display, "library/icons/Boue.ico");
		shell.setImage(icona);

		shell.addListener(SWT.Close, new Listener() {
			public void handleEvent(Event event) {

				DEF.gBroj = 0;
			}
		});

		shell.setLayout(layout);

		createWidgets();
		createButtons();
		// DEF.setLanguage(shell);

		if (DEF.gBroj != 0) { // ako je ureðivanje i ako je broj >0 popunjava
								// formu
			fillForm(DEF.gBroj);
		} else {
			shell.setText(DEF.getLabel("Edit"));
		}

		shell.open();

		DEF.CenterScreen(shell, display);
		shell.setBackground(display.getSystemColor(SWT.COLOR_WHITE));

		// Utils.centerDialogOnScreen(comp.getcomp());

		if (DEF.gBroj != 0) { // ako je ureðivanje i ako je broj >0 popunjava
								// formu
		// fillForm(DEF.gBroj);
		} else {
			shell.setText("Ureðivanje retka?");
		}

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}

	}

	private void createWidgets() {

		gray = display.getSystemColor(SWT.COLOR_WIDGET_LIGHT_SHADOW);

		lblCateg = new Label(shell, SWT.BORDER | SWT.CENTER);
		lblCateg.setLayoutData("span,width 340px");
		lblCateg.setBackground(display.getSystemColor(SWT.COLOR_BLACK));
		lblCateg.setText("Nema kategorije");
		lblCateg.setForeground(gray);

		comp = new Composite(shell, SWT.NONE);
		comp.setLayoutData("width 310px,height 320px");
		comp.setBackground(display.getSystemColor(SWT.COLOR_WHITE));
		comp.setLayout(layoutComp);

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
				case 0:
					// System.out.println("0");
					// iznNeg=0;
					// iznNeg=Math.abs(Double.parseDouble(txtIznos.getText()));
					// System.out.println(iznNeg);
					// // txtIznos.setText(String.valueOf(iznNeg));
					// // txtIznos.setText(DEF.FormatCur(txtIznos.getText()));
					// txtIznos.setText(DEF.FormatCur2(iznNeg));

					lblBrRata.setVisible(false);
					txtBrRata.setVisible(false);
					lblIznosU.setVisible(false);
					txtUkupRata.setVisible(false);
					txtMjesto2.setSize(265, 20);
					lblNet.setVisible(false);
					txtNet.setVisible(false);
					break;

				case 1:
					// System.out.println("1");
					// iznNeg=0;
					// iznNeg=Math.abs(Double.parseDouble(txtIznos.getText()));
					// // System.out.println(iznNeg);
					// // iznNeg = (iznNeg * - 1);
					// // txtIznos.setText(String.valueOf(iznNeg));
					// // txtIznos.setText(DEF.FormatCur(txtIznos.getText()));
					// txtIznos.setText(DEF.FormatCur2(iznNeg));

					lblBrRata.setVisible(false);
					txtBrRata.setVisible(false);
					lblIznosU.setVisible(false);
					txtUkupRata.setVisible(false);
					txtMjesto2.setSize(265, 20);
					lblNet.setVisible(false);
					txtNet.setVisible(false);
					break;

				case 2:
					// System.out.println("2");
					// iznNeg=0;
					// iznNeg=Math.abs(Double.parseDouble(txtIznos.getText()));
					// System.out.println(iznNeg);
					// iznNeg = (iznNeg * - 1);
					// // txtIznos.setText(String.valueOf(iznNeg));
					// // txtIznos.setText(DEF.FormatCur(txtIznos.getText()));
					// txtIznos.setText(DEF.FormatCur2(iznNeg));

					String sUK = String.valueOf(DEF.Round(
							DEF.getReal(txtIznos.getText())
									/ DEF.getReal(txtBrRata.getText()), 2));
					txtUkupRata.setText(DEF.FormatCur(sUK));
					lblBrRata.setVisible(true);
					txtBrRata.setVisible(true);
					lblIznosU.setVisible(true);
					txtUkupRata.setVisible(true);
					lblNet.setVisible(false);
					txtNet.setVisible(false);
					txtMjesto2.setSize(265, 20);
					break;

				case 3:
					// // System.out.println("3");
					// comp.layout(true);
					// iznNeg=0;
					// iznNeg=Math.abs(Double.parseDouble(txtIznos.getText()));
					// System.out.println(iznNeg);
					// iznNeg = (iznNeg * - 1);
					// // txtIznos.setText(String.valueOf(iznNeg));
					// // txtIznos.setText(DEF.FormatCur(txtIznos.getText()));
					// txtIznos.setText(DEF.FormatCur2(iznNeg));

					txtMjesto2.setSize(155, 20);
					txtMjesto2.setRedraw(true);
					lblNet.setLocation(225, 65);
					lblNet.setVisible(true);
					txtNet.setLocation(255, 62);
					txtNet.setVisible(true);
					lblBrRata.setVisible(false);
					txtBrRata.setVisible(false);
					lblIznosU.setVisible(false);
					txtUkupRata.setVisible(false);

					// // shell.setSize(shell.computeSize(SWT.DEFAULT,
					// SWT.DEFAULT));
					// comp.update();
					// comp.redraw();
					// comp.pack();
					// shell.update();
					// shell.redraw();
					//

					break;

				}

			}

		};

		String optText[] = { "Prihodi", "Rashodi", "Rate", "NetBanking" };
		opt = new Button[4];
		for (int i = 0; i < opt.length; i++) {
			opt[i] = new Button(comp, SWT.RADIO);
			opt[i].setText(DEF.getLabel("opt" + i));
			opt[i].setLayoutData("width 30px");
			opt[i].setBackground(display.getSystemColor(SWT.COLOR_WHITE));
			opt[i].addListener(SWT.Selection, listener);
		}

		opt[0].setLayoutData("split 4");
		opt[3].setLayoutData("wrap");

		sep = new Label(comp, SWT.SEPARATOR | SWT.SHADOW_IN | SWT.HORIZONTAL);
		sep.setLayoutData("width 330px,wrap");

		lbl = new Label(comp, SWT.NONE);
		lbl.setText(DEF.getLabel("lblDatum"));
		lbl.setLayoutData("width 50px,split2,gapright 6px,gapleft 2px");
		lbl.setBackground(display.getSystemColor(SWT.COLOR_WHITE));

		dtp = new DateTime(comp, SWT.DROP_DOWN);
		dtp.setLayoutData("width 100px,wrap");
		dtp.setDate(DEF.getToday().getYear(), DEF.getToday().getMonth(), DEF
				.getToday().getDay());
		dtp.setMonth(DEF.getToday().getMonth());
		dtp.setYear(DEF.getToday().getYear());

		dtp.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				switch (e.type) {
				case SWT.Selection:
					datum = dtp.getYear() + "-" + (dtp.getMonth()) + "-"
							+ dtp.getDay();
				}
			}

		});

		lbl = new Label(comp, SWT.NONE);
		lbl.setText(DEF.getLabel("lblMjesto"));
		lbl.setLayoutData("width 50px,split2,gapright 6px,gapleft 2px,span2");
		lbl.setBackground(display.getSystemColor(SWT.COLOR_WHITE));

		rs = RSet
				.openRS("select distinct mjesto from racuni where Mjesto is not null "); //$NON-NLS-1$

		lista = new ArrayList<String>();
		int i = 0;
		try {
			while (rs.next()) {
				lista.add(rs.getString("mjesto")); //$NON-NLS-1$
				i++;
			}
		} catch (SQLException e2) {
			e2.printStackTrace();
		}

		contentProvider2 = new TextAssistContentProvider() {
			// private final String[] EUROZONE = new String[] {
			// "Austria","Albanija","Australija","Amerika","Azerbejðan","Argentina","BIH","Belgium",
			// "Cyprus", "Estonia", "Finland", "France", "Germany", "Greece",
			// "Ireland", "Italy", "Luxembourg", "Malta", "Netherlands",
			// "Portugal", "Slovakia", "Slovenia", "Spain" };

			@Override
			public List<String> getContent(final String entry) {
				List<String> returnedList = new ArrayList<String>();

				for (String country : lista) {
					if (country.toLowerCase().startsWith(entry.toLowerCase())) {
						returnedList.add(country);
					}
				}
				return returnedList;
			}
		};

		txtMjesto2 = new TextAssist(comp, SWT.BORDER, contentProvider2); // txtMjesto2
																			// =
																			// new
																			// Text
																			// (comp,
																			// SWT.BORDER);
		txtMjesto2.setTextLimit(50);
		txtMjesto2.setLayoutData("width 265px");

		// txtMjesto2 = new Text (comp, SWT.BORDER);
		// txtMjesto2.setTextLimit(50);
		// txtMjesto2.setLayoutData ("width 265px");
		// txtMjesto2.setEditable(true);
		// txtMjesto2.addFocusListener(this);
		// txtMjesto2.addKeyListener(this);
		//

		// samo za NetBanking
		lblNet = new Label(comp, SWT.NONE);
		lblNet.setText(DEF.getLabel("lblNet"));
		lblNet.setLayoutData("width 30px");
		lblNet.setBackground(display.getSystemColor(SWT.COLOR_WHITE));
		lblNet.setLocation(190, 65);
		lblNet.setVisible(false);

		txtNet = new Text(comp, SWT.BORDER);
		txtNet.setTextLimit(10);
		txtNet.setLayoutData("width 75px,wrap");
		txtNet.addFocusListener(this);
		txtNet.addKeyListener(this);
		txtNet.setLocation(222, 62);
		txtNet.setVisible(false);

		lbl = new Label(comp, SWT.NONE);
		lbl.setText(DEF.getLabel("lblSvrha"));
		lbl.setLayoutData("width 50px,split2,gapright 7px,gapleft 2px,span");
		lbl.setBackground(display.getSystemColor(SWT.COLOR_WHITE));

		rs = RSet
				.openRS("select distinct svrha from racuni where svrha is not null "); //$NON-NLS-1$

		lista2 = new ArrayList<String>();
		try {
			while (rs.next()) {
				lista2.add(rs.getString("svrha")); //$NON-NLS-1$
			}
		} catch (SQLException e2) {
			e2.printStackTrace();
		}

		contentProvider2 = new TextAssistContentProvider() {
			// private final String[] EUROZONE = new String[] {
			// "Austria","Albanija","Australija","Amerika","Azerbejðan","Argentina","BIH","Belgium",
			// "Cyprus", "Estonia", "Finland", "France", "Germany", "Greece",
			// "Ireland", "Italy", "Luxembourg", "Malta", "Netherlands",
			// "Portugal", "Slovakia", "Slovenia", "Spain" };

			@Override
			public List<String> getContent(final String entry) {
				List<String> returnedList = new ArrayList<String>();

				for (String country : lista2) {
					if (country.toLowerCase().startsWith(entry.toLowerCase())) {
						returnedList.add(country);
					}
				}
				return returnedList;
			}
		};

		txtSvrha = new TextAssist(comp, SWT.SINGLE | SWT.BORDER,
				contentProvider2);
		txtSvrha.setTextLimit(50);
		txtSvrha.setLayoutData("width 265px,wrap");

		// txtSvrha = new Text (comp, SWT.BORDER);
		// txtSvrha.setTextLimit(50);
		// txtSvrha.setLayoutData ("width 265px,wrap");
		// txtSvrha.addFocusListener(this);
		// txtSvrha.addKeyListener(this);

		lbl = new Label(comp, SWT.NONE);
		lbl.setText(DEF.getLabel("lblIznos"));
		lbl.setLayoutData("width 50px,split6,gapright 7px,gapleft 2px,span");
		lbl.setBackground(display.getSystemColor(SWT.COLOR_WHITE));

		txtIznos = new Text(comp, SWT.BORDER);
		txtIznos.setText("0,00");
		txtIznos.setTextLimit(15);
		txtIznos.setLayoutData("width 65px,span");
		txtIznos.addFocusListener(this);
		txtIznos.addKeyListener(this);
		txtIznos.addVerifyListener(this);

		// samo za rate...
		// ---------------------------------------------------------------------------------//
		lblBrRata = new Label(comp, SWT.NONE);
		lblBrRata.setText(DEF.getLabel("lblBrRata"));
		lblBrRata.setLayoutData("width 30px");
		lblBrRata.setBackground(display.getSystemColor(SWT.COLOR_WHITE));
		lblBrRata.setVisible(false);

		txtBrRata = new Text(comp, SWT.BORDER);
		txtBrRata.setLayoutData("width 30px");
		txtBrRata.setTextLimit(3);
		txtBrRata.setText("12");
		txtBrRata.setVisible(false);
		txtBrRata.addFocusListener(this);
		txtBrRata.addKeyListener(this);
		txtBrRata.addVerifyListener(this);

		lblIznosU = new Label(comp, SWT.NONE);
		lblIznosU.setText(DEF.getLabel("lblIznosU"));
		lblIznosU.setData("L10");
		lblIznosU.setLayoutData("width 20px,split2");
		lblIznosU.setBackground(display.getSystemColor(SWT.COLOR_WHITE));
		lblIznosU.setVisible(false);

		txtUkupRata = new Text(comp, SWT.BORDER | SWT.READ_ONLY);
		txtUkupRata.setLayoutData("width 65px,span,wrap");
		txtUkupRata.setText("0,00");
		txtUkupRata.setVisible(false);
		txtUkupRata.addFocusListener(this);
		txtUkupRata.addKeyListener(this);
		txtUkupRata.addVerifyListener(this);

	}

	private void createButtons() {

		sep = new Label(comp, SWT.SEPARATOR | SWT.SHADOW_IN | SWT.HORIZONTAL);
		sep.setLayoutData("width 330px,wrap,span2");

		cmdSpremi = new Button(comp, SWT.PUSH | SWT.WRAP);
		cmdSpremi.setText(DEF.getLabel("cmdSpremi"));
		cmdSpremi.setLayoutData("width 320px,span,wrap");

		cmdSpremi.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				switch (e.type) {

				case SWT.Selection:
					lblMess.setText("Spremanje u tijeku!\n Molimo prièekajte...");
					if (checkValues() == true) {
						Spremi(DEF.gBroj);
						shell.dispose();
					} else {
						lblMess.setText("Nisu unešeni svi potrebni podaci!");
					}
				}
			}
		});

		// cmdExit = new Button(comp,SWT.PUSH|SWT.WRAP);
		// cmdExit.setText("Odustani");
		// cmdExit.setLayoutData ("width 240px,span 2,wrap");
		//
		//
		// cmdExit.addListener(SWT.Selection, new Listener() {
		// public void handleEvent(Event e) {
		// switch (e.type) {
		//
		// case SWT.Selection:
		// comp.close();
		//
		// }
		// }
		// });

		lblMess = new Label(comp, SWT.NONE);
		lblMess.setLayoutData("width 240px,growx");
		lblMess.setBackground(display.getSystemColor(SWT.COLOR_WHITE));
		lblMess.setForeground(display.getSystemColor(SWT.COLOR_RED));
		lblMess.setAlignment(SWT.CENTER);

		shell.setDefaultButton(cmdSpremi);
	}

	private static void fillForm(int broj) {
		ResultSet rs;

		// System.out.println("EDIT broja:" + broj);

		try {
			SQLquery = "select R.ID,R.Datum,R.Mjesto,R.Svrha,R.Iznos,R.Tip,C.R,C.G,C.B,C.Name,R.Iznos,R.Pintrans from racuni R LEFT OUTER JOIN "
					+ " categories C on R.kategorija=C.ID"
					+ " where R.ID = "
					+ broj;

			rs = RSet.openRS(SQLquery);
			while (rs.next()) {

				if (rs.getString("R") != null) {
					// System.out.println(rs.getString("R"));
					lblCateg.setBackground(new Color(shell.getDisplay(),
							new RGB(rs.getInt("R"), rs.getInt("G"), rs
									.getInt("B"))));
					lblCateg.setText(rs.getString("Name"));
				} else {
					lblCateg.setForeground(gray);
					lblCateg.setText(DEF.getLabel("NoCateg"));
				}

				txtSvrha.setText(rs.getString("svrha"));
				txtMjesto2.setText(rs.getString("mjesto"));

				txtNet.setText(DEF.iNull(rs.getString("PINtrans")));

				Double Iznos = rs.getDouble("iznos");
				txtIznos.setText(DEF.FormatCur2(Iznos));

				// txtIznos.setText("%1$.2f", Iznos);
				optIndex = rs.getInt("tip");

				for (int i = 0; i < 4; i++) {

					if (i == optIndex) {
						opt[i].setSelection(true);
						opt[i].setFocus();
						opt[i].notifyListeners(SWT.Selection, new Event());
						comp.changed(new Control[] { opt[i] });
						comp.layout();
						break;
					}
				}

				// System.out.println("dan" +
				// DEF.getSQLDay(rs.getDate("Datum")));
				// System.out.println("mjesec" +
				// DEF.getSQLMonth(rs.getDate("Datum")));
				// System.out.println("godina" +
				// DEF.getSQLYear(rs.getDate("Datum")));
				//

				dtp.setDay(Integer.parseInt(DEF.getSQLDay(rs.getDate("Datum"))));
				dtp.setMonth((Integer.parseInt(DEF.getSQLMonth(rs
						.getDate("Datum"))) - 1));
				dtp.setYear(Integer.parseInt(DEF.getSQLYear(rs.getDate("Datum"))));
				// dtp.setMonth();
				// dtp.setYear();

				// System.out.println(rs.getDate("datum"));

			}

			RSet.closeRS();

			txtSvrha.selectAll();

		} catch (SQLException e) {
			System.out.println("fillForm - greška RS!");
			logger.loggErr("editForm " + e.getMessage() + " ("
					+ e.getErrorCode() + ")");
			e.printStackTrace();
		}
	}

	private static void Spremi(int broj) {
		if (broj != 0) {
			RSet.updateRS("update racuni set mjesto='" + txtMjesto2.getText()
					+ "',svrha='" + txtSvrha.getText() + "',iznos='"
					+ DEF.getReal(txtIznos.getText()) + "',tip='" + optIndex
					+ "',datum='" + dtp.getYear() + "-" + (dtp.getMonth() + 1)
					+ "-" + dtp.getDay() + "' where ID= " + broj);
			System.out.println("update racuni set mjesto='"
					+ txtMjesto2.getText() + "',svrha='" + txtSvrha.getText()
					+ "',iznos='" + DEF.getReal(txtIznos.getText()) + "',tip='"
					+ optIndex + "',datum='" + dtp.getYear() + "-"
					+ (dtp.getMonth() + 1) + "-" + dtp.getDay()
					+ "' where ID= " + broj);
			logger.logg("Ažurirane vrijednosti u tablici " + sTablica + " (ID="
					+ broj + ")");
			DEF.gBroj = 0;

		} else {
			// System.out.println("ime='"+ txtIme.getText() +"',prezime='"+
			// txtPrezime.getText() +"',[user]='"+ txtUser.getText()
			// +"',pass='"+ txtPass.getText() +"',vrijediOd='"+ dtPoc
			// +"',vrijediDo='"+ dtKraj +"'");
			String col = "iznos,mjesto,svrha,tip,datum";
			String val = "'" + txtIznos.getText() + "','"
					+ txtMjesto2.getText() + "','" + txtSvrha.getText() + "','"
					+ optIndex + "','" + dtp.getYear() + "-"
					+ (dtp.getMonth() + 1) + "-" + dtp.getDay() + "'";
			RSet.addRS2(sTablica, col, val);
			logger.logg("Unos novih vrijednosti u tablicu " + sTablica + " ("
					+ txtSvrha.getText() + "," + txtIznos.getText() + ")");
			DEF.gBroj = 0;
		}
	}

	@Override
	public void focusGained(FocusEvent e) {
		Text t = (Text) e.widget;
		t.setBackground(display.getSystemColor(SWT.COLOR_INFO_BACKGROUND));
		t.selectAll();
	}

	@Override
	public void focusLost(FocusEvent e) {
		String UK = "0,00";

		Text t = (Text) e.widget;
		t.setBackground(display.getSystemColor(SWT.COLOR_WHITE));

		if (e.widget == txtIznos || e.widget == txtBrRata) {
			if (optIndex == 2) {
				UK = String.valueOf(DEF.Round(DEF.getReal(txtIznos.getText())
						/ DEF.getReal(txtBrRata.getText()), 2));

			} else if (optIndex != 0 && e.widget == txtIznos) {
				iznNeg = 0;
				iznNeg = Math.abs(DEF.getReal(txtIznos.getText()));
				iznNeg = (iznNeg * -1);

				txtIznos.setText(String.valueOf(iznNeg));

			} else if (optIndex == 0 && e.widget == txtIznos) {
				iznNeg = 0;
				iznNeg = Math.abs(DEF.getReal(txtIznos.getText()));
				iznNeg = Math.abs(iznNeg);
				txtIznos.setText(String.valueOf(iznNeg));
			}

			txtIznos.setText(DEF.FormatCur(txtIznos.getText()));
			txtUkupRata.setText(DEF.FormatCur(UK));
		}

	}

	@Override
	public void keyPressed(KeyEvent e) {
		Text t = (Text) e.widget;

		if (e.keyCode == 16777218) {// dolje
			t.traverse(SWT.TRAVERSE_TAB_NEXT);
		} else if (e.keyCode == 16777217) {// gore
			t.traverse(SWT.TRAVERSE_TAB_PREVIOUS);
		}

	}

	@Override
	public void verifyText(VerifyEvent e) {
		// iznos
		if (e.widget == txtIznos || e.widget == txtUkupRata) {
			String string = e.text;

			char[] chars = new char[string.length()];
			string.getChars(0, chars.length, chars, 0);

			for (int i = 0; i < chars.length; i++) {
				if (!('0' <= chars[i] && chars[i] <= '9' || '.' == chars[i]
						|| '-' == chars[i] || ',' == chars[i]))
					e.doit = false;
				return;

			}
		} else if (e.widget == txtBrRata) {
			String string = e.text;

			char[] chars = new char[string.length()];
			string.getChars(0, chars.length, chars, 0);

			for (int i = 0; i < chars.length; i++) {
				if (!('0' <= chars[i] && chars[i] <= '9'))
					e.doit = false;
				return;

			}
		}

	}

	private static boolean checkValues() {
		Boolean flgFields = false;
		int chk = 0;

		switch (optIndex) {
		case 2:
			if (txtBrRata.getText().length() != 0 || txtBrRata.getText() != "0")
				chk += 1;
		case 3:
			if (txtNet.getText().length() != 0)
				chk += 1;

		default:
			if (txtIznos.getText() != "0,00" || txtIznos.getText() != "-0,00"
					|| txtIznos.getText().length() != 0)
				chk += 1;
			if (txtMjesto2.getText().length() != 0
					|| txtMjesto2.getText() != "")
				chk += 1;
			if (txtSvrha.getText().length() != 0 || txtSvrha.getText() != "")
				chk += 1;

		}

		switch (optIndex) {
		case 2:
			if (chk == 4)
				flgFields = true;
		case 3:
			if (chk == 4)
				flgFields = true;

		default:
			if (chk == 3)
				flgFields = true;

		}

		return flgFields;

	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}

}
