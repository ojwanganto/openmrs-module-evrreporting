/**
 * The contents of this file are subject to the OpenMRS Public License
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * Copyright (C) OpenMRS, LLC.  All Rights Reserved.
 */

package org.openmrs.module.evrreports.reporting.builder;

import org.openmrs.module.reporting.cohort.definition.CohortDefinition;
import org.openmrs.module.reporting.dataset.definition.DataSetDefinition;
import org.openmrs.module.reporting.dataset.definition.PatientDataSetDefinition;
import org.openmrs.module.reporting.evaluation.parameter.Mapped;
import org.openmrs.module.reporting.evaluation.parameter.Parameter;
import org.openmrs.module.reporting.report.definition.ReportDefinition;

import java.util.Arrays;
import java.util.List;

/**
 * Abstract base class for report builders which build cohort reports - i.e. one row-per-patient dataset
 */
public abstract class EVRAbstractCohortReportBuilder extends EVRAbstractReportBuilder {

	/**
	 * @see EVRAbstractReportBuilder
	 */
	@Override
	protected List<Parameter> getParameters() {
		return Arrays.asList();
	}

	/**
	 * @see EVRAbstractReportBuilder
	 */
	@Override
	protected List<Mapped<DataSetDefinition>> buildDataSets(ReportDefinition rd) {
		PatientDataSetDefinition dsd = new PatientDataSetDefinition("name" + " DSD");
		dsd.addParameters(rd.getParameters()); // Same parameters as report

		Mapped<CohortDefinition> cohort = buildCohort(dsd);

		dsd.addRowFilter(cohort);

		addColumns(dsd);

		// Map all parameters straight through
		return Arrays.asList(new Mapped<DataSetDefinition>(dsd, Mapped.straightThroughMappings(dsd)));
	}

	/**
	 * Builds and maps the cohort to base this cohort report on
	 * @param dsd the data set definition
	 * @return the mapped cohort definition
	 */
	protected abstract Mapped<CohortDefinition> buildCohort(PatientDataSetDefinition dsd);

	/**
	 * Override this if you don't want the default (HIV ID, name, sex, age)
	 * @param dsd this will be modified by having columns added
	 */
	protected void addColumns(PatientDataSetDefinition dsd) {

	}


}
