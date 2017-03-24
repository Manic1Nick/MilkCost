<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<c:set var="costs" value="${costs}"/>
<c:set var="names" value="${names}"/>
<c:set var="totalCosts" value="${totalCosts}"/>

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

    <link href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" rel="stylesheet"/>
    <link href="https://cdnjs.cloudflare.com/ajax/libs/datatables/1.10.12/css/dataTables.bootstrap.min.css" rel="stylesheet"/>

    <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.1.1/jquery.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/datatables/1.10.12/js/jquery.dataTables.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/datatables/1.10.12/js/dataTables.bootstrap.min.js"></script>

    <script src= "https://cdn.zingchart.com/zingchart.min.js"></script>
    <script> zingchart.MODULESDIR = "https://cdn.zingchart.com/modules/";
    ZC.LICENSE = ["569d52cefae586f634c54f86dc99e6a9","ee6b7db5b51705a13dc2339db3edaf6d"];</script>
</head>
<body>
<div class="container">

    <h2>Cost Structure:</h2>

    <table id="table" class="table table-striped table-bordered table-hover" cellspacing="0" width="100%">
        <thead>
        <tr>
            <th>Period</th>
            <c:forEach items="${names}" var="name">
                <th>
                    <c:out value="${name}" />
                </th>
            </c:forEach>
        </tr>
        </thead>

        <tbody>
        <c:forEach items="${costs}" var="outerMap">
            <tr>
                <td><a href="${contextPath}/costs/get/one?period=${outerMap.key}">
                    <c:out value="${outerMap.key}" /></a>
                </td>
                <c:forEach items="${outerMap.value}" var="innerMap">
                    <td>
                        <c:out value="${innerMap.value}" />
                    </td>
                </c:forEach>
            </tr>
        </c:forEach>

        <tr>
            <td>
                <a href="${contextPath}/cost/history/all">
                    <strong>TOTAL</strong>
                </a>
            </td>
            <c:forEach items="${totalCosts}" var="totalCost" varStatus="loop">
                <td>
                    <a href="${contextPath}/cost/history?name=${totalCost.key}">
                        <strong><c:out value="${totalCost.value}" /></strong>
                    </a>
                </td>
            </c:forEach>
        </tr>
        </tbody>
    </table>

    <div id='myChart'></div>
</div>
</body>
</html>

<script>
    $(document).ready(function() {
        $('#table').dataTable( {
            "pageLength": 13
        } );
    });
</script>
