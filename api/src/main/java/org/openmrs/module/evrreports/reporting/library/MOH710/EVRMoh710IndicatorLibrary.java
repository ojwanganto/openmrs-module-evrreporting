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

import org.openmrs.module.reporting.indicator.CohortIndicator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static org.openmrs.module.evrreports.util.MOHReportUtil.map;
import static org.openmrs.module.evrreports.util.MOHReportUtil.cohortIndicator;

@Component
public class EVRMoh710IndicatorLibrary {

	//@Autowired
	private EVRMoh710CohortLibrary moh710CohortLibrary = new EVRMoh710CohortLibrary();

	String indParams = "startDate=${startDate},endDate=${endDate},facilityList=${facilityList}";


	/*Given BCG*/
	public CohortIndicator givenBCGVaccine() {

		return cohortIndicator("Given BCG",map(moh710CohortLibrary.givenBCGVaccineCl(), indParams)
		);
	}

	/*Given OPV*/
	public CohortIndicator givenOPV() {

		return cohortIndicator("Given OPV at Birth",map(moh710CohortLibrary.givenOPVCl(), indParams)
		);
	}

	/*Given OPV1*/
	public CohortIndicator givenOPV1() {

		return cohortIndicator("Given OPV1", map(moh710CohortLibrary.givenOPV1Cl(), indParams)
		);
	}

	/*Given OPV2*/
	public CohortIndicator givenOPV2() {

		return cohortIndicator("Given OPV2",map(moh710CohortLibrary.givenOPV2Cl(), indParams)
		);
	}

	/*Given OPV3*/
	public CohortIndicator givenOPV3() {

		return cohortIndicator("Given OPV3",map(moh710CohortLibrary.givenOPV3Cl(), indParams)
		);
	}

	/*Given IPV*/
	public CohortIndicator givenIpv() {

		return cohortIndicator("Given IPV",map(moh710CohortLibrary.givenIpvCl(), indParams)
		);
	}

	/*Given Dpt-Hep-Hib 1 Vaccine*/
	public CohortIndicator givenDptHepHibVaccine1() {

		return cohortIndicator("Given Dpt-Hep-Hib 1",map(moh710CohortLibrary.givenDptHepHibVaccine1Cl(), indParams)
		);
	}

	/*Given Given Dpt-Hep-Hib 2 vaccine*/
	public CohortIndicator givenDptHepHibVaccine2() {

		return cohortIndicator("Given Dpt-Hep-Hib 2",map(moh710CohortLibrary.givenDptHepHibVaccine2Cl(), indParams)
		);
	}

	/*Given Given Dpt-Hep-Hib 3 vaccine*/
	public CohortIndicator givenDptHepHibVaccine3() {

		return cohortIndicator("Given Dpt-Hep-Hib 3",map(moh710CohortLibrary.givenDptHepHibVaccine3Cl(), indParams)
		);
	}

	/*Given Pneumococcal 1*/
	public CohortIndicator givenPneumococcal1Vaccine() {

		return cohortIndicator("Given Pneumococcal 1",map(moh710CohortLibrary.givenPneumococcal1VaccineCl(), indParams)
		);
	}

	/*Given Pneumococcal 2*/
	public CohortIndicator givenPneumococcal2Vaccine() {

		return cohortIndicator("Given Pneumococcal 2",map(moh710CohortLibrary.givenPneumococcal2VaccineCl(), indParams)
		);
	}

	/*Given Pneumococcal 3*/
	public CohortIndicator givenPneumococcal3Vaccine() {

		return cohortIndicator("Given Pneumococcal 3",map(moh710CohortLibrary.givenPneumococcal3VaccineCl(), indParams)
		);
	}

	/*Given Rota 1*/
	public CohortIndicator givenRota1VirusVaccine() {

		return cohortIndicator("Given Rota 1",map(moh710CohortLibrary.givenRota1VirusVaccineCl(), indParams)
		);
	}

	/*Given Rota 2*/
	public CohortIndicator givenRota2VirusVaccine() {

		return cohortIndicator("Given Rota 2",map(moh710CohortLibrary.givenRota2VirusVaccineCl(), indParams)
		);
	}

