/*
 * @(#)ExceptionConverter.java 1.00 2006/02/21
 *
 * Copyright (c) 2006, Stephan Sann
 *
 * 21.02.2006 ssann        Vers. 1.0     created
 */


package de.lotk.yawebmail.util.faces;

import java.net.SocketException;
import java.util.Locale;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.mail.SendFailedException;
import javax.mail.internet.AddressException;

import de.lotk.yawebmail.application.Constants;
import de.lotk.yawebmail.exceptions.AccessDeniedException;
import de.lotk.yawebmail.exceptions.ConnectionEstablishException;
import de.lotk.yawebmail.exceptions.MailboxFolderException;
import de.lotk.yawebmail.exceptions.MessageRetrieveException;
import de.lotk.yawebmail.exceptions.SessionExpiredException;
import de.lotk.yawebmail.util.MessageMapper;


/**
 * Konvertiert Exceptions in Faces-Messages
 * 
 * @author Stephan Sann
 * @version 1.0
 */
public class ExceptionConverter {

  // ---------------------------------------------------------- private Methoden

  /**
   * Erstellt einen Message-Text fuer den uebergebenen Resource-Key aus der zu
   * der aktuellen Locale passenden Resource-Datei.
   * 
   * @param   facesContext  Aktueller FacesContext
   * @param   resourceKey   Schluessel fuer den Text aus der Resource-Datei
   * @param   params        Array mit Erstetzungs-Werten (z.B. "min {0} chars")
   * @return  <code>String</code> mit Message-Text
   */
  private static String assembleMessageText(FacesContext facesContext, String
          resourceKey, Object[] params) {

    String nameMessageBundle = facesContext.getApplication().getMessageBundle();
    Locale locale = facesContext.getViewRoot().getLocale();

    String messageText =
            MessageMapper.getMessageResourceString(nameMessageBundle,
            resourceKey, params, locale);

    return(messageText);
  }

  /**
   * Erstellt eine FacesMessage fuer den uebergebenen Message-Text. Auf Wunsch
   * wird der Stacktrace der uebergebenen Exception in die FacesMessage
   * geschrieben.
   * 
   * @param   messageText  Message-Text, der in der FacesMessage erscheint.
   * @param   stackTrace   Soll der Stacktrace der Exception in die FacesMessage
   *                       geschrieben werden?
   * @param   exception    Exception, deren Stacktrace ggf. in die FacesMessage
   *                       geschrieben wird.
   * @return  <code>FacesMessage</code> gemaess Uebergabe-Parametern.
   */
  private static FacesMessage assembleFacesMessage(String messageText, boolean
          stackTrace, Exception exception) {

    // Stacktrace in den detailierten Message-Text schreiben? 
    if(stackTrace) {

      StackTraceElement[] stackTraceElements = exception.getStackTrace();
      StringBuffer outputStack =
              (new StringBuffer(exception.getClass().getName())).append("\n\n");

      // exception.getMessage() kann evtl. NULL sein
      if(exception.getMessage() != null) {

        outputStack.append(exception.getMessage()).append("\n\n");
      }

      // Alle Stacktrace-Elemente in den StringBuffer "outputStack" schreiben
      for(int ii = 0; ii < stackTraceElements.length; ii++) {

        outputStack.append(stackTraceElements[ii].toString());
        outputStack.append("\n");
      }

      return(new FacesMessage(FacesMessage.SEVERITY_ERROR, messageText,
              outputStack.toString()));
    }

    // Sonst sind zusammengefasster und detailierter Message-Text gleich.
    else {
      
      return(new FacesMessage(FacesMessage.SEVERITY_ERROR, messageText,
              messageText));
    }
  }


  // ----------------------------------------------------- oeffentliche Methoden

