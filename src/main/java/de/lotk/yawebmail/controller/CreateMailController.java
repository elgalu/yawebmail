/*
 * @(#)CreateMailController.java 1.00 2006/04/24
 *
 * Copyright (c) 2006, Stephan Sann
 *
 * 24.04.2006 ssann        Vers. 1.0     created
 */


package de.lotk.yawebmail.controller;

import gnu.inet.encoding.IDNA;

import java.net.SocketException;
import java.util.Date;
import java.util.Locale;

import javax.faces.context.FacesContext;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.SendFailedException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.net.ssl.SSLHandshakeException;

import com.sun.mail.smtp.SMTPTransport;

import de.lotk.yawebmail.application.Configuration;
import de.lotk.yawebmail.application.Constants;
import de.lotk.yawebmail.bean.CheckCertsBean;
import de.lotk.yawebmail.bean.MailBasisBean;
import de.lotk.yawebmail.bean.SessionContainerBean;
import de.lotk.yawebmail.bean.SmtpConnectionBean;
import de.lotk.yawebmail.enumerations.MailTransportProtocolEnum;
import de.lotk.yawebmail.enumerations.SmtpHostChoiceEnum;
import de.lotk.yawebmail.exceptions.YawebmailCertificateException;
import de.lotk.yawebmail.util.JavamailUtils;
import de.lotk.yawebmail.util.faces.ExceptionConverter;
import de.lotk.yawebmail.util.faces.ManagedBeanUtils;

/**
 * Controller fuer das Erstellen und Senden von Mails 
 * 
 * @author Stephan Sann
 * @version 1.0
 */
public class CreateMailController extends BaseController {

  // ---------------------------------------------------------- private Methoden

  /**
   * Baut den User-Agent-Header-String zusammen.
   * 
   * @param   locale  Aktuelle Locale.
   * @return          <code>String</code> mit User-Agent-Header
   */
  private String assembleUserAgent(Locale locale) {

    // Falls ein Security-Manager das Lesen der System-Properties verbietet...
    try {

      String osName = System.getProperty("os.name");

      StringBuffer uaParts = new StringBuffer(Configuration.getProgramName());
      uaParts.append("/");
      uaParts.append(Configuration.getProgramVersion().toSimpleString());
      uaParts.append(" (").append(osName).append("; U; ");
      uaParts.append(osName).append(" ");
      uaParts.append(System.getProperty("os.version")).append(" (");
      uaParts.append(System.getProperty("os.arch")).append("); ");
      uaParts.append(locale.toString());
      uaParts.append(") http://yawebmail.sourceforge.net/");

      return(uaParts.toString());
    }

    // ...liefern wir einen einfachen String
    catch(Exception e) {

      return(Configuration.getProgramName() + "/" +
              Configuration.getProgramVersion().toSimpleString() +
              " - http://yawebmail.sourceforge.net/");
    }
  }

  /**
   * Baut ein Multipart-Objekt aus der uebergebenen MailBasisBean zusammen.
   * 
   * @param   mbb      MailBasisBean, aus der die notwendigen Infos kommen.
   * @return           <code>Multipart</code> gemaess MailBasisBean
   */
  private Multipart assembleMulitpart(MailBasisBean mbb) throws
          MessagingException {

    Multipart mp = new MimeMultipart();

    // Text in den Multipart einfuegen
    MimeBodyPart mbp = new MimeBodyPart();
    mbp.setText(mbb.getMailText(), Constants.MESSAGE_CHAR_ENCODING);
    mp.addBodyPart(mbp);

    // Attachments in den Multipart einfuegen
    for(int hh = 0; hh < mbb.getAttachments().size(); hh++) {

      mp.addBodyPart((MimeBodyPart)mbb.getAttachments().get(hh));
    }

    return(mp);
  }

