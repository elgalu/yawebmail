/*
 * @(#)OutputPerFolderLevelUIComponent.java 1.00 2007/07/25
 *
 * Copyright (c) 2007, Stephan Sann
 *
 * 25.07.2007 ssann        Vers. 1.0     created
 */


package de.lotk.yawebmail.cuic;

import java.io.IOException;

import javax.faces.component.UIOutput;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.mail.Folder;

/**
 * UI-Component zur Ausgabe von Inhalten pro Folder-Ebene
 * 
 * @author Stephan Sann
 * @version 1.0
 */
public class OutputPerFolderLevelUIComponent extends UIOutput {

  /* (non-Javadoc)
   * @see javax.faces.component.UIComponentBase#encodeBegin(javax.faces.context.FacesContext)
   */
  public void encodeBegin(FacesContext context) throws IOException {

    // span-Tag ausgeben
    ResponseWriter writer = context.getResponseWriter();
    writer.startElement("span", this);
    writer.writeAttribute("class", getAttributes().get("styleClass"), null);

    // Part-Content-Object einlesen
    Folder currentFolder = null;

    try {

      currentFolder = ((Folder)getAttributes().get("value"));

      // Wenn Part-Content nicht null, formatieren und ausgeben
      if(currentFolder != null) {

//      String folderName = currentFolder.getFullName();
//      int counter = 0;
//      int offset = 0;
//
//      // Wieviele Separatoren sind im Namen?
//      while((offset = folderName.indexOf('/', offset)) > (-1)) {
//
//        counter++;
//      }

        Folder ancestorFolder = currentFolder;
        int counter = 0;

        // Wieviele "Vorfahren" hat der aktuelle Ordner/
        while((ancestorFolder = ancestorFolder.getParent()) != null) {

          counter++;
        }

        // Pro Separator (Level) einmal den Inhalt in StringBuffer schreiben
        StringBuffer output = new StringBuffer();

        for(int ii = 0; ii < counter; ii++) {

          output.append(getAttributes().get("content"));
        }

        // Part-Content ausgeben
        writer.write(output.toString());
      }
    }
    catch(Exception e) {

      e.printStackTrace();
      throw(new IOException("Probleme beim Folder holen: " + e.getMessage()));
    }
  }

  /* (non-Javadoc)
   * @see javax.faces.component.UIComponentBase#encodeEnd(javax.faces.context.FacesContext)
   */
  public void encodeEnd(FacesContext context) throws IOException {

    ResponseWriter writer = context.getResponseWriter();

    writer.endElement("span");
  }

}
