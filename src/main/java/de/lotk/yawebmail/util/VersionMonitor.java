/*
 * @(#)VersionMonitor.java 1.00 2008/03/18
 *
 * Copyright (c) 2008, Stephan Sann
 *
 * 18.13.2008 ssann        Vers. 1.0     created
 */


package de.lotk.yawebmail.util;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Timer;
import java.util.TimerTask;

import de.lotk.yawebmail.application.Configuration;
import de.lotk.yawebmail.application.YawebmailVersion;

/**
 * Monitors the current yawebmail-version.
 * 
 * @author Stephan Sann
 * @version 1.0
 */
public class VersionMonitor {

  // ----------------------------------------------------------- class-variables

  /** Holds the current Version. Defaults to the one configured */
  private static YawebmailVersion currentVersion =
          Configuration.getProgramVersion();

  /** Holds if current version is greater than the configred version */
  private static boolean currentVersionGreaterThanConfiguredVersion = false;


  // -------------------------------------------------------- static initializer

  static {

    // Create a Timer that looks for a new version every 24 hours
    (new Timer()).schedule(new TimerTask() {

      /* (non-Javadoc)
       * @see java.util.TimerTask#run()
       */
      public void run() {

        updateCurrentVersion();
      }

    }, 5000l, 86400000l);
  }


  // --------------------------------------------------------- getter and setter

  /**
   * @return the currentVersion
   */
  public static YawebmailVersion getCurrentVersion() {

    return currentVersion;
  }

  
  /**
   * @return the currentVersionGreaterThanConfiguredVersion
   */
  public static boolean isCurrentVersionGreaterThanConfiguredVersion() {
  
    return currentVersionGreaterThanConfiguredVersion;
  }


  // ----------------------------------------------------------- private methods

  /**
   * Updates the current version
   */
  private static synchronized void updateCurrentVersion() {

    try {

      URLConnection updateConnection =
              (new URL(Configuration.getUrlCurrentVersion())).openConnection();

      updateConnection.connect();
      InputStream inputStream = updateConnection.getInputStream();
      StringBuilder versionStringBuilder = new StringBuilder();
      int readChar = 0;

      while((readChar = inputStream.read()) != (-1)) {

        versionStringBuilder.append((char)readChar);
      }
      inputStream.close();

      currentVersion =
              YawebmailVersion.getInstance(versionStringBuilder.toString());

      currentVersionGreaterThanConfiguredVersion =
              (currentVersion.compareTo(Configuration.getProgramVersion()) > 0);
    }
    catch(Exception e) {

      // Never mind - just print out a Stacktrace
      e.printStackTrace();
    }
  }

}
