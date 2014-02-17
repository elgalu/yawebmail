/*
 * @(#)SmtpConnectionBean.java 1.00 2006/04/21
 *
 * Copyright (c) 2006, Stephan Sann
 *
 * 21.04.2006 ssann        Vers. 1.0     created
 */


package de.lotk.yawebmail.bean;

import java.io.Serializable;

import de.lotk.yawebmail.application.Lifecycle;

/**
 * Speichert Infos zur SMTP-Connection
 * 
 * @author Stephan Sann
 * @version 1.0
 */
public class SmtpConnectionBean implements Lifecycle, Serializable {

  // ---------------------------------------------------------------- Konstanten

  /** serialVersionUID */
  private static final long serialVersionUID = 3869639447217374507L;


  // --------------------------------------------------------- Instanz-Variablen

  /** Hier merken wir uns den SMTP-Host */
  private String smtpHost = null;

  /** SMTP-Subdomain-Praefix */
  private String smtpSubdomainPrefix = null;

  /** SMTP-Domain */
  private String smtpDomain = null;

  /** Hier merken wir uns den SMTP-Port */
  private String smtpPort = "25";

  /** Hier merken wir uns bei SMTP-Auth den User */
  private String smtpAuthUser = null;

  /** Hier merken wir uns bei SMTP-Auth das Passwort */
  private String smtpAuthPass = null;

  /** Soll eine SSL-Verbindung hergestellt werden? */
  private boolean sslConnection = false;

  /** Soll eine TLS-Verbindung hergestellt werden? */
  private boolean tlsConnection = false;


  // --------------------------------------------------------- Getter und Setter

  /**
   * @param smtpHost The smtpHost to set.
   */
  public void setSmtpHost(String smtpHost) {
  
    this.smtpHost = smtpHost;
  }

  /**
   * @return Returns the smtpPort.
   */
  public String getSmtpPort() {
  
    return smtpPort;
  }

  /**
   * @param smtpPort The smtpPort to set.
   */
  public void setSmtpPort(String smtpPort) {
  
    this.smtpPort = smtpPort;
  }

  /**
   * @return Returns the smtpAuthPass.
   */
  public String getSmtpAuthPass() {

    return smtpAuthPass;
  }

  /**
   * @param smtpAuthPass The smtpAuthPass to set.
   */
  public void setSmtpAuthPass(String smtpAuthPass) {

    this.smtpAuthPass = smtpAuthPass;
  }

  /**
   * @return Returns the smtpAuthUser.
   */
  public String getSmtpAuthUser() {

    return smtpAuthUser;
  }

  /**
   * @param smtpAuthUser The smtpAuthUser to set.
   */
  public void setSmtpAuthUser(String smtpAuthUser) {
  
    this.smtpAuthUser = smtpAuthUser;
  }

  /**
   * @return the sslConnection
   */
  public boolean isSslConnection() {
  
    return sslConnection;
  }

  /**
   * @param sslConnection the sslConnection to set
   */
  public void setSslConnection(boolean sslConnection) {
  
    this.sslConnection = sslConnection;
  }

  /**
   * @return the tlsConnection
   */
  public boolean isTlsConnection() {
  
    return tlsConnection;
  }

  /**
   * @param tlsConnection the tlsConnection to set
   */
  public void setTlsConnection(boolean tlsConnection) {
  
    this.tlsConnection = tlsConnection;
  }

  /**
   * @return the smtpSubdomainPrefix
   */
  public String getSmtpSubdomainPrefix() {

    return smtpSubdomainPrefix;
  }

  /**
   * @param smtpSubdomainPrefix the smtpSubdomainPrefix to set
   */
  public void setSmtpSubdomainPrefix(String smtpSubdomainPrefix) {

    this.smtpSubdomainPrefix = smtpSubdomainPrefix;
  }

  /**
   * @return the smtpDomain
   */
  public String getSmtpDomain() {

    return smtpDomain;
  }

  /**
   * @param smtpDomain the smtpDomain to set
   */
  public void setSmtpDomain(String smtpDomain) {

    this.smtpDomain = smtpDomain;
  }


  // ----------------------------------------------------- oeffentliche Methoden

  /**
   * @return Returns the smtpHost.
   */
  public String getSmtpHost() {

    // Wenn smtpHost-Property NULL ist, dafuer aber smtpDomain und optional
    // smtpSubdomainPrefix am Start sind, diese liefern.
    if((smtpHost == null) && (smtpDomain != null)) {

      StringBuilder hostName = new StringBuilder();

      if(smtpSubdomainPrefix != null) {

        hostName.append(smtpSubdomainPrefix);  
      }

      hostName.append(smtpDomain);

      return(hostName.toString());
    }
    else {

      return(smtpHost);
    }
  }

  /**
   * Liefert den SMTP-Port als int.
   * 
   * @return  <code>int</code>-Wert des SMTP-Ports
   */
  public int getSmtpPortAsInt() {

    return(Integer.parseInt(this.smtpPort));
  }

  /**
   * Setzt die SmtpConnectionBean auf ihre Defaultwerte zurueck
   */
  public void reset() {

    this.smtpHost = null;
    this.smtpDomain = null;
    this.smtpSubdomainPrefix = null;
    this.smtpPort = "25";
    this.smtpAuthUser = null;
    this.smtpAuthPass = null;
    this.sslConnection = false;
    this.tlsConnection = false;
  }

  /* (non-Javadoc)
   * @see de.lotk.yawebmail.application.Lifecycle#destroy()
   */
  public void destroy() {

    this.reset();
  }

}
