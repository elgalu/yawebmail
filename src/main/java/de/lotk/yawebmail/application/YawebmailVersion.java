/*
 * @(#)YawebmailVersion.java 1.00 2008/05/25
 *
 * Copyright (c) 2008, Stephan Sann
 *
 * 25.05.2008 ssann        Vers. 1.0     created
 */


package de.lotk.yawebmail.application;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Represents a comparable yawebmail-version
 * 
 * @author Stephan Sann
 * @version 1.0
 */
public class YawebmailVersion implements Comparable<YawebmailVersion> {

  // ----------------------------------------------------------------- constants

  /** Message if a version-string can not be parsed */
  private static final String VERSION_STRING_EXCEPTION_MESSAGE =
          "Version-String has to be \"x.x.x[stage-indicator][stage-version]\"";


  // ------------------------------------------------------------- instance-vars

  /** Holds the major version */
  private int majorVersion = 0;

  /** Holds the minor version */
  private int minorVersion = 0;

  /** Holds the update version */
  private int updateVersion = 0;

  /** Holds the stage-indicator (defaults to final) */
  private StageIndicatorEnum stageIndicator = StageIndicatorEnum.FINAL;

  /** Holds the stage-version */
  private int stageVersion = 0;
  
  /**
   * @return the majorVersion
   */
  public int getMajorVersion() {
  
    return majorVersion;
  }


  // ------------------------------------------------------------- construcor(s)

  /**
   * Private constructor. Instances should be created by "getInstance". 
   * 
   * @see  #getInstance(String)
   */
  private YawebmailVersion() {
  }


  // --------------------------------------------------------- getter and setter

  /**
   * @param majorVersion the majorVersion to set
   */
  public void setMajorVersion(int majorVersion) {
  
    this.majorVersion = majorVersion;
  }

  /**
   * @return the minorVersion
   */
  public int getMinorVersion() {
  
    return minorVersion;
  }

  /**
   * @param minorVersion the minorVersion to set
   */
  public void setMinorVersion(int minorVersion) {
  
    this.minorVersion = minorVersion;
  }

  /**
   * @return the stageIndicator
   */
  public StageIndicatorEnum getStageIndicator() {
  
    return stageIndicator;
  }

  /**
   * @param stageIndicator the stageIndicator to set
   */
  public void setStageIndicator(StageIndicatorEnum stageIndicator) {
  
    this.stageIndicator = stageIndicator;
  }

  /**
   * @return the stageVersion
   */
  public int getStageVersion() {
  
    return stageVersion;
  }

  /**
   * @param stageVersion the stageVersion to set
   */
  public void setStageVersion(int stageVersion) {
  
    this.stageVersion = stageVersion;
  }

  /**
   * @return the updateVersion
   */
  public int getUpdateVersion() {
  
    return updateVersion;
  }

  /**
   * @param updateVersion the updateVersion to set
   */
  public void setUpdateVersion(int updateVersion) {
  
    this.updateVersion = updateVersion;
  }


  // ------------------------------------------------------------ public methods

  /**
   * Assembles a YawebmailVersion-object by a given String.
   * 
   * @param   aVersionString  String to assemble a YawebmailVersion-object from
   * @param   <code>YawebmailVersion</code>-object.
   * 
   * @throws  <code>IllegalArgumentException</code>, if the given version-String
   *          can not be resolved.
   */
  public static YawebmailVersion getInstance(String aVersionString) throws
          IllegalArgumentException {

    try {

      YawebmailVersion result = new YawebmailVersion();
      String[] tempVersionParts = aVersionString.split("\\.");

      if(tempVersionParts.length != 3) {

        throw(new Exception("Given String did not have 3 Parts."));
      }

      result.majorVersion = Integer.parseInt(tempVersionParts[0]);
      result.minorVersion = Integer.parseInt(tempVersionParts[1]);

      Pattern thirdVersionPartPattern =
              Pattern.compile("(\\d+)([a-zAZ]*)(\\d*)");
      Matcher thirdVersionPartMatcher =
              thirdVersionPartPattern.matcher(tempVersionParts[2]);

      if(thirdVersionPartMatcher.find()) {

        result.updateVersion =
              Integer.parseInt(thirdVersionPartMatcher.group(1));

        String tempSic = thirdVersionPartMatcher.group(2);

        if((tempSic) != null && (tempSic.trim().length() >= 1)) {

          result.stageIndicator =
                  StageIndicatorEnum.byStageIndicatorCode(tempSic);
        }

        String tempSv = thirdVersionPartMatcher.group(3);

        if((tempSv) != null && (tempSv.trim().length() >= 1)) {

          result.stageVersion = Integer.parseInt(tempSv);
        }
      }

      return(result);
    }
    catch(Exception e) {

      throw(new IllegalArgumentException((VERSION_STRING_EXCEPTION_MESSAGE +
              " (was: " + aVersionString + ")"), e));
    }
  }

