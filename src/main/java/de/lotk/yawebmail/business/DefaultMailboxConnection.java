/*
 * @(#)MailboxConnection.java 1.00 2005/02/05
 *
 * Copyright (c) 2005, Stephan Sann
 *
 * 05.02.2005 ssann        Vers. 1.0     created
 */
package de.lotk.yawebmail.business;

import java.security.GeneralSecurityException;
import java.util.Properties;
import java.util.TreeSet;

import javax.mail.AuthenticationFailedException;
import javax.mail.FetchProfile;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;
import javax.net.ssl.SSLHandshakeException;
import javax.net.ssl.TrustManager;

import com.sun.mail.util.MailSSLSocketFactory;

import de.lotk.yawebmail.application.Constants;
import de.lotk.yawebmail.bean.LoginDataBean;
import de.lotk.yawebmail.bean.RetrieveMessagesResultBean;
import de.lotk.yawebmail.enumerations.MailboxProtocolEnum;
import de.lotk.yawebmail.exceptions.AccessDeniedException;
import de.lotk.yawebmail.exceptions.ConnectionEstablishException;
import de.lotk.yawebmail.exceptions.LogoutException;
import de.lotk.yawebmail.exceptions.MailboxFolderException;
import de.lotk.yawebmail.exceptions.MessageDeletionException;
import de.lotk.yawebmail.exceptions.MessageMovementException;
import de.lotk.yawebmail.exceptions.MessageRetrieveException;
import de.lotk.yawebmail.exceptions.YawebmailCertificateException;
import de.lotk.yawebmail.util.JavamailUtils;

/**
 * Default-Implementierung des Interfaces MailboxConnection.
 * 
 * @author Stephan Sann
 * @version 1.0
 */
public class DefaultMailboxConnection implements MailboxConnection {

  // ---------------------------------------------------------------- Konstanten

  /** serialVersionUID */
  private static final long serialVersionUID = -8364316467231432686L;


  // --------------------------------------------------------- Instanz-Variablen

  /** Speichert die Login-Daten */
  private LoginDataBean loginData = null;

  /** Speichert einen JavaMail-Store */
  private Store store = null;

  /** Speichert einen JavaMail-Folder */
  private Folder folder = null;

  /** Speichert den FolderSeparator */
  private Character separator = null;

  /** Speichert ein TreeSet mit allen Foldern des Accounts */
  private TreeSet allFolders = null;

  /** Holds a TrustManager for secure connections */
  private YawebmailTrustManager trustManager = null;


  // --------------------------------------------------------------- Konstruktor

  /**
   * Initialisiert eine DefaultMailboxConnection-Instanz mit Login-Daten.
   * 
   * @param   loginData     Die Login-Daten
   * @param   trustManager  TrustManager to use for secure connections
   */
  public DefaultMailboxConnection(LoginDataBean loginData, YawebmailTrustManager
          trustManager) {

    this.loginData = loginData;
    this.trustManager = trustManager;
  }


  // ---------------------------------------------------------- private Methoden

  /**
   * Erstellt einen javax.mail.Store gemaess Anforderungen und schreibt sie in
   * die Instanz-Variable.
   */
  private void createStore() {

    try {

      MailboxProtocolEnum curMbPrtcl = this.loginData.getMailboxProtocol();

      // Get a Properties and Session object
      Properties props = JavamailUtils.getProperties();

      // Trustmanager needed?
      if(curMbPrtcl.isUseOfSsl()) {

        MailSSLSocketFactory socketFactory = new MailSSLSocketFactory();
        socketFactory.setTrustManagers(new TrustManager[]
                { this.trustManager });

        props.put(("mail." + curMbPrtcl.getProtocolId() + ".ssl.socketFactory"),
                socketFactory);
      }

      Session session = Session.getInstance(props, null);
      session.setDebug(false);

      this.store = session.getStore(curMbPrtcl.getProtocolId());
    }
    catch(NoSuchProviderException e) {

      throw(new RuntimeException(("Unknown Protocol: " +
              this.loginData.getMailboxProtocol()), e));
    }
    catch(GeneralSecurityException gse) {

      throw(new RuntimeException(("Security-problem: " + gse.getMessage()),
              gse));
    }
  }

