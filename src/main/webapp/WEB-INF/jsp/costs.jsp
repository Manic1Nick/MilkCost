<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<c:set var="costs" value="${costStructure}"/>

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

    <form id="gomenuForm" method="GET" action="${contextPath}/welcome">
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
    </form>
    <form id="logoutForm" method="POST" action="${contextPath}/logout">
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
    </form>
    <a onclick="document.forms['logoutForm'].submit()">Logout</a> |
    <a onclick="document.forms['gomenuForm'].submit()">Return to menu</a>

    <h2>COSTS STRUCTURE</h2>

        <table id="orders" class="table table-striped table-bordered table-hover" cellspacing="0" width="100%">
            <thead>
            <tr>
                <th>Date</th>

                <th>Gas</th>
                <th>Repairs</th>
                <th>Materials</th>
                <th>Other</th>
                <th>Food</th>
                <th>Electricity</th>
                <th>Salary</th>
                <th>Payroll_Tax</th>
                <th>Amortization</th>
                <th>Services</th>
            </tr>
            </thead>

            <tbody>
            <c:forEach items="${costs}" var="cost">
                <tr>
                    <td><c:out value="${cost[monthYear]}" /></td>

                    <td><c:out value="${cost[costGas]}" /></td>
                    <td><c:out value="${cost[costRepairs]}" /></td>
                    <td><c:out value="${cost[costMaterials]}" /></td>
                    <td><c:out value="${cost[costOther]}" /></td>
                    <td><c:out value="${cost[costFood]}" /></td>
                    <td><c:out value="${cost[costElectricity]}" /></td>
                    <td><c:out value="${cost[costSalary]}" /></td>
                    <td><c:out value="${cost[costPayroll_Tax]}" /></td>
                    <td><c:out value="${cost[costAmortization]}" /></td>
                    <td><c:out value="${cost[costServices]}" /></td>
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
