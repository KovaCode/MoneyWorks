import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Scanner;

import kovaUtils.OpalDialog.kovaDialog;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Monitor;
import org.eclipse.swt.widgets.Shell;

public class DEF {

	public static final String DATE_FORMAT = "%1$td.%1$tm.%1$tY";
	// dd.mm.yyyy

	static Locale hr = new Locale("hr", "HR");
	static Locale en = new Locale("en", "EN");
	static Locale de = new Locale("de", "DE");
	static Locale fr = new Locale("fr", "FR");
	static Locale es = new Locale("es", "ES");
	static Locale it = new Locale("it", "IT");
	static Locale locale = Locale.getDefault();

	private int limit;
	private boolean toUppercase = false;
	static int lipa;

	public static String appName;
	public static String appVer;
	public Date nowDate = null; // current date

	private URL resourceURL;

	// globalne varijable
	public static int gBroj = 0;
	private static int gUserID = 0;

	private static int gLoginForm;
	private static int gJezik;
	private static int gBaza;
	private static String gLoggPath;
	private static String gLoggErrPath;
	private static String gAplikacija;
	private static String gImeBaze;
	private static int gIndexBaze;
	private static String gServer;
	private static String gProfil;
	private static String gFile = "config.ini";
	private static String gBaseUser;
	private static String gBasePass;
	private static String gVerzija;

	private static String flgProfile;

	private static int tmill = 0;
	private static int tmil = 0;
	private static int ttis = 0;
	private static int tsto = 0;
	private static int tlip = 0;
	private static FormLayout FL = new FormLayout();
	private static boolean flgProfileRead;

	static ResourceBundle RSC = ResourceBundle.getBundle("ressources/lang",
			locale);

	private static BufferedReader reader;

	public static void CenterScreen(Shell shell, Display display) {
		Monitor primary = display.getPrimaryMonitor();
		Rectangle bounds = primary.getBounds();
		Rectangle rect = shell.getBounds();

		int x = bounds.x + (bounds.width - rect.width) / 2;
		int y = bounds.y + (bounds.height - rect.height) / 2;

		shell.setLocation(x, y);

	}

