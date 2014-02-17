/*
 * @(#)RenderResponsePhaseListener.java 1.00 2006/02/21
 *
 * Copyright (c) 2006, Stephan Sann
 *
 * 21.02.2006 ssann        Vers. 1.0     created
 */


package de.lotk.yawebmail.listener;

import java.util.Arrays;

import javax.faces.application.ViewHandler;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import javax.mail.Message;
import javax.mail.internet.MimeMessage;

import de.lotk.yawebmail.application.Constants;
import de.lotk.yawebmail.bean.CheckCertsBean;
import de.lotk.yawebmail.bean.DisplayMessageBean;
import de.lotk.yawebmail.bean.FolderWrapperBean;
import de.lotk.yawebmail.bean.RetrieveMessagesResultBean;
import de.lotk.yawebmail.bean.SessionContainerBean;
import de.lotk.yawebmail.business.MailboxConnection;
import de.lotk.yawebmail.controller.CheckCertsController;
import de.lotk.yawebmail.exceptions.AccessDeniedException;
import de.lotk.yawebmail.exceptions.ConnectionEstablishException;
import de.lotk.yawebmail.exceptions.LogoutException;
import de.lotk.yawebmail.exceptions.YawebmailCertificateException;
import de.lotk.yawebmail.util.DisplayMessageAssembler;
import de.lotk.yawebmail.util.OfflineMessageAssembler;
import de.lotk.yawebmail.util.faces.ExceptionConverter;
import de.lotk.yawebmail.util.faces.ManagedBeanUtils;

/**
 * Phase-Listener fuer die Render-Response-Phase
 * 
 * @author Stephan Sann
 * @version 1.0
 */
