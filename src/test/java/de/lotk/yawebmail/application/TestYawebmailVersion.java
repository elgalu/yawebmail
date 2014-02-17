/*
 * @(#)TestYawebmailVersion.java 1.00 2008/05/25
 *
 * Copyright (c) 2008, Stephan Sann
 *
 * 25.05.2008 ssann        Vers. 1.0     created
 */


package de.lotk.yawebmail.application;

import junit.framework.TestCase;

/**
 * Tests the class "de.lotk.yawebmail.application.YawebmailVersion"
 * 
 * @author Stephan Sann
 */
public class TestYawebmailVersion extends TestCase {

  // --------------------------------------------------------------- constructor

  /**
   * Initializes a new TestYawebmailVersion-object.
   * 
   * @param   name  Test-name
   */
  public TestYawebmailVersion(String name) {

    super(name);
  }


  // ------------------------------------------------------------ public methods

  /**
   * Tests the "getInstance"-method. 
   */
  public void testGetInstance() {

    YawebmailVersion yv = YawebmailVersion.getInstance("1.2.3");
    assertEquals(1, yv.getMajorVersion());
    assertEquals(2, yv.getMinorVersion());
    assertEquals(3, yv.getUpdateVersion());
    assertEquals(YawebmailVersion.StageIndicatorEnum.FINAL, yv.getStageIndicator());
    assertEquals(0, yv.getStageVersion());

    yv = YawebmailVersion.getInstance("44.55.66a");
    assertEquals(44, yv.getMajorVersion());
    assertEquals(55, yv.getMinorVersion());
    assertEquals(66, yv.getUpdateVersion());
    assertEquals(YawebmailVersion.StageIndicatorEnum.ALPHA, yv.getStageIndicator());
    assertEquals(0, yv.getStageVersion());

    yv = YawebmailVersion.getInstance("777.888.999b42");
    assertEquals(777, yv.getMajorVersion());
    assertEquals(888, yv.getMinorVersion());
    assertEquals(999, yv.getUpdateVersion());
    assertEquals(YawebmailVersion.StageIndicatorEnum.BETA, yv.getStageIndicator());
    assertEquals(42, yv.getStageVersion());

    yv = YawebmailVersion.getInstance("9.8.7dev3");
    assertEquals(YawebmailVersion.StageIndicatorEnum.DEVELOPMENT, yv.getStageIndicator());
    assertEquals(3, yv.getStageVersion());

    yv = YawebmailVersion.getInstance("6.5.4rc69");
    assertEquals(YawebmailVersion.StageIndicatorEnum.RELEASE_CANDIDATE, yv.getStageIndicator());
    assertEquals(69, yv.getStageVersion());

    yv = YawebmailVersion.getInstance("3.2.1f1");
    assertEquals(YawebmailVersion.StageIndicatorEnum.FINAL, yv.getStageIndicator());
    assertEquals(1, yv.getStageVersion());

    // Test exceptions

    try {

      yv = YawebmailVersion.getInstance("3.2");
    }
    catch(Exception e) {

      assertTrue((e instanceof IllegalArgumentException));
      assertEquals("Version-String has to be \"x.x.x[stage-indicator][stage-version]\" (was: 3.2)",
              e.getMessage());
      assertEquals("Given String did not have 3 Parts.",
              e.getCause().getMessage());

    }

    try {

      yv = YawebmailVersion.getInstance("7.a.b");
    }
    catch(Exception e) {

      assertTrue((e instanceof IllegalArgumentException));
      assertEquals("Version-String has to be \"x.x.x[stage-indicator][stage-version]\" (was: 7.a.b)",
              e.getMessage());
      assertTrue(e.getCause() instanceof NumberFormatException);
      assertEquals("For input string: \"a\"", e.getCause().getMessage());
    }
  }

