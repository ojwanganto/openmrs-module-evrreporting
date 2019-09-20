package org.openmrs.module.evrreports.web.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.api.APIException;
import org.openmrs.api.context.Context;
import org.openmrs.module.evrreports.MOHFacility;
import org.openmrs.module.evrreports.QueuedReport;
import org.openmrs.module.evrreports.reporting.provider.ReportProvider;
import org.openmrs.module.evrreports.service.QueuedReportService;
import org.openmrs.module.evrreports.service.ReportProviderRegistrar;
import org.openmrs.module.evrreports.service.UserFacilityService;
import org.openmrs.util.OpenmrsUtil;
import org.openmrs.web.WebConstants;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


/**
 * controller for Run AMRS Reports page
 */
@Controller
@SessionAttributes("queuedReports")
public class QueuedReportFormController {

	private final Log log = LogFactory.getLog(getClass());

	private static final String FORM_VIEW = "module/evrreports/queuedReportForm";
	private static final String SUCCESS_VIEW = "redirect:queuedReport.list";

	@ModelAttribute("facilities")
	public List<MOHFacility> getFacilities() {
		return Context.getService(UserFacilityService.class).getAllowedFacilitiesForUser(Context.getAuthenticatedUser());
	}

	@ModelAttribute("reportProviders")
	public List<ReportProvider> getReportProviders() {
		return ReportProviderRegistrar.getInstance().getAllReportProviders();
	}

	@ModelAttribute("datetimeFormat")
	public String getDatetimeFormat() {
		SimpleDateFormat sdf = Context.getDateFormat();
		String format = sdf.toPattern();
		//format += " h:mm a";
		return format;
	}

	@ModelAttribute("now")
	public String getNow() {
		SimpleDateFormat sdf = new SimpleDateFormat(getDatetimeFormat());
		return sdf.format(new Date());
	}

	@RequestMapping(method = RequestMethod.POST, value = "module/evrreports/queuedReport.form")
	public String processForm(HttpServletRequest request,
							  @ModelAttribute("queuedReports") QueuedReport editedReport,
							  BindingResult errors
	) throws Exception {

        HttpSession httpSession = request.getSession();
        QueuedReportService queuedReportService = Context.getService(QueuedReportService.class);

		if (editedReport.getRepeatInterval() == null || editedReport.getRepeatInterval() < 0) {
			editedReport.setRepeatInterval(0);

		}
		queuedReportService.saveQueuedReport(editedReport);
		httpSession.setAttribute(WebConstants.OPENMRS_MSG_ATTR, "Report queued for processing.");

		return SUCCESS_VIEW;
	}

	@RequestMapping(method = RequestMethod.GET, value = "module/evrreports/queuedReport.form")
	public String editQueuedReport(
			@RequestParam(value = "queuedReportId", required = false) Integer queuedReportId,
            @RequestParam(value = "status", required = false) String status,
			ModelMap modelMap
                                ) {

		QueuedReport queuedReport = null;
        String inlineInstruction ="";

		if (queuedReportId != null)
			queuedReport = Context.getService(QueuedReportService.class).getQueuedReport(queuedReportId);

		if (queuedReport == null) {
			queuedReport = new QueuedReport();
		}

        if (OpenmrsUtil.nullSafeEquals("ERROR", status)) {
            inlineInstruction = "Review report start and end date";
            queuedReport.setDateScheduled(new Date());/*
            queuedReport.setStatus(QueuedReport.STATUS_NEW);*/
        }

		modelMap.put("queuedReports", queuedReport);
        modelMap.put("inlineInstruction",inlineInstruction);
		return FORM_VIEW;
	}

    @RequestMapping(method = RequestMethod.GET, value = "module/evrreports/removeQueuedReport.form")
    public String removeQueuedReport( HttpServletRequest request,
            @RequestParam(value = "queuedReportId", required = false) Integer queuedReportId,
            ModelMap modelMap
    ) {

        HttpSession httpSession = request.getSession();
        QueuedReportService queuedReportService = Context.getService(QueuedReportService.class);

        QueuedReport queuedReport = null;

        if (queuedReportId != null)
            queuedReport = Context.getService(QueuedReportService.class).getQueuedReport(queuedReportId);

        try {
            queuedReportService.purgeQueuedReport(queuedReport);
            httpSession.setAttribute(WebConstants.OPENMRS_MSG_ATTR, "The report was successfully removed");
            return SUCCESS_VIEW;
        }
        catch (DataIntegrityViolationException e) {
            httpSession.setAttribute(WebConstants.OPENMRS_ERROR_ATTR, "error.object.inuse.cannot.purge");
            return FORM_VIEW;
        }
        catch (APIException e) {
            httpSession.setAttribute(WebConstants.OPENMRS_ERROR_ATTR, "error.general: " + e.getLocalizedMessage());
            return FORM_VIEW;
        }

    }

	@InitBinder
	private void dateBinder(WebDataBinder binder) {
		// The date format to parse or output your dates
		SimpleDateFormat dateFormat = Context.getDateFormat();

		// Another date format for datetime
		SimpleDateFormat datetimeFormat = new SimpleDateFormat(getDatetimeFormat());

		// Register them as custom editors for the Date type
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
		binder.registerCustomEditor(Date.class, new CustomDateEditor(datetimeFormat, true));
	}

}
