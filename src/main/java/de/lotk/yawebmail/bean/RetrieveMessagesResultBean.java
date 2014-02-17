/*
 * @(#)RetrieveMessagesResultBean.java 1.00 2008/08/15
 *
 * Copyright (c) 2008, Stephan Sann
 *
 * 15.08.2008 ssann        Vers. 1.0     created
 */


package de.lotk.yawebmail.bean;

import java.io.Serializable;

import javax.mail.Message;

import de.lotk.yawebmail.application.Lifecycle;

/**
 * Bean that takes the result-data of a message-retrieve-action 
 * 
 * @author Stephan Sann
 * @version 1.0
 */
public class RetrieveMessagesResultBean implements Lifecycle, Serializable {

  // ----------------------------------------------------------------- constants

  /** serialVersionUID */
  private static final long serialVersionUID = -8491627953285540966L;


  // ------------------------------------------------------------- instance-vars

  /** Hold the message-array */
  private Message[] messages = null;

  /** Holds the (overall) amount of messages within the message-folder */
  private int overallMessageCount = 0;


  // --------------------------------------------------------- getter und setter

  /**
   * @return the messages
   */
  public Message[] getMessages() {
  
    return messages;
  }

  /**
   * @param messages the messages to set
   */
  public void setMessages(Message[] messages) {
  
    this.messages = messages;
  }

  /**
   * @return the overallMessageCount
   */
  public int getOverallMessageCount() {
  
    return overallMessageCount;
  }

  /**
   * @param overallMessageCount the overallMessageCount to set
   */
  public void setOverallMessageCount(int overallMessageCount) {
  
    this.overallMessageCount = overallMessageCount;
  }


  // ----------------------------------------------------- oeffentliche Methoden

  /**
   * sets the bean back
   */
  public void reset() {

    this.messages = null;
    this.overallMessageCount = 0;
  }

  /* (non-Javadoc)
   * @see de.lotk.webftp.Lifecycle#destroy()
   */
  public void destroy() {

    this.reset();
  }

}
