import java.awt.Color;
import java.awt.Toolkit;
import java.awt.event.WindowEvent;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.ImageIcon;

import org.jfree.ui.ApplicationFrame;

import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PiePlot3D;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;
import org.jfree.util.Rotation;

public class graphMonth extends ApplicationFrame {

	public graphMonth(String title) {
		super(title);
		// TODO Auto-generated constructor stub
	}

	private static int tip;
	private static String SQLquery;
	private static ResultSet rs;
	private static int month;
	private static Display display;

	/**
	 * Starting point for the demonstration application.
	 * 
	 * @param args
	 *            ignored.
	 */
	public static void main(final String[] args) {
		final graphMonth demo = new graphMonth(
				"Grafièki mjeseèni pregled po kategorijama", 1, 4);

		demo.pack();
		RefineryUtilities.centerFrameOnScreen(demo);
		demo.setSize(800, 600);
		demo.setVisible(true);
		// demo.setResizable(false);

		// shell.pack();
		// shell.open();

	}

	public void windowClosing(final WindowEvent evt) {
		if (evt.getWindow() == this) {
			dispose();

		}
	}

	/**
	 * Creates a new demo.
	 * 
	 * @param title
	 *            the frame title.
	 */
	public graphMonth(final String title, int tip, int month) {
		super(title);
		super.setIconImage(Toolkit.getDefaultToolkit().getImage(
				"library/icons/Boue.ico"));
		this.tip = tip;
		this.month = month;

		// create a dataset...
		final PieDataset dataset = createDataset();

		// create the chart...
		final JFreeChart chart = createChart(dataset);
		chart.setBorderPaint(Color.WHITE);

		// add the chart to a panel...
		final ChartPanel chartPanel = new ChartPanel(chart);
		chartPanel.setBackground(Color.WHITE);
		chartPanel.setPreferredSize(new java.awt.Dimension(400, 300));
		setContentPane(chartPanel);

	}

	/**
	 * Creates a sample dataset for the demo.
	 * 
	 * @return A sample dataset.
	 */
	private static PieDataset createDataset() {

		final DefaultPieDataset result = new DefaultPieDataset();

		String EOMdate = DEF.getEOM(DEF.getToday().getYear(), month - 1, DEF
				.getToday().getDay());

		if (tip == 0) {
			SQLquery = " select (case tip when 0 then 'Prihodi' when 1 then 'Rashodi' when 2 then 'Rate' when 3 then 'NetBanking' else 'OSTALO' end) vrsta, "
					+ "abs(SUM(iznos))sumaIznos from racuni "
					+ "where datum between '2011-"
					+ month
					+ "-1' and '"
					+ EOMdate + "' group by tip";
		} else {
			SQLquery = "select R.kategorija,(case R.kategorija when 0 then 'OSTALO' else (select C.Name where R.kategorija = C.ID)end)vrsta, "
					+ " C.R,C.G,C.B,abs(sum(R.Iznos))sumaIznos from racuni R left OUTER JOIN categories C on R.kategorija=C.ID"
					+ " where R.datum between '2011-"
					+ month
					+ "-1' and '"
					+ EOMdate
					+ "' group by R.kategorija,C.R,C.G,C.B,C.ID,C.Name";
		}

		System.out.println("QUERY: " + SQLquery);

		rs = RSet.openRS(SQLquery);

		System.out.println(RSet.countRS(rs));

		try {
			while (rs.next()) {
				result.setValue(rs.getString("vrsta"),
						rs.getDouble("sumaIznos"));
			}
			rs.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.loggErr("graphMonth " + e.getMessage() + " ("
					+ e.getErrorCode() + ")");
		}

		// result.setValue("Java", new Double(43.2));
		// result.setValue("Visual Basic", new Double(10.0));
		// result.setValue("C/C++", new Double(17.5));
		// result.setValue("PHP", new Double(32.5));
		// result.setValue("Perl", new Double(1.0));
		return result;

	}

	// ****************************************************************************
	// * JFREECHART DEVELOPER GUIDE *
	// * The JFreeChart Developer Guide, written by David Gilbert, is available
	// *
	// * to purchase from Object Refinery Limited: *
	// * *
	// * http://www.object-refinery.com/jfreechart/guide.html *
	// * *
	// * Sales are used to provide funding for the JFreeChart project - please *
	// * support us so that we can continue developing free software. *
	// ****************************************************************************

