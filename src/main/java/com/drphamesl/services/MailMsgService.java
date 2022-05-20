package com.drphamesl.services;

import java.util.List;

import com.appslandia.common.jpa.EntityManagerFactoryImpl;
import com.appslandia.common.jpa.EntityManagerImpl;
import com.appslandia.common.logging.AppLogger;
import com.drphamesl.entities.MailMsg;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.transaction.UserTransaction;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
@ApplicationScoped
public class MailMsgService {

	@Inject
	protected AppLogger logger;

	@Inject
	protected EntityManagerImpl em;

	@Inject
	protected UserTransaction tx;

	@Inject
	protected EntityManagerFactoryImpl emf;

	@Transactional
	public void add(MailMsg obj) throws Exception {
		em.insert(obj);
	}

	public int countSent(int mailerId, long fromTime) throws Exception {
		EntityManagerImpl em = null;
		try {
			em = emf.createEntityManager();

			return em.createNamedQuery("MailMsg.countSent").setParameter("mailerId", mailerId).setParameter("fromTime", fromTime).getSingleOrNull();

		} finally {
			if (em != null) {
				em.close();
			}
		}
	}

	public List<MailMsg> queryUnsent(int mailerId, int maxResults) throws Exception {
		EntityManagerImpl em = null;
		try {
			em = emf.createEntityManager();

			return em.createNamedQuery("MailMsg.queryUnsent", MailMsg.class).setParameter("mailerId", mailerId).setStartPos(0).setMaxResults(maxResults)
					.asReadonly().getResultList();

		} finally {
			if (em != null) {
				em.close();
			}
		}
	}

	public void markSent(List<MailMsg> msgs) throws Exception {
		EntityManagerImpl em = null;
		try {
			em = emf.createEntityManager();
			tx.begin();

			for (MailMsg msg : msgs) {
				em.createNamedQuery("MailMsg.updateTimeSent").setParameter("mailMsgId", msg.getMailMsgId()).setParameter("timeSent", msg.getTimeSent())
						.executeUpdate();
			}

			tx.commit();

		} catch (Exception ex) {
			logger.error(() -> tx.rollback());
			throw ex;

		} finally {
			if (em != null) {
				em.close();
			}
		}
	}
}
