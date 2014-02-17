/*
 * @(#)RetrieveDisplayPartContentServlet.java 1.00 2006/04/04
 *
 * Copyright (c) 2006, Stephan Sann
 *
 * 04.04.2006 ssann        Vers. 1.0     created
 */


package de.lotk.yawebmail.nf;

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

import de.lotk.yawebmail.application.Constants;
import de.lotk.yawebmail.bean.DisplayMessageBean;
import de.lotk.yawebmail.util.PresentationUtils;

/**
 * Servlet, das den Content eines (Message-)Parts an den Browser sendet.
 * 
 * @author Stephan Sann
 * @version 1.0
 */
public final class RetrieveDisplayPartContentServlet extends HttpServlet {

  // ---------------------------------------------------------------- Konstanten

  /** serialVersionUID */
  private static final long serialVersionUID = -7113491732382019620L;


  // ------------------------------------------------------------ public Methods

  /* (non-Javadoc)
   * @see javax.servlet.http.HttpServlet#service(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
   */
  protected void service(HttpServletRequest request, HttpServletResponse
          response) throws ServletException, IOException {

  	// Welcher Part soll ausgegeben werden?
  	String numberPart = request.getParameter(Constants.REQ_PARAM_PART_NUMBER);

  	// Nur was tun, wenn Parameter gut am Start ist
  	if((numberPart != null) && (numberPart.trim().length() >= 1)) {

      try {

        // Session und daraus das DisplayMessage-Objekt besorgen
        HttpSession session = request.getSession();
        DisplayMessageBean displayMessage =
                (DisplayMessageBean)session.getAttribute(Constants.NAME_MBEAN_DISPLAYMESSAGE);

        // TODO was tun, wenn displayMessage = null?

        // Auszugebenden Part aus DisplayMessage-Objekt holen.
        int nrPart = Integer.parseInt(numberPart);
        Part part = (Part)displayMessage.getDisplayParts().get(nrPart);

        // Response-Header setzen
        ContentType partContentType = new ContentType(part.getContentType());
        response.setContentType(partContentType.toString());

        // Wenn PartContent = String, Links mit Derefer-Link ersetzen und
        // Verweise auf InlineParts ersetzen
        Object partContent = null;

        try {

          partContent = part.getContent();
        }
        catch(Exception e) {

          // Part scheint kaputt zu sein - wir machen nichts. Da partContent
          // somit null bleibt, wird der Part einfach ausgestreamt.
        }

        if((partContent != null) && (partContent instanceof String)) {

          String outStr =
                  PresentationUtils.dereferLinks((String)part.getContent(),
                  request);
          outStr = PresentationUtils.replaceContentIds(outStr, request, response);

          response.setContentLength(outStr.length());
          response.getWriter().write(outStr);
        }

        // Sonst den PartContent in den Response-Outputstream schreiben
        else {

          StringBuffer contentDisposition = new StringBuffer();
          contentDisposition.append("attachment; filename=\"");
          contentDisposition.append(part.getFileName()).append("\"");

          response.setHeader("Content-disposition", contentDisposition.toString());

          // geht nicht, weil "part.getSize()" nicht die decodete Size liefert.
          //response.setContentLength(part.getSize());

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
      }
      catch(Exception e) {

        e.printStackTrace();
        throw(new ServletException(("[RetrieveDisplayPartContentServlet] " +
                e.getMessage()), e));
      }
  	}
  }

}
