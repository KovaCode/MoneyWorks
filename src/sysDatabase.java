import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import net.miginfocom.swt.MigLayout;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolTip;

import kovaUtils.OpalDialog.kovaDialog;
import kovaUtils.OpalDialog.kovaDialog.OpalDialogType;

public class sysDatabase implements FocusListener, KeyListener {

	private static java.sql.Connection cn = null;

	static Display display;
	static Shell shell;
	Shell s;

	static String gDriver;

	static ResultSet rs;
	static Label lbl;
	static Text txtIP;
	static Text txtSQLuser;
	static Text txtSQLPass;

	static String suser;
	static String spass;

	static Combo cmbDB;
	static Combo cmbtipDB;

	static Button cmdProfil;
	static Button cmdSpremi;
	static Button cmdTest;
	static Button cmdExit;
	static Label lblMess;

	static Boolean flgChange = false;

	MigLayout layout = new MigLayout("", "15[]7[]", "20[]7[]7[]7[]7[]"); //$NON-NLS-2$ //$NON-NLS-3$
	MigLayout glayout = new MigLayout("", "15[]7[]", "10[]7[]7[]7[]7[]"); //$NON-NLS-2$ //$NON-NLS-3$

	// final Image image1 = new Image(display, "dist/icons/star_red.ico");
	// final Image image2= new Image(display, "dist/icons/star_green.ico");
	private FileDialog dialog;
	private static Label lblDriver;

	static Text txtProfil;

	private Label sep;

	static Label lblProfil;

	private Text txt;

	static Button cmdDefault;

	private Button cmdOpcije;

	private static Button cmdNewProfile;

	static Combo cmbProfil;

	static ToolTip tip;

	public static void main(String[] args) {

	}

