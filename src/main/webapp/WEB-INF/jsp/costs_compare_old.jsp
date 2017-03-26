<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<c:set var="costs" value="${costs}"/>
<c:set var="names" value="${names}"/>
<c:set var="totalCosts" value="${totalCosts}"/>
<c:set var="difference" value="${difference}"/>
<c:set var="previous" value="${previous}"/>
<c:set var="next" value="${next}"/>

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
                <th><c:out value="${name}" /></th>
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
                <td><c:out value="${innerMap.value}" /></td>
            </c:forEach>
        </tr>
        </c:forEach>
        <tr>
            <td><strong>DIFFERENCE</strong></td>
            <c:forEach items="${difference}" var="map">
                <td><strong><c:out value="${map.value}" /></strong></td>
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
<script>

    zingchart.MODULESDIR = "https://cdn.zingchart.com/modules/"; // set modules dir
    zingchart.loadModules('dragging'); // load dragging module

    var myConfig = { // chart configuration
        type: 'vbullet',
        title: {
            text: 'Costs Compare'
        },
        subtitle: {
            text: 'Bars are draggable'
        },
        scaleX: {
            labels: [
                <c:forEach var="name" items="${names}" varStatus="status">
                    "${name}"<c:if test="${!status.last}"> ,</c:if>
                </c:forEach>
            ]
        },
        tooltip: { // tooltip changes based on value
            fontSize: 14,
            borderRadius: 3,
            borderWidth: 0,
            shadow: true
        },
        plot : {
            valueBox:[
                {
                    type : 'all',
                    color : '#000',
                    placement : 'goal',
                    text: '[%node-value / %node-goal-value]'
                }
            ]
        },
        series : [
            {
                dataDragging : true, // need this to enable drag
                values:
                    ${next},
                goals:
                    ${previous},
                goal:{
                    backgroundColor: '#64b5f6',
                    borderWidth: 0
                },
                rules:[ // rules for color
                    {
                        rule: '%v >= %g', // if greater than goal
                        backgroundColor: '#ffca28'
                    },
                    {
                        rule: '%v < %g/2', // if less than half goal
                        backgroundColor: '#ef5350'
                    },
                    {
                        rule: '%v >= %g/2 && %v < %g', // if in between
                        backgroundColor: '#81c784'
                    }
                ]
            }
        ]
    };

    zingchart.render({
        id : 'myChart',
        data : myConfig,
        height: 500,
        width: '100%',
        modules : "dragging" // need this to enable drag
    });

</script>
<style type="text/css">
    html, body {
        height:100%;
        width:100%;
        margin:0;
        padding:0;
    }
    #myChart {
        height:100%;
        width:100%;
        min-height:150px;
    }
</style>






