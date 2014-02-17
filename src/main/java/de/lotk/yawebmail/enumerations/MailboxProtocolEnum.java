/*
 * @(#)MailboxProtocolEnum.java 1.00 2007/08/30
 *
 * Copyright (c) 2007, Stephan Sann
 *
 * 30.08.2007 ssann        Vers. 1.0     created
 */


package de.lotk.yawebmail.enumerations;

import java.util.HashMap;

/**
 * Enum fuer possible mailbox-protocols
 * 
 * @author Stephan Sann
 * @version 1.0
 */
public enum MailboxProtocolEnum {

  // ------------------------------------------------------ possible enum-values

  POP3("pop3", false), IMAP("imap", false), POP3_SSL("pop3s", true),
          IMAP_SSL("imaps", true);


  // ---------------------------------------------------------------- class-vars

  private static HashMap<String, MailboxProtocolEnum> byProtocolIdMap =
          new HashMap<String, MailboxProtocolEnum>();


  // ------------------------------------------------------------- instance-vars

  /** Protocol-ID of the particular enum-value */
  private String protocolId = null;

  /** If the certain protocol uses SSL */
  private boolean useOfSsl = false;


  // -------------------------------------------------------- static initializer

  static {

    for(MailboxProtocolEnum mpe : MailboxProtocolEnum.values()) {

      byProtocolIdMap.put(mpe.protocolId, mpe);
    }
  }


  // ------------------------------------------------------------- contructor(s)

  /**
   * Initializes a new MailboxProtocolEnum-object.
   * 
   * @param   protocolId  Protocol-ID
   * @param   useOfSsl    If SSL is being used.
   */
  private MailboxProtocolEnum(String protocolId, boolean useOfSsl) {

    this.protocolId = protocolId;
    this.useOfSsl = useOfSsl;
  }


  // --------------------------------------------------------- getter und setter

  /**
   * @return the protocolId
   */
  public String getProtocolId() {
  
    return protocolId;
  }
  
  /**
   * @return the useOfSsl
   */
  public boolean isUseOfSsl() {
  
    return useOfSsl;
  }


  // ------------------------------------------------------------ public methods

  /**
   * Liefert ein MailboxProtocolEnum anhand einer Protokoll-ID.
   * 
   * @param   protocolId  Die Protokoll-ID ("pop3", "imap", ...)
   * @return  <code>MailboxProtocolEnum</code>-Objekt, wenn fuer diese
   *          Protokoll-ID ein Mailbox-Protokoll gefunden wurde;
   *          <code>null</code>, wenn kein Mailbox-Protokoll gefunden wurde. 
   */
  public static MailboxProtocolEnum byProtocolId(String protocolId) {

    return(byProtocolIdMap.get(protocolId));
  }

}
