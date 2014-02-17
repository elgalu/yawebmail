/*
 * @(#)HtmlTagIfTag.java 1.00 2007/08/10
 *
 * Copyright (c) 2007, Stephan Sann
 *
 * 10.08.2007 ssann        Vers. 1.0     created
 */


package de.lotk.yawebmail.taglibs.cuic;

import javax.faces.component.UIComponent;

import org.apache.myfaces.custom.htmlTag.HtmlTagTag;

/**
 * Tag-Handler-Klasse zum HtmlTagIfUIComponent. 
 * 
 * @author Stephan Sann
 * @version 1.0
 */
public class HtmlTagIfTag extends HtmlTagTag {

  // ---------------------------------------------------------------- Konstanten

  /** Component-Type */
  public static final String COMPONENT_TYPE =
          "de.lotk.yawebmail.cuic.htmlTagIf";


  // --------------------------------------------------------- Instanz-Variablen

  /** Das Bean, dessen Value wir betrachten */
  private String bean = null;

  /** Was soll verglichen werden? */
  private String comparison = null;

  /** Der Wert, bei dem gerendert werden soll */
  private String expectedValue = null;

  /** Ggf. koennen wir hier die Logik umdrehen */
  private String reverse = null;


  // --------------------------------------------------------- Setter und Getter

  /**
   * @param bean the bean to set
   */
  public void setBean(String bean) {
  
    this.bean = bean;
  }

  /**
   * @param comparison the comparison to set
   */
  public void setComparison(String comparison) {
  
    this.comparison = comparison;
  }

  /**
   * @param expectedValue the expectedValue to set
   */
  public void setExpectedValue(String expectedValue) {
  
    this.expectedValue = expectedValue;
  }

  /**
   * @param reverse the reverse to set
   */
  public void setReverse(String reverse) {
  
    this.reverse = reverse;
  }


  // ----------------------------------------------------- oeffentliche Methoden

  /**
   * Liefert den Componenten-Typ (siehe faces-config.xml), der hinter diesem Tag
   * steht.
   */
  public String getComponentType() {

    return(COMPONENT_TYPE);
  }

  /* (non-Javadoc)
   * @see javax.faces.webapp.UIComponentTag#setProperties(javax.faces.component.UIComponent)
   */
  protected void setProperties(UIComponent component) {

    super.setProperties(component);

    // Alle Attribute als Property in die Komponente setzen
    setStringProperty(component, "bean", this.bean);
    setStringProperty(component, "comparison", this.comparison);
    setStringProperty(component, "expectedValue", this.expectedValue);
    setStringProperty(component, "reverse", this.reverse);
  }

  /* (non-Javadoc)
   * @see javax.faces.webapp.UIComponentTag#release()
   */
  public void release() {

    super.release();

    this.bean = null;
    this.comparison = null;
    this.expectedValue = null;
    this.reverse = null;
  }

}
