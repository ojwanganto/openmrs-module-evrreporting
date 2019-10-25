package org.openmrs.module.evrreports.task;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Transaction;
import org.hibernate.jdbc.Work;
import org.openmrs.api.context.Context;
import org.openmrs.api.db.hibernate.DbSessionFactory;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Updates EVR reporting tables
 */
public class RefreshETLTablesTask extends EVRReportsTask {

	private static final Log log = LogFactory.getLog(RefreshETLTablesTask.class);

	@Override
	public void doExecute() {

		Context.openSession();

		DbSessionFactory sf = Context.getRegisteredComponents(DbSessionFactory.class).get(0);

		Transaction tx = null;
		try {

			if (!Context.isAuthenticated()) {
				authenticate();
			}

			tx = sf.getHibernateSessionFactory().getCurrentSession().beginTransaction();
			final Transaction finalTx = tx;
			sf.getCurrentSession().doWork(new Work() {

				@Override
				public void execute(Connection connection) throws SQLException {

					CallableStatement cs = connection.prepareCall("{call create_etl_tables}");
					CallableStatement updates = connection.prepareCall("{CALL sp_scheduled_updates}");
					CallableStatement createMoh710Table = connection.prepareCall("{CALL create_moh_710_etl_table}");
					CallableStatement moh710Updates = connection.prepareCall("{CALL sp_scheduled_moh_710_updates}");

					cs.execute();
					updates.execute();
					createMoh710Table.execute();
					moh710Updates.execute();
				}
			});
			finalTx.commit();
		}
		catch (Exception e) {
			throw new IllegalArgumentException("Unable to execute query", e);
		} finally {
			Context.closeSession();
		}
	}
}