  /**
   * Liefert eine FacesMessage fuer eine ConnectionEstablishException.
   * 
   * @param   facesContext  Akteller FacesContext
   * @param   cee           Die ConnectionEstablishException
   * @param   stackTrace    Soll der Stacktrace der Exception in die
   *                        FacesMessage geschrieben werden?
   * @return  <code>FacesMessage</code> gemaess Uebergabe-Parameter
   */
  public static FacesMessage getFacesMessage(FacesContext facesContext,
          ConnectionEstablishException cee, boolean stackTrace) {

    String[] replacements = new String[2];
    replacements[0] = cee.getHost();
    replacements[1] = Integer.toString(cee.getPort());

    String messageText = assembleMessageText(facesContext,
            Constants.PROPERTY_KEY_EXCEPTION_NO_CONNECTION, replacements);

    return(assembleFacesMessage(messageText, stackTrace, cee));
  }

  /**
   * Liefert eine FacesMessage fuer eine AccessDeniedException.
   * 
   * @param   facesContext  Akteller FacesContext
   * @param   ade           Die AccessDeniedException
   * @param   stackTrace    Soll der Stacktrace der Exception in die
   *                        FacesMessage geschrieben werden?
   * @return  <code>FacesMessage</code> gemaess Uebergabe-Parameter
   */
  public static FacesMessage getFacesMessage(FacesContext facesContext,
          AccessDeniedException ade, boolean stackTrace) {

    String messageText = assembleMessageText(facesContext,
            Constants.PROPERTY_KEY_EXCEPTION_ACCESS_DENIED, null);

    return(assembleFacesMessage(messageText, stackTrace, ade));
  }

  /**
   * Liefert eine FacesMessage fuer eine MailboxFolderException.
   * 
   * @param   facesContext  Akteller FacesContext
   * @param   mfe           Die MailboxFolderException
   * @param   stackTrace    Soll der Stacktrace der Exception in die
   *                        FacesMessage geschrieben werden?
   * @return  <code>FacesMessage</code> gemaess Uebergabe-Parameter
   */
  public static FacesMessage getFacesMessage(FacesContext facesContext,
          MailboxFolderException mfe, boolean stackTrace) {

    String[] replacements = new String[1];
    replacements[0] = mfe.getFolderName();

    String messageText = assembleMessageText(facesContext,
            Constants.PROPERTY_KEY_EXCEPTION_MAILBOXFOLDER, replacements);

    return(assembleFacesMessage(messageText, stackTrace, mfe));
  }

  /**
   * Liefert eine FacesMessage fuer eine MessageRetrieveException.
   * 
   * @param   facesContext  Akteller FacesContext
   * @param   mre           Die MessageRetrieveException
   * @param   stackTrace    Soll der Stacktrace der Exception in die
   *                        FacesMessage geschrieben werden?
   * @return  <code>FacesMessage</code> gemaess Uebergabe-Parameter
   */
  public static FacesMessage getFacesMessage(FacesContext facesContext,
          MessageRetrieveException mre, boolean stackTrace) {

    String messageText = assembleMessageText(facesContext,
            Constants.PROPERTY_KEY_EXCEPTION_MESSAGE_RETRIEVE, null);

    return(assembleFacesMessage(messageText, stackTrace, mre));
  }

  /**
   * Liefert eine FacesMessage fuer eine InstantiationException.
   * 
   * @param   facesContext  Akteller FacesContext
   * @param   ie            Die InstantiationException
   * @param   stackTrace    Soll der Stacktrace der Exception in die
   *                        FacesMessage geschrieben werden?
   * @return  <code>FacesMessage</code> gemaess Uebergabe-Parameter
   */
  public static FacesMessage getFacesMessage(FacesContext facesContext,
          InstantiationException ie, boolean stackTrace) {

    String messageText = assembleMessageText(facesContext,
            Constants.PROPERTY_KEY_EXCEPTION_INSTANTIATION, null);

    return(assembleFacesMessage(messageText, stackTrace, ie));
  }

  /**
   * Liefert eine FacesMessage fuer eine AddressException.
   * 
   * @param   facesContext  Akteller FacesContext
   * @param   ae            Die AddressException
   * @param   stackTrace    Soll der Stacktrace der Exception in die
   *                        FacesMessage geschrieben werden?
   * @return  <code>FacesMessage</code> gemaess Uebergabe-Parameter
   */
  public static FacesMessage getFacesMessage(FacesContext facesContext,
          AddressException ae, boolean stackTrace) {

    String[] replacements = new String[2];
    replacements[0] = ae.getRef();
    replacements[1] = Integer.toString(ae.getPos());

    String messageText = assembleMessageText(facesContext,
            Constants.PROPERTY_KEY_EXCEPTION_ADDRESS, replacements);

    return(assembleFacesMessage(messageText, stackTrace, ae));
  }

