package org.openmrs.module.evrreports.util;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Cohort;
import org.openmrs.Concept;
import org.openmrs.module.evrreports.AmrsReportsConstants;
import org.openmrs.module.evrreports.MOHFacility;
import org.openmrs.module.evrreports.cache.MohCacheUtils;
import org.openmrs.module.reporting.cohort.definition.CohortDefinition;
import org.openmrs.module.reporting.dataset.DataSet;
import org.openmrs.module.reporting.dataset.DataSetColumn;
import org.openmrs.module.reporting.dataset.DataSetRow;
import org.openmrs.module.reporting.evaluation.parameter.Mapped;
import org.openmrs.module.reporting.evaluation.parameter.Parameter;
import org.openmrs.module.reporting.evaluation.parameter.Parameterizable;
import org.openmrs.module.reporting.evaluation.parameter.ParameterizableUtil;
import org.openmrs.module.reporting.indicator.CohortIndicator;
import org.openmrs.module.reporting.indicator.IndicatorResult;
import org.openmrs.module.reporting.report.ReportData;
import org.openmrs.util.OpenmrsUtil;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Helper utility for running reports and not overloading the system
 */
public class MOHReportUtil {

	private static final Log log = LogFactory.getLog(MOHReportUtil.class);
	public static final String DATE_FORMAT = "dd/MM/yyyy";

	private static Map<Integer, String> tableDrugs;

	public static String joinAsSingleCell(Collection<String> entries) {
		return StringUtils.join(entries, AmrsReportsConstants.INTER_CELL_SEPARATOR);
	}

	public static String joinAsSingleCell(String... entries) {
		return joinAsSingleCell(Arrays.asList(entries));
	}

	public static DataSet getFirstDataSetForReportData(ReportData reportData) {
		return reportData.getDataSets().values().iterator().next();
	}

	public static List<String> getColumnsFromReportData(ReportData results) {
		DataSet dataset = getFirstDataSetForReportData(results);
		List<DataSetColumn> columns = dataset.getMetaData().getColumns();

		List<String> columnNames = new ArrayList<String>();
		for (DataSetColumn column : columns) {
			columnNames.add(column.getLabel());
		}

		return columnNames;
	}

	/**
	 * builds a CSV from a {@link ReportData} object, writing to the reference OutputStream
	 */
	public static void renderCSVFromReportData(ReportData results, OutputStream out) throws IOException {

		CSVWriter w = new CSVWriter(new OutputStreamWriter(out, "UTF-8"), AmrsReportsConstants.DEFAULT_CSV_DELIMITER);

		DataSet dataset = getFirstDataSetForReportData(results);

		List<DataSetColumn> columns = dataset.getMetaData().getColumns();

		String[] outRow = new String[columns.size()];

		// header row
		int i = 0;
		for (DataSetColumn column : columns) {
			outRow[i++] = column.getLabel();
		}
		w.writeNext(outRow);

		// data rows
		for (DataSetRow row : dataset) {
			i = 0;
			for (DataSetColumn column : columns) {
				Object colValue = row.getColumnValue(column);
				if (colValue != null) {
					if (colValue instanceof Cohort) {
						outRow[i] = Integer.toString(((Cohort) colValue).size());
					} else if (colValue instanceof IndicatorResult) {
						outRow[i] = ((IndicatorResult) colValue).getValue().toString();
					} else {
						outRow[i] = colValue.toString();
					}
				} else {
					outRow[i] = "";
				}
				i++;
			}
			w.writeNext(outRow);
		}

		w.flush();
	}

	/**
	 * renders a map containing column headers and records, defaulting to empty array lists, from an input stream
	 */
	public static Map<String, Object> renderDataSetFromCSV(InputStream in) throws IOException {
		CSVReader r = new CSVReader(new InputStreamReader(in, "UTF-8"), AmrsReportsConstants.DEFAULT_CSV_DELIMITER);
		List<String[]> contents = r.readAll();

		Map<String, Object> results = new HashMap<String, Object>();
		results.put("columnHeaders", contents != null && contents.size() > 0 ? contents.remove(0) : new ArrayList());
		results.put("records", contents != null ? contents : new ArrayList());
		return results;
	}

