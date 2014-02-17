/*
 * @(#)DisplayMessageAssembler.java 1.00 2006/03/15
 *
 * Copyright (c) 2006, Stephan Sann
 *
 * 15.03.2006 ssann        Vers. 1.0     created
 */


package de.lotk.yawebmail.util;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimePart;

import com.sun.mail.util.BASE64DecoderStream;

import de.lotk.yawebmail.bean.DisplayMessageBean;
import de.lotk.yawebmail.exceptions.MessageRetrieveException;

/**
 * Erzeugt eine Display-Message aus einer javax.mail.Message.
 * 
 * @author Stephan Sann
 * @version 1.0
 */
public class DisplayMessageAssembler {

  // ---------------------------------------------------------- private Methoden

  /**
   * Saeubert den Content-ID-String. Ggf. mussen umschliessende Brackets
   * entfernt werden.
   * 
   * @param   contentID  Zu saeubernde Content-ID
   * @return             <code>String</code> mit gesaeuberter Content-ID 
   */
  private static String cleanContentID(String contentID) {

    String cid = contentID.trim();

    if(cid.startsWith("<") && cid.endsWith(">")) {

      cid = cid.substring(1, (cid.length() - 1));
    }

    return(cid);
  }

  /**
   * Fuegt einen DisplayPart hinzu.
   *  
   * @param   part          Aktuell zu behandelnder Part
   * @param   displayParts  List, in die die DisplayParts einsortiert werden.
   * @param   multiparts    Map, in die Multiparts gepackt werden.
   */
  private static void addDisplayPart(Part part, List<Part> displayParts,
          Map<Multipart, List<Integer>> multiparts) {

    // Wenn dieser Part ein BodyPart (Teil eines Multipart) ist, Parent heraus-
    // finden und DisplayPart-Nummer in multipart-Map merken
    if(part instanceof BodyPart) {
      
      BodyPart actPart = (BodyPart)part;
      Multipart parent = actPart.getParent();

      if(parent != null) {

        // Unter "multipart.get(parent)" muss eine Liste existieren, zu der wir
        // die DisplayPart-Nummer (= Groesse der DisplayPart-List) adden.
        ((List<Integer>)multiparts.get(parent)).add(new Integer(displayParts.size()));
      }
    }

    displayParts.add(part);
  }

  /**
   * Packt die verschachtelten Parts einer Message in eine flache List
   * (DisplayParts) und in eine Map (InlineParts).
   * 
   * @param   part          Aktuell zu behandelnder Part
   * @param   displayParts  List, in die die DisplayParts einsortiert werden.
   * @param   inlineParts   Map, in die die InlineParts gepackt werden.
   * @param   multiparts    Map, in die Multiparts gepackt werden.
   */
  private static void deflateMessageParts(Part part, List<Part> displayParts,
          Map<String, Part> inlineParts, Map<Multipart, List<Integer>>
          multiparts) throws IOException, MessagingException {

    // Versuchen, den Part-Content einlesen
    Object partContent = null;

    try {

      partContent = part.getContent();
    }
    catch(Exception e) {

      // Part scheint kaputt zu sein - sollen sich andere damit rumschlagen. 
      addDisplayPart(part, displayParts, multiparts);
    }

    // Wenn Part-Content MultiPart ist, uns selbst fuer jeden Part rekursiv
    // aufrufen.
    if(partContent instanceof Multipart) {

      Multipart actMultipart = (Multipart)partContent;
      multiparts.put(actMultipart, (new ArrayList<Integer>()));

      for(int tt = 0; tt < actMultipart.getCount(); tt++) {

        deflateMessageParts(actMultipart.getBodyPart(tt), displayParts,
                inlineParts, multiparts);
      }
    }

    // Wenn PartContent vom Typ Message, uns selbst mit PartContent aufrufen.
    // Ausser der Content-Type des Parts(!) ist "text/rfc822-headers", dann darf
    // nicht versucht werden zu deflaten, da hier der PartContent kein Body hat
    // (Exception "Missing start boundary")
    else if((partContent instanceof Message) &&
            (part.getContentType().toLowerCase().indexOf("rfc822-headers") < 0)) {

      deflateMessageParts((Message)partContent, displayParts, inlineParts,
              multiparts);
    }

    // For content-types that are unknown to the DataHandler system, an input
    // stream is returned as the content...
    //
    // BASE64DecoderStreams duerfen nicht in einen String geparst werden!
    else if((partContent instanceof InputStream) &&
            (! (partContent instanceof BASE64DecoderStream))) {

      BufferedInputStream bufReader =
              new BufferedInputStream((InputStream)partContent);
      ByteArrayOutputStream baos = new ByteArrayOutputStream();

      byte[] inBuf = new byte[4096];
      int len = 0;
      while((len = bufReader.read(inBuf)) > 0) {

        baos.write(inBuf, 0, len);
      }
      bufReader.close();

      part.setContent(baos.toString(), part.getContentType());
      addDisplayPart(part, displayParts, multiparts);
    }

    // Ist Part ein InlinePart? Dann in die DisplayParts-List packen.
    // 1.) Part muss MimeBodyPart sein
    // 2.) Part muss eine Content-ID haben.
    // 3.) Parent-Part muss "Content-Type: multipart/related;" haben.
    // 4.) Wenn "Content-Disposition" am Start, muss es "inline" sein.
    else if((part instanceof MimeBodyPart) &&
            (((MimeBodyPart)part).getContentID() != null) &&
            (((MimeBodyPart)part).getContentID().trim().length() >= 1) &&
            (((MimeBodyPart)part).getParent().getContentType() != null) &&
            (((MimeBodyPart)part).getParent().getContentType().toLowerCase().indexOf("multipart/related") >= 0) &&
            ((part.getDisposition() == null) || part.getDisposition().equalsIgnoreCase(Part.INLINE))) {

      inlineParts.put(cleanContentID(((MimePart)part).getContentID()), part);
    }

    // Sonst den aktuellen Part einfach in die DisplayParts-List packen.
    else {

      addDisplayPart(part, displayParts, multiparts);
    }
  }

