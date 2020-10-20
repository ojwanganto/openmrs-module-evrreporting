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

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.EncounterType;
import org.openmrs.Location;
import org.openmrs.PersonAttributeType;
import org.openmrs.api.APIException;
import org.openmrs.module.evrreports.reporting.cohort.definition.EVRMoh510CohortDefinition;
import org.openmrs.module.evrreports.reporting.converter.EncounterDatetimeConverter;
import org.openmrs.module.evrreports.reporting.data.ClientIdentifierDataDefinition;
import org.openmrs.module.evrreports.reporting.data.ClientParentGuardianNameDataDefinition;
import org.openmrs.module.evrreports.reporting.data.EVRDateOfVaccineDataDefinition;
import org.openmrs.module.evrreports.reporting.data.PatientLocationDataDefinition;
import org.openmrs.module.evrreports.reporting.data.RegistrationAddressDataDefinition;
import org.openmrs.module.evrreports.util.MOHReportUtil;
import org.openmrs.module.reporting.common.TimeQualifier;
import org.openmrs.module.reporting.data.DataDefinition;
import org.openmrs.module.reporting.data.converter.BirthdateConverter;
import org.openmrs.module.reporting.data.converter.DataConverter;
import org.openmrs.module.reporting.data.converter.DateConverter;
import org.openmrs.module.reporting.data.converter.ObjectFormatter;
import org.openmrs.module.reporting.data.encounter.definition.EncounterLocationDataDefinition;
import org.openmrs.module.reporting.data.patient.definition.EncountersForPatientDataDefinition;
import org.openmrs.module.reporting.data.person.definition.BirthdateDataDefinition;
import org.openmrs.module.reporting.data.person.definition.ConvertedPersonDataDefinition;
import org.openmrs.module.reporting.data.person.definition.GenderDataDefinition;
import org.openmrs.module.reporting.data.person.definition.PersonAttributeDataDefinition;
import org.openmrs.module.reporting.data.person.definition.PreferredNameDataDefinition;
import org.openmrs.module.reporting.dataset.definition.DataSetDefinition;
import org.openmrs.module.reporting.dataset.definition.PatientDataSetDefinition;
import org.openmrs.module.reporting.evaluation.parameter.Mapped;
import org.openmrs.module.reporting.evaluation.parameter.Parameter;
import org.openmrs.module.reporting.report.ReportDesign;
import org.openmrs.module.reporting.report.ReportDesignResource;
import org.openmrs.module.reporting.report.definition.ReportDefinition;
import org.openmrs.module.reporting.report.renderer.ExcelTemplateRenderer;
import org.openmrs.util.OpenmrsClassLoader;
import org.springframework.stereotype.Component;

import org.openmrs.api.context.Context;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Properties;

/**
 * MOH 510 Report
 */
@Component
public class EVRMoh510ReportBuilder extends EVRAbstractReportBuilder {

	protected static final Log log = LogFactory.getLog(EVRMoh510ReportBuilder.class);
	public static final String DATE_FORMAT = "dd/MM/yyyy";

	/**
	 * @see 
	 */
	@Override
	protected List<Parameter> getParameters() {
		return Arrays.asList(
				new Parameter("startDate", "Start Date", Date.class),
				new Parameter("endDate", "End Date", Date.class),
				new Parameter("facilityList", "Facility List", Location.class)
		);
	}

