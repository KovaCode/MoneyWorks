import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOError;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Scanner;

import kovaUtils.OpalDialog.kovaDialog;
import kovaUtils.OpalDialog.kovaDialog.OpalDialogType;

import net.miginfocom.swt.MigLayout;

import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.*;

public class login extends DEF implements FocusListener, KeyListener {
	private static Integer gUser;
	private static Integer gPrava;
	private static Integer jezik;
	Integer putaX = 0;

	public static String gDate;
	static String gHint;

	static Display display;
	static Shell shell;
	Shell s;

	static Label lblMess;
	static Label lblUser;
	static Label lblPass;
	static Label lblDatum;
	static Label sep;

	static Text txtUser;
	static Text txtPass;

	static DateTime dtp;

	static Button cmdEnter;
	static Button cmdExit;
	static Button chkZapamti;
	static Button chkTest;

	static Button chkGost;
	static Button cmdAdmin;

	protected boolean flgTest = false;
	protected Boolean flgRemeber = false;

	MigLayout layout = new MigLayout("", "15[]7[]", "60[]7[]7[]7[]7[]");
	MigLayout layoutC1 = new MigLayout("", "", "5[]10[]10[]10");
	MigLayout layoutC2 = new MigLayout();
	private Composite comp;
	private Composite comp2;
	static ToolTip tip;
	private static Boolean flgProfile = false;
	private static boolean flgOper;

	public static Locale loc = null;

	public static void main(String[] args) {

		setSystem();

		if (getgLoginForm() == 1) {
			setflgOper(true);
			setgPrava(2);
			new mainFrame();
		} else {
			new login();
		}

	}

