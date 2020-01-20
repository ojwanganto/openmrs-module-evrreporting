package org.openmrs.module.evrreports.reporting.data.evaluator;

import org.apache.commons.lang.StringUtils;
import org.openmrs.Person;
import org.openmrs.PersonAddress;
import org.openmrs.annotation.Handler;
import org.openmrs.api.PersonService;
import org.openmrs.api.context.Context;
import org.openmrs.module.evrreports.reporting.data.RegistrationAddressDataDefinition;
import org.openmrs.module.reporting.data.person.EvaluatedPersonData;
import org.openmrs.module.reporting.data.person.definition.PersonDataDefinition;
import org.openmrs.module.reporting.data.person.evaluator.PersonDataEvaluator;
import org.openmrs.module.reporting.evaluation.EvaluationContext;
import org.openmrs.module.reporting.evaluation.EvaluationException;

import java.util.ArrayList;
import java.util.List;

/**
 * Handler for enrollment date column
 */
@Handler(supports=RegistrationAddressDataDefinition.class, order=50)
public class RegistrationAddressDataEvaluator implements PersonDataEvaluator {

	@Override
	public EvaluatedPersonData evaluate(PersonDataDefinition definition, EvaluationContext context) throws EvaluationException {
		EvaluatedPersonData ret = new EvaluatedPersonData(definition, context);
		PersonService ps = Context.getPersonService();

		for (Integer personId: context.getBaseCohort().getMemberIds()) {



			List<String> addresses = new ArrayList<String>();
			Person person = ps.getPerson(personId);
			PersonAddress personAddress = person.getPersonAddress();
			String personAddressString = "";


			// get village - address 3
			if (personAddress !=null && personAddress.getAddress3() != null) {
				addresses.add(person.getPersonAddress().getAddress3());
			}

			// get landmark - address 2
			if (personAddress !=null && personAddress.getAddress2() != null) {
				addresses.add(person.getPersonAddress().getAddress2());
			}

			if (addresses.size() > 0) {
				personAddressString = StringUtils.join(addresses, "/");

			}
			ret.addData(personId, personAddressString);
		}

		return ret;
	}
}