  /**
   * Setzt den in der Instanzvariable folder gespeicherten Folder.
   * 
   * @param   mode        Mode, in dem der Folder geoeffnet werden soll.
   */
  private void openFolder(int mode) throws MailboxFolderException {

    // Versuchen den Folder zu oeffnen
    try {

      this.folder.open(mode);
    }
    catch(MessagingException e) {

      throw(new MailboxFolderException(("Probleme beim Oeffnen des Folders " +
              this.folder.getName()), e, this.folder.getName()));
    }
  }

  /**
   * Fuellt das private TreeSet mit allen Foldern des Accounts (Connection muss
   * fuer diese Aktion geoeffnet sein).
   */
  private void updateAllFoldersTreeSet() throws MessagingException {

    Folder[] allFoldersArray = this.store.getDefaultFolder().list("*");
    TreeSet<Folder> allFoldersTreeSet =
            new TreeSet<Folder>(new FolderByFullNameComparator());

    for(int ii = 0; ii < allFoldersArray.length; ii++) {

      allFoldersTreeSet.add(allFoldersArray[ii]);
    }

    this.allFolders = allFoldersTreeSet;
  }

  /**
   * Assembles a RetrieveMessagesResultBean.
   * 
   * @param   messages             Messages to set
   * @param   overallMessageCount  Overall message count to set.
   * @return  <code>RetrieveMessagesResultBean</code>-object.
   */
  private RetrieveMessagesResultBean assembleRetrieveMessagesResult(Message[]
          messages, int overallMessageCount) {

    RetrieveMessagesResultBean result = new RetrieveMessagesResultBean();
    result.setMessages(messages);
    result.setOverallMessageCount(overallMessageCount);

    return(result);
  }


  // ----------------------------------------------------- oeffentliche Methoden

  /* (non-Javadoc)
   * @see de.lotk.yawebmail.business.MailboxConnection#setLoginData(de.lotk.yawebmail.bean.LoginDataBean)
   */
  public void setLoginData(LoginDataBean loginData) {

    this.loginData = loginData;
  }

  /* (non-Javadoc)
   * @see de.lotk.yawebmail.business.MailboxConnection#login()
   */
  public void login() throws ConnectionEstablishException,
      AccessDeniedException, YawebmailCertificateException {

    // Ggf. Store erstellen
    if(this.store == null) {

      this.createStore();
    }

    // Perform login
    try {

      this.store.connect(this.loginData.getMailboxHost(),
              this.loginData.getMailboxPort(), this.loginData.getMailboxUser(),
              this.loginData.getMailboxPassword());

      // ggf. Separator setzen
      if(this.separator == null) {

        this.separator =
                Character.valueOf(this.store.getDefaultFolder().getSeparator());
      }

      // ggf. Liste mit allen Foldern des Accounts fuellen
      if(this.allFolders == null) {

        this.updateAllFoldersTreeSet();
      }
    }
    catch(AuthenticationFailedException afe) {

      throw(new AccessDeniedException("Autorisierung gescheitert.", afe));
    }
    catch(MessagingException me) {

      Exception nextException = me.getNextException();

      if((nextException != null) &&
              (nextException instanceof SSLHandshakeException)) {

        Throwable cause = ((SSLHandshakeException)nextException).getCause();

        if((cause != null) &&
                (cause instanceof YawebmailCertificateException)) {
            
          throw((YawebmailCertificateException)cause);
        }
      }

      throw(new ConnectionEstablishException("Verbindung gescheitert.", me,
              this.loginData.getMailboxHost(),
              this.loginData.getMailboxPort()));
    }
  }

  /* (non-Javadoc)
   * @see de.lotk.yawebmail.business.MailboxConnection#login(java.lang.String)
   */
  public void login(String folderName) throws ConnectionEstablishException,
      AccessDeniedException, MailboxFolderException,
      YawebmailCertificateException {

    this.login();
    this.changeFolder(folderName);
  }

