package org.openmrs.module.evrreports.reporting.converter;

import org.apache.commons.lang.StringUtils;
import org.openmrs.module.evrreports.reporting.common.ObsRepresentation;
import org.openmrs.module.evrreports.util.MOHReportUtil;
import org.openmrs.module.reporting.data.converter.DataConverter;

/**
 * Converter for formatting WHO Stage and Date column data
 */

public class WHOStageAndDateConverter implements DataConverter {

	@Override
	public Object convert(Object original) {
		ObsRepresentation o = (ObsRepresentation) original;

		if (o == null)
			return "";

		String whoStage = (String) new WHOStageConverter().convert(o);

		if (StringUtils.isBlank(whoStage))
			return "";

		return String.format(
				MOHReportUtil.joinAsSingleCell(whoStage, "%s"),
				MOHReportUtil.formatdates(o.getObsDatetime()));
	}

	@Override
	public Class<?> getInputDataType() {
		return ObsRepresentation.class;
	}

	@Override
	public Class<?> getDataType() {
		return String.class;
	}
}
