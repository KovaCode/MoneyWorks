import java.awt.Color;
import java.awt.Font;
import java.awt.event.WindowEvent;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JPanel;

import org.eclipse.swt.graphics.Image;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.annotations.XYTextAnnotation;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CombinedRangeXYPlot;
import org.jfree.chart.plot.DatasetRenderingOrder;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.StandardXYItemRenderer;
import org.jfree.chart.renderer.xy.XYAreaRenderer;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.time.Month;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.time.Year;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

/**
 * A demo showing a combined time series chart where one of the subplots is an
 * overlaid chart.
 */
public class graphYear extends ApplicationFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static ResultSet rs;
	static XYPlot plot1;
	static XYPlot plot2;
	static double x;
	static XYTextAnnotation annotation;
	static XYTextAnnotation annotation2;
	static double x2;

	/**
	 * Creates a new demo application.
	 * 
	 * @param title
	 *            the frame title.
	 */
	public graphYear(String title) {
		super(title);
		setContentPane(createDemoPanel());

	}

	public void windowClosing(final WindowEvent evt) {
		if (evt.getWindow() == this) {
			dispose();

		}
	}

	/**
	 * Creates a panel for the demo (used by SuperDemo.java).
	 * 
	 * @return A panel.
	 */
	public static JPanel createDemoPanel() {
		// create the first dataset...

		rs = RSet
				.openRS("select distinct year (datum)godina,SUM(iznos)sumaIznos from racuni where YEAR(datum) between 2000 and 2050 group by YEAR(datum)");

		TimeSeries series1 = new TimeSeries("Godišnji prikaz", Year.class);

		try {
			while (rs.next()) {
				series1.add(new Year(rs.getInt("godina")),
						rs.getDouble("sumaIznos"));
			}
			rs.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.loggErr("graphYear " + e.getMessage() + " ("
					+ e.getErrorCode() + ")");
		}

		TimeSeriesCollection dataset1 = new TimeSeriesCollection(series1);
		dataset1.setDomainIsPointsInTime(false);

		rs = RSet
				.openRS("select SUM(iznos)as sumaIznos,Year(datum)god, Month(datum)mj from racuni  where Datum between '2010/1/1' and '2050/12/31' GROUP BY Year(datum), Month(datum) order by god,mj");

		TimeSeries series2A = new TimeSeries("Mjeseèni prikaz 2010-2012",
				Month.class);
		try {
			while (rs.next()) {
				series2A.add(new Month(rs.getInt("mj"), rs.getInt("god")),
						rs.getDouble("sumaIznos"));
			}
			rs.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.loggErr("graphYear " + e.getMessage() + " ("
					+ e.getErrorCode() + ")");
		}

		TimeSeriesCollection dataset2 = new TimeSeriesCollection();
		dataset2.addSeries(series2A);

		XYItemRenderer renderer = new XYBarRenderer(0.20);
		plot1 = new XYPlot(dataset1, new DateAxis("Godine"), null, renderer);
		plot1.setBackgroundPaint(Color.WHITE);

		renderer = plot1.getRenderer();
		renderer.setSeriesPaint(0, Color.RED);

		XYItemRenderer renderer2 = new XYAreaRenderer();
		plot2 = new XYPlot(dataset2, new DateAxis("Mjeseci"), null, renderer2);

		renderer2 = plot2.getRenderer();
		renderer2.setSeriesPaint(0, Color.GREEN);

		popuniPodatke();

		StandardXYItemRenderer renderer3 = new StandardXYItemRenderer(
				StandardXYItemRenderer.SHAPES_AND_LINES);
		renderer3.setShapesFilled(true);

		// plot2.setDataset(1, dataset3);
		plot2.setRenderer(1, renderer3);
		plot2.setDatasetRenderingOrder(DatasetRenderingOrder.FORWARD);

		NumberAxis rangeAxis = new NumberAxis("Vrijednost (kn)");
		rangeAxis.setAutoRangeIncludesZero(false);
		CombinedRangeXYPlot combinedPlot = new CombinedRangeXYPlot(rangeAxis);
		combinedPlot.add(plot1, 1);
		combinedPlot.add(plot2, 4);

		// create the chart...
		JFreeChart chart = new JFreeChart("",
				new Font("Tahoma", Font.PLAIN, 9), combinedPlot, true);
		// add the chart to a panel...
		ChartPanel chartPanel = new ChartPanel(chart);
		chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
		return chartPanel;
	}

	/**
	 * Starting point for the demonstration application.
	 * 
	 * @param args
	 *            ignored.
	 */
	public static void main(String[] args) {

		graphYear demo = new graphYear("Ukupni pregled mjesec/godina");
		demo.pack();
		RefineryUtilities.centerFrameOnScreen(demo);
		demo.setVisible(true);

	}

	private static void popuniPodatke() {
		// godine
		rs = RSet
				.openRS(" select distinct year(datum)godina,SUM(iznos)sumaIznos from racuni where YEAR(datum) between 2000 and 2050 group by YEAR(datum)");
		try {
			while (rs.next()) {
				x = new Year(rs.getInt("godina")).getMiddleMillisecond();
				annotation = new XYTextAnnotation(DEF.FormatCur(Double
						.toString(rs.getDouble("sumaIznos"))) + "kn", x,
						(rs.getInt("sumaIznos") + 150));
				annotation.setFont(new Font("Arial", Font.PLAIN, 8));
				plot1.addAnnotation(annotation);
			}
			rs.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.loggErr("graphYear " + e.getMessage() + " ("
					+ e.getErrorCode() + ")");
		}

		// mjeseci
		rs = RSet
				.openRS("select SUM(iznos)as sumaIznos,Year(datum)god, Month(datum)mj from racuni  where Datum between '2010/1/1' and '2050/12/31' GROUP BY Year(datum), Month(datum) order by god,mj");
		try {
			while (rs.next()) {
				x2 = new Month(rs.getInt("mj"), rs.getInt("god"))
						.getMiddleMillisecond();
				annotation2 = new XYTextAnnotation(DEF.FormatCur(Double
						.toString(rs.getDouble("sumaIznos"))) + "kn", x2,
						(rs.getInt("sumaIznos") + 150));
				annotation2.setFont(new Font("Arial", Font.PLAIN, 8));
				plot2.addAnnotation(annotation2);
			}
			rs.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.loggErr("graphYear " + e.getMessage() + " ("
					+ e.getErrorCode() + ")");
		}

	}

}