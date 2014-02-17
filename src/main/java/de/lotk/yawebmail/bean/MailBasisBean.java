/*
 * @(#)MailBasisBean.java 1.00 2006/04/21
 *
 * Copyright (c) 2006, Stephan Sann
 *
 * 21.04.2006 ssann        Vers. 1.0     created
 */


package de.lotk.yawebmail.bean;

import de.lotk.yawebmail.application.Lifecycle;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.activation.DataHandler;
import javax.mail.internet.MimeBodyPart;
import javax.mail.util.ByteArrayDataSource;

import org.apache.myfaces.custom.fileupload.UploadedFile;

/**
 * Geruest einer Mail.
 * 
 * @author Stephan Sann
 * @version 1.0
 */
public class MailBasisBean implements Lifecycle, Serializable {

  // ---------------------------------------------------------------- Konstanten

  /** serialVersionUID */
  private static final long serialVersionUID = -6482098152567561001L;


  // --------------------------------------------------------- Instanz-Variablen

  /** Hier merken wir uns den Betreff */
  private String subject = null;

  /** Hier merken wir uns den Mail-Text */
  private String mailText = null;

  /** Hier merken wir uns den/die Absender */
  private String from = null;

  /** Hier merken wir uns den/die Empfaenger */
  private String rcptTo = null;

  /** Hier merken wir uns den/die CC-Empfaenger */
  private String rcptCc = null;

  /** Hier merken wir uns den/die BCC-Empfaenger */
  private String rcptBcc = null;

  /** Hier merken wir uns ggf. den "In-Reply-To"-Header */
  private String inReplyTo = null; 

  /** Hier merken wir uns das/die Attachments */
  private List<MimeBodyPart> attachments = new ArrayList<MimeBodyPart>();


  // --------------------------------------------------------- Getter und Setter

  /**
   * @return Returns the mailText.
   */
  public String getMailText() {

    return mailText;
  }

  /**
   * @param mailText
   *          The mailText to set.
   */
  public void setMailText(String mailText) {

    this.mailText = mailText;
  }

  /**
   * @return Returns the rcptBcc.
   */
  public String getRcptBcc() {

    return rcptBcc;
  }

  /**
   * @param rcptBcc
   *          The rcptBcc to set.
   */
  public void setRcptBcc(String rcptBcc) {

    this.rcptBcc = rcptBcc;
  }

  /**
   * @return Returns the rcptCc.
   */
  public String getRcptCc() {

    return rcptCc;
  }

  /**
   * @param rcptCc
   *          The rcptCc to set.
   */
  public void setRcptCc(String rcptCc) {

    this.rcptCc = rcptCc;
  }

  /**
   * @return Returns the rcptTo.
   */
  public String getRcptTo() {

    return rcptTo;
  }

  /**
   * @param rcptTo
   *          The rcptTo to set.
   */
  public void setRcptTo(String rcptTo) {

    this.rcptTo = rcptTo;
  }

  /**
   * @return Returns the subject.
   */
  public String getSubject() {

    return subject;
  }

  /**
   * @param subject
   *          The subject to set.
   */
  public void setSubject(String subject) {

    this.subject = subject;
  }

  /**
   * @return the attachments
   */
  public List<MimeBodyPart> getAttachments() {
  
    return attachments;
  }

  /**
   * @param attachments the attachments to set
   */
  public void setAttachments(List<MimeBodyPart> attachments) {
  
    this.attachments = attachments;
  }

  /**
   * @return Returns the from.
   */
  public String getFrom() {
  
    return from;
  }
  
  /**
   * @param from The from to set.
   */
  public void setFrom(String from) {
  
    this.from = from;
  }

  /**
   * @return Returns the inReplyTo.
   */
  public String getInReplyTo() {
  
    return inReplyTo;
  }

  /**
   * @param inReplyTo The inReplyTo to set.
   */
  public void setInReplyTo(String inReplyTo) {
  
    this.inReplyTo = inReplyTo;
  }


  // ---------------------------------------------------------- private Methoden

  /**
   * Speichert ein hochgeladenes Attachment in dem Attachment-Array.
   * 
   * @param   uploadedFile  Hochgeladenes File.
   */
  private void addAttachment(UploadedFile upFile) throws Exception {

    if((upFile.getName() != null) && (upFile.getName().length() >= 1)) {

      // Das hochgeladene File zu der Liste der Attachments hinzufuegen
      MimeBodyPart mbp = new MimeBodyPart();
      mbp.setFileName(upFile.getName());

      // Text-Contents (z.B. "text/xml") muessen als String rein.
      // (sonst Exception: DataHandler requires String-Object)
      if(upFile.getContentType().toLowerCase().startsWith("text")) {

        mbp.setContent(new String(upFile.getBytes()),
                upFile.getContentType());
      }

      // Sonst Content als byte-Array eintueten.
      else {

        mbp.setContent(upFile.getBytes(), upFile.getContentType());
        mbp.setDataHandler(new DataHandler(new
                ByteArrayDataSource(upFile.getBytes(),
                upFile.getContentType())));
      }

      this.attachments.add(mbp);
    }
  }


  // ----------------------------------------------------- oeffentliche Methoden

  /**
   * Setzt ein neues Attachment (JSF denkt es wuerde ein Property setzen, aber
   * in Wirklichkeit wird das UploadedFile-Objekt einfach dem Attachment-Array
   * hinzugefuegt).
   * 
   * @param   uploadedFile  Hochgeladenes File.
   */
  public void setNewAttachment(UploadedFile uploadedFile) {

    try {

      this.addAttachment(uploadedFile);
    }
    catch(Exception e) {

      // TODO irgendwas machen
    }
  }

  /**
   * Liefert ein (nicht vorhandenes) Property "newAttachment". Noetig, damit wir
   * dieses in der JSP verwenden koennen.
   */
  public UploadedFile getNewAttachment() {

    return(null);
  }

  /**
   * Setzt die Bean zurueck
   */
  public void reset() {

    this.subject = null;
    this.mailText = null;
    this.from = null;
    this.rcptTo = null;
    this.rcptCc = null;
    this.rcptBcc = null;
    this.inReplyTo = null;
    this.attachments = new ArrayList<MimeBodyPart>();
  }

  /* (non-Javadoc)
   * @see de.lotk.webftp.Lifecycle#destroy()
   */
  public void destroy() {

    this.reset();
  }

}
