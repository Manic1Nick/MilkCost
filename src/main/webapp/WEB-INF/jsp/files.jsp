<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<c:set var="files" value="${files}"/>
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

    <title>Cost structure page</title>

    <%--<link href="${contextPath}/resources/css/bootstrap.min.css" rel="stylesheet">
    <link href="${contextPath}/resources/css/common.css" rel="stylesheet">--%>

    <link href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" rel="stylesheet"/>
    <link href="https://cdnjs.cloudflare.com/ajax/libs/datatables/1.10.12/css/dataTables.bootstrap.min.css" rel="stylesheet"/>

    <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.1.1/jquery.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/datatables/1.10.12/js/jquery.dataTables.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/datatables/1.10.12/js/dataTables.bootstrap.min.js"></script>

    <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
    <!--[if lt IE 9]>
    <!--<script src="https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js"></script>
    <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>-->
    <%--<![endif]-->--%>
</head>
<body>
<div class="container">

    <script>
        $(document).ready(function() {
            $('#orders').dataTable();
        });
    </script>

    <%--<a onclick="${contextPath}/welcome">Return to menu</a>--%>

    <%--ALERT MESSAGE--%>
    <c:if test="${message != null}">
        <div class="alert alert-success">
            <h4><a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>
                <strong>${message}</strong></h4>
        </div>
    </c:if>

    <h2>FILES IN DATA BASE</h2>

        <table id="orders" class="table table-striped table-bordered table-hover" cellspacing="0" width="100%">
            <thead>
            <tr>
                <th>Period: Year</th>
                <th>Period: Month</th>
                <th>Date of Last Change</th>
                <th>File Name</th>
                <th>Type Costs</th>
                <th>Status</th>
            </tr>
            </thead>

            <tbody>
            <c:forEach items="${files}" var="file">
                <tr>
                    <td><c:out value="${file.period.year}" /></td>
                    <td><c:out value="${file.period.month}" /></td>
                    <td><c:out value="${file.dateOfLastChange}" /></td>
                    <td><c:out value="${file.fileName}" /></td>
                    <td><c:out value="${file.typeCosts}" /></td>
                    <c:if test="${file.changed == true}">
                        <td><c:out value="new file!" /></td>
                    </c:if>
                    <c:if test="${file.changed != true}">
                        <td><c:out value="old" /></td>
                    </c:if>
                </tr>
            </c:forEach>
           </tbody>
        </table>
</div>
<!-- /container -->
<%--<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.2/jquery.min.js"></script>
<script src="${contextPath}/resources/js/bootstrap.min.js"></script>--%>

</body>
</html>
