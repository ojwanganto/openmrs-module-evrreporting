package org.openmrs.module.evrreports.reporting.provider;

import org.apache.commons.io.IOUtils;
import org.openmrs.api.APIException;
import org.openmrs.module.evrreports.reporting.builder.EVRMoh710ReportBuilder;
import org.openmrs.module.reporting.cohort.definition.CohortDefinition;
import org.openmrs.module.reporting.report.ReportDesign;
import org.openmrs.module.reporting.report.ReportDesignResource;
import org.openmrs.module.reporting.report.definition.ReportDefinition;
import org.openmrs.module.reporting.report.renderer.ExcelTemplateRenderer;
import org.openmrs.util.OpenmrsClassLoader;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Provides mechanisms for rendering the MOH 361A Pre-ART Register
 */
public class MOH710ReportProvider extends ReportProvider {

	EVRMoh710ReportBuilder builder = new EVRMoh710ReportBuilder();

	public MOH710ReportProvider() {
		this.name = "MOH 710";
		this.visible = true;
	}

	@Override
	public ReportDefinition getReportDefinition() {

		return builder.build(this.name, "MOH 710 Report");

	}

	@Override
	public CohortDefinition getCohortDefinition() {
		return null;
	}

	@Override
	public ReportDesign getReportDesign() {
		ReportDesign design = new ReportDesign();
		design.setName("MOH 710 Report Design");
		design.setReportDefinition(this.getReportDefinition());
		design.setRendererType(ExcelTemplateRenderer.class);

		Properties props = new Properties();
		//props.put("repeatingSections", "sheet:1,row:4,dataset:allPatients");

		design.setProperties(props);

		ReportDesignResource resource = new ReportDesignResource();
		resource.setName("MOH_710_Report.xls");
		InputStream is = OpenmrsClassLoader.getInstance().getResourceAsStream("templates/moh710.xls");

		if (is == null)
			throw new APIException("Could not find report template.");

		try {
			resource.setContents(IOUtils.toByteArray(is));
		} catch (IOException ex) {
			throw new APIException("Could not create report design for MOH 710 Report.", ex);
		}

		IOUtils.closeQuietly(is);
		design.addResource(resource);

		return design;

	}
}
