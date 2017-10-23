import java.lang.reflect.InvocationTargetException;
import java.util.StringTokenizer;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import kovaUtils.utils.*;
import kovaUtils.OpalDialog.kovaDialog;
import kovaUtils.OpalDialog.kovaDialog.OpalDialogType;

public class ALERT extends ApplicationWindow {
	static String strBTN1 = "";
	static String strBTN2 = "";
	static String strBTN3 = "";
	private static String[] buttonLabels;
	static int tipPoruke = 0;
	private static int uk;

	public ALERT(Shell parentShell) {
		super(parentShell);
		DEF.CenterScreen(parentShell, parentShell.getDisplay());
	}

	public static void main(String[] args) {

	}

	public static int open(Shell parent, String tip, String naslov,
			String poruka, String tipke, int defTipka) {
		int answer = -1;
		MessageDialog dialog = null;

		String delims = "|";
		StringTokenizer st = new StringTokenizer(tipke, delims);

		uk = st.countTokens();

		buttonLabels = new String[st.countTokens()];

		int i = 0;
		while (st.hasMoreTokens()) {
			buttonLabels[i] = st.nextToken();
			System.out.println(buttonLabels[i]);
			i++;
		}
		System.out.println("1");

		if (tip == "") {
			tipPoruke = MessageDialog.NONE; // 0
		} else if (tip == "x") {
			tipPoruke = MessageDialog.ERROR; // 1
		} else if (tip == "i") {
			tipPoruke = MessageDialog.INFORMATION;// 2
		} else if (tip == "?") {
			tipPoruke = MessageDialog.QUESTION; // 3
		} else if (tip == "!") {
			tipPoruke = MessageDialog.WARNING; // 4
		} else {
			tipPoruke = MessageDialog.INFORMATION;// DEFAULT je information
		}

		switch (uk) {
		case 1:
			strBTN1 = buttonLabels[0];
			dialog = new MessageDialog(parent.getShell(), naslov, null, poruka,
					tipPoruke, new String[] { strBTN1 }, defTipka);
			answer = dialog.open();
			break;
		case 2:
			strBTN1 = buttonLabels[0];
			strBTN2 = buttonLabels[1];
			dialog = new MessageDialog(parent.getShell(), naslov, null, poruka,
					tipPoruke, new String[] { strBTN1, strBTN2 }, defTipka);
			answer = dialog.open();
			break;
		case 3:
			strBTN1 = buttonLabels[0];
			strBTN2 = buttonLabels[1];
			strBTN3 = buttonLabels[2];
			dialog = new MessageDialog(parent.getShell(), naslov, null, poruka,
					tipPoruke, new String[] { strBTN1, strBTN2, strBTN3 },
					defTipka);
			answer = dialog.open();
			break;
		}

		if (answer == -1) {
			logger.logg("Poruka: " + poruka + "|Odabrano: Izlaz \"X\"");
		} else {
			logger.logg("Poruka: " + poruka + "|Odabrano:"
					+ buttonLabels[answer]);
		}

		return answer;

	}

	public static void openInput(Shell shell) {
		IInputValidator validator = new IInputValidator() {
			@Override
			public String isValid(String newText) {
				if (newText.equalsIgnoreCase("SWT/JFace")
						|| newText.equalsIgnoreCase("AWT")
						|| newText.equalsIgnoreCase("Swing"))
					return null;
				else
					return "The allowed values are: SWT/JFace, AWT, Swing";
			}
		};
		InputDialog dialog = new InputDialog(shell.getShell(), "Question",
				"What's your favorite Java UI framework?", "SWT/JFace",
				validator);

		if (dialog.open() == Window.OK) {
			System.out.println("Your favorite Java UI framework is: "
					+ dialog.getValue());
		} else {
			System.out.println("Action cancelled");
		}
	}

	public static int openProgress(Shell shell, final int ukupno,
			final int korak) {
		IRunnableWithProgress runnableWithProgress = new IRunnableWithProgress() {

			@Override
			public void run(IProgressMonitor monitor)
					throws InvocationTargetException, InterruptedException {
				monitor.beginTask("Number counting", ukupno);
				monitor.setTaskName("Ispis dokumenta");

				for (int i = 1; i <= ukupno; i++) {
					if (monitor.isCanceled()) {
						monitor.done();

						return;
					}

					System.out.println("Count number: " + i);
					monitor.subTask("Odraðeno " + i + "/" + ukupno);
					// monitor.worked(i);

					monitor.internalWorked(korak); // korak

					Thread.sleep(500); // 0.5s.
				}
				monitor.done();
			}
		};

		ProgressMonitorDialog dialog = new ProgressMonitorDialog(
				shell.getShell());

		try {
			dialog.run(true, true, runnableWithProgress);
		} catch (InvocationTargetException e) {
			e.printStackTrace();
			logger.loggErr("ALERT " + e.getMessage());
		} catch (InterruptedException e) {
			e.printStackTrace();
			logger.loggErr("ALERT " + e.getMessage());
		}
		return 0;
	}

