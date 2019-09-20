/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.evrreports.reporting.library.MOH710;

import org.openmrs.module.evrreports.MOHFacility;
import org.openmrs.module.reporting.cohort.definition.CohortDefinition;
import org.openmrs.module.reporting.cohort.definition.SqlCohortDefinition;
import org.openmrs.module.reporting.evaluation.parameter.Parameter;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class EVRMoh710CohortLibrary {


	public EVRMoh710CohortLibrary() {
		// TODO Auto-generated constructor stub
	}



	/*"SELECT sum(bcg_lt_1) as bcg_lt_1, sum(bcg_gt_1) as bcg_gt_1, sum(opv_0) as opv_0, "
		        + "sum(opv_1_lt_1) as opv_1_lt_1, sum(opv_1_gt_1) as opv_1_gt_1, sum(opv_2_lt_1) as opv_2_lt_1, "
		        + "sum(opv_2_gt_1) as opv_2_gt_1, sum(opv_3_lt_1) as opv_3_lt_1, sum(opv_3_gt_1) as opv_3_gt_1, "
		        + "sum(ipv_lt_1) as ipv_lt_1, sum(ipv_gt_1) as ipv_gt_1, sum(penta_1_lt_1) as penta_1_lt_1, "
		        + "sum(penta_1_gt_1) as penta_1_gt_1, sum(penta_2_lt_1) as penta_2_lt_1, sum(penta_2_gt_1) as penta_2_gt_1, "
		        + "sum(penta_3_lt_1) as penta_3_lt_1, sum(penta_3_gt_1) as penta_3_gt_1, sum(pcv_1_lt_1) as pcv_1_lt_1, "
		        + "sum(pcv_1_gt_1) as pcv_1_gt_1, sum(pcv_2_lt_1) as pcv_2_lt_1, sum(pcv_2_gt_1) as pcv_2_gt_1, "
		        + "sum(pcv_3_lt_1) as pcv_3_lt_1, sum(pcv_3_gt_1) as pcv_3_gt_1, sum(rota_1_lt_1) as rota_1_lt_1, "
		        + "sum(rota_2_lt_1) as rota_2_lt_1, sum(vit_at_6) as vit_at_6, sum(yf_lt_1) as yf_lt_1, sum(yf_gt_1) as yf_gt_1, "
		        + "sum(mr_1_lt_1) as mr_1_lt_1, sum(mr_1_gt_1) as mr_1_gt_1, sum(fic) as fic, sum(vit_1) as vit_1, "
		        + "sum(vit_1_half) as vit_1_half, sum(mr_2_1_half_2) as mr_2_1_half_2, sum(mr_2_gt_2) as mr_2_gt_2, "
		        + "sum(vit_2_to_5) as vit_2_to_5 FROM openmrs_etl.etl_moh_710 t WHERE t.year=:year and t.month=:month "*/
/*mysql> desc openmrs_etl.etl_immunisations;
+------------------+---------+------+-----+---------+-------+
| Field            | Type    | Null | Key | Default | Extra |
+------------------+---------+------+-----+---------+-------+
| patient_id       | int(11) | NO   | PRI | NULL    |       |
| bcg_vx_date      | date    | YES  |     | NULL    |       |
| opv_0_vx_date    | date    | YES  |     | NULL    |       |
| opv_1_vx_date    | date    | YES  |     | NULL    |       |
| pcv_1_vx_date    | date    | YES  |     | NULL    |       |
| penta_1_vx_date  | date    | YES  |     | NULL    |       |
| rota_1_vx_date   | date    | YES  |     | NULL    |       |
| opv_2_vx_date    | date    | YES  |     | NULL    |       |
| pcv_2_vx_date    | date    | YES  |     | NULL    |       |
| penta_2_vx_date  | date    | YES  |     | NULL    |       |
| rota_2_vx_date   | date    | YES  |     | NULL    |       |
| opv_3_vx_date    | date    | YES  |     | NULL    |       |
| pcv_3_vx_date    | date    | YES  |     | NULL    |       |
| penta_3_vx_date  | date    | YES  |     | NULL    |       |
| ipv_vx_date      | date    | YES  |     | NULL    |       |
| mr_1_vx_date     | date    | YES  |     | NULL    |       |
| mr_2_vx_date     | date    | YES  |     | NULL    |       |
| mr_at_6_vx_date  | date    | YES  |     | NULL    |       |
| yf_vx_date       | date    | YES  |     | NULL    |       |
| vit_at_6_vx_date | date    | YES  |     | NULL    |       |
+------------------+---------+------+-----+---------+-------+
*/

	//Queries for MOH710
	/*Given BCG*/
	public CohortDefinition givenBCGVaccineCl() {
		SqlCohortDefinition cd = new SqlCohortDefinition();
		String sqlQuery = "select i.patient_id from openmrs_etl.etl_immunisations i inner join openmrs_etl.etl_patient_demographics d on i.patient_id=d.patient_id where date(i.bcg_vx_date) between date(:startDate) and date(:endDate) and d.health_facility_id in (:facilityList);";
		//String sqlQuery = "select i.patient_id from openmrs_etl.etl_immunisations i inner join openmrs_etl.etl_patient_demographics d on i.patient_id=d.patient_id where date(i.bcg_vx_date) between date(:startDate) and date(:endDate);";

		cd.setName("BCG");
		cd.setQuery(sqlQuery);
		cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
		cd.addParameter(new Parameter("endDate", "End Date", Date.class));
		cd.addParameter(new Parameter("facilityList", "Facility List", MOHFacility.class));
		cd.setDescription("Given BCG");

		return cd;
	}

	/*Given OPV at birth*/
	public CohortDefinition givenOPVCl() {
		SqlCohortDefinition cd = new SqlCohortDefinition();
		String sqlQuery = "select i.patient_id from openmrs_etl.etl_immunisations i inner join openmrs_etl.etl_patient_demographics d on i.patient_id=d.patient_id where date(i.opv_0_vx_date) between date(:startDate) and date(:endDate) and d.health_facility_id in (:facilityList);";

		cd.setName("OPV-0");
		cd.setQuery(sqlQuery);
		cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
		cd.addParameter(new Parameter("endDate", "End Date", Date.class));
		cd.addParameter(new Parameter("facilityList", "Facility List", MOHFacility.class));
		cd.setDescription("Given OPV at birth");

		return cd;
	}

	/*Given OPV 1*/
	public CohortDefinition givenOPV1Cl() {
		SqlCohortDefinition cd = new SqlCohortDefinition();
		String sqlQuery = "select i.patient_id from openmrs_etl.etl_immunisations i inner join openmrs_etl.etl_patient_demographics d on i.patient_id=d.patient_id where date(i.opv_1_vx_date) between date(:startDate) and date(:endDate) and d.health_facility_id in (:facilityList);";

		cd.setName("OPV-1");
		cd.setQuery(sqlQuery);
		cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
		cd.addParameter(new Parameter("endDate", "End Date", Date.class));
		cd.addParameter(new Parameter("facilityList", "Facility List", MOHFacility.class));
		cd.setDescription("Given OPV 1");

		return cd;
	}

	/*Given OPV 2*/
	public CohortDefinition givenOPV2Cl() {
		SqlCohortDefinition cd = new SqlCohortDefinition();
		String sqlQuery = "select i.patient_id from openmrs_etl.etl_immunisations i inner join openmrs_etl.etl_patient_demographics d on i.patient_id=d.patient_id where date(i.opv_2_vx_date) between date(:startDate) and date(:endDate) and d.health_facility_id in (:facilityList);";

		cd.setName("OPV-2");
		cd.setQuery(sqlQuery);
		cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
		cd.addParameter(new Parameter("endDate", "End Date", Date.class));
		cd.addParameter(new Parameter("facilityList", "Facility List", MOHFacility.class));
		cd.setDescription("Given OPV 2");

		return cd;
	}

	/*Given OPV 3*/
	public CohortDefinition givenOPV3Cl() {
		SqlCohortDefinition cd = new SqlCohortDefinition();
		String sqlQuery = "select i.patient_id from openmrs_etl.etl_immunisations i inner join openmrs_etl.etl_patient_demographics d on i.patient_id=d.patient_id where date(i.opv_3_vx_date) between date(:startDate) and date(:endDate) and d.health_facility_id in (:facilityList);";

		cd.setName("OPV-3");
		cd.setQuery(sqlQuery);
		cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
		cd.addParameter(new Parameter("endDate", "End Date", Date.class));
		cd.addParameter(new Parameter("facilityList", "Facility List", MOHFacility.class));
		cd.setDescription("Given OPV 3");

		return cd;
	}

	/*Given IPV*/
	public CohortDefinition givenIpvCl() {
		SqlCohortDefinition cd = new SqlCohortDefinition();
		String sqlQuery = "select i.patient_id from openmrs_etl.etl_immunisations i inner join openmrs_etl.etl_patient_demographics d on i.patient_id=d.patient_id where date(i.ipv_vx_date) between date(:startDate) and date(:endDate) and d.health_facility_id in (:facilityList);";

		cd.setName("IPV");
		cd.setQuery(sqlQuery);
		cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
		cd.addParameter(new Parameter("endDate", "End Date", Date.class));
		cd.addParameter(new Parameter("facilityList", "Facility List", MOHFacility.class));
		cd.setDescription("Given IPV");

		return cd;
	}

	/*Given Dpt-Hep-Hib 1*/
	public CohortDefinition givenDptHepHibVaccine1Cl() {
		SqlCohortDefinition cd = new SqlCohortDefinition();
		String sqlQuery = "select i.patient_id from openmrs_etl.etl_immunisations i inner join openmrs_etl.etl_patient_demographics d on i.patient_id=d.patient_id where date(i.penta_1_vx_date) between date(:startDate) and date(:endDate) and d.health_facility_id in (:facilityList);";

		cd.setName("DHH-1");
		cd.setQuery(sqlQuery);
		cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
		cd.addParameter(new Parameter("endDate", "End Date", Date.class));
		cd.addParameter(new Parameter("facilityList", "Facility List", MOHFacility.class));
		cd.setDescription("Given Dpt-Hep-Hib 1");

		return cd;
	}

	/*Given Dpt-Hep-Hib 2*/
	public CohortDefinition givenDptHepHibVaccine2Cl() {
		SqlCohortDefinition cd = new SqlCohortDefinition();
		String sqlQuery = "select i.patient_id from openmrs_etl.etl_immunisations i inner join openmrs_etl.etl_patient_demographics d on i.patient_id=d.patient_id where date(i.penta_2_vx_date) between date(:startDate) and date(:endDate) and d.health_facility_id in (:facilityList);";

		cd.setName("DHH-2");
		cd.setQuery(sqlQuery);
		cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
		cd.addParameter(new Parameter("endDate", "End Date", Date.class));
		cd.addParameter(new Parameter("facilityList", "Facility List", MOHFacility.class));
		cd.setDescription("Given Dpt-Hep-Hib 2");

		return cd;
	}

	/*Given Dpt-Hep-Hib 3*/
	public CohortDefinition givenDptHepHibVaccine3Cl() {
		SqlCohortDefinition cd = new SqlCohortDefinition();
		String sqlQuery = "select i.patient_id from openmrs_etl.etl_immunisations i inner join openmrs_etl.etl_patient_demographics d on i.patient_id=d.patient_id where date(i.penta_3_vx_date) between date(:startDate) and date(:endDate) and d.health_facility_id in (:facilityList);";

		cd.setName("DHH-3");
		cd.setQuery(sqlQuery);
		cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
		cd.addParameter(new Parameter("endDate", "End Date", Date.class));
		cd.addParameter(new Parameter("facilityList", "Facility List", MOHFacility.class));
		cd.setDescription("Given Dpt-Hep-Hib 3");

		return cd;
	}

	/*Given Pneumococcal 1*/
	public CohortDefinition givenPneumococcal1VaccineCl() {
		SqlCohortDefinition cd = new SqlCohortDefinition();
		String sqlQuery = "select i.patient_id from openmrs_etl.etl_immunisations i inner join openmrs_etl.etl_patient_demographics d on i.patient_id=d.patient_id where date(i.pcv_1_vx_date) between date(:startDate) and date(:endDate) and d.health_facility_id in (:facilityList);";

		cd.setName("PCV-1");
		cd.setQuery(sqlQuery);
		cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
		cd.addParameter(new Parameter("endDate", "End Date", Date.class));
		cd.addParameter(new Parameter("facilityList", "Facility List", MOHFacility.class));
		cd.setDescription("Given Pneumococcal 1");

		return cd;
	}

	/*Given Pneumococcal 2*/
	public CohortDefinition givenPneumococcal2VaccineCl() {
		SqlCohortDefinition cd = new SqlCohortDefinition();
		String sqlQuery = "select i.patient_id from openmrs_etl.etl_immunisations i inner join openmrs_etl.etl_patient_demographics d on i.patient_id=d.patient_id where date(i.pcv_2_vx_date) between date(:startDate) and date(:endDate) and d.health_facility_id in (:facilityList);";

		cd.setName("PCV-2");
		cd.setQuery(sqlQuery);
		cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
		cd.addParameter(new Parameter("endDate", "End Date", Date.class));
		cd.addParameter(new Parameter("facilityList", "Facility List", MOHFacility.class));
		cd.setDescription("Given Pneumococcal 2");

		return cd;
	}

	/*Given Pneumococcal 3*/
	public CohortDefinition givenPneumococcal3VaccineCl() {
		SqlCohortDefinition cd = new SqlCohortDefinition();
		String sqlQuery = "select i.patient_id from openmrs_etl.etl_immunisations i inner join openmrs_etl.etl_patient_demographics d on i.patient_id=d.patient_id where date(i.pcv_3_vx_date) between date(:startDate) and date(:endDate) and d.health_facility_id in (:facilityList);";

		cd.setName("PCV-3");
		cd.setQuery(sqlQuery);
		cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
		cd.addParameter(new Parameter("endDate", "End Date", Date.class));
		cd.addParameter(new Parameter("facilityList", "Facility List", MOHFacility.class));
		cd.setDescription("Given Pneumococcal 3");

		return cd;
	}

	/*Given Rota 1 vaccine*/
	public CohortDefinition givenRota1VirusVaccineCl() {
		SqlCohortDefinition cd = new SqlCohortDefinition();
		String sqlQuery = "select i.patient_id from openmrs_etl.etl_immunisations i inner join openmrs_etl.etl_patient_demographics d on i.patient_id=d.patient_id where date(i.rota_1_vx_date) between date(:startDate) and date(:endDate) and d.health_facility_id in (:facilityList);";

		cd.setName("ROTA-1");
		cd.setQuery(sqlQuery);
		cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
		cd.addParameter(new Parameter("endDate", "End Date", Date.class));
		cd.addParameter(new Parameter("facilityList", "Facility List", MOHFacility.class));
		cd.setDescription("Given Rota 1 vaccine");

		return cd;
	}

	/*Given Rota 2 vaccine*/
	public CohortDefinition givenRota2VirusVaccineCl() {
		SqlCohortDefinition cd = new SqlCohortDefinition();
		String sqlQuery = "select i.patient_id from openmrs_etl.etl_immunisations i inner join openmrs_etl.etl_patient_demographics d on i.patient_id=d.patient_id where date(i.rota_2_vx_date) between date(:startDate) and date(:endDate) and d.health_facility_id in (:facilityList);";

		cd.setName("ROTA-2");
		cd.setQuery(sqlQuery);
		cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
		cd.addParameter(new Parameter("endDate", "End Date", Date.class));
		cd.addParameter(new Parameter("facilityList", "Facility List", MOHFacility.class));
		cd.setDescription("Given Rota 2 vaccine");

		return cd;
	}

	/*Given Vitamin A at 6 Months*/
	public CohortDefinition givenVitAAt6MAgeCl() {
		SqlCohortDefinition cd = new SqlCohortDefinition();
		String sqlQuery = "select i.patient_id from openmrs_etl.etl_immunisations i inner join openmrs_etl.etl_patient_demographics d on i.patient_id=d.patient_id where date(i.vit_at_6_vx_date) between date(:startDate) and date(:endDate) and d.health_facility_id in (:facilityList);";

		cd.setName("VA6M");
		cd.setQuery(sqlQuery);
		cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
		cd.addParameter(new Parameter("endDate", "End Date", Date.class));
		cd.addParameter(new Parameter("facilityList", "Facility List", MOHFacility.class));
		cd.setDescription("Given Vitamin A at 6 Months");

		return cd;
	}

	/*Given Yellow Fever vaccine*/
	public CohortDefinition givenYellowFeverVaccineCl() {
		SqlCohortDefinition cd = new SqlCohortDefinition();
		String sqlQuery = "select i.patient_id from openmrs_etl.etl_immunisations i inner join openmrs_etl.etl_patient_demographics d on i.patient_id=d.patient_id where date(i.yf_vx_date) between date(:startDate) and date(:endDate) and d.health_facility_id in (:facilityList);";

		cd.setName("YF");
		cd.setQuery(sqlQuery);
		cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
		cd.addParameter(new Parameter("endDate", "End Date", Date.class));
		cd.addParameter(new Parameter("facilityList", "Facility List", MOHFacility.class));
		cd.setDescription("Given Yellow Fever vaccine");

		return cd;
	}

	/*Given Measles-Rubella 1 vaccine*/
	public CohortDefinition givenMeaslesRubella1VaccineCl() {
		SqlCohortDefinition cd = new SqlCohortDefinition();
		String sqlQuery = "select i.patient_id from openmrs_etl.etl_immunisations i inner join openmrs_etl.etl_patient_demographics d on i.patient_id=d.patient_id where date(i.mr_1_vx_date) between date(:startDate) and date(:endDate) and d.health_facility_id in (:facilityList);";

		cd.setName("MR-1");
		cd.setQuery(sqlQuery);
		cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
		cd.addParameter(new Parameter("endDate", "End Date", Date.class));
		cd.addParameter(new Parameter("facilityList", "Facility List", MOHFacility.class));
		cd.setDescription("Given Measles-Rubella 1 vaccine");

		return cd;
	}

	/*Fully immunized child*/
	/*public CohortDefinition fullyImmunizedCl() {
		SqlCohortDefinition cd = new SqlCohortDefinition();
		String sqlQuery = "select i.patient_id from kenyaemr_etl.etl_hei_immunization i inner join openmrs_etl.etl_patient_demographics d on i.patient_id=d.patient_id where date(i.fully_immunized) between date(:startDate) and date(:endDate) and d.health_facility_id in (:facilityList);";

		cd.setName("FIC");
		cd.setQuery(sqlQuery);
		cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
		cd.addParameter(new Parameter("endDate", "End Date", Date.class));
		cd.addParameter(new Parameter("facilityList", "Facility List", MOHFacility.class));
		cd.setDescription("Fully immunized child");

		return cd;
	}

	*//*Given Vitamin A at 1 years (200,000IU)*//*
	public CohortDefinition givenVitAAt12MonthsCl() {
		SqlCohortDefinition cd = new SqlCohortDefinition();
		String sqlQuery = "select i.patient_id from kenyaemr_etl.etl_hei_immunization i inner join openmrs_etl.etl_patient_demographics d on i.patient_id=d.patient_id where date(i.VitaminA_1_yr) between date(:startDate) and date(:endDate) and d.health_facility_id in (:facilityList);";

		cd.setName("VA-1Y");
		cd.setQuery(sqlQuery);
		cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
		cd.addParameter(new Parameter("endDate", "End Date", Date.class));
		cd.addParameter(new Parameter("facilityList", "Facility List", MOHFacility.class));
		cd.setDescription("Vitamin A at 1 years (200,000IU)");

		return cd;
	}

	*//*Given Vitamin A at 2 years to 5 years (200,000IU)*//*
	public CohortDefinition givenVitAAt18MonthsCl() {
		SqlCohortDefinition cd = new SqlCohortDefinition();
		String sqlQuery = "select i.patient_id from kenyaemr_etl.etl_hei_immunization i inner join openmrs_etl.etl_patient_demographics d on i.patient_id=d.patient_id where date(i.VitaminA_1_and_half_yr) between date(:startDate) and date(:endDate) and d.health_facility_id in (:facilityList);";

		cd.setName("VA-2Y-5Y");
		cd.setQuery(sqlQuery);
		cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
		cd.addParameter(new Parameter("endDate", "End Date", Date.class));
		cd.addParameter(new Parameter("facilityList", "Facility List", MOHFacility.class));
		cd.setDescription("Vitamin A at 2 years to 5 years (200,000IU)");

		return cd;
	}

	*//*Given Measles - Rubella 2(at 1 1/2 - 2 years)*//*
	public CohortDefinition givenVitAAt2To5YearsCl() {
		SqlCohortDefinition cd = new SqlCohortDefinition();
		String sqlQuery = "select i.patient_id from kenyaemr_etl.etl_hei_immunization i inner join openmrs_etl.etl_patient_demographics d on i.patient_id=d.patient_id where date(i.VitaminA_2_to_5_yr) between date(:startDate) and date(:endDate) and d.health_facility_id in (:facilityList);";

		cd.setName("MR-2-1.5Y>2Y");
		cd.setQuery(sqlQuery);
		cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
		cd.addParameter(new Parameter("endDate", "End Date", Date.class));
		cd.addParameter(new Parameter("facilityList", "Facility List", MOHFacility.class));
		cd.setDescription("Measles - Rubella 2(at 1 1/2 - 2 years)");

		return cd;
	}

	*//*Measles - Rubella 2(at 1 1/2 - 2 years)*//*
	public CohortDefinition givenMeaslesRubella2VaccineAge18To24MonthsCl() {
		SqlCohortDefinition cd = new SqlCohortDefinition();
		String sqlQuery = "select i.patient_id from kenyaemr_etl.etl_hei_immunization i inner join openmrs_etl.etl_patient_demographics d on i.patient_id=d.patient_id where date(i.Measles_rubella_2) between date(:startDate) and date(:endDate) and d.health_facility_id in (:facilityList);";

		cd.setName("MR-2-1.5Y>2Y");
		cd.setQuery(sqlQuery);
		cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
		cd.addParameter(new Parameter("endDate", "End Date", Date.class));
		cd.addParameter(new Parameter("facilityList", "Facility List", MOHFacility.class));
		cd.setDescription("Given Measles - Rubella 2(at 1 1/2 - 2 years)");

		return cd;
	}

	*//*Measles-Rubella 2 Above 2 years*//*
	public CohortDefinition givenMeaslesRubellaVaccine2AndAgedOver2YearsCl() {
		SqlCohortDefinition cd = new SqlCohortDefinition();
		String sqlQuery = "select i.patient_id from kenyaemr_etl.etl_hei_immunization i inner join openmrs_etl.etl_patient_demographics d on i.patient_id=d.patient_id where date(i.Measles_rubella_2) between date(:startDate) and date(:endDate) and d.health_facility_id in (:facilityList);";

		cd.setName("MR-2->2Y");
		cd.setQuery(sqlQuery);
		cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
		cd.addParameter(new Parameter("endDate", "End Date", Date.class));
		cd.addParameter(new Parameter("facilityList", "Facility List", MOHFacility.class));
		cd.setDescription("Measles-Rubella 2 Above 2 years");

		return cd;
	}

	*//*Tetanus Toxoid for pregnant women first dose*//*
	public CohortDefinition givenTTXFirstDoseCl() {
		SqlCohortDefinition cd = new SqlCohortDefinition();
		String sqlQuery = ";";

		cd.setName("TTX_FPW-1st");
		cd.setQuery(sqlQuery);
		cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
		cd.addParameter(new Parameter("endDate", "End Date", Date.class));
		cd.addParameter(new Parameter("facilityList", "Facility List", MOHFacility.class));
		cd.setDescription("Tetanus Toxoid for pregnant women first dose");

		return cd;
	}

	*//*Tetanus Toxoid for pregnant women second dose*//*
	public CohortDefinition givenTTXSecondDoseCl() {
		SqlCohortDefinition cd = new SqlCohortDefinition();
		String sqlQuery = ";";

		cd.setName("TTX_FPW-2nd");
		cd.setQuery(sqlQuery);
		cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
		cd.addParameter(new Parameter("endDate", "End Date", Date.class));
		cd.addParameter(new Parameter("facilityList", "Facility List", MOHFacility.class));
		cd.setDescription("Tetanus Toxoid for pregnant women second dose");

		return cd;
	}

	*//*Tetanus Toxoid plus(Booster) for pregnant women*//*
	public CohortDefinition givenTTXPlusCl() {
		SqlCohortDefinition cd = new SqlCohortDefinition();
		String sqlQuery = ";";

		cd.setName("TTX_FPW-TTPLUS");
		cd.setQuery(sqlQuery);
		cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
		cd.addParameter(new Parameter("endDate", "End Date", Date.class));
		cd.addParameter(new Parameter("facilityList", "Facility List", MOHFacility.class));
		cd.setDescription("Tetanus Toxoid plus(Booster) for pregnant women");

		return cd;
	}

	*//*Given Vitamin A supplement 2 -5 years (200,000 IU)*//*
	public CohortDefinition givenVitASupplementalCl() {
		SqlCohortDefinition cd = new SqlCohortDefinition();
		String sqlQuery = ";";

		cd.setName("VA-2-5Y");
		cd.setQuery(sqlQuery);
		cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
		cd.addParameter(new Parameter("endDate", "End Date", Date.class));
		cd.addParameter(new Parameter("facilityList", "Facility List", MOHFacility.class));
		cd.setDescription("2 -5 years (200,000 IU)");

		return cd;
	}

	*//*Vitamin A Supplemental Lactating Mothers(200,000 IU)*//*
	public CohortDefinition givenVitASupplementalLacCl() {
		SqlCohortDefinition cd = new SqlCohortDefinition();
		String sqlQuery = ";";

		cd.setName("VA-LAC");
		cd.setQuery(sqlQuery);
		cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
		cd.addParameter(new Parameter("endDate", "End Date", Date.class));
		cd.addParameter(new Parameter("facilityList", "Facility List", MOHFacility.class));
		cd.setDescription("Vitamin A Supplemental Lactating Mothers(200,000 IU)");

		return cd;
	}

	*//*Issued with LLITN in this Visit (under 1 year)*//*
	public CohortDefinition givenLLITNCl() {
		SqlCohortDefinition cd = new SqlCohortDefinition();
		String sqlQuery = ";";

		cd.setName("LLITN-LT1Y");
		cd.setQuery(sqlQuery);
		cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
		cd.addParameter(new Parameter("endDate", "End Date", Date.class));
		cd.addParameter(new Parameter("facilityList", "Facility List", MOHFacility.class));
		cd.setDescription("Issued with LLITN in this Visit (under 1 year)");

		return cd;
	}

	*//*Squint/White Eye Reflection under 1 year*//*
	public CohortDefinition squintWhiteEyeReflectionCl() {
		SqlCohortDefinition cd = new SqlCohortDefinition();
		String sqlQuery = ";";

		cd.setName("SER-<1Y");
		cd.setQuery(sqlQuery);
		cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
		cd.addParameter(new Parameter("endDate", "End Date", Date.class));
		cd.addParameter(new Parameter("facilityList", "Facility List", MOHFacility.class));
		cd.setDescription("Squint/White Eye Reflection under 1 year");

		return cd;
	}*/
}
