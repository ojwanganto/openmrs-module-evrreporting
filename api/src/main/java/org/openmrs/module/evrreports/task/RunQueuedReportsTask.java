package org.openmrs.module.evrreports.task;

import org.openmrs.module.evrreports.ReportQueueProcessor;

/**
 * Scheduled task for running queued reports
 */
public class RunQueuedReportsTask extends AMRSReportsTask {

	// Instance of processor
	private static ReportQueueProcessor processor = null;

	/**
	 * Default Constructor (Uses SchedulerConstants.username and SchedulerConstants.password
	 */
	public RunQueuedReportsTask() {
		if (processor == null) {
			processor = new ReportQueueProcessor();
		}
	}

	/**
	 * Process the next queued item
	 */
	public void doExecute() {
		processor.processQueuedReports();
	}

}