/*
 *
 * Copyright (C) 2007-2011 The kune development team (see CREDITS for details)
 * This file is part of kune.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package cc.kune.core.server.mail;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cc.kune.core.server.LogThis;
import cc.kune.core.server.properties.KuneProperties;

import com.google.common.base.Preconditions;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
@LogThis
public class MailServiceDefault implements MailService {

  public static class FormatedString {
    public static FormatedString build(final String plainMsg) {
      return new FormatedString(plainMsg);
    }

    public static FormatedString build(final String template, final Object... args) {
      return new FormatedString(template, args);
    }

    private final Object[] args;
    private String template;

    public FormatedString(final String plainMsg) {
      template = plainMsg;
      args = null;
    }

    public FormatedString(final String template, final Object... args) {
      this.template = template;
      this.args = args;
    }

    public String getString() {
      Preconditions.checkNotNull(template, "Template of FormatedString cannot be null");
      return args == null ? template : String.format(template, args);
    }

    public String getTemplate() {
      return template;
    }

    /*
     * Used to translate the template to the user language (when you don't know
     * already the language of the user)
     */
    public void setTemplate(final String template) {
      this.template = template;
    }
  }

  Log log = LogFactory.getLog(MailServiceDefault.class);
  private final Properties props;
  private final String smtpDefaultFrom;
  private final boolean smtpSkip;

  @Inject
  public MailServiceDefault(final KuneProperties kuneProperties) {
    final String smtpServer = kuneProperties.get(KuneProperties.SITE_SMTP_HOST);
    smtpDefaultFrom = kuneProperties.get(KuneProperties.SITE_SMTP_DEFAULT_FROM);
    smtpSkip = kuneProperties.getBoolean(KuneProperties.SITE_SMTP_SKIP);
    props = System.getProperties();
    props.put("mail.smtp.host", smtpServer);
  }

  @Override
  public void send(final FormatedString subject, final FormatedString body, final boolean isHtml,
      final String... tos) {
    send(smtpDefaultFrom, subject, body, isHtml, tos);
  }

  @Override
  public void send(final String from, final FormatedString subject, final FormatedString body,
      final boolean isHtml, final String... tos) {
    if (smtpSkip) {
      return;
    }

    // Get session
    final Session session = Session.getDefaultInstance(props, null);

    // Define message
    final MimeMessage message = new MimeMessage(session);
    for (final String to : tos) {
      try {
        message.setFrom(new InternetAddress(from));
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
        final String formatedSubject = subject.getString();
        message.setSubject(formatedSubject);
        final String formatedBody = body.getString();
        if (isHtml) {
          message.setContent(formatedBody, "text/html");
        } else {
          message.setText(formatedBody);
        }
        // Send message
        Transport.send(message);
      } catch (final AddressException e) {
      } catch (final MessagingException e) {
        final String error = String.format(
            "Error sendind an email to %s, with subject: %s, and body: %s", from, subject, to);
        log.error(error, e);
        // Better not to throw exceptions because users emails can be wrong...
        // throw new DefaultException(error, e);
      }
    }
  }

  @Override
  public void sendHtml(final FormatedString subject, final FormatedString body, final String... tos) {
    send(smtpDefaultFrom, subject, body, true, tos);
  }

  @Override
  public void sendHtml(final String from, final FormatedString subject, final FormatedString body,
      final String... tos) {
    send(from, subject, body, true, tos);
  }

  @Override
  public void sendPlain(final FormatedString subject, final FormatedString body, final String... tos) {
    send(smtpDefaultFrom, subject, body, false, tos);
  }

  @Override
  public void sendPlain(final String from, final FormatedString subject, final FormatedString body,
      final String... tos) {
    send(from, subject, body, false, tos);
  }
}
