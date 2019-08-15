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
import org.openmrs.module.evrreports.reporting.cohort.definition.VaccinatedClientsCohortDefinition;
import org.openmrs.module.reporting.cohort.EvaluatedCohort;
import org.openmrs.module.reporting.cohort.definition.CohortDefinition;
import org.openmrs.module.reporting.cohort.definition.evaluator.CohortDefinitionEvaluator;
import org.openmrs.module.reporting.evaluation.EvaluationContext;
import org.openmrs.module.reporting.evaluation.EvaluationException;
import org.openmrs.module.reporting.evaluation.querybuilder.SqlQueryBuilder;
import org.openmrs.module.reporting.evaluation.service.EvaluationService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Evaluator for patients for HTS Register
 */
@Handler(supports = {VaccinatedClientsCohortDefinition.class})
public class VaccinatedClientsCohortDefinitionEvaluator implements CohortDefinitionEvaluator {

	private final Log log = LogFactory.getLog(this.getClass());
	@Autowired
	EvaluationService evaluationService;

	@Override
	public EvaluatedCohort evaluate(CohortDefinition cohortDefinition, EvaluationContext context) throws EvaluationException {

		VaccinatedClientsCohortDefinition definition = (VaccinatedClientsCohortDefinition) cohortDefinition;

		if (definition == null)
			return null;

		String tableColumn = definition.getVaccineTableColumn();
		Cohort newCohort = new Cohort();

		//String qry = "select patient_id, IFNULL(:vaccineColumn, '') from openmrs_etl.etl_immunisations ";
		//String qry = "select i.patient_id from openmrs_etl.etl_immunisations i inner join openmrs_etl.etl_patient_demographics d on i.patient_id=d.patient_id where date(i.:vaccineColumn) between date(:startDate) and date(:endDate) and d.health_facility_id in (:facilityList);";
		String qry = "select i.patient_id from openmrs_etl.etl_immunisations i inner join openmrs_etl.etl_patient_demographics d on i.patient_id=d.patient_id where date(i.:vaccineColumn) between date(:startDate) and date(:endDate);";

		qry = qry.replaceAll(":vaccineColumn", tableColumn);
		/*String qry = "select d.patient_id \n" +
				"from openmrs_etl.etl_patient_demographics d left join openmrs_etl.etl_immunisations i \n" +
				"on d.patient_id = i.patient_id where (d.date_created between :startDate and :endDate) and d.health_facility_id in (:facilityList)";
*/
		SqlQueryBuilder builder = new SqlQueryBuilder();
		builder.append(qry);
		Date startDate = (Date) context.getParameterValue("startDate");
		Date endDate = (Date) context.getParameterValue("endDate");
		Set<Location> facilityList = (Set<Location>) context.getParameterValue("facilityList");

		System.out.println("Evaluating columns: " + facilityList);

		builder.addParameter("endDate", endDate);
		builder.addParameter("startDate", startDate);
		//builder.addParameter("facilityList", facilityList);
		List<Integer> ptIds = evaluationService.evaluateToList(builder, Integer.class, context);

		newCohort.setMemberIds(new HashSet<Integer>(ptIds));


		return new EvaluatedCohort(newCohort, definition, context);
	}

}

