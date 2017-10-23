import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.EventObject;

import kovaUtils.OpalDialog.kovaDialog;
import kovaUtils.OpalDialog.kovaDialog.OpalDialogType;
import net.miginfocom.swt.MigLayout;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.swt.widgets.TreeItem;

public class appLogg extends DEF {

	static Shell shell;
	static Display display;
	static Display display2;
	static Image icona;
	static Color color;

	static SimpleDateFormat sdf2 = new SimpleDateFormat(
			"dd.MMMM.yyyy , HH:mm:ss");

	MigLayout layout = new MigLayout("");
	MigLayout layout2 = new MigLayout("");
	MigLayout layout3 = new MigLayout("");
	MigLayout layoutP = new MigLayout("");
	MigLayout layoutP2 = new MigLayout("");
	MigLayout layoutU = new MigLayout("");
	MigLayout layoutU2 = new MigLayout("");

	private Label sep;
	private Composite compPreg;
	private Composite compUnos;

	private Composite comp;
	private Composite comp2;
	private Composite comp3;

	private String htmlContent;
	private String iconsDirPath = "library/icons";

	final String menuHeaderName = "Edit";
	final boolean preventTypingOfHTMLTags = true;
	final int compositeStyle = SWT.BORDER;
	final int toolBarStyle = SWT.NULL;
	final int styledTextStyle = SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL
			| SWT.WRAP;
	private ResultSet rs;
	private TreeItem root;
	private Button cmdVerzija;
	private TreeItem ti;
	private EventObject event;
	private TreeColumn tc1;
	private TreeColumn tc2;
	private TreeColumn tc3;
	private TreeColumn tc4;
	private ResultSet rs2;
	private TreeColumn tc;
	private StyledText txtOpisPreg;
	private Spinner spin;
	private Tree tree;
	private Button cmdUnos;
	private Button cmdBrisi;
	static Image image;
	static Button cmdSpremi;
	static Label lbl;
	static Label lblOper;

	static Composite compP;
	static Composite compP2;

	static Composite compU;
	static Composite compU2;
	static Group group;
	static Label lblTime;
	static Text txtNaslov;
	static Label lblVer;
	static StyledText txtOpis;
	static int[] percents;
	private int IDbroj;
	private Button cmdUredi;

	public appLogg(Shell parent) {
		shell = new Shell(parent, SWT.APPLICATION_MODAL | SWT.DIALOG_TRIM);
		display = shell.getDisplay();
		display2 = display;

		shell.setSize(500, 550);
		shell.setText(DEF.getLabel("appLogg") + " - " + DEF.appName + " - "
				+ DEF.appVer);

		icona = new Image(display, "library/icons/Boue.ico");
		image = new Image(display, "library/icons/boue48.ico");
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
		CenterScreen(shell, display);
		shell.setBackground(display.getSystemColor(SWT.COLOR_WHITE));

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
	}