  /**
   * Erstellt eine DisplayMessageBean, die eine fehlerhafte Mail anzeigt.
   *
   * @param   message         Fehlerhafte Message
   * @param   displayMessage  Aktuelle DisplayMessageBean
   * @param   displayParts    List, in die die DisplayParts einsortiert werden.
   * @param   inlineParts     Map, in die die InlineParts gepackt werden.
   * @param   multiparts      Map, in die Multiparts gepackt werden.
   * @param   e               Exception, die beim Einlesen geflogen ist
   */
  private static void assemblePartsForFaultySourceMessage(Message message,
          DisplayMessageBean displayMessage, List<Part> displayParts, Map
          inlineParts, Map multiparts, Exception e) throws MessagingException {

    // Alle vielleicht schon ansatzweise gefuellten Collections zuruecksetzen
    displayParts.clear();
    inlineParts.clear();
    multiparts.clear();

    // Part erstellen, der auf das Problem hinweist und den Quelltext anfuegt.
    StringBuffer mt = new StringBuffer("Message faulty!\n\n");
    mt.append("The requested messages is faulty because of this reason:\n");
    mt.append(e.getMessage()).append("\n\n");
    mt.append("This is the faulty source of the requested message:\n\n");
    mt.append(displayMessage.getMessageSource());

    // Info-Text-Message erstellen
    Message infoMessage = new MimeMessage((Session)null);
    infoMessage.setText(mt.toString());

    // Info-Text-Message in die Display-Parts packen
    displayParts.add(infoMessage);
  }


  // ----------------------------------------------------- oeffentliche Methoden

  /**
   * Baut aus einem Message-Objekt ein DisplayMessage-Objekt.
   * 
   * @param     message   Message, aus der das DisplayMessage-Objekt gebaut
   *                      werden soll.
   */
  public static DisplayMessageBean assembleDisplayMessage(Message message)
          throws MessageRetrieveException {

    DisplayMessageBean displayMessage = new DisplayMessageBean();

    return(refurbishGivenDisplayMessage(displayMessage, message));
  }

  /**
   * Setzt die Properties einen existierenden DisplayMessage-Objektes aus der
   * uebergebenen javax.mail.Message.
   *
   * @param   displayMessage  Bereits existierende Display-Message
   * @param   message         Message, aus der das DisplayMessage-Objekt gebaut
   *                          werden soll.
   */
  public static DisplayMessageBean
          refurbishGivenDisplayMessage(DisplayMessageBean displayMessage,
          Message message) throws MessageRetrieveException {

    displayMessage.setOriginMessage(message);

    // Parts aus der Message in eine flache ArrayList (DisplayParts) und eine
    // HashMap (InlineParts) packen. Ausserdem die Parts von Multipart-Objekten
    // in einer Map merken (Key: Multipart-Objekt / Value: List)
    List<Part> displayParts = new ArrayList<Part>();
    Map<String, Part> inlineParts = new HashMap<String, Part>();
    Map<Multipart, List<Integer>> multiparts =
            new HashMap<Multipart, List<Integer>>();

    try {

      deflateMessageParts(message, displayParts, inlineParts, multiparts);
    }
    catch(Exception e) {

      try {

        assemblePartsForFaultySourceMessage(message, displayMessage,
                displayParts, inlineParts, multiparts, e);
      }
      catch(MessagingException me) {

        e.printStackTrace();
        me.printStackTrace();
        throw(new MessageRetrieveException("Konnte Message-Parts nicht beziehen",
                e));
      }
    }

    displayMessage.setDisplayParts(displayParts);
    displayMessage.setInlineParts(inlineParts);
    displayMessage.setMultiparts(multiparts);

    return(displayMessage);
  }

}
