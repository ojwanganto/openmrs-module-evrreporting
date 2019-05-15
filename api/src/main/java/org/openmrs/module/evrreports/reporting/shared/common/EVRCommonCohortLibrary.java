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

import org.openmrs.Program;
import org.openmrs.module.reporting.cohort.definition.AgeCohortDefinition;
import org.openmrs.module.reporting.cohort.definition.CohortDefinition;
import org.openmrs.module.reporting.cohort.definition.GenderCohortDefinition;
import org.openmrs.module.reporting.cohort.definition.ProgramEnrollmentCohortDefinition;
import org.openmrs.module.reporting.common.DurationUnit;
import org.openmrs.module.reporting.evaluation.parameter.Parameter;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;

/**
 * Library of common cohort definitions
 */
@Component
public class EVRCommonCohortLibrary {

	/**
	 * Patients who are female
	 * @return the cohort definition
	 */
	public CohortDefinition females() {
		GenderCohortDefinition cd = new GenderCohortDefinition();
		cd.setName("females");
		cd.setFemaleIncluded(true);
		return cd;
	}

	/**
	 * Patients who are male
	 * @return the cohort definition
	 */
	public CohortDefinition males() {
		GenderCohortDefinition cd = new GenderCohortDefinition();
		cd.setName("males");
		cd.setMaleIncluded(true);
		return cd;
	}

	/**
	 * Patients who at most maxAge years old on ${effectiveDate}
	 * @return the cohort definition
	 */
	public CohortDefinition agedAtMost(int maxAge) {
		AgeCohortDefinition cd = new AgeCohortDefinition();
		cd.setName("aged at most " + maxAge);
		cd.addParameter(new Parameter("effectiveDate", "Effective Date", Date.class));
		cd.setMaxAge(maxAge);
		return cd;
	}

	/**
	 * Patients who are at least minAge years old on ${effectiveDate}
	 * @return the cohort definition
	 */
	public CohortDefinition agedAtLeast(int minAge) {
		AgeCohortDefinition cd = new AgeCohortDefinition();
		cd.setName("aged at least " + minAge);
		cd.addParameter(new Parameter("effectiveDate", "Effective Date", Date.class));
		cd.setMinAge(minAge);
		return cd;
	}

	/**
	 * patients who are at least minAge years old and at most years old on ${effectiveDate}
	 * @return CohortDefinition
	 */
	public CohortDefinition agedAtLeastAgedAtMost(int minAge, int maxAge) {
		AgeCohortDefinition cd = new AgeCohortDefinition();
		cd.setName("aged between "+minAge+" and "+maxAge+" years");
		cd.addParameter(new Parameter("effectiveDate", "Effective Date", Date.class));
		cd.setMinAge(minAge);
		cd.setMaxAge(maxAge);
		return cd;
	}
	/**
	 * patients who are at least minAge months old and at most months old on ${effectiveDate}
	 * @return CohortDefinition
	 */
	public CohortDefinition agedAtLeastAgedAtMostInMonths(int minAge, int maxAge) {
		AgeCohortDefinition cd = new AgeCohortDefinition();
		cd.setName("aged between "+minAge+" and "+maxAge+" years");
		cd.addParameter(new Parameter("effectiveDate", "Effective Date", Date.class));
		cd.setMinAge(minAge);
		cd.setMaxAge(maxAge);
		cd.setMinAgeUnit(DurationUnit.MONTHS);
		cd.setMaxAgeUnit(DurationUnit.MONTHS);
		return cd;
	}



	/**
	 * Patients who were enrolled on the given programs between ${enrolledOnOrAfter} and ${enrolledOnOrBefore}
	 * @param programs the programs
	 * @return the cohort definition
	 */
	public CohortDefinition enrolled(Program... programs) {
		ProgramEnrollmentCohortDefinition cd = new ProgramEnrollmentCohortDefinition();
		cd.setName("enrolled in program between dates");
		cd.addParameter(new Parameter("enrolledOnOrAfter", "After Date", Date.class));
		cd.addParameter(new Parameter("enrolledOnOrBefore", "Before Date", Date.class));
		if (programs.length > 0) {
			cd.setPrograms(Arrays.asList(programs));
		}
		return cd;
	}


}