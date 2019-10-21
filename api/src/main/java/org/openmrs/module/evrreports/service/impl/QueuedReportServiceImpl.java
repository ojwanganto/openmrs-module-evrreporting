package org.openmrs.module.evrreports.service.impl;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.time.StopWatch;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Cohort;
import org.openmrs.Location;
import org.openmrs.api.APIException;
import org.openmrs.api.AdministrationService;
import org.openmrs.api.context.Context;
import org.openmrs.module.evrreports.AmrsReportsConstants;
import org.openmrs.module.evrreports.MOHFacility;
import org.openmrs.module.evrreports.QueuedReport;
import org.openmrs.module.evrreports.db.QueuedReportDAO;
import org.openmrs.module.evrreports.reporting.provider.ReportProvider;
import org.openmrs.module.evrreports.service.QueuedReportService;
import org.openmrs.module.evrreports.service.ReportProviderRegistrar;
import org.openmrs.module.evrreports.util.MOHReportUtil;
import org.openmrs.module.reporting.cohort.definition.CohortDefinition;
import org.openmrs.module.reporting.cohort.definition.service.CohortDefinitionService;
import org.openmrs.module.reporting.evaluation.EvaluationContext;
import org.openmrs.module.reporting.evaluation.EvaluationException;
import org.openmrs.module.reporting.evaluation.parameter.Parameter;
import org.openmrs.module.reporting.report.ReportData;
import org.openmrs.module.reporting.report.ReportDesign;
import org.openmrs.module.reporting.report.definition.ReportDefinition;
import org.openmrs.module.reporting.report.definition.service.ReportDefinitionService;
import org.openmrs.module.reporting.report.renderer.ExcelTemplateRenderer;
import org.openmrs.util.OpenmrsUtil;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Implementation of {@link QueuedReportService}
 */
public class QueuedReportServiceImpl implements QueuedReportService {

	private QueuedReportDAO dao;
	private final Log log = LogFactory.getLog(this.getClass());

	public void setDao(QueuedReportDAO dao) {
		this.dao = dao;
	}

	@Override
	public QueuedReport getNextQueuedReport() {
		return dao.getNextQueuedReport(new Date());
	}

	@Override
	public void processQueuedReport(QueuedReport queuedReport) throws EvaluationException, IOException {

		// validate
		if (queuedReport.getReportName() == null)
			throw new APIException("The queued report must reference a report provider by name.");

		if (queuedReport.getFacility() == null)
			throw new APIException("The queued report must reference a facility.");

		// find the report provider
		ReportProvider reportProvider = ReportProviderRegistrar.getInstance().getReportProviderByName(queuedReport.getReportName());
		ReportDefinition reportDefinition = reportProvider.getReportDefinition();
		reportDefinition.addParameter(new Parameter("facilityList", "facilityList", MOHFacility.class));
		reportDefinition.addParameter(new Parameter("startDate", "Start Date", Date.class));
		reportDefinition.addParameter(new Parameter("endDate", "End Date", Date.class));

		// try rendering the report
		// set up evaluation context values
		EvaluationContext evaluationContext = new EvaluationContext();
		evaluationContext.setEvaluationDate(queuedReport.getEvaluationDate());
		evaluationContext.addParameterValue("startDate", queuedReport.getDateScheduled());
		evaluationContext.addParameterValue("endDate", queuedReport.getEvaluationDate());
		evaluationContext.addParameterValue("facilityList", queuedReport.getFacility());
		evaluationContext.addContextValue("facility.name", queuedReport.getFacility().getName());
		//evaluationContext.addContextValue("facility.code", queuedReport.getFacility().getCode());
		evaluationContext.addContextValue("period.year", new SimpleDateFormat("yyyy").format(queuedReport.getEvaluationDate()));


		StopWatch timer = new StopWatch();
		timer.start();

		// get the time the report was started (not finished)
		Date startTime = Calendar.getInstance().getTime();
		String formattedStartTime = new SimpleDateFormat("yyyy-MM-dd").format(queuedReport.getDateScheduled());
		String formattedEvaluationDate = new SimpleDateFormat("yyyy-MM-dd").format(queuedReport.getEvaluationDate());
		ReportData reportData = Context.getService(ReportDefinitionService.class)
				.evaluate(reportDefinition, evaluationContext);

		timer.stop();

		// find the directory to put the file in
		AdministrationService as = Context.getAdministrationService();
		String folderName = as.getGlobalProperty("evrreports.file_dir");

		// create a new file
		String code = queuedReport.getFacility().getName();

		String csvFilename = ""
				+ queuedReport.getReportName().replaceAll(" ", "-")
				+ "_"
				+ code
				+ "_"
				+ queuedReport.getFacility().getName().replaceAll(" ", "-")
				+ "_end_date_"
				+ formattedEvaluationDate
				+ "_start_date_"
				+ formattedStartTime
				+ ".csv";

		File loaddir = OpenmrsUtil.getDirectoryInApplicationDataDirectory(folderName);
		File amrsreport = new File(loaddir, csvFilename);
		BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(amrsreport));

		// renderCSVFromReportData the CSV
		MOHReportUtil.renderCSVFromReportData(reportData, outputStream);
		outputStream.close();

		String xlsFilename = FilenameUtils.getBaseName(csvFilename) + ".xls";
		File xlsFile = new File(loaddir, xlsFilename);
		OutputStream stream = new BufferedOutputStream(new FileOutputStream(xlsFile));

		// get the report design
		final ReportDesign design = reportProvider.getReportDesign();

		// build an Excel template renderer with the report design
		ExcelTemplateRenderer renderer = new ExcelTemplateRenderer() {
			public ReportDesign getDesign(String argument) {
				return design;
			}
		};

		// render the Excel template
		renderer.render(reportData, queuedReport.getReportName(), stream);
		stream.close();

		// finish off by setting stuff on the queued report
		queuedReport.setCsvFilename(csvFilename);
		queuedReport.setXlsFilename(xlsFilename);

		// Mark QueuedReport as complete and save status
		queuedReport.setStatus(QueuedReport.STATUS_COMPLETE);
		Context.getService(QueuedReportService.class).saveQueuedReport(queuedReport);

	}

	@Override
	public QueuedReport saveQueuedReport(QueuedReport queuedReport) {
		if (queuedReport == null)
			return queuedReport;

		if (queuedReport.getStatus() == null || queuedReport.getStatus().equals("ERROR"))
			queuedReport.setStatus(QueuedReport.STATUS_NEW);


		return dao.saveQueuedReport(queuedReport);
	}

	@Override
	public void purgeQueuedReport(QueuedReport queuedReport) {
		dao.purgeQueuedReport(queuedReport);
	}

	@Override
	public List<QueuedReport> getAllQueuedReports() {
		return dao.getAllQueuedReports();
	}

	@Override
	public List<QueuedReport> getQueuedReportsWithStatus(String status) {
		return dao.getQueuedReportsWithStatus(status);

	}

	@Override
	public QueuedReport getQueuedReport(Integer reportId) {
		return dao.getQueuedReport(reportId);
	}

	@Override
	public List<QueuedReport> getQueuedReportsByFacilities(List<Location> facilities, String status) {
		return dao.getQueuedReportsByFacilities(facilities, status);
	}

}