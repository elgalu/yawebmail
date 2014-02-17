/*
 * @(#)DereferServlet.java 1.00 2006/05/05
 *
 * Copyright (c) 2006, Stephan Sann
 *
 * 05.05.2006 ssann        Vers. 1.0     created
 */


package de.lotk.yawebmail.nf;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet, das zu einer externen Resource weiterleitet. Wird benutzt, um zu
 * verhindern, dass bei ausgeschalteten Cookies die Session-ID im
 * Http-Referer-Header auf dem exteren Server auslesbar ist.
 * 
 * @author Stephan Sann
 * @version 1.0
 */
public final class DereferServlet extends HttpServlet {

  // ---------------------------------------------------------------- Konstanten

  /** serialVersionUID */
  private static final long serialVersionUID = 6741843397697567705L;


  // ------------------------------------------------------------ Public Methods

  /* (non-Javadoc)
   * @see javax.servlet.http.HttpServlet#service(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
   */
  protected void service(HttpServletRequest request, HttpServletResponse
          response) throws ServletException, IOException {

    String target = request.getQueryString();

    StringBuffer html = new StringBuffer("<html>");
    html.append("<head><meta http-equiv=\"REFRESH\" content=\"0; URL=");
    html.append(target).append("\" /><title>Redirection</title></head>");
    html.append("<body>Please wait.<br/>If your browser doesn't redirect ");
    html.append("you, please click <a href=\"").append(target);
    html.append("\">here</a>.</body>");
    html.append("</html>");

    // Content-Type auf "text/html" setzen - sonst wird der Server den Content
    // ggf. als "text/plain" ausliefern (und nix wird passieren)...
    response.setContentType("text/html");

    // Printwriter besorgen und HTML ausgeben
    PrintWriter out = response.getWriter();
    out.write(html.toString());
  }

}
