package org.openmrs.module.evrreports;

import org.openmrs.BaseOpenmrsObject;
import org.openmrs.Location;

import java.util.Date;

/**
 * All information required to generate a report
 */
public class QueuedReport extends BaseOpenmrsObject {

	private Integer queuedReportId;
	private String reportName;
	private Location facility;
	private Date evaluationDate = new Date();
	private Date dateScheduled = new Date();
	private String status = STATUS_NEW;
    private Integer repeatInterval = 0;
	private String csvFilename;
	private String xlsFilename;
	private Location county;
	private Location subCounty;
	private Location ward;

	public static final String STATUS_NEW = "NEW";
	public static final String STATUS_ERROR = "ERROR";
	public static final String STATUS_RUNNING = "RUNNING";
	public static final String STATUS_COMPLETE = "COMPLETE";

	public Integer getQueuedReportId() {
		return queuedReportId;
	}

	public void setQueuedReportId(Integer queuedReportId) {
		this.queuedReportId = queuedReportId;
	}

	public String getReportName() {
		return reportName;
	}

	public void setReportName(String reportName) {
		this.reportName = reportName;
	}

	public Date getEvaluationDate() {
		return evaluationDate;
	}

	public void setEvaluationDate(Date evaluationDate) {
		this.evaluationDate = evaluationDate;
	}

	public Date getDateScheduled() {
		return dateScheduled;
	}

	public void setDateScheduled(Date dateScheduled) {
		this.dateScheduled = dateScheduled;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public Integer getId() {
		return getQueuedReportId();
	}

	@Override
	public void setId(Integer id) {
		setQueuedReportId(id);
	}

	public String getCsvFilename() {
		return csvFilename;
	}

	public void setCsvFilename(String csvFilename) {
		this.csvFilename = csvFilename;
	}

	public String getXlsFilename() {
		return xlsFilename;
	}

	public void setXlsFilename(String xlsFilename) {
		this.xlsFilename = xlsFilename;
	}

    public Integer getRepeatInterval() {
        return repeatInterval;
    }

    public void setRepeatInterval(Integer repeatInterval) {
        this.repeatInterval = repeatInterval;
    }

	public Location getFacility() {
		return facility;
	}

	public void setFacility(Location facility) {
		this.facility = facility;
	}

	public Location getCounty() {
		return county;
	}

	public void setCounty(Location county) {
		this.county = county;
	}

	public Location getSubCounty() {
		return subCounty;
	}

	public void setSubCounty(Location subCounty) {
		this.subCounty = subCounty;
	}

	public Location getWard() {
		return ward;
	}

	public void setWard(Location ward) {
		this.ward = ward;
	}
}
