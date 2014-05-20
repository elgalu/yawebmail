<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>

<f:view>

  <f:loadBundle basename="de.lotk.yawebmail.properties.view" var="viewProperties"/>

  <t:htmlTag value="html">

    <%@include file="inc/head.inc" %>

    <t:htmlTag value="body">
      <h:form id="createMailForm" enctype="multipart/form-data">

        <h:messages globalOnly="true" errorClass="errorText"/>

        <h:panelGrid styleClass="darkTableRow" columnClasses="smallPadding">

          <%-- Kopfzeile --%>
          <h:panelGrid columns="2"
                  columnClasses="leftAlignment, rightAlignment">

            <%-- Mail-Aktionen --%>
            <h:panelGroup>

              <h:commandLink styleClass="undecoratedLink"
                      action="#{createMailController.sendMail}">
                <h:graphicImage alt="send" url="/static/images/send.png"/>
                <h:outputText styleClass="buttonTexts"
                        value="#{viewProperties.button_send_mail}"/>
              </h:commandLink>

              <h:commandLink styleClass="undecoratedLink"
                      action="#{createMailController.reset}" immediate="true">
                <h:graphicImage alt="reset" url="/static/images/reset.png"/>
                <h:outputText styleClass="buttonTexts"
                        value="#{viewProperties.button_reset}"/>
              </h:commandLink>

              <h:commandLink styleClass="undecoratedLink"
                      action="#{createMailController.cancel}" immediate="true">
                <h:graphicImage alt="cancel" url="/static/images/cancel.png"/>
                <h:outputText styleClass="buttonTexts"
                        value="#{viewProperties.button_cancel}"/>
              </h:commandLink>

            </h:panelGroup>

            <%-- Hilfe und Logout --%>
            <h:panelGroup>

              <h:outputLink styleClass="undecoratedLink" target="_blank"
                      value="#{viewProperties.link_help_newMail}">
                <h:graphicImage alt="help"
                        url="/static/images/info.png"/>
                <h:outputText styleClass="buttonTexts"
                        value="#{viewProperties.button_help}"/>
              </h:outputLink>

              <h:commandLink styleClass="undecoratedLink" immediate="true"
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

        <%-- Aeusserer Table um Header-Infos und Attachment-Area --%>
        <h:panelGrid columns="2" styleClass="darkTableRow"
                columnClasses="bigColumn, smallColumn">

          <%-- Column 1: Header-Infos --%>
          <h:panelGrid columns="2" columnClasses="smallPadding"
                  rowClasses="darkTableRow, darkTableRow, twilightTableRow, twilightTableRow, twilightTableRow, twilightTableRow, twilightTableRow">

            <h:outputText value="#{viewProperties.label_smtpserver_and_port}"/>
            <h:panelGroup>

              <%-- SMTP-Host frei waehlbar --%>
              <h:inputText id="smtpHost" value="#{smtpConnectionBean.smtpHost}"
                      required="true"
                      rendered="#{createMailController.smtpHostChoiseFree}"/>

              <%-- SMTP-Host innerhalb Mailbox-Domain waehlbar --%>
              <h:inputText id="smtpSubdomainPrefix" styleClass="smallInputField"
                      value="#{smtpConnectionBean.smtpSubdomainPrefix}"
                      rendered="#{createMailController.smtpHostChoiseDomain}"/>
              <h:outputText value="#{smtpConnectionBean.smtpDomain}"
                      rendered="#{createMailController.smtpHostChoiseDomain}"/>

              <%-- SMTP-Host nicht waehlbar --%>
              <h:outputText value="#{smtpConnectionBean.smtpHost}"
                      rendered="#{createMailController.smtpHostChoiseNone}"/>

              <h:outputText value=":"/>

              <%-- SMTP-Port waehlbar --%>
              <h:inputText id="smtpPort" value="#{smtpConnectionBean.smtpPort}"
                      required="true" styleClass="tinyInputField"
                      rendered="#{! createMailController.smtpHostChoiseNone}"/>

              <%-- SMTP-Port nicht waehlbar --%>
              <h:outputText value="#{smtpConnectionBean.smtpPort}"
                      rendered="#{createMailController.smtpHostChoiseNone}"/>

              <h:message for="smtpHost" errorClass="errorText"/>
              <h:message for="smtpPort" errorClass="errorText"/>

              <%-- Checkbox SSL --%>
              <h:selectBooleanCheckbox
                      value="#{smtpConnectionBean.sslConnection}"/>
              <h:outputText value="#{viewProperties.label_smtp_ssl}"/>

              <%-- Checkbox TLS --%>
              <h:selectBooleanCheckbox
                      value="#{smtpConnectionBean.tlsConnection}"/>
              <h:outputText value="#{viewProperties.label_smtp_tls}"/>
            </h:panelGroup>

            <h:outputText value="#{viewProperties.label_smtpauth_user_pass}"/>
            <h:panelGroup>
              <h:inputText id="smtpAuthUser"
                      value="#{smtpConnectionBean.smtpAuthUser}"/>
              <h:inputSecret id="smtpAuthPass" redisplay="true"
                      value="#{smtpConnectionBean.smtpAuthPass}"/>
              <h:message for="smtpAuthUser" errorClass="errorText"/>
              <h:message for="smtpAuthPass" errorClass="errorText"/>
            </h:panelGroup>

            <h:outputText value="#{viewProperties.label_from}"/>
            <h:panelGroup>
              <h:inputText id="from" value="#{mailBasisBean.from}"
                      required="true" styleClass="largeInputField"/>
              <h:message for="from" errorClass="errorText"/>
            </h:panelGroup>

            <h:outputText value="#{viewProperties.label_toRecipient}"/>
            <h:panelGroup>
              <h:inputText id="rcptTo" value="#{mailBasisBean.rcptTo}"
                      required="true" styleClass="largeInputField"/>
              <h:message for="rcptTo" errorClass="errorText"/>
            </h:panelGroup>

            <h:outputText value="#{viewProperties.label_ccRecipient}"/>
            <h:panelGroup>
              <h:inputText id="rcptCc" value="#{mailBasisBean.rcptCc}"
                      styleClass="largeInputField"/>
              <h:message for="rcptCc" errorClass="errorText"/>
            </h:panelGroup>

            <h:outputText value="#{viewProperties.label_bccRecipient}"/>
            <h:panelGroup>
              <h:inputText id="rcptBcc" value="#{mailBasisBean.rcptBcc}"
                      styleClass="largeInputField"/>
              <h:message for="rcptBcc" errorClass="errorText"/>
            </h:panelGroup>

            <h:outputText value="#{viewProperties.label_subject}"/>
            <h:panelGroup>
              <h:inputText id="subject" value="#{mailBasisBean.subject}"
                      styleClass="largeInputField"/>
              <h:message for="subject" errorClass="errorText"/>
            </h:panelGroup>

          </h:panelGrid>

          <%-- Column 2: Attachment-Area --%>
          <t:htmlTag value="div" styleClass="attachmentDiv">

            <h:graphicImage alt="upload file" url="/static/images/attach.png"/>
            <h:outputText value=" #{viewProperties.prompt_already_attached}"/>
            <t:htmlTag value="br"/>

            <h:dataTable value="#{mailBasisBean.attachments}" var="attachment">

              <h:column>
                <h:outputText value="#{attachment.fileName}"/>
              </h:column>

            </h:dataTable>

            <t:inputFileUpload id="uploadFile" storage="file"
                    value="#{mailBasisBean.newAttachment}"/>
            <h:commandButton action="createMail" styleClass="commandButton"
                    value="#{viewProperties.button_upload_file}"/>

          </t:htmlTag>

        </h:panelGrid>

        <t:htmlTag value="div" styleClass="mailParts">

          <h:inputTextarea id="mailText" styleClass="mailTextInputTextArea"
                  value="#{mailBasisBean.mailText}"/>

        </t:htmlTag>

        <t:htmlTag value="hr"/>

        <h:panelGrid styleClass="darkTableRow" columnClasses="smallPadding">

          <h:panelGroup>

            <h:commandLink styleClass="undecoratedLink"
                    action="#{createMailController.sendMail}">
              <h:graphicImage alt="send" url="/static/images/send.png"/>
              <h:outputText styleClass="buttonTexts"
                      value="#{viewProperties.button_send_mail}"/>
            </h:commandLink>

            <h:commandLink styleClass="undecoratedLink"
                    action="#{createMailController.reset}" immediate="true">
              <h:graphicImage alt="reset" url="/static/images/reset.png"/>
              <h:outputText styleClass="buttonTexts"
                      value="#{viewProperties.button_reset}"/>
            </h:commandLink>

            <h:commandLink styleClass="undecoratedLink"
                    action="#{createMailController.cancel}" immediate="true">
              <h:graphicImage alt="cancel" url="/static/images/cancel.png"/>
              <h:outputText styleClass="buttonTexts"
                      value="#{viewProperties.button_cancel}"/>
            </h:commandLink>

          </h:panelGroup>

        </h:panelGrid>

      </h:form>

    <%-- </body> --%>
    </t:htmlTag>

  <%-- </html> --%>
  </t:htmlTag>

</f:view>
