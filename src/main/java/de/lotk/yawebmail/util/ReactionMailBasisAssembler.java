/*
 * @(#)ReactionMailBasisAssembler.java 1.00 2006/04/21
 *
 * Copyright (c) 2006, Stephan Sann
 *
 * 21.04.2006 ssann        Vers. 1.0     created
 */


package de.lotk.yawebmail.util;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;

import javax.mail.Address;
import javax.mail.Header;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Message.RecipientType;
import javax.mail.internet.ContentType;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;

import de.lotk.yawebmail.application.Constants;
import de.lotk.yawebmail.bean.DisplayMessageBean;
import de.lotk.yawebmail.bean.MailBasisBean;

/**
 * Erstellt die Grundlage einer Reaktions-Mail (Response oder Forward).
 * 
 * @author Stephan Sann
 * @version 1.0
 */
public class ReactionMailBasisAssembler {

  // ---------------------------------------------------------------- Konstanten

  /** Response */
  public static final int REACTIONTYPE_RESPONSE = 1;

  /** Forward */
  public static final int REACTIONTYPE_FORWARD = 2;

  /** Response-Kuerzel */
  public static final String RESPONSE_PREFIX = "Re: ";

  /** Forward */
  public static final String FORWARD_PREFIX = "Fwd: ";


  // ---------------------------------------------------------- private Methoden

  /**
   * Quotet einen uebergebenen Text.
   * 
   * @param     txt  Zu quotener Text.
   * @return          <code>String</code> mit gequoteten Text. 
   */
  private static String quoteText(String txt) throws Exception {

    StringBuffer quoteText = new StringBuffer();
    String actLine = null;

    // Text zeilenweise einlesen und quoten
    BufferedReader bufReader = new BufferedReader(new StringReader(txt));

    while((actLine = bufReader.readLine()) != null) {

      quoteText.append('>');

      // Wenn Zeile nicht leer und bereits vorher gequotet, noch ein Blank.
      if((actLine.length() >= 1) && (actLine.charAt(0) != '>')) {

        quoteText.append(' ');
      }

      quoteText.append(actLine).append("\n");
    }

    bufReader.close();

    return(quoteText.toString());
  }

  /**
   * Baut ein Reaction-Subject zusammen.
   * 
   * @param   subject       Subject des Urspungsmails
   * @param   reactionType  Typ der Reaktion
   * @return                <code>String</code> mit assembleten Reaction-Subj.
   */
  private static String assembleReactionSubject(String subject, int
          reactionType) {

    if(reactionType == REACTIONTYPE_RESPONSE) {

      if((subject != null) &&
              subject.toLowerCase().startsWith(RESPONSE_PREFIX.toLowerCase())) {

        return(subject);
      }
      else {

        return(RESPONSE_PREFIX +
                ((subject != null) ? subject : Constants.LEERSTRING));
      }
    }
    else {

      if((subject != null) &&
              subject.toLowerCase().startsWith(FORWARD_PREFIX.toLowerCase())) {

        return(subject);
      }
      else {

        return(FORWARD_PREFIX +
                ((subject != null) ? subject : Constants.LEERSTRING));
      }
    }
  }

  /**
   * Erstellt aus einem Array mit Mail-Adressen ein Address-String.
   * 
   * @param    addressesArray  Array mit Mail-Adressen
   * @return                   Komma-separierter <code>String</code> mit
   *                           Adressen aus Array.
   */
  private static String getAddressesStringFromArray(Address[] addressesArray) {

    StringBuffer allAddresses = new StringBuffer();
    String kommaSeparator = ", ";

    for(int ii = 0; ii < addressesArray.length; ii++) {

      // nach jeder Adresse ein Komma setzen (vor erster nicht!)
      if(ii >= 1) {

        allAddresses.append(kommaSeparator);
      }

      allAddresses.append(((InternetAddress)addressesArray[ii]).toUnicodeString());
    }

    return(allAddresses.toString());
  }

