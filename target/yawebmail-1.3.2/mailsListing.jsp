<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="/WEB-INF/cuic.tld" prefix="cuic" %>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>

<f:view>

  <f:loadBundle basename="de.lotk.yawebmail.properties.view"
          var="viewProperties"/>

  <t:htmlTag value="html">

    <%@include file="inc/head.inc" %>

    <t:htmlTag value="body">
      <h:form id="mailsListingForm">

        <h:panelGrid styleClass="darkTableRow" columnClasses="smallPadding">

          <%-- Kopfzeile --%>
          <h:panelGrid columns="2"
                  columnClasses="leftAlignment, rightAlignment">

            <%-- Mail-Aktionen --%>
            <h:panelGroup>

              <h:commandLink styleClass="undecoratedLink deleteSelectedMailsCommandLink"
                      id="deleteSelectedMails"
                      action="#{mailsListingController.deleteSelectedMails}">
                <h:graphicImage alt="delete" url="/static/images/delete.png"/>
                <h:outputText styleClass="buttonTexts"
                        value="#{viewProperties.button_delete_selected_mails}"/>
              </h:commandLink>

              <h:commandLink styleClass="undecoratedLink checkForNewMailCommandLink"
                      id="checkForNewMail"
                      action="#{mailsListingController.checkForNewMail}">
                <h:graphicImage alt="get new mail"
                        url="/static/images/get-new-mail.png"/>
                <h:outputText styleClass="buttonTexts"
                        value="#{viewProperties.button_check_for_new_mail}"/>
              </h:commandLink>

              <h:commandLink styleClass="undecoratedLink createMailCommandLink"
                      id="createMail"
                      action="createMail">
                <h:graphicImage alt="create new mail"
                        url="/static/images/create-new-mail.png"/>
                <h:outputText styleClass="buttonTexts"
                        value="#{viewProperties.button_create_new_mail}"/>
              </h:commandLink>

            </h:panelGroup>

            <%-- Hilfe und Logout --%>
            <h:panelGroup>

              <h:outputLink styleClass="undecoratedLink" target="_blank"
                      id="helpLink"
                      value="#{viewProperties.link_help_overview}">
                <h:graphicImage alt="help"
                        url="/static/images/info.png"/>
                <h:outputText styleClass="buttonTexts"
                        value="#{viewProperties.button_help}"/>
              </h:outputLink>

              <h:commandLink styleClass="undecoratedLink logoutCommandLink"
                      id="logoutLink"
                      action="#{logoutController.logout}">
                <h:graphicImage alt="logout"
                        url="/static/images/logout.png"/>
                <h:outputText styleClass="buttonTexts"
                        value="#{viewProperties.button_logout}"/>
              </h:commandLink>

            </h:panelGroup>

          </h:panelGrid>

        </h:panelGrid>

        <%-- Aeusserer Table um Ordner-Menue und Mails-Liste --%>
        <h:panelGrid columns="#{folderWrapperBean.swappable ? 2 : 1}" styleClass="darkTableRow"
                columnClasses="tinyColumn, hugeColumn">

          <%-- Column 1: Ordner-Menue --%>
          <t:htmlTag value="div" styleClass="foldersDiv"
                  rendered="#{folderWrapperBean.swappable}">

            <h:outputText value="#{viewProperties.header_folders}"
                    styleClass="headlineFolder"/>
            <t:htmlTag value="br"/>

            <%-- In den Default-Ordner wechseln --%>
            <h:commandLink styleClass="undecoratedLink"
                    action="#{mailsListingController.changeToDefaultFolder}">
              <h:graphicImage alt="open folder"
                      url="/static/images/folder_16_16.png"
                      rendered="#{! empty sessionContainerBean.currentMailboxFolder}"/>
              <h:graphicImage alt="current folder"
                      url="/static/images/folder_open_16_16.png"
                      rendered="#{empty sessionContainerBean.currentMailboxFolder}"/>
              <h:outputText value=" #{loginDataBean.mailboxUser}"/>
            </h:commandLink>

            <t:htmlTag value="br"/>

            <%-- Ordner-Liste --%>
            <t:dataList var="currentFolder"
                    value="#{sessionContainerBean.mailboxConnection.allFolders}"
                    rowCountVar="sfRowCount" rowIndexVar="sfRowIndex"
                    binding="#{mailsListingController.folderTable}">

              <cuic:outputPerFolderLevel value="#{currentFolder}" content=" "
                      styleClass="preformatted"/>
              <h:commandLink styleClass="folderLink"
                      action="#{mailsListingController.changeFolder}">
                <h:graphicImage alt="open folder"
                        url="/static/images/folder_16_16.png"
                        rendered="#{currentFolder.fullName ne folderWrapperBean.folder.fullName}"/>
                <h:graphicImage alt="current folder"
                        url="/static/images/folder_open_16_16.png"
                        rendered="#{currentFolder.fullName eq folderWrapperBean.folder.fullName}"/>
                <h:outputText title="#{currentFolder.fullName}"
                        value=" #{currentFolder.name}"/>
              </h:commandLink><t:htmlTag value="br"
                      rendered="#{sfRowIndex < (sfRowCount - 1)}"/>
            </t:dataList>

            <t:htmlTag value="hr"/>

            <h:outputText value="#{viewProperties.header_manage_subfolders}"
                    styleClass="headlineFolder"/>
            <t:htmlTag value="br"/>

            <%-- Subfolder anlegen --%>
            <h:inputText id="newSubfolder"
                    styleClass="#{folderWrapperBean.subfolderPossible ? 'folderInputField' : 'disabledFolderInputField'}"
                    value="#{folderManagementBean.newSubfolder}"
                    disabled="#{! folderWrapperBean.subfolderPossible}"/>
            <h:message for="newSubfolder" errorClass="errorText"/>
            <h:commandButton value="#{viewProperties.button_create_folder}"
                    styleClass="#{folderWrapperBean.subfolderPossible ? 'smallButton' : 'disabledSmallButton'}"
                    action="#{mailsListingController.createSubfolder}"
                    disabled="#{! folderWrapperBean.subfolderPossible}"/>

            <%-- Subfolder loeschen --%>
            <h:selectOneMenu id="subfolderToDelete"
                    styleClass="#{folderWrapperBean.subfolderPossible ? 'folderInputField' : 'disabledFolderInputField'}"
                    value="#{folderManagementBean.subfolderToDelete}"
                    disabled="#{! folderWrapperBean.subfolderPossible}">
              <f:selectItems value="#{folderWrapperBean.subfolderMap}"/>
            </h:selectOneMenu> 
            <h:message for="subfolderToDelete" errorClass="errorText"/>
            <h:commandButton value="#{viewProperties.button_delete_folder}"
                    styleClass="#{folderWrapperBean.subfolderPossible ? 'smallButton' : 'disabledSmallButton'}"
                    action="#{mailsListingController.deleteSubfolder}"
                    disabled="#{! folderWrapperBean.subfolderPossible}"/>

          </t:htmlTag>

          <%-- Column 2: Mails-Liste (wenn Mails am Start) --%>
          <h:dataTable id="mailsListingTableId"
                  value="#{folderWrapperBean.displayMessages}"
                  styleClass="mailsListingTable" columnClasses="smallPadding"
                  rowClasses="clearTableRow, brightTableRow"
                  footerClass="smallPadding" var="dMessage"
                  binding="#{mailsListingController.messageTable}"
                  rendered="#{folderWrapperBean.messageCount >= 1}">

            <h:column>
              <f:facet name="header">
                <h:commandLink styleClass="undecoratedLink"
                        id="sortBySubject"
                        action="#{mailsListingController.sortBySubject}">
                  <h:outputText value="#{viewProperties.tch_subject} "/>
                  <h:graphicImage alt="sort up"
                          url="/static/images/sort-up.gif"
                          rendered="#{mailsListingController.sortUpBySubject}"/>
                  <h:graphicImage alt="sort down"
                          url="/static/images/sort-down.gif"
                          rendered="#{mailsListingController.sortDownBySubject}"/>
                </h:commandLink>
              </f:facet>
              <h:selectBooleanCheckbox value="#{dMessage.selected}"
                      id="selectMessageCheckBox"
                      styleClass="checkBox selectMessageCheckBox"/>
              <h:commandLink styleClass="messageLink displaySelectedMailCommandLink"
                      id="displaySelectedMail"
                      action="#{mailsListingController.displaySelectedMail}"
                      value="#{(! empty dMessage.originMessage.subject) ? dMessage.originMessage.subject : viewProperties.prompt_no_subject}"
                      title="#{dMessage.originMessage.subjectTooltip}"/>
            </h:column>

            <h:column>
              <f:facet name="header">
                <h:commandLink styleClass="undecoratedLink"
                        id="sortBySender"
                        action="#{mailsListingController.sortBySender}">
                  <h:outputText value="#{viewProperties.tch_from} "/>
                  <h:graphicImage alt="sort up"
                          url="/static/images/sort-up.gif"
                          rendered="#{mailsListingController.sortUpBySender}"/>
                  <h:graphicImage alt="sort down"
                          url="/static/images/sort-down.gif"
                          rendered="#{mailsListingController.sortDownBySender}"/>
                </h:commandLink>
              </f:facet>
              <t:dataList value="#{dMessage.originMessage.from}" var="actFrom"
                      id="fromRowIndex"
                      rowCountVar="fromRowCount" rowIndexVar="fromRowIndex">
                <h:outputText value="#{actFrom.personal}"
                        rendered="#{! empty actFrom.personal}"/>
                <h:outputText value=" <"
                        rendered="#{! empty actFrom.personal}"/>
                <h:outputText value="#{actFrom.address}"/>
                <h:outputText value=">" rendered="#{! empty actFrom.personal}"/>
                <h:outputText value=","
                        rendered="#{fromRowIndex < (fromRowCount - 1)}"/>
              </t:dataList>
            </h:column>

            <h:column>
              <f:facet name="header">
                <h:commandLink styleClass="undecoratedLink"
                        id="sortByDate"
                        action="#{mailsListingController.sortByDate}">
                  <h:outputText value="#{viewProperties.tch_date} "/>
                  <h:graphicImage alt="sort up"
                          url="/static/images/sort-up.gif"
                          rendered="#{mailsListingController.sortUpByDate}"/>
                  <h:graphicImage alt="sort down"
                          url="/static/images/sort-down.gif"
                          rendered="#{mailsListingController.sortDownByDate}"/>
                </h:commandLink>
              </f:facet>
              <h:outputText value="#{dMessage.originMessage.sentDate}">
                <f:convertDateTime pattern="dd.MM.yyyy HH:mm"/>
              </h:outputText>
            </h:column>

            <h:column>
              <f:facet name="header">
                <h:commandLink styleClass="undecoratedLink"
                        id="sortBySpamlevel"
                        action="#{mailsListingController.sortBySpamlevel}">
                  <h:outputText value="#{viewProperties.tch_xSpamLevel} "/>
                  <h:graphicImage alt="sort up"
                          url="/static/images/sort-up.gif"
                          rendered="#{mailsListingController.sortUpBySpamlevel}"/>
                  <h:graphicImage alt="sort down"
                          url="/static/images/sort-down.gif"
                          rendered="#{mailsListingController.sortDownBySpamlevel}"/>
                </h:commandLink>
              </f:facet>
              <h:outputText
                      value="#{(dMessage.originMessage.spamLevel != null) ? dMessage.originMessage.spamLevel : viewProperties.prompt_xSpamLevel_not_set}" />
            </h:column>

            <%-- Footer --%>
            <f:facet name="footer">

              <h:panelGrid columns="3"
                      columnClasses="leftAlignment, rightAlignment, rightAlignment">

                <%-- "Alles auswaehlen"-Button --%>
                <h:commandLink id="selectAll" styleClass="undecoratedLink selectAllMailsCommandLink"
                        action="#{mailsListingController.selectAllMails}">
                  <h:graphicImage alt="select all"
                          url="/static/images/select-all.png"/>
                  <h:outputText styleClass="buttonTexts"
                          value="#{viewProperties.button_select_all}"/>
                </h:commandLink>

                <%-- Scope-info --%>
                <h:outputFormat value="#{viewProperties.prompt_scope_messages}">
                  <f:param value="#{sessionContainerBean.currentOffset + 1}"/>
                  <f:param value="#{mailsListingController.numberLastMessage}"/>
                  <f:param value="#{folderWrapperBean.overallMessageCount}"/>
                </h:outputFormat>

                <%-- Paging --%>
                <h:panelGroup>

                  <h:commandLink id="pagingStart" styleClass="undecoratedLink"
                          action="#{mailsListingController.demandPaging}"
                          rendered="#{sessionContainerBean.currentOffset > 0}">
                    <f:param name="newOffset" value="0"/>
                    <h:graphicImage alt="start" url="/static/images/start.png"/>
                  </h:commandLink>

                  <h:commandLink id="pagingBack" styleClass="undecoratedLink"
                          action="#{mailsListingController.demandPaging}"
                          rendered="#{sessionContainerBean.currentOffset > 0}">
                    <f:param name="newOffset" value="#{sessionContainerBean.currentOffset - sessionContainerBean.amountOfMessagesPerPage}"/>
                    <h:graphicImage alt="back" url="/static/images/reverse.png"/>
                  </h:commandLink>

                  <h:commandLink id="pagingForth" styleClass="undecoratedLink"
                          action="#{mailsListingController.demandPaging}"
                          rendered="#{mailsListingController.pagingForthPossible}">
                    <f:param name="newOffset" value="#{sessionContainerBean.currentOffset + sessionContainerBean.amountOfMessagesPerPage}"/>
                    <h:graphicImage alt="forth" url="/static/images/forth.png"/>
                  </h:commandLink>

                  <h:commandLink id="pagingEnd" styleClass="undecoratedLink"
                          action="#{mailsListingController.demandPaging}"
                          rendered="#{mailsListingController.pagingForthPossible}">
                    <f:param name="newOffset" value="#{mailsListingController.maxOffset}"/>
                    <h:graphicImage alt="forth" url="/static/images/end.png"/>
                  </h:commandLink>

                </h:panelGroup>

              </h:panelGrid>
            </f:facet>

          </h:dataTable>

          <%-- Column 2 - alternativ (wenn keine Mails am Start) --%>
          <h:panelGrid rowClasses="twilightTableRow, centeredAlignment"
                  rendered="#{folderWrapperBean.messageCount < 1}">

            <h:outputText value="#{viewProperties.prompt_no_messages_in_folder}"
                      styleClass="slightlyBiggerFont"/>

            <h:panelGroup>
              <h:graphicImage alt="empty folder"
                      url="/static/images/folder_empty_128_128.png"/>
              <t:htmlTag value="br"/>
              <h:outputText styleClass="bigFont"
                      value="#{viewProperties.prompt_no_messages_in_folder}"/>
            </h:panelGroup>

          </h:panelGrid>

        </h:panelGrid>

        <%-- Dropdownliste mit allen Foldern --%>
        <t:htmlTag value="div" rendered="#{folderWrapperBean.swappable}">

          <t:htmlTag value="hr"/>

          <h:panelGrid styleClass="darkTableRow" columnClasses="rightAlignment">

            <%-- Mail-Verschiebe-Aktionen --%>
            <h:panelGroup>

              <h:outputText value="#{viewProperties.label_move_messages_to} "/>
              <h:selectOneMenu id="actionFolder"
                      value="#{folderManagementBean.actionFolder}">
                <f:selectItems value="#{choicesRetriever.allFolders}"/>
              </h:selectOneMenu>

              <%-- Fake command-link (used via Javascript) --%>
              <h:commandLink id="moveSelectedMailsHidden" value=" "
                      styleClass="undecoratedLink"
                      action="#{mailsListingController.moveSelectedMails}"/>

              <h:commandButton value="#{viewProperties.button_execute}"
                      styleClass="commandButton" id="moveSelectedMails"
                      action="#{mailsListingController.moveSelectedMails}"/>
            </h:panelGroup>

          </h:panelGrid>

        </t:htmlTag>

      </h:form>

    <%-- </body> --%>
    </t:htmlTag>

  <%-- </html> --%>
  </t:htmlTag>

</f:view>