	private void createWidgets() {

		TabFolder tabFolder = new TabFolder(shell, SWT.NONE);
		tabFolder.setLayout(layout2);
		tabFolder.setLayoutData("height 450,wrap");

		// TabItem item = new TabItem (tabFolder, SWT.NULL);
		// item.setText("Unos loggova");
		// item.setControl(compUnos);
		//
		//

		compPreg = new Composite(tabFolder, SWT.NONE);
		compPreg.setBackground(display.getSystemColor(SWT.COLOR_WHITE));
		compPreg.setLayout(layoutP);
		compPreg.setLayoutData("");

		TabItem item2 = new TabItem(tabFolder, SWT.NULL);
		item2.setText("Pregled loggova");
		item2.setControl(compPreg);

		tree = new Tree(compPreg, SWT.BORDER | SWT.V_SCROLL
				| SWT.FULL_SELECTION);
		tree.setHeaderVisible(true);
		tree.setLinesVisible(true);
		tree.setLayoutData("height 250,width 400,wrap");

		tc = new TreeColumn(tree, SWT.NONE);
		tc.setText("Ver.\\ID");
		tc.setWidth(70);

		tc1 = new TreeColumn(tree, SWT.NONE);
		tc1.setText("Datum");
		tc1.setWidth(70);

		tc2 = new TreeColumn(tree, SWT.NONE);
		tc2.setText("Operater");
		tc2.setWidth(90);

		tc3 = new TreeColumn(tree, SWT.NONE);
		tc3.setText("Naziv");
		tc3.setWidth(160);

		tc4 = new TreeColumn(tree, SWT.NONE);
		tc4.setText("%");
		tc4.setWidth(70);

		freshTree();
		tree.addSelectionListener(new SelectionListener() {

			public void widgetDefaultSelected(SelectionEvent e) {
			}

			public void widgetSelected(SelectionEvent e) {
				if (tree.getSelectionCount() == 0)
					return;
				TreeItem sel = tree.getSelection()[0];

				if (isANumber(sel.getText(0))) {
					IDbroj = Integer.parseInt(sel.getText(0));

					rs = RSet.openRS("select top 1 opis from appLogg where id="
							+ IDbroj);

					try {
						while (rs.next()) {
							txtOpisPreg.setText(rs.getString("opis"));
						}
						rs.close();
					} catch (SQLException e2) {
						e2.printStackTrace();
					}
				}

			}

		});

		// root.setExpanded(true);

		group = new Group(compPreg, SWT.None);
		group.setText(getLabel("GR1"));
		group.setBackground(getColor(eColor.WHITE));
		group.setForeground(display.getSystemColor(SWT.COLOR_BLUE));
		group.setLayoutData("width 450, wrap");
		group.setLayout(layout3);

		txtOpisPreg = new StyledText(group, SWT.BORDER | SWT.V_SCROLL);
		txtOpisPreg.setTextLimit(500);
		txtOpisPreg.setLayoutData("height 120,width 430,wrap");
		txtOpisPreg.setWordWrap(true);

		tree.setSize(300, 330);

		cmdUnos = new Button(shell, SWT.PUSH);
		cmdUnos.setLayoutData("gapleft 50,width 100px,height 20px,split4");
		cmdUnos.setText(getLabel("cmdUnos"));
		cmdUnos.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				switch (e.type) {
				case SWT.Selection:
					new appLoggUnos(shell);
					freshTree();
					break;
				}
			}
		});

		cmdUredi = new Button(shell, SWT.PUSH);
		cmdUredi.setLayoutData("gapleft 10,width 100px,height 20px");
		cmdUredi.setText(getLabel("Edit"));
		cmdUredi.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				switch (e.type) {
				case SWT.Selection:
					gBroj = IDbroj;
					new appLoggUnos(shell);
					freshTree();
					break;
				}
			}
		});

		cmdBrisi = new Button(shell, SWT.PUSH);
		cmdBrisi.setLayoutData("gapleft 10,width 100px,height 20px");
		cmdBrisi.setText(getLabel("Delete"));
		cmdBrisi.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				switch (e.type) {
				case SWT.Selection:

					break;
				}
			}
		});

		cmdVerzija = new Button(shell, SWT.PUSH);
		cmdVerzija.setLayoutData("gapleft 10,width 100px,height 20px");
		cmdVerzija.setText("Promjena verzije!");
		cmdVerzija.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				switch (e.type) {
				case SWT.Selection:
					int odg = ALERT
							.show("Izmjena verzije aplikacije",
									"?",
									"Izmjenu aplikacije može napraviti kreator aplikacije<br/>Za daljnji rad je potrebno unijeti lozinku kreatora?<br/>Nastaviti?",
									OpalDialogType.OK_CANCEL,
									"Za nastavak potrebna lozinka kreatora aplikacije!",
									"");
					if (odg == 0) {
						String loz = kovaDialog.ask(shell,
								"Unos kreator lozinke", "Unesite lozinku", "");

						System.out.println(loz);
						if (loz != "" && loz != null) {
							if (loz.equals("skyline")) {
								String ver = kovaDialog.ask("Unos verziju!",
										"Unesite verziju aplikacije", "");

								if (ver != "" && ver != null) {
									setgVerzija(ver);
									writeINI("library/profiles/" + getgFile());
									readINI();
									// System.out.println(appVer);
									// lblVer.setText(appVer);
								}

							}
						}
					}
					break;
				}
			}
		});

		// RichTextEditorWidgetFeatureSet featureSet =
		// RichTextEditorWidgetFeatureSet.ALL_FEATURES;
		// IStyledTextWidgetImageCreator imageCreator = new
		// RichTextEditorWidgetBasicImageCreator(iconsDirPath, false);
		//
		// Composite parent = shell;
		// htmlTextEditorWidget = new
		// HTMLTextEditorWidget(featureSet,imageCreator,comp,null,preventTypingOfHTMLTags,compositeStyle,toolBarStyle,styledTextStyle);
		// htmlTextEditorWidget.setLayout(layout3);
		// htmlTextEditorWidget.setLayoutData("width 300,height 300");
		//
		// // Now set the content in your format (HTML in this case, but you
		// could write
		// // other converters for other content types)...
		// HTMLTextModel htmlTextModel = new HTMLTextModel(htmlContent);
		// htmlTextEditorWidget.setFormattedText(htmlTextModel);
		//

	}

	private void freshTree() {

		tree.removeAll();
		rs = RSet.openRS("select distinct ver from appLogg");

		try {
			while (rs.next()) {
				String verzija = rs.getString("ver");
				root = new TreeItem(tree, SWT.NONE);
				root.setText(verzija);

				rs2 = RSet
						.openRS("select id,naziv,opis,postotak,datum,(select ime+' '+prezime from operateri where id = appLogg.oper)as sOper from appLogg where ver= '"
								+ verzija + "'");
				try {
					while (rs2.next()) {
						Integer i = 0;
						ti = new TreeItem(root, SWT.NONE);
						ti.setText(new String[] {
								rs2.getString("id"),
								String.format(DATE_FORMAT, rs2.getDate("datum")),
								rs2.getString("sOper"), rs2.getString("naziv") });
						ti.setData(rs2.getInt("postotak"));
						i++;
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				rs2.close();
			}
			rs.close();
			root.setExpanded(true);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		tree.addListener(SWT.PaintItem, new Listener() {
			public void handleEvent(Event event) {
				int[] percents = new int[] { 50, 30, 5, 15 };

				if (event.index == 4) {
					TreeItem item = (TreeItem) event.item;
					TreeItem parent = item.getParentItem();
					if (parent != null) {
						GC gc = event.gc;
						int index = parent.indexOf(item);

						int percent = percents[index];

						int posto = (Integer) item.getData();

						Color foreground = gc.getForeground();
						Color background = gc.getBackground();
						gc.setForeground(getColor(eColor.RED));
						gc.setBackground(getColor(eColor.DARK_RED));
						int width = (tc4.getWidth() - 1) * posto / 100;
						gc.fillGradientRectangle(event.x, event.y, width,
								event.height, true);
						Rectangle rect2 = new Rectangle(event.x, event.y,
								width - 1, event.height - 1);
						gc.drawRectangle(rect2);
						gc.setForeground(getColor(eColor.BLUE));
						String text = posto + "%";
						Point size = event.gc.textExtent(text);
						int offset = Math.max(0, (event.height - size.y) / 2);
						gc.drawText(text, event.x + 2, event.y + offset, true);
						gc.setForeground(background);
						gc.setBackground(foreground);
					}
				}
			}
		});

		tree.addSelectionListener(new SelectionListener() {

			public void widgetDefaultSelected(SelectionEvent e) {
			}

			public void widgetSelected(SelectionEvent e) {
				if (tree.getSelectionCount() == 0)
					return;
				TreeItem sel = tree.getSelection()[0];

				if (isANumber(sel.getText(0))) {
					IDbroj = Integer.parseInt(sel.getText(0));

					rs = RSet.openRS("select top 1 opis from appLogg where id="
							+ IDbroj);

					try {
						while (rs.next()) {
							txtOpisPreg.setText(rs.getString("opis"));
						}
						rs.close();
					} catch (SQLException e2) {
						e2.printStackTrace();
					}
				}

			}

		});

	}

}