  /* (non-Javadoc)
   * @see de.lotk.yawebmail.business.MailboxConnection#logout()
   */
  public void logout() throws LogoutException {

    // Ggf. Folder schliessen
    if((this.folder != null) && this.folder.isOpen()) {

      try {

        this.folder.close(true);
      }
      catch(MessagingException me) {

        throw(new LogoutException("Problem beim Schliessen des Folders.", me));
      }

      this.folder = null;
    }

    // Ggf. Store schliessen
    if(this.store != null) {

      try {

        this.store.close();
      }
      catch(MessagingException me) {

        throw(new LogoutException("Problem beim Schliessen des Stores.", me));
      }

      this.store = null;
    }
  }

  /* (non-Javadoc)
   * @see de.lotk.yawebmail.business.MailboxConnection#validateLoginData()
   */
  public void validateLoginData() throws ConnectionEstablishException,
          AccessDeniedException, YawebmailCertificateException {

    this.login();
    
    // Wenn es ein Problem beim Logout gibt, dann ist das nicht so tragisch...
    try {

      this.logout();
    }
    catch(LogoutException e) {

      // empty
    }
  }

  /* (non-Javadoc)
   * @see de.lotk.yawebmail.business.MailboxConnection#getCurrentFolder()
   */
  public Folder getCurrentFolder() throws MailboxFolderException {

    if(this.folder != null) {

      return(this.folder);
    }
    else {

      throw(new MailboxFolderException("Es existiert z.Zt. kein Folder.",
              null));
    }
  }

  /**
   * Setzt die Instanzvariable folder auf den gewuenschten Folder
   * 
   * @param   folderName  Name des gewuenschten Folders.
   */
  public void changeFolder(String folderName) throws MailboxFolderException {

    try {

      if(Constants.LEERSTRING.equals(folderName)) {

        this.folder = this.store.getDefaultFolder();
      }
      else {

        this.folder = this.store.getFolder(folderName);
      }

      if(this.folder == null) {

        throw(new MailboxFolderException(("Invalid folder: " + folderName),
                null));
      }
    }
    catch(MessagingException me) {

      throw(new MailboxFolderException(("Probleme mit Folder: " + folderName),
              me, folderName));
    }
  }

  /* (non-Javadoc)
   * @see de.lotk.yawebmail.business.MailboxConnection#getMessages()
   */
  public RetrieveMessagesResultBean getMessages() throws MailboxFolderException,
          MessageRetrieveException {

    // Open folder
    this.openFolder(Folder.READ_ONLY);

    // Get messages and return them
    try {

      return(this.assembleRetrieveMessagesResult(this.folder.getMessages(),
              this.folder.getMessageCount()));
    }
    catch(MessagingException me) {

      String tempExceptionMessage =
              ("Could not retrieve messages from folder \"" +
                          this.folder.getName() + "\".");
      throw(new MessageRetrieveException(tempExceptionMessage, me));
    }
  }

  /* (non-Javadoc)
   * @see de.lotk.yawebmail.business.MailboxConnection#getMessages(int, int, boolean)
   */
  public RetrieveMessagesResultBean getMessages(int aStartNumber, int
          anEndNumber, boolean adjustParameters) throws MailboxFolderException,
          MessageRetrieveException {

    // Open folder (should happen before getting the message-count)
    this.openFolder(Folder.READ_ONLY);

    try {

      // The given numbers may not be larger than the amount of messages
      int tempMessageCount = this.folder.getMessageCount();

      if((aStartNumber> tempMessageCount) || (anEndNumber > tempMessageCount)) {

        if(adjustParameters) {

          // In case the start-number is bigger than the amount of messages, we
          // return an empty Message-Array.
          if(aStartNumber > tempMessageCount) {

            return(this.assembleRetrieveMessagesResult((new Message[0]),
                    this.folder.getMessageCount()));
          }

          if(anEndNumber > tempMessageCount) {

            anEndNumber = tempMessageCount;
          }
        }
        else {

          String tempExceptionMessage =
                  "Given message-numbers exceed the amount of messages";
          throw(new MessageRetrieveException(tempExceptionMessage));
        }
      }

      // Get messages and return them
      Message[] tempMessages = this.folder.getMessages(aStartNumber, anEndNumber);
      return(this.assembleRetrieveMessagesResult(tempMessages,
              this.folder.getMessageCount()));
    }
    catch(MessagingException me) {

      String tempExceptionMessage =
              ("Could not retrieve messages from folder \"" +
                          this.folder.getName() + "\".");
      throw(new MessageRetrieveException(tempExceptionMessage, me));
    }
  }

