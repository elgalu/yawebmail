document.onmouseup = mouseUp;

var dragObject = null;


function mouseUp() {

  dragObject = null;

  if(document.body.style.cursor != "wait") {

    document.body.style.cursor = "auto";
  }
}

function makeDraggable(item, swapCursor) {

  this.swapCursor = swapCursor;

  if(!item) return;

  item.onmousedown = function(ev) {

    dragObject = this;

    if(swapCursor) {

      document.body.style.cursor = "move";
    }

    return(false);
  }
}

function makeDroppable(item, cbl) {

  this.item = item;
  this.cbl = cbl;
  this.tempCursorStyle = null;

  if(!item) return;

  item.onmouseup = function(ev) {

    if(dragObject != null) {

      document.body.style.cursor = "wait";
      cbl.callBack(item);
      document.body.style.cursor = "auto";
    }
  }

  item.onmouseover = function(ev) {

    if(dragObject != null) {

      tempCursorStyle = document.body.style.cursor;
      document.body.style.cursor = "crosshair";
    }
  }

  item.onmouseout = function(ev) {

    if(tempCursorStyle != null) {

      document.body.style.cursor = tempCursorStyle;
      tempCursorStyle = null;
    }
  }
}


// ----------------------------------------------------------- Callback-Listener

function dropToMarkCallbackListener() {

  this.callBack = function dropToMarkCblCallBack(droppedToElement) {

    if(dragObject != droppedToElement) {

      var inputElements = document.getElementsByTagName("input");
      var ii;
      var markingScopeOn = false;

      for(ii = 0; ii < inputElements.length; ii++) {

        if(inputElements[ii].type == "checkbox") {

          if((inputElements[ii] == dragObject) ||
                 (inputElements[ii] == droppedToElement)) {

            markingScopeOn = (! markingScopeOn);
            inputElements[ii].checked = (! inputElements[ii].checked);
          }
          else if(markingScopeOn) {

            inputElements[ii].checked = (! inputElements[ii].checked);
          }
        }
      }
    }
  }
}

function dropToDeleteCallbackListener() {

  this.callBack = function dropToDeleteCblCallBack(droppedToElement) {

    dragObject.parentNode.getElementsByTagName("input")[0].checked = true;
    
    return(oamSubmitForm('mailsListingForm',
            'mailsListingForm:deleteSelectedMails'));
  }
}

function dropToMoveCallbackListener() {

  this.callBack = function dropToMoveCblCallBack(droppedToElement) {

    dragObject.parentNode.getElementsByTagName("input")[0].checked = true;

    // Target-folder?
    var targetFolder = droppedToElement.getElementsByTagName("span")[0].title;

    // Adjust Dropdownbox
    var dropDownBox = document.getElementById("mailsListingForm:actionFolder");
    var allOptions = dropDownBox.options;

    for(ii = 0; ii < allOptions.length; ii++) {

      if(allOptions[ii].value == targetFolder) {

        dropDownBox.selectedIndex = allOptions[ii].index;
        break;
      }
    }

    // Submit form
    return(oamSubmitForm('mailsListingForm',
            'mailsListingForm:moveSelectedMailsHidden'));
  }
}