  /**
   * Quotet die Texte aus einer DisplayMessage.
   * 
   * @param  displayMessage  DisplayMessage, aus d. d. Quote-Text erstellt wird.
   * @return                 <code>String</code> mit assembleten Quote-Text.
   */
  private static String assembleQuoteText(DisplayMessageBean displayMessage)
          throws Exception {

    StringBuffer quoteText = new StringBuffer();
    List displayParts = displayMessage.getDisplayParts(); 

    // Alle DisplayParts durchlaufen und Texte quoten
    for(int ii = 0; ii < displayParts.size(); ii++) {

      Part actPart = (Part)displayParts.get(ii);
      String partContentType =
              (new ContentType(actPart.getContentType())).getBaseType();

      // TODO multipart/alternative beachten / HTML-Parts quoten
      if(partContentType.equalsIgnoreCase("text/plain")) {

        quoteText.append(quoteText((String)actPart.getContent()));
        quoteText.append("\n\n");
      }
    }

    return(quoteText.toString());
  }

  /**
   * Erstellt aus einem uebergebenen String einen Filenamen.
   * 
   * @param   source  Quelle, aus der der Dateiname erstellt wird.
   * @return          <code>String</code> mit Dateinamen.
   */
  private static String assembleFilename(String source) {

    if((source == null) || (source.length() < 1)) {

      source = "message";
    }

    return(source.replaceAll("[ /\\\\><\\*\\.\\?\"'\\|:;]", "_") + ".eml");
  }

  /**
   * Erstellt einen MimeBodyPart aus einer Message.
   * 
   * @param   message  Message, aus der der MimeBodyPart erstellt wird.
   * @return           <code>MimeBodyPart</code>
   */
  private static MimeBodyPart assembleMimeBodyPart(Message message) throws
          Exception {

    MimeBodyPart mbp = new MimeBodyPart();
    Object messageContent = message.getContent(); 

    // Wenn die Message == MimeMessage, Header auf den MimeBodyPart uebertragen.
    if(message instanceof MimeMessage) {

      Enumeration allHeaders = ((MimeMessage)message).getAllHeaders();
      
      while(allHeaders.hasMoreElements()) {

        Header actHeader = (Header)allHeaders.nextElement();
        mbp.setHeader(actHeader.getName(), actHeader.getValue());
      }
    }

    // Content des MimeBodyParts setzen
    if(messageContent instanceof Multipart) {

      mbp.setContent((Multipart)messageContent);
    }
    else {

      mbp.setContent(messageContent, message.getContentType());
    }

    // Jetzt vergeben wir noch einen Filename fuer den Part (falls noch nicht
    // vorhanden (bei Messages unwahrscheinlich, aber sicher ist sicher)).
    if(mbp.getFileName() == null) {

      mbp.setFileName(assembleFilename(message.getSubject()));
    }

    return(mbp);
  }

  /**
   * Determines all TO-recipients (in case of "reply to all")
   * 
   * @param   originMessage  The origin message
   * @return  <code>Address</code>-array containing all TO-recipients
   */
  private static Address[] getToRecipientsForReplyToAll(Message originMessage)
          throws Exception {

    List<Address> rcptToList = new ArrayList<Address>();

    // add reply-to-address(es) of the origin message
    Address[] originReplyTo = originMessage.getReplyTo();

    if((originReplyTo != null) && (originReplyTo.length >= 1)) {

      rcptToList.addAll(Arrays.asList(originReplyTo));
    }

    // add to-address(es) of the origin message
    Address[] originToRcpts = originMessage.getRecipients(RecipientType.TO);

    if((originToRcpts != null) && (originToRcpts.length >= 1)) {

      for(Address rcptToAddress : originToRcpts) {

        if(! rcptToList.contains(rcptToAddress)) {

          rcptToList.add(rcptToAddress);
        }
      }
    }

    // convert list to array and return
    return(rcptToList.toArray(new Address[rcptToList.size()]));
  }


