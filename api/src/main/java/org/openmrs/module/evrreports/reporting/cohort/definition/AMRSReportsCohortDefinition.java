package org.openmrs.module.evrreports.reporting.cohort.definition;

import org.openmrs.module.evrreports.MOHFacility;
import org.openmrs.module.reporting.cohort.definition.BaseCohortDefinition;
import org.openmrs.module.reporting.common.Localized;
import org.openmrs.module.reporting.definition.configuration.ConfigurationProperty;
import org.openmrs.module.reporting.definition.configuration.ConfigurationPropertyCachingStrategy;
import org.openmrs.module.reporting.evaluation.caching.Caching;

/**
 * TODO:please provide a brief description for the class.
 */
@Caching(strategy = ConfigurationPropertyCachingStrategy.class)
@Localized("reporting.AMRSReportsCohortDefinition")
public class AMRSReportsCohortDefinition extends BaseCohortDefinition {

    @ConfigurationProperty
    private MOHFacility facility;

    public AMRSReportsCohortDefinition() {
        super();
    }

    public AMRSReportsCohortDefinition(MOHFacility facility) {
        super();
        this.facility = facility;
    }

    public MOHFacility getFacility() {
        return facility;
    }

    public void setFacility(MOHFacility facility) {
        this.facility = facility;
    }
}