  /* (non-Javadoc)
   * @see de.lotk.yawebmail.business.MailboxConnection#getMessage(int)
   */
  public Message getMessage(int messageNumber) throws MailboxFolderException,
      MessageRetrieveException {

    Message message = null;

    // Folder updaten und oeffnen
    this.openFolder(Folder.READ_ONLY);

    // Messages holen
    try {

      message = this.folder.getMessage(messageNumber);
    }
    catch(MessagingException me) {

      throw(new MessageRetrieveException(("Konnte Nachricht " + messageNumber +
              " aus Folder \"" + this.folder.getName() + "\" nicht beziehen."),
              me));
    }

    return(message);
  }

  /* (non-Javadoc)
   * @see de.lotk.yawebmail.business.MailboxConnection#getEnvelopes()
   */
  public RetrieveMessagesResultBean getEnvelopes() throws
          MailboxFolderException, MessageRetrieveException {

    try {

      // Wenn der aktuelle Folder der Default-Folder ist, leeres Array zurueck
      if(this.folder.getParent() == null) {

        return(this.assembleRetrieveMessagesResult(new Message[0], 0));
      }

      // Messages beziehen
      RetrieveMessagesResultBean rmr = this.getMessages();
      Message[] messages = rmr.getMessages();

      // load envelopes (only in case there is at lease one message)
      if(messages.length >= 1) {

        FetchProfile fp = new FetchProfile();
        fp.add(FetchProfile.Item.ENVELOPE);
        this.folder.fetch(messages, fp);
      }

      // Messages zurueckliefern
      return(rmr);
    }
    catch(MessagingException me) {

      throw(new MessageRetrieveException(("Konnte Envelopes aus Folder \"" +
              this.folder.getName() + "\" nicht beziehen."), me));
    }
  }

  /* (non-Javadoc)
   * @see de.lotk.yawebmail.business.MailboxConnection#getEnvelopes(int, int, boolean)
   */
  public RetrieveMessagesResultBean getEnvelopes(int aStartIndex, int
          anEndIndex, boolean adjustParameters) throws MailboxFolderException,
          MessageRetrieveException {

    try {

      // Wenn der aktuelle Folder der Default-Folder ist, leeres Array zurueck
      if(this.folder.getParent() == null) {

        return(this.assembleRetrieveMessagesResult((new Message[0]), 0));
      }

      // Messages beziehen
      RetrieveMessagesResultBean rmr =
              this.getMessages(aStartIndex, anEndIndex, adjustParameters);
      Message[] messages = rmr.getMessages();

      // load envelopes (only in case there is at lease one message)
      if(messages.length >= 1) {

        FetchProfile fp = new FetchProfile();
        fp.add(FetchProfile.Item.ENVELOPE);
        this.folder.fetch(messages, fp);
      }

      // Messages zurueckliefern
      return(rmr);
    }
    catch(MessagingException me) {

      throw(new MessageRetrieveException(("Konnte Envelopes aus Folder \"" +
              this.folder.getName() + "\" nicht beziehen."), me));
    }
  }

  /* (non-Javadoc)
   * @see de.lotk.yawebmail.business.MailboxConnection#setDeletedFlag(int)
   */
  public void setDeletedFlag(int messageNumber) throws MailboxFolderException,
          MessageDeletionException {

    // TODO schoener machen!
    int[] uebergabe = new int[1];
    uebergabe[0] = messageNumber;

    this.setMultipleDeletedFlags(uebergabe);
  }

