/*
 * @(#)FolderManagementBean.java 1.00 2007/07/24
 *
 * Copyright (c) 2007, Stephan Sann
 *
 * 24.07.2007 ssann        Vers. 1.0     created
 */


package de.lotk.yawebmail.bean;

import java.io.Serializable;

import de.lotk.yawebmail.application.Lifecycle;

/**
 * Bean die Daten zum Folder-Management aufnimmt 
 * 
 * @author Stephan Sann
 * @version 1.0
 */
public class FolderManagementBean implements Lifecycle, Serializable {

  // ---------------------------------------------------------------- Konstanten

  /** serialVersionUID */
  private static final long serialVersionUID = 1129798866717217867L;


  // --------------------------------------------------------- Instanz-Variablen

  /** Hier wird der Name eines neuen Subfolders abgelegt */
  private String newSubfolder = null;

  /** Hier wird der Name eines zu loeschenden Subfolders abgelegt */
  private String subfolderToDelete = null;

  /** Folder, auf den oder mit dem eine Aktion ausgefuehrt wird */
  private String actionFolder = null;


  // --------------------------------------------------------- Getter und Setter

  /**
   * @return the newSubfolder
   */
  public String getNewSubfolder() {
  
    return newSubfolder;
  }

  /**
   * @param newSubfolder the newSubfolder to set
   */
  public void setNewSubfolder(String newSubfolder) {
  
    this.newSubfolder = newSubfolder;
  }
  
  /**
   * @return the subfolderToDelete
   */
  public String getSubfolderToDelete() {
  
    return subfolderToDelete;
  }

  /**
   * @param subfolderToDelete the subfolderToDelete to set
   */
  public void setSubfolderToDelete(String subfolderToDelete) {
  
    this.subfolderToDelete = subfolderToDelete;
  }
  
  /**
   * @return the actionFolder
   */
  public String getActionFolder() {
  
    return actionFolder;
  }

  /**
   * @param actionFolder the actionFolder to set
   */
  public void setActionFolder(String actionFolder) {
  
    this.actionFolder = actionFolder;
  }


  // ----------------------------------------------------- oeffentliche Methoden

  /**
   * Setzt die Bean zurueck
   */
  public void reset() {

    this.newSubfolder = null;
    this.subfolderToDelete = null;
    this.actionFolder = null;
  }

  /* (non-Javadoc)
   * @see de.lotk.webftp.Lifecycle#destroy()
   */
  public void destroy() {

    this.reset();
  }

}
