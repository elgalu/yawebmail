/*
 * @(#)Constants.java 1.00 2006/02/17
 *
 * Copyright (c) 2006, Stephan Sann
 *
 * 17.02.2006 ssann        Vers. 1.0     created
 */


package de.lotk.yawebmail.application;

/**
 * Konstanten fuer die Applikation
 * 
 * @author Stephan Sann
 * @version 1.0
 */
public final class Constants {

  // ---------------------------------------------------------------- Konstanten

  // Allgemeines

  /** Welches Encoding soll in den Mails verwendet werden? */
  public static final String MESSAGE_CHAR_ENCODING = "UTF-8";

  /** Name der Properties-Datei fuer Application-Properies */
  public static final String PROPERTIES_FILE_APPLICATION =
          "de/lotk/yawebmail/properties/application.properties";

  /** Name der Properties-Datei fuer Logon-Properies */
  public static final String PROPERTIES_FILE_LOGON =
          "de/lotk/yawebmail/properties/logon.properties";

  /** Name der Properties-Datei fuer SMTP-Properies */
  public static final String PROPERTIES_FILE_SMTP =
          "de/lotk/yawebmail/properties/smtp.properties";

  /** Ein Leerstring */
  public static final String LEERSTRING = "";

  // Controller

  /** (managed-bean) Name of the CreateMailController */
  public static final String NAME_MBEAN_CREATEMAILCONTROLLER =
          "createMailController";

  // Managed beans

  /** Name der managed-bean LoginDataBean */
  public static final String NAME_MBEAN_SESSIONCONTAINER =
          "sessionContainerBean";

  /** Name der managed-bean LoginDataBean */
  public static final String NAME_MBEAN_LOGINDATA = "loginDataBean";

  /** Name of the managed-bean CheckCertsBean */
  public static final String NAME_MBEAN_CHECKCERTS = "checkCertsBean";

  /** Name der managed-bean FolderWrapperBean */
  public static final String NAME_MBEAN_FOLDERWRAPPER = "folderWrapperBean";

  /** Name der managed-bean FolderManagementBean */
  public static final String NAME_MBEAN_FOLDERMANAGEMENT =
          "folderManagementBean";

  /** Name der managed-bean DisplayMessageBean */
  public static final String NAME_MBEAN_DISPLAYMESSAGE = "displayMessageBean";

  /** Name der managed-bean SmtpConnectionBean */
  public static final String NAME_MBEAN_SMTPCONNECTION = "smtpConnectionBean";

  /** Name der managed-bean MailBasisBean */
  public static final String NAME_MBEAN_MAILBASIS = "mailBasisBean";

  // View-IDs

  /** View-ID der Index-View */
  public static final String VIEW_ID_INDEX = "/index.jsp";

  /** View-ID der Logon-View */
  public static final String VIEW_ID_LOGON = "/logon.jsp";

  /** View-ID der Mails-Listing-View */
  public static final String VIEW_ID_MAILSLISTING = "/mailsListing.jsp";

  /** View-ID der Display-Mail-View */
  public static final String VIEW_ID_DISPLAY_MAIL = "/displayMail.jsp";

  /** View-ID of the check-certificate(-chain)-view */
  public static final String VIEW_ID_CHECK_CERTS = "/checkCerts.jsp";

  /** View-ID der Technischer-Fehler-View */
  public static final String VIEW_ID_TECH_ERROR = "/techError.jsp";

  // Outcome-Strings

  /** Outcome-String zum Weiterleiten auf die "Technischer Fehler"-Seite */
  public static final String OUTCOME_TECH_ERROR = "techError";

  /** Outcome-String zum Weiterleiten auf die Logon-Seite */
  public static final String OUTCOME_LOGON = "logon";

  /** Outcome-String zum Weiterleiten auf die Mails-Listing-Seite */
  public static final String OUTCOME_MAILS_LISTING = "mailsListing";

