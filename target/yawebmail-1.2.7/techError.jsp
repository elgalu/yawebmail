<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>

<f:view>

  <t:htmlTag value="html">

    <%@include file="inc/head.inc" %>

    <t:htmlTag value="body">

      An error occurred:
      <br/>
      <h:messages id="errorMessagesSummary" showSummary="true" showDetail="false" errorClass="errorText"/>
      <br/><br/>

      Details:
      <br/>
      <h:messages id="errorMessagesDetail" showSummary="false" showDetail="true" errorClass="stackTrace"/>

    <%-- </body> --%>
    </t:htmlTag>

  <%-- </html> --%>
  </t:htmlTag>

</f:view>
