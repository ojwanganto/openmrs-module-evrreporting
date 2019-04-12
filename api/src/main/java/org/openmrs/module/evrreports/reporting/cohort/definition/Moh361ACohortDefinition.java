package org.openmrs.module.evrreports.reporting.cohort.definition;

import org.openmrs.module.reporting.common.Localized;
import org.openmrs.module.reporting.definition.configuration.ConfigurationPropertyCachingStrategy;
import org.openmrs.module.reporting.evaluation.caching.Caching;

/**
 * MOH 361A Register cohort definition
 */
@Caching(strategy = ConfigurationPropertyCachingStrategy.class)
@Localized("reporting.MOH361ACohortDefinition")
public class Moh361ACohortDefinition extends AMRSReportsCohortDefinition {

}
