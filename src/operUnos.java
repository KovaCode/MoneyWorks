import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

import net.miginfocom.swt.MigLayout;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Scale;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolTip;

public class operUnos extends DEF implements FocusListener, VerifyListener,
		KeyListener {
	static String sTablica = "operateri";

	static Label lblMess;
	static Label lblIme;
	static Label lblPrezime;
	static Text txtIme;
	static Text txtPrezime;

	static Label lblUser;
	static Label lblUserErr;
	static Text txtUser;
	static Label lblPass;
	static Text txtPass;
	static Text txtPass2;
	static Label lblPassErr;
	static Label lblHint;
	static Text txtHint;
	static Label sep;
	static Label lblPrava;

	static Scale slidePrava;

	static Label lbldatumOD;
	static Label lbldatumDO;

	static DateTime dtpOD;
	static DateTime dtpDO;

	static String dtPoc;
	static String dtKraj;

	static Button cmdSpremi;
	// static Button cmdOdustani;

	// static FormLayout FL = new FormLayout ();
	// static FormData data = new FormData ();

	MigLayout layout = new MigLayout("", "15[]7[]", "20[]7[]7[]7[]7[]");

	static Display display;
	static Shell shell;
	// String[] values;
	// String[] labels;

	static ToolTip tip;

	public operUnos(Shell parent) {
		dtPoc = getDateSQL();
		dtKraj = getDateSQL();

		// shell = new Shell(parent, SWT.APPLICATION_MODAL|SWT.DIALOG_TRIM );
		shell = new Shell(parent, SWT.APPLICATION_MODAL | SWT.DIALOG_TRIM);
		shell.setBounds(0, 0, 270, 470);
		display = shell.getDisplay();

		CenterScreen(shell, display);

		shell.setBackground(display.getSystemColor(SWT.COLOR_WHITE));
		shell.setLayout(layout);

		createWidgets();
		createControlButtons();
		// setLanguage(shell);
		shell.open();

		if (gBroj != 0) {
			fillForm(gBroj);
		} else {
			shell.setText(getLabel("NewOper") + "?");
		}

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}

	}

	public static void main(String[] args) throws SQLException {

	}

	private void createControlButtons() {

		sep = new Label(shell, SWT.SEPARATOR | SWT.SHADOW_IN | SWT.HORIZONTAL);
		sep.setLayoutData("width 250px,wrap,span2");

		cmdSpremi = new Button(shell, SWT.PUSH | SWT.WRAP);
		cmdSpremi.setText(getLabel("Save"));
		cmdSpremi.setLayoutData("width 240px,span 2,wrap");

		cmdSpremi.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				switch (e.type) {

				case SWT.Selection:
					lblMess.setText("Spremanje u tijeku!\n Molimo prièekajte...");
					if (checkFields() == true) {
						Spremi(gBroj);
						gBroj = 0;
						shell.dispose();
					} else {
						// lblMess.setText("Nisu unešeni svi potrebni podaci!");
					}
				}
			}
		});

		lblMess = new Label(shell, SWT.NONE);
		lblMess.setLayoutData("width 240px,span2");
		lblMess.setBackground(display.getSystemColor(SWT.COLOR_WHITE));
		lblMess.setForeground(display.getSystemColor(SWT.COLOR_RED));
		lblMess.setAlignment(SWT.CENTER);

		shell.setDefaultButton(cmdSpremi);
	}

	private void createWidgets() {

		tip = new ToolTip(shell, SWT.BALLOON | SWT.ICON_INFORMATION);

		lblIme = new Label(shell, SWT.NONE);
		lblIme.setText(getLabel("Names"));
		lblIme.setLayoutData("width 40px");
		lblIme.setBackground(display.getSystemColor(SWT.COLOR_WHITE));

		txtIme = new Text(shell, SWT.SINGLE | SWT.BORDER);
		txtIme.setLayoutData("width 150px,wrap");
		txtIme.addFocusListener(this);
		txtIme.addKeyListener(this);
		txtIme.setTextLimit(25);

		lblPrezime = new Label(shell, SWT.NONE);
		lblPrezime.setText(getLabel("Surname"));
		lblPrezime.setLayoutData("width 40px");
		lblPrezime.setBackground(display.getSystemColor(SWT.COLOR_WHITE));

		txtPrezime = new Text(shell, SWT.SINGLE | SWT.BORDER);
		txtPrezime.setLayoutData("width 150px,wrap");
		txtPrezime.addFocusListener(this);
		txtPrezime.addKeyListener(this);
		txtPrezime.setTextLimit(25);

		lblUser = new Label(shell, SWT.NONE);
		lblUser.setText(getLabel("UserName"));
		lblUser.setLayoutData("width 40px");
		lblUser.setBackground(display.getSystemColor(SWT.COLOR_WHITE));

		txtUser = new Text(shell, SWT.SINGLE | SWT.BORDER);
		txtUser.setLayoutData("width 100px,wrap");
		txtUser.addFocusListener(this);
		txtUser.addKeyListener(this);
		txtUser.setTextLimit(10);

		lblPass = new Label(shell, SWT.NONE);
		lblPass.setText(getLabel("Password"));
		lblPass.setLayoutData("width 40px");
		lblPass.setBackground(display.getSystemColor(SWT.COLOR_WHITE));

		txtPass = new Text(shell, SWT.SINGLE | SWT.BORDER | SWT.PASSWORD);
		txtPass.setLayoutData("width 70px,wrap");
		// txtPass.setEchoChar('*');
		txtPass.addFocusListener(this);
		txtPass.addKeyListener(this);
		txtPass.setTextLimit(50);

		lblPass = new Label(shell, SWT.SINGLE);
		lblPass.setText(getLabel("Password2"));
		lblPass.setLayoutData("width 40px");
		lblPass.setBackground(display.getSystemColor(SWT.COLOR_WHITE));

		txtPass2 = new Text(shell, SWT.SINGLE | SWT.BORDER | SWT.PASSWORD);
		// txtPass2.setEchoChar('*');
		txtPass2.setLayoutData("width 70px,wrap");
		// txtPass2.addVerifyListener(this);

		txtPass2.setTextLimit(50);

		txtPass2.addKeyListener(this);
		txtPass2.addFocusListener(this);
		txtPass2.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				String string = "";
				if ((e.stateMask & SWT.SHIFT) != 0) {
					if (e.keyCode == 112) {
						// logger.logg("Prikaz passworda");

						// System.out.println("pass to show: " +
						// txtPass2.getText());

						tip.setMessage("Password I : "
								+ crypt.Decrypt(txtPass.getText())
								+ "\nPassword II: "
								+ crypt.Decrypt(txtPass2.getText()));
						tip.setLocation(
								(txtPass2.getBounds().x + shell.getBounds().x + 35),
								(txtPass2.getBounds().y + shell.getBounds().y + 35));
						tip.setVisible(true);
						e.doit = false;
					}
				}
			}
		});

		lblPass = new Label(shell, SWT.SINGLE);
		lblPass.setText(getLabel("Rights"));
		lblPass.setLayoutData("width 40px");
		lblPass.setBackground(display.getSystemColor(SWT.COLOR_WHITE));

		slidePrava = new Scale(shell, SWT.HORIZONTAL);
		slidePrava.setBackground(display.getSystemColor(SWT.COLOR_WHITE));
		slidePrava.setMinimum(0);
		slidePrava.setMaximum(2);
		slidePrava.setIncrement(1);
		slidePrava.setPageIncrement(1);
		slidePrava.setLayoutData("width 100,height 15px,wrap,span2");

		slidePrava.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				String prava = "";

				switch (slidePrava.getSelection()) {
				case 0:
					prava = getLabel("RIGHTS0");
					break;
				case 1:
					prava = getLabel("RIGHTS1");
					break;
				case 2:
					prava = getLabel("RIGHTS2");
					break;
				}
				prava = getLabel("UserRights") + ": " + prava + " ("
						+ slidePrava.getSelection() + ")";
				lblPrava.setText(prava);
			}
		});

		lblPrava = new Label(shell, SWT.SINGLE | SWT.CENTER);
		lblPrava.setText("");
		lblPrava.setForeground(display.getSystemColor(SWT.COLOR_BLUE));
		lblPrava.setLayoutData("width 220px,span2,wrap");
		lblPrava.setBackground(display.getSystemColor(SWT.COLOR_WHITE));

		if (login.getflgOper() == true) {
			slidePrava.setSelection(2);
			slidePrava.setEnabled(false);
			lblPrava.setText(getLabel("UserRights") + ": "
					+ getLabel("RIGHTS2"));
		}

		sep = new Label(shell, SWT.SEPARATOR | SWT.SHADOW_IN | SWT.HORIZONTAL);
		sep.setLayoutData("width 250px,wrap,span2 ");

		lbldatumOD = new Label(shell, SWT.NONE);
		lbldatumOD.setText(getLabel("DateBeg"));
		lbldatumOD.setLayoutData("width 40px");
		lbldatumOD.setBackground(display.getSystemColor(SWT.COLOR_WHITE));

		dtpOD = new DateTime(shell, SWT.BORDER | SWT.DATE | SWT.DROP_DOWN);
		dtpOD.setLayoutData("width 100px, wrap");
		dtpOD.setDate(getToday().getYear(), getToday().getMonth(), DEF
				.getToday().getDay());

		dtpOD.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				switch (e.type) {
				case SWT.Selection:
					lblMess.setText("Poèetni dan odabran: " + dtpOD.getDay()
							+ "." + dtpOD.getMonth() + "." + dtpOD.getYear());
					dtPoc = dtpDO.getYear() + "-" + dtpDO.getMonth() + "-"
							+ dtpDO.getDay();
					break;
				}
			}

		});

		lbldatumOD = new Label(shell, SWT.NONE);
		lbldatumOD.setText(getLabel("DateEnd"));
		lbldatumOD.setLayoutData("width 40px");
		lbldatumOD.setBackground(display.getSystemColor(SWT.COLOR_WHITE));

		dtpDO = new DateTime(shell, SWT.BORDER | SWT.DATE | SWT.DROP_DOWN);
		dtpDO.setLayoutData("width 100px, wrap");
		dtpDO.setDate(getToday().getYear(), getToday().getMonth(), DEF
				.getToday().getDay());

		dtpDO.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				switch (e.type) {
				case SWT.Selection:
					dtKraj = dtpDO.getYear() + "-" + dtpDO.getMonth() + "-"
							+ dtpDO.getDay();

					lblMess.setText("Krajnji dan odabran: " + dtpDO.getDay()
							+ "." + dtpDO.getMonth() + "." + dtpDO.getYear());
					break;
				}
			}

		});

		sep = new Label(shell, SWT.SEPARATOR | SWT.SHADOW_IN | SWT.HORIZONTAL);
		sep.setLayoutData("width 250px,wrap,span2");

		lblHint = new Label(shell, SWT.SINGLE);
		lblHint.setText(getLabel("Hint"));
		lblHint.setLayoutData("width 80px,height 50px");
		lblHint.setBackground(display.getSystemColor(SWT.COLOR_WHITE));

		txtHint = new Text(shell, SWT.MULTI | SWT.BORDER | SWT.WRAP
				| SWT.V_SCROLL);
		txtHint.setLayoutData("width 160px,height 60px,wrap");
		txtHint.addKeyListener(this);
		txtHint.setTextLimit(100);

	}

	private static void Spremi(int broj) {

		if (broj != 0) {

			// System.out.println();
			// System.out.println("update operateri set ime='" +
			// txtIme.getText()
			// + "',prezime='" + txtPrezime.getText() + "',[user]='"
			// + txtUser.getText() + "',pass='" + txtPass.getText()
			// + "',vrijediOd='" + RSet.getSqlDate() + "',vrijediDo='"
			// + RSet.getSqlDate() + "',prava="
			// + slidePrava.getSelection() + " where ID= " + broj + "");

			RSet.updateRS("update operateri set ime='" + txtIme.getText()
					+ "',prezime='" + txtPrezime.getText() + "',korisnik='"
					+ txtUser.getText() + "',lozinka='"
					+ crypt.Encrypt(crypt.Decrypt((txtPass.getText())))
					+ "',vrijediOd='" + dtPoc + "',vrijediDo='" + dtKraj
					+ "',prava=" + slidePrava.getSelection() + ",hint='"
					+ iNull(txtHint.getText()) + "' where ID= " + broj);
			logger.logg("Ažurirane vrijednosti u tablici " + sTablica + " (ID="
					+ broj + ")");
			gBroj = 0;

		} else {
			// System.out.println("ime='" + txtIme.getText() + "',prezime='"
			// + txtPrezime.getText() + "',[user]='" + txtUser.getText()
			// + "',pass='" + txtPass.getText() + "',vrijediOd='" + dtPoc
			// + "',vrijediDo='" + dtKraj + "'");
			String col = "ime,prezime,korisnik,lozinka,vrijediOD,vrijediDO,prava,hint";
			String val = "'" + txtIme.getText() + "','" + txtPrezime.getText()
					+ "','" + txtUser.getText() + "','"
					+ crypt.Encrypt(txtPass.getText()) + "','" + dtPoc + "','"
					+ dtKraj + "'," + slidePrava.getSelection() + ",'"
					+ iNull(txtHint.getText()) + "'";
			RSet.addRS2(sTablica, col, val);
			logger.logg("Unos novih vrijednosti u tablicu " + sTablica + " ("
					+ txtIme.getText() + "," + txtPrezime.getText() + ")");
			gBroj = 0;
		}
	}

	private static void fillForm(int broj) {
		ResultSet rs;

		try {
			rs = RSet.openRS("select * from " + sTablica + " where ID=" + broj);

			while (rs.next()) {

				shell.setText(getLabel("Edit") + ": " + rs.getString("Ime")
						+ " " + rs.getString("Prezime"));

				txtIme.setText(rs.getString("Ime"));
				txtPrezime.setText(rs.getString("Prezime"));
				txtUser.setText(rs.getString("Korisnik"));
				txtPass.setText(rs.getString("Lozinka"));
				txtPass2.setText(rs.getString("Lozinka"));
				txtHint.setText(iNull(rs.getString("hint")));

				// System.out.println("Slider: " + rs.getInt("prava"));
				switch (rs.getInt("prava")) {
				case 0:
					lblPrava.setText(getLabel("UserRights") + ": "
							+ getLabel("RIGHTS0"));
					slidePrava.setSelection(0);
					break;

				case 1:
					lblPrava.setText(getLabel("UserRights") + ": "
							+ getLabel("RIGHTS1"));
					slidePrava.setSelection(1);
					break;

				case 2:
					lblPrava.setText(getLabel("UserRights") + ": "
							+ getLabel("RIGHTS2"));
					slidePrava.setSelection(2);
					break;
				}

				// String date = "2000-11-01";
				// SqlDate = java.sql.Date.valueOf(date);

				// System.out.println("Datum: " + rs.getString("vrijediOD"));
			}

			RSet.closeRS();

			txtIme.selectAll();

		} catch (SQLException e) {
			System.out.println("fillForm - greška RS!");
			e.printStackTrace();
			logger.loggErr("operUnos " + e.getMessage());
		}
	}

	private static boolean checkPass() {
		return txtPass.getText().equals(txtPass2.getText());
	}

	// ***************************************************************************************************//
	// verifikacija teksta PRIMJER (ne koristi se)
	@Override
	public void verifyText(VerifyEvent event) {
		// Assume we don't allow it
		event.doit = false;
		// Get the character typed
		char myChar = event.character;
		String text = ((Text) event.widget).getText();
		// Allow '-' if first character
		if (myChar == '-' && text.length() == 0)
			event.doit = true;
		// Allow 0-9
		if (Character.isDigit(myChar))
			event.doit = true;
		// Allow backspace
		if (myChar == '\b')
			event.doit = true;
	}

	// ***************************************************************************************************//

	@Override
	public void focusGained(FocusEvent e) {
		// TODO Auto-generated method stub
		Text t = (Text) e.widget;
		t.setBackground(display.getSystemColor(SWT.COLOR_INFO_BACKGROUND));
		t.selectAll();
	}

	@Override
	public void focusLost(FocusEvent e) {
		Text t = (Text) e.widget;

		t.setBackground(display.getSystemColor(SWT.COLOR_WHITE));

		if (t.getText() == "") {
			// System.out.println(e.toString());
			lblMess.setText("Vrijednost mora biti unešena!");

		} else {
			lblMess.setText("");
		}

		if (e.getSource() == txtPass) {

		} else if (e.getSource() == txtPass2) {
			if (checkPass() == false) {
				lblMess.setText("Passwordi nisu jednaki!");
			} else {
				lblMess.setText("");
			}

		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		String string = "";
		Text t = (Text) e.widget;

		// if ((e.stateMask & SWT.SHIFT) != 0) string += "SHIFT - keyCode = " +
		// e.keyCode;

		System.out.println(SWT.DOWN);

		if (e.keyCode == 16777218) {// dolje
			t.traverse(SWT.TRAVERSE_TAB_NEXT);
		} else if (e.keyCode == 16777217) {// gore
			t.traverse(SWT.TRAVERSE_TAB_PREVIOUS);
		}

	}

	@Override
	public void keyReleased(KeyEvent e) {

	};

	private boolean checkFields() {
		int sum = 0;

		// provjera unosa texta

		if (txtIme.getText() == "") {
			txtIme.setFocus();
		} else {
			sum += 1;
		}

		if (txtPrezime.getText() == "") {
			txtPrezime.setFocus();
		} else {
			sum += 1;
		}

		if (txtUser.getText() == "") {
			txtUser.setFocus();
		} else {
			sum += 1;
		}

		if (txtPass.getText() == "") {
			txtPass.setFocus();
		} else {
			sum += 1;
		}

		if (txtPass2.getText() == "") {
			txtPass2.setFocus();
		} else {
			sum += 1;
		}

		if (checkPass() == false) {
			txtPass2.setFocus();
		} else {
			sum += 5;
		}

		// Provjera datuma (kao funkcija datuma)
		Date dateOD = new Date(dtpOD.getYear(), dtpOD.getMonth(),
				dtpOD.getDay());
		Date dateDO = new Date(dtpDO.getYear(), dtpDO.getMonth(),
				dtpDO.getDay());

		if (dateOD.compareTo(dateDO) < 0) {
			sum += 1;
		} else if (dateOD.compareTo(dateDO) > 0) {
			lblMess.setText("Poèetni datum veæi od krajnjeg...");
		} else {
			sum += 1;
		}

		// System.out.println(sum);

		return (sum == 11);

	}

}