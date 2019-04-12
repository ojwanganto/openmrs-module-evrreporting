package org.openmrs.module.evrreports.service.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Patient;
import org.openmrs.api.impl.BaseOpenmrsService;
import org.openmrs.module.evrreports.HIVCareEnrollment;
import org.openmrs.module.evrreports.db.HIVCareEnrollmentDAO;
import org.openmrs.module.evrreports.service.HIVCareEnrollmentService;

/**
 * implementation of HIVCareEnrollmentService
 */
public class HIVCareEnrollmentServiceImpl extends BaseOpenmrsService implements HIVCareEnrollmentService {

	private HIVCareEnrollmentDAO dao;
	private final Log log = LogFactory.getLog(this.getClass());

	public void setDao(HIVCareEnrollmentDAO dao) {
		this.dao = dao;
	}

	@Override
	public HIVCareEnrollment getHIVCareEnrollmentForPatient(Patient patient) {
		return dao.getHIVCareEnrollmentForPatient(patient);
	}

	@Override
	public HIVCareEnrollment saveHIVCareEnrollment(HIVCareEnrollment hce) {
		return dao.saveHIVCareEnrollment(hce);
	}
}
