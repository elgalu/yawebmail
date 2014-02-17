/*
 * @(#)InitServlet.java 1.00 2008/10/23
 *
 * Copyright (c) 2008, Stephan Sann
 *
 * 23.10.2008 ssann        Vers. 1.0     created
 */

package de.lotk.yawebmail.nf;

import java.io.IOException;

import javax.servlet.GenericServlet;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * Sets initial properties for yawebmail.
 * 
 * @author Stephan Sann
 * @version 1.0
 */
public class InitServlet extends GenericServlet {

  /** serialVersionUID */
  private static final long serialVersionUID = -3952521058649493862L;

  /* (non-Javadoc)
   * @see javax.servlet.GenericServlet#init()
   */
  @Override
  public void init() throws ServletException {

    // Set system-property "mail.mime.decodetext.strict" to false
    System.setProperty("mail.mime.decodetext.strict", "false");

    // Apple-Mail produces crappy filename-headers. JavaMail has a fix for it.
    System.setProperty("mail.mime.applefilenames", "true");
  }

  /* (non-Javadoc)
   * @see javax.servlet.GenericServlet#service(javax.servlet.ServletRequest, javax.servlet.ServletResponse)
   */
  @Override
  public void service(ServletRequest req, ServletResponse res)
      throws ServletException, IOException {

    throw (new ServletException("Should never be called."));
  }

}
