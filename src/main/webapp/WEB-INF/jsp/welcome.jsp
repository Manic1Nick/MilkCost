<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<c:set var="contextPath" value="${pageContext.request.contextPath}"/>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- The above 3 meta tags *must* come first in the head;
    any other head content must come *after* these tags -->
    <meta name="description" content="">
    <meta name="author" content="">

    <title>Main menu</title>

    <link href="${contextPath}/resources/css/bootstrap.min.css" rel="stylesheet">
    <link href="${contextPath}/resources/css/common.css" rel="stylesheet">

    <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
    <!--[if lt IE 9]>
    <script src="https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js"></script>
    <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
    <![endif]-->

    <!-- /container -->
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.2/jquery.min.js"></script>
    <script src="${contextPath}/resources/js/bootstrap.min.js"></script>
</head>

<body>
<div class="container">

    <%--ALERT MESSAGE--%>
    <c:if test="${message != null && message != ''}">
        <div class="alert alert-success">
            <h4><a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>
                <strong>${message}</strong></h4>
        </div>
    </c:if>

    <p style="text-align:center;">
        <img src="${contextPath}/resources/images/the-true-cost-of-milk-img2.png"
             style="max-width:50%; max-height:50%;">
    </p>

    <%--SHOW COSTS--%>
    <h4 class="text-center">
        <a href="#" onclick="openSelectPeriodModal()"
           data-toggle="tooltip" title="Only show and analyze available periods">
                <strong>Show and analyze milk costs</strong>
        </a>
    </h4>

    <%--ADD NEW DATA--%>
    <h5 class="text-center">
        <a href="#" onclick="addNewCosts()"
           data-toggle="tooltip" title="If you want add new periods to database">
                Add data from new files
        </a>
    </h5>

    <%--UPDATE DB--%>
    <h5 class="text-center">
        <a href="#" onclick="updateDatabase()"
            data-toggle="tooltip" title="If you want update all periods in database">
                Update database
        </a>
    </h5>

    <div class="alert alert-warning">
        <a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>
        <h4>This is a program for analyzing data on the costs of milk production in Ukrainian Milk Company</h4>
    </div>

    <%--MODAL order confirm--%>
    <div class="modal fade" id="openModalPeriodSelect" role="dialog">
        <div class="modal-dialog modal-big">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal">&times;</button>
                    <h4 class="modal-title">Select period:</h4>
                </div>
                <div class="modal-body" id="modalDataConfirm">
                    <p>If some of the requested periods are missing in the database, you will get last of available periods</p>
                </div>
                <div class="modal-footer">
                    <p><button type="button" class="btn btn-success"
                               onclick="selectPeriod(1)">Last month</button>
                        <button type="button" class="btn btn-success"
                                onclick="selectPeriod(3)">Last quartile</button>
                        <button type="button" class="btn btn-success"
                                onclick="selectPeriod(12)">Last year</button>
                    </p>
                    <p>
                        <button type="button" class="btn btn-success"
                               onclick="selectPeriod('currentYear')">Current year</button>
                        <button type="button" class="btn btn-success"
                                onclick="selectPeriod('all')">All periods</button>
                        <button type="button" class="btn btn-default"
                                data-dismiss="modal">Close</button>
                    </p>
                </div>
            </div>
        </div>
    </div>

        <%--MODAL add & update--%>
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
</body>
</html>
<script>
    function openSelectPeriodModal() {
        $("#openModalPeriodSelect").modal('show');
    }

    function selectPeriod(months) {
        window.location.assign("${contextPath}/show/periods?months=" + months);
    }

    function addNewCosts() {
        $.ajax({
            url: '${contextPath}/costs/add',
            type: 'GET'
        }).success(function (resp) {
            $("#modalName").html("Adding costs info:");
            $("#modalData").html(resp);
            $("#openModal").modal('show');
        }).error(function (resp) {
            $("#modalName").html("ERROR:");
            $("#modalData").html("Error adding costs");
            $("#openModal").modal('show');
        })
    }

    function updateDatabase() {
        $.ajax({
            url: '${contextPath}/costs/update/database',
            type: 'GET'
        }).success(function (resp) {
            $("#modalName").html("Update database info:");
            $("#modalData").html(resp);
            $("#openModal").modal('show');
        }).error(function (resp) {
            $("#modalName").html("ERROR:");
            $("#modalData").html("Error updating database");
            $("#openModal").modal('show');
        })
    }
</script>
