package cc.kune.core.server.notifier;

import cc.kune.core.server.mail.FormatedString;
import cc.kune.core.server.utils.AbsoluteFileDownloadUtils;
import cc.kune.core.shared.utils.SharedFileDownloadUtils;

import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * The Class NotifyHtmlHelper is used to get html snippets for email
 * notifications.
 */
@Singleton
public class NotifyHtmlHelper {

  /** The template used from messages snippets from a group */
  private static String GROUP_TEMPLATE = "<table style=\"FIXME\" border=\"0\" cellpadding=\"2\" cellspacing=\"2\" width=\"100%%\">"
      + "<tbody><tr>"
      + "<td style=\"\" valign=\"top\"><a href=\"%s\">%s</a></td>"
      + "<td style=\"\" valign=\"top\" width=\"100%%\">"
      + "<a href=\"%s\">%s</a><br>"
      + "%s"
      + "</td></tr></tbody></table>";

  private final SharedFileDownloadUtils fileDownloadUtils;

  /**
   * Instantiates a new notify html helper.
   * 
   * @param fileDownloadUtils
   *          the file download utils
   */
  @Inject
  public NotifyHtmlHelper(final AbsoluteFileDownloadUtils fileDownloadUtils) {
    this.fileDownloadUtils = fileDownloadUtils;
  }

  /**
   * Generates a group notification in html like [logo|message]
   * 
   * @param groupName
   *          the group name you want to get the notification
   * @param hasLogo
   *          the has logo?
   * @param message
   *          the message to show close to the logo
   * @param readMoreMsg
   *          the read more msg
   * @return the html string
   */
  public FormatedString groupNotification(final String groupName, final boolean hasLogo,
      final String message) {
    final String groupUrl = fileDownloadUtils.getPrefix() + "#" + groupName;
    return FormatedString.build(false, GROUP_TEMPLATE, groupUrl,
        fileDownloadUtils.getLogoAvatarHtml(groupName, hasLogo, false, 50, 50), groupUrl, groupName,
        message);
  }
}