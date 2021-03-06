package com.drphamesl.beans;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.enterprise.concurrent.ManagedScheduledExecutorService;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.servlet.ServletContext;

import com.appslandia.common.base.InitializeException;
import com.appslandia.common.base.RateLimit;
import com.appslandia.common.crypto.SecureProps;
import com.appslandia.common.logging.AppLogger;
import com.appslandia.common.mail.MailerMessage;
import com.appslandia.common.mail.SmtpMailer;
import com.appslandia.common.utils.AssertUtils;
import com.appslandia.common.utils.CollectionUtils;
import com.appslandia.common.utils.DateUtils;
import com.appslandia.common.utils.ExceptionUtils;
import com.appslandia.plum.base.AppConfig;
import com.appslandia.plum.utils.ServletUtils;
import com.drphamesl.entities.MailMsg;
import com.drphamesl.services.MailMsgService;
import com.drphamesl.utils.MailMsgs;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
@ApplicationScoped
public class MailService {

	public static final String CONFIG_MAILER_INTERVAL1 = MailService.class.getName() + ".mailer_interval1";
	public static final String CONFIG_MAILER_INTERVAL2 = MailService.class.getName() + ".mailer_interval2";
	public static final String CONFIG_MAILER_RATELIMIT = MailService.class.getName() + ".mailer_rate_limit";

	@Inject
	protected ServletContext sc;

	@Inject
	protected AppLogger logger;

	@Inject
	protected AppConfig appConfig;

	@Inject
	protected MailMsgService mailMsgService;

	@Resource
	protected ManagedScheduledExecutorService executorService;

	protected long mailerInterval1;
	protected long mailerInterval2;
	protected RateLimit mailerRateLimit;

	final Map<Integer, SmtpMailer> mailers = new HashMap<>();

	// ERROR Log
	protected AtomicReference<String> lastErrorMsg = new AtomicReference<>();

	@PostConstruct
	protected void initialize() {
		logger.info("Initializing MailService...");

		String cfgMailerInteval1 = this.appConfig.getString(CONFIG_MAILER_INTERVAL1, "1.5m");
		String cfgMailerInteval2 = this.appConfig.getString(CONFIG_MAILER_INTERVAL2, "4h");
		String cfgMailerRateLimit = this.appConfig.getString(CONFIG_MAILER_RATELIMIT, "95/25h");

		logger.info("{}={}", CONFIG_MAILER_INTERVAL1, cfgMailerInteval1);
		logger.info("{}={}", CONFIG_MAILER_INTERVAL2, cfgMailerInteval2);
		logger.info("{}={}", CONFIG_MAILER_RATELIMIT, cfgMailerRateLimit);

		mailerInterval1 = DateUtils.translateToMs(cfgMailerInteval1);
		mailerInterval2 = DateUtils.translateToMs(cfgMailerInteval2);
		mailerRateLimit = RateLimit.parse(cfgMailerRateLimit);

		// Mailers
		SmtpMailer mailer1 = initSmtpMailer("/WEB-INF/mailer-1.properties");
		SmtpMailer mailer2 = initSmtpMailer("/WEB-INF/mailer-2.properties");

		CollectionUtils.toMap(this.mailers, MailMsgs.MAILER_1, mailer1, MailMsgs.MAILER_2, mailer2);

		// Mailer 1
		executorService.scheduleAtFixedRate(new Runnable() {

			@Override
			public void run() {
				try {

					sendEmails(MailMsgs.MAILER_1);

				} catch (Exception ex) {
					logException(ex);
				}
			}
		}, 120_000, mailerInterval1, TimeUnit.MILLISECONDS);

		// Mailer 2
		executorService.scheduleAtFixedRate(new Runnable() {

			@Override
			public void run() {
				try {

					sendEmails(MailMsgs.MAILER_2);

				} catch (Exception ex) {
					logException(ex);
				}
			}
		}, 120_000, mailerInterval2, TimeUnit.MILLISECONDS);

		logger.info("Finished initializing MailService.");
	}

	void logException(Exception ex) {
		String lastError = lastErrorMsg.get();
		String curError = ExceptionUtils.buildMessage(ex);

		if (curError.equals(lastError)) {
			logger.error(curError);

		} else {
			lastErrorMsg.set(curError);
			logger.error(ex);
		}
	}

	void sendEmails(int mailerId) throws Exception {
		final long lastMs = System.currentTimeMillis() - mailerRateLimit.getWindowMs();
		int recentSends = mailMsgService.countSent(mailerId, lastMs);

		int maxSends = (int) mailerRateLimit.getAccesses() - recentSends;
		if (maxSends <= 0) {
			return;
		}
		SmtpMailer mailer = mailers.get(mailerId);
		AssertUtils.assertStateNotNull(mailer);

		List<MailMsg> unsentMsgs = mailMsgService.queryUnsent(mailerId, maxSends);
		List<MailerMessage> mailerMsgs = new ArrayList<MailerMessage>(unsentMsgs.size());

		for (MailMsg msg : unsentMsgs) {
			mailerMsgs.add(msg.fillMsg(mailer.newMessage()));

			msg.setTimeSent(System.currentTimeMillis());
		}

		mailer.send(mailerMsgs);
		mailMsgService.markSent(unsentMsgs);
	}

	SmtpMailer initSmtpMailer(String configFile) throws InitializeException {
		try {
			SecureProps props = new SecureProps("${smtpPassword,env.DPESL_SMTP_PWD}");
			ServletUtils.loadProps(sc, configFile, props);
			return new SmtpMailer().setProps(props);

		} catch (Exception ex) {
			logger.error(ex);

			throw new InitializeException(ex);
		}
	}
}
