/*
 * @(#)TestLogonController.java 1.00 2007/12/03
 *
 * Copyright (c) 2007, Stephan Sann
 *
 * 03.12.2007 ssann        Vers. 1.0     created
 */


package de.lotk.yawebmail.controller;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import junit.framework.TestCase;
import de.lotk.yawebmail.application.Configuration;
import de.lotk.yawebmail.bean.LoginDataBean;
import de.lotk.yawebmail.bean.SmtpConnectionBean;
import de.lotk.yawebmail.enumerations.SmtpHostChoiceEnum;

/**
 * Tests the class "de.lotk.yawebmail.controller.LogonController"
 * 
 * @author Stephan Sann
 */
public class TestLogonController extends TestCase {

  // --------------------------------------------------------------- constructor

  /**
   * Initializes a new TestLogonController-object.
   * 
   * @param   name  Test-name
   */
  public TestLogonController(String name) {

    super(name);
  }


  // ----------------------------------------------------------- private methods

  /**
   * Gets the value of a private field.
   * 
   * @param   objectClass     <code>Class</code>-object that defines the field.
   * @param   objectInstance  <code>Object</code>-object that holds the value.
   * @return  <code>Object</code>-object representing the value.
   */
  // Warning with parameter "Class objectClass" (without "<?>")? Use:
  // @SuppressWarnings("unchecked")
  private Object getPrivateFieldValue(Class<?> objectClass, Object objectInstance,
          String fieldName) throws Exception {

    Field field = objectClass.getDeclaredField(fieldName);
    field.setAccessible(true);

    return(field.get(objectInstance));
  }

  /**
   * Gets a accessible "prefillSmtpConnection"-method.
   */
  private Method getAccessiblePrefillSmtpConnectionMethod() throws Exception {

    // Get the private method and set it accessible
    Class<?>[] parameterTypes =
            { SmtpConnectionBean.class, LoginDataBean.class };
    Method prefillSmtpConnectionMethod =
            LogonController.class.getDeclaredMethod("prefillSmtpConnection",
                    parameterTypes);
    prefillSmtpConnectionMethod.setAccessible(true);

    return(prefillSmtpConnectionMethod);
  }


  // ------------------------------------------------------------ public methods

