/*
 * @(#)SessionContainerBean.java 1.00 2006/02/17
 *
 * Copyright (c) 2006, Stephan Sann
 *
 * 17.02.2006 ssann        Vers. 1.0     created
 */


package de.lotk.yawebmail.bean;

import java.io.Serializable;
import java.util.Locale;

import javax.faces.context.FacesContext;
import javax.mail.Message;

import de.lotk.yawebmail.application.Configuration;
import de.lotk.yawebmail.application.Lifecycle;
import de.lotk.yawebmail.business.MailboxConnection;
import de.lotk.yawebmail.business.MessageByDateComparator;
import de.lotk.yawebmail.business.ReversibleComparator;
import de.lotk.yawebmail.business.YawebmailTrustManager;

/**
 * Container fuer Properties, die in der Session gespeichert werden sollen
 * 
 * @author Stephan Sann
 * @version 1.0
 */
public class SessionContainerBean implements Lifecycle, Serializable {

  // ---------------------------------------------------------------- Konstanten

  /** serialVersionUID */
  private static final long serialVersionUID = -2260053681939669329L;


  // --------------------------------------------------------- Instanz-Variablen

  /** Hier merken wir uns unsere MailboxConnection */
  private MailboxConnection mailboxConnection = null;

  /** Holds our Mailbox-TrustManager */
  private YawebmailTrustManager mailboxTrustManager =
          new YawebmailTrustManager();

  /** Holds our SMTP-TrustManager */
  private YawebmailTrustManager smtpTrustManager = new YawebmailTrustManager();

  /** Welcher Mailbox-Ordner ist gerade aktuell? */
  private String currentMailboxFolder = null;

  /** Sollen die Envelopes erneuert (nach neuer Mail gesehen) werden? */
  private boolean renewEnvelopes = true;

  /** Soll die Sortierreihenfolge erneuert werden? */
  private boolean renewSortorder = true;

  /** Comparator fuer die Sortierung der Mails (default by Date, umgekehrt) */
  private ReversibleComparator<Message> sortierComparator =
          new MessageByDateComparator<Message>(true);

  /** Should all messages be displayed on one page? */
  private boolean allMessagesOnOnePage = Configuration.isAllMessagesOnOnePage();

  /** Amount of Messages per page (in case (! allMessagesOnOnePage)) */
  private int amountOfMessagesPerPage =
          Configuration.getAmountOfMessagesPerPage();

  /** Current offset (in case (! allMessagesOnOnePage)) */
  private int currentOffset = 0;

  // TODO Die kann bestimmt noch eingespart werden...
  /** Welche Mail wird aktuell betrachtet? */
  private int messageNumberCurrentDisplayMail = 0;


  // --------------------------------------------------------- Getter und Setter

  /**
   * @return Returns the mailboxConnection.
   */
  public MailboxConnection getMailboxConnection() {

    return mailboxConnection;
  }

  /**
   * @param mailboxConnection
   *          The mailboxConnection to set.
   */
  public void setMailboxConnection(MailboxConnection mailboxConnection) {

    this.mailboxConnection = mailboxConnection;
  }

  /**
   * @return Returns the currentMailboxFolder.
   */
  public String getCurrentMailboxFolder() {
  
    return currentMailboxFolder;
  }
  
  /**
   * @param currentMailboxFolder The currentMailboxFolder to set.
   */
  public void setCurrentMailboxFolder(String currentMailboxFolder) {
  
    this.currentMailboxFolder = currentMailboxFolder;
  }

  /**
   * @return Returns the messageNumberCurrentDisplayMail.
   */
  public int getMessageNumberCurrentDisplayMail() {
  
    return messageNumberCurrentDisplayMail;
  }
  
  /**
   * @param messageNumberCurrentDisplayMail The messageNumberCurrentDisplayMail to set.
   */
  public void setMessageNumberCurrentDisplayMail(
      int messageNumberCurrentDisplayMail) {
  
    this.messageNumberCurrentDisplayMail = messageNumberCurrentDisplayMail;
  }

  /**
   * @return Returns the renewEnvelopes.
   */
  public boolean isRenewEnvelopes() {

    return renewEnvelopes;
  }

