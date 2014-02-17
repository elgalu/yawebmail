/*
 * @(#)YawebmailTrustManager.java 1.00 2008/02/10
 *
 * Copyright (c) 2008, Stephan Sann
 *
 * 10.02.2008 ssann        Vers. 1.0     created
 */


package de.lotk.yawebmail.business;

import java.security.KeyStore;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Arrays;

import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import de.lotk.yawebmail.exceptions.YawebmailCertificateException;

/**
 * yawebmail-implementation of a TrustManager.
 * 
 * @author Stephan Sann
 * @version 1.0
 */
public class YawebmailTrustManager implements X509TrustManager {

  // ------------------------------------------------------------- instance-vars

  /** A TrustManager to hand on method-calls */
  private X509TrustManager adapteeTrustManager = null;

  /** Holds the accepted certs */
  private X509Certificate[] acceptedCerts = null;


  // ------------------------------------------------------------ constructor(s)

  /**
   * Initializes a new YawebmailTrustManager-Instance.
   */
  public YawebmailTrustManager() {

    try {

      TrustManagerFactory tmf = TrustManagerFactory.getInstance("X509");
      tmf.init((KeyStore)null);
      adapteeTrustManager = (X509TrustManager)tmf.getTrustManagers()[0];
    }
    catch(Exception e) {

      throw(new RuntimeException(e.getMessage(), e));
    }
  }


  // ------------------------------------------------------- Getters ans Setters

  /**
   * @return the acceptedCerts
   */
  public X509Certificate[] getAcceptedCerts() {
  
    return acceptedCerts;
  }

  /**
   * @param acceptedCerts the acceptedCerts to set
   */
  public void setAcceptedCerts(X509Certificate[] acceptedCerts) {
  
    this.acceptedCerts = acceptedCerts;
  }


  // ------------------------------------------------------------ public methods

  /* (non-Javadoc)
   * @see javax.net.ssl.X509TrustManager#checkClientTrusted(java.security.cert.X509Certificate[], java.lang.String)
   */
  public void checkClientTrusted(X509Certificate[] certs, String authType) throws
          CertificateException {

    try {

      this.adapteeTrustManager.checkClientTrusted(certs, authType);
    }
    catch(CertificateException ce) {

      throw(new YawebmailCertificateException(certs, ce));
    }
  }

  /* (non-Javadoc)
   * @see javax.net.ssl.X509TrustManager#checkServerTrusted(java.security.cert.X509Certificate[], java.lang.String)
   */
  public void checkServerTrusted(X509Certificate[] certs, String authType) throws
          CertificateException{

    try {

      this.adapteeTrustManager.checkServerTrusted(certs, authType);
    }
    catch(CertificateException ce) {

      if((this.acceptedCerts == null) ||
              (! Arrays.equals(this.acceptedCerts, certs))) {

        throw(new YawebmailCertificateException(certs, ce));
      }
    }
  }

  /* (non-Javadoc)
   * @see javax.net.ssl.X509TrustManager#getAcceptedIssuers()
   */
  public X509Certificate[] getAcceptedIssuers() {

    return (adapteeTrustManager.getAcceptedIssuers());
  }

}