	public static int show(String title, String icon, String text,
			OpalDialogType tip, String footerText, String detailFooterText) {
		final kovaDialog dialog = new kovaDialog();
		dialog.setMinimumWidth(400);
		dialog.getMessageArea().setTitle(title).setText(text);

		if (icon == "") {
			dialog.getMessageArea().setIcon(null); // 0
		} else if (icon == "x") {
			dialog.getMessageArea().setIcon(
					Display.getCurrent().getSystemImage(SWT.ICON_ERROR)); // 1
		} else if (icon == "i") {
			dialog.getMessageArea().setIcon(
					Display.getCurrent().getSystemImage(SWT.ICON_INFORMATION)); // 1
		} else if (icon == "?") {
			dialog.getMessageArea().setIcon(
					Display.getCurrent().getSystemImage(SWT.ICON_QUESTION)); // 1
		} else if (icon == "!") {
			dialog.getMessageArea().setIcon(
					Display.getCurrent().getSystemImage(SWT.ICON_WARNING)); // 1
		} else if (icon == "-") {
			dialog.getMessageArea().setIcon(
					Display.getCurrent().getSystemImage(SWT.ICON_CANCEL)); // 1
		} else {
			dialog.getMessageArea().setIcon(
					SWTGraphicUtil.getInstance().createImage(icon));
		}

		// .setExpanded(false).addCheckBox("Detaljnije upute", true)
		if (detailFooterText != "") {
			dialog.getFooterArea().setDetailText(detailFooterText);
		}
		if (footerText != "") {
			dialog.getFooterArea().setFooterText(footerText);
		}

		dialog.setButtonType(tip);
		dialog.show();

		return dialog.getSelectedButton();

	}

	public static int show(String title, String icon, String text,
			OpalDialogType tip, String footerText, String detailFooterText,
			Boolean footerCheckBox, String checkText, Boolean checkVal) {
		final kovaDialog dialog = new kovaDialog();
		dialog.setMinimumWidth(400);
		dialog.getMessageArea().setTitle(title).setText(text);

		if (icon == "") {
			dialog.getMessageArea().setIcon(null); // 0
		} else if (icon == "x") {
			dialog.getMessageArea().setIcon(
					Display.getCurrent().getSystemImage(SWT.ICON_ERROR)); // 1
		} else if (icon == "i") {
			dialog.getMessageArea().setIcon(
					Display.getCurrent().getSystemImage(SWT.ICON_INFORMATION)); // 1
		} else if (icon == "?") {
			dialog.getMessageArea().setIcon(
					Display.getCurrent().getSystemImage(SWT.ICON_QUESTION)); // 1
		} else if (icon == "!") {
			dialog.getMessageArea().setIcon(
					Display.getCurrent().getSystemImage(SWT.ICON_WARNING)); // 1
		} else if (icon == "-") {
			dialog.getMessageArea().setIcon(
					Display.getCurrent().getSystemImage(SWT.ICON_CANCEL)); // 1
		} else {
			dialog.getMessageArea().setIcon(
					SWTGraphicUtil.getInstance().createImage(icon));
		}

		if (footerCheckBox == true) {
			dialog.getFooterArea().setExpanded(false)
					.addCheckBox(checkText, checkVal);
		}

		dialog.getFooterArea().setDetailText(detailFooterText);
		dialog.getFooterArea().setFooterText(footerText);

		dialog.setButtonType(tip);
		dialog.show();

		return dialog.getSelectedButton();
	}

	public static String input(Shell shell, String title, String icon,
			String text, String defaultValue, OpalDialogType tip,
			String footerText, String detailFooterText) {
		final kovaDialog dialog = new kovaDialog(shell);
		dialog.setMinimumWidth(400);
		if (title != "") {
			dialog.getMessageArea().setTitle(title);
		}

		if (text != "") {
			dialog.getMessageArea().setText(text);
		}
		dialog.getMessageArea().addTextBox(defaultValue);

		if (icon == "") {
			dialog.getMessageArea().setIcon(null); // 0
		} else if (icon == "x") {
			dialog.getMessageArea().setIcon(
					Display.getCurrent().getSystemImage(SWT.ICON_ERROR)); // 1
		} else if (icon == "i") {
			dialog.getMessageArea().setIcon(
					Display.getCurrent().getSystemImage(SWT.ICON_INFORMATION)); // 1
		} else if (icon == "?") {
			dialog.getMessageArea().setIcon(
					Display.getCurrent().getSystemImage(SWT.ICON_QUESTION)); // 1
		} else if (icon == "!") {
			dialog.getMessageArea().setIcon(
					Display.getCurrent().getSystemImage(SWT.ICON_WARNING)); // 1
		} else if (icon == "-") {
			dialog.getMessageArea().setIcon(
					Display.getCurrent().getSystemImage(SWT.ICON_CANCEL)); // 1
		} else {
			dialog.getMessageArea().setIcon(
					SWTGraphicUtil.getInstance().createImage(icon));
		}

		if (footerText != "") {
			dialog.getFooterArea().setFooterText(footerText);
		}

		if (detailFooterText != "") {
			dialog.getFooterArea().setDetailText(detailFooterText);
		}

		dialog.setButtonType(tip);

		if (dialog.show() == 0) {
			return dialog.getMessageArea().getTextBoxValue();
		} else {
			return null;
		}
	}

