package org.openmrs.module.evrreports.reporting.provider;

import org.apache.commons.io.IOUtils;
import org.openmrs.api.APIException;
import org.openmrs.module.evrreports.reporting.builder.EVRMoh510ReportBuilder;
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
public class MOH510ReportProvider extends ReportProvider {

	EVRMoh510ReportBuilder builder = new EVRMoh510ReportBuilder();

	public MOH510ReportProvider() {
		this.name = "MOH 510";
		this.visible = true;
	}

	@Override
	public ReportDefinition getReportDefinition() {

		return builder.build(this.name, "MOH 510 Report");

	}

	@Override
	public CohortDefinition getCohortDefinition() {
		return null;
	}

	@Override
	public ReportDesign getReportDesign() {
		ReportDesign design = new ReportDesign();
		design.setName("MOH 510 Report Design");
		design.setReportDefinition(this.getReportDefinition());
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

}
