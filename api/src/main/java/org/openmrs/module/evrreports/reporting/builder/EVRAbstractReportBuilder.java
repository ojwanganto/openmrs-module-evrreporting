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

import org.openmrs.module.reporting.dataset.definition.DataSetDefinition;
import org.openmrs.module.reporting.evaluation.parameter.Mapped;
import org.openmrs.module.reporting.evaluation.parameter.Parameter;
import org.openmrs.module.reporting.report.ReportDesign;
import org.openmrs.module.reporting.report.definition.ReportDefinition;

import java.util.List;

/**
 * Abstract base class for all report builders
 */
public abstract class EVRAbstractReportBuilder {

	/**
	 * Builds the report definition
	 * @return the report definition
	 */

	public ReportDefinition build(String name, String description) {
		ReportDefinition definition = new ReportDefinition();
		definition.setName(name);
		definition.setDescription(description);
		definition.addParameters(getParameters());


		// Add all datasets
		for (Mapped<DataSetDefinition> dataset : buildDataSets(definition)) {
			definition.addDataSetDefinition(dataset.getParameterizable().getName(), dataset);
		}

		return definition;
	}

	/**
	 * Gets the report parameters
	 * @return the report parameters
	 */
	protected abstract List<Parameter> getParameters();

	/**
	 * Builds and maps the data sets
	 * @param report the report definition
	 * @return the mapped datasets
	 */
	protected abstract List<Mapped<DataSetDefinition>> buildDataSets(ReportDefinition report);

	protected abstract ReportDesign getReportDesign();
}