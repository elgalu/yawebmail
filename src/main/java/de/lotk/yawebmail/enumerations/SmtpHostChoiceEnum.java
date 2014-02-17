/*
 * @(#)SmtpHostChoiceEnum.java 1.00 2007/11/20
 *
 * Copyright (c) 2007, Stephan Sann
 *
 * 20.11.2007 ssann        Vers. 1.0     created
 */


package de.lotk.yawebmail.enumerations;

/**
 * Enum fuer die moeglichen Auswahlmoeglichkeiten des SMTP-Hosts
 * 
 * @author Stephan Sann
 * @version 1.0
 */
public enum SmtpHostChoiceEnum {

  // ---------------------------------------------- Moegliche Enum-Auspraegungen

  FREE("free"), DOMAIN("domain"), NONE("none");


  // --------------------------------------------------------- Klassen-Variablen

  /** Alle Auspraegungen in einem Array */
  private static SmtpHostChoiceEnum[] allValues = SmtpHostChoiceEnum.values();


  // --------------------------------------------------------- Instanz-Variablen

  /** Kuerzel fuer die jeweilige Auspraegung */
  private String id = null;


  // ----------------------------------------------------------- Konstruktor(en)

  /**
   * Initialisiert ein neues SmtpHostChoiceEnum-Objekt.
   * 
   * @param   id  Kuerzel
   */
  private SmtpHostChoiceEnum(String id) {

    this.id = id;
  }


  // --------------------------------------------------------- Getter und Setter

  /**
   * @return the id
   */
  public String getId() {
  
    return id;
  }


  // ----------------------------------------------------- oeffentliche Methoden

  /**
   * Liefert ein SmtpHostChoiceEnum anhand einer ID.
   * 
   * @param   id   Die ID
   * @return  <code>SmtpHostChoiceEnum</code>-Objekt, wenn fuer diese ID eines
   *          gefunden wurde;
   *          <code>null</code>, wenn kein Enum gefunden wurde. 
   */
  public static SmtpHostChoiceEnum byId(String id) {

    for(int ii = 0; ii < allValues.length; ii++) {

      if(allValues[ii].id.equals(id)) {

        return(allValues[ii]);
      }
    }

    return(null);
  }

}