  /** Outcome-String zum Weiterleiten auf die Display-Mail-Seite */
  public static final String OUTCOME_DISPLAY_MAIL = "displayMail";

  /** Outcome-String zum Weiterleiten auf die Create-Mail-Seite */
  public static final String OUTCOME_CREATE_MAIL = "createMail";

  /** Outcome-String to forward to the "check certs"-page */
  public static final String OUTCOME_CHECK_CERTS = "checkCerts";

  // Client-IDs

  /** Client-ID der Mailbox-Host-Komponente */
  public static final String CLIENT_ID_MAILBOXHOST = "logonForm:mailboxHost";

  /** Client-ID der Mailbox-User-Komponente */
  public static final String CLIENT_ID_MAILBOXUSER = "logonForm:mailboxUser";

  /** Client-ID der Mailbox-Passwort-Komponente */
  public static final String CLIENT_ID_MAILBOXPASSWORD =
          "logonForm:mailboxPassword";

  /** Client-ID der New-Subfolder-Komponente */
  public static final String CLIENT_ID_NEW_SUBFOLDER =
          "mailsListingForm:newSubfolder";

  /** Client-ID der Delete-Subfolder-Komponente */
  public static final String CLIENT_ID_SUBFOLDER_TO_DELETE =
          "mailsListingForm:subfolderToDelete";

  /** Client-ID der SMTP-Host-Komponente */
  public static final String CLIENT_ID_SMTPHOST = "createMailForm:smtpHost";

  /** Client-ID der Mail-From-Komponente */
  public static final String CLIENT_ID_MAILFROM = "createMailForm:from";

  /** Client-ID der RCPT-TO-Komponente */
  public static final String CLIENT_ID_RCPT_TO = "createMailForm:rcptTo";

  /** Client-ID der RCPT-CC-Komponente */
  public static final String CLIENT_ID_RCPT_CC = "createMailForm:rcptCc";

  /** Client-ID der RCPT-BCC-Komponente */
  public static final String CLIENT_ID_RCPT_BCC = "createMailForm:rcptBcc";

  // Property-Keys
  public static final String PROPERTY_KEY_EXCEPTION_NO_CONNECTION =
          "exception_no_connection";

  public static final String PROPERTY_KEY_EXCEPTION_ACCESS_DENIED =
          "exception_access_denied";

  public static final String PROPERTY_KEY_EXCEPTION_MAILBOXFOLDER =
          "exception_mailboxfolder";

  public static final String PROPERTY_KEY_EXCEPTION_MESSAGE_RETRIEVE =
          "message_retrieve";

  public static final String PROPERTY_KEY_EXCEPTION_INSTANTIATION =
          "exception_instantiation";

  public static final String PROPERTY_KEY_EXCEPTION_ADDRESS =
          "exception_address";

  public static final String PROPERTY_KEY_EXCEPTION_SESSION_EXPIRED =
          "exception_session_expired";

  public static final String PROPERTY_KEY_EXCEPTION_MAIL_COULD_NOT_BE_SEND =
          "exception_mail_could_not_be_send";

  public static final String
          PROPERTY_KEY_EXCEPTION_CONNECTION_COULD_NOT_BE_ESTABLISHED =
                  "exception_connection_could_not_be_established";

  public static final String PROPERTY_KEY_EXCEPTION_TECHNICAL_ERROR =
          "exception_technical_error";

  public static final String PROPERTY_KEY_LANGUAGE_INTERNATIONALNAME =
          "resourceFile_language_internationalName";

  public static final String PROPERTY_KEY_LANGUAGE_LOCALNAME =
          "resourceFile_language_localName";

  // Request-Parameter

  /** Request-Parameter zur Uebergabe einer Part-Number */
  public static final String REQ_PARAM_PART_NUMBER = "partNumber";

  /** Request-Param to pass a new offset */
  public static final String REQ_PARAM_NEW_OFFSET = "newOffset";

}
