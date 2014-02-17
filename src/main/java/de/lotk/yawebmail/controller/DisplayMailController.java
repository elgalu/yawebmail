/*
 * @(#)DisplayMailController.java 1.00 2006/04/03
 *
 * Copyright (c) 2006, Stephan Sann
 *
 * 03.04.2006 ssann        Vers. 1.0     created
 */


package de.lotk.yawebmail.controller;

import java.io.BufferedInputStream;
import java.io.OutputStream;
import java.util.Map;

import javax.faces.context.FacesContext;
import javax.mail.Part;
import javax.mail.internet.ContentType;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.lotk.yawebmail.application.Constants;
import de.lotk.yawebmail.bean.DisplayMessageBean;
import de.lotk.yawebmail.bean.MailBasisBean;
import de.lotk.yawebmail.bean.SessionContainerBean;
import de.lotk.yawebmail.business.MailboxConnection;
import de.lotk.yawebmail.exceptions.AccessDeniedException;
import de.lotk.yawebmail.exceptions.ConnectionEstablishException;
import de.lotk.yawebmail.exceptions.LogoutException;
import de.lotk.yawebmail.exceptions.MailboxFolderException;
import de.lotk.yawebmail.exceptions.MessageDeletionException;
import de.lotk.yawebmail.util.PresentationUtils;
import de.lotk.yawebmail.util.ReactionMailBasisAssembler;
import de.lotk.yawebmail.util.faces.ExceptionConverter;

/**
 * Controller fuer das Display-Mail-Aktionen 
 * 
 * @author Stephan Sann
 * @version 1.0
 */
public class DisplayMailController extends BaseMailboxActionController {

  // ----------------------------------------------------------- private methods

  /**
   * Prepare a re-mail and forwards to the create-mail-page.
   * 
   * @return  <code>String</code>-object with info were to navigate to. 
   */
  private String createReMail(boolean responseToAll) {

    FacesContext facesContext = FacesContext.getCurrentInstance();

    try {

      // MailBasis- und DisplayMessage-Objekt aus der Session holen
      MailBasisBean mailBasis =
              (MailBasisBean)this.getManagedBeanByName(Constants.NAME_MBEAN_MAILBASIS);
      DisplayMessageBean displayMessage =
              (DisplayMessageBean)this.getManagedBeanByName(Constants.NAME_MBEAN_DISPLAYMESSAGE);

      // MailBasis-Objekt als ReMail vorbefuellen
      ReactionMailBasisAssembler.refurbishGivenResponseMailBasis(mailBasis,
              displayMessage, responseToAll);
    }
    catch(Exception e) {

      facesContext.addMessage(null,
              ExceptionConverter.getFacesMessage(facesContext, e, true));
      return(Constants.OUTCOME_TECH_ERROR);
    }

    return(Constants.OUTCOME_CREATE_MAIL);
  }


  // ------------------------------------------------------------ public methods

  /**
   * Versucht den Content eines Mail-Parts auszugeben.
   * 
   * @return          <code>String</code>-Objekt mit Info, wohin navigiert wird. 
   */
  public String displayPartContent() {

    FacesContext facesContext = FacesContext.getCurrentInstance();
    Map params = facesContext.getExternalContext().getRequestParameterMap();

    // Aus dem Request den Parameter "partNumber" auslesen
    if(params.containsKey(Constants.REQ_PARAM_PART_NUMBER)) {

      try {

        // DisplayMailBean, gewuenschten Part und Response-Objekt holen
        DisplayMessageBean displayMessage =
                (DisplayMessageBean)this.getManagedBeanByName(Constants.NAME_MBEAN_DISPLAYMESSAGE);
        int partNumber =
                Integer.parseInt((String)params.get(Constants.REQ_PARAM_PART_NUMBER));
        HttpServletResponse response =
                (HttpServletResponse)facesContext.getExternalContext().getResponse();

        // Part holen und in Response schreiben
        Part part = (Part)displayMessage.getDisplayParts().get(partNumber);

        ContentType contentType = new ContentType(part.getContentType());
        response.setContentType(contentType.getBaseType());

        // Wenn PartContent = String, Links mit Derefer-Link ersetzen und
        // Verweise auf InlineParts ersetzen
        if(part.getContent() instanceof String) {

          HttpServletRequest request =
                  (HttpServletRequest)facesContext.getExternalContext().getRequest();
          String outStr =
                  PresentationUtils.dereferLinks((String)part.getContent(),
                  request);
          outStr = PresentationUtils.replaceContentIds(outStr, request, response);

          response.setContentLength(outStr.length());
          response.getWriter().write(outStr);
        }

        // Sonst den PartContent in den Response-Outputstream schreiben
        else {

          StringBuffer contentDisposition = new StringBuffer();
          contentDisposition.append("attachment; filename=\"");
          contentDisposition.append(part.getFileName()).append("\"");

          response.setHeader("Content-disposition", contentDisposition.toString());
          response.setContentLength(part.getSize());

          BufferedInputStream bufInputStream =
                  new BufferedInputStream(part.getInputStream());
          OutputStream outputStream = response.getOutputStream();

          byte[] inBuf = new byte[4096];
          int len = 0;
          while((len = bufInputStream.read(inBuf)) > 0) {

            outputStream.write(inBuf, 0, len);
          }
        }

        // Faces mitteilen, dass der Response "durch" ist.
        facesContext.responseComplete();

        // Faces "im Sinn" wieder auf die Display-Mail-Seite leiten.
        return(Constants.OUTCOME_DISPLAY_MAIL);
      }
      catch(Exception e) {

        facesContext.addMessage(null,
                ExceptionConverter.getFacesMessage(facesContext, e, true));
        return(Constants.OUTCOME_TECH_ERROR);
      }
    }
    else {

      facesContext.addMessage(null,
              ExceptionConverter.getFacesMessage(facesContext, new
              Exception("Parameter \"partNumber\" nicht vorhanden."), true));
      return(Constants.OUTCOME_TECH_ERROR);
    }
  }

