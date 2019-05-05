/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.evrreports.reporting.cohort.definition.evaluator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Cohort;
import org.openmrs.Location;
import org.openmrs.annotation.Handler;
import org.openmrs.api.context.Context;
import org.openmrs.module.evrreports.reporting.cohort.definition.Moh361BCohortDefinition;
import org.openmrs.module.evrreports.reporting.cohort.definition.Moh510CohortDefinition;
import org.openmrs.module.reporting.cohort.EvaluatedCohort;
import org.openmrs.module.reporting.cohort.definition.CohortDefinition;
import org.openmrs.module.reporting.cohort.definition.SqlCohortDefinition;
import org.openmrs.module.reporting.cohort.definition.evaluator.CohortDefinitionEvaluator;
import org.openmrs.module.reporting.cohort.definition.service.CohortDefinitionService;
import org.openmrs.module.reporting.evaluation.EvaluationContext;
import org.openmrs.module.reporting.evaluation.EvaluationException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Evaluator for patients for HTS Register
 */
@Handler(supports = {Moh510CohortDefinition.class})
public class Moh510CohortDefinitionEvaluator implements CohortDefinitionEvaluator {

	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	private final Log log = LogFactory.getLog(this.getClass());

	@Override
	public EvaluatedCohort evaluate(CohortDefinition cohortDefinition, EvaluationContext context) throws EvaluationException {

		Moh361BCohortDefinition definition = (Moh361BCohortDefinition) cohortDefinition;

		if (definition == null)
			return null;


		if (definition.getFacility() == null)
			return null;


		String reportDate = sdf.format(context.getEvaluationDate());
		List<Location> locationList = new ArrayList<Location>();
		locationList.addAll(definition.getFacility().getLocations());
		context.addParameterValue("locationList", locationList);

		String sql =
				"select patient_id" +
						" from amrsreports_hiv_care_enrollment " +
						" where " +
						"  first_arv_date is not NULL" +
						"  and enrollment_date is not NULL" +
						"  and first_arv_date <= ':reportDate'" +
						"  and first_arv_location_id in ( :locationList )";

        /*add transfer ins*/

		for (Location location : locationList) {
			String personAttributeQuery =
					" union " +
							" select pa.person_id" +
							" from person_attribute pa join amrsreports_hiv_care_enrollment ae" +
							"     on pa.person_id = ae.patient_id" +
							"       and ae.first_arv_date is not null" +
							"       and ae.first_arv_date <= ':reportDate'" +
							"   join encounter e " +
							"     on e.patient_id = pa.person_id" +
							"       and e.voided = 0" +
							"       and e.location_id in ( :locationList )" +
							" where (pa.voided = 0" +
							"        or (pa.voided = 1 and pa.void_reason like 'New value: %'))" +
							"   and pa.date_created >= ae.first_arv_date" +
							"   and pa.person_attribute_type_id = 7" +
							"   and pa.value = '" + location.getLocationId() + "'" +
							"   and pa.date_created <= ':reportDate'";

			sql = sql + personAttributeQuery;
		}



		SqlCohortDefinition sqlCohortDefinition = new SqlCohortDefinition(sql.replaceAll(":reportDate", reportDate));
		Cohort results = Context.getService(CohortDefinitionService.class).evaluate(sqlCohortDefinition, context);

		return new EvaluatedCohort(results, sqlCohortDefinition, context);
	}
}
