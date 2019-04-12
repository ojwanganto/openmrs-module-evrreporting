package org.openmrs.module.evrreports.reporting.converter;

import org.openmrs.module.evrreports.reporting.common.ObsRepresentation;
import org.openmrs.module.reporting.data.converter.DataConverter;

import java.util.Date;

/**
 * Converter to get valueDatetime from an observation
 */
public class ObsRepresentationValueDatetimeConverter implements DataConverter {
	@Override
	public Object convert(Object original) {
		ObsRepresentation o = (ObsRepresentation) original;

		if (o == null)
			return null;

		return o.getValueDatetime();
	}

	@Override
	public Class<?> getInputDataType() {
		return ObsRepresentation.class;
	}

	@Override
	public Class<?> getDataType() {
		return Date.class;
	}
}
