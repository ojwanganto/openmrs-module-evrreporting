package org.openmrs.module.evrreports.reporting.data;

import org.openmrs.module.evrreports.MOHFacility;
import org.openmrs.module.reporting.data.BaseDataDefinition;
import org.openmrs.module.reporting.data.person.definition.PersonDataDefinition;
import org.openmrs.module.reporting.definition.configuration.ConfigurationPropertyCachingStrategy;
import org.openmrs.module.reporting.evaluation.caching.Caching;
import org.openmrs.module.reporting.evaluation.parameter.Parameter;

/**
 * Enrollment Date column
 */
@Caching(strategy=ConfigurationPropertyCachingStrategy.class)
public class SerialNumberDataDefinition extends BaseDataDefinition implements PersonDataDefinition {

	public SerialNumberDataDefinition() {
		Parameter facility = new Parameter();
		facility.setName("facility");
		facility.setType(MOHFacility.class);
		this.addParameter(facility);
	}

	@Override
	public Class<?> getDataType() {
		return String.class;
	}
}