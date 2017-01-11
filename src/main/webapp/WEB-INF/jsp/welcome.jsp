<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<c:set var="user" value="${currentUser}"/>
<c:set var="message" value="${message}"/>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->
    <meta name="description" content="">
    <meta name="author" content="">

    <title>Welcome</title>

    <link href="${contextPath}/resources/css/bootstrap.min.css" rel="stylesheet">

    <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
    <!--[if lt IE 9]>
    <script src="https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js"></script>
    <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
    <![endif]-->
</head>
<body>
<div class="container">

    <script>
        $(document).ready(function(){
            $('[data-toggle="tooltip"]').tooltip();
        });
    </script>

    <c:if test="${pageContext.request.userPrincipal.name != null}">

        <%--ALERT MESSAGE--%>
        <c:if test="${message != null}">
            <div class="alert alert-success">
                <h4><a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>
                    <strong>${message}</strong></h4>
            </div>
        </c:if>

        <%--LOGOUT--%>
        <form id="logoutForm" method="POST" action="${contextPath}/logout">
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
        </form>
        <a onclick="document.forms['logoutForm'].submit()">Logout</a>

        <%--ADD NEW DATA--%>
        <p>
            <a href="${contextPath}/costs/add">Add new data</a>
        </p>

        <%--SHOW TOTAL COSTS--%>
        <p>
            <a href="${contextPath}/costs/get?type=TOTAL">Show total costs</a>
        </p>

        <%--SHOW DIRECT COSTS--%>
        <p>
            <a href="${contextPath}/costs/get?type=DIRECT">Show direct costs</a>
        </p>

        <%--SHOW OVERHEAD COSTS--%>
        <p>
            <a href="${contextPath}/costs/get?type=OVERHEAD">Show overhead costs</a>
        </p>

    </c:if>

</div>
<!-- /container -->
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.2/jquery.min.js"></script>
<script src="${contextPath}/resources/js/bootstrap.min.js"></script>
</body>
</html>