  /**
   * Wandelt eMail-Adressen mit Umlauten in die Punycode-Entsprechung.
   * 
   * @param   eMailAddress  Zu wandelnde eMail-Adresse
   * @return  <code>String</code> mit gewandelter Adresse.
   */
  private String punyEncode(String eMailAddress) throws Exception {

    StringBuffer result = new StringBuffer();
    String[] addressParts = eMailAddress.split("@");

    result.append(IDNA.toASCII(addressParts[0]));
    result.append('@');
    result.append(IDNA.toASCII(addressParts[1]));

    return(result.toString());
  }

  /**
   * Erstellt aus einem Adress-String eine encodete (Sonderzeichen und
   * IDN-Domains codiert) InternetAddress.
   * 
   * @param   addressString  Adress-String
   * @return  <code>InternetAddress</code>-Objekt.
   */
  private InternetAddress getInternetAddressFromString(String addressString)
          throws AddressException {

    InternetAddress ia = new InternetAddress(addressString);

    try {

      ia.setPersonal(ia.getPersonal(), Constants.MESSAGE_CHAR_ENCODING);
      ia.setAddress(this.punyEncode(ia.getAddress()));
    }
    catch(Exception e) {

      // Nur Stacktrace ausgeben, sonst nix - dann bleibt die Adresse eben
      // uncodiert...
      e.printStackTrace();
    }

    return(ia); 
  }

  /**
   * Erstellt aus einem String mit Mail-Adressen ein Address-Array.
   * 
   * @param  addressesArrayString  String mit Mail-Adressen
   * @return                       <code>Address[]</code> mit Adressen aus Str.
   */
  private Address[] getAddressArrayFromString(String addressesString) throws
          AddressException {

    String[] strAddresses = addressesString.split(",");
    Address[] inetAddresses = new Address[strAddresses.length];

    for(int ii = 0; ii < strAddresses.length; ii++) {

      inetAddresses[ii] =
              this.getInternetAddressFromString(strAddresses[ii].trim());
    }

    return(inetAddresses);
  }

  /**
   * Baut eine MimeMessage aus der uebergebenen MailBasisBean zusammen.
   * 
   * @param   mbb      MailBasisBean, aus der die notwendigen Infos kommen.
   * @param   jms      <code>javax.mail.Session</code> fuer die MimeMessage.
   * @param   locale   Aktuelle Locale
   * @return           <code>MimeMessage</code> gemaess MailBasisBean
   */
  private MimeMessage assembleMimeMessage(MailBasisBean mbb, Session jms, Locale
          locale) throws MessagingException {

    // Erstmal die MimeMessage erstellen
    MimeMessage mm = new MimeMessage(jms);

    // User-Agent setzen
    mm.addHeader("User-Agent", this.assembleUserAgent(locale));

    // Absender, Empfaenger, Betreff
    if((mbb.getFrom() != null) && (mbb.getFrom().length() >= 1)) {

      try {

        mm.setFrom(this.getInternetAddressFromString(mbb.getFrom()));
      }
      catch(AddressException ae) {

        AddressException neuAe = new AddressException(ae.getMessage(),
                Constants.CLIENT_ID_MAILFROM);
        neuAe.setNextException(ae);

        throw(neuAe);
      }
    }
    if((mbb.getRcptTo() != null) && (mbb.getRcptTo().length() >= 1)) {

      try {

        mm.setRecipients(Message.RecipientType.TO,
                this.getAddressArrayFromString(mbb.getRcptTo()));
      }
      catch(AddressException ae) {

        AddressException neuAe = new AddressException(ae.getMessage(),
                Constants.CLIENT_ID_RCPT_TO);
        neuAe.setNextException(ae);

        throw(neuAe);
      }
    }
    if((mbb.getRcptCc() != null) && (mbb.getRcptCc().length() >= 1)) {

      try {

        mm.setRecipients(Message.RecipientType.CC,
                this.getAddressArrayFromString(mbb.getRcptCc()));
      }
      catch(AddressException ae) {

        AddressException neuAe = new AddressException(ae.getMessage(),
                Constants.CLIENT_ID_RCPT_CC);
        neuAe.setNextException(ae);

        throw(neuAe);
      }
    }
    if((mbb.getRcptBcc() != null) && (mbb.getRcptBcc().length() >= 1)) {

      try {

        mm.setRecipients(Message.RecipientType.BCC,
                this.getAddressArrayFromString(mbb.getRcptBcc()));
      }
      catch(AddressException ae) {

        AddressException neuAe = new AddressException(ae.getMessage(),
                Constants.CLIENT_ID_RCPT_BCC);
        neuAe.setNextException(ae);

        throw(neuAe);
      }
    }

    mm.setSentDate(new Date());
    mm.setSubject(mbb.getSubject(), Constants.MESSAGE_CHAR_ENCODING);

    // Ggf. "In-Reply-To"-Header setzen
    if((mbb.getInReplyTo() != null) && (mbb.getInReplyTo().length() >= 1)) {

      mm.setHeader("In-Reply-To", mbb.getInReplyTo());
    }

    // MailBody -> Wenn keine Attachments am Start, Plain-Text, sonst Multipart
    if(mbb.getAttachments().size() < 1) {

      mm.setText(mbb.getMailText(), Constants.MESSAGE_CHAR_ENCODING);
    }
    else {

      mm.setContent(this.assembleMulitpart(mbb));
    }

    mm.saveChanges();

    return(mm);
  }

