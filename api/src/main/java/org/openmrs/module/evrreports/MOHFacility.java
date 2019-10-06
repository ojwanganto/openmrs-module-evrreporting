package org.openmrs.module.evrreports;

import org.openmrs.BaseOpenmrsMetadata;
import org.openmrs.Location;

import java.util.HashSet;
import java.util.Set;

/**
 * Assignment of an MOH facility code to one or more locations
 */
public class MOHFacility extends BaseOpenmrsMetadata {

	private Integer facilityId;
	private String code;
	private Set<Location> locations;
	private String county;
	private String subCounty;
	private String ward;

	@Override
	public Integer getId() {
		return facilityId;
	}

	@Override
	public void setId(Integer id) {
		facilityId = id;
	}

	public Integer getFacilityId() {
		return facilityId;
	}

	public void setFacilityId(Integer facilityId) {
		this.facilityId = facilityId;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Set<Location> getLocations() {
		if (locations == null)
			locations = new HashSet<Location>();
		return locations;
	}

	public void setLocations(Set<Location> locations) {
		this.locations = locations;
	}

	public void addLocation(Location location) {
		this.getLocations().add(location);
	}

	public String getCounty() {
		return county;
	}

	public void setCounty(String county) {
		this.county = county;
	}

	public String getSubCounty() {
		return subCounty;
	}

	public void setSubCounty(String subCounty) {
		this.subCounty = subCounty;
	}

	public String getWard() {
		return ward;
	}

	public void setWard(String ward) {
		this.ward = ward;
	}

	@Override
	public String toString() {
		return String.format("%s - %s in %s County", this.getCode(), this.getName(), this.getCounty());
	}
}
