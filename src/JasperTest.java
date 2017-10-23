import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.util.HashMap;

import kovaUtils.InfinitePanel.InfiniteProgressPanel;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JRDesignQuery;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.JRHtmlExporter;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRRtfExporter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXmlExporter;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class JasperTest extends ApplicationWindow {

	public JasperTest(Shell parentShell) {
		super(parentShell);
		// TODO Auto-generated constructor stub
	}

	private static Shell shell;
	private static Display parentShell;
	static String report = "repPregRacuna.jrxml";
	static Connection connection;
	private static String filePath = "";
	private static String fileName = "";
	private static String sExp;

	// public JasperTest(Shell parentShell,boolean indeterminate) {
	// super(parentShell);
	// }

	// //za ispis potrebno ** naslov,operater,ikona(default),rs ili sql **
	// public static void main(String args[]) throws InvocationTargetException,
	// InterruptedException {
	//
	//
	// // String
	// SQLquery="select * from racuni where datum between '2011-5-1' and '2011-5-31'";
	// String
	// SQLquery="select id as ID,datum as Datum,mjesto as Mjesto,svrha as Svrha,iznos as Iznos,tip,pintrans from racuni where datum between '2011-5-1' and '2011-5-31' order by datum desc";
	// RSet.setDriver(driver)
	// RSet.setSQLip("localhost");
	// RSet.setSQLuser("sa");
	// RSet.setSQLpass("skyline");
	// connection = RSet.openCN();
	//
	// ispis("Pregled raèuna 5/2011","Ivan",SQLquery,0);
	//
	//
	// login.setFlgProfile(true);
	// }

	//
	// filePath = saveDialog.open(shell);
	// fileName = saveDialog.getFileName();
	//
	// if (DEF.fileExists(filePath)==true){
	// int odg = ALERT.open(shell, "i", "Izvoz datoteke!", "Datoteka \""+
	// saveDialog.getFilePath()+
	// "\" veæ postoji!\nŽelite li presnimiti datoteku!", "Presnimi|Odustani",
	// 0);
	// if (odg==0){
	// if (DEF.iNull(filePath)!= ""){
	// export("Mjeseèni pregled raèuna za 5/2011","Ivan Kovaèiæ",SQLquery,0,saveDialog.getFileType(),filePath,fileName);
	// }
	// }else{
	//
	// }
	// }else{
	// if (DEF.iNull(filePath)!= ""){
	// export("Mjeseèni pregled raèuna za 5/2011","Ivan Kovaèiæ",SQLquery,0,saveDialog.getFileType(),filePath,fileName);
	// ALERT.open(shell, "i", "JMoney - " +"Izvoz datoteke...",
	// "Izvoz datoteka \""+ fileName + "\" uspješno izvršen!", "Nastavak", 0);
	// }
	// }
	//
	//
	//
	// /*
	// VRSTE:
	// -------------------------------------------------------
	// 0=PDF
	// 1=XLS
	// 2=XLSX
	// 3=RTF
	// 4=XML
	// 5=HTML
	// --------------------------------------------------------
	// */
	//
	//
	//
	// shell.pack();
	// shell.open();
	//
	//

	public static void ispis(String naslov, String operater,
			final String SQLquery, int tip) {
		shell = new Shell(parentShell, SWT.DIALOG_TRIM | SWT.PRIMARY_MODAL);
		shell.setLayout(new GridLayout(1, false));

		shell.setSize(300, 200);

		connection = RSet.openCN();

		// System.out.println(connection);

		switch (tip) {
		case 0: // mjeseèni pregled
			report = "repPregRacuna.jrxml";
			break;
		case 1: // mjeseèni pregled OD-DO
			break;
		case 2: // operater...
			break;
		}

		final HashMap<String, Object> hm = new HashMap<String, Object>();
		hm.put("naslov", naslov);
		hm.put("operater", operater);
		hm.put("icona", "library/icons/Boue.gif");
		hm.put("aplikacija", DEF.appName + " (" + DEF.appVer + ")");

		final ProgressMonitorDialog dialog = new ProgressMonitorDialog(shell);

		try {
			dialog.run(true, false, new IRunnableWithProgress() {

				@Override
				public void run(IProgressMonitor monitor)
						throws InvocationTargetException, InterruptedException {
					monitor.beginTask("Inicijalizacija ispisa",
							IProgressMonitor.UNKNOWN);
					monitor.subTask("Molimo prièekajte trenutak...");

					try {
						JRDesignQuery jasperQUERY = new JRDesignQuery();
						jasperQUERY.setText(SQLquery);

						System.out.println("0");
						JasperDesign jasperDesign = JRXmlLoader
								.load("library/reports/" + report);
						System.out.println("1");
						jasperDesign.setQuery(jasperQUERY);

						System.out.println("2");

						JasperReport jasperReport = JasperCompileManager
								.compileReport(jasperDesign);
						JasperPrint jasperPrint = JasperFillManager.fillReport(
								jasperReport, hm, connection);
						JasperViewer.viewReport(jasperPrint, false);

					} catch (Exception e) {
						String connectMsg = "Izvještaj nije moguæe kreirati "
								+ e.getMessage() + " "
								+ e.getLocalizedMessage();
						System.out.println(connectMsg);
						logger.loggErr("JasperTest " + e.getMessage() + " ("
								+ e.getLocalizedMessage() + ")");
						// logger.logg(connectMsg);
					}

					monitor.done();

				}

			});
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.loggErr("JasperTest " + e.getMessage());
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.loggErr("JasperTest " + e.getMessage());
		}

	}

	public static void export(String naslov, String operater,
			final String SQLquery, int vrsta, final int vrstaExporta,
			String path, final String nazivFilea) {
		connection = RSet.openCN();

		filePath = path;
		fileName = nazivFilea;

		switch (vrsta) {
		case 0: // mjeseèni pregled
			report = "repPregRacuna.jrxml";
			break;
		case 1: // mjeseèni pregled OD-DO
			break;
		case 2: // operater...
			break;
		}

		switch (vrstaExporta) {
		case 0: // PDF
			sExp = "u PDF";
			break;
		case 1: // XLS
			sExp = "u XLS";
			break;
		case 2: // XLSX
			break;
		case 3: // RTF
			sExp = "u RTF";
			break;
		case 4: // XML
			sExp = "u XML";
			break;
		case 5: // HTML
			sExp = "u HTML";
			break;
		}

		final HashMap<String, Object> hm = new HashMap<String, Object>();
		hm.put("naslov", naslov);
		hm.put("operater", operater);
		hm.put("icona", "library/icons/Boue.gif");
		hm.put("aplikacija", DEF.appName + " (" + DEF.appVer + ")");

		final ProgressMonitorDialog dialog = new ProgressMonitorDialog(shell);

		try {
			dialog.run(true, false, new IRunnableWithProgress() {

				@Override
				public void run(IProgressMonitor monitor)
						throws InvocationTargetException, InterruptedException {
					monitor.beginTask("Inicijalizacija izvoza " + sExp + " ("
							+ nazivFilea + ")", IProgressMonitor.UNKNOWN);
					monitor.subTask("Molimo prièekajte trenutak...");

					try {

						JRDesignQuery jasperQUERY = new JRDesignQuery();
						jasperQUERY.setText(SQLquery);

						JasperDesign jasperDesign = JRXmlLoader
								.load("library/reports/" + report);
						jasperDesign.setQuery(jasperQUERY);

						JasperReport jasperReport = JasperCompileManager
								.compileReport(jasperDesign);
						JasperPrint jasperPrint = JasperFillManager.fillReport(
								jasperReport, hm, connection);

						switch (vrstaExporta) {
						case 0: // PDF
							// System.out.println("PDF");
							exportPDF(jasperPrint);
							break;
						case 1: // XLS
							// System.out.println("XLS");
							exportXLS(jasperPrint);
							break;
						case 2: // XLSX
							break;
						case 3: // RTF
							exportRTF(jasperPrint);
							break;
						case 4: // XML
							exportXML(jasperPrint);
							break;
						case 5: // HTML
							exportHTML(jasperPrint);
							break;
						}

					} catch (Exception e) {
						String connectMsg = "Izvještaj nije moguæe kreirati "
								+ e.getMessage() + " "
								+ e.getLocalizedMessage();
						System.out.println(connectMsg);
						logger.loggErr("JasperTest " + e.getMessage());
						// logger.logg(connectMsg);
					}

					monitor.done();

				}

			});
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.loggErr("JasperTest " + e.getMessage());
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.loggErr("JasperTest " + e.getMessage());
		}

	}

	private static void exportPDF(JasperPrint report) {
		JRPdfExporter exporterPDF = new JRPdfExporter();
		exporterPDF.setParameter(JRExporterParameter.JASPER_PRINT, report);
		exporterPDF
				.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, filePath);
		try {
			exporterPDF.exportReport();
		} catch (JRException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.loggErr("JasperTest " + e.getMessage());
		}
	}

	private static void exportXLS(JasperPrint report) {
		JRXlsExporter exporter = new JRXlsExporter();
		exporter.setParameter(JRExporterParameter.JASPER_PRINT, report);
		exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, filePath);
		try {
			exporter.exportReport();
		} catch (JRException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.loggErr("JasperTest " + e.getMessage());
		}
	}

	private static void exportRTF(JasperPrint report) {
		JRRtfExporter exporter = new JRRtfExporter();
		exporter.setParameter(JRExporterParameter.JASPER_PRINT, report);
		exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, filePath);
		try {
			exporter.exportReport();
		} catch (JRException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.loggErr("JasperTest " + e.getMessage());
		}
	}

	private static void exportXML(JasperPrint report) {
		JRXmlExporter exporter = new JRXmlExporter();
		exporter.setParameter(JRExporterParameter.JASPER_PRINT, report);
		exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, filePath);
		try {
			exporter.exportReport();
		} catch (JRException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.loggErr("JasperTest " + e.getMessage());
		}
	}

	private static void exportHTML(JasperPrint report) {
		JRHtmlExporter exporter = new JRHtmlExporter();
		exporter.setParameter(JRExporterParameter.JASPER_PRINT, report);
		exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, filePath);
		try {
			exporter.exportReport();
		} catch (JRException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.loggErr("JasperTest " + e.getMessage());
		}
	}

}