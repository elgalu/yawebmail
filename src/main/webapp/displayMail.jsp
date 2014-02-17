<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>
<%@ taglib uri="/WEB-INF/cuic.tld" prefix="cuic" %>

<f:view>

  <f:loadBundle basename="de.lotk.yawebmail.properties.view" var="viewProperties"/>

  <t:htmlTag value="html">

    <%@include file="inc/head.inc" %>

    <t:htmlTag value="body">
      <h:form id="displayMailForm">

        <h:panelGrid styleClass="darkTableRow" columnClasses="smallPadding">

          <%-- Kopfzeile --%>
          <h:panelGrid columns="2"
                  columnClasses="leftAlignment, rightAlignment">

            <%-- Mail-Aktionen --%>
            <h:panelGroup>

              <h:commandLink styleClass="undecoratedLink"
                      action="#{displayMailController.createReMail}">
                <h:graphicImage alt="reply" url="/static/images/reply.png"/>
                <h:outputText styleClass="replyToButtonText"
                        value="#{viewProperties.button_reply}"/>
              </h:commandLink>

              <h:commandLink styleClass="undecoratedLink"
                      action="#{displayMailController.createReToAllMail}">
                <h:outputText styleClass="buttonTexts"
                        value="| #{viewProperties.prompt_to_all}"/>
              </h:commandLink>

              <h:commandLink styleClass="undecoratedLink"
                      action="#{displayMailController.createFwdMail}">
                <h:graphicImage alt="forward" url="/static/images/forward.png"/>
                <h:outputText styleClass="buttonTexts"
                        value="#{viewProperties.button_forward_mail}"/>
              </h:commandLink>

              <h:commandLink styleClass="undecoratedLink"
                      action="viewMessageSource">
                <h:graphicImage alt="sourcecode"
                        url="/static/images/sourcecode.png"/>
                <h:outputText styleClass="buttonTexts"
                        value="#{viewProperties.button_view_message_source}"/>
              </h:commandLink>

              <h:commandLink styleClass="undecoratedLink"
                      action="#{displayMailController.deleteMail}">
                <h:graphicImage alt="delete" url="/static/images/delete.png"/>
                <h:outputText styleClass="buttonTexts"
                        value="#{viewProperties.button_delete_mail}"/>
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
                      value="#{viewProperties.link_help_mailview}">
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

        <h:panelGrid columns="2" styleClass="darkTableRow"
                columnClasses="smallPadding">

          <h:outputText value="#{viewProperties.label_subject}"/>
          <h:outputText value="#{(! empty displayMessageBean.originMessage.subject) ? displayMessageBean.originMessage.subject : viewProperties.prompt_no_subject}"/>

          <h:outputText value="#{viewProperties.label_from}"
                  rendered="#{! empty displayMessageBean.originMessage.from}"/>
          <t:dataList rendered="#{! empty displayMessageBean.originMessage.from}"
                  value="#{displayMessageBean.originMessage.from}" var="actFrom"
                  rowCountVar="fromRowCount" rowIndexVar="fromRowIndex">
            <h:outputText value="#{actFrom.personal}"
                    rendered="#{! empty actFrom.personal}"/>
            <h:outputText value=" <" rendered="#{! empty actFrom.personal}"/>
            <h:outputText value="#{actFrom.address}"/>
            <h:outputText value=">" rendered="#{! empty actFrom.personal}"/>
            <h:outputText value=","
                    rendered="#{fromRowIndex < (fromRowCount - 1)}"/>
          </t:dataList>

          <h:outputText value="#{viewProperties.label_replyTo}"
                  rendered="#{(! empty displayMessageBean.originMessage.replyTo) && (displayMessageBean.originMessage.replyTo[0] != displayMessageBean.originMessage.from[0])}"/>
          <t:dataList rendered="#{(! empty displayMessageBean.originMessage.replyTo) && (displayMessageBean.originMessage.replyTo[0] != displayMessageBean.originMessage.from[0])}"
                  value="#{displayMessageBean.originMessage.replyTo}" var="actReplyTo"
                  rowCountVar="replyToRowCount" rowIndexVar="replyToRowIndex">
            <h:outputText value="#{actReplyTo.personal}"
                    rendered="#{! empty actReplyTo.personal}"/>
            <h:outputText value=" <" rendered="#{! empty actReplyTo.personal}"/>
            <h:outputText value="#{actReplyTo.address}"/>
            <h:outputText value=">" rendered="#{! empty actReplyTo.personal}"/>
            <h:outputText value=","
                    rendered="#{fromRowIndex < (fromRowCount - 1)}"/>
          </t:dataList>

          <h:outputText value="#{viewProperties.label_sendDate}"/>
          <h:outputText value="#{displayMessageBean.originMessage.sentDate}">
            <f:convertDateTime pattern="dd.MM.yyyy HH:mm"/>
          </h:outputText>

          <h:outputText value="#{viewProperties.label_toRecipient}"
                  rendered="#{! empty displayMessageBean.toRecipients}"/>
          <t:dataList rendered="#{! empty displayMessageBean.toRecipients}"
                  value="#{displayMessageBean.toRecipients}" var="actTo"
                  rowCountVar="toRowCount" rowIndexVar="toRowIndex">
            <h:outputText value="#{actTo.personal}"
                    rendered="#{! empty actTo.personal}"/>
            <h:outputText value=" <" rendered="#{! empty actTo.personal}"/>
            <h:outputText value="#{actTo.address}"/>
            <h:outputText value=">" rendered="#{! empty actTo.personal}"/>
            <h:outputText value=","
                    rendered="#{toRowIndex < (toRowCount - 1)}"/>
          </t:dataList>

          <h:outputText value="#{viewProperties.label_ccRecipient}"
                  rendered="#{! empty displayMessageBean.ccRecipients}"/>
          <t:dataList rendered="#{! empty displayMessageBean.ccRecipients}"
                  value="#{displayMessageBean.ccRecipients}" var="actCc"
                  rowCountVar="ccRowCount" rowIndexVar="ccRowIndex">
            <h:outputText value="#{actCc.personal}"
                    rendered="#{! empty actCc.personal}"/>
            <h:outputText value=" <" rendered="#{! empty actCc.personal}"/>
            <h:outputText value="#{actCc.address}"/>
            <h:outputText value=">" rendered="#{! empty actCc.personal}"/>
            <h:outputText value=","
                    rendered="#{ccRowIndex < (ccRowCount - 1)}"/>
          </t:dataList>

          <h:outputText value="#{viewProperties.label_bccRecipient}"
                  rendered="#{! empty displayMessageBean.bccRecipients}"/>
          <t:dataList rendered="#{! empty displayMessageBean.bccRecipients}"
                  value="#{displayMessageBean.bccRecipients}" var="actBcc"
                  rowCountVar="bccRowCount" rowIndexVar="bccRowIndex">
            <h:outputText value="#{actBcc.personal}"
                    rendered="#{! empty actBcc.personal}"/>
            <h:outputText value=" <" rendered="#{! empty actBcc.personal}"/>
            <h:outputText value="#{actBcc.address}"/>
            <h:outputText value=">" rendered="#{! empty actBcc.personal}"/>
            <h:outputText value=","
                    rendered="#{bccRowIndex < (bccRowCount - 1)}"/>
          </t:dataList>

        </h:panelGrid>

        <t:htmlTag value="hr"/>

        <t:dataList value="#{displayMessageBean.displayParts}" var="curPart"
                rowCountVar="partRowCount" rowIndexVar="partRowIndex">

          <%-- "text/plain"-Part --%>
          <cuic:htmlTagIf value="div" styleClass="mailParts" bean="#{curPart}"
                  comparison="ContentType" expectedValue="text/plain">

            <%--
              - Wenn aktueller Part vom Typ "BodyPart", versuchen den Parent-
              - Part zu holen, und sehen, ob der den ContentType
              - "multipart/alternative" hat. Falls ja, Hinweis ausgeben.
              --%>
            <cuic:htmlTagIf value="div" bean="#{curPart}"
                    comparison="MessageType"
                    expectedValue="javax.mail.BodyPart">

              <cuic:htmlTagIf value="div" styleClass="multipartNotice"
                      bean="#{curPart.parent}" comparison="ContentType"
                      expectedValue="multipart/alternative">

                <h:outputText value="#{viewProperties.prompt_alternative_multipart_notice}"/>
                <t:htmlTag value="br"/>

                <t:dataList var="actChildDisplayPartNumber"
                        value="#{displayMessageBean.multiparts[curPart.parent]}"
                        rowCountVar="cdpRowCount" rowIndexVar="cdpRowIndex">

                  <h:outputLink target="_blank"
                          value="#{facesContext.externalContext.requestContextPath}/retrieveDisplayPartContentServlet.svl">

                    <f:param name="partNumber" value="#{actChildDisplayPartNumber}"/>
                    <h:outputText value="#{displayMessageBean.displayParts[actChildDisplayPartNumber].contentType}"/>

                  </h:outputLink>
                  <h:outputText value=","
                         rendered="#{cdpRowIndex < (cdpRowCount - 1)}"/>

                </t:dataList>

              </cuic:htmlTagIf>

            </cuic:htmlTagIf>

            <cuic:outputPlainTextPart value="#{curPart}"
                    styleClass="fixWidthChars"/>

          </cuic:htmlTagIf>

          <%-- "text/html"- oder "text/enriched"-Part --%>
          <cuic:htmlTagIf value="div" styleClass="mailParts" bean="#{curPart}"
                  comparison="ContentType"
                  expectedValue="text/html,text/enriched">

            <%--
            <h:commandLink
                    action="#{displayMailController.displayPartContent}"
                    value="#{viewProperties.prompt_open_html_mail}"
                    target="_blank">

              <f:param name="partNumber" value="#{partRowIndex}"/>
            </h:commandLink>
            --%>

            <h:outputLink target="_blank"
                    value="#{facesContext.externalContext.requestContextPath}/retrieveDisplayPartContentServlet.svl">

              <f:param name="partNumber" value="#{partRowIndex}"/>
              <h:outputText value="#{viewProperties.prompt_open_html_mail}"/>

            </h:outputLink>

          </cuic:htmlTagIf>

          <%-- "image"-Part --%>
          <cuic:htmlTagIf value="div" styleClass="mailParts" bean="#{curPart}"
                  comparison="ContentType"
                  expectedValue="image/gif,image/jpeg,image/jpg,image/png">

            <h:graphicImage alt="#{curPart.fileName}"
                    url="/retrieveDisplayPartContentServlet.svl?partNumber=#{partRowIndex}"/>

          </cuic:htmlTagIf>

          <%-- Part, der nicht Text, HTML oder Image ist --%>
          <cuic:htmlTagIf value="div" styleClass="mailParts" bean="#{curPart}"
                  comparison="ContentType" reverse="true"
                  expectedValue="text/plain,text/html,text/enriched,image/gif,image/jpeg,image/jpg,image/png">

            <h:outputText value="["/>
            <h:outputLink target="_blank"
                    value="#{facesContext.externalContext.requestContextPath}/retrieveDisplayPartContentServlet.svl">

              <f:param name="partNumber" value="#{partRowIndex}"/>
              <h:outputText
                      value="#{curPart.fileName} (#{curPart.contentType})"/>

            </h:outputLink>
            <h:outputText value="]"/>

          </cuic:htmlTagIf>

          <%-- Trenner nur ausgeben, wenn es noch mehr Parts gibt. --%>
          <t:htmlTag value="hr" styleClass="mailPartSeparator"
                  rendered="#{partRowIndex < (partRowCount - 1)}"/>

        </t:dataList>

        <t:htmlTag value="hr"/>

        <h:panelGrid styleClass="darkTableRow" columnClasses="smallPadding">

          <h:panelGroup>

            <h:commandLink styleClass="undecoratedLink"
                    action="#{displayMailController.createReMail}">
              <h:graphicImage alt="reply" url="/static/images/reply.png"/>
              <h:outputText styleClass="replyToButtonText"
                      value="#{viewProperties.button_reply}"/>
            </h:commandLink>

            <h:commandLink styleClass="undecoratedLink"
                    action="#{displayMailController.createReToAllMail}">
              <h:outputText styleClass="buttonTexts"
                      value="| #{viewProperties.prompt_to_all}"/>
            </h:commandLink>

            <h:commandLink styleClass="undecoratedLink"
                    action="#{displayMailController.createFwdMail}">
              <h:graphicImage alt="forward" url="/static/images/forward.png"/>
              <h:outputText styleClass="buttonTexts"
                      value="#{viewProperties.button_forward_mail}"/>
            </h:commandLink>

            <h:commandLink styleClass="undecoratedLink"
                    action="viewMessageSource">
              <h:graphicImage alt="sourcecode"
                      url="/static/images/sourcecode.png"/>
              <h:outputText styleClass="buttonTexts"
                      value="#{viewProperties.button_view_message_source}"/>
            </h:commandLink>

            <h:commandLink styleClass="undecoratedLink"
                    action="#{displayMailController.deleteMail}">
              <h:graphicImage alt="delete" url="/static/images/delete.png"/>
              <h:outputText styleClass="buttonTexts"
                      value="#{viewProperties.button_delete_mail}"/>
            </h:commandLink>

            <h:commandLink styleClass="undecoratedLink" action="mailsListing">
              <h:graphicImage alt="summary" url="/static/images/summary.png"/>
              <h:outputText styleClass="buttonTexts"
                      value="#{viewProperties.button_back_to_summary}"/>
            </h:commandLink>

          </h:panelGroup>

        </h:panelGrid>

        <%-- Header ausgeben --%>
        <t:htmlTag value="hr"/>

        <t:htmlTag value="div" styleClass="headerLines">

          <t:dataList var="currentHeader"
                  value="#{displayMessageBean.allHeaderLinesAsList}">

            <h:outputText value="#{currentHeader}"/>
            <t:htmlTag value="br"/>

          </t:dataList>

        </t:htmlTag>

      </h:form>

    <%-- </body> --%>
    </t:htmlTag>

  <%-- </html> --%>
  </t:htmlTag>

</f:view>