	private static void setSystem() {
		display = new Display();
		shell = new Shell(display);
		shell = new Shell(shell);

		final Image icona = new Image(display, "library/icons/boue.ico");
		shell.setImage(icona);

		try {
		 Integer i='a';
		}catch (Exception e) {
			kovaDialog.showException(e);
		}
		
		
		display.setAppName("moneyWorks");
		// System.out.println(getJarFolder2());
		// kovaDialog.inform("JARfolder: ", getJarFolder2());

		File file = new File("profiles/config.ini");
		boolean exists = file.exists();

		if (exists == true) {
			try {
				jezik = Integer.parseInt(readINIsingle("Jezik"));

				System.out.println("JEZIK" + jezik);

				switch (jezik) {
				case 0: // HRV
					Locale hr = new Locale("hr", "HR");
					locale = hr;
					Locale.setDefault(hr);
					break;

				case 1: // ENG
					// System.out.println("1");
					Locale en = new Locale("en", "EN"); //
					locale = en;
					Locale.setDefault(en);
					break;

				case 2: // DEU
					System.out.println("NjemaÄki");
					Locale de = new Locale("de", "DE"); //
					locale = de;
					Locale.setDefault(de);
					break;

				case 3:
					// System.out.println("3");
					Locale fr = new Locale("fr", "FR"); //
					locale = fr;
					Locale.setDefault(fr);
					break;

				case 4:
					// System.out.println("4");
					Locale es = new Locale("es", "ES"); //
					locale = es;
					Locale.setDefault(es);
					break;

				case 5:
					// System.out.println("5");
					Locale it = new Locale("it", "IT"); //
					locale = it;
					Locale.setDefault(it);
					break;

				default:
					System.out.println("default");
					Locale.setDefault(Locale.getDefault());
					break;

				}
			} catch (IOError err) {
				Locale.setDefault(Locale.getDefault());
				kovaDialog.showException(err);
				System.out.println(err);

			}
		} else {
			Locale hr = new Locale("hr", "HR");
			locale = hr;
			Locale.setDefault(hr);
		}

		RSC = ResourceBundle.getBundle("ressources/lang", locale);
		checkProfile:

		if (checkProfiles() == false) {
			setFlgProfile(false);

			// int odg=ALERT.open(shell, "!", "Database profili...",
			// "Za aplikaciju nije kreiran niti jedan bazni profil..." +
			// "\nZa nastavak rada treba postojati barem jedna konekcija na bazu!\nï¿½elite li sada kreirati profil?",
			// "Kreiraj profil|Ne", 0);

			int odg = ALERT.show(getLabel("Mess1"), "!", getLabel("Mess1txt"),
					OpalDialogType.OK_CANCEL, "", "");

			if (odg == 0) {

				new sysDatabase(shell);
				initialSQL();
				break checkProfile;

			}

		}

		if (readINI() == true) {
			System.out.println(DEF.getgLoggErrPath());

			if (isFlgProfileRead() == false) {

				int choice = Dialog.choice(getLabel("Mess2") + getgFile()
						+ "\"?", getLabel("Mess2txt"), 0, new ChoiceItem(
						getLabel("ch1"), getLabel("ch1txt")), new ChoiceItem(
						getLabel("ch2"), getLabel("ch2txt") + getgFile()
								+ getLabel("ch2txt2")), new ChoiceItem(
						getLabel("ch3"), getLabel("ch3txt")));

				if (choice == 0) {
					new sysDatabase(shell);
				} else if (choice == 1) {

					FileDialog dialog = new FileDialog(shell, SWT.SAVE);
					dialog.setFilterNames(new String[] {
							"Initial file (*.ini)", "All Files (*.*)" });
					dialog.setFilterExtensions(new String[] { "*.ini", "*.*" });
					dialog.setFilterPath("c:\\");

					dialog.setFileName(getgFile());

					String filePath = dialog.open();
					String fileName = dialog.getFileName();

					try {

						int odg = ALERT.show(getLabel("mess4"), "?",
								getLabel("mess4txt") + fileName + "?",
								OpalDialogType.YES_NO, "", "");

						if (odg == 0) {
							copy(filePath, "profiles/" + getgFile());
							ALERT.show(getLabel("mess5"), "?",
									getLabel("mess5txt"),
									OpalDialogType.CONTINUE, "", "");
							readINI();

							RSet.setSQLip(getgServer());
							RSet.setgTipBaze(getgBaza());
							RSet.setSQLDBase(getgImeBaze());

							RSet.initRS();
							// checkUser();
						} else {
							ALERT.show(getLabel("mess6"), "-",
									getLabel("mess6txt"),
									OpalDialogType.CONTINUE, "", "");
						}

					} catch (IOException e) {
						System.err.println(e.getMessage());
						kovaDialog.showException(e);
					}

				} else if (choice == 2) {
					System.exit(0);
				}

			} else {

				setFlgProfile(true);
			}
			// System.out.println("PROFIL:" + getgProfil());
			// System.out.println("Aplikacija:" + getgAplikacija());
			// System.out.println("Server:" + getgServer());
			// System.out.println("Baza:" + getgBaza());
			// System.out.println("Ime baze:" + getgImeBaze());
			// System.out.println("Index baze:" + getgIndexBaze());
			// System.out.println("jezik:" + getgJezik());
			// System.out.println("LoggPath:" + getgLoggPath());
			// System.out.println("LoginForm:" + getgLoginForm());
			//
			// System.out.println("Base user:" + getgBaseUser());
			// System.out.println("Base pass:" + getgBasePass());

			RSet.setSQLip(getgServer());
			RSet.setgTipBaze(getgBaza());
			RSet.setSQLDBase(getgImeBaze());

			System.out.println("DBASE: " + RSet.getSQLDBase());
			RSet.initRS();
			checkUser();

			// System.out.println("RSET.baza:" + RSet.getSQLDBase());
			// if (getgLoginForm()==1){
			// checkUser();
			// }else{
			// checkUser();
			// }
			// logger.logg("Provjera sustava uspjeï¿½no zavrï¿½ena...");
		} else {
			// ALERT.open(shell, "!", "Greï¿½ka u aplikaciji!",
			// "Aplikacija neï¿½e zapoï¿½eti!\n Konfiguracijska datoteka (\"config.ini\") ne postoji!",
			// "Nastavak", 0);
			ALERT.show(getLabel("mess7"), "!", getLabel("mess7txt"),
					OpalDialogType.CONTINUE, "", "");
			System.exit(0);
		}

		// //napraviti da se ï¿½ita iz config.ini-a
		// if (fileExists("logg/logg.xml")==false){
		// logger.logg(getLabel("logg1"));
		// }

	}

