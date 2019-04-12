package org.openmrs.module.evrreports.db;

import org.openmrs.User;
import org.openmrs.module.evrreports.MOHFacility;
import org.openmrs.module.evrreports.UserFacility;

import java.util.List;

/**
 * DAO for manipulating UserFacility objects
 */
public interface UserFacilityDAO {

	public UserFacility saveUserFacility(UserFacility userFacility);

	public UserFacility getUserFacility(Integer userFacilityId);

	public List<UserFacility> getAllUserFacilities();

	public void purgeUserFacility(UserFacility userFacility);

	public List<MOHFacility> getAllowedFacilitiesForUser(User user);

	public UserFacility getUserFacilityFor(User user, MOHFacility facility);
}