  /**
   * Set back the create-mail-form. Then navigate to the given target.
   * 
   * @return  <code>String</code>-object with info where to navigate to 
   */
  public String reset(String navigationTarget) {

    FacesContext facesContext = FacesContext.getCurrentInstance();

    try {

      // SMTP-Connection und Mail-Basis holen
      MailBasisBean mailBasis =
              (MailBasisBean)this.getManagedBeanByName(Constants.NAME_MBEAN_MAILBASIS);

      // MailBasis-Bean zuruecksetzen
      mailBasis.reset();
    }
    catch(Exception e) {

      facesContext.addMessage(null,
              ExceptionConverter.getFacesMessage(facesContext, e, true));
      return(Constants.OUTCOME_TECH_ERROR);
    }

    // Wenn wir hier angelangt sind, scheint alles i.O. zu sein.
    return(navigationTarget);
  }


  // ----------------------------------------------------- oeffentliche Methoden

  /**
   * Set back mail-creation-form.
   * 
   * @return  <code>String</code>-object with info where to navigate to 
   */
  public String reset() {

    return(this.reset(Constants.OUTCOME_CREATE_MAIL));
  }

  /**
   * Set back mail-creation-form and return to the mails-listing-page.
   * 
   * @return  <code>String</code>-object with info where to navigate to 
   */
  public String cancel() {

    return(this.reset(Constants.OUTCOME_MAILS_LISTING));
  }

