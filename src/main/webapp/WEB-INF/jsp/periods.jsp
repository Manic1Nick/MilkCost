<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<c:set var="periods" value="${periods}"/>
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

    <title>Periods</title>

    <link href="${contextPath}/resources/css/bootstrap.min.css" rel="stylesheet">

    <link href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" rel="stylesheet"/>
    <link href="https://cdnjs.cloudflare.com/ajax/libs/datatables/1.10.12/css/dataTables.bootstrap.min.css" rel="stylesheet"/>

    <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.1.1/jquery.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/datatables/1.10.12/js/jquery.dataTables.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/datatables/1.10.12/js/dataTables.bootstrap.min.js"></script>


    <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
    <!--[if lt IE 9]>
    <script src="https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js"></script>
    <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
    <![endif]-->
</head>
<body>
<div class="container">

    <%--ALERT MESSAGE--%>
    <c:if test="${message != null && message != ''}">
        <div class="alert alert-info">
            <h4><a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>
                <strong>${message}</strong></h4>
        </div>
    </c:if>

        <%--PERIODS--%>
    <c:if test="${periods != null}" >

        <h2>Available periods:</h2>

        <table id="table" class="table table-striped table-bordered table-hover" cellspacing="0" width="100%">
            <thead>
            <tr>
                <th>Select Years:</th>
                <th>Select Month:</th>
                <th id="sendCostsCompare"><a href="#">Compare 2 periods</a></th>
                <th>Changed</th>
            </tr>
            </thead>

            <tbody>
            <form id="costsCompare" name="input" action="${contextPath}/costs/compare" method="get">

            <c:forEach items="${periods}" var="period">
                <tr>
                    <td><a href="${contextPath}/costs/get/all?year=${period.year}">
                        <c:out value="${period.year}" /></a>
                    </td>
                    <td><a href="${contextPath}/costs/get/one?period=${period.year}-${period.month}">
                        <c:out value="${period.monthName}" /></a>
                    </td>
                    <td>
                        <div class="checkbox">
                            <label><input type="checkbox" name="comparingCosts" value="${period.id}" /></label>
                        </div>
                    </td>
                    <td>
                        <c:if test="${period.updated == true}">
                            <strong>NEW!</strong>
                        </c:if>
                        <c:if test="${period.updated == false}">
                            not changed
                        </c:if>
                    </td>
                </tr>
            </c:forEach>

            </form>
            </tbody>
        </table>

    </c:if>

        <%--MODAL select periods error--%>
        <div class="modal fade" id="openModal" role="dialog">
            <div class="modal-dialog modal-sm">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal">&times;</button>
                        <h4 class="modal-title" id="modalName"><%--content--%></h4>
                    </div>
                    <div class="modal-body" id="modalData">
                        <p><strong><%--content--%></strong></p>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                    </div>
                </div>
            </div>
        </div>

</div>
<!-- /container -->
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.2/jquery.min.js"></script>
<script src="${contextPath}/resources/js/bootstrap.min.js"></script>
</body>
</html>

<script>
    $(document).ready(function() {
        $('#table').dataTable( {
            "pageLength": 10
        } );
    });
</script>
<script>
    var form = document.getElementById("costsCompare");
    document.getElementById("sendCostsCompare").addEventListener("click", function () {
        if ($(":checkbox[name='comparingCosts']:checked").length == 2) {
            form.submit();
        } else {
            $("#modalName").html("Selection error:");
            $("#modalData").html("Please select 2 periods for comparing");
            $("#openModal").modal('show');
        }
    });
    $(":checkbox[name='comparingCosts']").change(function(){
        if ($(":checkbox[name='comparingCosts']:checked").length == 2)
            $(':checkbox:not(:checked)').prop('disabled', true);
        else
            $(':checkbox:not(:checked)').prop('disabled', false);
    });
</script>
