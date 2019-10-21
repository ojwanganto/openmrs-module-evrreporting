package org.openmrs.module.evrreports.db;

import org.openmrs.Location;
import org.openmrs.module.evrreports.MOHFacility;
import org.openmrs.module.evrreports.QueuedReport;

import java.util.Date;
import java.util.List;

/**
 * DAO for QueuedReport objects
 */
public interface QueuedReportDAO {

	public QueuedReport saveQueuedReport(QueuedReport queuedReport);

	public QueuedReport getNextQueuedReport(Date date);

	public void purgeQueuedReport(QueuedReport queuedReport);

	public List<QueuedReport> getAllQueuedReports();

	public List<QueuedReport> getQueuedReportsWithStatus(String status);

	public QueuedReport getQueuedReport(Integer reportId);

	public List<QueuedReport> getQueuedReportsByFacilities(List<Location> allowedFacilities, String status);

}