	public static Boolean checkProfiles() {

		int cnt = 0;
		Boolean flgProfile = false;
		String path = "library/profiles/";
		String files;
		File folder = new File(path);
		File[] listOfFiles = folder.listFiles();

		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].isFile()) {
				files = listOfFiles[i].getName();
				if (files.endsWith(".ini")) {
					cnt += 1;
				}
			}
		}

		if (cnt == 0) {
			flgProfile = false;

		} else {
			flgProfile = true;
		}

		return flgProfile;
	}

	public static Boolean readINI() {
		Boolean flgReadini;
		String initialFile;

		if (fileExists("moneyWorks.ini") == true) {
			flgReadini = true;
			try {
				FileInputStream fstream = new FileInputStream("moneyWorks.ini");
				// Get the object of DataInputStream
				DataInputStream ini = new DataInputStream(fstream);
				BufferedReader br = new BufferedReader(new InputStreamReader(
						ini));

				initialFile = br.readLine();
				// System.out.println("INI File: " + initialFile);
				setgFile(initialFile);

				ini.close();
				br.close();

				if (fileExists("library/profiles/" + getgFile()) == false) {
					setFlgProfileRead(false);
				} else {
					setFlgProfileRead(true);
				}

				fstream = new FileInputStream("library/profiles/" + getgFile());
				ini = new DataInputStream(fstream);
				br = new BufferedReader(new InputStreamReader(ini));

				setgLoginForm(Integer.parseInt(String.valueOf(br.readLine()
						.substring(10))));
				setgJezik(Integer.parseInt(String.valueOf(br.readLine()
						.substring(6))));
				setgBaza(Integer.parseInt(String.valueOf(br.readLine()
						.substring(8))));
				setgImeBaze(br.readLine().substring(8));
				setgIndexBaze(Integer.parseInt(String.valueOf(br.readLine()
						.substring(10))));
				setgServer(br.readLine().substring(7));
				String custLoggPath = br.readLine().substring(9);
				setgLoggPath(custLoggPath);
				setgLoggErrPath(br.readLine().substring(12));
				setgAplikacija(br.readLine().substring(11));
				setgProfil(br.readLine().substring(7));

				setgBaseUser(br.readLine().substring(9));
				setgBasePass(crypt.Decrypt(br.readLine().substring(9)));
				setgVerzija(br.readLine().substring(4));

				appName = getgAplikacija();
				appVer = getgVerzija();

				RSet.setSQLuser(getgBaseUser());
				RSet.setSQLpass(getgBasePass());

				ini.close();

			} catch (Exception e) {// Catch exception if any
				System.err.println("Error: " + e.getMessage());
				logger.loggErr("DEF " + e.getMessage());
			}
		} else {
			System.out.println("READ ini ne postoji");
			flgReadini = false;
		}
		return flgReadini;
	}

	public static Boolean writeINI(String filePath) {
		Boolean flgWriteINI = false;
		try {
			FileWriter fstream = new FileWriter("moneyWorks.ini");
			BufferedWriter out = new BufferedWriter(fstream);

			// System.out.println("gFile:" + getgFile());

			out.write(getgFile());

			out.close();
			fstream.close();

			fstream = new FileWriter(filePath);
			out = new BufferedWriter(fstream);

			out.write("LoginForm=" + DEF.getgLoginForm());
			out.newLine();
			out.write("Jezik=" + DEF.getgJezik());
			out.newLine();
			out.write("TipBaze=" + DEF.getgBaza());
			out.newLine();
			out.write("ImeBaze=" + DEF.getgImeBaze());
			out.newLine();
			out.write("IndexBaze=" + DEF.getgIndexBaze());
			out.newLine();
			out.write("Server=" + DEF.getgServer());
			out.newLine();
			out.write("LoggPath=" + DEF.getgLoggPath());
			out.newLine();
			out.write("LoggErrPath=" + DEF.getgLoggErrPath());
			out.newLine();
			out.write("Aplikacija=" + DEF.getgAplikacija());
			out.newLine();
			out.write("Profil=" + DEF.getgProfil());
			out.newLine();
			out.write("BaseUser=" + DEF.getgBaseUser());
			out.newLine();
			out.write("BasePass=" + crypt.Encrypt(DEF.getgBasePass()));
			out.newLine();
			out.write("ver=" + getgVerzija());
			out.close();
			flgWriteINI = true;

		} catch (Exception e) {// Catch exception if any
			System.err.println("Error: " + e.getMessage());
			flgWriteINI = false;
			logger.loggErr("DEF " + e.getMessage());
		}
		return flgWriteINI;
	}

	public static boolean fileExists(String filePath) {
		File file = new File(filePath);
		boolean exists = file.exists();

		// System.out.println("Da li postoji:" + exists);

		return exists;
	}

	public static String iNull0(String str) {
		String s = "0";
		if (str == null) {
			s = "0";
		} else {
			s = str;
		}
		return s;
	}

	public static String iNull(String str) {
		String s = "";
		if (str == null) {
			s = "";
		} else {
			s = str;
		}
		return s;
	}

	public static String getSQLDay(java.sql.Date dan) {
		DateFormat dateFormat = new SimpleDateFormat("dd");
		return dateFormat.format(dan);
	}

	public static String getSQLMonth(java.sql.Date mjesec) {
		DateFormat dateFormat = new SimpleDateFormat("MM");
		return dateFormat.format(mjesec);
	}

	public static String getSQLYear(java.sql.Date godina) {
		DateFormat dateFormat = new SimpleDateFormat("yyyy");
		return dateFormat.format(godina);
	}

	public static String getNow() {
		DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
		Date date = new Date();
		return dateFormat.format(date);
	}

	public static Date getToday() {
		Date date = new Date();
		DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
		return date;
	}

	public static String getDate() {
		DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
		Date date = new Date();
		return dateFormat.format(date);
	}

	public static String getDateSQL() {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		return dateFormat.format(date);
	}

	public String getBackupFolderName() {
		Date date = Calendar.getInstance().getTime();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd.hhmmss");
		return sdf.format(date);
	}

	public static String getTimeLogg() {
		Date date = Calendar.getInstance().getTime();
		SimpleDateFormat sdf = new SimpleDateFormat("hhmmssSS");
		return sdf.format(date);
	}

	public static String getTime() {
		Date date = Calendar.getInstance().getTime();
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		return sdf.format(date);
	}

	public static String getEOM(int year, int month, int day) {
		int maxDay = 0;
		Calendar calendar = Calendar.getInstance();

		calendar.set(year, month, day);
		maxDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

		if (year == 111) {
			year = 2011;
		}

		return year + "-" + (month + 1) + "-" + maxDay;

	}

	public static String getEOM(Date d) {
		int year;
		int month;
		int day;

		day = d.getDay();
		month = d.getMonth();
		year = d.getYear();

		if (year == 111) {
			year = 2011;
		}

		if (month == 1 || month == 3 || month == 5 || month == 7 || month == 8
				|| month == 10 || month == 12) {
			return year + "-" + month + "-" + 31;
		}
		if (month == 4 || month == 6 || month == 9 || month == 11) {
			return year + "-" + month + "-" + 30;
		}
		if (month == 2) {
			if (isLeapYear(year)) {
				return year + "-" + month + "-" + 29;
			} else {
				return year + "-" + month + "-" + 28;
			}
		}
		return "";
	}

	public static boolean isLeapYear(int year) {
		return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0);
	}

	// FORMATIRA (une�ene brojeve u iznos) ok!!!
	public static String FormatCur(String broj) {
		String s = "";
		double br = 0;

		if (broj.length() > 3) {
			if ((broj.charAt(broj.length() - 3)) == '.'
					|| broj.charAt(broj.length() - 2) == '.') {
				broj = broj.replace('.', ',');
			}
		}

		broj = broj.replace(".", "");
		broj = broj.trim();

		broj = broj.replace(",", ".");
		broj = broj.trim();

		br = Double.parseDouble(broj);

		NumberFormat n = NumberFormat.getNumberInstance();
		n.setMinimumFractionDigits(2);
		broj = n.format(br);

		return broj;

	}

	static final String NUM_REGEX = "-?((([0-9]{1,3})(,[0-9]{3})*)|[0-9]*)(\\.[0-9]+)?([Ee][0-9]*)?";

	public static boolean isNum(String s) {
		return s != null && s.length() > 0 && s.matches(NUM_REGEX);
	}

	public boolean isANumber(String s) {
		try {
			BigDecimal a = new BigDecimal(s);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	public static double isNumeric(String str) {
		Double doubleValue = 0.0;
		System.out.println(str);
		if (str != "" && str != null) {
			Scanner scanner = new Scanner(str);

			while (scanner.hasNext()) {
				if (scanner.hasNextDouble()) {
					doubleValue = scanner.nextDouble();
					System.out.println(doubleValue);
				} else {
					String stringValue = scanner.next();
					System.out.println(stringValue);
				}
			}
		}
		return doubleValue;
	}

	public static String FormatCur2(Double iznos) {
		System.out.println("format unos " + iznos);
		String format = "###,###.###";
		NumberFormat nf = NumberFormat.getNumberInstance(Locale.getDefault());
		DecimalFormat df = (DecimalFormat) nf;
		df.applyPattern(format);
		String formatIznos = df.format(iznos);

		// System.out.println("format:" + format + "\noutput: " + formatIznos +
		// "\n" + Locale.getDefault().toString());

		return formatIznos;

	}

	// public static Double FormatCur (Double broj){
	// double money = 0;
	//
	// money=broj;
	//
	// NumberFormat formatter = new DecimalFormat("#,###.00");
	//
	// money=(formatter.format(money));
	// return money;
	// }

	public static Double getReal(String broj) {
		double br = 0;

		if (broj.isEmpty()) {
			broj = "0,00";
		}

		broj = broj.replace(".", "");
		broj = broj.trim();
		broj = broj.replaceFirst(",", ".");
		broj = broj.trim();

		br = Double.parseDouble(broj);
		br = Round(br, 2);

		return br;

	}

	public static double Round(double br, int Rpl) {
		double p = (double) Math.pow(10, Rpl);
		br = br * p;
		double tmp = Math.round(br);
		return (double) tmp / p;
	}

	// klase za validaciju (unos samo malih slova)
	public static boolean getLettersENG(int key) {
		boolean chk = false;

		if (key >= 65 && key <= 90) {
			chk = true;
		} else if (key >= 37 && key <= 40) {
			chk = true;
		} else if (key == 8) {
			chk = true;
		} else if (key == 127) {// delete
			chk = true;
		} else if (key == 10) {// ENTER
			chk = true;
		} else {
			chk = false;
		}
		return chk;
	}

	// klase za validaciju (unos iznosa)
	public static boolean getNum(int key) {
		boolean chk = false;

		if (key >= 47 && key <= 58) {
			chk = true;
		} else if (key >= 96 && key <= 105) {
			chk = true;
		} else if (key >= 37 && key <= 40) {
			chk = true;
		} else if (key == 8) {
			chk = true;
		} else if (key == 127) {// delete
			chk = true;
		} else if (key == 10) {// ENTER
			chk = true;
		} else {
			chk = false;
		}
		return chk;
	}

	// klase za validaciju (unos iznosa)
	public static boolean getCur(int key) {
		boolean chk = false;

		if (key >= 47 && key <= 58) {
			chk = true;
		} else if (key >= 96 && key <= 105) {
			chk = true;
		} else if (key >= 37 && key <= 40) {
			chk = true;
		} else if (key == 8) {
			chk = true;
		} else if (key == 44) { // ","
			chk = true;
		} else if (key == 46) {// "."
			chk = true;
		} else if (key == 127) {// delete
			chk = true;
		} else if (key == 10) {// ENTER
			chk = true;
		} else {
			chk = false;
		}
		return chk;
	}

	// ispis broja slovima
	public static String getSlova(String broj) {
		String slovima = null;

		broj = broj.replace(".", "");
		broj = broj.trim();
		broj = broj.replace(",", ".");
		broj = broj.trim();

		for (int k = 0; k < 13; k++) {
			if (broj.length() < 13)
				broj = "0" + broj;
		}

		int i = broj.indexOf('.');

		// lipe
		tlip = Integer.parseInt(broj.substring((i + 1), (i + 3)));
		// stotine
		tsto = Integer.parseInt(broj.substring(7, 10));
		// tisu�e
		ttis = Integer.parseInt(broj.substring(4, 7));
		// milijuni
		tmil = Integer.parseInt(broj.substring(1, 4));
		// milijarde
		tmill = Integer.parseInt(broj.substring(0, 1));

		slovima = slova(tlip) + " lipa";
		if (tsto > 0)
			slovima = slova(tsto) + " kuna i " + slova(tlip) + " lipa";
		if (ttis > 0)
			slovima = slova(ttis) + " tisu�a " + slova(tsto) + " kuna i "
					+ slova(tlip) + " lipa";
		if (tmil > 0)
			slovima = slova(tmil) + " milijun " + slova(ttis) + "tisu�a "
					+ slova(tsto) + " kuna i " + slova(tlip) + " lipa";
		if (tmill > 0)
			slovima = slova(tmill) + " milijarda " + slova(tmil) + "milijun "
					+ slova(ttis) + "tisu�a " + slova(tsto) + " kuna i "
					+ slova(tlip) + " lipa";

		slovima = slovimaFix(slovima);

		return slovima;
	}

	private static String slovimaFix(String slovima) {
		String fix = "";
		fix = slovima;
		fix = fix.replaceAll("jedna tisu�a", "tisu�u");

		fix = fix.replaceAll("jednamilijarda", "milijarda");

		fix = fix.replaceAll("milijun", "milijuna");
		fix = fix.replaceAll("jedanmilijuna", "milijun");
		fix = fix.replaceAll("dvije milijuna", "dva milijuna");

		fix = fix.replaceAll("tisu�u", "tisu�a");
		fix = fix.replaceAll("tritisu�a", "tri tisu�e");
		fix = fix.replaceAll("dvijetisu�a", "dvije tisu�e");
		fix = fix.replaceAll("�etiritisu�a", "�etiri tisu�e");

		fix = fix.replaceAll("dvije kuna", "dvije kune");
		fix = fix.replaceAll("tri kuna", "tri kune");
		fix = fix.replaceAll("�etiri kuna", "�etiri kune");
		fix = fix.replaceAll("dvije lipa", "dvije lipe");
		fix = fix.replaceAll("tri lipa", "tri lipe");
		fix = fix.replaceAll("�etiri lipa", "�etiri lipe");
		fix = fix.replaceAll("i  lipa", "");
		return fix;
	}

	public static String slova(int t) {
		String s = "";
		String rijec = "";
		int n = 0;
		int a = 0;

		n = t / 100;
		// stotice
		switch (n) {
		case 0:
			s = "";
			break;
		case 1:
			s = "sto";
			break;
		case 2:
			s = "dvijesto";
			break;
		case 3:
			s = "tristo";
			break;
		case 4:
			s = "�etiristo";
			break;
		case 5:
			s = "petsto";
			break;
		case 6:
			s = "�esto";
			break;
		case 7:
			s = "sedamsto";
			break;
		case 8:
			s = "osamsto";
			break;
		case 9:
			s = "devetsto";
			break;
		}

		rijec = s;

		// desetice
		n = t / 10;
		n = n % 10;

		switch (n) {
		case 0:
			s = "";
			break;
		case 1:
			s = "";
			a = 10;
			break;
		case 2:
			s = "dvadeset";
			break;
		case 3:
			s = "trideset";
			break;
		case 4:
			s = "�etrdeset";
			break;
		case 5:
			s = "pedeset";
			break;
		case 6:
			s = "�ezdeset";
			break;
		case 7:
			s = "sedamdeset";
			break;
		case 8:
			s = "osamdeset";
			break;
		case 9:
			s = "devedeset";
			break;
		}
		rijec = rijec + "" + s;

		// jedinice
		n = t % 10;
		n = n + a;

		switch (n) {
		case 0:
			s = "";
			break;
		case 1:
			if (lipa == 1) {
				s = "jedan";
			} else {
				s = "jedna";
			}
			break;
		case 2:
			if (lipa == 1) {
				s = "dva";
			} else {
				s = "dvije";
			}
			break;
		case 3:
			s = "tri";
			break;
		case 4:
			s = "�etiri";
			break;
		case 5:
			s = "pet";
			break;
		case 6:
			s = "�est";
			break;
		case 7:
			s = "sedam";
			break;
		case 8:
			s = "osam";
			break;
		case 9:
			s = "devet";
			break;
		case 10:
			s = "deset";
			break;
		case 11:
			s = "jedanaest";
			break;
		case 12:
			s = "dvanaest";
			break;
		case 13:
			s = "trinaest";
			break;
		case 14:
			s = "�etrnaest";
			break;
		case 15:
			s = "petnaest";
			break;
		case 16:
			s = "�esnaest";
			break;
		case 17:
			s = "sedamnaest";
			break;
		case 18:
			s = "osamnaest";
			break;
		case 19:
			s = "devetnaest";
			break;
		}
		rijec = rijec + "" + s;

		return rijec;
	}

	public static boolean checkJMBG(String sJMBG) {
		boolean flgJMBG = false;

		if (sJMBG.length() != 13)
			flgJMBG = false;

		/*
		 * '' DODATNA PROVJERA ----------------- If val(Mid(s, 5, 3)) < 900 And
		 * val(Mid(s, 5, 3)) > 100 Then Exit Function If val(Mid(s, 3, 2)) > 12
		 * Then Exit Function '' ----------------------------------
		 * 
		 * z = 0 For i = 1 To 6 z = z + (val(Mid$(s, i, 1)) + val(Mid$(s, i + 6,
		 * 1))) * (8 - i) Next i z = (11 - (z Mod 11)) Mod 11 If z = 10 Then
		 * Exit Function CheckJMBG = Right$(s, 1) = z
		 */

		return flgJMBG;
	}

	public static int getgUserID() {
		return gUserID;
	}

	public static int getgLoginForm() {
		return gLoginForm;
	}

	public static int getgBaza() {
		return gBaza;
	}

	public static String getgLoggPath() {
		return gLoggPath;
	}

	public static String getgAplikacija() {
		return gAplikacija;
	}

	public static String getgImeBaze() {
		return gImeBaze;
	}

	public static String getgServer() {
		return gServer;
	}

	public static int getgJezik() {
		return gJezik;
	}

	public static int getgIndexBaze() {
		return gIndexBaze;
	}

	public static void setgLoginForm(int gLoginForm) {
		DEF.gLoginForm = gLoginForm;
	}

	public static void setgUserID(int gUserID) {
		DEF.gUserID = gUserID;
		System.out.println("DEF-gUserID: " + gUserID);
	}

	public static void setgJezik(int gJezik) {
		DEF.gJezik = gJezik;
	}

	public static void setgBaza(int gBaza) {
		DEF.gBaza = gBaza;
		RSet.gTipBaze = DEF.gBaza;
		RSet.initRS();
	}

	public static void setgAplikacija(String gAplikacija) {
		DEF.gAplikacija = gAplikacija;
	}

	public static void setgImeBaze(String gImeBaze) {
		DEF.gImeBaze = gImeBaze;
	}

	public static void setgServer(String gServer) {
		DEF.gServer = gServer;
		RSet.setSQLip(gServer);
		RSet.setUrl();
	}

	public static void setgLoggPath(String gLoggPath) {
		DEF.gLoggPath = gLoggPath;
	}

	public static void setgIndexBaze(int gIndexBaze) {
		DEF.gIndexBaze = gIndexBaze;
	}

	public static void setgProfil(String gProfil) {
		DEF.gProfil = gProfil;
	}

	public static String getgProfil() {
		return gProfil;
	}

	public static void setgFile(String gFile) {
		DEF.gFile = gFile;
	}

	public static String getgFile() {
		return gFile;
	}

	public static void setgBaseUser(String gBaseUser) {
		DEF.gBaseUser = gBaseUser;
	}

	public static String getgBaseUser() {
		return gBaseUser;
	}

	public static void setgBasePass(String gBasePass) {
		DEF.gBasePass = gBasePass;
	}

	public static String getgBasePass() {
		return gBasePass;
	}

	public static void setFlgProfileRead(boolean flgProfileRead) {
		DEF.flgProfileRead = flgProfileRead;
	}

	public static boolean isFlgProfileRead() {
		return flgProfileRead;
	}

	public static void createBase() {
		RSet.executeRS("if not exists(select * from sys.databases where name = 'jmoney') create database jmoney");
	}

	public static void initialSQL() {
		CallableStatement proc_stmt = null;
		Connection cn = null;
		ResultSet rs = null;

		// if(SQL){
		RSet.setDefaultBase("jmoney");

		// za SQL

		String SQLProecedure = "create PROCEDURE [dbo].[INITIAL] "
				+ "		                        AS"
				+ "		                        BEGIN"
				+ "		                        CREATE TABLE JMoney.dbo.categories("
				+ "		                        	[ID] [numeric](18, 0) IDENTITY(1,1) NOT NULL,"
				+ "		                        	[Name] [varchar](50) NOT NULL,"
				+ "		                        	[ShortcutKey] [varchar](10) NULL,"
				+ "		                        	[Color] [varchar](50) NULL,"
				+ "		                        	[R] [smallint] NULL,"
				+ "		                        	[G] [smallint] NULL,"
				+ "		                        	[B] [smallint] NULL,"
				+ "		                        	[red] [numeric](18, 0) NULL,"
				+ "		                        	PRIMARY KEY CLUSTERED "
				+ "						( "
				+ "						[ID] ASC "
				+ "                        )WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY] "
				+ "                        ) ON [PRIMARY] "
				+ "										"
				+ "                        CREATE TABLE JMoney.dbo.language( "
				+ "                        	[ID] [numeric](18, 0) NOT NULL, "
				+ "                        	[grupa] [varchar](6) NOT NULL, "
				+ "                        	[HRV] [varchar](50) NOT NULL, "
				+ "                       	[ENG] [varchar](50) NOT NULL "
				+ "                        ) ON [PRIMARY] "
				+ ""
				+ ""
				+ " 		                    CREATE TABLE JMoney.dbo.racuni(  "
				+ "                       	[ID] [numeric](18, 0) IDENTITY(1,1) NOT NULL, "
				+ "                        	[Datum] [date] NULL, "
				+ "                        	[Iznos] [money] NULL, "
				+ "                        	[Svrha] [varchar](250) NULL, "
				+ "							[Mjesto] [varchar](250) NULL, "
				+ "                        	[tip] [int] NULL, "
				+ "                        	[PINtrans] [varchar](11) NULL, "
				+ "                        	[vrijeme] [time](7) NULL, "
				+ "                        	[oper] [varchar](10) NULL, "
				+ "                        	[rateID] [int] NULL, "
				+ "                        	[kategorija] [int] NULL, "
				+ "                        	[active] [bit] NULL, "
				+ "                        CONSTRAINT [PK_racuni] PRIMARY KEY CLUSTERED "
				+ "                        ( "
				+ "                        	[ID] ASC "
				+ "                        )WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY] "
				+ "                        ) ON [PRIMARY] "
				+ ""
				+ ""
				+ "					CREATE TABLE JMoney.[dbo].[operateri]( "
				+ "								[id] [numeric](18, 0) IDENTITY(1,1) NOT NULL,  "
				+ "						[Ime] [varchar](50) NULL,	 "
				+ "						[Prezime] [varchar](50) NULL,  "
				+ "						[korisnik] [varchar](25) NULL, "
				+ "						[lozinka] [varchar](100) NULL, "
				+ "						[Prava] [numeric](18, 0) NULL, "
				+ "						[Vrijeme] [datetime] NULL, "
				+ "						[vrijediOD] [date] NULL, "
				+ "						[vrijediDO] [date] NULL, "
				+ "					[hint] [varchar](100) NULL, "
				+ "						[logged] [smallint] NULL, "
				+ "					PRIMARY KEY CLUSTERED  "
				+ "					( "
				+ "						[id] ASC "
				+ "					)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY] "
				+ "					) ON [PRIMARY] " + "                              "
				+ "                       END";

		RSet.executeRS(SQLProecedure);

		cn = RSet.openCN();

		try {
			proc_stmt = cn.prepareCall("{ call INITIAL() }");
			proc_stmt.executeQuery();
			proc_stmt.close();
			cn.close();

		} catch (SQLException e) {
			logger.loggErr("DEF " + e.getMessage());
			e.printStackTrace();
		}

		// }else{
		//
		// }

	}

	public static void setgLoggErrPath(String gLoggErrPath) {
		DEF.gLoggErrPath = gLoggErrPath;
	}

	public static String getgLoggErrPath() {
		return gLoggErrPath;
	}

	// try {
	// copy("fromFile.txt", "toFile.txt");
	// } catch (IOException e) {
	// System.err.println(e.getMessage());
	// }

	public static void copy(String fromFileName, String toFileName)
			throws IOException {
		File fromFile = new File(fromFileName);
		File toFile = new File(toFileName);

		if (!fromFile.exists())
			throw new IOException("FileCopy: " + "no such source file: "
					+ fromFileName);
		if (!fromFile.isFile())
			throw new IOException("FileCopy: " + "can't copy directory: "
					+ fromFileName);
		if (!fromFile.canRead())
			throw new IOException("FileCopy: " + "source file is unreadable: "
					+ fromFileName);

		if (toFile.isDirectory())
			toFile = new File(toFile, fromFile.getName());

		if (toFile.exists()) {
			if (!toFile.canWrite())
				throw new IOException("FileCopy: "
						+ "destination file is unwriteable: " + toFileName);
			System.out.print("Overwrite existing file " + toFile.getName()
					+ "? (Y/N): ");
			System.out.flush();
			BufferedReader in = new BufferedReader(new InputStreamReader(
					System.in));
			String response = in.readLine();
			if (!response.equals("Y") && !response.equals("y"))
				throw new IOException("FileCopy: "
						+ "existing file was not overwritten.");
		} else {
			String parent = toFile.getParent();
			if (parent == null)
				parent = System.getProperty("user.dir");
			File dir = new File(parent);
			if (!dir.exists())
				throw new IOException("FileCopy: "
						+ "destination directory doesn't exist: " + parent);
			if (dir.isFile())
				throw new IOException("FileCopy: "
						+ "destination is not a directory: " + parent);
			if (!dir.canWrite())
				throw new IOException("FileCopy: "
						+ "destination directory is unwriteable: " + parent);
		}

		FileInputStream from = null;
		FileOutputStream to = null;
		try {
			from = new FileInputStream(fromFile);
			to = new FileOutputStream(toFile);
			byte[] buffer = new byte[4096];
			int bytesRead;

			while ((bytesRead = from.read(buffer)) != -1)
				to.write(buffer, 0, bytesRead); // write
		} finally {
			if (from != null)
				try {
					from.close();
				} catch (IOException e) {
					;
				}
			if (to != null)
				try {
					to.close();
				} catch (IOException e) {
					;
				}
		}
	}

	/**
	 * Get a translated label
	 * 
	 * @param key
	 *            key to get
	 * @return the translated value of the key
	 */
	public static String getLabel(final String key) {
		try {
			return RSC.getString(key);
		} catch (Exception e) {
			logger.loggErr("Ne postoji prijevod za oznaku: " + key);
			kovaDialog.showException(e);
			return key;
		}

	}

	public static String getJarFolder() {
		String name = login.class.getName().replace('.', '/');
		String s = login.class.getResource("/" + "login.class").toString();
		s = s.replace('/', File.separatorChar);

		// System.out.println(s);

		// s = s.substring(0, s.indexOf("jar")+4);
		// s = s.substring(s.lastIndexOf(':')-1);

		s = s.substring(0, s.lastIndexOf(File.separatorChar) - 1);
		// s = s + "moneyWorks.jar";

		return s;

		// return s.substring(0, s.lastIndexOf(File.separatorChar)-3);
	}

	public static String getJarFolder2() {
		String name = login.class.getName().replace('.', '/');
		String s = login.class.getResource("/" + name + ".class").toString();
		s = s.replace('/', File.separatorChar);
		s = s.substring(0, s.indexOf(".jar") + 4);
		s = s.substring(s.lastIndexOf(':') - 1);
		s.substring(0, s.lastIndexOf(File.separatorChar) + 1);

		// "C:\java\java.exe -jar C:\jar_you_want_to_run.jar"

		return s;
	}

	public static boolean checkAdminRights(String adminPass) {
		ResultSet rs;
		Boolean flgLogin = false;

		RSet.setDefaultBase("jmoney");
		RSet.initRS();

		// System.out.println(AppCrypt.crypt.Encrypt(adminPass));

		rs = RSet
				.openRS("select lozinka from operateri where lozinka ='" + crypt.Encrypt(adminPass) + " ' and  prava=2 "); //$NON-NLS-1$

		System.out.println("DEF.UKUPNO: " + RSet.countRS(rs));

		if (RSet.countRS(rs) > 0) {
			flgLogin = true;
		} else {
			flgLogin = false;
		}
		// RSet.closeRS();

		return flgLogin;
	}

	public static Color getColor(eColor color) {
		Color clr = Display.getCurrent().getSystemColor(SWT.COLOR_WHITE);

		switch (color.getCode()) {
		case 0:
			clr = Display.getCurrent().getSystemColor(SWT.COLOR_WHITE);
			break;
		case 1:
			clr = Display.getCurrent().getSystemColor(SWT.COLOR_BLACK);
			break;
		case 2:
			clr = Display.getCurrent().getSystemColor(SWT.COLOR_GRAY);
			break;
		case 3:
			clr = Display.getCurrent().getSystemColor(SWT.COLOR_RED);
			break;
		case 4:
			clr = Display.getCurrent().getSystemColor(SWT.COLOR_YELLOW);
			break;
		case 5:
			clr = Display.getCurrent().getSystemColor(SWT.COLOR_BLUE);
			break;
		case 6:
			clr = Display.getCurrent().getSystemColor(SWT.COLOR_GREEN);
			break;
		case 7:
			clr = Display.getCurrent().getSystemColor(SWT.COLOR_CYAN);
			break;
		case 8:
			clr = Display.getCurrent().getSystemColor(SWT.COLOR_MAGENTA);
			break;
		case 9:
			clr = Display.getCurrent().getSystemColor(SWT.COLOR_DARK_GRAY);
			break;
		case 10:
			clr = Display.getCurrent().getSystemColor(SWT.COLOR_DARK_RED);
			break;
		case 11:
			clr = Display.getCurrent().getSystemColor(SWT.COLOR_DARK_YELLOW);
			break;
		case 12:
			clr = Display.getCurrent().getSystemColor(SWT.COLOR_DARK_BLUE);
			break;
		case 13:
			clr = Display.getCurrent().getSystemColor(SWT.COLOR_DARK_GREEN);
			break;
		case 14:
			clr = Display.getCurrent().getSystemColor(SWT.COLOR_DARK_CYAN);
			break;
		case 15:
			clr = Display.getCurrent().getSystemColor(SWT.COLOR_DARK_MAGENTA);
			break;
		case 16:
			clr = Display.getCurrent().getSystemColor(
					SWT.COLOR_WIDGET_BACKGROUND);
			break;
		case 17:
			clr = Display.getCurrent().getSystemColor(SWT.COLOR_LIST_SELECTION);
			break;
		case 18:
			clr = Display.getCurrent().getSystemColor(
					SWT.COLOR_TITLE_BACKGROUND);
			break;
		}
		return clr;

	}

	public static Color getColorRGB(int R, int G, int B) {
		Color clr = Display.getCurrent().getSystemColor(SWT.COLOR_WHITE);

		clr = (new Color(Display.getCurrent(), new RGB(R, G, B)));

		return clr;
	}

	public static Font getFont(String font, int fontSize, eFontType fontType) {
		int fontT = 0;

		switch (fontType.getCode()) {
		case 0:
			fontT = SWT.NORMAL;
			break;
		case 1:
			fontT = SWT.BOLD;
			break;
		case 2:
			fontT = SWT.ITALIC;
			break;
		case 3:
			fontT = SWT.ITALIC | SWT.NORMAL;
			break;
		case 4:
			fontT = SWT.BOLD | SWT.ITALIC;
			break;
		}

		Font reFont = new Font(Display.getCurrent(), font, fontSize, fontT);

		return reFont;
	}

	public static void setgVerzija(String gVerzija) {
		DEF.gVerzija = gVerzija;
	}

	public static String getgVerzija() {
		return gVerzija;
	}

	public static enum eColor {
		WHITE(0), BLACK(1), GRAY(2), RED(3), YELLOW(4), BLUE(5), GREEN(6), CYAN(
				7), MAGENTA(8), DARK_GRAY(9), DARK_RED(10), DARK_YELLOW(11), DARK_BLUE(
				12), DARK_GREEN(13), DARK_CYAN(14), DARK_MAGENTA(15), WIDGET_BACKGROUND(
				16), LIST_SELECTION(17), TITLE_BACKGROUND(18);

		private int code;

		private eColor(int c) {
			code = c;
		}

		public int getCode() {
			return code;
		}
	}

	public static enum eFontType {
		NORMAL(0), BOLD(1), ITALIC(2), NORMAL_ITALIC(3), BOLD_ITALIC(4);

		private int code;

		private eFontType(int c) {
			code = c;
		}

		public int getCode() {
			return code;
		}
	}

}