  /**
   * Tests the "prefillSmtpConnection"-method with SMTP-host-choice "domain". 
   */
  public void testPrefillSmtpConnection_smtpHostChoiceDomain() {

    Configuration.setSmtpHostChoice(SmtpHostChoiceEnum.DOMAIN);

    try {

      Method prefillSmtpConnectionMethod =
              this.getAccessiblePrefillSmtpConnectionMethod();

      // Prepare needed objects
      LogonController logonController = new LogonController();
      SmtpConnectionBean smtpConnectionBean = new SmtpConnectionBean();
      LoginDataBean loginDataBean = new LoginDataBean();
      Object[] parameterObjects = { smtpConnectionBean, loginDataBean };

      // Check method with SMTP-host-choice "domain"

      loginDataBean.setMailboxHost("pop.domain1.tld");
      prefillSmtpConnectionMethod.invoke(logonController, parameterObjects);
      assertEquals("smtp.", smtpConnectionBean.getSmtpSubdomainPrefix());
      assertEquals("domain1.tld", smtpConnectionBean.getSmtpDomain());
      // get the real field-value - the getSmtpHost() assembles a SMTP-host...
      assertEquals(null, this.getPrivateFieldValue(SmtpConnectionBean.class,
              smtpConnectionBean, "smtpHost"));
      // ...from smtp-subdomain-prefix and smtp-domain
      assertEquals("smtp.domain1.tld", smtpConnectionBean.getSmtpHost());
      smtpConnectionBean.reset();

      loginDataBean.setMailboxHost("pop3.domain2.tld");
      prefillSmtpConnectionMethod.invoke(logonController, parameterObjects);
      assertEquals("smtp.", smtpConnectionBean.getSmtpSubdomainPrefix());
      assertEquals("domain2.tld", smtpConnectionBean.getSmtpDomain());
      // get the real field-value - the getSmtpHost() assembles a SMTP-host...
      assertEquals(null, this.getPrivateFieldValue(SmtpConnectionBean.class,
              smtpConnectionBean, "smtpHost"));
      // ...from smtp-subdomain-prefix and smtp-domain
      assertEquals("smtp.domain2.tld", smtpConnectionBean.getSmtpHost());
      smtpConnectionBean.reset();

      loginDataBean.setMailboxHost("imap.domain3.tld");
      prefillSmtpConnectionMethod.invoke(logonController, parameterObjects);
      assertEquals("smtp.", smtpConnectionBean.getSmtpSubdomainPrefix());
      assertEquals("domain3.tld", smtpConnectionBean.getSmtpDomain());
      // get the real field-value - the getSmtpHost() assembles a SMTP-host...
      assertEquals(null, this.getPrivateFieldValue(SmtpConnectionBean.class,
              smtpConnectionBean, "smtpHost"));
      // ...from smtp-subdomain-prefix and smtp-domain
      assertEquals("smtp.domain3.tld", smtpConnectionBean.getSmtpHost());
      smtpConnectionBean.reset();

      loginDataBean.setMailboxHost("imap4.domain4.tld");
      prefillSmtpConnectionMethod.invoke(logonController, parameterObjects);
      assertEquals("smtp.", smtpConnectionBean.getSmtpSubdomainPrefix());
      assertEquals("domain4.tld", smtpConnectionBean.getSmtpDomain());
      // get the real field-value - the getSmtpHost() assembles a SMTP-host...
      assertEquals(null, this.getPrivateFieldValue(SmtpConnectionBean.class,
              smtpConnectionBean, "smtpHost"));
      // ...from smtp-subdomain-prefix and smtp-domain
      assertEquals("smtp.domain4.tld", smtpConnectionBean.getSmtpHost());
      smtpConnectionBean.reset();

      loginDataBean.setMailboxHost("pop3s.domain5.tld");
      prefillSmtpConnectionMethod.invoke(logonController, parameterObjects);
      assertEquals("smtp.", smtpConnectionBean.getSmtpSubdomainPrefix());
      assertEquals("domain5.tld", smtpConnectionBean.getSmtpDomain());
      // get the real field-value - the getSmtpHost() assembles a SMTP-host...
      assertEquals(null, this.getPrivateFieldValue(SmtpConnectionBean.class,
              smtpConnectionBean, "smtpHost"));
      // ...from smtp-subdomain-prefix and smtp-domain
      assertEquals("smtp.domain5.tld", smtpConnectionBean.getSmtpHost());
      smtpConnectionBean.reset();

      loginDataBean.setMailboxHost("imaps.domain6.tld");
      prefillSmtpConnectionMethod.invoke(logonController, parameterObjects);
      assertEquals("smtp.", smtpConnectionBean.getSmtpSubdomainPrefix());
      assertEquals("domain6.tld", smtpConnectionBean.getSmtpDomain());
      // get the real field-value - the getSmtpHost() assembles a SMTP-host...
      assertEquals(null, this.getPrivateFieldValue(SmtpConnectionBean.class,
              smtpConnectionBean, "smtpHost"));
      // ...from smtp-subdomain-prefix and smtp-domain
      assertEquals("smtp.domain6.tld", smtpConnectionBean.getSmtpHost());
      smtpConnectionBean.reset();

      loginDataBean.setMailboxHost("domain7.tld");
      prefillSmtpConnectionMethod.invoke(logonController, parameterObjects);
      assertEquals(null, smtpConnectionBean.getSmtpSubdomainPrefix());
      assertEquals("domain7.tld", smtpConnectionBean.getSmtpDomain());
      // get the real field-value - the getSmtpHost() assembles a SMTP-host...
      assertEquals(null, this.getPrivateFieldValue(SmtpConnectionBean.class,
              smtpConnectionBean, "smtpHost"));
      // ...from smtp-subdomain-prefix and smtp-domain
      assertEquals("domain7.tld", smtpConnectionBean.getSmtpHost());
      smtpConnectionBean.reset();

      loginDataBean.setMailboxHost("pop3mail.domain8.tld");
      prefillSmtpConnectionMethod.invoke(logonController, parameterObjects);
      assertEquals(null, smtpConnectionBean.getSmtpSubdomainPrefix());
      assertEquals("pop3mail.domain8.tld", smtpConnectionBean.getSmtpDomain());
      // get the real field-value - the getSmtpHost() assembles a SMTP-host...
      assertEquals(null, this.getPrivateFieldValue(SmtpConnectionBean.class,
              smtpConnectionBean, "smtpHost"));
      // ...from smtp-subdomain-prefix and smtp-domain
      assertEquals("pop3mail.domain8.tld", smtpConnectionBean.getSmtpHost());
      smtpConnectionBean.reset();
    }
    catch(Exception e) {

      e.printStackTrace();
      fail(e.getMessage());
    }
  }

