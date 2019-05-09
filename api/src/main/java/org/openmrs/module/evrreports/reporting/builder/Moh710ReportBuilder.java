/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.evrreports.reporting.builder;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.module.evrreports.reporting.ColumnParameters;
import org.openmrs.module.evrreports.reporting.EmrReportingUtils;
import org.openmrs.module.evrreports.reporting.library.MOH710.Moh710IndicatorLibrary;
import org.openmrs.module.evrreports.reporting.shared.common.CommonDimensionLibrary;
import org.openmrs.module.evrreports.util.MOHReportUtil;
import org.openmrs.module.reporting.dataset.definition.CohortIndicatorDataSetDefinition;
import org.openmrs.module.reporting.dataset.definition.DataSetDefinition;
import org.openmrs.module.reporting.evaluation.parameter.Mapped;
import org.openmrs.module.reporting.evaluation.parameter.Parameter;
import org.openmrs.module.reporting.report.definition.ReportDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * MOH 710 Report
 */
@Component
public class Moh710ReportBuilder extends AbstractReportBuilder {

	protected static final Log log = LogFactory.getLog(Moh710ReportBuilder.class);

	@Autowired
	private Moh710IndicatorLibrary moh710Indicators;

	@Autowired
	private CommonDimensionLibrary commonDimensions;

	/**
	 * @see 
	 */
	@Override
	protected List<Parameter> getParameters() {
		return Arrays.asList(
				new Parameter("startDate", "Start Date", Date.class),
				new Parameter("endDate", "End Date", Date.class)
				//new Parameter("dateBasedReporting", "", String.class)
		);
	}

