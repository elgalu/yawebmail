/*
 * @(#)YawebmailCertificateException.java 1.00 2008/02/14
 *
 * Copyright (c) 2008, Stephan Sann
 *
 * 14.02.2008 ssann        Vers. 1.0     created
 */


package de.lotk.yawebmail.exceptions;

import java.security.cert.Certificate;
import java.security.cert.CertificateException;

/**
 * Exception that wraps a CertificateException an trasports the "failing" certs.
 * 
 * @author Stephan Sann
 * @version 1.0
 */
public class YawebmailCertificateException extends CertificateException {

  // ----------------------------------------------------------------- constants

  /** serialVersionUID */
  private static final long serialVersionUID = 5916241713232215246L;


  // ------------------------------------------------------------- instance vars

  /** holds the "failing" cert */
  private Certificate[] certs = null;

  /** holds the causing CertificateException */
  private CertificateException cause = null;


  // ------------------------------------------------------------ constructor(s)

  /**
   * Initializes a new YawebmailCertificateException with a "failing" certs and
   * the causing CertificateException.
   * 
   * @param   certs  The "failing" certs.
   * @param   cause  The causing CertificateException.
   */
  public YawebmailCertificateException(Certificate[] certs, CertificateException
          cause) {

    this.certs = certs;
    this.cause = cause;
  }


  // -------------------------------------------------------------------- getter

  /**
   * @return the cause
   */
  public CertificateException getCause() {
  
    return cause;
  }

  /**
   * @return the certs
   */
  public Certificate[] getCerts() {
  
    return certs;
  }

}
