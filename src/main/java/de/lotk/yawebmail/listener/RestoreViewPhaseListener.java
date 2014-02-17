/*
 * @(#)RestoreViewPhaseListener.java 1.00 2006/05/02
 *
 * Copyright (c) 2006, Stephan Sann
 *
 * 02.05.2006 ssann        Vers. 1.0     created
 */


package de.lotk.yawebmail.listener;

import javax.faces.application.ViewHandler;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;

import de.lotk.yawebmail.application.Constants;
import de.lotk.yawebmail.bean.SessionContainerBean;
import de.lotk.yawebmail.business.MailboxConnection;
import de.lotk.yawebmail.exceptions.SessionExpiredException;
import de.lotk.yawebmail.util.faces.ExceptionConverter;

/**
 * Phase-Listener fuer die Restore-View-Phase
 * 
 * @author Stephan Sann
 * @version 1.0
 */
public class RestoreViewPhaseListener extends BaseListener implements
        PhaseListener {

  // ---------------------------------------------------------------- Konstanten

  /** serialVersionUID */
  private static final long serialVersionUID = -2994659593672966426L;


  // ----------------------------------------------------- oeffentliche Methoden

  /* (non-Javadoc)
   * @see javax.faces.event.PhaseListener#getPhaseId()
   */
  public PhaseId getPhaseId() {

    // dieser Listener interessiert sich nur fuer die Render-Response-Phase 
    return(PhaseId.RESTORE_VIEW);
  }

  /* (non-Javadoc)
   * @see javax.faces.event.PhaseListener#afterPhase(javax.faces.event.PhaseEvent)
   */
  public void afterPhase(PhaseEvent phaseEvent) {

    FacesContext facesContext = phaseEvent.getFacesContext();
    String viewId = facesContext.getViewRoot().getViewId();

    // Wenn die "viewId" NICHT "index.jsp" oder "logon.jsp" ist, versuchen die
    // Mailbox-Connection aus dem Session-Container zu bekommen. Wenn die nicht
    // am Start ist, ist die Session wohl abgelaufen...
    if((! viewId.equals(Constants.VIEW_ID_LOGON)) &&
            (! viewId.equals(Constants.VIEW_ID_INDEX))) {

      try {

        SessionContainerBean sessionContainer =
                (SessionContainerBean)this.getManagedBeanByName(facesContext,
                Constants.NAME_MBEAN_SESSIONCONTAINER);

        MailboxConnection mailboxConnection =
                sessionContainer.getMailboxConnection();

        if(mailboxConnection == null) {

          throw(new SessionExpiredException());
        }
      }
      catch(SessionExpiredException see) {

        facesContext.addMessage(null,
                ExceptionConverter.getFacesMessage(facesContext, see, false));

        ViewHandler vh  = facesContext.getApplication().getViewHandler();

        // TODO konfigurierbar machen!
        UIViewRoot newRoot = vh.createView(facesContext, "/logon.jsp");
        facesContext.setViewRoot(newRoot);
      }
      catch(Exception e) {

        e.printStackTrace();

        facesContext.addMessage(null,
                ExceptionConverter.getFacesMessage(facesContext, e, true));

        ViewHandler vh  = facesContext.getApplication().getViewHandler();

        // TODO konfigurierbar machen!
        UIViewRoot newRoot = vh.createView(facesContext, "/techError.jsp");
        facesContext.setViewRoot(newRoot);
      }
    }
  }

  /* (non-Javadoc)
   * @see javax.faces.event.PhaseListener#beforePhase(javax.faces.event.PhaseEvent)
   */
  public void beforePhase(PhaseEvent phaseEvent) {

    // empty;
  }

}