	/**
	 * @see 
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected List<Mapped<DataSetDefinition>> buildDataSets(ReportDefinition report) {
		return Arrays.asList(
				MOHReportUtil.map(immunizationsDataSet(), "startDate=${startDate},endDate=${endDate}")
		);
	}


	/**
	 * Creates the dataset for section #1: Immunizations
	 *
	 * @return the dataset
	 */
	protected DataSetDefinition immunizationsDataSet() {
		CohortIndicatorDataSetDefinition dsd = new CohortIndicatorDataSetDefinition();
		dsd.setName("Immunizations");
		dsd.setDescription("MOH 710 Immunizations");
		dsd.addParameter(new Parameter("startDate", "Start Date", Date.class));
		dsd.addParameter(new Parameter("endDate", "End Date", Date.class));



		dsd.addDimension("age", MOHReportUtil.map(commonDimensions.moh710AgeGroups(), "onDate=${endDate}"));
		ColumnParameters infantLess_1 = new ColumnParameters(null, "<1", "age=<1");
		ColumnParameters infantAtleast_1 = new ColumnParameters(null, ">=1", "age=>=1");

		ColumnParameters childBtw18mAnd24m = new ColumnParameters(null, "18-24", "age=18-24");
		ColumnParameters childOver2y = new ColumnParameters(null, ">2", "age=>2");

		List<ColumnParameters> moh710Disaggregations = Arrays.asList(infantLess_1, infantAtleast_1);
		List<ColumnParameters> moh710DisaggregationsMR = Arrays.asList(childBtw18mAnd24m, childOver2y);

		String indParams = "startDate=${startDate},endDate=${endDate}";

		EmrReportingUtils.addRow(dsd, "BCG", "Given BCG", MOHReportUtil.map(moh710Indicators.givenBCGVaccine(), indParams), moh710Disaggregations, Arrays.asList("01", "02"));
		EmrReportingUtils.addRow(dsd,"OPV-0", "Given OPV at birth", MOHReportUtil.map(moh710Indicators.givenOPV(), indParams),moh710Disaggregations, Arrays.asList("01", "02"));
		EmrReportingUtils.addRow(dsd,"OPV-1", "Given OPV 1", MOHReportUtil.map(moh710Indicators.givenOPV1(), indParams), moh710Disaggregations, Arrays.asList("01", "02"));
		EmrReportingUtils.addRow(dsd,"OPV-2", "Given OPV 2", MOHReportUtil.map(moh710Indicators.givenOPV2(), indParams), moh710Disaggregations, Arrays.asList("01", "02"));
		EmrReportingUtils.addRow(dsd,"OPV-3", "Given OPV 3", MOHReportUtil.map(moh710Indicators.givenOPV3(), indParams), moh710Disaggregations, Arrays.asList("01", "02"));
		EmrReportingUtils.addRow(dsd,"IPV", "Given IPV", MOHReportUtil.map(moh710Indicators.givenIpv(), indParams),moh710Disaggregations, Arrays.asList("01", "02"));
		EmrReportingUtils.addRow(dsd,"DHH-1", "Given Dpt-Hep-Hib 1", MOHReportUtil.map(moh710Indicators.givenDptHepHibVaccine1(), indParams), moh710Disaggregations, Arrays.asList("01", "02"));
		EmrReportingUtils.addRow(dsd,"DHH-2", "Given Dpt-Hep-Hib 2", MOHReportUtil.map(moh710Indicators.givenDptHepHibVaccine2(), indParams), moh710Disaggregations, Arrays.asList("01", "02"));
		EmrReportingUtils.addRow(dsd,"DHH-3", "Given Dpt-Hep-Hib 3", MOHReportUtil.map(moh710Indicators.givenDptHepHibVaccine3(), indParams), moh710Disaggregations, Arrays.asList("01", "02"));
		EmrReportingUtils.addRow(dsd,"PCV-1", "Given Pneumococcal 1", MOHReportUtil.map(moh710Indicators.givenPneumococcal1Vaccine(), indParams), moh710Disaggregations, Arrays.asList("01", "02"));
		EmrReportingUtils.addRow(dsd,"PCV-2", "Given Pneumococcal 2", MOHReportUtil.map(moh710Indicators.givenPneumococcal2Vaccine(), indParams),moh710Disaggregations, Arrays.asList("01", "02"));
		EmrReportingUtils.addRow(dsd,"PCV-3", "Given Pneumococcal 3", MOHReportUtil.map(moh710Indicators.givenPneumococcal3Vaccine(), indParams), moh710Disaggregations, Arrays.asList("01", "02"));
		EmrReportingUtils.addRow(dsd,"ROTA-1", "Given Rota 1", MOHReportUtil.map(moh710Indicators.givenRota1VirusVaccine(), indParams), moh710Disaggregations, Arrays.asList("01", "02"));
		EmrReportingUtils.addRow(dsd,"ROTA-2", "Given Rota 2", MOHReportUtil.map(moh710Indicators.givenRota2VirusVaccine(), indParams), moh710Disaggregations, Arrays.asList("01", "02"));
		EmrReportingUtils.addRow(dsd,"VA6M", "Given Vitamin A at 6 Months", MOHReportUtil.map(moh710Indicators.givenVitAAt6MAge(), indParams), moh710Disaggregations, Arrays.asList("01", "02"));
		EmrReportingUtils.addRow(dsd,"YF", "Given Yellow Fever", MOHReportUtil.map(moh710Indicators.givenYellowFeverVaccine(), indParams), moh710Disaggregations, Arrays.asList("01", "02"));
		EmrReportingUtils.addRow(dsd,"MR-1", "Given Measles-Rubella 1 vaccine", MOHReportUtil.map(moh710Indicators.givenMeaslesRubella1Vaccine(), indParams), moh710Disaggregations, Arrays.asList("01", "02"));
		EmrReportingUtils.addRow(dsd,"FIC", "Fully immunized children", MOHReportUtil.map(moh710Indicators.fullyImmunized(), indParams), moh710Disaggregations, Arrays.asList("01", "02"));
		EmrReportingUtils.addRow(dsd,"VA-1Y", "Vitamin A at 1 years (200,000IU)", MOHReportUtil.map(moh710Indicators.givenVitAAt12Months(), indParams), moh710Disaggregations, Arrays.asList("01", "02"));
		EmrReportingUtils.addRow(dsd,"VA-1.5Y", "Vitamin A at 1 1/2 years(200,000 IU)", MOHReportUtil.map(moh710Indicators.givenVitAAt18Months(), indParams), moh710Disaggregations, Arrays.asList("01", "02"));
		EmrReportingUtils.addRow(dsd,"VA-2Y-5Y", "Vitamin A at 2 years to 5 years (200,000IU)", MOHReportUtil.map(moh710Indicators.givenVitAAt2To5Years(), indParams), moh710Disaggregations, Arrays.asList("01", "02"));
		//EmrReportingUtils.addRow(dsd,"VAS", "Vitamin A Supplemental Lactating Mothers(200,000 IU)", MOHReportUtil.map(moh710Indicators.givenVitASupplemental(), indParams),moh710Disaggregations, Arrays.asList("01", "02"));
		EmrReportingUtils.addRow(dsd,"MR-2", "Measles - Rubella 2 (in Months)", MOHReportUtil.map(moh710Indicators.givenMeaslesRubella2VaccineAge18To24Months(), indParams), moh710DisaggregationsMR, Arrays.asList("01", "02"));
		EmrReportingUtils.addRow(dsd,"MR-2", "Measles - Rubella 2 (Years)", MOHReportUtil.map(moh710Indicators.givenMeaslesRubellaVaccine2AndAgedOver2Years(), indParams), moh710DisaggregationsMR, Arrays.asList("01", "02"));
		//EmrReportingUtils.addRow(dsd,"SER-<1Y", "Squint/White Eye Reflection under 1 year", MOHReportUtil.map(moh710Indicators.squintWhiteEyeReflection(), indParams), moh710Disaggregations, Arrays.asList("01", "02"));
		//dsd.addColumn("TTX_FPW-1st", "Tetanus Toxoid for pregnant women first dose", MOHReportUtil.map(moh710Indicators.givenTTXFirstDose(), indParams),"");
		//dsd.addColumn("TTX_FPW-2nd", "Tetanus Toxoid for pregnant women second dose", MOHReportUtil.map(moh710Indicators.givenTTXSecondDose(), indParams), "");
		//EmrReportingUtils.addRow(dsd,"TTX_FPW-TTPLUS", "Tetanus Toxoid plus(Booster) for pregnant women", MOHReportUtil.map(moh710Indicators.givenTTXPlus(), indParams), moh710Disaggregations, Arrays.asList("01", "02"));

		//Adverse events following immunization
		//MissingQueries dsd.addColumn("VA-2-5Y", "2 -5 years (200,000 IU)", MOHReportUtil.map(moh710Indicators.givenVitASupplemental(), indParams), "");
		//MissingQueries dsd.addColumn("VA-LAC", "Vitamin A Supplemental Lactating Mothers(200,000 IU)", MOHReportUtil.map(moh710Indicators.givenVitASupplementalLac(), indParams), "");
		//MissingQueries dsd.addColumn("LLITN-LT1Y", "Issued with LLITN in this Visit (under 1 year)", MOHReportUtil.map(moh710Indicators.givenLLITN(), indParams), "");

		return dsd;
	}

}