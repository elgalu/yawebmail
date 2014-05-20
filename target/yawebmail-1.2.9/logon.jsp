<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>
<%@ taglib uri="/WEB-INF/cuic.tld" prefix="cuic" %>

<f:view>

  <f:loadBundle basename="de.lotk.yawebmail.properties.application"
          var="applicationProperties"/>
  <f:loadBundle basename="de.lotk.yawebmail.properties.view"
          var="viewProperties"/>

  <t:htmlTag value="html">

    <%@include file="inc/head.inc" %>

    <t:htmlTag value="body">

      <h:form id="logonForm">

        <%-- Globale Fehler ausgeben --%>
        <h:messages globalOnly="true" errorClass="errorText"/>

        <%-- Logo und Versions-Info --%>
        <t:htmlTag value="div" styleClass="headlineBig">

          <h:graphicImage alt="yawebmail" url="/static/images/logo.gif"/>

        </t:htmlTag>

        <t:htmlTag value="p" styleClass="infoText">
          <h:outputText value="#{viewProperties.prompt_version_info} ("/>
          <h:outputText value=")"/>
        </t:htmlTag>

        <%-- Login-Daten-Tabelle --%>
        <h:panelGrid columns="2"
                columnClasses="logonTableLeftColumn, logonTableRightColumn"
                rowClasses="darkTableRow, twilightTableRow, twilightTableRow, twilightTableRow, twilightTableRow, twilightTableRow, darkTableRow">

          <%-- Sprache --%>
          <h:panelGroup>
            <cuic:outputLocalizedText key="prompt_language" language="en"/>
            <h:outputText value=" (#{viewProperties.prompt_language})"/>
          </h:panelGroup>
          <h:selectOneMenu id="language" value="#{sessionContainerBean.locale}"
                  immediate="true"
                  valueChangeListener="#{logonController.changeLanguage}">
            <f:selectItems value="#{choicesRetriever.sprachauswahlChoices}"/>
          </h:selectOneMenu>

          <%-- Mailbox-Host --%>
          <h:outputText value="#{viewProperties.label_mailbox_host}"/>
          <h:panelGroup>
            <h:inputText id="mailboxHost" value="#{loginDataBean.mailboxHost}"
                    required="true" styleClass="mailboxHostInput"
                    disabled="#{! logonController.editableMailboxHost}"/>
            <h:message for="mailboxHost" errorClass="errorText"/>
          </h:panelGroup>

          <%-- Mailbox-Protokoll --%>
          <h:panelGroup>
            <h:outputText value="#{viewProperties.label_mailbox_protocol}"/>
            <h:outputText value=" / #{viewProperties.label_mailbox_port}"
                    rendered="#{loginDataBean.advancedLogonProperties}"/>
          </h:panelGroup>
          <h:panelGroup>
            <h:selectOneMenu id="mailboxProtocol"
                    value="#{loginDataBean.mailboxProtocol}"
                    disabled="#{false}">
              <f:selectItems
                      value="#{choicesRetriever.mailboxProtocolChoices}"/>
            </h:selectOneMenu> 
            <h:message for="mailboxProtocol" errorClass="errorText"/>
            <h:commandLink styleClass="undecoratedLink" immediate="true"
                    action="#{logonController.configureAdvancedLogonProperties}"
                    rendered="#{false}">
              <h:graphicImage alt="configure"
                      url="/static/images/configure_16_16.png"/>
            </h:commandLink>
            <h:inputText id="mailboxPort" value="#{loginDataBean.mailboxPort}"
                    rendered="#{true}"
                    styleClass="smallInputField mailboxPortInput">
              <f:validateLongRange minimum="1" maximum="65535"/>
            </h:inputText>
            <h:message for="mailboxPort" errorClass="errorText"/>
          </h:panelGroup>

          <%-- Mailbox-User --%>
          <h:outputText value="#{viewProperties.label_mailbox_user}"/>
          <h:panelGroup>
            <h:inputText id="mailboxUser" value="#{loginDataBean.mailboxUser}"
                    required="true" styleClass="mailboxUserInput"/>
            <h:message for="mailboxUser" errorClass="errorText"/>
          </h:panelGroup>

          <%-- Mailbox-Passwort --%>
          <h:outputText value="#{viewProperties.label_mailbox_pass}"/>
          <h:panelGroup>
            <h:inputSecret id="mailboxPassword" styleClass="mailboxPasswordInput"
                    value="#{loginDataBean.mailboxPassword}" required="true"/>
            <h:message for="mailboxPassword" errorClass="errorText"/>
          </h:panelGroup>

          <%-- Message-arrangement --%>
          <h:outputText value="#{viewProperties.label_message_arrangement}"/>
          <h:selectOneRadio id="messageArrangement" layout="pageDirection"
                  value="#{sessionContainerBean.allMessagesOnOnePage}">
            <f:selectItem id="onePageYes" itemValue="#{true}"
                    itemLabel="#{viewProperties.prompt_message_arrangement_one_page}"/>
            <f:selectItem id="onePageNo" itemValue="#{false}"
                    itemLabel="#{viewProperties.prompt_message_arrangement_multiple_pages}"/>
          </h:selectOneRadio>

          <%-- <html-client-dependent> --%>
          <f:verbatim escape="false">&nbsp;</f:verbatim>
          <%-- </html-client-dependent> --%>

          <%-- Login-, Cancel- und Hilfe-Button --%>
          <h:panelGrid columns="2"
                  columnClasses="leftAlignment, rightAlignment">

            <%-- Login- und Cancel-Button --%>
            <h:panelGroup>
              <h:commandButton value="#{viewProperties.button_login}"
                      action="#{logonController.logon}"
                      styleClass="commandButton loginCommandButton"/>
              <h:commandButton value="#{viewProperties.button_reset}"
                      action="#{logonController.reset}"
                      styleClass="commandButton resetCommandButton" immediate="true"/>
            </h:panelGroup>

          </h:panelGrid>

        </h:panelGrid>

        <t:htmlTag value="p" styleClass="infoText">
          <h:outputText value="#{viewProperties.prompt_useAtOwnRisk}"/>
        </t:htmlTag>

        <t:htmlTag value="p" styleClass="infoText">
          <h:outputText value="#{viewProperties.prompt_gplInformation}"/>
        </t:htmlTag>

        <t:htmlTag value="p" styleClass="infoText">

          <h:outputText value="#{applicationProperties.program_version}"/>

          <h:outputText value=" ("
                  rendered="#{logonController.newYawebmailVersionAvailable}"/>
          <h:outputLink target="_blank"
                  value="#{applicationProperties.link_download_yawebmail}"
                  rendered="#{logonController.newYawebmailVersionAvailable}">
            <h:outputText
                    value="#{viewProperties.prompt_new_version_available}"/>
          </h:outputLink>
          <h:outputText value=")"
                  rendered="#{logonController.newYawebmailVersionAvailable}"/>

          <h:outputText value=" | #{viewProperties.prompt_language} : "/>
          <h:outputText
                  value="#{viewProperties.resourceFile_language_localName} | "/>

          <h:outputText value="#{viewProperties.prompt_translatedBy} : "/>
          <h:outputText value="#{viewProperties.resourceFile_author}"/>
        </t:htmlTag>

      </h:form>

    <%-- </body> --%>
    </t:htmlTag>

  <%-- </html> --%>
  </t:htmlTag>

</f:view>
