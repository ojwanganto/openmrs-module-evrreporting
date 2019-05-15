/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.evrreports.reporting.shared.common;

import org.springframework.stereotype.Component;


/**
 * Library of common indicator definitions. All indicators require parameters ${startDate} and ${endDate}
 */
@Component
public class EVRCommonIndicatorLibrary {

	/*@Autowired
	private CommonCohortLibrary commonCohorts;

	*//**
	 * Number of patients enrolled in the given program (including transfers)
	 * @param program the program
	 * @return the indicator
	 *//*
	public CohortIndicator enrolled(Program program) {
		return cohortIndicator("new patients enrolled in " + program.getName() + " including transfers",
				MOHReportUtil.map(commonCohorts.enrolledExcludingTransfers(program), "onOrAfter=${startDate},onOrBefore=${endDate}"));
	}

	*//**
	 * Number of patients enrolled in the given program (excluding transfers)
	 * @param program the program
	 * @return the indicator
	 *//*
	public CohortIndicator enrolledExcludingTransfers(Program program) {
		return cohortIndicator("new patients enrolled in " + program.getName() + " excluding transfers",
				ReportUtils.map(commonCohorts.enrolledExcludingTransfers(program), "onOrAfter=${startDate},onOrBefore=${endDate}"));
	}

	*//**
	 * Number of patients ever enrolled in the given program (including transfers) up to ${endDate}
	 * @param program the program
	 * @return the indicator
	 *//*
	public CohortIndicator enrolledCumulative(Program program) {
		return cohortIndicator("total patients ever enrolled in " + program.getName() + " excluding transfers",
				ReportUtils.map(commonCohorts.enrolledExcludingTransfersOnDate(program), "onOrBefore=${endDate}"));
	}*/
}