  /**
   * @param renewEnvelopes The renewEnvelopes to set.
   */
  public void setRenewEnvelopes(boolean renewEnvelopes) {
  
    this.renewEnvelopes = renewEnvelopes;
  }

  /**
   * @return Returns the sortierComparator.
   */
  public ReversibleComparator<Message> getSortierComparator() {
  
    return sortierComparator;
  }

  /**
   * @param sortierComparator The sortierComparator to set.
   */
  public void setSortierComparator(ReversibleComparator<Message>
          sortierComparator) {
  
    this.sortierComparator = sortierComparator;
  }

  /**
   * @return Returns the renewSortorder.
   */
  public boolean isRenewSortorder() {
  
    return renewSortorder;
  }

  /**
   * @param renewSortorder The renewSortorder to set.
   */
  public void setRenewSortorder(boolean renewSortorder) {
  
    this.renewSortorder = renewSortorder;
  }
  
  /**
   * @return the allMessagesOnOnePage
   */
  public boolean isAllMessagesOnOnePage() {
  
    return allMessagesOnOnePage;
  }

  /**
   * @param allMessagesOnOnePage the allMessagesOnOnePage to set
   */
  public void setAllMessagesOnOnePage(boolean allMessagesOnOnePage) {
  
    this.allMessagesOnOnePage = allMessagesOnOnePage;
  }

  /**
   * @return the amountOfMessagesPerPage
   */
  public int getAmountOfMessagesPerPage() {
  
    return amountOfMessagesPerPage;
  }

  /**
   * @param amountOfMessagesPerPage the amountOfMessagesPerPage to set
   */
  public void setAmountOfMessagesPerPage(int amountOfMessagesPerPage) {
  
    this.amountOfMessagesPerPage = amountOfMessagesPerPage;
  }

  /**
   * @return the currentOffset
   */
  public int getCurrentOffset() {
  
    return currentOffset;
  }

  /**
   * @param currentOffset the currentOffset to set
   */
  public void setCurrentOffset(int currentOffset) {
  
    this.currentOffset = currentOffset;
  }

  /**
   * @return the mailboxTrustManager
   */
  public YawebmailTrustManager getMailboxTrustManager() {
  
    return mailboxTrustManager;
  }

  /**
   * @param mailboxTrustManager the mailboxTrustManager to set
   */
  public void setMailboxTrustManager(YawebmailTrustManager mailboxTrustManager) {
  
    this.mailboxTrustManager = mailboxTrustManager;
  }

  /**
   * @return the smtpTrustManager
   */
  public YawebmailTrustManager getSmtpTrustManager() {
  
    return smtpTrustManager;
  }

  /**
   * @param smtpTrustManager the smtpTrustManager to set
   */
  public void setSmtpTrustManager(YawebmailTrustManager smtpTrustManager) {
  
    this.smtpTrustManager = smtpTrustManager;
  }


  // ----------------------------------------------------- oeffentliche Methoden

  /**
   * @return Returns the locale.
   */
  public Locale getLocale() {
  
    FacesContext facesContext = FacesContext.getCurrentInstance();

    return(facesContext.getViewRoot().getLocale());
  }

  /**
   * @param locale The locale to set.
   */
  public void setLocale(Locale locale) {
  
    FacesContext facesContext = FacesContext.getCurrentInstance();

    facesContext.getViewRoot().setLocale(locale);
  }

  /**
   * Setzt den Session-Container auf seine Defaultwerte zurueck
   */
  public void reset() {

    this.mailboxConnection = null;
    this.currentMailboxFolder = null;
    this.renewEnvelopes = true;
    this.messageNumberCurrentDisplayMail = 0;
    this.sortierComparator = new MessageByDateComparator<Message>(true);
    this.renewSortorder = true;
    this.allMessagesOnOnePage = Configuration.isAllMessagesOnOnePage();
    this.amountOfMessagesPerPage = Configuration.getAmountOfMessagesPerPage();
    this.currentOffset = 0;
    this.mailboxTrustManager = new YawebmailTrustManager();
    this.smtpTrustManager = new YawebmailTrustManager();
  }

  /*
   * (non-Javadoc)
   * 
   * @see de.lotk.webftp.Lifecycle#destroy()
   */
  public void destroy() {

    this.reset();
  }

}
