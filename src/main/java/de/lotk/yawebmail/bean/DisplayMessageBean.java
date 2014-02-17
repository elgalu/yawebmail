/*
 * @(#)DisplayMessage.java 1.00 2006/03/03
 *
 * Copyright (c) 2006, Stephan Sann
 *
 * 03.03.2006 ssann                  Vers. 1.0     created
 */


package de.lotk.yawebmail.bean;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

import javax.mail.Address;
import javax.mail.Header;
import javax.mail.Message;
import javax.mail.MessagingException;

import de.lotk.yawebmail.application.Lifecycle;

/**
 * Bean die Daten zur Anzeige einer Message aufnimmt. 
 * 
 * @author Stephan Sann
 * @version 1.0
 */
public class DisplayMessageBean implements Lifecycle, Serializable {

  // ---------------------------------------------------------------- Konstanten

  /** serialVersionUID */
  private static final long serialVersionUID = -5647805002355682456L;


  // --------------------------------------------------------- Instanz-Variablen

  /** Originaere Message */
  private Message originMessage = null;

  /** Anzuzeigende Parts */
  private List displayParts = null;

  /** Inline-Parts */
  private Map inlineParts = null;

  /** Map mit den Mutlipart-Objekten (und deren Child-Parts) */
  private Map multiparts = null;

  /** Ist diese Message selektiert? */
  private boolean selected = false;


  // --------------------------------------------------------- Getter und Setter

  /**
   * @return Returns the originMessage.
   */
  public Message getOriginMessage() {
    return originMessage;
  }

  /**
   * @param originMessage The originMessage to set.
   */
  public void setOriginMessage(Message originMessage) {
    this.originMessage = originMessage;
  }

  /**
   * @return Returns the displayParts.
   */
  public List getDisplayParts() {
    return displayParts;
  }

  /**
   * @param displayParts The displayParts to set.
   */
  public void setDisplayParts(List displayParts) {
    this.displayParts = displayParts;
  }

  /**
   * @return Returns the inlineParts.
   */
  public Map getInlineParts() {
    return inlineParts;
  }

  /**
   * @param inlineParts The inlineParts to set.
   */
  public void setInlineParts(Map inlineParts) {
    this.inlineParts = inlineParts;
  }

  /**
   * @return Returns the multiparts.
   */
  public Map getMultiparts() {

    return this.multiparts;
  }

  /**
   * @param multiparts The multiparts to set.
   */
  public void setMultiparts(Map multiparts) {

    this.multiparts = multiparts;
  }

  /**
   * @return Returns the selected.
   */
  public boolean isSelected() {

    return (this.selected);
  }

  /**
   * @param selected The selected to set.
   */
  public void setSelected(boolean selected) {

    this.selected = selected;
  }


  // ----------------------------------------------------- oeffentliche Methoden

  /**
   * Liefert die To-Recipients
   */
  public Address[] getToRecipients() {

    try {

      return(this.originMessage.getRecipients(Message.RecipientType.TO));
    }
    catch(Exception e) {

      e.printStackTrace();
      return(null);
    }
  }

  /**
   * Liefert die CC-Recipients
   */
  public Address[] getCcRecipients() {

    try {

      return(this.originMessage.getRecipients(Message.RecipientType.CC));
    }
    catch (MessagingException e) {

      e.printStackTrace();
      return(null);
    }
  }

  /**
   * Liefert die BCC-Recipients
   */
  public Address[] getBccRecipients() {

    try {

      return(this.originMessage.getRecipients(Message.RecipientType.BCC));
    }
    catch (MessagingException e) {

      e.printStackTrace();
      return(null);
    }
  }

  /**
   * Liefert den Message-Quelltext
   */
  public String getMessageSource() {

    try {

      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      this.originMessage.writeTo(baos);

      return(baos.toString());
    }
    catch(Exception e) {

      e.printStackTrace();
      return(e.getMessage());
    }
  }

  /**
   * Liefert alle Header-Lines als Liste.
   */
  public List getAllHeaderLinesAsList() {

    try {

      List<String> allHeaderLines = new ArrayList<String>();
      Enumeration headerEnum = this.originMessage.getAllHeaders();

      while(headerEnum.hasMoreElements()) {

        Header currentHeader = (Header)headerEnum.nextElement();
        String headerLine =
                currentHeader.getName() + ": " + currentHeader.getValue(); 

        allHeaderLines.add(headerLine);
      }

      return(allHeaderLines);
    }
    catch(MessagingException e) {

      throw(new RuntimeException("Konnte Header-Lines nicht beziehen.", e));
    }
  }

  /**
   * Setzt die Bean zurueck
   */
  public void reset() {

    this.originMessage = null;
    this.displayParts = null;
    this.inlineParts = null;
    this.multiparts = null;
    this.selected = false;
  }

  /* (non-Javadoc)
   * @see de.lotk.webftp.Lifecycle#destroy()
   */
  public void destroy() {

    this.reset();
  }

}
