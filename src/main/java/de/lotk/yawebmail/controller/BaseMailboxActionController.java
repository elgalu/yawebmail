/*
 * @(#)BaseMailboxActionController.java 1.00 2006/03/10
 *
 * Copyright (c) 2006, Stephan Sann
 *
 * 10.03.2006 ssann        Vers. 1.0     created
 */


package de.lotk.yawebmail.controller;

import de.lotk.yawebmail.application.Constants;
import de.lotk.yawebmail.bean.SessionContainerBean;
import de.lotk.yawebmail.business.MailboxConnection;

/**
 * Basis-Klasse, die grundlegende Mailbox-Actions wrappt.
 *
 * @author Stephan Sann
 * @version 1.0
 */
public class BaseMailboxActionController extends BaseController {

  // --------------------------------------------------------- Protected Methods

  /**
   * Versucht das MailboxConnection-Objekt aus der Session zu lesen.
   *
   * @return                     <code>MailboxConnection</code>, wenn
   *                             MailboxConnection-Objekt in Session gefunden;
   */
  protected MailboxConnection getMailboxConnection() throws Exception {

    // SessionContainer besorgen.
    SessionContainerBean sessionContainer =
            (SessionContainerBean)this.getManagedBeanByName(Constants.NAME_MBEAN_SESSIONCONTAINER);

	return(sessionContainer.getMailboxConnection());
  }

  /**
   * Versucht den aktuellen Mailbox-Folder aus der Session zu lesen.
   *
   * @return             <code>String</code> mit aktuellem Mailbox-Folder;
   */
  protected String getCurrentMailboxFolder() throws Exception {

    // SessionContainer besorgen.
    SessionContainerBean sessionContainer =
            (SessionContainerBean)this.getManagedBeanByName(Constants.NAME_MBEAN_SESSIONCONTAINER);

    return(sessionContainer.getCurrentMailboxFolder());
  }

}
