/*
 * @(#)FolderByFullNameComparator.java 1.00 2007/07/25
 *
 * Copyright (c) 2007, Stephan Sann
 *
 * 25.07.2007 ssann        Vers. 1.0     created
 */


package de.lotk.yawebmail.business;

import java.io.Serializable;
import java.util.Comparator;

import javax.mail.Folder;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Comparator fuer das Sortieren von Foldern nach ihrem vollen Namen
 * 
 * @author Stephan Sann
 * @version 1.0
 */
public class FolderByFullNameComparator implements Comparator<Folder>,
        Serializable {

  // ----------------------------------------------------------------- Constants

  /** serialVersionUID */
  private static final long serialVersionUID = -5513920870856710489L;


  // --------------------------------------------------------- Klassen-Variablen

  /** Logging-Instanz */
  protected static final Log LOG =
          LogFactory.getLog(FolderByFullNameComparator.class);


  // ----------------------------------------------------- oeffentliche Methoden

  /* (non-Javadoc)
   * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
   */
  public int compare(Folder o1, Folder o2) {

    int rueck = 0;

    try {

      // Sind die Sender-Arrays beider Messages am Start und nicht leer?
      boolean einsDa = (o1 != null);
      boolean zweiDa = (o2 != null);
      
      if(einsDa && zweiDa) {

        String fullNameFolderEins = ((Folder)o1).getFullName();
        String fullNameFolderZwei = ((Folder)o2).getFullName();

        // Wir sortieren nach dem ersten Sender
        rueck = fullNameFolderEins.compareToIgnoreCase(fullNameFolderZwei);
      }

      // Sonst Messages mit leeren "Froms" nach oben sortieren
      else if((! einsDa) && zweiDa) {

        rueck = (-50);
      }
      else if(einsDa && (! zweiDa)) {

        rueck = 50;
      }
      else {

        rueck = 0;
      }

      return(rueck);
    }
    catch(Exception e) {

      // Bei einer Exception geben wir 0 zurueck.
      LOG.error("[compare] Problem beim Beziehen der Folder.", e);
      return(0);
    }
  }

}
