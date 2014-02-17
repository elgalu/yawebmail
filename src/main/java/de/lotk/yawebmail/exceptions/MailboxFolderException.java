/*
 * @(#)MailboxFolderException.java 1.00 2005/02/06
 *
 * Copyright (c) 2005, Stephan Sann
 *
 * 06.02.2005 ssann        Vers. 1.0     created
 */


package de.lotk.yawebmail.exceptions;

/**
 * Tritt auf, wenn es ein Problem mit einem Mailbox-Folder gibt.
 * 
 * @author Stephan Sann
 * @version 1.0
 */
public class MailboxFolderException extends Exception {

  // ---------------------------------------------------------------- Konstanten

  /** serialVersionUID */
  private static final long serialVersionUID = 4158788849930892133L;


  // --------------------------------------------------------- Instanz-Variablen

  /** Speichert den Folder-Namen */
  private String folderName = null;


  // ----------------------------------------------------------- Konstruktor(en)

  /**
   * Initialisiert eine MailboxFolderException mit einer Message und einem
   * Folder-Namen.
   * 
   * @param   message     Message fuer die Exception
   * @param   folderName  Name des Sorgen-Folders
   */
  public MailboxFolderException(String message, String folderName) {

    super(message);
    this.folderName = folderName;
  }

  /**
   * Initialisiert eine MailboxFolderException mit einer Message, einem
   * ausloesenden Throwable und einem Folder-Namen.
   * 
   * @param   message     Message fuer die Exception
   * @param   cause       Ausloesender Throwable
   * @param   folderName  Name des Sorgen-Folders
   */
  public MailboxFolderException(String message, Throwable cause, String
          folderName) {

    super(message, cause);
    this.folderName = folderName;
  }


  // --------------------------------------------------------- Getter und Setter

  /**
   * @return Returns the folderName.
   */
  public String getFolderName() {
  
    return folderName;
  }

}