  // ----------------------------------------------------- oeffentliche Methoden

  /**
   * Baut aus einem DisplayMessage-Objekt ein Response-MailBasis-Objekt.
   * 
   * @param   displayMessage  DisplayMessage, aus der das MailBasis-Objekt
   *                          gebaut werden soll.
   * @param   responseToAll   Soll Response an alle gehen?
   */
  public static MailBasisBean assembleResponseMailBasis(DisplayMessageBean
          displayMessage, boolean responseToAll) throws Exception {

    MailBasisBean mailBasis = new MailBasisBean();

    return(refurbishGivenResponseMailBasis(mailBasis, displayMessage,
            responseToAll));
  }

  /**
   * Setzt die Properties einen existierenden MailBasis-Objektes aus dem
   * uebergebenen DisplayMessage-Objekt.
   *
   * @param   mailBasis       Bereits existierendes MailBasis-Objekt
   * @param   displayMessage  DisplayMessage, aus der das MailBasis-Objekt
   *                          gebaut werden soll.
   * @param   responseToAll   Soll Response an alle gehen?
   */
  public static MailBasisBean refurbishGivenResponseMailBasis(MailBasisBean
          mailBasis, DisplayMessageBean displayMessage, boolean responseToAll)
          throws Exception {

    // Origin-Message aus Display-Message holen
    Message originMessage = displayMessage.getOriginMessage();

    // Properties der MailBasis setzen
    mailBasis.setSubject(assembleReactionSubject(originMessage.getSubject(),
            REACTIONTYPE_RESPONSE));
    mailBasis.setMailText(assembleQuoteText(displayMessage));

    // Bei Mime-Messages auch die Message-ID setzen
    if(originMessage instanceof MimeMessage) {

      mailBasis.setInReplyTo(((MimeMessage)originMessage).getMessageID());
    }

    // Response to all?
    if(responseToAll) {

      // TO-recipients
      Address[] rcptToArray = getToRecipientsForReplyToAll(originMessage);
      mailBasis.setRcptTo(getAddressesStringFromArray(rcptToArray));

      // CC-recipients
      Address[] originCcRcpts = originMessage.getRecipients(RecipientType.CC);

      if((originCcRcpts != null) && (originCcRcpts.length >= 1)) {

        mailBasis.setRcptCc(getAddressesStringFromArray(originCcRcpts));
      }
    }

    // Response to sender (or "reply-to"-address) only
    else {

      mailBasis.setRcptTo(getAddressesStringFromArray(originMessage.getReplyTo()));
    }

    return(mailBasis);
  }

  /**
   * Baut aus einem DisplayMessage-Objekt ein Forward-MailBasis-Objekt.
   * 
   * @param   displayMessage  DisplayMessage, aus der das MailBasis-Objekt
   *                          gebaut werden soll.
   */
  public static MailBasisBean assembleForwardMailBasis(DisplayMessageBean
          displayMessage) throws Exception {

    MailBasisBean mailBasis = new MailBasisBean();

    return(refurbishGivenForwardMailBasis(mailBasis, displayMessage));
  }

  /**
   * Setzt die Properties einen existierenden MailBasis-Objektes aus dem
   * uebergebenen DisplayMessage-Objekt.
   *
   * @param   mailBasis       Bereits existierendes MailBasis-Objekt
   * @param   displayMessage  DisplayMessage, aus der das MailBasis-Objekt
   *                          gebaut werden soll.
   */
  public static MailBasisBean refurbishGivenForwardMailBasis(MailBasisBean
          mailBasis, DisplayMessageBean displayMessage) throws Exception {

    // Origin-Message aus Display-Message holen
    Message originMessage = displayMessage.getOriginMessage();

    // Properties der MailBasis setzen
    mailBasis.setSubject(assembleReactionSubject(originMessage.getSubject(),
            REACTIONTYPE_FORWARD));
    mailBasis.getAttachments().add(assembleMimeBodyPart(originMessage));

    return(mailBasis);
  }

}
