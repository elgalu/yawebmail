/*
 * @(#)ChoicesRetriever.java 1.00 2007/02/19
 *
 * Copyright (c) 2006, Stephan Sann
 *
 * 19.02.2007 ssann        Vers. 1.0     created
 */


package de.lotk.yawebmail.util.faces;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.mail.Folder;

import de.lotk.yawebmail.application.Constants;
import de.lotk.yawebmail.bean.SessionContainerBean;
import de.lotk.yawebmail.enumerations.MailboxProtocolEnum;
import de.lotk.yawebmail.util.MessageMapper;

/**
 * Holt die Auswahlmoeglichkeiten fuer Dropdownlisten
 * 
 * @author Stephan Sann
 * @version 1.0
 */
public class ChoicesRetriever {

  // --------------------------------------------------------- Instanz-Variablen

  /** Haelt die SelectItems fuer Sprachauswahl-Dropdownlisten */
  private SelectItem[] sprachauswahlChoices = null;

  /** Haelt die erlaubten Mailbox-Protokolle */
  private SelectItem[] mailboxProtocolChoices = null;


  // ----------------------------------------------------------- Konstruktor(en)

  /**
   * Initialisiert ein neues ChoicesRetriever-Objekt. 
   */
  public ChoicesRetriever() {

    // Sprachauswahl-Choices initialisieren.
    this.initSprachauswahlChoices();

    // MailboxProtocol-Choices initialisieren.
    this.initMailboxProtocolChoices();
  }


  // ---------------------------------------------------------- private Methoden

  /**
   * Liefernt Auswahlmoeglichkeiten zum uebergebenen Enum-Array. Muss aus einer
   * Faces-Anwendung heraus aufgerufen werden.
   * 
   * @param   enumArray  Array mit Enum-Elementen
   * @return  <code>SelectItem[]</code> mit String-Schluesseln und Enum-Werten.
   */
  private SelectItem[] getEnumChoices(Enum[] enumArray) {

    FacesContext fc = FacesContext.getCurrentInstance();
    String bundle = fc.getApplication().getMessageBundle();
    Locale locale = fc.getViewRoot().getLocale();

    SelectItem[] selectItems = new SelectItem[enumArray.length];

    for(int ii = 0; ii < enumArray.length; ii++) {

      String tmpText = MessageMapper.getMessageResourceString(bundle,
              enumArray[ii].name(), null, locale);

      selectItems[ii] = new SelectItem(enumArray[ii], tmpText);
    }

    return(selectItems);
  }

  /**
   * Initialisiert die Sprachauswahl-Choices. 
   */
  private void initSprachauswahlChoices() {

    // Was wurde in der faces-config.xml als Sprachen definiert?
    FacesContext fc = FacesContext.getCurrentInstance();
    Iterator supportedLocales = fc.getApplication().getSupportedLocales();

    List<SelectItem> selectItemsList = new ArrayList<SelectItem>(); 
    String nameMessageBundle = fc.getApplication().getMessageBundle();

    // SelectItem-Array iterativ befuellen
    for(int ii = 0; supportedLocales.hasNext(); ii++) {

      Locale aktLocale = (Locale)supportedLocales.next();

      String internationalName =
              MessageMapper.getMessageResourceString(nameMessageBundle,
              Constants.PROPERTY_KEY_LANGUAGE_INTERNATIONALNAME, null,
              aktLocale);
      String localName =
              MessageMapper.getMessageResourceString(nameMessageBundle,
              Constants.PROPERTY_KEY_LANGUAGE_LOCALNAME, null,
              aktLocale);

      StringBuilder itemLabel = new StringBuilder(internationalName);
      itemLabel.append(" (").append(localName).append(")");

      selectItemsList.add(new SelectItem(aktLocale, itemLabel.toString()));
    }

    // Herausgefundene SelectItems in Instanz-Variable speichern.
    this.sprachauswahlChoices =
            (SelectItem[])selectItemsList.toArray(new SelectItem[selectItemsList.size()]);
  }

  /**
   * Initialisiert die Sprachauswahl-Choices. 
   */
  private void initMailboxProtocolChoices() {

    this.mailboxProtocolChoices =
            this.getEnumChoices(MailboxProtocolEnum.values());
  }


  // ----------------------------------------------------- oeffentliche Methoden

  /**
   * Holt die Sprachauswahl-Moeglichkeiten. Muss aus einer Faces-Anwendung
   * heraus aufgerufen werden.
   * 
   * @return <code>SelectItem</code>-Array mit String-Schluesseln und
   *         Locale-Werten
   */
  public SelectItem[] getSprachauswahlChoices() {

    return(this.sprachauswahlChoices);
  }

  /**
   * Holt die MailboxProtocol-Moeglichkeiten. Muss aus einer Faces-Anwendung
   * heraus aufgerufen werden.
   * 
   * @return <code>SelectItem</code>-Array mit String-Schluesseln und
   *         String-Werten
   */
  public SelectItem[] getMailboxProtocolChoices() {

    return(this.mailboxProtocolChoices);
  }

  /**
   * Liefert alle Ordner eines Accounts.
   */
  public SelectItem[] getAllFolders() {

    FacesContext fc = FacesContext.getCurrentInstance();

    try {

      SessionContainerBean sessionContainer =
              (SessionContainerBean)ManagedBeanUtils.getManagedBeanByName(fc, Constants.NAME_MBEAN_SESSIONCONTAINER);

      // Alle Folder aus Mailbox-Connection beziehen und in SelectItems packen
      Set allFolders = sessionContainer.getMailboxConnection().getAllFolders();
      SelectItem[] selectItems = new SelectItem[allFolders.size()];
      Iterator folderIterator = allFolders.iterator();

      for(int ii = 0; folderIterator.hasNext(); ii++) {

        String currentFolderName =
                ((Folder)folderIterator.next()).getFullName();
        SelectItem si = new SelectItem();
        si.setLabel(currentFolderName);
        si.setValue(currentFolderName);

        selectItems[ii] = si;
      }

      return(selectItems);
    }
    catch(Exception e) {

      e.printStackTrace();
      throw(new RuntimeException("Fehler bei getAllFolders()."));
    }
  }

}