	/**
	 * @see 
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected List<Mapped<DataSetDefinition>> buildDataSets(ReportDefinition report) {
		return Arrays.asList(
				MOHReportUtil.map(moh510DataSetDefinition(), "startDate=${startDate},endDate=${endDate},facilityList=${facilityList}")
		);
	}

	@Override
	public ReportDesign getReportDesign() {
		ReportDesign design = new ReportDesign();
		design.setName("MOH 510 Report Design");
		design.setReportDefinition(this.build("MOH 510", "MOH 510"));
		design.setRendererType(ExcelTemplateRenderer.class);

		Properties props = new Properties();
		props.put("repeatingSections", "sheet:1,row:7,dataset:immunizationRegister");

		design.setProperties(props);

		ReportDesignResource resource = new ReportDesignResource();
		resource.setName("MOH_510_Report.xls");
		InputStream is = OpenmrsClassLoader.getInstance().getResourceAsStream("templates/moh510.xls");

		if (is == null)
			throw new APIException("Could not find report template.");

		try {
			resource.setContents(IOUtils.toByteArray(is));
		} catch (IOException ex) {
			throw new APIException("Could not create report design for MOH 510 Report.", ex);
		}

		IOUtils.closeQuietly(is);
		design.addResource(resource);

		return design;

	}


	/**
	 * Creates the dataset for section #1: Immunizations
	 *
	 * @return the dataset
	 */
	protected DataSetDefinition moh510DataSetDefinition() {

		String paramMapping = "startDate=${startDate},endDate=${endDate},facilityList=${facilityList}";

		PatientDataSetDefinition dsd = new PatientDataSetDefinition("immunizationRegister");
		dsd.addParameter(new Parameter("startDate", "Start Date", Date.class));
		dsd.addParameter(new Parameter("endDate", "End Date", Date.class));
		dsd.addParameter(new Parameter("facilityList", "Facility List", Location.class));

		PersonAttributeType telephoneNumber = Context.getPersonService().getPersonAttributeTypeByName("Telephone contact");
		EncounterType immunizationEncounterType = Context.getEncounterService().getEncounterType("Vaccination");

		EncountersForPatientDataDefinition firstEncounterDf = new EncountersForPatientDataDefinition();
		firstEncounterDf.setWhich(TimeQualifier.FIRST);
		firstEncounterDf.addType(immunizationEncounterType);


		DataConverter nameFormatter = new ObjectFormatter("{familyName}, {givenName}");
		DataDefinition nameDef = new ConvertedPersonDataDefinition("name", new PreferredNameDataDefinition(), nameFormatter);

		dsd.addColumn("Name", nameDef, "");
		dsd.addColumn("Sex", new GenderDataDefinition(), "");
		dsd.addColumn("Date of Birth", new BirthdateDataDefinition(), "", new BirthdateConverter(DATE_FORMAT));

        dsd.addColumn("KIP ID", new ClientIdentifierDataDefinition("id", "kip_id"), "");
        dsd.addColumn("Serial Number", new ClientIdentifierDataDefinition("Serial Number", "permanent_register_number"), "");
        dsd.addColumn("CWC Number", new ClientIdentifierDataDefinition("CWC Number", "cwc_number"), "");

        dsd.addColumn("Fathers full name", new ClientParentGuardianNameDataDefinition("Fathers full name", "father"), "");
        dsd.addColumn("Mothers full name", new ClientParentGuardianNameDataDefinition("Mothers full name", "mother"), "");
		dsd.addColumn("Mothers phone number", new ClientIdentifierDataDefinition("Mothers phone number", "mother_phone_numer"), "");
		dsd.addColumn("Telephone contact", new PersonAttributeDataDefinition("Telephone Number", telephoneNumber), "");
		dsd.addColumn("Address", new RegistrationAddressDataDefinition(), "");
		dsd.addColumn("Date first seen", firstEncounterDf, "", new EncounterDatetimeConverter(DATE_FORMAT));



        dsd.addColumn("BCG", new EVRDateOfVaccineDataDefinition("BCG", "bcg_vx_date"), "", new DateConverter(DATE_FORMAT));
        dsd.addColumn("Polio birth Dose", new EVRDateOfVaccineDataDefinition("Polio birth Dose", "opv_0_vx_date"), "",  new DateConverter(DATE_FORMAT));
        dsd.addColumn("OPV 1", new EVRDateOfVaccineDataDefinition("OPV 1", "opv_1_vx_date"), "", new DateConverter(DATE_FORMAT));
        dsd.addColumn("OPV 2", new EVRDateOfVaccineDataDefinition("OPV 2", "opv_2_vx_date"), "", new DateConverter(DATE_FORMAT));
        dsd.addColumn("OPV 3", new EVRDateOfVaccineDataDefinition("OPV 3", "opv_3_vx_date"), "", new DateConverter(DATE_FORMAT));
        dsd.addColumn("IPV", new EVRDateOfVaccineDataDefinition("IPV", "ipv_vx_date"), "",new DateConverter(DATE_FORMAT));
        dsd.addColumn("DPT_HepB_Hib 1", new EVRDateOfVaccineDataDefinition("DPT/Hep.B/Hib 1", "penta_1_vx_date"), "", new DateConverter(DATE_FORMAT));
        dsd.addColumn("DPT_HepB_Hib 2", new EVRDateOfVaccineDataDefinition("DPT/Hep.B/Hib 2", "penta_2_vx_date"), "", new DateConverter(DATE_FORMAT));
        dsd.addColumn("DPT_HepB_Hib 3", new EVRDateOfVaccineDataDefinition("DPT/Hep.B/Hib 3", "penta_3_vx_date"), "", new DateConverter(DATE_FORMAT));
        dsd.addColumn("PCV 10(Pneumococcal) 1", new EVRDateOfVaccineDataDefinition("PCV 10(Pneumococcal) 1", "pcv_1_vx_date"), "", new DateConverter(DATE_FORMAT));
        dsd.addColumn("PCV 10(Pneumococcal) 2", new EVRDateOfVaccineDataDefinition("PCV 10(Pneumococcal) 2", "pcv_2_vx_date"), "", new DateConverter(DATE_FORMAT));
        dsd.addColumn("PCV 10(Pneumococcal) 3", new EVRDateOfVaccineDataDefinition("PCV 10(Pneumococcal) 3", "pcv_3_vx_date"), "", new DateConverter(DATE_FORMAT));
        dsd.addColumn("ROTA 1", new EVRDateOfVaccineDataDefinition("ROTA 1", "rota_1_vx_date"), "", new DateConverter(DATE_FORMAT));
        dsd.addColumn("ROTA 2", new EVRDateOfVaccineDataDefinition("ROTA 2", "rota_2_vx_date"), "", new DateConverter(DATE_FORMAT));
        dsd.addColumn("Vitamin A", new EVRDateOfVaccineDataDefinition("Vitamin A", "vit_at_6_vx_date"), "", new DateConverter(DATE_FORMAT));

        dsd.addColumn("Measles 1", new EVRDateOfVaccineDataDefinition("Measles 1", "mr_1_vx_date"), "", new DateConverter(DATE_FORMAT));
        dsd.addColumn("Yellow Fever", new EVRDateOfVaccineDataDefinition("Yellow Fever", "yf_vx_date"), "", new DateConverter(DATE_FORMAT));
        //dsd.addColumn("Fully Immunized Child", null, "");
        dsd.addColumn("Measles 2", new EVRDateOfVaccineDataDefinition("Measles 2", "mr_2_vx_date"), "", new DateConverter(DATE_FORMAT));
        dsd.addColumn("Facility Name", new PatientLocationDataDefinition(), "", null);

        EVRMoh510CohortDefinition cd = new EVRMoh510CohortDefinition();
		cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
		cd.addParameter(new Parameter("endDate", "End Date", Date.class));
		cd.addParameter(new Parameter("facilityList", "Facility List", Location.class));

		dsd.addRowFilter(cd, paramMapping);
		return dsd;
	}

}