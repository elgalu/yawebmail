<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>

<f:view>

  <f:loadBundle basename="de.lotk.yawebmail.properties.view" var="viewProperties"/>

  <t:htmlTag value="html">

    <%@include file="inc/head.inc" %>

    <t:htmlTag value="body">
      <h:form id="displayMessageSourceForm">

        <h:panelGrid styleClass="darkTableRow" columnClasses="smallPadding">

          <%-- Kopfzeile --%>
          <h:panelGrid columns="2"
                  columnClasses="leftAlignment, rightAlignment">

            <%-- Mail-Aktionen --%>
            <h:panelGroup>

              <h:commandLink styleClass="undecoratedLink" action="displayMail">
                <h:graphicImage alt="back to mail"
                        url="/static/images/back.png"/>
                <h:outputText styleClass="buttonTexts"
                        value="#{viewProperties.button_back_to_mail}"/>
              </h:commandLink>

              <h:commandLink styleClass="undecoratedLink" action="mailsListing">
                <h:graphicImage alt="summary" url="/static/images/summary.png"/>
                <h:outputText styleClass="buttonTexts"
                        value="#{viewProperties.button_back_to_summary}"/>
              </h:commandLink>

            </h:panelGroup>

            <%-- Hilfe und Logout --%>
            <h:panelGroup>

              <h:outputLink styleClass="undecoratedLink" target="_blank"
                      value="#{viewProperties.link_help_messageSource}">
                <h:graphicImage alt="help"
                        url="/static/images/info.png"/>
                <h:outputText styleClass="buttonTexts"
                        value="#{viewProperties.button_help}"/>
              </h:outputLink>

              <h:commandLink styleClass="undecoratedLink"
                      action="#{logoutController.logout}">
                <h:graphicImage alt="logout"
                        url="/static/images/logout.png"/>
                <h:outputText styleClass="buttonTexts"
                        value="#{viewProperties.button_logout}"/>
              </h:commandLink>

            </h:panelGroup>

          </h:panelGrid>

        </h:panelGrid>

        <t:htmlTag value="hr"/>

        <t:htmlTag value="div" styleClass="preformatted">

<%--
  - Tag darf keine fuehrenden Leerstellen haben, da die sonst mit ausgegeben
  - werden wuerden
  --%>
<h:outputText value="#{displayMessageBean.messageSource}"/>

        </t:htmlTag>

        <t:htmlTag value="hr"/>

        <h:panelGrid styleClass="darkTableRow" columnClasses="smallPadding">

          <h:panelGroup>

            <h:commandLink styleClass="undecoratedLink" action="displayMail">
              <h:graphicImage alt="back to mail" url="/static/images/back.png"/>
              <h:outputText styleClass="buttonTexts"
                      value="#{viewProperties.button_back_to_mail}"/>
            </h:commandLink>

            <h:commandLink styleClass="undecoratedLink" action="mailsListing">
              <h:graphicImage alt="summary" url="/static/images/summary.png"/>
              <h:outputText styleClass="buttonTexts"
                      value="#{viewProperties.button_back_to_summary}"/>
            </h:commandLink>

          </h:panelGroup>

        </h:panelGrid>

      </h:form>

    <%-- </body> --%>
    </t:htmlTag>

  <%-- </html> --%>
  </t:htmlTag>

</f:view>
