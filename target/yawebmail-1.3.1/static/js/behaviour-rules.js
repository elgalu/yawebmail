var myrules = {

  '#logonForm:language' : function(el) {

    el.onchange = function() {

      document.forms["logonForm"].submit();
    };
  },

  '.messageLink' : function(el) {

    makeDraggable(el, true);
  },

  '.checkBox' : function(el) {

    makeDraggable(el, false);
    makeDroppable(el, new dropToMarkCallbackListener());
  },

  '.folderLink' : function(el) {

    makeDroppable(el, new dropToMoveCallbackListener());
  },

  '#mailsListingForm:deleteSelectedMails' : function(el) {

    makeDroppable(el, new dropToDeleteCallbackListener());
  },

  '#mailsListingForm:mailsListingTable:selectAll' : function(el) {

    el.onclick = function() {

      var inputElements = document.getElementsByTagName("input");
      var ii;

      for(ii = 0; ii < inputElements.length; ii++) {

        if(inputElements[ii].type == "checkbox") {

          inputElements[ii].checked = true;
        }
      }
    };
  }

};

Behaviour.register(myrules);