	public login() {
		System.out.println("2");
		shell.setText("moneyWorks - Login");
		shell.setBackground(display.getSystemColor(SWT.COLOR_WHITE));
		shell.setLayout(layout);
		shell.setSize(260, 260);

		CenterScreen(shell, display);

		// File file = new
		// File(login.class.getProtectionDomain().getCodeSource().getLocation().getPath());
		// kovaDialog.inform("Putanja:", getJarFolder());

		createWidgets();
		createButtons();

		// setLanguage(shell);

		shell.open();

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		// display.dispose ();
	}

	private void createWidgets() {
		tip = new ToolTip(shell, SWT.ICON_INFORMATION);
		final Image image = new Image(display, "library/icons/boue48.ico");
		// image.getImageData().scanlinePad = 40;

		shell.addPaintListener(new PaintListener() {
			public void paintControl(PaintEvent event) {
				event.gc.drawImage(image, 100, 5);

			}
		});

		sep = new Label(shell, SWT.SEPARATOR | SWT.SHADOW_IN | SWT.HORIZONTAL);
		sep.setData("sep");
		sep.setLayoutData("width 220px,wrap,span2");

		comp = new Composite(shell, SWT.NONE);
		comp.setBackground(display.getSystemColor(SWT.COLOR_WHITE));
		comp.setLayout(layoutC1);
		comp.setData("COMP");
		comp.setLayoutData("split2");

		comp2 = new Composite(shell, SWT.NONE);
		comp2.setBackground(display.getSystemColor(SWT.COLOR_WHITE));
		comp2.setLayout(layoutC2);
		comp2.setData("COMP2");
		comp2.setLayoutData("wrap");

		lblUser = new Label(comp, SWT.NONE);
		lblUser.setText(getLabel("lblUser"));
		lblUser.setLayoutData("width 50px,wrap");
		lblUser.setBackground(display.getSystemColor(SWT.COLOR_WHITE));

		txtUser = new Text(comp2, SWT.SINGLE | SWT.BORDER);
		txtUser.setTextLimit(10);
		txtUser.setLayoutData("width 120px,wrap,span 2,wrap");
		txtUser.addFocusListener(this);
		txtUser.addKeyListener(this);

		txtUser.addVerifyListener(new VerifyListener() {
			public void verifyText(VerifyEvent e) {
				e.text = e.text.toUpperCase();
			}
		});

		lblPass = new Label(comp, SWT.NONE);
		lblPass.setText(getLabel("lblPass"));
		lblPass.setLayoutData("width 50px,wrap");
		lblPass.setBackground(display.getSystemColor(SWT.COLOR_WHITE));

		txtPass = new Text(comp2, SWT.SINGLE | SWT.BORDER | SWT.PASSWORD);
		txtPass.setLayoutData("width 120px,wrap,span 2");
		;
		txtPass.setTextLimit(10);
		// txtPass.setEchoChar('*');
		txtPass.addFocusListener(this);
		txtPass.addKeyListener(this);

		txtPass.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if ((e.stateMask & SWT.SHIFT) != 0) {
					if (e.keyCode == 112) {
						// logger.logg("Prikaz passworda SHIFT+P");
						tip.setMessage("Password: " + txtPass.getText());
						tip.setLocation(
								(txtPass.getBounds().x + shell.getBounds().x + 120),
								(txtPass.getBounds().y + shell.getBounds().y + 100));
						tip.setVisible(true);
						e.doit = false;
					}
				}
			}
		});

		txtPass.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if ((e.stateMask & SWT.SHIFT) != 0) {
					if (e.keyCode == 97) {

						new sysDatabase(shell);
						// setLanguage(shell);
						e.doit = false;
					}
				}
			}
		});

		lblDatum = new Label(comp, SWT.NONE);
		lblDatum.setText(getLabel("lblDatum"));
		lblDatum.setLayoutData("width 50px");
		lblDatum.setBackground(display.getSystemColor(SWT.COLOR_WHITE));

		dtp = new DateTime(comp2, SWT.BORDER | SWT.DATE | SWT.DROP_DOWN);
		dtp.setData("DTP");
		dtp.setLayoutData("width 100px");
		dtp.setDay(getToday().getDay());
		dtp.setMonth(getToday().getMonth());
		dtp.setYear(getToday().getYear());
		dtp.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				switch (e.type) {
				case SWT.Selection:
					// lblMess.setText("Poï¿½etni dan odabran: "+
					// dtp.getDay()+"."+ dtp.getMonth()+"."+ dtpOD.getYear());
					gDate = dtp.getDay() + "." + dtp.getMonth() + "."
							+ dtp.getYear(); //
					break;
				}
			}

		});

		final Image edit = new Image(display, "library/icons/edit.ico");
		cmdAdmin = new Button(comp2, SWT.PUSH);
		cmdAdmin.setLayoutData("width 16px,height 16px,wrap");
		cmdAdmin.setImage(edit);

		cmdAdmin.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				switch (e.type) {
				case SWT.Selection:
					new sysDatabase(shell);
					break;
				}
			}

		});

		// chkZapamti = new Button(shell,SWT.CHECK);
		// chkZapamti.setText("Zapamti me..");
		// chkZapamti.setBackground(display.getSystemColor(SWT.COLOR_WHITE));
		// chkZapamti.setLayoutData ("width 40px,gapleft 50px,span3,split4");
		// chkZapamti.addListener (SWT.Selection, new Listener() {
		// public void handleEvent (Event e) {
		// if (chkZapamti.getSelection()) {
		// flgRemeber=true;
		// } else {
		// flgRemeber=false;
		// }
		// }
		// });
		//
		// chkTest = new Button(shell,SWT.CHECK);
		// chkTest.setText("Test");
		// chkTest.setBackground(display.getSystemColor(SWT.COLOR_WHITE));
		// chkTest.setLayoutData ("width 105px,wrap");
		// chkTest.addListener (SWT.Selection, new Listener() {
		// public void handleEvent (Event e) {
		// if (chkTest.getSelection()) {
		// flgTest=true;
		// } else {
		// flgTest=false;
		// }
		// }
		// });

	}

	private void createButtons() {
		sep = new Label(shell, SWT.SEPARATOR | SWT.SHADOW_IN | SWT.HORIZONTAL);
		sep.setData("sep");
		sep.setLayoutData("width 220px,wrap,span2");

		cmdEnter = new Button(shell, SWT.PUSH);
		cmdEnter.setText(getLabel("cmdEnter"));
		cmdEnter.setLayoutData("width 105px,span2,split2");

		cmdEnter.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				switch (e.type) {
				case SWT.Selection:

					putaX = putaX + 1;

					if (putaX >= 3) {
						if (txtUser.getText().length() != 0) {

							ResultSet rs = RSet
									.openRS("select hint from operateri where korisnik='"
											+ txtUser.getText() + "'"); //

							try {

								while (rs.next()) {
									gHint = rs.getString("hint");
								}
								RSet.closeRS();

								tip.setMessage(iNull(gHint));
								tip.setLocation(
										(txtPass.getBounds().x
												+ shell.getBounds().x + 110),
										(txtPass.getBounds().y
												+ shell.getBounds().y + 110));
								tip.setVisible(true);

								RSet.closeAll();

							} catch (SQLException e1) {
								e1.printStackTrace();
								logger.loggErr("login " + e1.getMessage());
								kovaDialog.showException(e1);
							}
						}

					}

					if (checkUser(txtUser.getText(), txtPass.getText()) == true) {

						// logger.logg("Ulaz u aplikaciju!");
						display.dispose();
						new mainFrame();

					}

					break;
				}
			}

		});

		cmdExit = new Button(shell, SWT.PUSH);
		cmdExit.setText(getLabel("cmdExit"));
		cmdExit.setLayoutData("width 105px,span 2,wrap");
		cmdExit.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				System.exit(0);
			}

		});

		shell.setDefaultButton(cmdEnter);

		lblMess = new Label(shell, SWT.NONE);
		lblMess.setData("L0");
		lblMess.setForeground(display.getSystemColor(SWT.COLOR_RED));
		lblMess.setAlignment(SWT.CENTER);
		lblMess.setBackground(display.getSystemColor(SWT.COLOR_WHITE));
		lblMess.setLayoutData("width 200px,span");
	}

	private static boolean checkUser() {
		ResultSet rs;
		Boolean flgLogin = false;

		// SYSTEM appPref
		rs = RSet
				.openRS("select distinct id,prava,korisnik,lozinka from operateri where prava=2");

		// System.out.println("ukupno;" + RSet.countRS(rs));

		if (RSet.countRS(rs) > 0) {
			setflgOper(true);

			try {
				while (rs.next()) {
					if (getgLoginForm() == 1) {
						gUser = rs.getInt("id");
						gPrava = rs.getInt("prava");
						setgPrava(gPrava);
						System.out.println(getgPrava());

						// System.out.println("JEZIK login: " +
						// rs.getInt("jezik"));
						// setgJezik(rs.getInt("jezik"));
						setgUserID(gUser);

						flgLogin = true;
						logger.logg(getLabel("logg2"));
					} else {
						flgLogin = false;
						gUser = rs.getInt("id");
						gPrava = rs.getInt("prava");
						setgPrava(gPrava);

						if (gPrava < 2) {
							setflgOper(false);
						}

						flgLogin = true;
						// logger.logg(getLabel("logg3"));
					}
				}
			} catch (SQLException e) {
				e.printStackTrace();
				logger.logg(e.getMessage());
				kovaDialog.showException(e);
			}
		} else {
			// int odg=ALERT.open(shell, "!", "Operateri...",
			// "Za aplikaciju nije kreiran niti jedan operater...\nZa nastavak rada treba postojati unijeti operatera s administratorskim pravima!\nï¿½elite li sada kreirati Administratora?",
			// "Kreiraj administratora|Ne", 0);

			int odg = ALERT.show(getLabel("mess8"), "!", getLabel("mess8txt"),
					OpalDialogType.YES_NO, "", "");

			setflgOper(false);
			if (odg == 0)
				new operUnos(shell);

		}

		RSet.closeRS();

		return flgLogin;
	}

	private static boolean checkUser(String user, String pass) {
		ResultSet rs;
		Boolean flgUserChk = false;

		if (user.length() == 0) {
			lblMess.setText(getLabel("mess9"));
			txtUser.setFocus();
			return flgUserChk;
		} else if (pass.length() == 0) {
			lblMess.setText(getLabel("mess10"));
			txtPass.setFocus();
			return flgUserChk;
		} else {
			lblMess.setText("");
		}

		try {
			// System.out.println(pass);
			// System.out.println(AppCrypt.crypt.Encrypt(pass));

			rs = RSet.openRS("SELECT id,prava FROM operateri WHERE korisnik='"
					+ user + "' AND lozinka='" + crypt.Encrypt(pass) + "'");

			// System.out.println("Ukupno: " + RSet.countRS(rs));

			if (RSet.countRS(rs) > 0) {

				while (rs.next()) {
					gUser = rs.getInt("ID");
					gDate = dtp.getDay() + "." + dtp.getMonth() + "."
							+ dtp.getYear();
					gPrava = rs.getInt("prava");
					setgPrava(gPrava);
					// System.out.println("PRAVA:" + gPrava);
					setgUserID(gUser);
					// setgJezik(rs.getInt("jezik"));
				}
				flgUserChk = true;
				RSet.closeRS();

				RSet.updateRS("update operateri set logged=1 where id="
						+ getgUserID());

				RSet.closeRS();
			} else {

				logger.logg("Osoba \"" + user
						+ "\" neuspješno pokušla uæi u sustav!");
				lblMess.setText(getLabel("mess11"));
				txtUser.setFocus();
				flgUserChk = false;
				RSet.closeRS();

			}
		} catch (SQLException e) {
			while (e != null) {
				System.out.println("Login");
				System.out.println("State  : " + e.getSQLState());
				System.out.println("Message: " + e.getMessage());
				System.out.println("Error  : " + e.getErrorCode());

				e = e.getNextException();
				// logger.loggErr("login " + e.getMessage());
			}
			// e.printStackTrace();
		}
		return flgUserChk;

	}

	@Override
	public void focusGained(FocusEvent e) {
		Text t = (Text) e.widget;
		t.setBackground(display.getSystemColor(SWT.COLOR_INFO_BACKGROUND));
		t.selectAll();

	}

	@Override
	public void focusLost(FocusEvent e) {
		Text t = (Text) e.widget;
		t.setBackground(display.getSystemColor(SWT.COLOR_WHITE));

	}

	public void keyPressed(KeyEvent e) {
		Text t = (Text) e.widget;

		if (e.keyCode == 16777218) {// dolje
			t.traverse(SWT.TRAVERSE_TAB_NEXT);
		} else if (e.keyCode == 16777217) {// gore
			t.traverse(SWT.TRAVERSE_TAB_PREVIOUS);
		}

	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}

	public static Integer getgPrava() {
		return gPrava;
	}

	public static void setgPrava(Integer gPrava) {
		login.gPrava = gPrava;
	}

	public static void setJezik(Integer gJezik) {
		login.jezik = gJezik;
	}

	public static Integer getJezik() {
		return jezik;
	}

	public static void ININTIAL() {

	}

	public static void setFlgProfile(Boolean flgProfile) {
		login.flgProfile = flgProfile;
	}

	public static Boolean getFlgProfile() {
		return flgProfile;
	}

	public static void setflgOper(boolean flgOper) {
		login.flgOper = flgOper;
	}

	public static boolean getflgOper() {
		return flgOper;
	}

	public static String readINIsingle(String key) {
		String initialFile;
		String vrijed = "";
		BufferedReader reader = null;

		// if (fileExists("dist/moneyWorks.ini") == true) {
		try {
			FileInputStream fstream = new FileInputStream("moneyWorks.ini");
			// Get the object of DataInputStream
			DataInputStream ini = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(ini));

			initialFile = br.readLine();
			ini.close();
			br.close();

			fstream = new FileInputStream("profiles/" + initialFile);
			ini = new DataInputStream(fstream);

			try {
				reader = new BufferedReader(new InputStreamReader(ini));
				for (String next, line = reader.readLine(); line != null; line = next) {
					next = reader.readLine();

					if (line.substring(0, key.length()).equals(key)) {
						vrijed = line.substring((key.length() + 1));

					}
				}
			} finally {
				if (reader != null)
					try {
						reader.close();
					} catch (IOException logOrIgnore) {
					}
			}

		} catch (Exception e) {// Catch exception if any
			System.err.println("Error: " + e.getMessage());
			logger.loggErr("DEF " + e.getMessage());
			kovaDialog.showException(e);
		}
		// }else{
		// ALERT.open(Display.getCurrent().getActiveShell().getShell(), "!",
		// "Greï¿½ka!", "Datoteka Config.ini ne postoji!", "Nastavak", 0);
		// }
		return vrijed;
	}

}