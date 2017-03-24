<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<c:set var="name" value="${costName}"/>
<c:set var="dates" value="${dates}"/>
<c:set var="sums" value="${sums}"/>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->
    <meta name="description" content="">
    <meta name="author" content="">

    <title>Cost dynamic page</title>

    <link href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" rel="stylesheet"/>
    <link href="https://cdnjs.cloudflare.com/ajax/libs/datatables/1.10.12/css/dataTables.bootstrap.min.css" rel="stylesheet"/>

    <script src= "https://cdn.zingchart.com/zingchart.min.js"></script>
    <script> zingchart.MODULESDIR = "https://cdn.zingchart.com/modules/";
    ZC.LICENSE = ["569d52cefae586f634c54f86dc99e6a9","ee6b7db5b51705a13dc2339db3edaf6d"];</script>
</head>
<body>
<div class="container">
    <div id='myChart'></div>
</div>
</body>
</html>

<script>
    var minDate = convertDateToUnix('${dates[0]}');
    var myConfig = {
        type: 'line',
        title:{
            text: "Cost " + '${name}' + " in dynamic",
            adjustLayout: true,
            fontColor:"#53535e",
            marginTop: 7
        },
        legend:{
            align: 'center',
            verticalAlign: 'top',
            backgroundColor:'none',
            borderWidth: 0,
            item:{
                fontColor:'#53535e',
                cursor: 'hand'
            },
            marker:{
                type:'circle',
                borderWidth: 0,
                cursor: 'hand'
            }
        },
        plotarea:{
            margin:'dynamic 70'
        },
        plot:{
            aspect: 'spline',
            lineWidth: 2,
            marker:{
                borderWidth: 0,
                size: 5
            }
        },
        scaleX:{
            lineColor: '#E3E3E5',
            zooming: true,
            zoomTo:[0,15],
            minValue: minDate,
            step: 'month',
            item:{
                fontColor:'#53535e'
            },
            transform:{
                type: 'date',
                all: '%Y %M'
            }
        },
        scaleY:{
            minorTicks: 1,
            lineColor: '#E3E3E5',
            tick:{
                lineColor: '#E3E3E5'
            },
            minorTick:{
                lineColor: '#E3E3E5'
            },
            minorGuide:{
                visible: true,
                lineWidth: 1,
                lineColor: '#E3E3E5',
                alpha: 0.7,
                lineStyle: 'dashed'
            },
            guide:{
                lineStyle: 'dashed'
            },
            item:{
                fontColor:'#53535e'
            }
        },
        tooltip:{
            borderWidth: 0,
            borderRadius: 3
        },
        preview:{
            adjustLayout: true,
            borderColor:'#E3E3E5',
            mask:{
                backgroundColor:'#E3E3E5'
            }
        },
        crosshairX:{
            plotLabel:{
                multiple: true,
                borderRadius: 3
            },
            scaleLabel:{
                backgroundColor:'#53535e',
                borderRadius: 3
            },
            marker:{
                size: 7,
                alpha: 0.5
            }
        },
        crosshairY:{
            lineColor:'#E3E3E5',
            type:'multiple',
            scaleLabel:{
                decimals: 2,
                borderRadius: 3,
                offsetX: -5,
                fontColor:"#53535e",
                bold: true
            }
        },
        shapes:[
            {
                type:'rectangle',
                id:'view_all',
                height:'20px',
                width:'75px',
                borderColor:'#E3E3E5',
                borderWidth:1,
                borderRadius: 3,
                x:'85%',
                y:'11%',
                backgroundColor:'#53535e',
                cursor:'hand',
                label:{
                    text:'View All',
                    fontColor:'#E3E3E5',
                    fontSize:12,
                    bold:true
                }
            }
        ],
        series: [
            /*{
                values: [218.92,212.85,241.95,200.76,203.87,245.26,249.9,240.05,241.8,251.4,230.06,null,null,203.04,229.05,232.37,190.89,236.63,249.18,188.98,194.06,234.61,241.91,196.4,191.87,213.5,194.83,228.48,235.66,235.04,195.74,243.04,null,197.51,232.64,238.99,227.8,235.81,221.7,193.46,252.64,205.35,248.52,218.32,188.55],
                lineColor:'#E34247',
                marker:{
                    backgroundColor:'#E34247'
                }
            },
            {
                values:[165.57,170.47,197.17,164.64,132.73,176.89,139.41,158.71,177.85,138.87,135.74,167.06,156.42,182,169.73,151.08,165.58,146.29,124.5,181.71,143.96,null,null,null,146,172.6,149.81,161.09,175.88,149.39,184.1,123.85,186.82,139.72,138.61,170.28,164.06,184.33,null,null,131.85,133.32,134.49,143.79,125.23],
                lineColor: '#FEB32E',
                marker:{
                    backgroundColor:'#FEB32E'
                }
            },*/
            {
                text: '${name}',
                values: ${sums},
                lineColor:'#31A59A',
                marker:{
                    backgroundColor:'#31A59A'
                }
            }
        ]
    };

    zingchart.render({
        id: 'myChart',
        data: myConfig,
        height: '500',
        width: '725'
    });

    zingchart.shape_click = function(p){
        if(p.shapeid == "view_all"){
            zingchart.exec(p.id,'viewall');
        }
    };

    function convertDateToUnix(val){
        var date = new Date(val);
        return Date.parse(date);
    }

</script>


<style type="text/css">

    html, body {
        height:100%;
        width:100%;
        margin-left:10;
        padding:0;
    }
    #myChart {
        height:100%;
        width:100%;
        min-height:150px;
    }
    .zc-ref {
        display:none;
    }


</style>






