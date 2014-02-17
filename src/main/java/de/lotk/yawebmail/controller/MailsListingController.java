/*
 * @(#)MailsListingController.java 1.00 2006/03/03
 *
 * Copyright (c) 2006, Stephan Sann
 *
 * 03.03.2006 ssann        Vers. 1.0     created
 */


package de.lotk.yawebmail.controller;

import java.util.Map;

import javax.faces.component.UIData;
import javax.faces.context.FacesContext;
import javax.mail.Folder;
import javax.mail.Message;

import de.lotk.yawebmail.application.Constants;
import de.lotk.yawebmail.bean.DisplayMessageBean;
import de.lotk.yawebmail.bean.FolderManagementBean;
import de.lotk.yawebmail.bean.FolderWrapperBean;
import de.lotk.yawebmail.bean.SessionContainerBean;
import de.lotk.yawebmail.business.MailboxConnection;
import de.lotk.yawebmail.business.MessageByDateComparator;
import de.lotk.yawebmail.business.MessageBySenderComparator;
import de.lotk.yawebmail.business.MessageBySpamlevelComparator;
import de.lotk.yawebmail.business.MessageBySubjectComparator;
import de.lotk.yawebmail.business.ReversibleComparator;
import de.lotk.yawebmail.exceptions.AccessDeniedException;
import de.lotk.yawebmail.exceptions.ConnectionEstablishException;
import de.lotk.yawebmail.exceptions.LogoutException;
import de.lotk.yawebmail.exceptions.MailboxFolderException;
import de.lotk.yawebmail.exceptions.MessageDeletionException;
import de.lotk.yawebmail.exceptions.MessageMovementException;
import de.lotk.yawebmail.util.faces.ExceptionConverter;

/**
 * Controller fuer das Mails-Listing 
 * 
 * @author Stephan Sann
 * @version 1.0
 */
public class MailsListingController extends BaseMailboxActionController {

  // ----------------------------------------------------------------- Constants

  /** Used to sort messages by sender */
  public static final MessageByDateComparator<Message>
          MESSAGE_BY_DATE_COMPARATOR = new MessageByDateComparator<Message>();

  /** Used to sort messages by sender */
  public static final MessageBySenderComparator<Message>
          MESSAGE_BY_SENDER_COMPARATOR =
                  new MessageBySenderComparator<Message>();

  /** Used to sort messages by spam-level */
  public static final MessageBySpamlevelComparator<Message>
          MESSAGE_BY_SPAMLEVEL_COMPARATOR =
                  new MessageBySpamlevelComparator<Message>();

  /** Used to sort messages by sender */
  public static final MessageBySubjectComparator<Message>
          MESSAGE_BY_SUBJECT_COMPARATOR =
                  new MessageBySubjectComparator<Message>();


  // --------------------------------------------------------- Instanz-Variablen

  /** Handle auf die Folder-Tabelle */
  private UIData folderTable = null;

  /** Handle auf die Nachrichten-Tabelle */
  private UIData messageTable = null;


  // --------------------------------------------------------- Getter und Setter

  /**
   * @return the folderTable
   */
  public UIData getFolderTable() {
  
    return folderTable;
  }

  /**
   * @param folderTable the folderTable to set
   */
  public void setFolderTable(UIData folderTable) {
  
    this.folderTable = folderTable;
  }

  /**
   * @return the messageTable
   */
  public UIData getMessageTable() {
  
    return messageTable;
  }

  /**
   * @param messageTable the messageTable to set
   */
  public void setMessageTable(UIData messageTable) {
  
    this.messageTable = messageTable;
  }


  // ---------------------------------------------------------- private Methoden

