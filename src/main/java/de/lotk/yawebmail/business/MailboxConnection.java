/*
 * @(#)MailboxConnection.java 1.00 2005/02/05
 *
 * Copyright (c) 2005, Stephan Sann
 *
 * 05.02.2005 ssann        Vers. 1.0     created
 */
package de.lotk.yawebmail.business;

import java.io.Serializable;
import java.util.TreeSet;

import javax.mail.Folder;
import javax.mail.Message;

import de.lotk.yawebmail.application.Lifecycle;
import de.lotk.yawebmail.bean.LoginDataBean;
import de.lotk.yawebmail.bean.RetrieveMessagesResultBean;
import de.lotk.yawebmail.exceptions.AccessDeniedException;
import de.lotk.yawebmail.exceptions.ConnectionEstablishException;
import de.lotk.yawebmail.exceptions.LogoutException;
import de.lotk.yawebmail.exceptions.MailboxFolderException;
import de.lotk.yawebmail.exceptions.MessageDeletionException;
import de.lotk.yawebmail.exceptions.MessageMovementException;
import de.lotk.yawebmail.exceptions.MessageRetrieveException;
import de.lotk.yawebmail.exceptions.YawebmailCertificateException;

/**
 * Stellt Methoden zur Abfrage einer Mailbox bereit.
 * 
 * @author Stephan Sann
 * @version 1.0
 */
public interface MailboxConnection extends Lifecycle, Serializable {

  // ----------------------------------------------------- oeffentliche Methoden

  /**
   * Setzt die Login-Daten fuer diese Mailbox-Connection.
   */
  public void setLoginData(LoginDataBean loginData);

  /**
   * Fuehrt ein Login am Mailbox-Host aus.
   */
  public void login() throws ConnectionEstablishException,
          AccessDeniedException, YawebmailCertificateException;


  /**
   * Fuehrt ein Login am Mailbox-Host aus und wechselt in den uebergebenen
   * Ordner.
   * 
   * @param     folderName   Names des Ordners, in den gewechselt werden soll.
   */
  public void login(String folderName) throws ConnectionEstablishException,
          AccessDeniedException, MailboxFolderException,
          YawebmailCertificateException;


  /**
   * Fuehrt ein Logout am Mailbox-Host aus.
   */
  public void logout() throws LogoutException;


  /**
   * Testet ob mit den Daten eines LoginDataBean-Objektes ein Login
   * durchgefuehrt werden kann.<br/>
   * Wenn diese Methode keine Exception wirft kann davon ausgegangen werden,
   * dass ein Login moeglich ist.
   */
  public void validateLoginData() throws ConnectionEstablishException,
          AccessDeniedException, YawebmailCertificateException;


  /**
   * Liefert den aktuellen Mailfolder als "javax.mail.Folder"-Objekt.
   * 
   * @return   Aktuelles <code>Folder</code>-Objekt.
   */
  public Folder getCurrentFolder() throws MailboxFolderException;


  /**
   * Wechselt den aktuellen Mailfolder.
   * 
   * @param     folderName  Folder, in den gewechselt werden soll.
   */
  public void changeFolder(String folderName) throws MailboxFolderException;


  /**
   * Retrieves all messages of the current folder.
   * 
   * @return  <code>RetrieveMessagesResultBean</code> containig all messages.
   */
  public RetrieveMessagesResultBean getMessages() throws MailboxFolderException,
          MessageRetrieveException;

  /**
   * Get the Message objects for message numbers ranging from start
   * through end, both start and end inclusive. Note that message 
   * numbers start at 1, not 0.
   * 
   * @param   aStartNumber      The number of the first message
   * @param   anEndNumber       The number of the last message
   * @param   adjustParameters  This enables the method to adjust the start-
   *                            and/or teh end-number if they are out of range
   *                            of the messages in the current folder.
   * @return  <code>RetrieveMessagesResultBean</code> containig all messages.
   */
  public RetrieveMessagesResultBean getMessages(int aStartNumber, int
          anEndNumber, boolean adjustParameters) throws MailboxFolderException,
          MessageRetrieveException;

  /**
   * Liefert eine Nachricht aus dem aktuell gewaehlten Folder.
   * 
   * @return                <code>Message</code> gemaess Parameter.
   */
  public Message getMessage(int messageNumber) throws MailboxFolderException,
          MessageRetrieveException;


  /**
   * Retrieves the envelopes of all messages in the current folder.
   * 
   * @return  <code>RetrieveMessagesResultBean</code> containig all envelopes.
   */
  public RetrieveMessagesResultBean getEnvelopes() throws
          MailboxFolderException, MessageRetrieveException;

  /**
   * Get the Message objects filled with the Envelope-data for teh message
   * numbers ranging from start through end, both start and end inclusive. Note
   * that message numbers start at 1, not 0.
   * 
   * @param   aStartNumber      The number of the first message
   * @param   anEndNumber       The number of the last message
   * @param   adjustParameters  This enables the method to adjust the start-
   *                            and/or teh end-number if they are out of range
   *                            of the messages in the current folder.
   * @return  <code>RetrieveMessagesResultBean</code> containig all envelopes.
   */
  public RetrieveMessagesResultBean getEnvelopes(int aStartNumber, int
          anEndNumber, boolean adjustParameters) throws MailboxFolderException,
          MessageRetrieveException;

  /**
   * Markiert eine Mail im aktuell gewaehlten Folder als geloescht.
   * 
   * @param     messageNumber  Die zu loeschende Message.
   */
  public void setDeletedFlag(int messageNumber) throws MailboxFolderException,
          MessageDeletionException;

  /**
   * Markiert mehrere Mails im aktuell gewaehlten Folder als geloescht.
   * 
   * @param     messageNumbers  Die zu loeschenden Messages.
   */
  public void setMultipleDeletedFlags(int[] messageNumbers) throws
          MailboxFolderException, MessageDeletionException;

  /**
   * Liefert den Delimiter, der Folder von Unter-Foldern trennt. Darf erst
   * aufgerufen werden, wenn zumindest einmal ein Login erfolgt ist.
   */
  public char getFolderSeparator() throws Exception;

  /**
   * Liefert ein TreeSet mit allen Foldern des Accounts.
   * 
   * @return  <code>TreeSet</code>-Objekt mit Foldern des Accounts.
   */
  public TreeSet getAllFolders() throws Exception;

  /**
   * Erstellt einen neuen Ordner.
   * 
   * @param   folderName  Name des zu erstellenden Folders.
   */
  public void createFolder(String folderName) throws MailboxFolderException;

  /**
   * Loescht einen bestehenden Ordner.
   * 
   * @param   folderName  Name des zu loeschenden Folders.
   */
  public void deleteFolder(String folderName) throws MailboxFolderException;

  /**
   * Verschiebt Messages in einen anderen Ordner.
   * 
   * @param   messageNumbers    Die zu verschiebenden Messages.
   * @param   targetFolderName  Name des Ziel-Ordners
   */
  public void moveMessages(int[] messageNumbers, String targetFolderName) throws
          MailboxFolderException, MessageMovementException;

}
