package org.openmrs.module.evrreports.reporting.cohort.definition;

import org.openmrs.module.reporting.common.Localized;
import org.openmrs.module.reporting.definition.configuration.ConfigurationPropertyCachingStrategy;
import org.openmrs.module.reporting.evaluation.caching.Caching;

/**
 * MOH 361B Register cohort definition
 */
@Caching(strategy = ConfigurationPropertyCachingStrategy.class)
@Localized("reporting.MOH361BCohortDefinition")
public class Moh361BCohortDefinition extends AMRSReportsCohortDefinition {

}
