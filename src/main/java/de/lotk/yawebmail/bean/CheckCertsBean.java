/*
 * @(#)CheckCertsBean.java 1.00 2008/03/12
 *
 * Copyright (c) 2008, Stephan Sann
 *
 * 12.03.2008 ssann        Vers. 1.0     created
 */


package de.lotk.yawebmail.bean;

import java.io.Serializable;
import java.security.cert.Certificate;

import de.lotk.yawebmail.application.Lifecycle;
import de.lotk.yawebmail.controller.CheckCertsController;

/**
 * Bean that takes data for cert-checking 
 * 
 * @author Stephan Sann
 * @version 1.0
 */
public class CheckCertsBean implements Lifecycle, Serializable {

  // ----------------------------------------------------------------- constants

  /** serialVersionUID */
  private static final long serialVersionUID = 1129777777717127867L;


  // ------------------------------------------------------------- instance-vars

  /** Which certs to check */
  private CheckCertsController.WhichCertsEnum whichCerts = null;

  /** Certificates of the Server */
  private Certificate[] serverCerts = null;


  // --------------------------------------------------------- getter und setter
  
  /**
   * @return the serverCerts
   */
  public Certificate[] getServerCerts() {
  
    return serverCerts;
  }

  /**
   * @param serverCerts the serverCerts to set
   */
  public void setServerCerts(Certificate[] serverCerts) {
  
    this.serverCerts = serverCerts;
  }
  
  /**
   * @return the whichCerts
   */
  public CheckCertsController.WhichCertsEnum getWhichCerts() {
  
    return whichCerts;
  }

  /**
   * @param whichCerts the whichCerts to set
   */
  public void setWhichCerts(CheckCertsController.WhichCertsEnum whichCerts) {
  
    this.whichCerts = whichCerts;
  }


  // ----------------------------------------------------- oeffentliche Methoden

  /**
   * sets the bean back
   */
  public void reset() {

    this.serverCerts = null;
    this.whichCerts = null;
  }

  /* (non-Javadoc)
   * @see de.lotk.webftp.Lifecycle#destroy()
   */
  public void destroy() {

    this.reset();
  }

}
