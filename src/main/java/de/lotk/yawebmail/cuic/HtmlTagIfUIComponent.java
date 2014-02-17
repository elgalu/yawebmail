/*
 * @(#)HtmlTagIfUIComponent.java 1.00 2007/08/10
 *
 * Copyright (c) 2007, Stephan Sann
 *
 * 10.08.2007 ssann        Vers. 1.0     created
 */


package de.lotk.yawebmail.cuic;

import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.internet.ContentType;

import org.apache.myfaces.custom.htmlTag.HtmlTag;

/**
 * Gibt einen HTML-Tag (incl. Body) aus, wenn der ContentType eines uebergebenen
 * Parts "stimmt".
 * 
 * @author Stephan Sann
 * @version 1.0
 */
public class HtmlTagIfUIComponent extends HtmlTag {

  // ---------------------------------------------------------- private Methoden

  /**
   * Ermittelt, ob die in der Bean gefundenen Content-Types mit der Vorgabe
   * uebereinstimmen.
   * 
   * @param   bean           Bean-Objekt, das betrachtet werden soll.
   * @param   expectedValue  Erwarteter Wert
   * @return  <code>boolean</code>, ob gerendert werden soll.
   */
  private boolean compareContentTypes(Object bean, String expectedValue) {

    // ContentType des aktuellen Mail-Elements auslesen.
    String mailElementContentType = null;

    try {

      String contentTypeString = null;
      
      if(bean instanceof Part) {

        contentTypeString = ((Part)bean).getContentType();
      }
      else if(bean instanceof Multipart) {

        contentTypeString = ((Multipart)bean).getContentType();
      }

      // Da "getContentType()" ggf. weitere Infos zurueckgibt (z.B. das Charset
      // oder den Filename), duefen wir hier nur den ersten Teilstring benutzen.
      mailElementContentType =
              (new ContentType(contentTypeString)).getBaseType();
    }
    catch(Exception e) {

      e.printStackTrace();
      throw(new RuntimeException("ContentType des aktuellen Mail-Elements nicht lesbar."));
    }

    // Zurueckgeben, ob es einen Match gibt
    return(expectedValue.indexOf(mailElementContentType.toLowerCase()) > (-1));
  }

  /**
   * Ermittelt, ob die Bean von einem gewuenschten Message-Type ist.
   * 
   * @param   bean           Bean-Objekt, das betrachtet werden soll.
   * @param   expectedValue  Erwarteter Wert
   * @return  <code>boolean</code>, ob gerendert werden soll.
   */
  private boolean compareMessageTypes(Object bean, String expectedValue) {

    try {

      Class compareClass = Class.forName(expectedValue, false,
                      this.getClass().getClassLoader());

      // Zurueckgeben, ob es einen Match gibt
      return(compareClass.isInstance(bean));
    }
    catch(ClassNotFoundException e) {

      throw(new RuntimeException("Angegebener Typ nicht gefunden.", e));
    }
  }


  // ----------------------------------------------------- oeffentliche Methoden

  /* (non-Javadoc)
   * @see org.apache.myfaces.custom.htmlTag.HtmlTag#isRendered()
   */
  public boolean isRendered() {

    String comparison = (String)this.getAttributes().get("comparison");
    Object bean = this.getAttributes().get("bean");
    String expectedValue = (String)this.getAttributes().get("expectedValue");
    String reverse = (String)this.getAttributes().get("reverse");

    // Wenn "comparison" oder Bean-Objekt nicht am Start, wird nicht gerendert
    if((comparison == null) || (bean == null)) {

      return(false);
    }

    // Feststellen, ob es einen Match gibt.
    boolean obAusgabe = false;
    
    if(comparison.equalsIgnoreCase("ContentType")) {

      obAusgabe = this.compareContentTypes(bean, expectedValue);
    }
    else if(comparison.equalsIgnoreCase("MessageType")) {

      obAusgabe = this.compareMessageTypes(bean, expectedValue);
    }
    else {

      throw(new RuntimeException("Ungueltiger comparison-Wert: " + comparison));
    }

    // Ggf. Ausgabe-boolean umdrehen
    if((reverse != null) && Boolean.valueOf(reverse).booleanValue()) {

      obAusgabe = (! obAusgabe);
    }
    
    // Body ggf. ausgeben.
    return(obAusgabe);
  }

}
