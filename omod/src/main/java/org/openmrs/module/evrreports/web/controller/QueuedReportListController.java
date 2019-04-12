package org.openmrs.module.evrreports.web.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.api.context.Context;
import org.openmrs.module.evrreports.QueuedReport;
import org.openmrs.module.evrreports.service.QueuedReportService;
import org.openmrs.util.OpenmrsUtil;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * controller for View AMRS Reports page
 */
@Controller
public class QueuedReportListController {

	private static final Log log = LogFactory.getLog(QueuedReportListController.class);

	@RequestMapping(method = RequestMethod.GET, value = "module/evrreports/queuedReport.list")
	public String preparePage() {
		return "module/evrreports/queuedReportList";
	}

	@RequestMapping(value = "/module/evrreports/downloadxls")
	public void downloadXLS(HttpServletResponse response,
	                        @RequestParam(required = true, value = "reportId") Integer reportId) throws IOException {

		if (reportId == null) {
			// TODO say something ...
			return;
		}

		QueuedReport report = Context.getService(QueuedReportService.class).getQueuedReport(reportId);

		if (report == null) {
			// TODO say something ...
			return;
		}

		String folderName = Context.getAdministrationService().getGlobalProperty("evrreports.file_dir");

		File fileDir = OpenmrsUtil.getDirectoryInApplicationDataDirectory(folderName);
		File amrsFileToDownload = new File(fileDir, report.getXlsFilename());

		response.setContentType("application/vnd.ms-excel");
		response.setHeader("Content-Disposition", "attachment; filename=" + report.getXlsFilename());
		response.setContentLength((int) amrsFileToDownload.length());

		FileCopyUtils.copy(new FileInputStream(amrsFileToDownload), response.getOutputStream());
	}

}