  /**
   * Tests the "compareTo"-method. 
   */
  public void testCompareTo() {

    YawebmailVersion yv1 = YawebmailVersion.getInstance("1.2.3");
    YawebmailVersion yv2 = YawebmailVersion.getInstance("1.2.3");
    assertEquals(0, yv1.compareTo(yv2));

    yv1 = YawebmailVersion.getInstance("4.5.6a");
    yv2 = YawebmailVersion.getInstance("4.5.6a");
    assertEquals(0, yv1.compareTo(yv2));

    yv1 = YawebmailVersion.getInstance("7.8.9b12");
    yv2 = YawebmailVersion.getInstance("7.8.9b12");
    assertEquals(0, yv1.compareTo(yv2));

    yv1 = YawebmailVersion.getInstance("1.1.1");
    yv2 = YawebmailVersion.getInstance("2.2.2");
    assertEquals((-10000), yv1.compareTo(yv2));

    yv1 = YawebmailVersion.getInstance("1.5.0");
    yv2 = YawebmailVersion.getInstance("1.3.5");
    assertEquals(2000, yv1.compareTo(yv2));

    yv1 = YawebmailVersion.getInstance("2.3.4");
    yv2 = YawebmailVersion.getInstance("2.3.7");
    assertEquals((-300), yv1.compareTo(yv2));

    yv1 = YawebmailVersion.getInstance("4.4.4b");
    yv2 = YawebmailVersion.getInstance("4.4.4a");
    assertEquals(10, yv1.compareTo(yv2));

    yv1 = YawebmailVersion.getInstance("11.22.33dev22");
    yv2 = YawebmailVersion.getInstance("11.22.33dev25");
    assertEquals((-3), yv1.compareTo(yv2));

    yv1 = YawebmailVersion.getInstance("2.3.60");
    yv2 = YawebmailVersion.getInstance("2.3.40");
    assertEquals(2000, yv1.compareTo(yv2));
  }

  /**
   * Tests the "toString"-method. 
   */
  public void testToString() {

    YawebmailVersion yv = YawebmailVersion.getInstance("1.2.3");
    String tempExpected =
            "[yawebmail-version] <major>: 1, <minor>: 2, <update>: 3, <stage>: FINAL, <stage-version>: 0";
    assertEquals(tempExpected, yv.toString());

    yv = YawebmailVersion.getInstance("44.55.66a");
    tempExpected =
            "[yawebmail-version] <major>: 44, <minor>: 55, <update>: 66, <stage>: ALPHA, <stage-version>: 0";
    assertEquals(tempExpected, yv.toString());

    yv = YawebmailVersion.getInstance("777.888.999b42");
    tempExpected =
            "[yawebmail-version] <major>: 777, <minor>: 888, <update>: 999, <stage>: BETA, <stage-version>: 42";
    assertEquals(tempExpected, yv.toString());

    yv = YawebmailVersion.getInstance("9.8.7dev3");
    tempExpected =
            "[yawebmail-version] <major>: 9, <minor>: 8, <update>: 7, <stage>: DEVELOPMENT, <stage-version>: 3";
    assertEquals(tempExpected, yv.toString());

    yv = YawebmailVersion.getInstance("6.5.4rc69");
    tempExpected =
            "[yawebmail-version] <major>: 6, <minor>: 5, <update>: 4, <stage>: RELEASE_CANDIDATE, <stage-version>: 69";
    assertEquals(tempExpected, yv.toString());

    yv = YawebmailVersion.getInstance("3.2.1f1");
    tempExpected =
            "[yawebmail-version] <major>: 3, <minor>: 2, <update>: 1, <stage>: FINAL, <stage-version>: 1";
    assertEquals(tempExpected, yv.toString());
  }

  /**
   * Tests the "toSimpleString"-method. 
   */
  public void testToSimpleString() {

    YawebmailVersion yv = YawebmailVersion.getInstance("1.2.3");
    String tempExpected = "1.2.3";
    assertEquals(tempExpected, yv.toSimpleString());

    yv = YawebmailVersion.getInstance("9.8.7dev3");
    tempExpected = "9.8.7";
    assertEquals(tempExpected, yv.toSimpleString());
  }

}