	/*Given Vitamin A at 6 Months*/
	public CohortIndicator givenVitAAt6MAge() {

		return cohortIndicator(null,map(moh710CohortLibrary.givenVitAAt6MAgeCl(), indParams)
		);
	}

	/*Given Yellow Fever vaccine*/
	public CohortIndicator givenYellowFeverVaccine() {

		return cohortIndicator(null,map(moh710CohortLibrary.givenYellowFeverVaccineCl(), indParams)
		);
	}

	/*Given Measles-Rubella 1 vaccine*/
	public CohortIndicator givenMeaslesRubella1Vaccine() {

		return cohortIndicator("Given Measles Rubella 1",map(moh710CohortLibrary.givenMeaslesRubella1VaccineCl(), indParams)
		);
	}

	/*Fully immunized child*/
	public CohortIndicator fullyImmunized() {

		return cohortIndicator(null,map(moh710CohortLibrary.fullyImmunizedCl(), indParams)
		);
	}


	/*Given Vitamin A at 1 years (200,000IU)*/
	public CohortIndicator givenVitAAt12Months() {

		return cohortIndicator(null,map(moh710CohortLibrary.givenVitAAt12MonthsCl(), indParams)
		);
	}

	/*Given Vitamin A at 1 1/2 years (200,000IU)*/
	public CohortIndicator givenVitAAt18Months() {

		return cohortIndicator(null,map(moh710CohortLibrary.givenVitAAt18MonthsCl(), indParams)
		);
	}

	/*Given Vitamin A at 2 years to 5 years (200,000IU)*/
	public CohortIndicator givenVitAAt2To5Years() {

		return cohortIndicator(null,map(moh710CohortLibrary.givenVitAAt2To5YearsCl(), indParams)
		);
	}

	/*Vitamin A Supplemental Lactating Mothers(200,000 IU)*/
	public CohortIndicator givenVitASupplementalLac() {

		return cohortIndicator(null,map(moh710CohortLibrary.givenVitASupplementalLacCl(), indParams)
		);
	}

	/*Measles - Rubella 2(at 1 1/2 - 2 years)*/
	public CohortIndicator givenMeaslesRubella2VaccineAge18To24Months() {

		return cohortIndicator(null,map(moh710CohortLibrary.givenMeaslesRubella2VaccineAge18To24MonthsCl(), indParams)
		);
	}

	/*Measles-Rubella 2 Above 2 years*/
	public CohortIndicator givenMeaslesRubellaVaccine2AndAgedOver2Years() {

		return cohortIndicator(null,map(moh710CohortLibrary.givenMeaslesRubellaVaccine2AndAgedOver2YearsCl(), indParams)
		);
	}

	/*Tetanus Toxoid for pregnant women first dose*/
	public CohortIndicator givenTTXFirstDose() {

		return cohortIndicator(null,map(moh710CohortLibrary.givenTTXFirstDoseCl(), indParams)
		);
	}

	/*Tetanus Toxoid for pregnant women second dose*/
	public CohortIndicator givenTTXSecondDose() {

		return cohortIndicator(null,map(moh710CohortLibrary.givenTTXSecondDoseCl(), indParams)
		);
	}

	/*Tetanus Toxoid plus(Booster) for pregnant women*/
	public CohortIndicator givenTTXPlus() {

		return cohortIndicator(null,map(moh710CohortLibrary.givenTTXPlusCl(), indParams)
		);
	}
//Adverse events following immunization

	/*2 -5 years (200,000 IU)*/
	public CohortIndicator givenVitASupplemental() {

		return cohortIndicator(null,map(moh710CohortLibrary.givenVitASupplementalCl(), indParams)
		);
	}

	/*Issued with LLITN in this Visit (under 1 year)*/
	public CohortIndicator givenLLITN() {

		return cohortIndicator(null,map(moh710CohortLibrary.givenLLITNCl(), indParams)
		);
	}

	/*Squint/White Eye Reflection under 1 year*/
	public CohortIndicator squintWhiteEyeReflection() {

		return cohortIndicator(null,map(moh710CohortLibrary.squintWhiteEyeReflectionCl(), indParams)
		);
	}
}


