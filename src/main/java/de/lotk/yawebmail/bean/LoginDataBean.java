/*
 * @(#)LoginDataBean.java 1.00 2006/02/14
 *
 * Copyright (c) 2006, Stephan Sann
 *
 * 14.02.2006 ssann        Vers. 1.0     created
 */


package de.lotk.yawebmail.bean;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import de.lotk.yawebmail.application.Configuration;
import de.lotk.yawebmail.application.Lifecycle;
import de.lotk.yawebmail.enumerations.MailboxProtocolEnum;

/**
 * Bean die Daten zum Login auf einem Mailbox-Server aufnimmt 
 * 
 * @author Stephan Sann
 * @version 1.0
 */
public class LoginDataBean implements Lifecycle, Serializable {

  // ---------------------------------------------------------------- Konstanten

  /** serialVersionUID */
  private static final long serialVersionUID = 1129798977717127867L;


  // --------------------------------------------------------- Klassen-Variablen

  /** Zuordnung von Ports und Protokollen */
  private static Map<MailboxProtocolEnum, Integer> protocolPorts =
          new HashMap<MailboxProtocolEnum, Integer>();


  // --------------------------------------------------------- Instanz-Variablen

  /** Hier merken wir uns das Mailbox-Protokoll */
  private MailboxProtocolEnum mailboxProtocol = null;

  /** Hier merken wir uns den Mailbox-Host */
  private String mailboxHost = Configuration.getPreselectionMailboxHost();

  /** Hier merken wir uns den Mailbox-Port */
  private int mailboxPort = (-1);


  /** Hier merken wir uns den Mailbox-Benutzernamen */
  private String mailboxUser = null;

  /** Hier merken wir uns das Mailbox-Passwort */
  private String mailboxPassword = null;


  /** Arbeiten wir mit erweiterten Logon-Properties? */
  private boolean advancedLogonProperties = true;


  // ------------------------------------------------- statische Initialisierung

  static {

    // Zuordnung von Protokollen und Portnummern fuellen
    protocolPorts.put(MailboxProtocolEnum.POP3,
            (new Integer(Configuration.getPortnumberPop3())));
    protocolPorts.put(MailboxProtocolEnum.IMAP,
            (new Integer(Configuration.getPortnumberImap())));
    protocolPorts.put(MailboxProtocolEnum.POP3_SSL,
            (new Integer(Configuration.getPortnumberPop3s())));
    protocolPorts.put(MailboxProtocolEnum.IMAP_SSL,
            (new Integer(Configuration.getPortnumberImaps())));
  }


  // ----------------------------------------------------------- Konstruktor(en)

  /**
   * Initialisiert eine neue LoginDataBean-Instanz. 
   */
  public LoginDataBean() {

    // Einmalig "setMailboxProtocol" aufrufen, damit der Port gesetzt wird.
    this.setMailboxProtocol(MailboxProtocolEnum.byProtocolId(Configuration.getPreselectionMailboxProtocol()));
  }


  // --------------------------------------------------------- Getter und Setter

  /**
   * @return Returns the mailboxPort.
   */
  public int getMailboxPort() {
    return mailboxPort;
  }

  /**
   * @param mailboxPort The mailboxPort to set.
   */
  public void setMailboxPort(int mailboxPort) {
    this.mailboxPort = mailboxPort;
  }

  /**
   * @return Returns the mailboxProtocol.
   */
  public MailboxProtocolEnum getMailboxProtocol() {
    return mailboxProtocol;
  }

  /**
   * @param mailboxProtocol The mailboxProtocol to set.
   */
  public void setMailboxProtocol(MailboxProtocolEnum mailboxProtocol) {

    this.mailboxProtocol = mailboxProtocol;

    // Wenn wir uns NICHT im "Advanced mode" befinden, passen wir auch gleich
    // den Port an
    this.mailboxPort =
            ((Integer)protocolPorts.get(mailboxProtocol)).intValue();
  }

  /**
   * @return Returns the mailboxHost.
   */
  public String getMailboxHost() {
    return mailboxHost;
  }

  /**
   * @param mailboxHost The mailboxHost to set.
   */
  public void setMailboxHost(String mailboxHost) {
    this.mailboxHost = mailboxHost;
  }

  /**
   * @return Returns the mailboxPassword.
   */
  public String getMailboxPassword() {
    return mailboxPassword;
  }

  /**
   * @param mailboxPassword The mailboxPassword to set.
   */
  public void setMailboxPassword(String mailboxPassword) {
    this.mailboxPassword = mailboxPassword;
  }

  /**
   * @return Returns the mailboxUser.
   */
  public String getMailboxUser() {
    return mailboxUser;
  }

  /**
   * @param mailboxUser The mailboxUser to set.
   */
  public void setMailboxUser(String mailboxUser) {
    this.mailboxUser = mailboxUser;
  }

  /**
   * @return the advancedLogonProperties
   */
  public boolean isAdvancedLogonProperties() {
  
    return advancedLogonProperties;
  }

  /**
   * @param advancedLogonProperties the advancedLogonProperties to set
   */
  public void setAdvancedLogonProperties(boolean advancedLogonProperties) {
  
    this.advancedLogonProperties = advancedLogonProperties;
  }


  // ----------------------------------------------------- oeffentliche Methoden

  /**
   * Setzt die Bean zurueck
   */
  public void reset() {

    // Defaultmaessig befinden wir uns nicht im "Advanced mode"
    this.advancedLogonProperties = true;

    // Protocol und Port zuruecksetzen
    this.setMailboxProtocol(MailboxProtocolEnum.byProtocolId(Configuration.getPreselectionMailboxProtocol()));

    // Andere Properties zuruecksetzen
    this.mailboxHost = Configuration.getPreselectionMailboxHost();
    this.mailboxUser = null;
    this.mailboxPassword = null;
  }

  /* (non-Javadoc)
   * @see de.lotk.webftp.Lifecycle#destroy()
   */
  public void destroy() {

    this.reset();
  }

}