public class RenderResponsePhaseListener extends BaseListener implements
        PhaseListener {

  // ---------------------------------------------------------------- Konstanten

  /** serialVersionUID */
  private static final long serialVersionUID = 6690767913768137935L;


  // ---------------------------------------------------------- private Methoden

  /**
   * Schreibt die Envelopes der auf der Server befindlichen Mails in die managed
   * Bean "FolderWrapperBean".
   * 
   * @param   sessionContainer  Der aktuelle SessionContainer
   * @param   facesContext      Der aktuelle Faces-Context
   */
  private void schreibeEnvelopesInFolderWrapperBean(SessionContainerBean
          sessionContainer, FacesContext facesContext) throws Exception {

    // Muessen wir die Envelopes neu besorgen?
    if(sessionContainer.isRenewEnvelopes()) {

      MailboxConnection mailboxConnection =
              sessionContainer.getMailboxConnection();

      // Warum try/catch, wenn wir alle Exceptions re-thrown?
      // Damit wir finally aufraeumen koennen.
      try {

        mailboxConnection.login(sessionContainer.getCurrentMailboxFolder());

        // Messages(-Envelopes) ueber die mailBoxConnection beziehen...
        RetrieveMessagesResultBean rmr = null;

        if(sessionContainer.isAllMessagesOnOnePage()) {
        
          rmr = mailboxConnection.getEnvelopes();
        }
        else {

          int tempStartNumber = (sessionContainer.getCurrentOffset() + 1);
          int tempEndNumber = (tempStartNumber +
                  sessionContainer.getAmountOfMessagesPerPage() - 1);
          rmr = mailboxConnection.getEnvelopes(tempStartNumber,
                  tempEndNumber, true);
        }

        // ...und kopieren (da getEnvelopes() nur leichtgewichtige Message-
        // Objekte zurueckgibt, die ihre Properties erst bei Zugriff nachladen,
        // haben wir ein Problem, wenn wir schon wieder offline sind).
        // POP3- und IMAP-Messages sind immer MimeMessages, so dass wir hier
        // ruhigen Gewissens casten koennen.
        Message[] messages = rmr.getMessages();

        for(int ii = 0; ii < messages.length; ii++) {

          messages[ii] =
                  OfflineMessageAssembler.assembleOverviewOfflineMimeMessage((MimeMessage)messages[ii]);
        }

        // Sortieren
        Arrays.sort(messages, sessionContainer.getSortierComparator());

        // Als managed bean in die Session schreiben
        FolderWrapperBean folderWrapper =
                (FolderWrapperBean)this.getManagedBeanByName(facesContext,
                Constants.NAME_MBEAN_FOLDERWRAPPER);
        folderWrapper.setFolder(mailboxConnection.getCurrentFolder());
        folderWrapper.setMessages(messages);
        folderWrapper.setOverallMessageCount(rmr.getOverallMessageCount());

        // Wenn jemand neue Envelopes will, muss er das anmelden; jetzt setzen
        // wir "renewEnvelopes" erstmal auf false.
        sessionContainer.setRenewEnvelopes(false);
      }
      catch(Exception e) {

        throw(e);
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
    
    // muss ansonsten ggf. neu sortiert werden?
    else if(sessionContainer.isRenewSortorder()) {

      // Messages ueber den FolderWrapper beziehen und neu sortieren.
      FolderWrapperBean folderWrapper =
              (FolderWrapperBean)this.getManagedBeanByName(facesContext,
              Constants.NAME_MBEAN_FOLDERWRAPPER);
      Message[] messages = folderWrapper.getMessages();

      Arrays.sort(messages, sessionContainer.getSortierComparator());
      
      // Sortiertes Array wieder in den FolderWrapper setzen (notwendig, da
      // dieses dort wieder konvertiert werden muß).
      folderWrapper.setMessages(messages);

      // Wenn jemand neu sortieren will, muss er das neu anmelden.
      sessionContainer.setRenewSortorder(false);
    }
  }

  /**
   * Schreibt die ausgewaehlte Mail in die managed Bean "DisplayMessageBean".
   * 
   * @param   sessionContainer  Der aktuelle SessionContainer
   * @param   facesContext      Der aktuelle Faces-Context
   */
  private void schreibeMessageInDisplayMessageBean(SessionContainerBean
          sessionContainer, FacesContext facesContext) throws Exception {

    MailboxConnection mailboxConnection =
            sessionContainer.getMailboxConnection();

    // Warum try/catch, wenn wir alle Exceptions re-thrown?
    // Damit wir finally aufraeumen koennen.
    try {

      mailboxConnection.login(sessionContainer.getCurrentMailboxFolder());

      // Message ueber die mailBoxConnection beziehen...
      Message message =
              mailboxConnection.getMessage(sessionContainer.getMessageNumberCurrentDisplayMail());

      // ...und kopieren (da getMessage() nur ein leichtgewichtiges Message-
      // Objekt zurueckgibt, das seine Properties erst bei Zugriff nachlaedt,
      // haben wir ein Problem, wenn wir schon wieder offline sind).
      // POP3- und IMAP-Messages sind immer MimeMessages, so dass wir hier
      // ruhigen Gewissens casten koennen.
      MimeMessage offlineMimeMessage =
              OfflineMessageAssembler.assembleOfflineMimeMessage((MimeMessage)message);

      // Als managed bean in die Session schreiben
      DisplayMessageBean displayMessage =
              (DisplayMessageBean)this.getManagedBeanByName(facesContext,
              Constants.NAME_MBEAN_DISPLAYMESSAGE);

      // Display-Message gemaess Message aufbereiten
      DisplayMessageAssembler.refurbishGivenDisplayMessage(displayMessage,
              offlineMimeMessage);
    }
    catch(Exception e) {

      throw(e);
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


  // ----------------------------------------------------- oeffentliche Methoden

  /* (non-Javadoc)
   * @see javax.faces.event.PhaseListener#getPhaseId()
   */
  public PhaseId getPhaseId() {

    // dieser Listener interessiert sich nur fuer die Render-Response-Phase 
    return(PhaseId.RENDER_RESPONSE);
  }

  /* (non-Javadoc)
   * @see javax.faces.event.PhaseListener#afterPhase(javax.faces.event.PhaseEvent)
   */
  public void afterPhase(PhaseEvent phaseEvent) {

    // empty;
  }

  /* (non-Javadoc)
   * @see javax.faces.event.PhaseListener#beforePhase(javax.faces.event.PhaseEvent)
   */
  public void beforePhase(PhaseEvent phaseEvent) {

    // Faces-Context holen und feststellen, wohin dieser Request laeuft...
    FacesContext facesContext = phaseEvent.getFacesContext();
    String viewId = facesContext.getViewRoot().getViewId(); 

    try {

      SessionContainerBean sessionContainer =
              (SessionContainerBean)this.getManagedBeanByName(facesContext,
              Constants.NAME_MBEAN_SESSIONCONTAINER);

      // Wenn es zur Mail-Uebersicht geht, aktuelle Mails des Ordners einlesen
      if(viewId.equals(Constants.VIEW_ID_MAILSLISTING)) {

        this.schreibeEnvelopesInFolderWrapperBean(sessionContainer,
                facesContext);
      }

      // Wenn es zur Mail-Anzeige geht, aktuelle Mail einlesen
      else if(viewId.equals(Constants.VIEW_ID_DISPLAY_MAIL)) {

        this.schreibeMessageInDisplayMessageBean(sessionContainer,
                facesContext);
      } 
    }
    catch(ConnectionEstablishException cee) {

      facesContext.addMessage(Constants.CLIENT_ID_MAILBOXHOST,
              ExceptionConverter.getFacesMessage(facesContext, cee, false));

      ViewHandler vh  = facesContext.getApplication().getViewHandler();
      UIViewRoot newRoot = vh.createView(facesContext,
              Constants.VIEW_ID_LOGON);
      facesContext.setViewRoot(newRoot);
    }
    catch(AccessDeniedException ade) {

      facesContext.addMessage(Constants.CLIENT_ID_MAILBOXPASSWORD,
              ExceptionConverter.getFacesMessage(facesContext, ade, false));

      ViewHandler vh  = facesContext.getApplication().getViewHandler();
      UIViewRoot newRoot = vh.createView(facesContext,
              Constants.VIEW_ID_LOGON);
      facesContext.setViewRoot(newRoot);
    }
    catch(YawebmailCertificateException yce) {

      try {

        CheckCertsBean checkCerts =
                (CheckCertsBean)ManagedBeanUtils.getManagedBeanByName(facesContext,
                        Constants.NAME_MBEAN_CHECKCERTS);

        checkCerts.setWhichCerts(CheckCertsController.WhichCertsEnum.MAILBOX);
        checkCerts.setServerCerts(yce.getCerts());

        ViewHandler vh  = facesContext.getApplication().getViewHandler();
        UIViewRoot newRoot = vh.createView(facesContext,
                Constants.VIEW_ID_CHECK_CERTS);
        facesContext.setViewRoot(newRoot);
      }
      catch(Exception e) {

        facesContext.addMessage(null,
                ExceptionConverter.getFacesMessage(facesContext, e, true));

        ViewHandler vh  = facesContext.getApplication().getViewHandler();
        UIViewRoot newRoot = vh.createView(facesContext,
                Constants.VIEW_ID_TECH_ERROR);
        facesContext.setViewRoot(newRoot);
      }
    }
    catch(Exception e) {

      facesContext.addMessage(null,
              ExceptionConverter.getFacesMessage(facesContext, e, true));

      ViewHandler vh  = facesContext.getApplication().getViewHandler();
      UIViewRoot newRoot = vh.createView(facesContext,
              Constants.VIEW_ID_TECH_ERROR);
      facesContext.setViewRoot(newRoot);
    }
  }

}
