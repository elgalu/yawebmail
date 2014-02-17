/*
 * @(#)CheckCertsController.java 1.00 2008/03/12
 *
 * Copyright (c) 2008, Stephan Sann
 *
 * 12.03.2008 ssann        Vers. 1.0     created
 */


package de.lotk.yawebmail.controller;

import java.security.cert.X509Certificate;

import javax.faces.context.FacesContext;

import de.lotk.yawebmail.application.Constants;
import de.lotk.yawebmail.bean.CheckCertsBean;
import de.lotk.yawebmail.bean.SessionContainerBean;
import de.lotk.yawebmail.util.faces.ExceptionConverter;
import de.lotk.yawebmail.util.faces.ManagedBeanUtils;

/**
 * Controller for the check of certs 
 * 
 * @author Stephan Sann
 * @version 1.0
 */
public class CheckCertsController extends BaseController {

  // ----------------------------------------------------- oeffentliche Methoden

  /**
   * User has decided to trust the displayed certs.
   * 
   * @return  <code>String</code>-object with info where to navigate to 
   */
  public String certsTrusted() {

    FacesContext facesContext = FacesContext.getCurrentInstance(); 

    try {

      // Get beans
      SessionContainerBean sessionContainer =
              (SessionContainerBean)this.getManagedBeanByName(Constants.NAME_MBEAN_SESSIONCONTAINER);
      CheckCertsBean checkCerts =
              (CheckCertsBean)this.getManagedBeanByName(Constants.NAME_MBEAN_CHECKCERTS);

      X509Certificate[] acceptedCerts =
              (X509Certificate[])checkCerts.getServerCerts();

      CheckCertsController.WhichCertsEnum wce = checkCerts.getWhichCerts();
      return(wce.processTrusted(sessionContainer, acceptedCerts));
    }
    catch(Exception e) {

      facesContext.addMessage(null,
              ExceptionConverter.getFacesMessage(facesContext, e, true));
      return(Constants.OUTCOME_TECH_ERROR);
    }
  }

  /**
   * User has decided NOT to trust the displayed certs.
   * 
   * @return  <code>String</code>-object with info where to navigate to 
   */
  public String certsNotTrusted() {

    FacesContext facesContext = FacesContext.getCurrentInstance(); 

    try {

      // Get CheckCert-bean
      CheckCertsBean checkCerts =
              (CheckCertsBean)this.getManagedBeanByName(Constants.NAME_MBEAN_CHECKCERTS);

      CheckCertsController.WhichCertsEnum wce = checkCerts.getWhichCerts();

      return(wce.processNotTrusted());
    }
    catch(Exception e) {

      facesContext.addMessage(null,
              ExceptionConverter.getFacesMessage(facesContext, e, true));
      return(Constants.OUTCOME_TECH_ERROR);
    }
  }


  // ------------------------------------------------------------- inner classes

  /**
   * Enum with the possible Cert-checks.
   */
  public static enum WhichCertsEnum {

    MAILBOX {

      /* (non-Javadoc)
       * @see de.lotk.yawebmail.controller.CheckCertsController.WhichCertsEnum#processTrusted(de.lotk.yawebmail.bean.SessionContainerBean, java.security.cert.X509Certificate[])
       */
      @Override
      public String processTrusted(SessionContainerBean sessionContainer,
              X509Certificate[] acceptedCerts) throws Exception {

        sessionContainer.getMailboxTrustManager().setAcceptedCerts(acceptedCerts);  
        return(Constants.OUTCOME_MAILS_LISTING);
      }

      /* (non-Javadoc)
       * @see de.lotk.yawebmail.controller.CheckCertsController.WhichCertsEnum#processNotTrusted()
       */
      @Override
      public String processNotTrusted() throws Exception {

        // Return to logon-screen
        return(Constants.OUTCOME_LOGON);
      }
    },
    SMTP {

      /* (non-Javadoc)
       * @see de.lotk.yawebmail.controller.CheckCertsController.WhichCertsEnum#processTrusted(de.lotk.yawebmail.bean.SessionContainerBean, java.security.cert.X509Certificate[])
       */
      @Override
      public String processTrusted(SessionContainerBean sessionContainer,
              X509Certificate[] acceptedCerts) throws Exception {

        sessionContainer.getSmtpTrustManager().setAcceptedCerts(acceptedCerts);

        FacesContext fc = FacesContext.getCurrentInstance();

        CreateMailController createMailController =
                (CreateMailController)ManagedBeanUtils.getManagedBeanByName(fc,
                        Constants.NAME_MBEAN_CREATEMAILCONTROLLER);

        return(createMailController.sendMail());
      }

      /* (non-Javadoc)
       * @see de.lotk.yawebmail.controller.CheckCertsController.WhichCertsEnum#processNotTrusted()
       */
      @Override
      public String processNotTrusted() throws Exception {

        // Return to creat-mail-screen
        return(Constants.OUTCOME_CREATE_MAIL);
      }
    };

    /**
     * Processing if the user choses to trust the cert.
     * Each Enum-value has to define its own version of this method.
     * 
     * @param   sessionContainer  Current SessionContainer
     * @param   acceptedCerts     Accepted Certs
     * @return  <code>String</code>-object with info where to navigate to.
     * @throws  Exception         If something goes wrong.
     */
    public abstract String processTrusted(SessionContainerBean sessionContainer,
            X509Certificate[] acceptedCerts) throws Exception;

    /**
     * Processing if the user choses not to trust the cert.
     * Each Enum-value has to define its own version of this method.
     * 
     * @return  <code>String</code>-object with info where to navigate to.
     * @throws  Exception         If something goes wrong.
     */
    public abstract String processNotTrusted() throws Exception;
  }

}
