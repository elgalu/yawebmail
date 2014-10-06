<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>

<f:view>

  <f:loadBundle basename="de.lotk.yawebmail.properties.view"
          var="viewProperties"/>

  <t:htmlTag value="html">

    <%@include file="inc/head.inc" %>

    <t:htmlTag value="body">
      <h:form id="checkCertsForm">

        <h:dataTable id="certificateChainTable" var="cert"
                value="#{checkCertsBean.serverCerts}"
                rowClasses="preformatted" footerClass="darkTableRow">

          <%-- Header --%>
          <f:facet name="header">
            <h:outputText value="#{viewProperties.prompt_check_certs}"/>
          </f:facet>

          <h:column>
            <h:outputText value="#{cert}"/>
          </h:column>

          <%-- Footer --%>
          <f:facet name="footer">

            <t:htmlTag value="span">
  
              <h:outputText value="#{viewProperties.prompt_whether_to_trust_certs} "/>
              <h:commandButton value="#{viewProperties.button_yes}"
                      action="#{checkCertsController.certsTrusted}"
                      styleClass="commandButton"/>
              <h:commandButton value="#{viewProperties.button_no}"
                      action="#{checkCertsController.certsNotTrusted}"
                      styleClass="commandButton"/>
            </t:htmlTag>
          </f:facet>

        </h:dataTable>

      </h:form>

    <%-- </body> --%>
    </t:htmlTag>

  <%-- </html> --%>
  </t:htmlTag>

</f:view>