  /**
   * Versucht eine Mail zu senden.
   * 
   * @return          <code>String</code>-Objekt mit Info, wohin navigiert wird. 
   */
  public String sendMail() {

    FacesContext facesContext = FacesContext.getCurrentInstance();

    try {

      // Catch SessionContainer, SMTP-Connection and Mail-Basis
      SmtpConnectionBean smtpConnection =
              (SmtpConnectionBean)this.getManagedBeanByName(Constants.NAME_MBEAN_SMTPCONNECTION);
      SessionContainerBean sessionContainer =
              (SessionContainerBean)this.getManagedBeanByName(Constants.NAME_MBEAN_SESSIONCONTAINER);
      MailBasisBean mailBasis =
              (MailBasisBean)this.getManagedBeanByName(Constants.NAME_MBEAN_MAILBASIS);

      try {

        // javax.mail.Session und MimeMessage erstellen
        Session jms = JavamailUtils.assembleJavaxMailSession(smtpConnection,
                sessionContainer.getSmtpTrustManager());
        MimeMessage mm = this.assembleMimeMessage(mailBasis, jms,
                facesContext.getViewRoot().getLocale());

        // Mail versenden - ohne SSL
        if(! smtpConnection.isSslConnection()) {

          Transport.send(mm);
        }

        // Mail versenden - mit SSL
        else {

          SMTPTransport transport =
                (SMTPTransport)jms.getTransport(MailTransportProtocolEnum.SMTP_SSL.getProtocolId());

          transport.connect(smtpConnection.getSmtpHost(),
                  smtpConnection.getSmtpPortAsInt(),
                  smtpConnection.getSmtpAuthUser(),
                  smtpConnection.getSmtpAuthPass());
          transport.sendMessage(mm, mm.getAllRecipients());
          transport.close();
        }

        // MailBasisBean zuruecksetzen
        mailBasis.reset();
      }
      catch(AddressException ae) {

        String viewId = ae.getRef();

        // Wenn eine Cause-Exception existiert und diese vom Typ
        // AddressException ist, die Variable ae auf die Cause-Exception setzen. 
        if((ae.getCause() != null) &&
                (ae.getCause() instanceof AddressException)) {

          ae = (AddressException)ae.getCause();
        }

        facesContext.addMessage(viewId,
                ExceptionConverter.getFacesMessage(facesContext, ae, false));
        return(Constants.OUTCOME_CREATE_MAIL);
      }
      catch(SendFailedException sfe) {

        // Hier wollen wir mehr wissen...
        sfe.printStackTrace();

        facesContext.addMessage(null,
                ExceptionConverter.getFacesMessage(facesContext, sfe, false));
        return(Constants.OUTCOME_CREATE_MAIL);
      }
      catch(MessagingException me) {

        Exception ne = me.getNextException();

        if(ne instanceof SocketException) {

          facesContext.addMessage(Constants.CLIENT_ID_SMTPHOST,
                  ExceptionConverter.getFacesMessage(facesContext,
                  (SocketException)ne, false));
          return(Constants.OUTCOME_CREATE_MAIL);
        }
        else if((ne instanceof SSLHandshakeException) &&
                (ne.getCause() instanceof YawebmailCertificateException)) {

          CheckCertsBean checkCerts =
                  (CheckCertsBean)ManagedBeanUtils.getManagedBeanByName(facesContext,
                          Constants.NAME_MBEAN_CHECKCERTS);

          checkCerts.setWhichCerts(CheckCertsController.WhichCertsEnum.SMTP);
          checkCerts.setServerCerts(((YawebmailCertificateException)ne.getCause()).getCerts());

          return(Constants.OUTCOME_CHECK_CERTS);
        }
        else {

          throw(me);
        }
      }
    }
    catch(Exception e) {

      facesContext.addMessage(null,
              ExceptionConverter.getFacesMessage(facesContext, e, true));
      return(Constants.OUTCOME_TECH_ERROR);
    }

    // Wenn wir hier angelangt sind, scheint alles i.O. zu sein.
    return(Constants.OUTCOME_MAILS_LISTING);
  }

  /**
   * Freie SMTP-Host-Wahl?
   * 
   * @return  <code>true</code>, bei freier SMTP-Host-Wahl;
   *          <code>false</code>, wenn SMTP-Host-Wahl nicht frei.
   */
  public boolean isSmtpHostChoiseFree() {

    return(Configuration.getSmtpHostChoice() == SmtpHostChoiceEnum.FREE);
  }

  /**
   * Freie SMTP-Host-Wahl innerhalb der Mailbox-Domain?
   * 
   * @return  <code>true</code>, bei SMTP-Host-Wahl "Domain";
   *          <code>false</code>, wenn SMTP-Host-Wahl nicht "Domain".
   */
  public boolean isSmtpHostChoiseDomain() {

    return(Configuration.getSmtpHostChoice() == SmtpHostChoiceEnum.DOMAIN);
  }

  /**
   * Keine SMTP-Host-Wahl?
   * 
   * @return  <code>true</code>, bei SMTP-Host-Wahl "none";
   *          <code>false</code>, wenn SMTP-Host-Wahl nicht "none".
   */
  public boolean isSmtpHostChoiseNone() {

    return(Configuration.getSmtpHostChoice() == SmtpHostChoiceEnum.NONE);
  }

}
