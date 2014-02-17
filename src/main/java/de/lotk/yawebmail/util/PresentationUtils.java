/*
 * @(#)PresentationUtils.java 1.00 2005/05/11
 *
 * Copyright (c) 2005, Stephan Sann
 *
 * 11.05.2005 ssann        Vers. 1.0     created
 */


package de.lotk.yawebmail.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.oro.text.perl.Perl5Util;

/**
 * Stellt Methoden zur Praesentations-Aufbereitung von Mails zur Verfuegung.
 *
 * @author Stephan Sann
 * @version 1.0
 */
public class PresentationUtils {

  // ---------------------------------------------------------------- Konstanten

  /** Reg-Expr fuer HTTP(S)-URLs */
  public static final String HTTP_URL_REGEXPR =
          "((?:https?://(?:(?:(?:(?:(?:[a-zA-Z\\d](?:(?:[a-zA-Z\\d]|-)*[a-zA-Z\\d])?)\\.)*(?:[a-zA-Z](?:(?:[a-zA-Z\\d]|-)*[a-zA-Z\\d])?))|(?:(?:\\d+)(?:\\.(?:\\d+)){3}))(?::(?:\\d+))?)(?:/(?:(?:(?:(?:[a-zA-Z\\d$\\-_.+!*'(),]|(?:%[a-fA-F\\d]{2}))|[;:@&=])*)(?:/(?:(?:(?:[a-zA-Z\\d$\\-_.+!*'(),]|(?:%[a-fA-F\\d]{2}))|[;:@&=])*))*)(?:\\?(?:(?:(?:[a-zA-Z\\d$\\-_.+!*'(),]|(?:%[a-fA-F\\d]{2}))|[;:@&=])*))?)?))";


  // --------------------------------------------------------- Klassen-Variablen

  /** Perl5Util zur Arbeit mit regulaeren Ausdruecken */
  private static Perl5Util perl5Util = new Perl5Util();

  /** Logging-Instanz */
  protected static final Log LOG = LogFactory.getLog(PresentationUtils.class);


  // ----------------------------------------------------- oeffentliche Methoden

  /**
   * Filter the specified string for characters that are senstive to
   * HTML interpreters, returning the string with these characters replaced
   * by the corresponding character entities.
   *
   * @param value The string to be filtered and returned
   */
  public static String htmlEntities(String value) {

    if(value == null) {

      return(null);
    }

    char content[] = new char[value.length()];
    value.getChars(0, value.length(), content, 0);
    StringBuffer result = new StringBuffer(content.length + 50);
    int orgAppends = 0;

    for(int i = 0; i < content.length; i++) {

      switch(content[i]) {
        case '<':
          result.append("&lt;");
          break;
        case '>':
          result.append("&gt;");
          break;

        // TODO Loesung ausdenken, die nicht die URLs im Text zerschiesst 

        /*
        case '&':
          result.append("&amp;");
          break;
        case '"':
          result.append("&quot;");
          break;
        case '\'':
          result.append("&#39;");
          break;
        */

        default:
          result.append(content[i]);
          orgAppends++;
      }
    }

    // Gab es ueberhaupt Aenderungen? Sonst Original-String zurueck.
    if(orgAppends == content.length) {

      return(value);
    }
    else {

      return(result.toString());  
    }
  }

  /**
   * Formatiert einen Plain-Text mit HTML-Format-Tags (fuegt HTML-Umbrueche ein
   * und ersetzt mehrfach hintereinander vorkommende Blanks durch HTML-Blanks.).
   *
   * @param   value   String, der bearbeitet werden soll.
   * @return          <code>String</code> mit formatiertem Text.   
   */
  public static String htmlFormat(String value) {

    // Umbrueche
    StringBuffer stringBuffer = new StringBuffer();
    String actLine = null;

    try {

      // Text zeilenweise einlesen und HTML-Line-Breaks amfuegen
      BufferedReader bufReader = new BufferedReader(new StringReader(value));

      while((actLine = bufReader.readLine()) != null) {

        stringBuffer.append(actLine).append("<br/>\n");
      }

      bufReader.close();

      value = stringBuffer.toString();
    }
    catch(IOException ioe) {

      LOG.error("[htmlFormat] Fehler beim Einlesen des Strings.", ioe);
    }
    
    // Ggf. Blanks am Zeilenanfang ersetzen
    if(perl5Util.match("#^ #m", value)) {
      
      value = perl5Util.substitute("s#^ #&nbsp;#gm", value);
    }

    // Gibt es jetzt noch mehrfach hintereinander vorkommende Blanks?
    if(perl5Util.match("# {2,}#m", value)) {

      value = perl5Util.substitute("s# {2}# &nbsp;#gm", value);
    }

    return(value);
  }

  /**
   * Ersetzt URLs durch Derefer-Links zur jeweiligen URL.
   *
   * @param     partContent   Inhalt des Parts
   * @param     request       Aktueller Request
   * @return                  <code>String</code> mit ersetzten URLs
   */
  public static String replaceUrlsWithLinks(String partContent,
          HttpServletRequest request) {

    StringBuffer regExpr = new StringBuffer();
    regExpr.append("s#").append(HTTP_URL_REGEXPR).append("#<a href=\"");
    regExpr.append(request.getContextPath());
    regExpr.append("/dereferServlet.svl?$1\" target=\"_blank\">$1</a>#gi");

    return(perl5Util.substitute(regExpr.toString(), partContent));
  }

  /**
   * Ersetzt Links durch Derefer-Links.
   *
   * @param     partContent   Inhalt des Parts
   * @param     request       Aktueller Request
   * @return                  <code>String</code> mit ersetzten URLs
   */
  public static String dereferLinks(String partContent, HttpServletRequest
          request) {

    StringBuffer regExpr = new StringBuffer();
    regExpr.append("s#(<\\s*a[^>]*href\\s*=\\s*\")(.*?)(\".*?>)#$1");
    regExpr.append(request.getContextPath());
    regExpr.append("/dereferServlet.svl?$2$3#gi");

    return(perl5Util.substitute(regExpr.toString(), partContent));
  }

  /**
   * Ersetzt Inline-Content-IDs mit Links zur RetrieveInlinePartContentAction.
   * 
   * @param     partContent   Inhalt des Parts
   * @param     request       Aktueller Request
   * @param     response      Aktueller Response
   * @return                  <code>String</code> mit ersetzten Content-IDs
   */
  public static String replaceContentIds(String partContent, HttpServletRequest
          request, HttpServletResponse response) {

    // Link zum ActionServlet, das InlineParts ausgibt
    StringBuffer retrieveActionLink = new StringBuffer();
    retrieveActionLink.append(request.getContextPath());
    retrieveActionLink.append("/retrieveInlinePartContentServlet.svl");

    // Ggf. Session-ID hinzufuegen (wenn Cookies deaktiviert)
    String encRetrieveActionLink =
            response.encodeURL(retrieveActionLink.toString());

    StringBuffer regExpr = new StringBuffer();
    regExpr.append("s#\"cid:(.*?)\"#\"").append(encRetrieveActionLink);
    regExpr.append("?$1\"#gi");

    return(perl5Util.substitute(regExpr.toString(), partContent));
  }

}