  /**
   * Tests the "prefillSmtpConnection"-method with SMTP-host-choice "free". 
   */
  public void testPrefillSmtpConnection_smtpHostChoiceFree() {

    Configuration.setSmtpHostChoice(SmtpHostChoiceEnum.FREE);

    try {

      Method prefillSmtpConnectionMethod =
              this.getAccessiblePrefillSmtpConnectionMethod();

      // Prepare needed objects
      LogonController logonController = new LogonController();
      SmtpConnectionBean smtpConnectionBean = new SmtpConnectionBean();
      LoginDataBean loginDataBean = new LoginDataBean();
      Object[] parameterObjects = { smtpConnectionBean, loginDataBean };

      // Check method with SMTP-host-choice "free"

      loginDataBean.setMailboxHost("pop.domain1.tld");
      prefillSmtpConnectionMethod.invoke(logonController, parameterObjects);
      // get the real field-value - the getSmtpHost() could assemble a value...
      assertEquals("smtp.domain1.tld",
              this.getPrivateFieldValue(SmtpConnectionBean.class,
                       smtpConnectionBean, "smtpHost"));
      // ...from smtp-subdomain-prefix and smtp-domain
      assertEquals("smtp.domain1.tld", smtpConnectionBean.getSmtpHost());
      smtpConnectionBean.reset();

      loginDataBean.setMailboxHost("pop3.domain2.tld");
      prefillSmtpConnectionMethod.invoke(logonController, parameterObjects);
      // get the real field-value - the getSmtpHost() could assemble a value...
      assertEquals("smtp.domain2.tld",
              this.getPrivateFieldValue(SmtpConnectionBean.class,
                       smtpConnectionBean, "smtpHost"));
      // ...from smtp-subdomain-prefix and smtp-domain
      assertEquals("smtp.domain2.tld", smtpConnectionBean.getSmtpHost());
      smtpConnectionBean.reset();

      loginDataBean.setMailboxHost("imap.domain3.tld");
      prefillSmtpConnectionMethod.invoke(logonController, parameterObjects);
      // get the real field-value - the getSmtpHost() could assemble a value...
      assertEquals("smtp.domain3.tld",
              this.getPrivateFieldValue(SmtpConnectionBean.class,
                       smtpConnectionBean, "smtpHost"));
      // ...from smtp-subdomain-prefix and smtp-domain
      assertEquals("smtp.domain3.tld", smtpConnectionBean.getSmtpHost());
      smtpConnectionBean.reset();

      loginDataBean.setMailboxHost("imap4.domain4.tld");
      prefillSmtpConnectionMethod.invoke(logonController, parameterObjects);
      // get the real field-value - the getSmtpHost() could assemble a value...
      assertEquals("smtp.domain4.tld",
              this.getPrivateFieldValue(SmtpConnectionBean.class,
                       smtpConnectionBean, "smtpHost"));
      // ...from smtp-subdomain-prefix and smtp-domain
      assertEquals("smtp.domain4.tld", smtpConnectionBean.getSmtpHost());
      smtpConnectionBean.reset();

      loginDataBean.setMailboxHost("pop3s.domain5.tld");
      prefillSmtpConnectionMethod.invoke(logonController, parameterObjects);
      // get the real field-value - the getSmtpHost() could assemble a value...
      assertEquals("smtp.domain5.tld",
              this.getPrivateFieldValue(SmtpConnectionBean.class,
                       smtpConnectionBean, "smtpHost"));
      // ...from smtp-subdomain-prefix and smtp-domain
      assertEquals("smtp.domain5.tld", smtpConnectionBean.getSmtpHost());
      smtpConnectionBean.reset();

      loginDataBean.setMailboxHost("imaps.domain6.tld");
      prefillSmtpConnectionMethod.invoke(logonController, parameterObjects);
      // get the real field-value - the getSmtpHost() could assemble a value...
      assertEquals("smtp.domain6.tld",
              this.getPrivateFieldValue(SmtpConnectionBean.class,
                       smtpConnectionBean, "smtpHost"));
      // ...from smtp-subdomain-prefix and smtp-domain
      assertEquals("smtp.domain6.tld", smtpConnectionBean.getSmtpHost());
      smtpConnectionBean.reset();

      loginDataBean.setMailboxHost("domain7.tld");
      prefillSmtpConnectionMethod.invoke(logonController, parameterObjects);
      // get the real field-value - the getSmtpHost() could assemble a value...
      assertEquals("domain7.tld",
              this.getPrivateFieldValue(SmtpConnectionBean.class,
                       smtpConnectionBean, "smtpHost"));
      // ...from smtp-subdomain-prefix and smtp-domain
      assertEquals("domain7.tld", smtpConnectionBean.getSmtpHost());
      smtpConnectionBean.reset();

      loginDataBean.setMailboxHost("pop3mail.domain8.tld");
      prefillSmtpConnectionMethod.invoke(logonController, parameterObjects);
      // get the real field-value - the getSmtpHost() could assemble a value...
      assertEquals("pop3mail.domain8.tld",
              this.getPrivateFieldValue(SmtpConnectionBean.class,
                       smtpConnectionBean, "smtpHost"));
      // ...from smtp-subdomain-prefix and smtp-domain
      assertEquals("pop3mail.domain8.tld", smtpConnectionBean.getSmtpHost());
      smtpConnectionBean.reset();
    }
    catch(Exception e) {

      e.printStackTrace();
      fail(e.getMessage());
    }
  }