  /**
   * Aendert die Sortierreihenfolge auf Sortierung nach uebergebenen Comparator-
   * Typ. Wenn bereits nach uebergebenen Comparator-Typ sortiert wurde, wird die
   * bisherige Sortierung invertiert.
   * 
   * @return          <code>String</code>-Objekt mit Info, wohin navigiert wird. 
   */
  private String sortByX(ReversibleComparator<Message> aComparator) {

    FacesContext facesContext = FacesContext.getCurrentInstance();

    try {

      // SessionContainerBean und aktuellen SortierComparator holen
      SessionContainerBean sessionContainer =
              (SessionContainerBean)this.getManagedBeanByName(Constants.NAME_MBEAN_SESSIONCONTAINER);
      ReversibleComparator<Message> currentComparator =
              sessionContainer.getSortierComparator();

      // Wird bereits nach uebergebenem Comparator-Typ sortiert?
      if(aComparator.getClass() == currentComparator.getClass()) {

        // Sortierreihenfolge umdrehen
        currentComparator.setReverse(! currentComparator.isReverse());
      }

      // Sonst neuen Comparator gemaess Parameter in den SessionContainer setzen
      else {

        sessionContainer.setSortierComparator(aComparator);
      }

      // Neue Sortierung anmelden
      sessionContainer.setRenewSortorder(true);
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
   * Liefert die aktuelle ComparatorKlasse, wenn vom Typ X
   * 
   * @param   sortierComparatorClass  Klasse des Sortier-Comparators
   * @return  Aktuelles <code>ReversibleComparator</code>-Objekt, wenn vom
   *          uebergebenen Typ; sonst <code>null</code>.
   */
  private ReversibleComparator getSortByComparatorIfX(Class
          sortierComparatorClass) {

    try {

      // SessionContainerBean und aktuellen SortierComparator holen
      SessionContainerBean sessionContainer =
              (SessionContainerBean)this.getManagedBeanByName(Constants.NAME_MBEAN_SESSIONCONTAINER);
      ReversibleComparator sortierComparatorInstance =
              sessionContainer.getSortierComparator();

      // Wird nach uebergebenem Comparator-Typ sortiert?
      if(sortierComparatorClass.isInstance(sortierComparatorInstance)) {

        return(sortierComparatorInstance);
      }

      // Sonst ist die Antwort NULL 
      else {

        return(null);
      }
    }
    catch(Exception e) {

      e.printStackTrace();
      throw(new RuntimeException(e.getMessage(), e));
    }
  }

  /**
   * Wird nach X aufsteigend sortiert?
   * 
   * @param   sortierComparatorClass  Klasse des Sortier-Comparators
   * @return  <code>true</code>, wenn aufsteigend sortiert wird;
   *          <code>false</code>, wenn absteigend sortiert wird.
   */
  private boolean isSortUpByX(Class sortierComparatorClass) {

    ReversibleComparator currentComparator =
            this.getSortByComparatorIfX(sortierComparatorClass);

    // Wenn wir nicht NULL zuruck bekommen haben, wird aktuell nach X sortiert.
    if(currentComparator != null) {

      // Nachsehen, ob aktueller Comparator aufsteigend sortiert wird.
      return(! currentComparator.isReverse());
    }

    return false;
  }

  /**
   * Wird nach X absteigend sortiert?
   * 
   * @param   sortierComparatorClass  Klasse des Sortier-Comparators
   * @return  <code>true</code>, wenn absteigend sortiert wird;
   *          <code>false</code>, wenn aufsteigend sortiert wird.
   */
  private boolean isSortDownByX(Class sortierComparatorClass) {

    ReversibleComparator currentComparator =
            this.getSortByComparatorIfX(sortierComparatorClass);

    // Wenn wir nicht NULL zuruck bekommen haben, wird aktuell nach X sortiert.
    if(currentComparator != null) {

      // Nachsehen, ob aktueller Comparator aufsteigend sortiert wird.
      return(currentComparator.isReverse());
    }

    return false;
  }

  /**
   * Wechselt den Folder in der aktuellen Session auf den uebergebenen.
   * 
   * @param   folderPath  Pfad des Folders, in den gewechselt werden soll.
   * @return  <code>String</code>-Objekt mit Info, wohin navigiert wird.
   */
  private String changeFolderToX(String folderPath) {

    FacesContext facesContext = FacesContext.getCurrentInstance();

    // Folder-path has to be set
    if(folderPath != null) {

      try {

        // Get Session-Container
        SessionContainerBean sessionContainer =
                (SessionContainerBean)this.getManagedBeanByName(Constants.NAME_MBEAN_SESSIONCONTAINER);

        // Reset current offset to 0
        sessionContainer.setCurrentOffset(0);

        // Write new folder to the session.
        sessionContainer.setCurrentMailboxFolder(folderPath);

        // All envelopes of the new folder have to be read in
        sessionContainer.setRenewEnvelopes(true);

        // Back to the "mails listing"
        return(Constants.OUTCOME_MAILS_LISTING);
      }
      catch(Exception e) {

        facesContext.addMessage(null,
                ExceptionConverter.getFacesMessage(facesContext, e, true));
        return(Constants.OUTCOME_TECH_ERROR);
      }
    }
    else {

      facesContext.addMessage(null,
              ExceptionConverter.getFacesMessage(facesContext, new
              Exception("Parameter \"folderPath\" nicht uebergeben."), true));
      return(Constants.OUTCOME_TECH_ERROR);
    }
  }

  /**
   * Checks the current offset after an operation that deletes messages from
   * the current folder. If the new amount of messages in the current folder is
   * equal or less the current offset, the current offset has to be decreased.
   * 
   * @param  folderWrapper     Current FolderWrapperBean
   * @param  sessionContainer  Current SessionContainerBean
   * @param  selectedMessages  Messages to be removed.
   */
  private void assureCorrectOffset(FolderWrapperBean folderWrapper,
          SessionContainerBean sessionContainer, int[] selectedMessages) {

    if((sessionContainer.getCurrentOffset() > 0) &&
          ((folderWrapper.getOverallMessageCount() - selectedMessages.length) <=
              sessionContainer.getCurrentOffset())) {

      sessionContainer.setCurrentOffset(sessionContainer.getCurrentOffset() -
              sessionContainer.getAmountOfMessagesPerPage());
    }
  }


  // ----------------------------------------------------- oeffentliche Methoden

  /**
   * Versucht die selektierten Mails zu loeschen
   * 
   * @return          <code>String</code>-Objekt mit Info, wohin navigiert wird. 
   */
  public String deleteSelectedMails() {

    FacesContext facesContext = FacesContext.getCurrentInstance();

    try {

      // FolderWrapperBean holen
      FolderWrapperBean folderWrapper =
              (FolderWrapperBean)this.getManagedBeanByName(Constants.NAME_MBEAN_FOLDERWRAPPER);

      // Message-Numbers der selektierten Display-Messages holen
      int[] messageNumbersOfSelectedDisplayMessages =
              folderWrapper.getMessageNumbersOfSelectedDisplayMessages();

      // Wir tun nur was, wenn mindestens eine Mail selektiert wurde.
      if(messageNumbersOfSelectedDisplayMessages.length >= 1) {

        // MailboxConnection und aktuellen Mailbox-Folder aus der Session holen
        MailboxConnection mailboxConnection = this.getMailboxConnection();
        String currentMailboxFolder = this.getCurrentMailboxFolder();

        // Mails loeschen
        try {

          mailboxConnection.login(currentMailboxFolder);
          mailboxConnection.setMultipleDeletedFlags(messageNumbersOfSelectedDisplayMessages);

          // Nach dem Loeschen sind die gecachten Envelopes nicht mehr aktuell.
          SessionContainerBean sessionContainer =
                  (SessionContainerBean)this.getManagedBeanByName(Constants.NAME_MBEAN_SESSIONCONTAINER);
          sessionContainer.setRenewEnvelopes(true);

          // Ggf. aktuellen Offset anpassen
          assureCorrectOffset(folderWrapper, sessionContainer,
                  messageNumbersOfSelectedDisplayMessages);
        }
        catch(ConnectionEstablishException cee) {

          facesContext.addMessage(Constants.CLIENT_ID_MAILBOXHOST,
                  ExceptionConverter.getFacesMessage(facesContext, cee, false));
          return(Constants.OUTCOME_LOGON);
        }
        catch(AccessDeniedException ade) {

          facesContext.addMessage(Constants.CLIENT_ID_MAILBOXPASSWORD,
                  ExceptionConverter.getFacesMessage(facesContext, ade, false));
          return(Constants.OUTCOME_LOGON);
        }
        catch(MailboxFolderException mfe) {

          facesContext.addMessage(null,
                  ExceptionConverter.getFacesMessage(facesContext, mfe, true));
          return(Constants.OUTCOME_TECH_ERROR);
        }
        catch(MessageDeletionException mde) {

          facesContext.addMessage(null,
                  ExceptionConverter.getFacesMessage(facesContext, mde, true));
          return(Constants.OUTCOME_TECH_ERROR);
        }
        finally {

          try {

            mailboxConnection.logout();
          }
          catch(LogoutException le) {

            facesContext.addMessage(null,
                    ExceptionConverter.getFacesMessage(facesContext, le, true));
            return(Constants.OUTCOME_TECH_ERROR);
          }
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
   * Versucht die selektierten Mails zu verschieben
   * 
   * @return          <code>String</code>-Objekt mit Info, wohin navigiert wird. 
   */
  public String moveSelectedMails() {

    FacesContext facesContext = FacesContext.getCurrentInstance();

    try {

      // FolderWrapperBean und FolderManagementBean holen
      FolderWrapperBean folderWrapper =
              (FolderWrapperBean)this.getManagedBeanByName(Constants.NAME_MBEAN_FOLDERWRAPPER);
      FolderManagementBean folderManagement =
              (FolderManagementBean)this.getManagedBeanByName(Constants.NAME_MBEAN_FOLDERMANAGEMENT);

      // Message-Numbers der selektierten Display-Messages holen
      int[] messageNumbersOfSelectedDisplayMessages =
              folderWrapper.getMessageNumbersOfSelectedDisplayMessages();

      // Wir tun nur was, wenn mindestens eine Mail selektiert wurde.
      if(messageNumbersOfSelectedDisplayMessages.length >= 1) {

        // MailboxConnection und aktuellen Mailbox-Folder aus der Session holen
        MailboxConnection mailboxConnection = this.getMailboxConnection();
        String currentMailboxFolder = this.getCurrentMailboxFolder();

        // Mails verschieben
        try {

          mailboxConnection.login(currentMailboxFolder);
          mailboxConnection.moveMessages(messageNumbersOfSelectedDisplayMessages,
                  folderManagement.getActionFolder());

          // Nach dem Loeschen sind die gecachten Envelopes nicht mehr aktuell.
          SessionContainerBean sessionContainer =
                  (SessionContainerBean)this.getManagedBeanByName(Constants.NAME_MBEAN_SESSIONCONTAINER);
          sessionContainer.setRenewEnvelopes(true);

          // Ggf. aktuellen Offset anpassen
          assureCorrectOffset(folderWrapper, sessionContainer,
                  messageNumbersOfSelectedDisplayMessages);
        }
        catch(ConnectionEstablishException cee) {

          facesContext.addMessage(Constants.CLIENT_ID_MAILBOXHOST,
                  ExceptionConverter.getFacesMessage(facesContext, cee, false));
          return(Constants.OUTCOME_LOGON);
        }
        catch(AccessDeniedException ade) {

          facesContext.addMessage(Constants.CLIENT_ID_MAILBOXPASSWORD,
                  ExceptionConverter.getFacesMessage(facesContext, ade, false));
          return(Constants.OUTCOME_LOGON);
        }
        catch(MailboxFolderException mfe) {

          facesContext.addMessage(null,
                  ExceptionConverter.getFacesMessage(facesContext, mfe, true));
          return(Constants.OUTCOME_TECH_ERROR);
        }
        catch(MessageMovementException mme) {

          facesContext.addMessage(null,
                  ExceptionConverter.getFacesMessage(facesContext, mme, true));
          return(Constants.OUTCOME_TECH_ERROR);
        }
        finally {

          try {

            mailboxConnection.logout();
          }
          catch(LogoutException le) {

            facesContext.addMessage(null,
                    ExceptionConverter.getFacesMessage(facesContext, le, true));
            return(Constants.OUTCOME_TECH_ERROR);
          }
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
   * Bereitet die Anzeige einer ausgewaehlten Message vor.
   * 
   * @return          <code>String</code>-Objekt mit Info, wohin navigiert wird.
   */
  public String displaySelectedMail() {

    FacesContext facesContext = FacesContext.getCurrentInstance();

    try {

      // SessionContainerBean holen
      SessionContainerBean sessionContainer =
              (SessionContainerBean)this.getManagedBeanByName(Constants.NAME_MBEAN_SESSIONCONTAINER);

      // Message-Number der gewaehlten Mail in Session-Container schreiben.
      DisplayMessageBean dmb =
              (DisplayMessageBean)this.messageTable.getRowData();
      int messageNumber = dmb.getOriginMessage().getMessageNumber();
      sessionContainer.setMessageNumberCurrentDisplayMail(messageNumber);

      // Und nun zur "Mail anzeigen"-Seite
      return(Constants.OUTCOME_DISPLAY_MAIL);
    }
    catch(Exception e) {

      facesContext.addMessage(null,
              ExceptionConverter.getFacesMessage(facesContext, e, true));
      return(Constants.OUTCOME_TECH_ERROR);
    }
  }

  /**
   * Bereitet das Checken von neuen Mails vor.
   * 
   * @return          <code>String</code>-Objekt mit Info, wohin navigiert wird.
   */
  public String checkForNewMail() {

    FacesContext facesContext = FacesContext.getCurrentInstance();

    try {

      // Session-Container holen
      SessionContainerBean sessionContainer =
              (SessionContainerBean)this.getManagedBeanByName(Constants.NAME_MBEAN_SESSIONCONTAINER);

      // Wir setzen nur das Flag im Session-Container, den Rest (neue Envelopes
      // holen) macht der RenderResponsePhaseListener.
      sessionContainer.setRenewEnvelopes(true);

      // Zurueck zur "mails listing"-Seite
      return(Constants.OUTCOME_MAILS_LISTING);
    }
    catch(Exception e) {

      facesContext.addMessage(null,
              ExceptionConverter.getFacesMessage(facesContext, e, true));
      return(Constants.OUTCOME_TECH_ERROR);
    }
  }

  /**
   * Wechselt den Folder in der aktuellen Session.
   * 
   * @return          <code>String</code>-Objekt mit Info, wohin navigiert wird.
   */
  public String changeFolder() {

    String folderName = ((Folder)this.folderTable.getRowData()).getFullName();
    return(this.changeFolderToX(folderName));
  }

  /**
   * Wechselt den Folder in den Default-Ordner.
   * 
   * @return          <code>String</code>-Objekt mit Info, wohin navigiert wird.
   */
  public String changeToDefaultFolder() {

    return(this.changeFolderToX(Constants.LEERSTRING));
  }

  /**
   * Legt einen neuen Subfolder an.
   * 
   * @return          <code>String</code>-Objekt mit Info, wohin navigiert wird.
   */
  public String createSubfolder() {

    FacesContext facesContext = FacesContext.getCurrentInstance();

    try {

      // SessionContainerBean und FolderManagementBean holen
      SessionContainerBean sessionContainer =
              (SessionContainerBean)this.getManagedBeanByName(Constants.NAME_MBEAN_SESSIONCONTAINER);
      FolderManagementBean folderManagement =
              (FolderManagementBean)this.getManagedBeanByName(Constants.NAME_MBEAN_FOLDERMANAGEMENT);

      // Aus dem SessionContainer die Connection holen
      MailboxConnection mailboxConnection =
              sessionContainer.getMailboxConnection();

      // Kompletter Name des neuen Folders (bei einem neuen Ordner unterhalb des
      // Default-Folders ist der komplette Name gleich dem eingegebenen)
      StringBuffer nameNewFolder = new StringBuffer();
      String currentMailboxFolder = sessionContainer.getCurrentMailboxFolder(); 

      if(! currentMailboxFolder.equals(Constants.LEERSTRING)) {

        nameNewFolder.append(currentMailboxFolder);
        nameNewFolder.append(mailboxConnection.getFolderSeparator());
      }

      nameNewFolder.append(folderManagement.getNewSubfolder());

      // Folder anlegen
      try {

        mailboxConnection.login();
        mailboxConnection.createFolder(nameNewFolder.toString());
      }
      finally {

        // Auf jeden Fall schlieﬂen
        mailboxConnection.logout();
      }

      // Wir setzen nur das Flag im Session-Container, den Rest (Folder neu
      // laden) macht der RenderResponsePhaseListener.
      sessionContainer.setRenewEnvelopes(true);

      // Zurueck zur "mails listing"-Seite
      return(Constants.OUTCOME_MAILS_LISTING);
    }
    catch(MailboxFolderException mfe) {

      facesContext.addMessage(Constants.CLIENT_ID_NEW_SUBFOLDER,
              ExceptionConverter.getFacesMessage(facesContext, mfe, false));
      return(Constants.OUTCOME_MAILS_LISTING);
    }
    catch(Exception e) {

      facesContext.addMessage(null,
              ExceptionConverter.getFacesMessage(facesContext, e, true));
      return(Constants.OUTCOME_TECH_ERROR);
    }
  }

  /**
   * Loescht einen Subfolder.
   * 
   * @return          <code>String</code>-Objekt mit Info, wohin navigiert wird.
   */
  public String deleteSubfolder() {

    FacesContext facesContext = FacesContext.getCurrentInstance();

    try {

      // SessionContainerBean und FolderManagementBean holen
      SessionContainerBean sessionContainer =
              (SessionContainerBean)this.getManagedBeanByName(Constants.NAME_MBEAN_SESSIONCONTAINER);
      FolderManagementBean folderManagement =
              (FolderManagementBean)this.getManagedBeanByName(Constants.NAME_MBEAN_FOLDERMANAGEMENT);

      // Aus FolderManagementBean den zu loeschenden Folder holen
      String folderToDelete = folderManagement.getSubfolderToDelete();

      // Aus dem SessionContainer die Connection holen und Folder loeschen
      MailboxConnection mailboxConnection =
              sessionContainer.getMailboxConnection();

      // Folder loeschen
      try {

        mailboxConnection.login();
        mailboxConnection.deleteFolder(folderToDelete);
      }
      finally {

        // Auf jeden Fall schlieﬂen
        mailboxConnection.logout();
      }

      // Wir setzen nur das Flag im Session-Container, den Rest (Folder neu
      // laden) macht der RenderResponsePhaseListener.
      sessionContainer.setRenewEnvelopes(true);

      // Zurueck zur "mails listing"-Seite
      return(Constants.OUTCOME_MAILS_LISTING);
    }
    catch(MailboxFolderException mfe) {

      facesContext.addMessage(Constants.CLIENT_ID_SUBFOLDER_TO_DELETE,
              ExceptionConverter.getFacesMessage(facesContext, mfe, false));
      return(Constants.OUTCOME_MAILS_LISTING);
    }
    catch(Exception e) {

      facesContext.addMessage(null,
              ExceptionConverter.getFacesMessage(facesContext, e, true));
      return(Constants.OUTCOME_TECH_ERROR);
    }
  }

  /**
   * Versucht alle Mails zu selektieren
   * 
   * @return          <code>String</code>-Objekt mit Info, wohin navigiert wird. 
   */
  public String selectAllMails() {

    FacesContext facesContext = FacesContext.getCurrentInstance();

    try {

      // FolderWrapperBean holen
      FolderWrapperBean folderWrapper =
              (FolderWrapperBean)this.getManagedBeanByName(Constants.NAME_MBEAN_FOLDERWRAPPER);

      // Display-Messages holen und selektieren
      DisplayMessageBean[] displayMessages = folderWrapper.getDisplayMessages();

      for(int ii = 0; ii < displayMessages.length; ii++) {

        displayMessages[ii].setSelected(true);
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
   * Jumps to another paging-scope.
   * 
   * @return  <code>String</code>-object with info where to navigate to.
   */
  public String demandPaging() {

    FacesContext facesContext = FacesContext.getCurrentInstance();
    Map params = facesContext.getExternalContext().getRequestParameterMap();

    // Get param "newOffset" from the request
    if(params.containsKey(Constants.REQ_PARAM_NEW_OFFSET)) {

      try {

        // Get current SessionContainerBean
        SessionContainerBean sessionContainer =
                (SessionContainerBean)this.getManagedBeanByName(Constants.NAME_MBEAN_SESSIONCONTAINER);
        int tempNewOffset =
                Integer.parseInt((String)params.get(Constants.REQ_PARAM_NEW_OFFSET));

        // Set offset to new value
        sessionContainer.setCurrentOffset(tempNewOffset);

        // Instruct to re-load envelopes
        sessionContainer.setRenewEnvelopes(true);
      }
      catch(Exception e) {

        facesContext.addMessage(null,
                ExceptionConverter.getFacesMessage(facesContext, e, true));
        return(Constants.OUTCOME_TECH_ERROR);
      }
    }

    // If we get here, everything seems to be okay.
    return(Constants.OUTCOME_MAILS_LISTING);
  }

  /**
   * Aendert die Sortierreihenfolge auf Sortierung nach Subject. Wenn bereits
   * nach Subject sortiert wurde, wird die bisherige Sortierung invertiert.
   * 
   * @return          <code>String</code>-Objekt mit Info, wohin navigiert wird. 
   */
  public String sortBySubject() {

    return(this.sortByX(MESSAGE_BY_SUBJECT_COMPARATOR));
  }

  /**
   * Aendert die Sortierreihenfolge auf Sortierung nach Absender. Wenn bereits
   * nach Absender sortiert wurde, wird die bisherige Sortierung invertiert.
   * 
   * @return          <code>String</code>-Objekt mit Info, wohin navigiert wird. 
   */
  public String sortBySender() {

    return(this.sortByX(MESSAGE_BY_SENDER_COMPARATOR));
  }

  /**
   * Aendert die Sortierreihenfolge auf Sortierung nach Datum. Wenn bereits nach
   * Datum sortiert wurde, wird die bisherige Sortierung invertiert.
   * 
   * @return          <code>String</code>-Objekt mit Info, wohin navigiert wird. 
   */
  public String sortByDate() {

    return(this.sortByX(MESSAGE_BY_DATE_COMPARATOR));
  }

  /**
   * Aendert die Sortierreihenfolge auf Sortierung nach Spamlevel. Wenn bereits
   * nach Spamlevel sortiert wurde, wird die bisherige Sortierung invertiert.
   * 
   * @return          <code>String</code>-Objekt mit Info, wohin navigiert wird. 
   */
  public String sortBySpamlevel() {

    return(this.sortByX(MESSAGE_BY_SPAMLEVEL_COMPARATOR));
  }

  /**
   * Wird nach Subject aufsteigend sortiert?
   * 
   * @return  <code>true</code>, wenn aufsteigend sortiert wird;
   *          <code>false</code>, wenn absteigend sortiert wird.
   */
  public boolean isSortUpBySubject() {

    return(this.isSortUpByX(MessageBySubjectComparator.class));
  }

  /**
   * Wird nach Subject absteigend sortiert?
   * 
   * @return  <code>true</code>, wenn absteigend sortiert wird;
   *          <code>false</code>, wenn aufsteigend sortiert wird.
   */
  public boolean isSortDownBySubject() {

    return(this.isSortDownByX(MessageBySubjectComparator.class));
  }

  /**
   * Wird nach Sender aufsteigend sortiert?
   * 
   * @return  <code>true</code>, wenn aufsteigend sortiert wird;
   *          <code>false</code>, wenn absteigend sortiert wird.
   */
  public boolean isSortUpBySender() {

    return(this.isSortUpByX(MessageBySenderComparator.class));
  }

  /**
   * Wird nach Sender absteigend sortiert?
   * 
   * @return  <code>true</code>, wenn absteigend sortiert wird;
   *          <code>false</code>, wenn aufsteigend sortiert wird.
   */
  public boolean isSortDownBySender() {

    return(this.isSortDownByX(MessageBySenderComparator.class));
  }

  /**
   * Wird nach Date aufsteigend sortiert?
   * 
   * @return  <code>true</code>, wenn aufsteigend sortiert wird;
   *          <code>false</code>, wenn absteigend sortiert wird.
   */
  public boolean isSortUpByDate() {

    return(this.isSortUpByX(MessageByDateComparator.class));
  }

  /**
   * Wird nach Date absteigend sortiert?
   * 
   * @return  <code>true</code>, wenn absteigend sortiert wird;
   *          <code>false</code>, wenn aufsteigend sortiert wird.
   */
  public boolean isSortDownByDate() {

    return(this.isSortDownByX(MessageByDateComparator.class));
  }

  /**
   * Wird nach Spamlevel aufsteigend sortiert?
   * 
   * @return  <code>true</code>, wenn aufsteigend sortiert wird;
   *          <code>false</code>, wenn absteigend sortiert wird.
   */
  public boolean isSortUpBySpamlevel() {

    return(this.isSortUpByX(MessageBySpamlevelComparator.class));
  }

  /**
   * Wird nach Spamlevel absteigend sortiert?
   * 
   * @return  <code>true</code>, wenn absteigend sortiert wird;
   *          <code>false</code>, wenn aufsteigend sortiert wird.
   */
  public boolean isSortDownBySpamlevel() {

    return(this.isSortDownByX(MessageBySpamlevelComparator.class));
  }

  /**
   * Determine the number of the last message on the page.
   * 
   * @return  <code>int</code>-value of the number of the last message.
   */
  public int getNumberLastMessage() {

    try {

      // Get SessionContainerBean and FolderWrapperBean 
      SessionContainerBean sessionContainer =
              (SessionContainerBean)this.getManagedBeanByName(Constants.NAME_MBEAN_SESSIONCONTAINER);
      FolderWrapperBean folderWrapper =
              (FolderWrapperBean)this.getManagedBeanByName(Constants.NAME_MBEAN_FOLDERWRAPPER);

      // Paging-mode? With "all on one page" we return the overall message count
      if(sessionContainer.isAllMessagesOnOnePage()) {

        return(folderWrapper.getOverallMessageCount());
      }

      // Else calculate the number
      else {

        int currentOffsetPlusMessagesPerPage =
                sessionContainer.getCurrentOffset() +
                sessionContainer.getAmountOfMessagesPerPage(); 

        if(currentOffsetPlusMessagesPerPage >
                folderWrapper.getOverallMessageCount()) {

          return(folderWrapper.getOverallMessageCount());
        }
        else {

          return(currentOffsetPlusMessagesPerPage);
        }
      }
    }
    catch(Exception e) {

      // Should not happen. But if so, throw RuntimeException
      throw(new RuntimeException(e.getMessage(), e));
    }
  }

  /**
   * Determine the max offset (based on the amount of messages in the current
   * folder).
   * 
   * @return  <code>int</code>-value with the max offset.
   */
  public int getMaxOffset() {

    try {

      // Get SessionContainerBean and FolderWrapperBean 
      SessionContainerBean sessionContainer =
              (SessionContainerBean)this.getManagedBeanByName(Constants.NAME_MBEAN_SESSIONCONTAINER);
      FolderWrapperBean folderWrapper =
              (FolderWrapperBean)this.getManagedBeanByName(Constants.NAME_MBEAN_FOLDERWRAPPER);

      // No messages - max offset = 0
      if(folderWrapper.getOverallMessageCount() < 1) {

        return(0);
      }
      else {

        int x = (folderWrapper.getOverallMessageCount() - 1) /
                sessionContainer.getAmountOfMessagesPerPage();
        return(x * sessionContainer.getAmountOfMessagesPerPage());
      }
    }
    catch(Exception e) {

      // Should not happen. But if so, throw RuntimeException
      throw(new RuntimeException(e.getMessage(), e));
    }
  }

  /**
   * Determine if paging-forth is possible.
   * 
   * @return  <code>true</code>, if paging-forth is possible;
   *          <code>false</code>, if paging-forth is not possible.
   */
  public boolean isPagingForthPossible() {

    try {

      // Get SessionContainerBean and FolderWrapperBean 
      SessionContainerBean sessionContainer =
              (SessionContainerBean)this.getManagedBeanByName(Constants.NAME_MBEAN_SESSIONCONTAINER);
      FolderWrapperBean folderWrapper =
              (FolderWrapperBean)this.getManagedBeanByName(Constants.NAME_MBEAN_FOLDERWRAPPER);

      // Paging-mode? With "all on one page" we return FALSE
      if(sessionContainer.isAllMessagesOnOnePage()) {

        return(false);
      }

      // Else calculate possibility
      else {

        return(folderWrapper.getOverallMessageCount() >
                (sessionContainer.getCurrentOffset() +
                        sessionContainer.getAmountOfMessagesPerPage()));
      }
    }
    catch(Exception e) {

      // Should not happen. But if so, throw RuntimeException
      throw(new RuntimeException(e.getMessage(), e));
    }
  }

}
