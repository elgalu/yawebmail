/*
 * @(#)Configuration.java 1.00 2007/12/03
 *
 * Copyright (c) 2007, Stephan Sann
 *
 * 03.12.2007 ssann        Vers. 1.0     created
 */


package de.lotk.yawebmail.application;

import java.util.Properties;

import de.lotk.yawebmail.enumerations.SmtpHostChoiceEnum;
import de.lotk.yawebmail.util.ClassLoaderUtils;

/**
 * Holds configuration-values.
 * 
 * @author Stephan Sann
 * @version 1.0
 */
public final class Configuration {

  // ------------------------------------------------------------- instance-vars

  // Application-properties

  /** Name of program */
  private static String programName;

  /** Version of program */
  private static YawebmailVersion programVersion;

  /** URL where to retrieve current version */
  private static String urlCurrentVersion;

  /** Max size of a subject on message-overview-site */
  private static int maxLenSubjectOnMailslisting;

  /** Should all messages be displayed on one page? */
  private static boolean allMessagesOnOnePage;

  /** Default amout of messages per page (in case (! allMessagesOnOnePage)) */
  private static int amountOfMessagesPerPage;

  // Logon-properties

  /** POP3-portnumber */
  private static int portnumberPop3; 

  /** IMAP-portnumber */
  private static int portnumberImap; 

  /** POP3 (SSL)-portnumber */
  private static int portnumberPop3s; 

  /** IMAP (SSL)-portnumber */
  private static int portnumberImaps; 

  /** Preselected mailbox-Host */
  private static String preselectionMailboxHost;

  /** Should the preselected mailbox-host be forced? */
  private static boolean forcePreselectedMailboxHost;

  /** Preselected mailbox-protocol */
  private static String preselectionMailboxProtocol;

  /** Should the preselected mailbox-protocol be forced? */
  private static boolean forcePreselectedMailboxProtocol;

  // SMTP-Properties

  /** SMTP-host-choice */
  private static SmtpHostChoiceEnum smtpHostChoice;

  /** Forced SMTP-host-Name */
  private static String forcedSmtpHostName;

  /** Forced SMTP-host-Port */
  private static String forcedSmtpHostPort;


  // ----------------------------------------------------- static initialization

  static {

    // Read property files.
    Properties appProps = new Properties();
    Properties logonProps = new Properties();
    Properties smtpProps = new Properties();

    try {

      ClassLoader classLoader =
              ClassLoaderUtils.getCurrentClassLoader(appProps);
      appProps.load(classLoader.getResourceAsStream(Constants.PROPERTIES_FILE_APPLICATION));
      logonProps.load(classLoader.getResourceAsStream(Constants.PROPERTIES_FILE_LOGON));
      smtpProps.load(classLoader.getResourceAsStream(Constants.PROPERTIES_FILE_SMTP));
    }
    catch(Exception e) {

      e.printStackTrace();
      throw(new RuntimeException("Properties could not be loaded!"));
    }

    // Set application-properties

    programName = appProps.getProperty("program_name");
    programVersion = YawebmailVersion.getInstance(appProps.getProperty("program_version"));
    urlCurrentVersion = appProps.getProperty("url_current_version");

    maxLenSubjectOnMailslisting =
            Integer.parseInt(appProps.getProperty("max_len_subject_on_mailslisting", "50"));
    allMessagesOnOnePage =
            Boolean.valueOf(appProps.getProperty("display_all_messages_on_one_page",
                    "true")).booleanValue();
    amountOfMessagesPerPage =
            Integer.parseInt(appProps.getProperty("amout_of_messages_per_page", "25"));

    // Set default logon-properties
    portnumberPop3 =
            Integer.parseInt(logonProps.getProperty("portnumber_pop3", "110"));
    portnumberImap =
            Integer.parseInt(logonProps.getProperty("portnumber_imap", "143"));
    portnumberPop3s =
            Integer.parseInt(logonProps.getProperty("portnumber_pop3s", "995"));
    portnumberImaps =
            Integer.parseInt(logonProps.getProperty("portnumber_imaps", "993"));

    preselectionMailboxHost =
            logonProps.getProperty("preselection_mailbox_host");
    forcePreselectedMailboxHost =
            Boolean.valueOf(logonProps.getProperty("force_preselected_mailbox_host")).booleanValue();
    preselectionMailboxProtocol =
            logonProps.getProperty("preselection_mailbox_protocol");
    forcePreselectedMailboxProtocol =
            Boolean.valueOf(logonProps.getProperty("force_preselected_mailbox_protocol")).booleanValue();

    // Set SMTP-properties
    smtpHostChoice =
            SmtpHostChoiceEnum.byId(smtpProps.getProperty("smtp_host_choice", "free"));
    forcedSmtpHostName = smtpProps.getProperty("forced_smtp_host_name");
    forcedSmtpHostPort = smtpProps.getProperty("forced_smtp_host_port");
  }