  /**
   * Tests the "prefillSmtpConnection"-method with SMTP-host-choice "none". 
   */
  public void testPrefillSmtpConnection_smtpHostChoiceNone() {

    Configuration.setSmtpHostChoice(SmtpHostChoiceEnum.NONE);

    try {

      Method prefillSmtpConnectionMethod =
              this.getAccessiblePrefillSmtpConnectionMethod();

      // Prepare needed objects
      LogonController logonController = new LogonController();
      SmtpConnectionBean smtpConnectionBean = new SmtpConnectionBean();
      LoginDataBean loginDataBean = new LoginDataBean();
      Object[] parameterObjects = { smtpConnectionBean, loginDataBean };

      // Check method with SMTP-host-choice "none"

      Configuration.setForcedSmtpHostName("forced.domain1.tld");
      loginDataBean.setMailboxHost("pop.domain1.tld");
      prefillSmtpConnectionMethod.invoke(logonController, parameterObjects);
      // get the real field-value - the getSmtpHost() could assemble a value...
      assertEquals("forced.domain1.tld",
              this.getPrivateFieldValue(SmtpConnectionBean.class,
                       smtpConnectionBean, "smtpHost"));
      // ...from smtp-subdomain-prefix and smtp-domain
      assertEquals("forced.domain1.tld", smtpConnectionBean.getSmtpHost());
      smtpConnectionBean.reset();

      Configuration.setForcedSmtpHostName("forced.domain2.tld");
      loginDataBean.setMailboxHost("pop3.domain2.tld");
      prefillSmtpConnectionMethod.invoke(logonController, parameterObjects);
      // get the real field-value - the getSmtpHost() could assemble a value...
      assertEquals("forced.domain2.tld",
              this.getPrivateFieldValue(SmtpConnectionBean.class,
                       smtpConnectionBean, "smtpHost"));
      // ...from smtp-subdomain-prefix and smtp-domain
      assertEquals("forced.domain2.tld", smtpConnectionBean.getSmtpHost());
      smtpConnectionBean.reset();

      Configuration.setForcedSmtpHostName("forced.domain3.tld");
      loginDataBean.setMailboxHost("imap.domain3.tld");
      prefillSmtpConnectionMethod.invoke(logonController, parameterObjects);
      // get the real field-value - the getSmtpHost() could assemble a value...
      assertEquals("forced.domain3.tld",
              this.getPrivateFieldValue(SmtpConnectionBean.class,
                       smtpConnectionBean, "smtpHost"));
      // ...from smtp-subdomain-prefix and smtp-domain
      assertEquals("forced.domain3.tld", smtpConnectionBean.getSmtpHost());
      smtpConnectionBean.reset();

      Configuration.setForcedSmtpHostName("forced.domain4.tld");
      loginDataBean.setMailboxHost("imap4.domain4.tld");
      prefillSmtpConnectionMethod.invoke(logonController, parameterObjects);
      // get the real field-value - the getSmtpHost() could assemble a value...
      assertEquals("forced.domain4.tld",
              this.getPrivateFieldValue(SmtpConnectionBean.class,
                       smtpConnectionBean, "smtpHost"));
      // ...from smtp-subdomain-prefix and smtp-domain
      assertEquals("forced.domain4.tld", smtpConnectionBean.getSmtpHost());
      smtpConnectionBean.reset();

      Configuration.setForcedSmtpHostName("forced.domain5.tld");
      loginDataBean.setMailboxHost("pop3s.domain5.tld");
      prefillSmtpConnectionMethod.invoke(logonController, parameterObjects);
      // get the real field-value - the getSmtpHost() could assemble a value...
      assertEquals("forced.domain5.tld",
              this.getPrivateFieldValue(SmtpConnectionBean.class,
                       smtpConnectionBean, "smtpHost"));
      // ...from smtp-subdomain-prefix and smtp-domain
      assertEquals("forced.domain5.tld", smtpConnectionBean.getSmtpHost());
      smtpConnectionBean.reset();

      Configuration.setForcedSmtpHostName("forced.domain6.tld");
      loginDataBean.setMailboxHost("imaps.domain6.tld");
      prefillSmtpConnectionMethod.invoke(logonController, parameterObjects);
      // get the real field-value - the getSmtpHost() could assemble a value...
      assertEquals("forced.domain6.tld",
              this.getPrivateFieldValue(SmtpConnectionBean.class,
                       smtpConnectionBean, "smtpHost"));
      // ...from smtp-subdomain-prefix and smtp-domain
      assertEquals("forced.domain6.tld", smtpConnectionBean.getSmtpHost());
      smtpConnectionBean.reset();

      Configuration.setForcedSmtpHostName("forced.domain7.tld");
      loginDataBean.setMailboxHost("domain7.tld");
      prefillSmtpConnectionMethod.invoke(logonController, parameterObjects);
      // get the real field-value - the getSmtpHost() could assemble a value...
      assertEquals("forced.domain7.tld",
              this.getPrivateFieldValue(SmtpConnectionBean.class,
                       smtpConnectionBean, "smtpHost"));
      // ...from smtp-subdomain-prefix and smtp-domain
      assertEquals("forced.domain7.tld", smtpConnectionBean.getSmtpHost());
      smtpConnectionBean.reset();

      Configuration.setForcedSmtpHostName("forced.domain8.tld");
      loginDataBean.setMailboxHost("pop3mail.domain8.tld");
      prefillSmtpConnectionMethod.invoke(logonController, parameterObjects);
      // get the real field-value - the getSmtpHost() could assemble a value...
      assertEquals("forced.domain8.tld",
              this.getPrivateFieldValue(SmtpConnectionBean.class,
                       smtpConnectionBean, "smtpHost"));
      // ...from smtp-subdomain-prefix and smtp-domain
      assertEquals("forced.domain8.tld", smtpConnectionBean.getSmtpHost());
      smtpConnectionBean.reset();
    }
    catch(Exception e) {

      e.printStackTrace();
      fail(e.getMessage());
    }
  }

}
