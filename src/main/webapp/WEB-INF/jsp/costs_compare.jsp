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

    zingchart.MODULESDIR = "https://cdn.zingchart.com/modules/";
    var myConfig = {
        "type":"bullet",
        "title":{
            "text":"Cost Compare",
            "background-color":"none",
            "color":"#333"
        },
        "plotarea":{

        },
        "scaleX":{
            "guide":{
                "visible":false
            },
            "label":["Costs"],
            "values":[
                <c:forEach var="name" items="${names}" varStatus="status">
                    "${name}"<c:if test="${!status.last}"> ,</c:if>
                </c:forEach>
            ],
            "tick":{
                "line-color":"#333"
            },
            "line-color":"#666",
            "item":{
                "wrapText":true,
                "color":"#333"
            }
        },
        "scaleY":{
            "tick":{
                "line-color":"#333"
            },
            "line-color":"#333",
            "guide":{
                "lineStyle":"solid"
            },
            "item":{
                "wrapText":true,
                "color":"#333"
            },
            "short":true,
            "thousands-separator":","
        },
        "plot":{
            "tooltip":{
                "fontSize":15,
                "align":"left",
                "borderRadius":3,
                "borderWidth":2,
                "borderColor":"%color-1",
                "backgroundColor":"#fff",
                "shadow":0,
                "alpha":0.9,
                "padding":10,
                "color":"#000",
                "negation":"currency",
                "thousandsSeparator":",",
                "text":"<b style=\"color:%color\">Current: %node-value</b> UAH<br><em style=\"color:#C4473F\">Previous: %g</em> UAH",
            },
            "goal":{
                "background-color":"#C4473F"
            }
        },
        "series":[
            {
                "values":${next},
                "goals":${previous},
                rules:[ // rules for color
                    {
                        rule: '%v > %g/0.9', // if greater than goal
                        backgroundColor: '#ef5350'
                    },
                    {
                        rule: '%v < %g/0.9 && %v > %g/1.1', // if less than half goal
                        backgroundColor: '#ffca28'
                    },
                    {
                        rule: '%v < %g/1.1', // if in between
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
        width: 725
    });



</script>
<style type="text/css">
    .zc-ref {
        display: none;
    }
</style>