	/**
	 * Creates a sample chart.
	 * 
	 * @param dataset
	 *            the dataset.
	 * 
	 * @return A chart.
	 */
	private static JFreeChart createChart(final PieDataset dataset) {

		final JFreeChart chart = ChartFactory.createPieChart3D("", dataset,
				true, true, false);
		final PiePlot3D plot = (PiePlot3D) chart.getPlot();
		plot.setStartAngle(145);
		plot.setDirection(Rotation.CLOCKWISE);
		// plot.setForegroundAlpha(0.5f);
		plot.setBaseSectionPaint(Color.WHITE);
		plot.setNoDataMessage("Nema podataka za pregled");

		String EOMdate = DEF.getEOM(DEF.getToday().getYear(), month - 1, DEF
				.getToday().getDay());
		if (tip == 0) {
			SQLquery = " select (case tip when 0 then 'Prihodi' when 1 then 'Rashodi' when 2 then 'Rate' when 3 then 'NetBanking'end) vrsta, "
					+ "abs(SUM(iznos))sumaIznos from racuni "
					+ "where datum between '2011-"
					+ month
					+ "-1' and '"
					+ EOMdate + "' group by tip";
		} else {
			SQLquery = "select R.kategorija,(case R.kategorija when 0 then 'OSTALO' else (select C.Name where R.kategorija = C.ID)end)vrsta, "
					+ " C.R,C.G,C.B,abs(sum(R.Iznos))sumaIznos from racuni R left OUTER JOIN categories C on R.kategorija=C.ID"
					+ " where R.datum between '2011-"
					+ month
					+ "-1' and '"
					+ EOMdate
					+ "' group by R.kategorija,C.R,C.G,C.B,C.ID,C.Name";
		}
		rs = RSet.openRS(SQLquery);

		PiePlot ColorConfigurator = (PiePlot) chart.getPlot(); /*
																 * get PiePlot
																 * object for
																 * changing
																 */
		if (tip == 0) {

			/*
			 * We can now use setSectionPaint method to change the color of our
			 * chart
			 */
			// ColorConfigurator.setSectionPaint("Java", new Color(255, 160,
			// 255));
			// ColorConfigurator.setSectionPaint("C++", Color.RED);
			// ColorConfigurator.setSectionPaint("C", Color.BLUE);
			// ColorConfigurator.setSectionPaint("VB", Color.GREEN);
			// ColorConfigurator.setSectionPaint("Shell Script", Color.YELLOW);
		} else {
			try {
				while (rs.next()) {
					ColorConfigurator.setSectionPaint(
							rs.getString("vrsta"),
							new Color(rs.getInt("R"), rs.getInt("G"), rs
									.getInt("B")));
				}
				rs.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				logger.loggErr("graphMonth " + e.getMessage() + " ("
						+ e.getErrorCode() + ")");
			}
		}

		// PiePlot ColorConfigurator = (PiePlot) chart.getPlot(); /* get PiePlot
		// object for changing */
		// /* We can now use setSectionPaint method to change the color of our
		// chart */
		// ColorConfigurator.setSectionPaint("Java", new Color(255, 160, 255));
		// ColorConfigurator.setSectionPaint("C++", Color.RED);
		// ColorConfigurator.setSectionPaint("C", Color.BLUE);
		// ColorConfigurator.setSectionPaint("VB", Color.GREEN);
		// ColorConfigurator.setSectionPaint("Shell Script", Color.YELLOW);

		ColorConfigurator.setExplodePercent("Shell Script", 0.30);
		/*
		 * A format mask specified to display labels. Here {0} is the section
		 * name, and {1} is the value. We can also use {2} which will display a
		 * percent value
		 */
		ColorConfigurator
				.setLabelGenerator(new StandardPieSectionLabelGenerator(
						"{0}:{1}"));
		/* Set color of the label background on the pie chart */
		ColorConfigurator.setLabelBackgroundPaint(new Color(220, 220, 220));
		ColorConfigurator.setLabelBackgroundPaint(Color.white);
		ColorConfigurator.setExplodePercent("Shell Script", 0.30);

		return chart;

	}
}