/*
 * @(#)LogonController.java 1.00 2006/02/14
 *
 * Copyright (c) 2006, Stephan Sann
 *
 * 14.02.2006 ssann        Vers. 1.0     created
 */


package de.lotk.yawebmail.controller;

import java.util.Locale;

import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;

import org.apache.oro.text.perl.Perl5Util;

import de.lotk.yawebmail.application.Configuration;
import de.lotk.yawebmail.application.Constants;
import de.lotk.yawebmail.bean.LoginDataBean;
import de.lotk.yawebmail.bean.SessionContainerBean;
import de.lotk.yawebmail.bean.SmtpConnectionBean;
import de.lotk.yawebmail.business.MailboxConnection;
import de.lotk.yawebmail.business.MailboxConnectionFactory;
import de.lotk.yawebmail.enumerations.SmtpHostChoiceEnum;
import de.lotk.yawebmail.util.VersionMonitor;
import de.lotk.yawebmail.util.faces.ExceptionConverter;

/**
 * Controller fuer das Logon 
 * 
 * @author Stephan Sann
 * @version 1.0
 */
public class LogonController extends BaseController {

  // --------------------------------------------------------- Klassen-Variablen

  /** Perl5Util zum Suchen und Ersetzen */
  private static Perl5Util perl5Util = new Perl5Util();


  // ---------------------------------------------------------- private Methoden

  /**
   * Initiale Befuellung des SMTP-Connection-Objektes.
   * 
   * @param   smtpConnection  Zu befuellendes SmtpConnectionBean-Objekt
   * @param   loginData       Die Login-Daten
   */
  private void prefillSmtpConnection(SmtpConnectionBean smtpConnection,
          LoginDataBean loginData) {

    smtpConnection.setSmtpAuthUser(loginData.getMailboxUser());

    // Durch Konfiguration erzwungener SMTP-Host
    if(Configuration.getSmtpHostChoice() == SmtpHostChoiceEnum.NONE) {

      smtpConnection.setSmtpHost(Configuration.getForcedSmtpHostName());
      smtpConnection.setSmtpPort(Configuration.getForcedSmtpHostPort());
    }

    // SMTP-Host frei oder innerhalb der Domain waehlbar
    else {

      String mailboxHost = loginData.getMailboxHost();

      // SMTP-Host innerhalb der Domain waehlbar
      if(Configuration.getSmtpHostChoice() == SmtpHostChoiceEnum.DOMAIN) {

        if(perl5Util.match("m#^(pop|imap)\\d?s?\\.#i", mailboxHost)) {

          String smtpDomain =
                  perl5Util.substitute("s#^(pop|imap)\\d?s?\\.##i", mailboxHost);

          smtpConnection.setSmtpSubdomainPrefix("smtp.");
          smtpConnection.setSmtpDomain(smtpDomain);
        }
        else {

          smtpConnection.setSmtpDomain(mailboxHost);
        }
      }

      // SMTP-Host frei waehlbar
      else {

        if(perl5Util.match("m#^(pop|imap)\\d?s?\\.#i", mailboxHost)) {

          String smtpHost =
                  perl5Util.substitute("s#^(pop|imap)\\d?s?#smtp#i", mailboxHost);

          smtpConnection.setSmtpHost(smtpHost);
        }
        else {

          smtpConnection.setSmtpHost(mailboxHost);
        }
      }
    }
  }


  // ----------------------------------------------------- oeffentliche Methoden

