/*
 * @(#)OutputPerFolderLevelTag.java 1.00 2007/07/25
 *
 * Copyright (c) 2007, Stephan Sann
 *
 * 25.07.2007 ssann        Vers. 1.0     created
 */


package de.lotk.yawebmail.taglibs.cuic;

import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import javax.faces.webapp.UIComponentTag;

/**
 * Tag-Handler-Klasse zur Ausgabe von Inhalten pro Folder-Ebene
 * 
 * @author Stephan Sann
 * @version 1.0
 */
public class OutputPerFolderLevelTag extends UIComponentTag {

  // ---------------------------------------------------------------- Konstanten

  /** Component-Type */
  public static final String COMPONENT_TYPE =
          "de.lotk.yawebmail.cuic.outputPerFolderLevel";


  // --------------------------------------------------------- Instanz-Variablen

  /** Per Attribut uebergebenes Value */
  private String value = null;

  /** Per Attribut uebergebene Style-Class */
  private String styleClass = null;

  /** Per Attribut uebergebener Content */
  private String content = null;


  // --------------------------------------------------------- Setter und Getter

  /**
   * @param value The value to set.
   */
  public void setValue(String value) {

    this.value = value;
  }

  /**
   * @param styleClass The styleClass to set.
   */
  public void setStyleClass(String styleClass) {
  
    this.styleClass = styleClass;
  }
  
  /**
   * @param content the content to set
   */
  public void setContent(String content) {
  
    this.content = content;
  }


  // ---------------------------------------------------------- private Methoden

  /**
   * Setzt ein Property in eine Komponente (Helfer-Methode fuer "setProperties")
   * 
   * @param   component        Komponente, fuer die das Property gesetzt wird.
   * @param   propertyName     Name des Property, das gesetzt wird.
   * @param   propertyContent  Inhalt des Property, das gesetzt wird.
   */
  private void setOneComponentProperty(UIComponent component, String
          propertyName, String propertyContent) {

    // value-Attribut setzen
    if(propertyContent != null) {

      if(isValueReference(propertyContent)) {

        FacesContext context = FacesContext.getCurrentInstance();
        Application app = context.getApplication();
        ValueBinding vb = app.createValueBinding(propertyContent);
        component.setValueBinding(propertyName, vb);                  
      }
      else {

        component.getAttributes().put(propertyName, propertyContent);
      }
    }                         
  }


  // ----------------------------------------------------- oeffentliche Methoden

  /* (non-Javadoc)
   * @see javax.faces.webapp.UIComponentTag#getComponentType()
   */
  public String getComponentType() {

    return(COMPONENT_TYPE);
  }

  /* (non-Javadoc)
   * @see javax.faces.webapp.UIComponentTag#getRendererType()
   */
  public String getRendererType() {

    // null means the component renders itself
    return(null);
  }

  /* (non-Javadoc)
   * @see javax.faces.webapp.UIComponentTag#setProperties(javax.faces.component.UIComponent)
   */
  protected void setProperties(UIComponent component) {

    super.setProperties(component);

    // Alle Attribute als Property in die Komponente setzen
    this.setOneComponentProperty(component, "value", this.value);
    this.setOneComponentProperty(component, "styleClass", this.styleClass);
    this.setOneComponentProperty(component, "content", this.content);
  }

  /* (non-Javadoc)
   * @see javax.faces.webapp.UIComponentTag#release()
   */
  public void release() {

    super.release();

    this.value = null;
    this.styleClass = null;
    this.content = null;
  }

}
