/*
 * @(#)FolderWrapperBean.java 1.00 2006/02/28
 *
 * Copyright (c) 2006, Stephan Sann
 *
 * 28.02.2006 ssann        Vers. 1.0     created
 */


package de.lotk.yawebmail.bean;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;

import com.sun.mail.imap.IMAPFolder;
import com.sun.mail.pop3.POP3Folder;

import de.lotk.yawebmail.application.Lifecycle;

/**
 * Bean die einen Folder wrappt und zu diesem verschiedene Hilfmethoden zur
 * Verfuegung stellt.
 * <br/><br/>
 * 
 * Das per "setMessages" uebergebene Array wird in ein Array von DisplayMessages
 * konvertiert, so dass zusaetzliche Infos zu den Mails gemerkt werden koennen.
 * 
 * @author Stephan Sann
 * @version 1.0
 */
public class FolderWrapperBean implements Lifecycle, Serializable {

  // ---------------------------------------------------------------- Konstanten

  /** serialVersionUID */
  private static final long serialVersionUID = -3400739251118573376L;


  // --------------------------------------------------------- Instanz-Variablen

  /** Hier merken wir uns den javax-mail-folder */
  private Folder folder = null;

  /** Hier merken wir uns die Subfolder des aktuellen javax-mail-folders */
  private Folder[] subfolders = null;

  /** Hier merken wir uns den Type des aktuellen javax-mail-folders */
  private int folderType = (-1);

  /** Hier merken wir uns das Message-Array als Display-Messages*/
  private DisplayMessageBean[] displayMessages = null;

  /** Hier merken wir uns das originale Message-Array */
  private Message[] messages = null;

  /** Holds the (overall) amount of messages within the message-folder */
  private int overallMessageCount = 0;


  // --------------------------------------------------------- Getter und Setter

  /**
   * @return Returns the folder.
   */
  public Folder getFolder() {

    return folder;
  }

  /**
   * @return the subfolders
   */
  public Folder[] getSubfolders() {
  
    return subfolders;
  }

  /**
   * @return the folderType
   */
  public int getFolderType() {

    return folderType;
  }

  /**
   * @return Returns the messages.
   */
  public DisplayMessageBean[] getDisplayMessages() {

    return(this.displayMessages);
  }

  /**
   * Gibt das originale Message-Array unserer Display-Messages zurueck.
   * 
   * @param   messages   The messages to set.
   */
  public Message[] getMessages() {

    return(this.messages);
  }
  
  /**
   * @return the overallMessageCount
   */
  public int getOverallMessageCount() {
  
    return overallMessageCount;
  }

  /**
   * @param overallMessageCount the overallMessageCount to set
   */
  public void setOverallMessageCount(int overallMessageCount) {
  
    this.overallMessageCount = overallMessageCount;
  }


  // ----------------------------------------------------- oeffentliche Methoden

  /**
   * Wenn der Folder gesetzt wird, speichern wir auch gleich die Subfolder und
   * den Type (sonst muesste spaeter extra eine Verbindung aufgebaut werden,
   * wenn die Subfolder oder der Type benoetigt werden).
   * 
   * @param folder The folder to set.
   */
  public void setFolder(Folder folder) {

    try {

      this.folder = folder;
      this.folderType = folder.getType();

      // Wenn der Folder ein POP3Folder ist, wird ein leeres Folder-Array
      // gespeichert zurueckgeliefert, da POP3 keine Unterordner unterstuetzt.
      if(folder instanceof POP3Folder) {

        this.subfolders = new Folder[0];
      }

      // Sonst Unterordner aus dem Folder lesen und speichern.
      else {

        this.subfolders = folder.list();
      }
    }
    catch(Exception e) {

      throw(new RuntimeException("Probleme beim Setzen des Folders.", e));
    }
  }

  /**
   * Hier kann ein "normales" Message-Array uebergeben werden. Dieses wird in
   * ein DisplayMessage-Array konvertiert und gespeichert.
   * 
   * @param   messages   The messages to set.
   */
  public void setMessages(Message[] messages) {

    // Das originale Message-Array speichern - es koennte noch gebraucht werden
    this.messages = messages;

    // Jetzt alle Messges im DisplayMessages-Array speichern
    this.displayMessages = new DisplayMessageBean[messages.length];

    for(int ii = 0; ii < messages.length; ii++) {

      this.displayMessages[ii] = new DisplayMessageBean();
      this.displayMessages[ii].setOriginMessage(messages[ii]);
    }
  }

  /**
   * Kann aus diesem Ordner in einen anderen Ordner gewechselt werden? Je nach
   * Protokoll sind nur 1 oder n Ordner moeglich.
   * 
   * @return   <code>boolean</code>, ob aus diesem Ordner in einen anderen
   *           gewechselt werden kann.
   */
  public boolean isSwappable() {

    // Bei IMAP sagen wir ja, bei allem anderen (ausser POP3 unterstuetzen wir
    // z.Zt. aber kein Protokoll) nein.
    return(this.folder instanceof IMAPFolder);
  }

  /**
   * Kann dieser Ordner Subfolder besitzen?
   * 
   * @return  <code>boolean</code>, ob dieser Ordner Subfolder enthalten kann.
   */
  public boolean isSubfolderPossible() {

    // TRUE zurueckliefern, wenn der Ordner nicht NUR Messages halten kann.
    return(this.folderType != Folder.HOLDS_MESSAGES);
  }

  /**
   * Liefert die Unterordner als Map (Key = Name, Value = voller Name).
   * 
   * @return  <code>Map</code>-Objekt mit Unterordner-Mapping.
   */
  public Map getSubfolderMap() throws MessagingException {

    Folder[] subfolders = this.getSubfolders();
    HashMap<String, String> subfolderMap = new HashMap<String, String>();

    for(int ii = 0; ii < subfolders.length; ii++) {

      subfolderMap.put(subfolders[ii].getName(), subfolders[ii].getFullName());
    }

    return(subfolderMap);
  }

  /**
   * Liefert die Message-Numbers der selektierten Display-Messages.
   * 
   * @return    <code>int</code>-Array mit den Message-Numbers.
   */
  public int[] getMessageNumbersOfSelectedDisplayMessages() {

    int[] workArray = new int[this.displayMessages.length]; 
    int amountSelectedMessages = 0;

    for(int ii = 0; ii < this.displayMessages.length; ii++) {

      if(this.displayMessages[ii].isSelected()) {

        workArray[amountSelectedMessages++] =
                this.displayMessages[ii].getOriginMessage().getMessageNumber();
      }
    }

    int[] messageNumbersOfSelectedDisplayMessages =
            new int[amountSelectedMessages];
    System.arraycopy(workArray, 0, messageNumbersOfSelectedDisplayMessages, 0,
            amountSelectedMessages);
    return(messageNumbersOfSelectedDisplayMessages);
  }

  /**
   * Liefert die Anzahl der Nachrichten im aktuellen Folder.
   * 
   * @return  <code>int</code>-Wert mit Anzahl der Nachrichten.
   */
  public int getMessageCount() {

    return((this.messages != null) ? this.messages.length : 0);
  }

  /**
   * Setzt die Bean zurueck
   */
  public void reset() {

    this.folder = null;
    this.subfolders = null;
    this.displayMessages = null;
    this.messages = null;
    this.overallMessageCount = 0;
  }

  /* (non-Javadoc)
   * @see de.lotk.webftp.Lifecycle#destroy()
   */
  public void destroy() {

    this.reset();
  }

}