	public static String formatdates(Date date) {
		if (date == null)
			return "Unknown";

		Format formatter;
		formatter = new SimpleDateFormat(DATE_FORMAT);
		String s = formatter.format(date);

		return s;

	}


	/**
	 * helper method to reduce code for validation methods
	 *
	 * @param concept
	 * @param name
	 * @return
	 */
	public static boolean compareConceptToName(Concept concept, String name) {
		return OpenmrsUtil.nullSafeEquals(concept, MohCacheUtils.getConcept(name));
	}

	/**
	 * Creates a new cohort indicator
	 * @param name the indicator name
	 * @param cohort the mapped cohort
	 * @return the cohort indicator
	 */
	public static CohortIndicator cohortIndicator(String name, Mapped<CohortDefinition> cohort) {
		CohortIndicator ind = new CohortIndicator(name);
		ind.addParameter(new Parameter("startDate", "Start Date", Date.class));
		ind.addParameter(new Parameter("endDate", "End Date", Date.class));
		ind.addParameter(new Parameter("facilityList", "Facility List", MOHFacility.class));

		ind.setCohortDefinition(cohort);
		return ind;
	}

	/**
	 * Creates a new cohort indicator with numerator and a denominator cohort
	 * @param name the indicator name
	 * @param numeratorCohort the mapped numerator cohort
	 * @param denominatorCohort the mapped denominator cohort
	 * @return the cohort indicator
	 */
	public static CohortIndicator cohortIndicator(String name, Mapped<CohortDefinition> numeratorCohort, Mapped<CohortDefinition> denominatorCohort) {
		CohortIndicator ind = cohortIndicator(name, numeratorCohort);
		ind.setDenominator(denominatorCohort);
		ind.setType(CohortIndicator.IndicatorType.FRACTION);
		return ind;
	}

	/**
	 * Maps a parameterizable item with no parameters
	 * @param parameterizable the parameterizable item
	 * @param <T>
	 * @return the mapped item
	 */
	public static <T extends Parameterizable> Mapped<T> map(T parameterizable) {
		if (parameterizable == null) {
			throw new IllegalArgumentException("Parameterizable cannot be null");
		}
		return new Mapped<T>(parameterizable, null);
	}

	/**
	 * Maps a parameterizable item using a string list of parameters and values
	 * @param parameterizable the parameterizable item
	 * @param mappings the string list of mappings
	 * @param <T>
	 * @return the mapped item
	 */
	public static <T extends Parameterizable> Mapped<T> map(T parameterizable, String mappings) {
		if (parameterizable == null) {
			throw new IllegalArgumentException("Parameterizable cannot be null");
		}
		if (mappings == null) {
			mappings = ""; // probably not necessary, just to be safe
		}
		return new Mapped<T>(parameterizable, ParameterizableUtil.createParameterMappings(mappings));
	}

	/**
	 * Maps a parameterizable item using a string list of parameters and values
	 * @param parameterizable the parameterizable item
	 * @param mappings the string list of mappings
	 * @param <T>
	 * @return the mapped item
	 */
	public static <T extends Parameterizable> Mapped<T> map(T parameterizable, Object ... mappings) {
		if (parameterizable == null) {
			throw new IllegalArgumentException("Parameterizable cannot be null");
		}

		Map<String, Object> paramMap = new HashMap<String, Object>();
		for (int m = 0; m < mappings.length; m += 2) {
			String param = (String) mappings[m];
			Object value = mappings[m + 1];
			paramMap.put(param, value);
		}
		return new Mapped<T>(parameterizable, paramMap);
	}
}
