import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import net.miginfocom.swt.MigLayout;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

public class loggerView {
	MigLayout layout = new MigLayout("", "", "");
	MigLayout layout2 = new MigLayout("", "[][]20[][][][]", "");
	MigLayout layout3 = new MigLayout("");
	// private static String filepath = "src/logg/logg.xml";
	private static String filepath;
	static String string = "";
	static Display display;
	static Shell shell;
	static Tree tree;

	Element rootNode;
	Element node0;
	Element node1;
	Element node2;

	Button chkMoje;
	Label lbl;

	List<Object> list0;
	List<Object> list1;
	List<Object> list2;

	String sOperater = RSet.getOsoba(DEF.getgUserID()) + "_" + DEF.getgUserID();

	public loggerView(Shell parent, int tip) {

		shell = new Shell(parent, SWT.APPLICATION_MODAL | SWT.DIALOG_TRIM);
		display = shell.getDisplay();

		if (tip == 0) {
			shell.setText(DEF.getLabel("LogPrew") + " - (" + getFilepath()
					+ ")");
			// setFilepath(filepath = DEF.getgLoggPath()+ sOperater + ".xml");
			filepath = DEF.getgLoggPath();
		} else {
			shell.setText(DEF.getLabel("LogPrew") + " - (" + getFilepath()
					+ ")");
			// setFilepath(DEF.getgLoggErrPath());
			filepath = DEF.getgLoggErrPath();
			System.out.println(filepath);
		}

		shell.setBackground(display.getSystemColor(SWT.COLOR_WHITE));
		shell.setSize(550, 560);
		DEF.CenterScreen(shell, display);
		shell.setLayout(layout);

		sOperater = sOperater.replace(" ", "_");

		createWidgets();

		shell.addListener(SWT.Close, new Listener() {
			@Override
			public void handleEvent(Event event) {
				// int style = SWT.APPLICATION_MODAL | SWT.YES | SWT.NO;
				// MessageBox messageBox = new MessageBox(shell, style);
				// messageBox.setText("Information");
				// messageBox.setMessage("Close the shell?");
				// event.doit = messageBox.open() == SWT.YES;
				DEF.gBroj = 0;
				// shell.dispose();

			}
		});

		shell.open();

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
	}

	public static void main(String[] args) throws SQLException {
		new loggerView(shell, 0);
	}

	private void createWidgets() {

		Group group = new Group(shell, SWT.NONE);
		group.setText("Filter");
		group.setBackground(display.getSystemColor(SWT.COLOR_WHITE));
		group.setLayoutData("span,growx,wrap");
		group.setLayout(layout2);

		lbl = new Label(group, SWT.NONE);
		lbl.setText("Odabir datuma:");
		lbl.setLayoutData("width 30px");
		lbl.setBackground(display.getSystemColor(SWT.COLOR_WHITE));

		DateTime dtp = new DateTime(group, SWT.DATE | SWT.DROP_DOWN);
		dtp.setLayoutData("width 100px");

		lbl = new Label(group, SWT.NONE);
		lbl.setText("Vrijeme od:");
		lbl.setLayoutData("width 30px");
		lbl.setBackground(display.getSystemColor(SWT.COLOR_WHITE));

		DateTime time1 = new DateTime(group, SWT.TIME | SWT.DROP_DOWN);
		time1.setLayoutData("");

		lbl = new Label(group, SWT.NONE);
		lbl.setText("-");
		lbl.setLayoutData("");
		lbl.setBackground(display.getSystemColor(SWT.COLOR_WHITE));

		DateTime time2 = new DateTime(group, SWT.TIME | SWT.DROP_DOWN);
		time2.setLayoutData("wrap");

		Group group2 = new Group(shell, SWT.NONE);
		group2.setText("Pregled");
		group2.setBackground(display.getSystemColor(SWT.COLOR_WHITE));
		group2.setLayoutData("span,wrap");
		group2.setLayout(layout3);

		tree = new Tree(group2, SWT.NONE);
		tree.setLayoutData("width 500px, height 400px");

		createXML(1);

		// chkMoje = new Button (shell, SWT.CHECK);
		// chkMoje.setSelection(true);
		// chkMoje.setText("Prikaži samo moje");
		// chkMoje.setLayoutData("width 50px");
		// chkMoje.setBackground(display.getSystemColor(SWT.COLOR_WHITE));
		// chkMoje.addListener (SWT.Selection, new Listener() {
		// @Override
		// public void handleEvent (Event e) {
		// if (chkMoje.getSelection()) {
		// // System.out.println("Selected");
		// createXML(1);
		// } else {
		// // System.out.println("Not selected");
		// createXML(0);
		// }
		// }
		// });
		//
	}

	private void createXML(int tip) {
		tree.removeAll();
		SAXBuilder builder = new SAXBuilder();

		System.out.println(getFilepath());

		File xmlFile = new File(filepath);
		try {

			Document document = builder.build(xmlFile);
			rootNode = document.getRootElement();
			// System.out.println(rootNode.getChildren());

			if (tip == 1) {
				list0 = rootNode.getChildren();

			} else {
				list0 = rootNode.getChildren();
			}

			for (int x = 0; x < list0.size(); x++) {
				node0 = (Element) list0.get(x);

				System.out.println(node0.getName());

				list1 = node0.getChildren();
				TreeItem item0 = new TreeItem(tree, 0);
				item0.setText(node0.getName().toString());

				for (int i = 0; i < list1.size(); i++) {
					node1 = (Element) list1.get(i);

					System.out.println("\t" + node1.getName());
					TreeItem item1 = new TreeItem(item0, 0);
					item1.setText(node1.getValue().toString());

					list2 = node1.getChildren();
					System.out.println(list2.size());
					//
					// for (int j = 0; j < list2.size(); j++) {
					// node2 = (Element) list2.get(j);
					// TreeItem item2 = new TreeItem(item1, 0);
					// item2.setText(node2.getValue().toString());
					//
					//
					// //ispis u konzoli
					// // System.out.print("\t\t<" + node2.getName() + ">");
					// // System.out.print(" " + node2.getValue() + " ");
					// // System.out.println("</" + node2.getName() + ">");
					// }
				}
			}

		} catch (IOException io) {
			logger.loggErr("loggerView " + io.getMessage());

		} catch (JDOMException jdomex) {
			logger.loggErr("loggerView " + jdomex.getMessage());
			// System.out.println(jdomex.getMessage());
		}
		// selection listener
		tree.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event e) {
				TreeItem[] selection = tree.getSelection();
				for (int i = 0; i < selection.length; i++)
					string += selection[i] + " ";
				// System.out.println(string);
			}
		});

		Menu menu = new Menu(shell);
		MenuItem menuItem = new MenuItem(menu, SWT.PUSH);
		menuItem.setText("Copy");

		tree.setMenu(menu);
	}

	public static void setFilepath(String filepath) {
		loggerView.filepath = filepath;
	}

	public static String getFilepath() {
		return filepath;
	}

}
