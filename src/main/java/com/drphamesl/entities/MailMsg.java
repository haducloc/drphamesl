package com.drphamesl.entities;

import java.io.Serializable;

import javax.mail.internet.AddressException;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQuery;
import javax.validation.constraints.NotNull;

import com.appslandia.common.jpa.EntityBase;
import com.appslandia.common.mail.MailerMessage;
import com.appslandia.common.utils.BitBool;
import com.appslandia.common.validators.BoolType;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
@Entity
@NamedQuery(name = "MailMsg.countSent", query = "SELECT COUNT(e) FROM MailMsg e WHERE e.mailerId=:mailerId AND e.timeSent >= :fromTime")
@NamedQuery(name = "MailMsg.queryUnsent", query = "SELECT e FROM MailMsg e WHERE e.mailerId=:mailerId AND e.timeSent IS NULL ORDER BY e.priority, e.timeCreated")
@NamedNativeQuery(name = "MailMsg.updateTimeSent", query = "UPDATE MailMsg e SET e.timeSent=:timeSent WHERE e.mailMsgId=:mailMsgId")
public class MailMsg extends EntityBase {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long mailMsgId;

	@Column(length = 255)
	private String fromEmail;

	@Column(length = 255)
	private String replyToEmail;

	@NotNull
	@Column(length = 255)
	private String toEmail;

	@NotNull
	@Column(length = 255)
	private String subject;

	@NotNull
	@Column(length = 8000)
	private String content;

	@BoolType
	private int isHtml;

	private int serviceId;

	private int priority;

	private int mailerId;

	@NotNull
	@Column(length = 45)
	private String clientId;

	private long timeCreated;

	private Long timeSent;

	@Override
	public Serializable getPk() {
		return this.mailMsgId;
	}

	public MailerMessage fillMsg(MailerMessage obj) throws AddressException {
		if (fromEmail != null)
			obj.clearFrom().from(fromEmail);

		if (replyToEmail != null)
			obj.replyTo(replyToEmail);

		obj.to(toEmail);
		obj.subject(subject);

		if (this.isHtml == BitBool.TRUE) {
			obj.htmlContent(content);
		} else {
			obj.textContent(content);
		}
		return obj;
	}

	public Long getMailMsgId() {
		return mailMsgId;
	}

	public void setMailMsgId(Long mailMsgId) {
		this.mailMsgId = mailMsgId;
	}

	public String getFromEmail() {
		return fromEmail;
	}

	public void setFromEmail(String fromEmail) {
		this.fromEmail = fromEmail;
	}

	public String getReplyToEmail() {
		return replyToEmail;
	}

	public void setReplyToEmail(String replyToEmail) {
		this.replyToEmail = replyToEmail;
	}

	public String getToEmail() {
		return toEmail;
	}

	public void setToEmail(String toEmail) {
		this.toEmail = toEmail;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getIsHtml() {
		return isHtml;
	}

	public void setIsHtml(int isHtml) {
		this.isHtml = isHtml;
	}

	public int getServiceId() {
		return serviceId;
	}

	public void setServiceId(int serviceId) {
		this.serviceId = serviceId;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public int getMailerId() {
		return mailerId;
	}

	public void setMailerId(int mailerId) {
		this.mailerId = mailerId;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public long getTimeCreated() {
		return timeCreated;
	}

	public void setTimeCreated(long timeCreated) {
		this.timeCreated = timeCreated;
	}

	public Long getTimeSent() {
		return timeSent;
	}

	public void setTimeSent(Long timeSent) {
		this.timeSent = timeSent;
	}
}
