/*
 * @(#)OutputPlainTextPartUIComponent.java 1.00 2006/03/16
 *
 * Copyright (c) 2006, Stephan Sann
 *
 * 16.03.2006 ssann        Vers. 1.0     created
 */


package de.lotk.yawebmail.cuic;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.faces.component.UIOutput;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.mail.MessagingException;
import javax.mail.Part;
import javax.mail.internet.MimeBodyPart;
import javax.servlet.http.HttpServletRequest;

import de.lotk.yawebmail.util.PresentationUtils;

/**
 * UI-Component zur Ausgabe eines Plain-Text-Message-Parts 
 * 
 * @author Stephan Sann
 * @version 1.0
 */
public class OutputPlainTextPartUIComponent extends UIOutput {

  // ---------------------------------------------------------- private Methoden

  /**
   * Liest einen Part "raw" ein.
   * 
   * @param   mbPart  MimeBodyPart, der eingelesen werden soll.
   * @return  <code>String</code>-Objekt mit Inhalt des Parts.
   */
  private String readPartRaw(MimeBodyPart mbPart) throws IOException {

    try {

      InputStream contentStream = mbPart.getRawInputStream();
      ByteArrayOutputStream baos = new ByteArrayOutputStream();

      int len = 0;
      byte[] buf = new byte[1024];

      while((len = contentStream.read(buf)) > 0) {

        baos.write(buf, 0, len);
      }

      baos.close();
      contentStream.close();

      return(baos.toString());
    }
    catch(MessagingException me) {

      me.printStackTrace();
      throw(new IOException("Probleme beim Part-Content einlesen: " +
              me.getMessage()));
    }
  }


  // ----------------------------------------------------- oeffentliche Methoden

  /* (non-Javadoc)
   * @see javax.faces.component.UIComponentBase#encodeBegin(javax.faces.context.FacesContext)
   */
  public void encodeBegin(FacesContext context) throws IOException {

    // span-Tag ausgeben
    ResponseWriter writer = context.getResponseWriter();
    writer.startElement("span", this);
    writer.writeAttribute("class", getAttributes().get("styleClass"), null);

    // Part-Content-Object einlesen
    Object objPartContent = null;

    try {

      objPartContent = ((Part)getAttributes().get("value")).getContent();
    }
    catch(Exception e) {

      // Wenn Part ein MimeBodyPart ist, versuchen wir ihn "raw" zu einzulesen.
      Part part = ((Part)getAttributes().get("value"));

      if(part instanceof MimeBodyPart) {

        objPartContent = this.readPartRaw((MimeBodyPart)part);
      }

      // Sonst koennen wir nix tun...
      else {

        e.printStackTrace();
        throw(new IOException("Probleme beim Part-Content einlesen: " +
                e.getMessage()));
      }
    }

    // Wenn Part-Content nicht null, formatieren und ausgeben
    if(objPartContent != null) {

      String partContent = (objPartContent instanceof String) ?
              (String)objPartContent : objPartContent.toString();

      // Part-Content in HTML umwandeln
      partContent = PresentationUtils.htmlEntities(partContent);
      partContent = PresentationUtils.htmlFormat(partContent);

      // URLs durch Links ersetzen
      partContent = PresentationUtils.replaceUrlsWithLinks(partContent,
              (HttpServletRequest)context.getExternalContext().getRequest()); 

      // Part-Content ausgeben
      writer.write(partContent);
    }
  }

  /* (non-Javadoc)
   * @see javax.faces.component.UIComponentBase#encodeEnd(javax.faces.context.FacesContext)
   */
  public void encodeEnd(FacesContext context) throws IOException {

    ResponseWriter writer = context.getResponseWriter();

    writer.endElement("span");
  }

}