	public sysDatabase(Shell parent) {
		shell = new Shell(parent, SWT.APPLICATION_MODAL | SWT.DIALOG_TRIM);
		display = shell.getDisplay();

		Image icona = new Image(display, "icons/Boue.ico");
		shell.setImage(icona);

		DEF.CenterScreen(shell, display);

		shell.setText(DEF.appName + "- Datbase setUp");
		shell.setBackground(display.getSystemColor(SWT.COLOR_WHITE));
		shell.setSize(360, 450);
		shell.setLayout(layout);

		createWidgets();
		fillForm();

		shell.open();

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}

	}

	public void createWidgets() {

		tip = new ToolTip(shell, SWT.BALLOON | SWT.ICON_INFORMATION);

		lblProfil = new Label(shell, SWT.CENTER);
		lblProfil.setText(DEF.getLabel("lblProfil"));
		lblProfil.setLayoutData("width 80px,split3");
		lblProfil.setBackground(display.getSystemColor(SWT.COLOR_WHITE));

		cmbProfil = new Combo(shell, SWT.READ_ONLY);
		cmbProfil.setLayoutData("width 140px");

		String path = "profiles/";
		String files;
		File folder = new File(path);
		File[] listOfFiles = folder.listFiles();

		int uk = 0;
		uk = listOfFiles.length;

		int j = 0;
		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].isFile()) {
				files = listOfFiles[i].getName();
				if (files.endsWith(".ini")) {
					System.out.println(j);
					cmbProfil.add(files, j);
					j += 1;
				}
			}
		}
		cmbProfil.select(0);
		cmbProfil.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {

			}

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				DEF.setgFile(cmbProfil.getText());

				if (DEF.fileExists("profiles/" + cmbProfil.getText()) == true) {
					try {
						FileInputStream fstream = new FileInputStream(
								"profiles/" + cmbProfil.getText());

						DataInputStream ini = new DataInputStream(fstream);
						BufferedReader br = new BufferedReader(
								new InputStreamReader(ini));

						DEF.setgLoginForm(Integer.parseInt(String.valueOf(br
								.readLine().substring(10))));
						DEF.setgJezik(Integer.parseInt(String.valueOf(br
								.readLine().substring(6))));
						DEF.setgBaza(Integer.parseInt(String.valueOf(br
								.readLine().substring(8))));
						DEF.setgImeBaze(br.readLine().substring(8));
						DEF.setgIndexBaze(Integer.parseInt(String.valueOf(br
								.readLine().substring(10))));
						DEF.setgServer(br.readLine().substring(7));
						DEF.setgLoggPath(br.readLine().substring(9));
						DEF.setgLoggErrPath(br.readLine().substring(12));
						DEF.setgAplikacija(br.readLine().substring(11));
						DEF.setgProfil(br.readLine().substring(7));

						fillForm();

						ini.close();

					} catch (Exception e) {// Catch exception if any
						System.err.println("Error: " + e.getMessage());
						logger.loggErr("sysDatabase " + e.getMessage());
					}
				} else {
					// System.out.println("READ ini ne postoji");
					ALERT.open(
							Display.getCurrent().getActiveShell().getShell(),
							"!", "Greška!", "Datoteka Config.ini ne postoji!", "Nastavak", 0); //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
					kovaDialog.inform(
							DEF.getLabel("mess12"), DEF.getLabel("mess12txt")); //$NON-NLS-2$
				}

			}

		});

		cmdNewProfile = new Button(shell, SWT.PUSH);
		cmdNewProfile.setText(DEF.getLabel("cmdNewProfile"));
		cmdNewProfile.setLayoutData("width 105px,wrap");

		cmdNewProfile.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {

				// int odg = ALERT.open(shell, "?",
				// "Spremanje","Želite li kreirati novi profil?","Novi profil|Odustani",
				// 0);

				int odg = ALERT.show(
						DEF.getLabel("mess13"), "?", DEF.getLabel("mess13txt"), OpalDialogType.OK_CANCEL, "", ""); //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$

				if (odg == 0) {
					ResetForm();

				} else {
					cmdSpremi.setEnabled(false);
				}

			}

		});

		// cmdProfil = new Button(shell,SWT.PUSH);
		// cmdProfil.setText("Uèitaj...");
		// cmdProfil.setLayoutData("width 80px,wrap");
		//
		// cmdProfil.addListener(SWT.Selection, new Listener() {
		// public void handleEvent(Event e) {
		// switch (e.type) {
		// case SWT.Selection:
		//
		// DEF.setgFile(cmbProfil.getText());
		//
		// if (DEF.fileExists("src/profiles/" + cmbProfil.getText()) == true) {
		// try {
		// FileInputStream fstream = new FileInputStream("src/profiles/" +
		// cmbProfil.getText());
		//
		// DataInputStream ini = new DataInputStream(fstream);
		// BufferedReader br = new BufferedReader(new InputStreamReader(ini));
		//
		// DEF.setgLoginForm(Integer.parseInt(String.valueOf(br.readLine().substring(10))));
		// DEF.setgJezik(Integer.parseInt(String.valueOf(br.readLine().substring(6))));
		// DEF.setgBaza(Integer.parseInt(String.valueOf(br.readLine().substring(8))));
		// DEF.setgImeBaze(br.readLine().substring(8));
		// DEF.setgIndexBaze(Integer.parseInt(String.valueOf(br.readLine().substring(10))));
		// DEF.setgServer(br.readLine().substring(7));
		// DEF.setgLoggPath(br.readLine().substring(9));
		// DEF.setgAplikacija(br.readLine().substring(11));
		// DEF.setgProfil(br.readLine().substring(7));
		//
		//
		// fillForm();
		//
		//
		// ini.close();
		//
		// } catch (Exception evt) {// Catch exception if any
		// System.err.println("Error: " + evt.getMessage());
		// }
		// }else{
		// System.out.println("READ ini ne postoji");
		// ALERT.open(Display.getCurrent().getActiveShell().getShell(), "!",
		// "Greška!", "Datoteka Config.ini ne postoji!", "Nastavak", 0);
		// }
		//
		// }
		// }
		//
		// });

		lbl = new Label(shell, SWT.NONE);
		lbl.setText(DEF.getLabel("lblProfil2"));
		lbl.setLayoutData("width 80px,split5");
		lbl.setBackground(display.getSystemColor(SWT.COLOR_WHITE));

		txtProfil = new Text(shell, SWT.SINGLE | SWT.BORDER);
		txtProfil.setLayoutData("width 150px,wrap");
		txtProfil.addFocusListener(this);
		txtProfil.addKeyListener(this);

		sep = new Label(shell, SWT.SEPARATOR | SWT.SHADOW_IN | SWT.HORIZONTAL);
		sep.setLayoutData("width 270px,wrap");
		sep.setData("SEP");

		lbl = new Label(shell, SWT.NONE);
		lbl.setText(DEF.getLabel("lblServer"));
		lbl.setLayoutData("width 80px,split3");
		lbl.setBackground(display.getSystemColor(SWT.COLOR_WHITE));

		txtIP = new Text(shell, SWT.SINGLE | SWT.BORDER);
		txtIP.setTextLimit(15);
		txtIP.setLayoutData("width 150px,wrap");
		txtIP.addFocusListener(this);
		txtIP.addKeyListener(this);

		lbl = new Label(shell, SWT.NONE);
		lbl.setText(DEF.getLabel("lblConnType"));
		lbl.setLayoutData("width 80px,split3");
		lbl.setBackground(display.getSystemColor(SWT.COLOR_WHITE));

		cmbtipDB = new Combo(shell, SWT.READ_ONLY);
		cmbtipDB.setLayoutData("width 150px,wrap");
		cmbtipDB.add("SQL", 0);
		cmbtipDB.add("MySQL", 1);
		cmbtipDB.add("Oracle", 2);
		cmbtipDB.addFocusListener(this);
		cmbtipDB.select(0);

		cmbtipDB.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent e) {
				switch (cmbtipDB.getSelectionIndex()) {
				case 0:
					gDriver = "sun.jdbc.odbc.JdbcOdbcDriver";
					lblDriver.setText(gDriver);
					RSet.setgTipBaze(0);
					break;
				case 1:
					gDriver = "com.mysql.jdbc.Driver";
					lblDriver.setText(gDriver);
					RSet.setgTipBaze(1);
					break;
				case 2:
					gDriver = "";
					lblDriver.setText(gDriver);
					break;
				}

				RSet.setUrl();
				RSet.initRS();
				connectDatabase();

			}

			public void widgetDefaultSelected(SelectionEvent e) {
				lblDriver.setText(cmbtipDB.getText());
				// System.out.println("Default selected index: "
				// + cmbtipDB.getSelectionIndex()
				// + ", selected item: "
				// + (cmbtipDB.getSelectionIndex() == -1 ? "<null>" : cmbtipDB
				// .getItem(cmbtipDB.getSelectionIndex())) +
				// ", text content in the text field: "
				// + cmbtipDB.getText());
			}
		});

		lblDriver = new Label(shell, SWT.CENTER);
		lblDriver.setText("");
		lblDriver.setBackground(display
				.getSystemColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND));
		lblDriver.setForeground(display.getSystemColor(SWT.COLOR_DARK_BLUE));
		lblDriver.setLayoutData("width 300,wrap");
		lblDriver.setBackground(display.getSystemColor(SWT.COLOR_WHITE));

		switch (cmbtipDB.getSelectionIndex()) {
		case 0:
			gDriver = "sun.jdbc.odbc.JdbcOdbcDriver";
			lblDriver.setText(gDriver);
			break;
		case 1:
			gDriver = "com.mysql.jdbc.Driver";
			lblDriver.setText(gDriver);
			break;
		case 2:
			gDriver = "";
			lblDriver.setText(gDriver);
			break;
		}

		lbl = new Label(shell, SWT.NONE);
		lbl.setText(DEF.getLabel("lblBase"));
		lbl.setLayoutData("width 80px,split3");
		lbl.setBackground(display.getSystemColor(SWT.COLOR_WHITE));

		cmbDB = new Combo(shell, SWT.READ_ONLY);
		cmbDB.setLayoutData("width 150px,wrap");
		cmbDB.addFocusListener(this);
		cmbDB.setEnabled(false);
		connectDatabase();

		// cmbDB.addSelectionListener(new SelectionListener() {
		// public void widgetSelected(SelectionEvent e) {
		// switch(cmbDB.getSelectionIndex()){
		// case 0:
		//
		// case 1:
		//
		// case 2:
		//
		// }
		//
		// fillCMB();
		//
		// }
		//
		// public void widgetDefaultSelected(SelectionEvent e) {
		// lblDriver.setText(cmbtipDB.getText());
		// System.out.println("Default selected index: "
		// + cmbtipDB.getSelectionIndex()
		// + ", selected item: "
		// + (cmbtipDB.getSelectionIndex() == -1 ? "<null>" : cmbtipDB
		// .getItem(cmbtipDB.getSelectionIndex())) +
		// ", text content in the text field: "
		// + cmbtipDB.getText());
		// }
		// });
		//

		sep = new Label(shell, SWT.SEPARATOR | SWT.SHADOW_IN | SWT.HORIZONTAL);
		sep.setLayoutData("width 270px,wrap");
		sep.setData("SEP");

		Group group = new Group(shell, SWT.None);
		group.setLayoutData("width 260px,height 100px,wrap,span2");
		group.setText(DEF.getLabel("GR3"));
		group.setBackground(display.getSystemColor(SWT.COLOR_WHITE));
		group.setLayout(glayout);

		lbl = new Label(group, SWT.NONE);
		lbl.setText(DEF.getLabel("lblUser"));
		lbl.setLayoutData("width 50px,split3");
		lbl.setBackground(display.getSystemColor(SWT.COLOR_WHITE));

		txtSQLuser = new Text(group, SWT.SINGLE | SWT.BORDER);
		txtSQLuser.setLayoutData("width 150px,wrap");
		txtSQLuser.addFocusListener(this);

		lbl = new Label(group, SWT.NONE);
		lbl.setText(DEF.getLabel("lblPass"));
		lbl.setLayoutData("width 50px,split3");
		lbl.setBackground(display.getSystemColor(SWT.COLOR_WHITE));

		txtSQLPass = new Text(group, SWT.SINGLE | SWT.BORDER | SWT.PASSWORD);
		txtSQLPass.setLayoutData("width 150px,wrap");
		txtSQLPass.setTextLimit(25);
		txtSQLPass.addFocusListener(this);

		sep = new Label(group, SWT.SEPARATOR | SWT.SHADOW_IN | SWT.HORIZONTAL);
		sep.setLayoutData("width 200px,wrap");
		sep.setData("SEP");

		cmdTest = new Button(group, SWT.PUSH);
		cmdTest.setText("URL test");
		cmdTest.setLayoutData("width 200px");

		cmdTest.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				switch (e.type) {
				case SWT.Selection:

					if (CheckConnection() == true) {
						shell.addPaintListener(new PaintListener() {
							public void paintControl(PaintEvent event) {
								// event.gc.drawImage(image1, 250, 25);

							}
						});
					} else {
						shell.addPaintListener(new PaintListener() {
							public void paintControl(PaintEvent event) {
								// event.gc.drawImage(image2, 250, 25);

							}
						});
					}

					lblMess.setText(RSet.getUrl());

					break;
				}
			}

		});

		group.pack();

		sep = new Label(shell, SWT.SEPARATOR | SWT.SHADOW_IN | SWT.HORIZONTAL);
		sep.setLayoutData("width 270px,wrap");
		sep.setData("SEP");

		lblMess = new Label(shell, SWT.NONE);
		lblMess.setForeground(display.getSystemColor(SWT.COLOR_BLUE));
		lblMess.setAlignment(SWT.CENTER);
		lblMess.setBackground(display.getSystemColor(SWT.COLOR_WHITE));
		lblMess.setLayoutData("width 300px,span");

		sep = new Label(shell, SWT.SEPARATOR | SWT.SHADOW_IN | SWT.HORIZONTAL);
		sep.setLayoutData("width 270px,wrap");
		sep.setData("SEP");

		cmdSpremi = new Button(shell, SWT.PUSH);
		cmdSpremi.setText(DEF.getLabel("cmdSpremi"));
		cmdSpremi.setLayoutData("width 105px,split 3");
		// cmdSpremi.setEnabled(false);

		cmdSpremi.addListener(SWT.Selection, new Listener() {
			private String fileName;

			public void handleEvent(Event e) {
				switch (e.type) {
				case SWT.Selection:

					// provjera vrijednosti
					if (checkValues() == false) {

						kovaDialog.inform(
								DEF.getLabel("mess14"), DEF.getLabel("mess14txt")); //$NON-NLS-2$
						// ALERT.open(shell, "!", "Provjera vrijednosti",
						// "Nisu unešene sve potrebne vrijednosti!",
						// "Nastavak", 0);
					} else {

						// int odg = ALERT.open(shell, "?", "Spremanje",
						// "Želite li saèuvati ovu konekciju?",
						// "Spremi|Odustani", 0);

						int odg = ALERT.show(
								DEF.getLabel("mess15"), "?", DEF.getLabel("mess15txt"), OpalDialogType.OK_CANCEL, "", ""); //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$

						if (odg == 0) {
							// RSet.setSystemBase();
							// RSet.updateRS("update appPref set imeBaze ='"
							// + cmbDB.getText() + "', indexBaze ="
							// + cmbDB.getSelectionIndex() + ", server ='"
							// + txtIP.getText() + "'");
							// RSet.setDefaultBase();
							// RSet.setgTipBaze(cmbtipDB.getSelectionIndex());
							// RSet.setDriver(gDriver);
							// RSet.setSQLDBase(cmbDB.getText());
							// RSet.setSQLip(txtIP.getText());
							// RSet.setSQLpass(txtSQLPass.getText());
							// RSet.setSQLuser(txtSQLuser.getText());
							// RSet.setUrl();

							DEF.setgBaza(cmbtipDB.getSelectionIndex());
							DEF.setgImeBaze(cmbDB.getText());
							DEF.setgIndexBaze(cmbDB.getSelectionIndex());
							DEF.setgServer(txtIP.getText());
							DEF.setgLoggPath("logg/logg" + cmbtipDB.getText() + ".xml"); //$NON-NLS-2$
							DEF.setgLoggErrPath("logg/loggErr.xml");
							DEF.setgJezik(0);
							DEF.setgBaseUser(txtSQLuser.getText());
							DEF.setgBasePass(txtSQLPass.getText());
							DEF.setgAplikacija("1");
							DEF.setgProfil(txtProfil.getText());
							lblMess.setText(RSet.getUrl());

							// kreiranje datoteke

							String filePath = "";
							dialog = new FileDialog(shell, SWT.SAVE);
							dialog.setFilterNames(new String[] { "Standard configuration settings (*.ini)" });
							dialog.setFilterExtensions(new String[] { "*.ini" });
							dialog.setFilterPath("profiles/");
							dialog.setFileName("config.ini");
							filePath = dialog.open();
							fileName = dialog.getFileName();

							cmbProfil.add(fileName);

							DEF.setgFile(fileName);

							for (int i = 0; i < cmbProfil.getItemCount(); i++) {
								if (DEF.getgFile().equalsIgnoreCase(
										cmbProfil.getItem(i).toString())) {
									cmbProfil.select(i);
									break;
								}
							}

							if (DEF.writeINI(filePath) == true) {
								// ALERT.open(shell, "i",
								// "Kreiranja datoteke završeno!",
								// "Datoteka uspješno \"" + filePath
								// + "\" kreirana!", "Nastavak", 0);

								kovaDialog.inform(
										DEF.getLabel("mess16"), DEF.getLabel("mess16txt") + filePath + "\"</b>"); //$NON-NLS-2$ //$NON-NLS-3$

							} else {
								// ALERT.open(shell, "!",
								// "Greška prilikom kreiranja datoteke!",
								// "Datoteka nije kreirana!", "Nastavak",
								// 0);
								kovaDialog.inform(
										DEF.getLabel("mess17"), DEF.getLabel("mess17txt")); //$NON-NLS-2$
							}

							break;
						}
					}

				}
			}

		});

		cmdDefault = new Button(shell, SWT.PUSH);
		cmdDefault.setText(DEF.getLabel("cmdDefault"));
		cmdDefault.setLayoutData("width 105px");
		cmdDefault.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {

				// int odg = ALERT.open(shell, "?",
				// "Spremanje","Želite li postaviti ovaj profil za daljnje korištenje?","Postavi profil|Odustani",
				// 0);

				int odg = ALERT.show(
						DEF.getLabel("mess18"), "?", DEF.getLabel("mess18txt"), OpalDialogType.OK_CANCEL, "", ""); //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$

				RSet.setgTipBaze(cmbtipDB.getSelectionIndex());
				RSet.setDriver(gDriver);
				RSet.setSQLDBase(cmbDB.getText());
				RSet.setSQLip(txtIP.getText());
				RSet.setSQLpass(txtSQLPass.getText());
				RSet.setSQLuser(txtSQLuser.getText());

				RSet.setUrl();

				// loginform
				// jezik
				DEF.setgBaza(cmbtipDB.getSelectionIndex());
				DEF.setgImeBaze(cmbDB.getText());
				DEF.setgIndexBaze(cmbDB.getSelectionIndex());
				DEF.setgServer(txtIP.getText());
				// loggPath
				DEF.setgAplikacija("1");
				DEF.setgProfil(txtProfil.getText());
				DEF.writeINI("profiles/" + cmbProfil.getText());
			}

		});

		cmdOpcije = new Button(shell, SWT.PUSH);
		cmdOpcije.setText(DEF.getLabel("Option"));
		cmdOpcije.setLayoutData("width 105px,wrap");
		cmdOpcije.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {

				String input = ALERT
						.input(shell,
								"Unos administracijske lozinke",
								"!",
								"Unesite lozinku super administratora:",
								"",
								OpalDialogType.OK_CANCEL,
								"",
								"Potrebna je lozinka administratora\n pošto se podešenja poput jezika, logiranja i sl. odvijaju u slijedecem prozoru!\nMolimo administratorsku lozinku!");

				System.out.println("Prava: " + DEF.checkAdminRights(input));

				if (DEF.checkAdminRights(input) == true) {
					new opcije(shell);
				} else {
					kovaDialog.inform("Neispravna lozinka",
							"Neispravna administratorska lozinka!");
					input = ALERT
							.input(shell,
									"Unos administracijske lozinke",
									"!",
									"Unesite lozinku super administratora!",
									"",
									OpalDialogType.OK_CANCEL,
									"",
									"Potrebna je lozinka administratora\n pošto se podešenja poput jezika, logiranja i sl. odvijaju u slijedecem prozoru!\nMolimo administratorsku lozinku!");
				}

			}

		});

		// shell.setDefaultButton(cmdSpremi);
		//

		//
		// shell.pack();

	}

	private static Boolean checkValues() {
		Boolean flgChk = false;
		int chk = 0;

		if (txtIP.getText().length() > 0)
			chk = 1;
		if (txtProfil.getText().length() > 0)
			chk = 1;
		if (cmbDB.getText() != "" || cmbDB.getSelectionIndex() > 0)
			chk = 1;

		// if (chk==3) flgChk=true;
		flgChk = true;
		return flgChk;
	}

	private static void connectDatabase() {

		int iSelect = 0;
		if (CheckConnection() == true) {

			iSelect = fillDatabases();

			if (iSelect == 0) {
				// ALERT.open(shell,"i","Kreiranje baze...","Za aplikaciju nije pronaðena potrebna baza!\nAplikacija æe sustavom automatizacije na vaš bazni engine kreirati bazu JMoney!","Nastavak",
				// 0);
				kovaDialog.inform(
						DEF.getLabel("mess19"), DEF.getLabel("mess19txt")); //$NON-NLS-2$

				DEF.createBase();

				iSelect = fillDatabases();
				cmbDB.select(iSelect);

			} else {
				cmbDB.select(iSelect);
			}

		}
	}

	private static int fillDatabases() {
		int iSelect = 0;

		if (RSet.getgTipBaze() == 0) { // sql
			rs = RSet.openRS("SELECT name FROM sys.sysdatabases");
		} else if (RSet.getgTipBaze() == 1) { // mysql
			rs = RSet.openRS("show databases");
		}

		cmbDB.removeAll();
		try {
			int i = 0;
			while (rs.next()) {
				cmbDB.add(rs.getString(1), (i));
				System.out.println(rs.getString(1));
				if (rs.getString(1).equalsIgnoreCase("jmoney")) {
					iSelect = i;
				}
				i++;
			}
		} catch (SQLException e) {
			logger.loggErr("sysDatabase " + e.getMessage());

		}

		return iSelect;

	}

	private static void fillForm() {

		txtProfil.setText(DEF.iNull(DEF.getgProfil()));

		if (txtProfil.getText().length() == 0) {
			cmdNewProfile.setEnabled(false);
			cmdSpremi.setEnabled(true);
		} else {
			cmdNewProfile.setEnabled(true);
			cmdSpremi.setEnabled(false);
		}

		txtSQLuser.setText(DEF.iNull(DEF.getgBaseUser()));
		txtSQLPass.setText(DEF.iNull(DEF.getgBasePass()));
		txtIP.setText(DEF.iNull(DEF.getgServer()));
		lblMess.setText(DEF.iNull(RSet.getUrl()));

		// popunjava profile
		for (int i = 0; i < cmbProfil.getItemCount(); i++) {
			if (DEF.getgFile()
					.equalsIgnoreCase(cmbProfil.getItem(i).toString())) {
				cmbProfil.select(i);
				break;
			}
		}

		// popunjava drivere - vrste baza
		cmbtipDB.select(DEF.getgBaza());
		switch (cmbtipDB.getSelectionIndex()) {
		case 0:
			gDriver = "sun.jdbc.odbc.JdbcOdbcDriver";
			lblDriver.setText(gDriver);
			break;
		case 1:
			gDriver = "com.mysql.jdbc.Driver";
			lblDriver.setText(gDriver);
			break;
		case 2:
			gDriver = "";
			lblDriver.setText(gDriver);
			break;
		}

		RSet.setUrl();
		RSet.initRS();
		connectDatabase();

		// popunjava drivere - vrste baza
		for (int i = 0; i < cmbDB.getItemCount(); i++) {
			if (DEF.getgImeBaze().equalsIgnoreCase(cmbDB.getItem(i).toString())) {
				cmbDB.select(i);
				break;
			}
		}

		// txtSQLPass.setText(RSet.getSQLpass());
		// txtSQLuser.setText(RSet.getSQLuser());

	}

	private static boolean CheckConnection() {
		Boolean chkConn;

		if (RSet.getSQLip() == null) {
			chkConn = false;
		} else {

			try {
				RSet.setSQLDBase("master");
				Class.forName(RSet.getDriver());
				cn = DriverManager.getConnection(RSet.getUrl(),
						txtSQLuser.getText(), txtSQLPass.getText());

				if (cn != null) {
					chkConn = true;
					// System.out.println("Connection Successful!");
					cmbDB.setEnabled(true);
				} else {

					chkConn = false;
					cmbDB.setEnabled(false);
					// System.out.println("Connection none!");
				}

			} catch (Exception e) {
				chkConn = false;

			}
		}
		// System.out.println("CheckConnection vrijed: " + chkConn);
		return chkConn;

	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void focusGained(FocusEvent e) {
		if (e.widget instanceof Text) {
			Text t = (Text) e.widget;
			t.setBackground(display.getSystemColor(SWT.COLOR_INFO_BACKGROUND));
			t.selectAll();
		} else if (e.widget instanceof Combo) {
			Combo cmb = (Combo) e.widget;
			cmb.setBackground(display.getSystemColor(SWT.COLOR_INFO_BACKGROUND));
		}

	}

	@Override
	public void focusLost(FocusEvent e) {

		if (e.widget instanceof Text) {
			Text t = (Text) e.widget;
			t.setBackground(display.getSystemColor(SWT.COLOR_WHITE));

			if (t.equals(txtIP)) {
				RSet.setSQLip(txtIP.getText());
				RSet.setUrl();
				connectDatabase();
			}

			if (t.equals(txtSQLuser)) {
				RSet.setSQLuser(txtSQLuser.getText());
				RSet.setUrl();
				connectDatabase();
			}

			if (t.equals(txtSQLPass)) {

				RSet.setSQLpass(txtSQLPass.getText());
				RSet.setUrl();
				connectDatabase();
			}

		} else if (e.widget instanceof Combo) {
			Combo cmb = (Combo) e.widget;
			cmb.setBackground(display.getSystemColor(SWT.COLOR_WHITE));

			if (cmb.equals(cmbDB)) {
				RSet.setSQLDBase(cmbDB.getText());
				RSet.setUrl();
			}
		}

	}

	public void keyPressed(KeyEvent e) {
		Text t = (Text) e.widget;

		if (e.keyCode == 16777218) {// dolje
			t.traverse(SWT.TRAVERSE_TAB_NEXT);
		} else if (e.keyCode == 16777217) {// gore
			t.traverse(SWT.TRAVERSE_TAB_PREVIOUS);
		}

	}

	private static void ResetForm() {
		txtProfil.setText("");
		txtIP.setText("");
		txtSQLPass.setText("");
		txtSQLuser.setText("");
		cmbDB.removeAll();
		cmbProfil.add("");
		cmbtipDB.select(0);
		cmdSpremi.setEnabled(true);
		cmdNewProfile.setEnabled(false);
		cmdDefault.setEnabled(false);
		cmbProfil.select((cmbProfil.getItemCount() - 1));
	}

}
