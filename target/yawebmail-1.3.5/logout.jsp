<%@ page session="true"%>

<script>
    // Helper function to clear cookies
    var delete_cookie = function(name) {
        document.cookie = name + '=;expires=Thu, 01 Jan 1970 00:00:01 GMT;';
    };

    // Clear session cookies to simulate logout
    delete_cookie('jsessionid');
    delete_cookie('JSESSIONID');

    // Redirect to login page
    setTimeout(function() {
        window.location.replace('/yawebmail/');
    }, 100);
</script>

<%
// Commented jsp redirect since we are using Javascript for now.
// response.sendRedirect("logon.jsf");
%>
