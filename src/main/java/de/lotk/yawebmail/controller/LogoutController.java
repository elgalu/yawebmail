/*
 * @(#)LogoutController.java 1.00 2007/03/08
 *
 * Copyright (c) 2007, Stephan Sann
 *
 * 08.03.2006 ssann        Vers. 1.0     created
 */


package de.lotk.yawebmail.controller;

import javax.faces.context.FacesContext;

import de.lotk.yawebmail.application.Constants;
import de.lotk.yawebmail.bean.CheckCertsBean;
import de.lotk.yawebmail.bean.DisplayMessageBean;
import de.lotk.yawebmail.bean.FolderWrapperBean;
import de.lotk.yawebmail.bean.LoginDataBean;
import de.lotk.yawebmail.bean.MailBasisBean;
import de.lotk.yawebmail.bean.SessionContainerBean;
import de.lotk.yawebmail.bean.SmtpConnectionBean;
import de.lotk.yawebmail.util.faces.ExceptionConverter;

/**
 * Controller fuer das Logout 
 * 
 * @author Stephan Sann
 * @version 1.0
 */
public class LogoutController extends BaseController {

  // ----------------------------------------------------- oeffentliche Methoden

  /**
   * Versucht das Logout durchzufuehren.
   * 
   * @return          <code>String</code>-Objekt mit Info, wohin navigiert wird. 
   */
  public String logout() {

    FacesContext facesContext = FacesContext.getCurrentInstance(); 

    try {

      // Alle Daten-speichernden Beans holen
      SessionContainerBean sessionContainer =
              (SessionContainerBean)this.getManagedBeanByName(Constants.NAME_MBEAN_SESSIONCONTAINER);
      LoginDataBean loginData =
              (LoginDataBean)this.getManagedBeanByName(Constants.NAME_MBEAN_LOGINDATA);
      CheckCertsBean checkCerts =
              (CheckCertsBean)this.getManagedBeanByName(Constants.NAME_MBEAN_CHECKCERTS);
      SmtpConnectionBean smtpConnection =
              (SmtpConnectionBean)this.getManagedBeanByName(Constants.NAME_MBEAN_SMTPCONNECTION);
      FolderWrapperBean folderWrapper =
              (FolderWrapperBean)this.getManagedBeanByName(Constants.NAME_MBEAN_FOLDERWRAPPER);
      DisplayMessageBean displayMessage =
              (DisplayMessageBean)this.getManagedBeanByName(Constants.NAME_MBEAN_DISPLAYMESSAGE);
      MailBasisBean mailBasisBean =
              (MailBasisBean)this.getManagedBeanByName(Constants.NAME_MBEAN_MAILBASIS);

      // Alle Beans zuruecksetzen
      sessionContainer.reset();
      loginData.reset();
      checkCerts.reset();
      smtpConnection.reset();
      folderWrapper.reset();
      displayMessage.reset();
      mailBasisBean.reset();
    }
    catch(Exception e) {

      facesContext.addMessage(null,
              ExceptionConverter.getFacesMessage(facesContext, e, true));
      return(Constants.OUTCOME_TECH_ERROR);
    }

    // Wenn wir hier angelangt sind, scheint alles i.O. zu sein.
    return(Constants.OUTCOME_LOGON);
  }

}
