import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;

public class saveDialog {

	static int fileType;

	public static String filePath = "";
	private static String fileName = "";

	private static FileDialog dialog;

	public saveDialog(Shell shell) {
		open(shell);
	}

	public static String open(Shell shell) {

		dialog = new FileDialog(shell, SWT.SAVE);

		// excel

		// if (fileType=="XLS"){
		dialog.setFilterNames(new String[] { "Adobe PDF (*.pdf)",
				"Excel Files 97-2003 (*.xls)", "Excel Files 07-2011 (*.xlsx)",
				"Rich text format (*.rtf)",
				"EXtensible Markup Language (*.xml)",
				"HyperText Markup Language (*.html)", "All Files (*.*)" });
		dialog.setFilterExtensions(new String[] { "*.pdf", "*.xls", "*.xlsx",
				"*.rtf", "*.xml", "*.html", "*.*" });
		dialog.setFilterPath("c:\\");

		dialog.setFileName(DEF.getDate());

		filePath = dialog.open();
		setFileName(dialog.getFileName());
		fileType = dialog.getFilterIndex();

		return getFilePath();
	}

	public static void main(String[] args) {

	}

	public static String getFilePath() {
		return filePath;
	}

	public static int getFileType() {
		return fileType;
	}

	public static void setFileType(int fileType) {
		saveDialog.fileType = fileType;
	}

	public static void setFileName(String fileName) {
		saveDialog.fileName = fileName;
	}

	public static String getFileName() {
		return fileName;
	}

	// public static void setFilterNames(String[] strings) {
	// dialog.setFilterNames(new String[] {
	// "Adobe PDF (*.pdf)","Excel Files 97-2003 (*.xls)","Excel Files 07-2011 (*.xlsx)","Rich text format (*.rtf)","EXtensible Markup Language (*.xml)","HyperText Markup Language (*.html)",
	// "All Files (*.*)" });
	// }
	//
	// public static void setFilterExtensions(String[] strings) {
	// dialog.setFilterExtensions(new String[] { "*.pdf","*.xls",
	// "*.xlsx","*.rtf", "*.xml","*.html", "*.*" });
	// }

}