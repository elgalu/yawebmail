/*
 * @(#)RetrieveInlinePartContentAction.java 1.00 2006/04/05
 *
 * Copyright (c) 2006, Stephan Sann
 *
 * 05.04.2006 ssann        Vers. 1.0     created
 */


package de.lotk.yawebmail.nf;

import de.lotk.yawebmail.application.Constants;
import de.lotk.yawebmail.bean.DisplayMessageBean;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.mail.Part;
import javax.mail.internet.ContentType;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet, das den Content eines (Message-)Parts an den Browser sendet.
 * 
 * @author Stephan Sann
 * @version 1.0
 */
public final class RetrieveInlinePartContentServlet extends HttpServlet {

  // ---------------------------------------------------------------- Konstanten

  /** serialVersionUID */
  private static final long serialVersionUID = -2532842002722731642L;


  // ------------------------------------------------------------ Public Methods

  /* (non-Javadoc)
   * @see javax.servlet.http.HttpServlet#service(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
   */
  protected void service(HttpServletRequest request, HttpServletResponse
          response) throws ServletException, IOException {

  	// Welcher Part soll ausgegeben werden?
  	String idPart = request.getQueryString();

  	// Nur was tun, wenn Parameter gut am Start ist
  	if((idPart != null) && (idPart.trim().length() >= 1)) {

      try {

        // Session und daraus das DisplayMessage-Objekt besorgen
        HttpSession session = request.getSession();
        DisplayMessageBean displayMessage =
                (DisplayMessageBean)session.getAttribute(Constants.NAME_MBEAN_DISPLAYMESSAGE);

        // TODO was tun, wenn displayMessage = null?

        // Auszugebenden Part aus DisplayMessage-Objekt holen.
        Part part = (Part)displayMessage.getInlineParts().get(idPart);

        // Response-Header setzen
        ContentType contentType = new ContentType(part.getContentType());
        response.setContentType(contentType.getBaseType());

        StringBuffer contentDisposition = new StringBuffer();
        contentDisposition.append("inline; filename=\"");
        contentDisposition.append(part.getFileName()).append("\"");

        response.setHeader("Content-disposition", contentDisposition.toString());
        
        // geht nicht, weil "part.getSize()" nicht die decodete Size liefert.
        //response.setContentLength(part.getSize());

        // Den Part-Content in den Response-Outputstream schreiben
        BufferedInputStream bufInputStream =
                new BufferedInputStream(part.getInputStream());
        OutputStream outputStream = response.getOutputStream();

        byte[] inBuf = new byte[4096];
        int len = 0;
        while((len = bufInputStream.read(inBuf)) > 0) {

          outputStream.write(inBuf, 0, len);
        }

        outputStream.flush();
        outputStream.close();
      }
      catch(Exception e) {

        e.printStackTrace();
        throw(new ServletException(("[RetrieveInlinePartContentServlet] " +
                e.getMessage()), e));
      }
  	}
  }

}
