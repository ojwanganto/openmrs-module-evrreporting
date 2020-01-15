package org.openmrs.module.evrreports.reporting.converter;

import org.openmrs.Encounter;
import org.openmrs.module.reporting.data.converter.DataConverter;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Converter to get the encounter date from an encounter
 */
public class EncounterDatetimeConverter implements DataConverter {
	private String dateFormat;
	public EncounterDatetimeConverter(){

	}

	public EncounterDatetimeConverter(String dateFormat) {
		this.dateFormat = dateFormat;
	}
	@Override
	public Object convert(Object original) {
		Encounter e = (Encounter) original;

		if (e == null)
			return null;

		if (dateFormat != null) {
			SimpleDateFormat df = new SimpleDateFormat(dateFormat);
			return df.format(e.getEncounterDatetime());
		}
		return e.getEncounterDatetime();
	}

	@Override
	public Class<?> getInputDataType() {
		return Encounter.class;
	}

	@Override
	public Class<?> getDataType() {
		return String.class;
	}

	public String getDateFormat() {
		return dateFormat;
	}

	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
	}
}