  /**
   * Bereitet eine Re-Mail vor und leitet an die Mail-erstellen-Seite.
   * 
   * @return          <code>String</code>-Objekt mit Info, wohin navigiert wird. 
   */
  public String createReMail() {

    return(this.createReMail(false));
  }

  /**
   * Bereitet eine Re-Mail an alle vor und leitet an die Mail-erstellen-Seite.
   * 
   * @return          <code>String</code>-Objekt mit Info, wohin navigiert wird. 
   */
  public String createReToAllMail() {

    return(this.createReMail(true));
  }

  /**
   * Bereitet eine Forward-Mail vor und leitet an die Mail-erstellen-Seite.
   * 
   * @return          <code>String</code>-Objekt mit Info, wohin navigiert wird. 
   */
  public String createFwdMail() {

    FacesContext facesContext = FacesContext.getCurrentInstance();

    try {

      // MailBasis- und DisplayMessage-Objekt aus der Session holen
      MailBasisBean mailBasis =
              (MailBasisBean)this.getManagedBeanByName(Constants.NAME_MBEAN_MAILBASIS);
      DisplayMessageBean displayMessage =
              (DisplayMessageBean)this.getManagedBeanByName(Constants.NAME_MBEAN_DISPLAYMESSAGE);

      // MailBasis-Objekt als ReMail vorbefuellen
      ReactionMailBasisAssembler.refurbishGivenForwardMailBasis(mailBasis,
              displayMessage);
    }
    catch(Exception e) {

      facesContext.addMessage(null,
              ExceptionConverter.getFacesMessage(facesContext, e, true));
      return(Constants.OUTCOME_TECH_ERROR);
    }

    return(Constants.OUTCOME_CREATE_MAIL);
  }

  /**
   * Versucht die aktuelle Mail zu loeschen
   * 
   * @return          <code>String</code>-Objekt mit Info, wohin navigiert wird. 
   */
  public String deleteMail() {

    FacesContext facesContext = FacesContext.getCurrentInstance();

    try {

      // DisplayMessage-Objekt aus der Session holen
      DisplayMessageBean displayMessage =
              (DisplayMessageBean)this.getManagedBeanByName(Constants.NAME_MBEAN_DISPLAYMESSAGE);

      // Message-Number der aktuellen Display-Message holen
      int messageNumber = displayMessage.getOriginMessage().getMessageNumber();

      // MailboxConnection und aktuellen Mailbox-Folder aus der Session holen
      MailboxConnection mailboxConnection = this.getMailboxConnection();
      String currentMailboxFolder = this.getCurrentMailboxFolder();

      // Mail loeschen
      try {

        mailboxConnection.login(currentMailboxFolder);
        mailboxConnection.setDeletedFlag(messageNumber);
        mailboxConnection.logout();

        // Nach dem Loeschen sind die gecachten Envelopes nicht mehr aktuell.
        SessionContainerBean sessionContainer =
                (SessionContainerBean)this.getManagedBeanByName(Constants.NAME_MBEAN_SESSIONCONTAINER);
        sessionContainer.setRenewEnvelopes(true);
      }
      catch(ConnectionEstablishException cee) {

        facesContext.addMessage(Constants.CLIENT_ID_MAILBOXHOST,
                ExceptionConverter.getFacesMessage(facesContext, cee, false));
        return(Constants.OUTCOME_LOGON);
      }
      catch(AccessDeniedException ade) {

        facesContext.addMessage(Constants.CLIENT_ID_MAILBOXPASSWORD,
                ExceptionConverter.getFacesMessage(facesContext, ade, false));
        return(Constants.OUTCOME_LOGON);
      }
      catch(MailboxFolderException mfe) {

        facesContext.addMessage(null,
                ExceptionConverter.getFacesMessage(facesContext, mfe, true));
        return(Constants.OUTCOME_TECH_ERROR);
      }
      catch(MessageDeletionException mde) {

        facesContext.addMessage(null,
                ExceptionConverter.getFacesMessage(facesContext, mde, true));
        return(Constants.OUTCOME_TECH_ERROR);
      }
      catch(LogoutException le) {

        facesContext.addMessage(null,
                ExceptionConverter.getFacesMessage(facesContext, le, true));
        return(Constants.OUTCOME_TECH_ERROR);
      }
      finally {

        try {

          mailboxConnection.logout();
        }
        catch(LogoutException le) {

          // empty;
        }
      }
    }
    catch(Exception e) {

      facesContext.addMessage(null,
              ExceptionConverter.getFacesMessage(facesContext, e, true));
      return(Constants.OUTCOME_TECH_ERROR);
    }

    // Wenn wir hier angelangt sind, scheint alles i.O. zu sein.
    return(Constants.OUTCOME_MAILS_LISTING);
  }

}