	public static String input(Shell shell, String title, String icon,
			String text, String defaultValue, OpalDialogType tip,
			String footerText, String detailFooterText, Boolean footerCheckBox,
			String checkText, Boolean checkVal) {
		final kovaDialog dialog = new kovaDialog(shell);
		dialog.setMinimumWidth(400);
		if (title != "") {
			dialog.getMessageArea().setTitle(title);
		}

		if (text != "") {
			dialog.getMessageArea().setText(text);
		}
		dialog.getMessageArea().addTextBox(defaultValue);

		if (icon == "") {
			dialog.getMessageArea().setIcon(null); // 0
		} else if (icon == "x") {
			dialog.getMessageArea().setIcon(
					Display.getCurrent().getSystemImage(SWT.ICON_ERROR)); // 1
		} else if (icon == "i") {
			dialog.getMessageArea().setIcon(
					Display.getCurrent().getSystemImage(SWT.ICON_INFORMATION)); // 1
		} else if (icon == "?") {
			dialog.getMessageArea().setIcon(
					Display.getCurrent().getSystemImage(SWT.ICON_QUESTION)); // 1
		} else if (icon == "!") {
			dialog.getMessageArea().setIcon(
					Display.getCurrent().getSystemImage(SWT.ICON_WARNING)); // 1
		} else if (icon == "-") {
			dialog.getMessageArea().setIcon(
					Display.getCurrent().getSystemImage(SWT.ICON_CANCEL)); // 1
		} else {
			dialog.getMessageArea().setIcon(
					SWTGraphicUtil.getInstance().createImage(icon));
		}

		dialog.getFooterArea().setDetailText(detailFooterText);
		dialog.getFooterArea().setFooterText(footerText);

		if (footerCheckBox == true) {
			dialog.getFooterArea().setExpanded(false)
					.addCheckBox(checkText, checkVal);
		}

		dialog.setButtonType(tip);

		if (dialog.show() == 0) {
			return dialog.getMessageArea().getTextBoxValue();
		} else {
			return null;
		}
	}

	public static void progress(String title, String icon, String text,
			int min, int max, int val, OpalDialogType tip, String footerText,
			String detailFooterText) {
		final kovaDialog dialog = new kovaDialog();

		dialog.setTitle("Ivan");
		dialog.setMinimumWidth(400);
		dialog.getMessageArea().setTitle(title);

		dialog.getMessageArea().setText(text);
		dialog.getMessageArea().addProgressBar(min, max, val);

		if (icon == "") {
			dialog.getMessageArea().setIcon(null); // 0
		} else if (icon == "x") {
			dialog.getMessageArea().setIcon(
					Display.getCurrent().getSystemImage(SWT.ICON_ERROR)); // 1
		} else if (icon == "i") {
			dialog.getMessageArea().setIcon(
					Display.getCurrent().getSystemImage(SWT.ICON_INFORMATION)); // 1
		} else if (icon == "?") {
			dialog.getMessageArea().setIcon(
					Display.getCurrent().getSystemImage(SWT.ICON_QUESTION)); // 1
		} else if (icon == "!") {
			dialog.getMessageArea().setIcon(
					Display.getCurrent().getSystemImage(SWT.ICON_WARNING)); // 1
		} else if (icon == "-") {
			dialog.getMessageArea().setIcon(
					Display.getCurrent().getSystemImage(SWT.ICON_CANCEL)); // 1
		} else {
			dialog.getMessageArea().setIcon(
					SWTGraphicUtil.getInstance().createImage(icon));
		}

		// .setExpanded(false).addCheckBox("Detaljnije upute", true)
		if (detailFooterText != "") {
			dialog.getFooterArea().setDetailText(detailFooterText);
		}
		if (footerText != "") {
			dialog.getFooterArea().setFooterText(footerText);
		}

		final int[] counter = new int[1];
		counter[0] = 10;

		Display.getCurrent().timerExec(500, new Runnable() {

			@Override
			public void run() {
				dialog.getMessageArea().setProgressBarValue(counter[0]);
				counter[0] += 10;
				if (counter[0] < 120) {
					Display.getCurrent().timerExec(500, this);
				} else {
					dialog.close();
				}
			}
		});

		dialog.show();
	}

}
