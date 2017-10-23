import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import kovaUtils.OpalDialog.ChoiceItem;
import kovaUtils.OpalDialog.kovaDialog;
import kovaUtils.OpalDialog.kovaDialog.OpalDialogType;
import kovaUtils.Panels.BlurredPanel;
import kovaUtils.Panels.DarkPanel;
import kovaUtils.TextAssist.TextAssist;
import kovaUtils.TextAssist.TextAssistContentProvider;

import net.miginfocom.swt.MigLayout;

import org.eclipse.jface.dialogs.DialogTray;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
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
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolTip;
import org.eclipse.swt.widgets.Tray;
import org.eclipse.swt.widgets.TrayItem;
import org.jfree.ui.RefineryUtilities;
import javax.swing.Timer;

@SuppressWarnings("static-access")
public class mainFrame extends DEF implements FocusListener, KeyListener,
		VerifyListener, SelectionListener, MouseListener {

	static Boolean flgAdmin = false;

	static List<String> lista;
	static List<String> lista2;
	static String[] EUROZONE2;
	static TextAssistContentProvider contentProvider;
	static SimpleDateFormat sdf = new SimpleDateFormat(
			"dd.MMMM.yyyy , HH:mm:ss");
	static Timer timer;

	static String SQLquery = "";
	static String datum;
	static String sTablica = "racuni";
	static String selectedDir;

	static int IDbroj = 0;
	static int optIndex;
	static int tempSelect;
	static int tempIDbroj;

	static double ukIznos = 0;
	static double iznNeg = 0;

	static ResultSet rs;
	static ResultSet rsMenu;

	final Image icona;

	private Label lblStatusIcon;

	private Label lblFast;

	private Menu fileHelp;
	private MenuItem tabHelp;
	private MenuItem itemAbout;
	private MenuItem itemManual;
	private Menu helpMenu;
	private MenuItem itemAppLogg;

	static Image icon;

	static Shell shell;
	static Shell s;
	static Display display;

	static MigLayout layout = new MigLayout();
	static MigLayout layout2 = new MigLayout();
	static MigLayout layoutComp = new MigLayout();
	static MigLayout layout3 = new MigLayout();
	static MigLayout layout4 = new MigLayout("", "", "1[]7[]7[]1");
	static MigLayout layout5 = new MigLayout("", "", "1[]15[]15[]15");
	static MigLayout layoutStatus = new MigLayout("", "1[]1[]1[]1",
			"1[]1[]1[]1");

	static Composite comp;
	static Composite comp2;
	static Composite compStatus;

	static Group group;
	static Group group2;
	static Group group3;

	static DateTime dtp;
	static DateTime dtp2;

	static Text txtBrRata;
	static Text txtUkupRata;
	static TextAssist txtMjesto;
	static TextAssist txtSvrha;
	static Text txtIznos;
	static Text txtNet;

	static Label sep;
	static Label lbl;
	static Label lblKategs;
	static Label lblDatum;
	static Label lblNet;
	static Label lblUkIznos;
	static Label lblMjesto;
	static Label lblIznos;
	static Label lblSvrha;
	static Label lblBrRata;
	static Label lblIznosU;
	static Label lblStatus;
	static Label lblStatus2;
	static Label lblStatus3;

	static Tray tray;
	static TrayItem trayItem;

	static DialogTray tray2;
	static ToolTip tip;

	static Button cmdGraph;
	static Button[] opt;
	static Button cmdSpremi;
	static Button cmdDEL;
	static Button cmdLogg;
	static Button cmdOper;
	static Button cmdExit;
	static Button cmdIspis;
	static Button cmdPrew;
	static Button cmdNext;
	static Button cmdExp;
	static Button chkAdmin;

	static ImageCombo cmbKategs;

	static kovaTable table;
	static TableColumn colSort;
	static TableColumn IDCol, svrhaCol, datumCol, mjestoCol, colTip,
			colPinTrans, colR, colG, colB, colIznos, colCateg;

	static private Listener listener;

	static Menu mnu;
	static Menu categMenu;
	static Menu fileMenuStat;
	static Menu menuBar, fileMenu, adminMenu;

	static MenuItem separ;
	static MenuItem tabFile, tabEdit, itemIspis, itemIzlaz, itemIzvoz,
			itemKategorije;
	static MenuItem tabStat, itemMonth, itemMonth2, itemYear;
	static MenuItem tabAdmin, itemBalance, itemPregLogg, itemPregErrLogg,
			itemPregOper, itemOpcije, itemBackup, itemRestore;
	static MenuItem itemC;
	static MenuItem itemSep;

	static Color red;
	static Color white;
	static Color gray;
	static Color black;
	static Color blueish;

	private static String[][] val;
	private static int iRed;
	private static boolean flgSort;

	static Listener armListener;
	static Listener showListener;
	static Listener hideListener;

	static DarkPanel p;
	static BlurredPanel p2;

	public mainFrame() {
		shell = new Shell(display, SWT.TITLE | SWT.MIN);
		display = shell.getDisplay();
		shell = new Shell(display, SWT.TITLE | SWT.MIN);

		RSet.setOperIn(getgUserID());
		// logger.logg(getLabel("logg5"));

		tip = new ToolTip(shell, SWT.BALLOON | SWT.ICON_INFORMATION);
		icona = new Image(display, "library/icons/boue.ico");
		shell.setImage(icona);
		p = new DarkPanel(shell);
		p2 = new BlurredPanel(shell);

		red = display.getSystemColor(SWT.COLOR_RED);
		white = display.getSystemColor(SWT.COLOR_WHITE);
		black = display.getSystemColor(SWT.COLOR_BLACK);
		gray = display.getSystemColor(SWT.COLOR_WIDGET_LIGHT_SHADOW);
		blueish = (new Color(shell.getDisplay(), new RGB(191, 216, 255)));

		shell.setText(appName + " - " + appVer);
		shell.setBackground(white);

		if (login.getgPrava() == 2) { // admin prava
			flgAdmin = true;
			shell.setSize(520, 750);
		} else {
			flgAdmin = false;
			shell.setSize(520, 750);
		}

		CenterScreen(shell, display);

		shell.setLayout(layout);

		armListener = new Listener() {
			public void handleEvent(Event event) {
				MenuItem item = (MenuItem) event.widget;

				lblStatus.setText(item.getText());
				lblStatus.update();
			}
		};
		showListener = new Listener() {
			public void handleEvent(Event event) {
				Menu menu = (Menu) event.widget;
				MenuItem item = menu.getParentItem();
				if (item != null) {
					lblStatus.setText(item.getText());
					lblStatus.update();
				}
			}
		};
		hideListener = new Listener() {
			public void handleEvent(Event event) {
				lblStatus.setText("");
				lblStatus.update();
			}
		};

		shell.addListener(SWT.Close, new Listener() {
			public void handleEvent(final Event event) {

				p2.show();
				boolean confirm = kovaDialog.isConfirmed(getLabel("mess26"),
						getLabel("mess26txt"));

				if (confirm == true) {
					System.exit(1);
					RSet.setOperOut(getgUserID());
					// logger.logg(getLabel("logg6") + confirm);
				} else {
					event.doit = false;
					p2.hide();
				}

				// int odg=ALERT.open(shell, "?", "Iskljuèiti "+ appName
				// +" aplikaciju?", "Želite li izaæi iz aplikacije?",
				// "Izlaz|Odustani", 0);
				//
				// boolean confirm =
				// kovaDialog.isConfirmed("Želite li izaæi iz aplikacije?",
				// "Molim saèekajte prije izlaza!");

				// if (confirm==true){
				// System.exit(1);
				// RSet.setOperOut(getgUserID());
				// logger.logg("Osoba izašla iz aplikacije, odabrano" +
				// confirm);
				// }
				//
				//
				// if (odg==0 || odg==-1){
				// event.doit=true;
				// try {
				// System.runFinalization();
				// } catch (Throwable e) {
				// e.printStackTrace();
				// logger.loggErr("mainFrame " + e.getMessage());
				// }
				//
				// RSet.setOperOut(getgUserID());
				// logger.logg("Osoba izašla iz aplikacije");
				//
				// }else{
				// event.doit=false;
				// }
				// }

			}

		});

		createWidgets();
		// setLanguage(shell);

		shell.open();

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		// if (icona != null)
		// icona.dispose();
		display.dispose();

	}

	protected void finalize() throws Throwable {
		super.finalize(); // not necessary if extending Object.
	}

	// public static void main(String[] args) {
	//
	// new mainFrame();
	// }
	//

	private void createWidgets() {
		optIndex = 1;

		menuBar = new Menu(shell, SWT.BAR);
		tabFile = new MenuItem(menuBar, SWT.CASCADE);
		tabFile.setText(getLabel("Application"));

		fileMenu = new Menu(shell, SWT.DROP_DOWN);
		fileMenu.addListener(SWT.Hide, hideListener);
		fileMenu.addListener(SWT.Show, showListener);
		itemKategorije = new MenuItem(fileMenu, SWT.PUSH);
		itemKategorije.setText(getLabel("Categories"));
		itemKategorije.addSelectionListener(new MenuItemListener());
		itemKategorije.addListener(SWT.Arm, armListener);
		itemKategorije.addListener(SWT.Hide, hideListener);
		itemKategorije.addListener(SWT.Show, showListener);
		itemSep = new MenuItem(fileMenu, SWT.SEPARATOR);

		itemIspis = new MenuItem(fileMenu, SWT.PUSH);
		itemIspis.setText(getLabel("Print"));
		itemIspis.addSelectionListener(new MenuItemListener());
		itemIspis.addListener(SWT.Arm, armListener);
		itemIspis.addListener(SWT.Hide, hideListener);
		itemIspis.addListener(SWT.Show, showListener);

		itemIzvoz = new MenuItem(fileMenu, SWT.CASCADE);
		itemIzvoz.setText(getLabel("Export"));
		itemIzvoz.addSelectionListener(new MenuItemListener());
		itemIzvoz.addListener(SWT.Arm, armListener);
		itemIzvoz.addListener(SWT.Hide, hideListener);
		itemIzvoz.addListener(SWT.Show, showListener);

		itemSep = new MenuItem(fileMenu, SWT.SEPARATOR);

		itemIzlaz = new MenuItem(fileMenu, SWT.PUSH);
		itemIzlaz.setText(getLabel("Exit"));
		itemIzlaz.addSelectionListener(new MenuItemListener());
		itemIzlaz.addListener(SWT.Arm, armListener);
		itemIzlaz.addListener(SWT.Hide, hideListener);
		itemIzlaz.addListener(SWT.Show, showListener);
		tabFile.setMenu(fileMenu);

		tabStat = new MenuItem(menuBar, SWT.CASCADE);
		tabStat.setText(getLabel("Stat"));
		tabStat.setData("");

		fileMenuStat = new Menu(shell, SWT.DROP_DOWN);
		fileMenuStat.addListener(SWT.Hide, hideListener);
		fileMenuStat.addListener(SWT.Show, showListener);

		itemMonth = new MenuItem(fileMenuStat, SWT.PUSH);
		itemMonth.setText(getLabel("MonthStat1"));
		itemMonth.setData("");
		itemMonth.addSelectionListener(new MenuItemListener());
		itemMonth.addListener(SWT.Arm, armListener);
		itemMonth.addListener(SWT.Hide, hideListener);
		itemMonth.addListener(SWT.Show, showListener);

		itemMonth2 = new MenuItem(fileMenuStat, SWT.PUSH);
		itemMonth2.setText(getLabel("MonthStat2"));
		itemMonth2.addSelectionListener(new MenuItemListener());
		itemMonth2.addListener(SWT.Arm, armListener);
		itemMonth2.addListener(SWT.Hide, hideListener);
		itemMonth2.addListener(SWT.Show, showListener);

		itemSep = new MenuItem(fileMenuStat, SWT.SEPARATOR);

		itemYear = new MenuItem(fileMenuStat, SWT.PUSH);
		itemYear.setText(getLabel("YearStat"));
		itemYear.addSelectionListener(new MenuItemListener());
		itemYear.addListener(SWT.Arm, armListener);
		itemYear.addListener(SWT.Hide, hideListener);
		itemYear.addListener(SWT.Show, showListener);

		tabStat.setMenu(fileMenuStat);

		if (flgAdmin == true) {
			tabAdmin = new MenuItem(menuBar, SWT.CASCADE);
			tabAdmin.setText(getLabel("Admin"));
			adminMenu = new Menu(shell, SWT.DROP_DOWN);
			adminMenu.addListener(SWT.Hide, hideListener);
			adminMenu.addListener(SWT.Show, showListener);

			itemBalance = new MenuItem(adminMenu, SWT.PUSH);
			itemBalance.setText(getLabel("Balance"));
			itemBalance.addSelectionListener(new MenuItemListener());
			itemBalance.addListener(SWT.Arm, armListener);
			itemBalance.addListener(SWT.Hide, hideListener);
			itemBalance.addListener(SWT.Show, showListener);

			itemSep = new MenuItem(adminMenu, SWT.SEPARATOR);

			itemPregLogg = new MenuItem(adminMenu, SWT.PUSH);
			itemPregLogg.setText(getLabel("LogPrew"));
			itemPregLogg.addSelectionListener(new MenuItemListener());
			itemPregLogg.addListener(SWT.Arm, armListener);
			itemPregLogg.addListener(SWT.Hide, hideListener);
			itemPregLogg.addListener(SWT.Show, showListener);

			itemPregErrLogg = new MenuItem(adminMenu, SWT.PUSH);
			itemPregErrLogg.setText(getLabel("LogErrPrew"));
			itemPregErrLogg.addSelectionListener(new MenuItemListener());
			itemPregErrLogg.addListener(SWT.Arm, armListener);
			itemPregErrLogg.addListener(SWT.Hide, hideListener);
			itemPregErrLogg.addListener(SWT.Show, showListener);

			itemSep = new MenuItem(adminMenu, SWT.SEPARATOR);

			itemPregOper = new MenuItem(adminMenu, SWT.PUSH);
			itemPregOper.setText(getLabel("Oper"));
			itemPregOper.addSelectionListener(new MenuItemListener());
			itemPregOper.addListener(SWT.Arm, armListener);
			itemPregOper.addListener(SWT.Hide, hideListener);
			itemPregOper.addListener(SWT.Show, showListener);

			itemSep = new MenuItem(adminMenu, SWT.SEPARATOR);

			itemOpcije = new MenuItem(adminMenu, SWT.PUSH);
			itemOpcije.setText(getLabel("Option"));
			itemOpcije.addSelectionListener(new MenuItemListener());
			itemOpcije.addListener(SWT.Arm, armListener);
			itemOpcije.addListener(SWT.Hide, hideListener);
			itemOpcije.addListener(SWT.Show, showListener);
			tabAdmin.setMenu(adminMenu);

			itemSep = new MenuItem(adminMenu, SWT.SEPARATOR);

			itemBackup = new MenuItem(adminMenu, SWT.PUSH);
			itemBackup.setText(getLabel("Backup"));
			itemBackup.addSelectionListener(new MenuItemListener());
			itemBackup.addListener(SWT.Arm, armListener);
			itemBackup.addListener(SWT.Hide, hideListener);
			itemBackup.addListener(SWT.Show, showListener);

			itemRestore = new MenuItem(adminMenu, SWT.PUSH);
			itemRestore.setText(getLabel("Restore"));
			itemRestore.addSelectionListener(new MenuItemListener());
			itemRestore.addListener(SWT.Arm, armListener);
			itemRestore.addListener(SWT.Hide, hideListener);
			itemRestore.addListener(SWT.Show, showListener);

		}

		tabHelp = new MenuItem(menuBar, SWT.CASCADE);
		tabHelp.setText(getLabel("help"));

		helpMenu = new Menu(shell, SWT.DROP_DOWN);
		helpMenu.addListener(SWT.Hide, hideListener);
		helpMenu.addListener(SWT.Show, showListener);

		itemAppLogg = new MenuItem(helpMenu, SWT.PUSH);
		itemAppLogg.setText(getLabel("appLogg"));
		itemAppLogg.addSelectionListener(new MenuItemListener());
		itemAppLogg.addListener(SWT.Arm, armListener);
		itemAppLogg.addListener(SWT.Hide, hideListener);
		itemAppLogg.addListener(SWT.Show, showListener);

		itemSep = new MenuItem(helpMenu, SWT.SEPARATOR);

		itemAbout = new MenuItem(helpMenu, SWT.PUSH);
		itemAbout.setText(getLabel("about"));
		itemAbout.addSelectionListener(new MenuItemListener());
		itemAbout.addListener(SWT.Arm, armListener);
		itemAbout.addListener(SWT.Hide, hideListener);
		itemAbout.addListener(SWT.Show, showListener);

		itemSep = new MenuItem(helpMenu, SWT.SEPARATOR);

		itemManual = new MenuItem(helpMenu, SWT.PUSH);
		itemManual.setText(getLabel("manual"));
		itemManual.addSelectionListener(new MenuItemListener());
		itemManual.addListener(SWT.Arm, armListener);
		itemManual.addListener(SWT.Hide, hideListener);
		itemManual.addListener(SWT.Show, showListener);

		tabHelp.setMenu(helpMenu);

		shell.setMenuBar(menuBar);

		group = new Group(shell, SWT.None);
		group.setText(getLabel("GR1"));
		group.setBackground(white);
		group.setForeground(display.getSystemColor(SWT.COLOR_BLUE));
		group.setLayoutData("width 500, wrap");
		group.setLayout(layout2);

		//
		comp = new Composite(group, SWT.None);
		comp.setBackground(white);
		comp.setLayout(layoutComp);
		comp.setData("COMP");

		comp2 = new Composite(group, SWT.None);
		comp2.setBackground(white);
		comp2.setLayout(layoutComp);
		comp2.setData("COMP2");

		//
		//

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
					iznNeg = 0;
					iznNeg = getReal(txtIznos.getText());
					iznNeg = Math.abs(iznNeg);
					txtIznos.setText(String.valueOf(iznNeg));
					txtIznos.setText(FormatCur(txtIznos.getText()));

					lblBrRata.setVisible(false);
					txtBrRata.setVisible(false);
					lblIznosU.setVisible(false);
					txtUkupRata.setVisible(false);
					txtMjesto.setSize(260, 20);
					lblNet.setVisible(false);
					txtNet.setVisible(false);
					break;

				case 1:
					iznNeg = 0;
					iznNeg = Math.abs(getReal(txtIznos.getText()));
					iznNeg = (iznNeg * -1);
					txtIznos.setText(String.valueOf(iznNeg));
					txtIznos.setText(FormatCur(txtIznos.getText()));

					lblBrRata.setVisible(false);
					txtBrRata.setVisible(false);
					lblIznosU.setVisible(false);
					txtUkupRata.setVisible(false);
					txtMjesto.setSize(260, 20);
					lblNet.setVisible(false);
					txtNet.setVisible(false);
					break;

				case 2:
					iznNeg = 0;
					iznNeg = Math.abs(getReal(txtIznos.getText()));
					iznNeg = (iznNeg * -1);
					txtIznos.setText(String.valueOf(iznNeg));
					txtIznos.setText(FormatCur(txtIznos.getText()));
					String sUK = String.valueOf(Round(
							getReal(txtIznos.getText())
									/ getReal(txtBrRata.getText()), 2));
					txtUkupRata.setText(FormatCur(sUK));
					lblBrRata.setVisible(true);
					txtBrRata.setVisible(true);
					lblIznosU.setVisible(true);
					txtUkupRata.setVisible(true);
					lblNet.setVisible(false);
					txtNet.setVisible(false);
					txtMjesto.setSize(260, 20);
					break;

				case 3:
					iznNeg = 0;
					iznNeg = Math.abs(getReal(txtIznos.getText()));
					iznNeg = (iznNeg * -1);
					txtIznos.setText(String.valueOf(iznNeg));
					txtIznos.setText(FormatCur(txtIznos.getText()));
					txtMjesto.setSize(170, 20);
					txtMjesto.setRedraw(true);
					lblNet.setLocation(270, 90);
					lblNet.setVisible(true);
					txtNet.setLocation(300, 88);
					txtNet.setVisible(true);
					lblBrRata.setVisible(false);
					txtBrRata.setVisible(false);
					lblIznosU.setVisible(false);
					txtUkupRata.setVisible(false);
					break;

				}

			}

		};

		String optText[] = { "Prihodi", "Rashodi", "Rate", "NetBanking" };
		opt = new Button[4];
		for (int i = 0; i < opt.length; i++) {
			opt[i] = new Button(comp, SWT.RADIO);
			opt[i].setText(getLabel("opt" + i));
			opt[i].setLayoutData("width 30px");
			opt[i].setBackground(white);
			opt[i].addListener(SWT.Selection, listener);
		}

		opt[0].setLayoutData("split 4");
		opt[3].setLayoutData("wrap");
		opt[1].setSelection(true);

		sep = new Label(comp, SWT.SEPARATOR | SWT.SHADOW_IN | SWT.HORIZONTAL);
		sep.setLayoutData("width 400px,wrap");
		sep.setData("SEP");

		lblKategs = new Label(comp, SWT.NONE);
		lblKategs.setText(getLabel("lblKategs"));
		lblKategs.setData("");
		lblKategs.setLayoutData("width 70px,split 2,gapleft 2px");
		lblKategs.setBackground(white);

		cmbKategs = new ImageCombo(comp, SWT.READ_ONLY | SWT.BORDER);
		cmbKategs.setLayoutData("width 150px,gapleft 5px,wrap");
		fillKategs();

		lblDatum = new Label(comp, SWT.NONE);
		lblDatum.setText(getLabel("lblDatum"));
		lblDatum.setLayoutData("width 70px,split2,gapright 6px,gapleft 2px");
		lblDatum.setBackground(white);

		dtp = new DateTime(comp, SWT.DATE | SWT.DROP_DOWN);
		dtp.setLayoutData("width 100px");
		dtp.setDate(getToday().getYear(), getToday().getMonth(), getToday()
				.getDay());
		dtp.setMonth(getToday().getMonth());
		dtp.setYear(getToday().getYear());
		dtp.addSelectionListener(this);

		dtp.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				switch (e.type) {
				case SWT.Selection:
					dtp2.setDate(dtp.getYear(), dtp.getMonth(), dtp.getDay());
					datum = dtp.getYear() + "-" + dtp.getMonth() + "-"
							+ dtp.getDay();
					freshTable();
					break;
				}
			}

		});

		// samo za NetBanking
		lblNet = new Label(comp, SWT.NONE);
		lblNet.setText(getLabel("lblNet"));
		lblNet.setLayoutData("width 30px");
		lblNet.setBackground(white);
		lblNet.setVisible(false);

		txtNet = new Text(comp, SWT.BORDER);
		txtNet.setTextLimit(11);
		txtNet.setLayoutData("width 75px,wrap");
		txtNet.addFocusListener(this);
		txtNet.addKeyListener(this);
		txtNet.setVisible(false);

		lblMjesto = new Label(comp, SWT.NONE);
		lblMjesto.setText(getLabel("lblMjesto"));
		lblMjesto
				.setLayoutData("width 70px,split2,gapright 6px,gapleft 2px,span2");
		lblMjesto.setBackground(white);

		rs = RSet
				.openRS("select distinct mjesto from racuni where Mjesto is not null ");

		lista = new ArrayList<String>();
		int i = 0;
		try {
			while (rs.next()) {
				lista.add(rs.getString("mjesto"));
				i++;
			}
		} catch (SQLException e2) {
			e2.printStackTrace();
			kovaDialog.showException(e2);
		}

		contentProvider = new TextAssistContentProvider() {
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

		txtMjesto = new TextAssist(comp, SWT.BORDER, contentProvider); // txtMjesto
																		// = new
																		// Text
																		// (comp,
																		// SWT.BORDER);
		txtMjesto.setTextLimit(50);
		txtMjesto.setLayoutData("width 290px,wrap");
		// txtMjesto.addFocusListener(this);
		// txtMjesto.addKeyListener(this);

		lblSvrha = new Label(comp, SWT.NONE);
		lblSvrha.setText(getLabel("lblSvrha"));
		lblSvrha.setLayoutData("width 62px,split2,gapright 14px,gapleft 2px,span");
		lblSvrha.setBackground(white);

		rs = RSet
				.openRS("select distinct svrha from racuni where svrha is not null ");

		lista2 = new ArrayList<String>();
		try {
			while (rs.next()) {
				lista2.add(rs.getString("svrha"));
			}
		} catch (SQLException e2) {
			e2.printStackTrace();
			kovaDialog.showException(e2);
		}

		contentProvider = new TextAssistContentProvider() {
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
				contentProvider);
		txtSvrha.setTextLimit(50);
		txtSvrha.setLayoutData("width 290px,wrap");
		// txtSvrha.addFocusListener(this);
		// txtSvrha.addKeyListener(this);

		lblIznos = new Label(comp, SWT.NONE);
		lblIznos.setText(getLabel("lblIznos"));
		lblIznos.setLayoutData("width 61px,split6,gapright 15px,gapleft 2px,span");
		lblIznos.setBackground(white);

		txtIznos = new Text(comp, SWT.BORDER | SWT.RIGHT);
		txtIznos.setText("0,00");
		txtIznos.setTextLimit(15);
		txtIznos.setLayoutData("width 85px,span");
		txtIznos.addFocusListener(this);
		txtIznos.addKeyListener(this);
		txtIznos.addVerifyListener(this);

		// samo za rate...
		// ---------------------------------------------------------------------------------//
		lblBrRata = new Label(comp, SWT.NONE);
		lblBrRata.setText(getLabel("lblBrRata"));
		lblBrRata.setLayoutData("width 22px");
		lblBrRata.setBackground(white);
		lblBrRata.setVisible(false);

		txtBrRata = new Text(comp, SWT.BORDER);
		txtBrRata.setTextLimit(3);
		txtBrRata.setText("12");
		txtBrRata.setVisible(false);
		txtBrRata.addFocusListener(this);
		txtBrRata.addKeyListener(this);
		txtBrRata.addVerifyListener(this);

		lblIznosU = new Label(comp, SWT.NONE);
		lblIznosU.setText(getLabel("lblIznosU"));
		lblIznosU.setLayoutData("width 75px,split2,gapright 2px");
		lblIznosU.setBackground(white);
		lblIznosU.setVisible(false);

		txtUkupRata = new Text(comp, SWT.BORDER | SWT.RIGHT | SWT.READ_ONLY);
		txtUkupRata.setLayoutData("width 65px,span,wrap");
		txtUkupRata.setText("0,00");
		txtUkupRata.setVisible(false);
		txtUkupRata.addFocusListener(this);
		txtUkupRata.addKeyListener(this);
		txtUkupRata.addVerifyListener(this);

		// ---------------------------------------------------------------------------------//

		sep = new Label(comp, SWT.SEPARATOR | SWT.SHADOW_IN | SWT.HORIZONTAL);
		sep.setData("sep2");
		sep.setLayoutData("width 400px,wrap");

		// final Image icoSpremi= new Image(display,"library/icons/Save24.ico");
		cmdSpremi = new Button(comp, SWT.PUSH);
		cmdSpremi.setText(getLabel("cmdSpremi"));
		cmdSpremi.setLayoutData("width 105px,split 2");
		cmdSpremi.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
			}
		});

		cmdSpremi.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				switch (e.type) {
				case SWT.Selection:

					int rateID = 0;
					if (checkValues() == true) {
						if (optIndex == 2) {
							int god = dtp.getYear();
							int mj = dtp.getMonth();
							int dan = dtp.getDay();
							String datum;

							rs = RSet.openRS("select max(id)as Uk from racuni");

							try {
								while (rs.next()) {
									rateID = rs.getInt("uk");
								}
							} catch (SQLException e1) {
								e1.printStackTrace();
								// logger.loggErr("mainFrame " +
								// e1.getMessage());
								kovaDialog.showException(e1);
							}
							RSet.closeAll();

							for (int i = 0; i < Integer.parseInt(txtBrRata
									.getText()); i++) {
								if (mj == 12) {
									god += 1;
									mj = 1;
									datum = god + "-" + mj + "-" + dtp.getDay();
								} else {
									mj += 1;
									datum = god + "-" + mj + "-" + dtp.getDay();

								}

								String col = "datum,mjesto,svrha,iznos,tip,PINtrans,oper,rateID,kategorija";
								String val = "'"
										+ datum
										+ "','"
										+ txtMjesto.getText()
										+ "','"
										+ txtSvrha.getText()
										+ " "
										+ (i + 1)
										+ ".rata"
										+ "','"
										+ getReal(txtUkupRata.getText())
										+ "',"
										+ optIndex
										+ ",'"
										+ iNull(txtNet.getText() + "','"
												+ getgUserID() + "'," + rateID
												+ ",0");
								RSet.addRS2(sTablica, col, val);

								logger.logg(getLabel("logg7") + " " + sTablica);
								gBroj = 0;
							}

						} else {
							int categ = 0;
							rs = RSet.openRS("select id,name from categories");
							try {
								while (rs.next()) {

									if (cmbKategs.getText().equalsIgnoreCase(
											rs.getString("name"))) {
										categ = rs.getInt("id");
										break;
									}
								}
								rs.close();
							} catch (SQLException e2) {
								e2.printStackTrace();
								kovaDialog.showException(e2);
							}

							String col = "datum,mjesto,svrha,iznos,tip,PINtrans,oper,rateID,kategorija";
							String val = "'"
									+ dtp.getYear()
									+ "-"
									+ (dtp.getMonth() + 1)
									+ "-"
									+ dtp.getDay()
									+ "','"
									+ txtMjesto.getText()
									+ "','"
									+ txtSvrha.getText()
									+ "','"
									+ getReal(txtIznos.getText())
									+ "',"
									+ optIndex
									+ ",'"
									+ iNull(txtNet.getText() + "','"
											+ getgUserID() + "'," + rateID
											+ "," + categ);
							RSet.addRS2(sTablica, col, val);
							logger.logg(getLabel("logg8") + " " + sTablica);
							gBroj = 0;

							// RSet.setDefaultBase();
							// ResultSet rs=RSet.openRS(SQLquery +
							// "order by ID desc limit 1");
							// System.out.println(SQLquery +
							// "order by ID desc limit 1");
							// final TableItem created =
							// table.insertItem(SWT.NONE,table.fillVal2(rs));
							// table.setSelection(created);
							//
							//
							freshTable();

						}

						dtp.setDate(getToday().getYear(),
								getToday().getMonth(), getToday().getDay());
						dtp2.setMonth(dtp.getMonth());
						dtp2.setYear(dtp.getYear());
						txtMjesto.setText("");
						txtIznos.setText("0,00");
						txtSvrha.setText("");
						txtBrRata.setText("12");
						txtNet.setText("");
						cmbKategs.select(0);

						// ALERT.open(shell, "i", "Unos podataka...",
						// "Vrijednosti uspješno unešene!", "Nastavak",0);
						kovaDialog.inform(getLabel("mess25"),
								getLabel("mess25txt"));

					} else {
						kovaDialog.inform(getLabel("mess27"),
								getLabel("mess27txt"));
						// ALERT.open(shell, "!", "Unos podataka...",
						// "Nisu popunjena sva potrebna polja!", "Nastavak",0);

					}

				}

			}

		});

		cmdDEL = new Button(comp, SWT.PUSH);
		cmdDEL.setText(getLabel("cmdDEL"));
		cmdDEL.setLayoutData("width 105px,span 2");
		cmdDEL.setEnabled(false);
		cmdDEL.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {

				int tip = Integer.parseInt(getColVal(5));

				if (tip == 2) {
					// int odg =
					// ALERT.open(shell,"?","Brisanje?","Želite li obrisati rate?","Sve rate|Jednu ratu|Odustani",2);

					int odg = kovaDialog.choice(getLabel("mess28"), "", 1,
							new ChoiceItem(getLabel("ch281"),
									getLabel("ch2812")), new ChoiceItem(
									getLabel("ch282"), getLabel("ch2822")),
							new ChoiceItem(getLabel("ch283"),
									getLabel("ch2832")));

					int rateBr = 0;

					switch (odg) {
					case 0: // sve rate

						rs = RSet.openRS("select max(id)as Uk from racuni");

						try {
							while (rs.next()) {
								rateBr = rs.getInt("uk");
							}
						} catch (SQLException e1) {
							e1.printStackTrace();
							logger.loggErr("mainFrame " + e1.getMessage());
							kovaDialog.showException(e1);
						}
						RSet.closeAll();
						RSet.deleteRS(rateBr, sTablica, "rateID");
						freshTable();
						break;
					case 1: // jednu ratu
						RSet.deleteRSid(IDbroj, sTablica);
						freshTable();
						break;
					case 2: // odustani

						break;
					}
				} else {

					// int odg =
					// ALERT.open(shell,"?","Brisanje?","Molimo potvrdite brisanje!",
					// "Obriši|Odustani", 1);

					// final boolean confirm =
					// kovaDialog.isConfirmed("Želite li obrisati stavku pod rednim brojem "+
					// IDbroj + "?",="Mjesto: <b>"+ iNull(getColVal(3)) +
					// "</b><br/> Svrha: <b>" + iNull(getColVal(4))+
					// "</b><br/>Iznos: <b>"+ FormatCur(iNull0(getColVal(10)))+
					// "</b><br/>" );
					final boolean confirm = kovaDialog.isConfirmed(
							getLabel("mess1N"), getLabel("mess1T"));

					if (confirm == true) {
						RSet.deleteRSid(IDbroj, sTablica);
						freshTable();
						cmdDEL.setEnabled(false);
						logger.logg(getLabel("logg9") + " " + IDbroj + "!");
					} else {
						cmdDEL.setEnabled(false);
					}
				}

			}

		});

		// *****************************TABLICA******************************//

		group2 = new Group(shell, SWT.None);
		group2.setText(getLabel("GR2"));
		group2.setBackground(display.getSystemColor(SWT.COLOR_WHITE));
		group2.setLayoutData("span,wrap");
		group2.setForeground(display.getSystemColor(SWT.COLOR_BLUE));
		group2.setLayout(layout3);

		// ************************************kreiranje
		// tablice********************************************//

		cmdPrew = new Button(group2, SWT.PUSH);
		cmdPrew.setText(getLabel("cmdPrew"));
		cmdPrew.setLayoutData("width 100px,split 3");
		cmdPrew.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				if (dtp2.getMonth() == 0) {

					dtp2.setDate(dtp2.getYear() - 1, 11, dtp2.getDay());
					dtp.setDate(dtp2.getYear(), 11, dtp2.getDay());
					freshTable();
				} else {
					dtp2.setDate(dtp2.getYear(), dtp2.getMonth() - 1,
							dtp2.getDay());
					dtp.setDate(dtp.getYear(), dtp2.getMonth(), dtp2.getDay());
					freshTable();
				}
			}

		});

		dtp2 = new DateTime(group2, SWT.DATE | SWT.SHORT | SWT.BORDER);
		dtp2.setForeground(display.getSystemColor(SWT.COLOR_BLUE));
		dtp2.setBackground(display.getSystemColor(SWT.COLOR_BLUE));
		dtp2.setLayoutData("width 265px");
		dtp2.setData("DTP2");
		dtp2.setDay(getToday().getDay());
		dtp2.setMonth(getToday().getMonth());
		dtp2.setYear(getToday().getYear());

		dtp2.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				switch (e.type) {
				case SWT.Selection:
					dtp.setDay(dtp.getDay());
					dtp.setMonth(dtp2.getMonth());
					dtp.setYear(dtp2.getYear());
					datum = dtp.getYear() + "-" + dtp.getMonth() + "-"
							+ dtp.getDay();
					freshTable();
					break;
				}
			}

		});

		cmdNext = new Button(group2, SWT.PUSH);
		cmdNext.setText(getLabel("cmdNext"));
		cmdNext.setLayoutData("width 100px,wrap");
		cmdNext.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				if (dtp2.getMonth() == 11) {
					dtp2.setDate(dtp2.getYear() + 1, 0, dtp2.getDay());
					dtp.setDate(dtp2.getYear(), 0, dtp2.getDay());
					freshTable();
				} else {

					dtp2.setDate(dtp2.getYear(), dtp2.getMonth() + 1,
							dtp2.getDay());
					dtp.setDate(dtp2.getYear(), dtp2.getMonth(), dtp2.getDay());
					freshTable();
				}
			}

		});

		table = fillKovaTable(group2);
		makePopUp();

		lblUkIznos = new Label(group2, SWT.RIGHT);
		lblUkIznos.setLayoutData("width 120px,span,gapleft 340");
		lblUkIznos.setBackground(white);

		lblFast = new Label(group2, SWT.NONE);
		lblFast.setText(getLabel("lblFast"));
		lblFast.setLayoutData("width 70px,split 2");
		lblFast.setBackground(white);

		table.createSearchField(group2, SWT.BORDER).setLayoutData("growx,wrap");
		table.contentTable.addMouseListener(this);

		table.contentTable.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				if (table.contentTable.getSelectionIndex() != -1) {
					IDbroj = Integer.parseInt(table.getItem(
							table.contentTable.getSelectionIndex()).getText(1));
					cmdDEL.setEnabled(true);
				} else {
					IDbroj = 0;
					cmdDEL.setEnabled(false);
				}

			}
		});

		colCateg = table.getColumn(0);
		colCateg.setData("");
		colCateg.setWidth(15);
		colCateg.addSelectionListener(this);
		colCateg.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				colSort = table.getColumn(0);
				setSort();
				table.applyFilter();
			}
		});

		IDCol = table.getColumn(1);
		IDCol.setText(getLabel("IDCol"));
		IDCol.setWidth(50);
		IDCol.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				colSort = table.getColumn(1);
				setSort();
				table.applyFilter();
			}
		});

		datumCol = table.getColumn(2);
		datumCol.setText(getLabel("datumCol"));
		datumCol.setWidth(70);
		datumCol.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				colSort = datumCol;
				setSort();
				table.applyFilter();
			}
		});

		mjestoCol = table.getColumn(3);
		mjestoCol.setText(getLabel("mjestoCol"));
		mjestoCol.setWidth(120);
		mjestoCol.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				colSort = table.getColumn(3);
				setSort();
				table.applyFilter();
			}
		});

		svrhaCol = table.getColumn(4);
		svrhaCol.setText(getLabel("svrhaCol"));
		svrhaCol.setWidth(145);
		svrhaCol.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				colSort = table.getColumn(4);
				setSort();
				table.applyFilter();
			}
		});

		colTip = table.getColumn(5);
		colTip.setData("COL6");
		colTip.setWidth(0);

		colPinTrans = table.getColumn(6);
		colPinTrans.setData("COL7");
		colPinTrans.setWidth(0);

		colR = table.getColumn(7);
		colR.setData("COL8");
		colR.setWidth(0);

		colG = table.getColumn(8);
		colG.setData("COL9");
		colG.setWidth(0);

		colB = table.getColumn(9);
		colB.setData("COL10");
		colB.setWidth(0);

		colIznos = table.getColumn(10);
		colIznos.setText(getLabel("colIznos"));
		colIznos.setWidth(70);
		colIznos.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				colSort = table.getColumn(10);
				setSort();
				table.applyFilter();
			}
		});

		table.setLayoutData("width 470px,height 300px,span");
		table.setEditable(false, 1, 2, 3);

		table.addColorListener(new Listener() {
			public void handleEvent(final Event event) {
				for (TableItem item : table.getItems()) {
					applyColors(item);
				}
			}
		});

		for (TableItem cIznos : table.getItems()) {
			double iznos = Double.parseDouble(cIznos.getText(10));
			ukIznos = ukIznos + iznos;
		}

		if (ukIznos < 0) {
			lblUkIznos.setForeground(display.getSystemColor(SWT.COLOR_RED));
		} else {
			lblUkIznos.setForeground(display
					.getSystemColor(SWT.COLOR_DARK_GREEN));
		}
		lblUkIznos.setText(FormatCur(String.valueOf(Round(ukIznos, 2))));

		// initial coloring
		for (TableItem singleItem : table.getItems()) {
			applyColors(singleItem);
		}

		cmdIspis = new Button(group2, SWT.PUSH);
		cmdIspis.setText(getLabel("cmdIspis"));
		cmdIspis.setLayoutData("width 105px,split 4");
		cmdIspis.addListener(SWT.Selection, new Listener() {

			public void handleEvent(Event e) {
				p2.show();
				JasperTest.ispis(getLabel("mess29") + (dtp.getMonth() + 1)
						+ "/" + dtp.getYear(), RSet.getOsoba(getgUserID()),
						SQLquery, 0);
				// new Categories();
				p2.hide();
			}

		});

		cmdExp = new Button(group2, SWT.PUSH);
		cmdExp.setText(getLabel("cmdExp"));
		cmdExp.setLayoutData("width 105px");
		cmdExp.addListener(SWT.Selection, new Listener() {
			private String fileName;

			public void handleEvent(Event e) {
				p2.show();
				String filePath = "";
				filePath = saveDialog.open(shell);
				fileName = saveDialog.getFileName();

				if (iNull(filePath) != "") {
					if (fileExists(filePath) == true) {
						// int odg =
						// ALERT.open(shell,"i","Izvoz datoteke!","Datoteka \""+
						// saveDialog.getFilePath()+
						// "\" veæ postoji!\nŽelite li presnimiti datoteku!","Presnimi|Odustani",
						// 0);

						int odg = ALERT
								.show(getLabel("mess2N"), "?", getLabel("mess2T") + " " + saveDialog.getFilePath() + "\"", OpalDialogType.OK_CANCEL, "", ""); //$NON-NLS-7$

						if (odg == 0) {
							if (iNull(filePath) != "") {
								JasperTest.export(
										getLabel("mess29")
												+ (dtp.getMonth() + 1) + "/"
												+ dtp.getYear(),
										RSet.getOsoba(getgUserID()), SQLquery,
										0, saveDialog.getFileType(), filePath,
										fileName);
							}
							p2.hide();
						} else {
							p2.hide();
						}
					} else {
						if (iNull(filePath) != "") {
							JasperTest.export(
									getLabel("mess30") + (dtp.getMonth() + 1)
											+ "/" + dtp.getYear(),
									RSet.getOsoba(getgUserID()), SQLquery, 0,
									saveDialog.getFileType(), filePath,
									fileName);
							// ALERT.open(shell, "i", appName +" - "
							// + "Izvoz datoteke...", "Izvoz datoteka \""
							// + fileName + "\" uspješno izvršen!",
							// "Nastavak", 0);

							kovaDialog.inform(appName + " - "
									+ getLabel("mess31"), getLabel("mess32")
									+ fileName + getLabel("mess33"));
							p2.hide();
						}
						p2.hide();
					}
					p2.hide();
				}
				p2.hide();
			}
		});

		// lblStatus = new Label(shell,SWT.NONE);
		// lblStatus.setBackground(display.getSystemColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT));
		// lblStatus.setLayoutData("south,width " + shell.getBounds().width
		// +",top "+ shell.getBounds().height);

		compStatus = new Composite(shell, SWT.NONE);
		compStatus.setBackground(display.getSystemColor(SWT.COLOR_WHITE));
		compStatus.setLayout(layoutStatus);
		compStatus.setLayoutData("south,width " + (shell.getBounds().width)
				+ ",top " + shell.getBounds().height + ",split 4");

		lblStatus = new Label(compStatus, SWT.NONE);
		lblStatus.setBackground(display.getSystemColor(SWT.COLOR_WHITE));
		lblStatus.setLayoutData("width 240");

		lblStatusIcon = new Label(compStatus, SWT.NONE);
		lblStatusIcon.setBackground(display.getSystemColor(SWT.COLOR_WHITE));
		lblStatusIcon.setLayoutData("width 10");
		// lblStatusIcon.setImage(createIcon(blueish));
		// lblStatusIcon.setImage(new
		// Image(display,"library/icons/Public.ico"));

		lblStatus2 = new Label(compStatus, SWT.NONE);
		lblStatus2.setBackground(display.getSystemColor(SWT.COLOR_WHITE));
		lblStatus2.setLayoutData("width 90,center");
		lblStatus2.setText(RSet.getOsoba(getgUserID()));

		lblStatusIcon = new Label(compStatus, SWT.NONE);
		lblStatusIcon.setBackground(display.getSystemColor(SWT.COLOR_WHITE));
		lblStatusIcon.setLayoutData("width 10");
		// lblStatusIcon.setImage(createIcon(blueish));
		// lblStatusIcon.setImage(new
		// Image(display,"library/icons/Calendar.ico"));

		lblStatus3 = new Label(compStatus, SWT.NONE);
		lblStatus3.setBackground(display.getSystemColor(SWT.COLOR_WHITE));
		lblStatus3.setLayoutData("width 100");
		// lblStatus3.setText(RSet.getOsoba(getgUserID()));
		lblStatus3.setText(sdf.format(new Date(System.currentTimeMillis())));

		final int time = 1000;
		Runnable timer = new Runnable() {
			public void run() {
				lblStatus3.setText(sdf.format(new Date(System
						.currentTimeMillis())));
				display.timerExec(time, this);
			}
		};
		display.timerExec(time, timer);

		Image image = null;
		String sPrava;
		switch (login.getgPrava()) {
		case 0:
			sPrava = getLabel("RIGHTS0");
			break;
		case 1:
			sPrava = getLabel("RIGHTS1");
			break;
		case 2:
			sPrava = getLabel("RIGHTS2");
			break;
		case 3:
			sPrava = getLabel("RIGHTS3");
			break;
		default:
			sPrava = getLabel("RIGHTS0");
			break;
		}

		final ToolTip tip = new ToolTip(shell, SWT.BALLOON
				| SWT.ICON_INFORMATION);
		tip.setMessage(getLabel("welcome2") + " " + RSet.getOsoba(getgUserID())
				+ " " + getLabel("welcome3") + " " + appName + " "
				+ getLabel("welcome4") + appVer + "\n " + getLabel("welcome5")
				+ RSet.getSQLDBase() + "\n " + getLabel("welcome6") + getDate()
				+ "\n " + getLabel("welcome7") + sPrava);
		tray = display.getSystemTray();

		if (tray != null) {
			TrayItem item = new TrayItem(display.getSystemTray(), SWT.NONE);
			// image = new Image(display, "library/icons/boue.ico");
			item.setImage(image);
			tip.setText(appName + "...");
			item.setToolTip(tip);

			final Menu menu = new Menu(shell, SWT.POP_UP);
			MenuItem mi = new MenuItem(menu, SWT.PUSH);
			mi.setText(getLabel("notif"));

			// MenuItem sep = new MenuItem(menu, SWT.SEPARATOR);

			MenuItem mi2 = new MenuItem(menu, SWT.PUSH);
			mi2.setText(getLabel("notif2"));

			item.addListener(SWT.MenuDetect, new Listener() {
				public void handleEvent(Event event) {
					menu.setVisible(true);
				}
			});

			item.addListener(SWT.Show, new Listener() {
				public void handleEvent(Event event) {
				}
			});
			item.addListener(SWT.Hide, new Listener() {
				public void handleEvent(Event event) {
				}
			});
			item.addListener(SWT.Selection, new Listener() {
				public void handleEvent(Event event) {
				}
			});

		} else {
			tip.setText("Notification from anywhere");
			tip.setLocation(400, 400);
		}
		tip.setVisible(true);
	}

	private static void freshTable() {
		String EOMdate = getEOM(dtp.getYear(), dtp.getMonth(), dtp.getDay());

		SQLquery = "select C.name,R.ID,R.Datum,R.Mjesto,R.Svrha,R.Tip,R.pintrans,C.R,C.G,C.B,R.Iznos from racuni R left OUTER JOIN "
				+ " categories C on R.kategorija=C.ID"
				+ " where R.datum between "
				+ "'"
				+ dtp.getYear()
				+ "-"
				+ (dtp.getMonth() + 1) + "-1' and '" + EOMdate + "' "; // +

		System.out.println(SQLquery);

		rs = RSet.openRS(SQLquery);
		val = kovaTable.fillVal(rs);

		table.resetValues(kovaTable.fillVal(rs));

		if (flgSort == true) {
			table.lastDescendingColumn = null;
		} else {
			table.lastDescendingColumn = colSort;
		}

		fillKategs();
		table.sortTable(colSort);
		table.applyFilter();
		//

		// sum
		ukIznos = 0;
		for (TableItem cIznos : table.getItems()) {
			double iznos = Double.parseDouble(cIznos.getText(10));
			ukIznos = ukIznos + iznos;
			ukIznos = Round(ukIznos, 2);

		}

		if (ukIznos < 0) {
			lblUkIznos.setForeground(display.getSystemColor(SWT.COLOR_RED));
		} else {
			lblUkIznos.setForeground(display
					.getSystemColor(SWT.COLOR_DARK_GREEN));
		}

		lblUkIznos.setText(FormatCur(String.valueOf(ukIznos)));
		cmdDEL.setEnabled(false);

	}

	// ureðivanje boja i uvjeta za bojanje
	static void applyColors(final TableItem target) {

		if (Double.parseDouble(target.getText(10)) > 0) {
			target.setForeground(target.getDisplay().getSystemColor(
					SWT.COLOR_DARK_GREEN));
		}
		if (iRed == 0) {
			if (target.getText(7) != "") {
				try {
					icon = createIcon(new Color(shell.getDisplay(), new RGB(
							Integer.parseInt(target.getText(7)),
							Integer.parseInt(target.getText(8)),
							Integer.parseInt(target.getText(9)))));
				} catch (Exception e) {
					e.printStackTrace();
					logger.loggErr("mainFrame " + e.getMessage());
					kovaDialog.showException(e);
				}
				target.setImage(0, icon);
			} else {
				target.setText("");
			}

			target.setBackground(white);
			iRed = 1;
		} else {
			if (target.getText(7) != "") {
				icon = createIcon(new Color(shell.getDisplay(), new RGB(
						Integer.parseInt(target.getText(7)),
						Integer.parseInt(target.getText(8)),
						Integer.parseInt(target.getText(9)))));
				target.setImage(0, icon);
			} else {
				target.setText("");
			}
			target.setBackground(blueish);
			iRed = 0;
		}

	}

	@Override
	public void focusGained(FocusEvent e) {
		if (e.widget instanceof Text) {
			Text t = (Text) e.widget;
			t.setBackground(display.getSystemColor(SWT.COLOR_INFO_BACKGROUND));
			t.selectAll();

			// }else if (e.widget instanceof TextAssist){
			// Text ta = (Text) e.widget;
			// ta.setBackground(display.getSystemColor(SWT.COLOR_INFO_BACKGROUND));
			// ta.selectAll ();

		} else if (e.widget instanceof kovaTable) {

		}

	}

	@Override
	public void focusLost(FocusEvent e) {
		String UK = "0,00";

		if (e.widget instanceof Text) {
			Text t = (Text) e.widget;
			t.setBackground(display.getSystemColor(SWT.COLOR_WHITE));

			if (e.widget == txtIznos || e.widget == txtBrRata) {
				if (optIndex == 2) {
					UK = String.valueOf(Round(getReal(txtIznos.getText())
							/ getReal(txtBrRata.getText()), 2));

				} else if (optIndex != 0 && e.widget == txtIznos) {
					iznNeg = 0;
					iznNeg = Math.abs(getReal(txtIznos.getText()));
					iznNeg = (iznNeg * -1);

					txtIznos.setText(String.valueOf(iznNeg));

				} else if (optIndex == 0 && e.widget == txtIznos) {
					iznNeg = 0;
					iznNeg = Math.abs(getReal(txtIznos.getText()));
					iznNeg = Math.abs(iznNeg);
					txtIznos.setText(String.valueOf(iznNeg));
				}

				txtIznos.setText(FormatCur(txtIznos.getText()));
				txtUkupRata.setText(FormatCur(UK));
			}

			// }else if (e.widget instanceof TextAssist){
			// Text ta = (Text) e.widget;
			// ta.setBackground(display.getSystemColor(SWT.COLOR_WHITE));

		} else if (e.widget instanceof Table) {
			Table tbl = (Table) e.widget;
			cmdDEL.setEnabled(false);
		}

	}

	public void keyPressed(KeyEvent e) {

		if (e.widget instanceof Text) {
			Text t = (Text) e.widget;
			if (e.keyCode == 16777218) {// dolje
				t.traverse(SWT.TRAVERSE_TAB_NEXT);
			} else if (e.keyCode == 16777217) {// gore
				t.traverse(SWT.TRAVERSE_TAB_PREVIOUS);
			}

			// }else if (e.widget instanceof TextAssist){
			// TextAssist ta = (TextAssist) e.widget;
			//
			// if (e.keyCode==16777218){//dolje
			// System.out.println("dolje");
			// ta.traverse(SWT.TRAVERSE_TAB_NEXT);
			// }else if (e.keyCode==16777217){//gore
			// System.out.println("gore");
			// ta.traverse(SWT.TRAVERSE_TAB_PREVIOUS);
			// }

		} else if (e.widget instanceof Table) {
			Table tbl = (Table) e.widget;
		}

	}

	// @Override
	// public void keyReleased(KeyEvent e) {
	// if(e.widget==txtSvrha)
	// setAutoCompletion(txtSvrha,"svrha","racuni");
	//
	// // if(e.widget==txtMjesto)
	// // setAutoCompletion(txtMjesto,"mjesto","racuni");
	// }

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
			if (txtMjesto.getText().length() != 0 || txtMjesto.getText() != "")
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
	public void widgetDefaultSelected(SelectionEvent e) {
		System.out.println(e.widget);

	}

	@Override
	public void widgetSelected(SelectionEvent e) {
		if (e.widget instanceof TableItem) {
			TableColumn kolona = (TableColumn) e.widget;

			if (kolona == IDCol) {
				colSort = table.getColumn(0);
			} else if (kolona == colIznos) {
				colSort = colIznos;
			} else if (kolona == svrhaCol) {
				colSort = svrhaCol;
			} else if (kolona == datumCol) {
				colSort = datumCol;
			} else if (kolona == colCateg) {
				colSort = colCateg;
			} else if (kolona == mjestoCol) {
				colSort = mjestoCol;
			}
		}

	}

	class MenuItemListener extends SelectionAdapter {
		private Menu categMenu;

		public void widgetSelected(SelectionEvent e) {
			if (((MenuItem) e.widget).getText().equals("E&xit")) {
				shell.close();
			}

			if (e.widget == itemIzlaz) {
				p2.show();
				boolean confirm = kovaDialog.isConfirmed(getLabel("mess34"),
						getLabel("mess34txt"));
				System.out.println("Choice is..." + confirm);
				if (confirm == true) {
					System.exit(1);
					RSet.setOperOut(getgUserID());
					logger.logg(getLabel("logg10") + confirm);
				}
				p2.hide();

			} else if (e.widget == itemIspis) {
				p2.show();
				JasperTest.ispis(getLabel("jasper") + (dtp.getMonth() + 1)
						+ "/" + dtp.getYear(), RSet.getOsoba(getgUserID()),
						SQLquery, 0);
				p2.hide();

			} else if (e.widget == itemBalance) {
				p2.show();
				new balanceForm(shell, dtp.getMonth() + 1, dtp.getYear());
				p2.hide();

			} else if (e.widget == itemPregLogg) {
				p2.show();
				new loggerView(shell, 0);
				p2.hide();

			} else if (e.widget == itemPregErrLogg) {
				p2.show();
				new loggerView(shell, 1);
				p2.hide();

			} else if (e.widget == itemPregOper) {
				p2.show();
				new operPregledNew(shell);
				p2.hide();
				gBroj = 0;

			} else if (e.widget == itemOpcije) {
				p2.show();
				new opcije(shell);
				p2.hide();
				// setLanguage(shell);

			} else if (e.widget == itemBackup) {
				p2.show();
				new underConstruction(shell);
				p2.hide();

			} else if (e.widget == itemRestore) {
				p2.show();
				new underConstruction(shell);
				p2.hide();

			} else if (e.widget == itemIzvoz) {
				p2.show();
				String filePath = "";
				String fileName = "";

				filePath = saveDialog.open(shell);
				fileName = saveDialog.getFileName();

				if (iNull(filePath) != "") {
					if (fileExists(filePath) == true) {
						// int odg = ALERT.open(shell, "i", "Izvoz datoteke!",
						// "Datoteka  veæ postoji!\nŽelite li presnimiti datoteku \""+
						// saveDialog.getFilePath()+ "\"?", "Presnimi|Odustani",
						// 0);
						int odg = ALERT.show(
								getLabel("mess35"),
								"i",
								getLabel("mess35txt")
										+ saveDialog.getFilePath() + "\"?",
								OpalDialogType.OK_CANCEL, "", "");

						if (odg == 0) {
							if (iNull(filePath) != "") {
								JasperTest.export(
										getLabel("jasper2")
												+ (dtp.getMonth() + 1) + "/"
												+ dtp.getYear(),
										RSet.getOsoba(getgUserID()), SQLquery,
										0, saveDialog.getFileType(), filePath,
										fileName);
								p2.hide();
							}
							p2.hide();
						}
						p2.hide();
					} else {
						if (iNull(filePath) != "") {
							JasperTest.export(
									getLabel("jasper3") + (dtp.getMonth() + 1)
											+ "/" + dtp.getYear(),
									RSet.getOsoba(getgUserID()), SQLquery, 0,
									saveDialog.getFileType(), filePath,
									fileName);
						}
						p2.hide();
					}

					p2.hide();
				}
				p2.hide();

			} else if (e.widget == itemKategorije) {
				p2.show();
				tempSelect = table.getSelectionIndex();
				gBroj = IDbroj;
				new Categories(shell);
				p2.hide();
				makePopUp();
				freshTable();
				table.contentTable.select(tempSelect);
				table.contentTable.showSelection();

			} else if (e.widget == itemMonth) {
				p2.show();
				final graphMonth graph = new graphMonth(getLabel("jasper4")
						+ (dtp.getMonth() + 1) + getLabel("jasper4txt"), 0,
						dtp.getMonth() + 1);
				graph.pack();
				RefineryUtilities.centerFrameOnScreen(graph);
				graph.setVisible(true);
				p2.hide();

			} else if (e.widget == itemMonth2) {
				p2.show();
				final graphMonth graph2 = new graphMonth(getLabel("jasper5")
						+ (dtp.getMonth() + 1) + getLabel("jasper5txt"), 1,
						dtp.getMonth() + 1);
				graph2.pack();
				RefineryUtilities.centerFrameOnScreen(graph2);
				graph2.setVisible(true);
				p2.hide();

			} else if (e.widget == itemYear) {
				p2.show();
				graphYear demo = new graphYear(getLabel("jasper6"));
				demo.pack();
				RefineryUtilities.centerFrameOnScreen(demo);
				demo.setVisible(true);
				p2.hide();

			} else if (e.widget == itemAppLogg) {
				p2.show();
				new appLogg(shell);
				p2.hide();

			} else if (e.widget == itemAbout) {
				p2.show();
				new about(shell);
				p2.hide();

			} else if (e.widget == itemManual) {
				p2.show();
				// Process p;
				// try {
				// p =
				// Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler /library/upute/Upute.pdf");
				// p.waitFor();
				// } catch (IOException e1) {
				// // TODO Auto-generated catch block
				// e1.printStackTrace();
				// kovaDialog.showException(e1);
				// } catch (InterruptedException e2) {
				// // TODO Auto-generated catch block
				// e2.printStackTrace();
				// kovaDialog.showException(e2);
				// }

				try {
					File myFile = new File("library/upute/Upute.pdf");
					Desktop.getDesktop().open(myFile);
				} catch (IOException ex) {
					// no application registered for PDFs
				}

				p2.hide();

			}

		}
	}

	private static String getColVal(int kolona) {
		String val = "";

		if (table.contentTable.getSelectionCount() > 0) {
			val = table.getItem(table.contentTable.getSelectionIndex())
					.getText(kolona);
		}

		return val;
	}

	@Override
	public void mouseDoubleClick(MouseEvent e) {
		p2.show();
		tempSelect = table.getSelectionIndex();
		gBroj = IDbroj;
		new editForm(shell);
		p2.hide();
		freshTable();
		table.contentTable.select(tempSelect);
		table.contentTable.showSelection();
	}

	@Override
	public void mouseDown(MouseEvent e) {
		// System.out.println(table.getSelectionIndex());
	}

	@Override
	public void mouseUp(MouseEvent arg0) {
		// TODO Auto-generated method stub

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

		final Image icon = new Image(display, imageData, maskData);
		return icon;
	}

	private static void makePopUp() {
		Menu popupMenu = new Menu(table.contentTable);

		MenuItem itemCateg = new MenuItem(popupMenu, SWT.CASCADE);
		itemCateg.setText(getLabel("Categories"));

		MenuItem itemEdit = new MenuItem(popupMenu, SWT.NONE);
		itemEdit.setText(getLabel("Edit"));
		itemEdit.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				tempSelect = table.getSelectionIndex();
				gBroj = IDbroj;
				tempIDbroj = IDbroj;
				p2.show();
				new editForm(shell);
				p2.hide();
				freshTable();
				table.contentTable.select(tempSelect);
				table.contentTable.showSelection();

			}
		});

		MenuItem itemDel = new MenuItem(popupMenu, SWT.NONE);
		itemDel.setText(getLabel("Delete"));
		itemDel.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {

				int tip = Integer.parseInt(getColVal(5));

				if (tip == 2) {
					// int odg =
					// ALERT.open(shell,"?","Brisanje?","Želite li obrisati rate?","Sve rate|Jednu ratu|Odustani",2);

					int odg = kovaDialog.choice(getLabel("mess36"), "", 1,
							new ChoiceItem(getLabel("ch36"),
									getLabel("ch36txt")), new ChoiceItem(
									getLabel("ch361"), getLabel("ch361txt")),
							new ChoiceItem(getLabel("ch362"),
									getLabel("ch363txt")));

					int rateBr = 0;

					switch (odg) {
					case 0: // sve rate

						rs = RSet.openRS("select max(id)as Uk from racuni");

						try {
							while (rs.next()) {
								rateBr = rs.getInt("uk");
							}
						} catch (SQLException e1) {
							e1.printStackTrace();
							logger.loggErr("mainFrame " + e1.getMessage());
							kovaDialog.showException(e1);
						}
						RSet.closeAll();
						RSet.deleteRS(rateBr, sTablica, "rateID");
						freshTable();
						break;
					case 1: // jednu ratu
						RSet.deleteRSid(IDbroj, sTablica);
						freshTable();
						break;
					case 2: // odustani

						break;
					}
				} else {

					// int odg =
					// ALERT.open(shell,"?","Brisanje?","Želite li obrisati stavku pod rednim brojem "+
					// IDbroj + "?\n\n Mjesto: " + iNull(getColVal(3)) +
					// "\n Svrha: " + iNull(getColVal(4))+ "\n Iznos: "+
					// FormatCur(iNull0(getColVal(10)))+ "", "Obriši|Odustani",
					// 1);

					final boolean confirm = kovaDialog.isConfirmed(
							getLabel("mess38") + IDbroj + "?",
							getLabel("mess38txt") + iNull(getColVal(3))
									+ getLabel("mess381txt")
									+ iNull(getColVal(4))
									+ getLabel("mess382txt") + "<b>"
									+ FormatCur(iNull0(getColVal(10)))
									+ "</b><br/>");

					if (confirm == true) {
						RSet.deleteRSid(IDbroj, sTablica);
						freshTable();
						cmdDEL.setEnabled(false);
						logger.logg(getLabel("mess39") + IDbroj + "!");
					} else {
						cmdDEL.setEnabled(false);
					}
				}

			}
		});

		categMenu = new Menu(popupMenu);
		itemCateg.setMenu(categMenu);

		int i = 1;
		rs = RSet.openRS("select id,name,r,g,b from categories");
		try {
			while (rs.next()) {
				itemC = new MenuItem(categMenu, SWT.NONE);
				itemC.setText(rs.getString("name"));
				itemC.setData(rs.getInt("id"));
				itemC.setImage(createIcon(new Color(shell.getDisplay(),
						new RGB(rs.getInt("R"), rs.getInt("G"), rs.getInt("B")))));
				itemC.addSelectionListener(new SelectionAdapter() {
					public void widgetSelected(SelectionEvent e) {
						MenuItem item = (MenuItem) e.widget;

						tempSelect = table.getSelectionIndex();
						if (getColVal(7) == "") {
							RSet.updateRS("update racuni set kategorija= "
									+ item.getData() + " where ID=" + IDbroj);
						} else {
							RSet.updateRS("update racuni set kategorija=0 where ID="
									+ IDbroj);
						}
						freshTable();
						table.contentTable.select(tempSelect);
						table.contentTable.showSelection();

					}
				});
				i++;
			}
			rs.close();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			logger.loggErr("mainFrame " + e1.getMessage());
			kovaDialog.showException(e1);
		}

		MenuItem itemCategories = new MenuItem(categMenu, SWT.NONE);
		itemCategories.setText(getLabel("Edit"));
		// final Image icos= new Image(display,"library/icons/icos.ico");
		// itemCategories.setImage(icos);
		itemCategories.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				p2.show();
				new Categories(shell);
				p2.hide();
				tempSelect = table.getSelectionIndex();
				makePopUp();
				freshTable();
				table.contentTable.select(tempSelect);
				table.contentTable.showSelection();

			}
		});

		table.contentTable.setMenu(popupMenu);

	}

	private static void fillKategs() {
		rs = RSet.openRS("select name,R,G,B from categories order by name");
		try {
			cmbKategs.removeAll();
			cmbKategs.add(getLabel("NoCateg"), null);

			while (rs.next()) {
				cmbKategs
						.add(rs.getString("name"),
								createIcon(new Color(shell.getDisplay(),
										new RGB(rs.getInt("R"), rs.getInt("G"),
												rs.getInt("B")))));
			}

			cmbKategs.select(0);

			rs.close();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			logger.loggErr("mainFrame " + e1.getMessage());
			kovaDialog.showException(e1);
		}
	}

	private static kovaTable fillKovaTable(final Composite parent) {

		// može ovako, a može se i iz metaData-e izèitati
		final String[] header = { "", "ID", "Datum", "Mjesto", "Svrha", "tip",
				"Pintrans", "", "", "", "Iznos" };

		final String EOMdate = getEOM(dtp.getYear(), dtp.getMonth(),
				dtp.getDay());

		SQLquery = "select C.name,R.ID,R.Datum,R.Mjesto,R.Svrha,R.Tip,R.pintrans,C.R,C.G,C.B,R.Iznos from racuni R left OUTER JOIN "
				+ " categories C on R.kategorija=C.ID"
				+ " where R.datum between "
				+ "'"
				+ dtp.getYear()
				+ "-"
				+ (dtp.getMonth() + 1) + "-1' and '" + EOMdate + "' ";

		System.out.println("FILL: " + SQLquery);

		rs = RSet.openRS(SQLquery);
		val = kovaTable.fillVal(rs);

		// create the table itself
		final kovaTable table = kovaTable.createKovaTable(parent,
				SWT.FULL_SELECTION | SWT.MULTI, header, val);
		table.setLayoutData("width 470px,height 345px,span");

		// kovaTable.setColumnsMeta(rs,table);
		// table.setEditable(false, 1, 2, 3);

		// initial coloring
		for (TableItem singleItem : table.getItems()) {
			applyColors(singleItem);
		}

		table.sortTable(table.getColumn(1));

		return table;
	}

	private static void setSort() {
		flgSort = (flgSort) ? false : true;
		table.lastDescendingColumn = (flgSort) ? colSort : null;

	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}

}