  // --------------------------------------------------------- getter and setter

  /**
   * @return the forcedSmtpHostName
   */
  public static String getForcedSmtpHostName() {
  
    return forcedSmtpHostName;
  }

  /**
   * @param forcedSmtpHostName the forcedSmtpHostName to set
   */
  public static void setForcedSmtpHostName(String forcedSmtpHostName) {
  
    Configuration.forcedSmtpHostName = forcedSmtpHostName;
  }

  /**
   * @return the programName
   */
  public static String getProgramName() {
  
    return programName;
  }

  /**
   * @param programName the programName to set
   */
  public static void setProgramName(String programName) {
  
    Configuration.programName = programName;
  }

  /**
   * @return the programVersion
   */
  public static YawebmailVersion getProgramVersion() {
  
    return programVersion;
  }

  /**
   * @param programVersion the programVersion to set
   */
  public static void setProgramVersion(YawebmailVersion programVersion) {

    Configuration.programVersion = programVersion;
  }

  /**
   * @return the urlCurrentVersion
   */
  public static String getUrlCurrentVersion() {
  
    return urlCurrentVersion;
  }

  /**
   * @param urlCurrentVersion the urlCurrentVersion to set
   */
  public static void setUrlCurrentVersion(String urlCurrentVersion) {
  
    Configuration.urlCurrentVersion = urlCurrentVersion;
  }

  /**
   * @return the maxLenSubjectOnMailslisting
   */
  public static int getMaxLenSubjectOnMailslisting() {
  
    return maxLenSubjectOnMailslisting;
  }

  /**
   * @param maxLenSubjectOnMailslisting the maxLenSubjectOnMailslisting to set
   */
  public static void setMaxLenSubjectOnMailslisting(
      int maxLenSubjectOnMailslisting) {
  
    Configuration.maxLenSubjectOnMailslisting = maxLenSubjectOnMailslisting;
  }

  /**
   * @return the portnumberPop3
   */
  public static int getPortnumberPop3() {
  
    return portnumberPop3;
  }

  /**
   * @param portnumberPop3 the portnumberPop3 to set
   */
  public static void setPortnumberPop3(int portnumberPop3) {
  
    Configuration.portnumberPop3 = portnumberPop3;
  }

  /**
   * @return the portnumberImap
   */
  public static int getPortnumberImap() {
  
    return portnumberImap;
  }

  /**
   * @param portnumberImap the portnumberImap to set
   */
  public static void setPortnumberImap(int portnumberImap) {
  
    Configuration.portnumberImap = portnumberImap;
  }

  /**
   * @return the portnumberPop3s
   */
  public static int getPortnumberPop3s() {
  
    return portnumberPop3s;
  }

  /**
   * @param portnumberPop3s the portnumberPop3s to set
   */
  public static void setPortnumberPop3s(int portnumberPop3s) {
  
    Configuration.portnumberPop3s = portnumberPop3s;
  }

