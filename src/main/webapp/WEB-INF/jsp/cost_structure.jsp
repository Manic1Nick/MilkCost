<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<c:set var="period" value="${period}"/>
<c:set var="structure" value="${structure}"/>
<c:set var="costs" value="${costs}"/>
<c:set var="names" value="${names}"/>

<!DOCTYPE html>
<html>

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


    <script src= "https://cdn.zingchart.com/zingchart.min.js"></script>
    <script> zingchart.MODULESDIR = "https://cdn.zingchart.com/modules/";
    ZC.LICENSE = ["569d52cefae586f634c54f86dc99e6a9","ee6b7db5b51705a13dc2339db3edaf6d"];</script>
</head>
<body>
<div class="container">

    <table id="table" class="table table-striped table-bordered table-hover" cellspacing="0" width="100%">
        <thead>
        <tr>
            <th>Period</th>
            <c:forEach items="${names}" var="name">
                <th><c:out value="${name}" /></th>
            </c:forEach>
        </tr>
        </thead>

        <tbody>
        <c:forEach items="${costs}" var="outerMap">
            <tr>
                <td><c:out value="${outerMap.key}" /></td>
                <c:forEach items="${outerMap.value}" var="innerMap">
                    <td><c:out value="${innerMap.value}" /></td>
                </c:forEach>
            </tr>
        </c:forEach>
        </tbody>
    </table>

    <div id='myChart'></div>

</div>
</body>
</html>

<script>
    $(document).ready(function() {
        $('#table').dataTable();
    });
</script>

<script>
    var period = '<c:out value="${period.monthName}, ${period.year}"/>';
    var myConfig = {
        type: "pie",
        backgroundColor: "#ffffff",
        plot: {
            borderColor: "#ffffff",
            borderWidth: 5,
            // slice: 90,
            valueBox: {
                placement: 'out',
                text: '%t\n%npv%',
                fontFamily: "Open Sans"
            },
            tooltip:{
                fontSize: '18',
                fontFamily: "Open Sans",
                padding: "5 10",
                text: "%npv%"
            },
            animation:{
                effect: 2,
                method: 5,
                speed: 500,
                sequence: 1
            }
        },
        source: {
            text: 'gs.statcounter.com',
            fontColor: "#8e99a9",
            fontFamily: "Open Sans"
        },
        title: {
            fontColor: "#000000",
            text: 'Cost Structure',
            align: "left",
            offsetX: 10,
            fontFamily: "Open Sans",
            fontSize: 25
        },
        subtitle: {
            offsetX: 10,
            offsetY: 10,
            fontColor: "#000000",
            fontFamily: "Open Sans",
            fontSize: "16",
            text: period,
            align: "left"
        },
        plotarea: {
            margin: "20 0 0 0"
        },
        series : [
            <c:forEach var="cost" items="${structure}" varStatus="status">
            {
                values : [${cost.share}],
                text: "${cost.nameCosts}",
                backgroundColor: '${cost.color}'
            }<c:if test="${!status.last}">,</c:if>
            </c:forEach>
        ]
    };

    zingchart.render({
        id : 'myChart',
        data : myConfig,
        height: 500,
        width: 725
    });
</script>