  /**
   * Versucht das Logon durchzufuehren.
   * 
   * @return          <code>String</code>-Objekt mit Info, wohin navigiert wird. 
   */
  public String logon() {

    FacesContext facesContext = FacesContext.getCurrentInstance(); 

    try {

      // SessionContainer- und LoginDataBean holen
      SessionContainerBean sessionContainer =
              (SessionContainerBean)this.getManagedBeanByName(Constants.NAME_MBEAN_SESSIONCONTAINER);
      LoginDataBean loginData =
              (LoginDataBean)this.getManagedBeanByName(Constants.NAME_MBEAN_LOGINDATA);
      SmtpConnectionBean smtpConnection =
              (SmtpConnectionBean)this.getManagedBeanByName(Constants.NAME_MBEAN_SMTPCONNECTION);

      // Beim Login setzen wir als aktuellen Folder immer "INBOX".
      sessionContainer.setCurrentMailboxFolder("INBOX");

      // MailboxConnection erstellen und in Session speichern
      MailboxConnection mailboxConnection =
              MailboxConnectionFactory.getInstance().createMailboxConnection(loginData,
                      sessionContainer.getMailboxTrustManager());
      sessionContainer.setMailboxConnection(mailboxConnection);

      // Versuchen, einen Login durchzufuehren
      //mailboxConnection.validateLoginData();

      // SmtpConnection-Objekt vorbefuellen
      this.prefillSmtpConnection(smtpConnection, loginData);
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
   * Erweiterte Login-Properties sollen konfiguriert werden.
   * 
   * @return          <code>String</code>-Objekt mit Info, wohin navigiert wird. 
   */
  public String configureAdvancedLogonProperties() {

    FacesContext facesContext = FacesContext.getCurrentInstance(); 

    try {

      // LoginDataBean holen
      LoginDataBean loginData =
              (LoginDataBean)this.getManagedBeanByName(Constants.NAME_MBEAN_LOGINDATA);

      // AdvancedLogonProperties "aktivieren"
      loginData.setAdvancedLogonProperties(true);

      // TODO raus! (Dirty Hack, damit der Host "stehen bleibt")
      UIInput uicMbHost =
              (UIInput)facesContext.getViewRoot().findComponent(Constants.CLIENT_ID_MAILBOXHOST);
      loginData.setMailboxHost((String)uicMbHost.getSubmittedValue());
    }
    catch(Exception e) {

      facesContext.addMessage(null,
              ExceptionConverter.getFacesMessage(facesContext, e, true));
      return(Constants.OUTCOME_TECH_ERROR);
    }

    // Wenn wir hier angelangt sind, scheint alles i.O. zu sein.
    return(Constants.OUTCOME_LOGON);
  }

  /**
   * Formular zuruecksetzen.
   * 
   * @return          <code>String</code>-Objekt mit Info, wohin navigiert wird. 
   */
  public String reset() {

    FacesContext facesContext = FacesContext.getCurrentInstance(); 

    try {

      // LoginDataBean holen
      LoginDataBean loginData =
              (LoginDataBean)this.getManagedBeanByName(Constants.NAME_MBEAN_LOGINDATA);

      // LoginDataBean zuruecksetzen
      loginData.reset();
    }
    catch(Exception e) {

      facesContext.addMessage(null,
              ExceptionConverter.getFacesMessage(facesContext, e, true));
      return(Constants.OUTCOME_TECH_ERROR);
    }

    // Wenn wir hier angelangt sind, scheint alles i.O. zu sein.
    return(Constants.OUTCOME_LOGON);
  }

  /**
   * Wird beim Wechseln der Sprache involviert.
   * 
   * @param   event  Aktuelles ValueChangeEvent.
   */
  public void changeLanguage(ValueChangeEvent event) {

    FacesContext fc = FacesContext.getCurrentInstance();

    // Wir machen nichts, ausser die Sprache zu wechseln.
    fc.getViewRoot().setLocale((Locale)event.getNewValue());

    // Und gleich wieder die View rendern.
    fc.renderResponse();
  }

  /**
   * Soll das Eingabefeld fuer den Mailbox-Host editierbar sein?
   * 
   * @return  <code>true</code>, wenn das Feld editierbar sein soll;
   *          <code>false</code>, wenn das Feld nicht editierbar sein soll.
   */
  public boolean isEditableMailboxHost() {

    return(! Configuration.isForcePreselectedMailboxHost());
  }

  /**
   * Soll das Eingabefeld fuer das Mailbox-Protokoll editierbar sein?
   * 
   * @return  <code>true</code>, wenn das Feld editierbar sein soll;
   *          <code>false</code>, wenn das Feld nicht editierbar sein soll.
   */
  public boolean isEditableMailboxProtocol() {

    return(! Configuration.isForcePreselectedMailboxProtocol());
  }

  /**
   * Is there a new version of yawebmail available?
   * 
   * @return  <code>true</code>, if there is a new version available;
   *          <code>false</code>, if not.
   */
  public boolean isNewYawebmailVersionAvailable() {

    return(VersionMonitor.isCurrentVersionGreaterThanConfiguredVersion());
  }

}