  /* (non-Javadoc)
   * @see de.lotk.yawebmail.business.MailboxConnection#setMultipleDeletedFlags(int[])
   */
  public void setMultipleDeletedFlags(int[] messageNumbers)
          throws MailboxFolderException, MessageDeletionException {

    // Folder updaten und oeffnen
    this.openFolder(Folder.READ_WRITE);

    // Uebergebene Mails als expunged markieren
    for(int ww = 0; ww < messageNumbers.length; ww++) {

      try {

        this.folder.getMessage(messageNumbers[ww]).setFlag(Flags.Flag.DELETED,
                true);
      }
      catch(MessagingException me) {

        throw(new MessageDeletionException("Probleme beim DELETED-Flags setzen",
                 me));
      }
    }
  }

  /* (non-Javadoc)
   * @see de.lotk.yawebmail.business.MailboxConnection#getFolderSeparator()
   */
  public char getFolderSeparator() throws Exception {

    // Haben wir einen Folder am Start?
    if(this.separator != null) {

      return(this.separator.charValue());
    }

    // Sonst koennen wir nicht helfen.
    else {

      throw(new Exception("FolderSeparator nicht beziehbar."));
    }
  }

  /* (non-Javadoc)
   * @see de.lotk.yawebmail.business.MailboxConnection#getAllFolders()
   */
  public TreeSet getAllFolders() throws Exception {

    // Haben wir die Liste am Start?
    if(this.allFolders != null) {

      return(this.allFolders);
    }

    // Sonst koennen wir nicht helfen.
    else {

      throw(new Exception("Ordnerliste nicht beziehbar."));
    }
  }

  /* (non-Javadoc)
   * @see de.lotk.yawebmail.business.MailboxConnection#createFolder(java.lang.String)
   */
  public void createFolder(String folderName) throws MailboxFolderException {

    try {

      Folder newFolder = this.store.getFolder(folderName);

      if(! newFolder.exists()) {

        if(! newFolder.create(Folder.HOLDS_MESSAGES)) {

          throw(new Exception("Folder konnte nicht erstellt werden."));
        }

        // Liste aller Ordner aktualisieren
        this.updateAllFoldersTreeSet();
      }
    }
    catch(Exception e) {

      e.printStackTrace();
      throw(new MailboxFolderException(e.getMessage(), e, folderName));
    }
  }

  /* (non-Javadoc)
   * @see de.lotk.yawebmail.business.MailboxConnection#deleteFolder(java.lang.String)
   */
  public void deleteFolder(String folderName) throws MailboxFolderException {

    try {

      Folder folderToDelete = this.store.getFolder(folderName);

      if(folderToDelete.exists()) {

        if(! folderToDelete.delete(true)) {

          throw(new Exception("Folder konnte nicht geloescht werden."));
        }

        // Liste aller Ordner aktualisieren
        this.updateAllFoldersTreeSet();
      }
    }
    catch(Exception e) {

      e.printStackTrace();
      throw(new MailboxFolderException(e.getMessage(), e, folderName));
    }
  }

  /* (non-Javadoc)
   * @see de.lotk.yawebmail.business.MailboxConnection#moveMessages(int[], java.lang.String)
   */
  public void moveMessages(int[] messageNumbers, String targetFolderName) throws
          MailboxFolderException, MessageMovementException {

    try {

      // Aktuellen Folder oeffnen und Array mit zu verschiebenden Messages bauen
      this.openFolder(Folder.READ_WRITE);
      Message[] messages = new Message[messageNumbers.length];

      for(int ii = 0; ii < messageNumbers.length; ii++) {

        messages[ii] = this.folder.getMessage(messageNumbers[ii]);
      }

      // Messages in den Zielordner kopieren.
      Folder targetFolderObj = this.store.getFolder(targetFolderName);
      this.folder.copyMessages(messages, targetFolderObj);

      // Messages in diesem Ordner auf geloescht setzen
      for(int kk = 0; kk < messages.length; kk++) {

        messages[kk].setFlag(Flags.Flag.DELETED, true);
      }
    }
    catch(MessagingException e) {

      throw(new MessageMovementException("Problem beim Verschieben.", e));
    }
  }

  /* (non-Javadoc)
   * @see de.lotk.yawebmail.Lifecycle#destroy()
   */
  public void destroy() {

    // Wenn es ein Problem beim Logout gibt, dann ist das nicht so tragisch...
    try {

      this.logout();
    }
    catch(LogoutException e) {

      // empty
    }

    this.loginData = null;
  }

}
