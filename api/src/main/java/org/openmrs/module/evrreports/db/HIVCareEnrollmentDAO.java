package org.openmrs.module.evrreports.db;

import org.openmrs.Patient;
import org.openmrs.module.evrreports.HIVCareEnrollment;

/**
 * DAO for HIVCareEnrollments
 */
public interface HIVCareEnrollmentDAO {

	public HIVCareEnrollment getHIVCareEnrollmentForPatient(Patient patient);

	public HIVCareEnrollment saveHIVCareEnrollment(HIVCareEnrollment hce);
}
