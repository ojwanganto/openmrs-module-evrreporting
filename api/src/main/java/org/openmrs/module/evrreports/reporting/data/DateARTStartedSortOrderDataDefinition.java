package org.openmrs.module.evrreports.reporting.data;

import org.openmrs.module.reporting.data.BaseDataDefinition;
import org.openmrs.module.reporting.data.person.definition.PersonDataDefinition;
import org.openmrs.module.reporting.definition.configuration.ConfigurationPropertyCachingStrategy;
import org.openmrs.module.reporting.evaluation.caching.Caching;

/**
 * Gets the year and month values from ART Start Date
 */
@Caching(strategy=ConfigurationPropertyCachingStrategy.class)
public class DateARTStartedSortOrderDataDefinition extends BaseDataDefinition implements PersonDataDefinition {

	@Override
	public Class<?> getDataType() {
		return String.class;
	}

}