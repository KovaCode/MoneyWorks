import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Locale;

import kovaUtils.OpalDialog.kovaDialog;
import kovaUtils.OpalDialog.kovaDialog.OpalDialogType;

import net.miginfocom.swt.MigLayout;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class opcije extends DEF implements SelectionListener {

	static Shell shell;
	static Composite comp;
	static Shell s;
	static Display display;
	MigLayout layout = new MigLayout("");
	MigLayout layout2 = new MigLayout("");
	MigLayout layout3 = new MigLayout("");
	MigLayout layoutComp = new MigLayout("");
	Image icona;

	static int gLogin;
	private static int jezik = 0;

	static Button cmdBrowse;
	static Button chkLogin;
	static Button cmdSpremi;
	static Combo cmbJezik;
	static Label lbl;
	static Text txtOpis;
	static Text txtPath;
	private Label sep;
	static Label lblOpis;

	static Group groupO;

	public opcije(Shell parent) {
		shell = new Shell(parent, SWT.APPLICATION_MODAL | SWT.DIALOG_TRIM);
		display = shell.getDisplay();

		shell.setSize(320, 225);
		shell.setText(getLabel("Option"));

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
		fillForm();
		// setLanguage(shell);

		// createControlButtons();

		shell.open();

		CenterScreen(shell, display);
		shell.setBackground(display.getSystemColor(SWT.COLOR_WHITE));

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}

	}

	public static void main(String[] args) {

	}

	private void createWidgets() {

		comp = new Composite(shell, SWT.NONE);
		comp.setLayoutData("width 310px,wrap");
		comp.setBackground(display.getSystemColor(SWT.COLOR_WHITE));
		comp.setLayout(layoutComp);

		chkLogin = new Button(comp, SWT.CHECK);
		chkLogin.setText(getLabel("chkLogin"));
		chkLogin.setBackground(display.getSystemColor(SWT.COLOR_WHITE));
		chkLogin.setLayoutData("width 180px,wrap");
		chkLogin.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event e) {
				if (chkLogin.getSelection()) {
					// int odg = ALERT.open(shell, "!",
					// "Iskljuèiti logiranje","Želite li iskljuèiti logiranje korisnika?\nTada neæe biti moguæe praæenje ukoliko ima više korisnika.\nNastaviti?",
					// "Iskljuèi|Odustani",1);
					int odg = ALERT.show(getLabel("mess20"), "?",
							getLabel("mess20txt"), OpalDialogType.OK_CANCEL,
							"", "");

					switch (odg) {

					case 0:
						gLogin = 1;
						chkLogin.setSelection(true);
						break;
					case 1:
						chkLogin.setSelection(false);
						gLogin = 0;
						break;
					}
				}
			}
		});

		sep = new Label(comp, SWT.SEPARATOR | SWT.SHADOW_IN | SWT.HORIZONTAL);
		sep.setLayoutData("width 280px,wrap");
		sep.setData("SEP");

		lbl = new Label(comp, SWT.NONE);
		lbl.setBackground(display.getSystemColor(SWT.COLOR_WHITE));
		lbl.setText(getLabel("lblLang"));
		lbl.setLayoutData("width 30px,wrap");

		cmbJezik = new Combo(comp, SWT.DROP_DOWN | SWT.READ_ONLY);

		Locale hr = new Locale("hr", "HR"); //$NON-NLS-2$
		Locale en = new Locale("en", "EN"); //$NON-NLS-2$
		Locale de = new Locale("de", "DE"); //$NON-NLS-2$
		Locale fr = new Locale("fr", "FR"); //$NON-NLS-2$
		Locale es = new Locale("es", "ES"); //$NON-NLS-2$
		Locale it = new Locale("it", "IT"); //$NON-NLS-2$

		// System.out.println(de.getDisplayCountry(de));
		cmbJezik.add(hr.getDisplayLanguage(Locale.getDefault()), 0);
		cmbJezik.add(en.getDisplayLanguage(Locale.getDefault()), 1);
		cmbJezik.add(de.getDisplayLanguage(Locale.getDefault()), 2);
		cmbJezik.add(fr.getDisplayLanguage(Locale.getDefault()), 3);
		cmbJezik.add(es.getDisplayLanguage(Locale.getDefault()), 4);
		cmbJezik.add(it.getDisplayLanguage(Locale.getDefault()), 5);
		cmbJezik.setLayoutData("width 100px,wrap");
		cmbJezik.select(0);
		cmbJezik.addSelectionListener(this);

		sep = new Label(comp, SWT.SEPARATOR | SWT.SHADOW_IN | SWT.HORIZONTAL);
		sep.setLayoutData("width 280px,wrap");
		sep.setData("SEP");

		lbl = new Label(comp, SWT.NONE);
		lbl.setBackground(display.getSystemColor(SWT.COLOR_WHITE));
		lbl.setText(getLabel("lblPath"));
		lbl.setLayoutData("width 30px,wrap");

		txtPath = new Text(comp, SWT.BORDER);
		txtPath.setLayoutData("width 250px,split 2");

		cmdBrowse = new Button(comp, SWT.PUSH);
		cmdBrowse.setText("...");
		cmdBrowse.setLayoutData("width 30px,wrap");

		cmdBrowse.addListener(SWT.Selection, new Listener() {
			private String selectedDir;

			@Override
			public void handleEvent(Event e) {
				switch (e.type) {
				case SWT.Selection:
					DirectoryDialog directoryDialog = new DirectoryDialog(shell);

					directoryDialog.setFilterPath(selectedDir);
					directoryDialog.setMessage(getLabel("mess22"));

					String dir = directoryDialog.open();
					if (dir != null) {
						// System.out.println(("Selected dir: " + dir));
						txtPath.setText(dir + "\\logg.xml");
						selectedDir = dir;

						// ALERT.open(shell,"!","Promjena lokacije logg datoteke?",
						// "Nadalje æe lokacija logg datoteke biti na lokaciji:\n"
						// + txtPath.getText()+"!", "Nastavak", 0);
						kovaDialog.inform(
								getLabel("mess23"),
								getLabel("mess23txt") + " <b><br/> "
										+ txtPath.getText() + "</b>!");
					}
				}
			}

		});

		sep = new Label(comp, SWT.SEPARATOR | SWT.SHADOW_IN | SWT.HORIZONTAL);
		sep.setLayoutData("width 280px,wrap");
		sep.setData("SEP");

		// groupO = new Group(shell,SWT.None);
		// groupO.setData("GR3");
		// groupO.setLayout(layout3);
		// groupO.setBackground(display.getSystemColor(SWT.COLOR_WHITE));
		// groupO.setLayoutData ("width 305px,wrap");
		// groupO.setText("Opis");
		//
		//
		// lblOpis = new Label (groupO,SWT.None);
		// lblOpis.setData("L");
		// lblOpis.setLayoutData ("width 150px,wrap");

		cmdSpremi = new Button(shell, SWT.PUSH);
		cmdSpremi.setText(getLabel("cmdSpremi"));
		cmdSpremi.setLayoutData("width 105px,split 2");

		cmdSpremi.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event e) {
				switch (e.type) {
				case SWT.Selection:
					// int odg=ALERT.open(shell, "?", "Spremiti izmjene?",
					// "Želite li spremiti izmjene?", "Spremi|Odustani", 0);
					int odg = ALERT.show(getLabel("mess24"), "?",
							getLabel("mess24txt"), OpalDialogType.OK_CANCEL,
							"", "");

					if (odg == 0) {
						// RSet.initRS();
						// System.out.println("update operateri set login=1 where id="+
						// getgUserID());
						// RSet.updateRS("update appPref set loginForm="+ gLogin
						// +", jezik="+ getJezik()+",loggPath='"+
						// txtPath.getText() +"'");
						// RSet.closeRS();

						setgLoggPath(txtPath.getText());
						setgLoginForm(gLogin);
						setgJezik(jezik);

						writeINI("library/profiles/" + getgFile());

						// Locale localeDE = new Locale("de", "DE");
						// Locale.setDefault(localeDE);
						//
						// Locale localeES = new Locale("es", "ES");
						// Locale.setDefault(localeES);
						//
						// Locale localeFR = new Locale("fr", "FR");
						// Locale.setDefault(localeFR);
						//
						// Locale localeENG = new Locale("en", "EN");
						// Locale.setDefault(localeENG);

						// setLanguage(shell);
					} else {

					}

					// setLanguage(gJezik,comp);

				}
			}

		});

	}

	private static void fillForm() {
		ResultSet rs;

		// try {
		//
		// RSet.initRS();
		// rs = RSet.openRS("select * from appPref");
		//
		// while (rs.next()) {

		if (getgLoginForm() == 1) {
			chkLogin.setSelection(true);
		} else {
			chkLogin.setSelection(false);
		}

		cmbJezik.select(getgJezik());
		txtPath.setText(getgLoggPath());

		// }
		//
		// RSet.closeRS();
		//
		//
		// } catch (SQLException e) {
		// System.out.println("fillForm - greška RS!");
		// e.printStackTrace();
		// logger.loggErr("opcije " + e.getMessage());
		// }
	}

	@Override
	public void widgetDefaultSelected(SelectionEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void widgetSelected(SelectionEvent e) {
		System.out.println("ušao");
		if (e.widget instanceof Combo) {
			// kovaDialog.inform(getLabel("mess41"), getLabel("mess41txt"));
			int odg = ALERT.show(getLabel("mess41"), "?",
					getLabel("mess41txt"), OpalDialogType.OK_CANCEL, "", "");

			if (odg == 0) {

				Process p;

				// try{
				// kovaDialog.inform("Putanja:", getJarFolder2());
				// }catch (Exception err){
				// kovaDialog.showException(err);
				// display.dispose();
				// }

				try {
					p = Runtime.getRuntime().exec(
							"rundll32 url.dll,FileProtocolHandler"
									+ getJarFolder2());
					kovaDialog.inform("Prošlo", "1");

					// Runtime runtime = Runtime.getRuntime();
					// try {
					// Process proc = runtime.exec("java -jar" +
					// getJarFolder2());
					// } catch (IOException e1) {
					// // TODO Auto-generated catch block
					// e1.printStackTrace();
					// }
					// System.exit(0);

					setgJezik(cmbJezik.getSelectionIndex());
					setgLoggPath(txtPath.getText());
					setgLoginForm(gLogin);
					writeINI("library/profiles/" + getgFile());
					kovaDialog.inform("Prošlo", "2");
					p.waitFor();
					// System.exit(0);
					kovaDialog.inform("Prošlo", "4");
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					kovaDialog.showException(e1);
				} catch (InterruptedException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
					kovaDialog.showException(e2);
				} catch (Exception err) {
					kovaDialog.showException(err);
				}

			}

		} else {

		}

		setJezik(cmbJezik.getSelectionIndex());
	}

	public static void setJezik(int jezik) {
		opcije.jezik = jezik;
	}

	public static int getJezik() {
		return jezik;
	}

}