  /**
   * Liefert eine FacesMessage fuer eine SendFailedException.
   * 
   * @param   facesContext  Akteller FacesContext
   * @param   sfe           Die SendFailedException
   * @param   stackTrace    Soll der Stacktrace der Exception in die
   *                        FacesMessage geschrieben werden?
   * @return  <code>FacesMessage</code> gemaess Uebergabe-Parameter
   */
  public static FacesMessage getFacesMessage(FacesContext facesContext,
          SendFailedException sfe, boolean stackTrace) {

    String messageText = assembleMessageText(facesContext,
            Constants.PROPERTY_KEY_EXCEPTION_MAIL_COULD_NOT_BE_SEND, null);
    return(assembleFacesMessage(messageText, stackTrace, sfe));
  }

  /**
   * Liefert eine FacesMessage fuer eine SocketException.
   * 
   * @param   facesContext  Akteller FacesContext
   * @param   se            Die SocketException
   * @param   stackTrace    Soll der Stacktrace der Exception in die
   *                        FacesMessage geschrieben werden?
   * @return  <code>FacesMessage</code> gemaess Uebergabe-Parameter
   */
  public static FacesMessage getFacesMessage(FacesContext facesContext,
          SocketException se, boolean stackTrace) {

    String messageText = assembleMessageText(facesContext,
            Constants.PROPERTY_KEY_EXCEPTION_CONNECTION_COULD_NOT_BE_ESTABLISHED,
            null);
    return(assembleFacesMessage(messageText, stackTrace, se));
  }

  /**
   * Liefert eine FacesMessage fuer eine SessionExpiredException.
   * 
   * @param   facesContext  Akteller FacesContext
   * @param   see           Die SessionExpiredException
   * @param   stackTrace    Soll der Stacktrace der Exception in die
   *                        FacesMessage geschrieben werden?
   * @return  <code>FacesMessage</code> gemaess Uebergabe-Parameter
   */
  public static FacesMessage getFacesMessage(FacesContext facesContext,
          SessionExpiredException see, boolean stackTrace) {

    String messageText = assembleMessageText(facesContext,
            Constants.PROPERTY_KEY_EXCEPTION_SESSION_EXPIRED, null);
    return(assembleFacesMessage(messageText, stackTrace, see));
  }

  /**
   * Liefert eine FacesMessage fuer eine allgemeine Exception.
   * 
   * @param   facesContext  Akteller FacesContext
   * @param   e             Die Exception
   * @param   stackTrace    Soll der Stacktrace der Exception in die
   *                        FacesMessage geschrieben werden?
   * @return  <code>FacesMessage</code> gemaess Uebergabe-Parameter
   */
  public static FacesMessage getFacesMessage(FacesContext facesContext,
          Exception e, boolean stackTrace) {

    String messageText = assembleMessageText(facesContext,
            Constants.PROPERTY_KEY_EXCEPTION_TECHNICAL_ERROR, null);
    return(assembleFacesMessage(messageText, stackTrace, e));
  }

  /**
   * Liefert eine FacesMessage fuer ein Throwable-Objekt.
   * 
   * @param   facesContext  Akteller FacesContext
   * @param   t             Das Throwable-Objekt
   * @param   stackTrace    Soll der Stacktrace des Throwable in die
   *                        FacesMessage geschrieben werden?
   * @return  <code>FacesMessage</code> gemaess Uebergabe-Parameter
   */
  public static FacesMessage getFacesMessage(FacesContext facesContext,
          Throwable t, boolean stackTrace) {

    if(t instanceof Exception) {

      return(getFacesMessage(facesContext, (Exception)t, stackTrace));
    }
    else {

      Exception e = new Exception(t);
      return(getFacesMessage(facesContext, e, stackTrace));
    }
  }

}