  /**
   * @return the portnumberImaps
   */
  public static int getPortnumberImaps() {
  
    return portnumberImaps;
  }

  /**
   * @param portnumberImaps the portnumberImaps to set
   */
  public static void setPortnumberImaps(int portnumberImaps) {
  
    Configuration.portnumberImaps = portnumberImaps;
  }

  /**
   * @return the preselectionMailboxHost
   */
  public static String getPreselectionMailboxHost() {
  
    return preselectionMailboxHost;
  }

  /**
   * @param preselectionMailboxHost the preselectionMailboxHost to set
   */
  public static void setPreselectionMailboxHost(String preselectionMailboxHost) {
  
    Configuration.preselectionMailboxHost = preselectionMailboxHost;
  }

  /**
   * @return the forcePreselectedMailboxHost
   */
  public static boolean isForcePreselectedMailboxHost() {
  
    return forcePreselectedMailboxHost;
  }

  /**
   * @param forcePreselectedMailboxHost the forcePreselectedMailboxHost to set
   */
  public static void setForcePreselectedMailboxHost(
      boolean forcePreselectedMailboxHost) {
  
    Configuration.forcePreselectedMailboxHost = forcePreselectedMailboxHost;
  }

  /**
   * @return the preselectionMailboxProtocol
   */
  public static String getPreselectionMailboxProtocol() {
  
    return preselectionMailboxProtocol;
  }

  /**
   * @param preselectionMailboxProtocol the preselectionMailboxProtocol to set
   */
  public static void setPreselectionMailboxProtocol(
      String preselectionMailboxProtocol) {
  
    Configuration.preselectionMailboxProtocol = preselectionMailboxProtocol;
  }

  /**
   * @return the forcePreselectedMailboxProtocol
   */
  public static boolean isForcePreselectedMailboxProtocol() {
  
    return forcePreselectedMailboxProtocol;
  }

  /**
   * @param forcePreselectedMailboxProtocol the forcePreselectedMailboxProtocol to set
   */
  public static void setForcePreselectedMailboxProtocol(
      boolean forcePreselectedMailboxProtocol) {
  
    Configuration.forcePreselectedMailboxProtocol = forcePreselectedMailboxProtocol;
  }

  /**
   * @return the smtpHostChoice
   */
  public static SmtpHostChoiceEnum getSmtpHostChoice() {
  
    return smtpHostChoice;
  }

  /**
   * @param smtpHostChoice the smtpHostChoice to set
   */
  public static void setSmtpHostChoice(SmtpHostChoiceEnum smtpHostChoice) {
  
    Configuration.smtpHostChoice = smtpHostChoice;
  }

  /**
   * @return the forcedSmtpHostPort
   */
  public static String getForcedSmtpHostPort() {
  
    return forcedSmtpHostPort;
  }

  /**
   * @param forcedSmtpHostPort the forcedSmtpHostPort to set
   */
  public static void setForcedSmtpHostPort(String forcedSmtpHostPort) {
  
    Configuration.forcedSmtpHostPort = forcedSmtpHostPort;
  }

  /**
   * @return the amountOfMessagesPerPage
   */
  public static int getAmountOfMessagesPerPage() {
  
    return amountOfMessagesPerPage;
  }

  /**
   * @param amountOfMessagesPerPage the amountOfMessagesPerPage to set
   */
  public static void setAmountOfMessagesPerPage(int amountOfMessagesPerPage) {
  
    Configuration.amountOfMessagesPerPage = amountOfMessagesPerPage;
  }

  /**
   * @return the allMessagesOnOnePage
   */
  public static boolean isAllMessagesOnOnePage() {
  
    return allMessagesOnOnePage;
  }

  /**
   * @param allMessagesOnOnePage the allMessagesOnOnePage to set
   */
  public static void setAllMessagesOnOnePage(boolean allMessagesOnOnePage) {
  
    Configuration.allMessagesOnOnePage = allMessagesOnOnePage;
  }

}