  /* (non-Javadoc)
   * @see java.lang.Comparable#compareTo(java.lang.Object)
   */
  public int compareTo(YawebmailVersion yvtc) {

    // compare major version
    if(this.majorVersion != yvtc.majorVersion) {

      return((this.majorVersion - yvtc.majorVersion) * 10000);
    }

    // compare minor version
    if(this.minorVersion != yvtc.minorVersion) {

      return((this.minorVersion - yvtc.minorVersion) * 1000);
    }

    // compare update version
    if(this.updateVersion != yvtc.updateVersion) {

      return((this.updateVersion - yvtc.updateVersion) * 100);
    }

    // compare stage-indicator
    int stageIndicatorCompareValue =
            this.stageIndicator.compareTo(yvtc.stageIndicator);

    if(stageIndicatorCompareValue != 0) {

      return(stageIndicatorCompareValue * 10);
    }

    // Not returned yet? Then finally it's up to the stage-version...
    return(this.stageVersion - yvtc.stageVersion);
  }

  /* (non-Javadoc)
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {

    StringBuilder tempResult = new StringBuilder("[yawebmail-version] <major>: ");
    tempResult.append(this.majorVersion).append(", <minor>: ");
    tempResult.append(this.minorVersion).append(", <update>: ");
    tempResult.append(this.updateVersion).append(", <stage>: ");
    tempResult.append(this.stageIndicator).append(", <stage-version>: ");
    tempResult.append(this.stageVersion);

    return(tempResult.toString());
  }

  /**
   * Returns a simple version-string. No stage-version-infos.
   * 
   * @return  <code>String</code>-object containing the version.
   */
  public String toSimpleString() {

    StringBuilder tempResult = new StringBuilder();
    tempResult.append(this.majorVersion).append(".").append(this.minorVersion);
    tempResult.append(".").append(this.updateVersion);

    return(tempResult.toString());
  }


  // ----------------------------------------------------- inner classes / enums

  /** An enum for the stage indicators */
  public enum StageIndicatorEnum {

    DEVELOPMENT("dev"), ALPHA("a"), BETA("b"), RELEASE_CANDIDATE("rc"),
            FINAL("f");

    /** All values in one array */
    private static Map<String, StageIndicatorEnum> allValuesByCode = null;

    /** Holds the stage-indicator-code */
    private String stageIndicatorCode = null;

    static {

      allValuesByCode = new HashMap<String, StageIndicatorEnum>();

      for(StageIndicatorEnum sie : StageIndicatorEnum.values()) {

        allValuesByCode.put(sie.getStageIndicatorCode(), sie);
      }
    }

    /**
     * Initializes a new StageIndicatorEnum-object.
     * 
     * @param   aStageIndicatorCode  StageIndicatorCode-String
     */
    private StageIndicatorEnum(String aStageIndicatorCode) {

      this.stageIndicatorCode = aStageIndicatorCode;
    }

    /**
     * @return the stageIndicatorCode
     */
    public String getStageIndicatorCode() {

      return(this.stageIndicatorCode);
    }

    /**
     * Get a StageIndicatorEnum-object by it's stage-indicator-code.
     * 
     * @param   aStageIndicatorCode  StageIndicatorCode-String.
     * @return  <code>StageIndicatorEnum</code>-object, if found;
     *          <code>null</code>, if no Enum-object is found for given code.
     */
    public static StageIndicatorEnum byStageIndicatorCode(String
            aStageIndicatorCode) {

      return(allValuesByCode.get(aStageIndicatorCode));
    }
  }

}
