/**
 * Created by basti on 6/14/14.
 */

var initialRange;

function initFlotForSummary(data){
    initGeneralChart(data["condensedTracks"], false)
}

function initFlot(data){
    initGeneralChart(data["tracks"], true)
}

function initGeneralChart(data, legend) {
    var dataSet = []

    for (x in data) {
        var track = data[x];
        dataSet.push(getDataSet(track))
    }

    var options = {
        series: {
            shadowSize: 5,
            lines: {
                show: true
            }
        },
        crosshair: {
            mode: "x"
        },
        xaxis: {
            mode: "time"
        },
        selection: {
            mode: "x"
        },
        grid: {
            hoverable: true,
            autoHighlight: false
        },
        legend: {
            show: legend
        }

    }

    var plot = $.plot($("#chart"), dataSet, options);


    var range = Array(plot.getAxes().xaxis.min, plot.getAxes().xaxis.max)


    $('<div class="button" style="right:20px;bottom:30px">zoom out</div>').appendTo($("#chart")).click(function (e) {
        plot.setSelection({ xaxis: { from: range[0], to: range[1]}});
    });

    $("#chart").fadeIn( 1000 )


    $("#chart").bind("plotselected", function (event, ranges) {

        $.each(plot.getXAxes(), function (_, axis) {
            var opts = axis.options;
            opts.min = ranges.xaxis.from;
            opts.max = ranges.xaxis.to;
        });

        plot.setupGrid();
        plot.draw();
        plot.clearSelection();

    });

    $("#chart").bind("plothover", function (event, pos, item) {

        var selectedDate = new Date(pos.x).getTime();
        var bestTrkPt;
        var bestDiff = Number.MAX_VALUE;

        for(x in data){
            var track = data[x];
            for(y in track["trackPoints"]){
               var trackPt = track["trackPoints"][y];
               if(Math.abs(new Date(Date.fromISO(trackPt["datetime"] + "+0000" )).getTime() - selectedDate) < bestDiff){
                    bestDiff = Math.abs(new Date(Date.fromISO(trackPt["datetime"] + "+0000" )).getTime() - selectedDate);
                    bestTrkPt = trackPt;
               }
            }
        }
        moveMarkerTo(bestTrkPt["longitude"], bestTrkPt["latitude"], bestTrkPt["elevation"], bestTrkPt["datetime"])
    });
}

function getDataSet(track){

    var points = [];
    for(i in track["trackPoints"]){
        var trkPt = track["trackPoints"][i]

        var date = trkPt["datetime"] + "+0000"
        var elevation = trkPt["elevation"]

        var date = new Date(Date.fromISO(date))

        points.push(Array(date.getTime(), elevation))

    }

    var dataSet = {
        label: track["name"],
        data: points,
        color: getColorForTrackId(track["trackId"]),
        lines: {
            show: true,
            fill: true,
            fillColor: { colors: [{ opacity: 0.1 }, { opacity: 0.1}] }
        }
    }

    return dataSet;
}


Date.fromISO= (function(){
    var testIso = '2011-11-24T09:00:27+0200';
    // Chrome
    var diso= Date.parse(testIso);
    if(diso===1322118027000) return function(s){
        return new Date(Date.parse(s));
    }
    // JS 1.8 gecko
    var noOffset = function(s) {
        var day= s.slice(0,-5).split(/\D/).map(function(itm){
            return parseInt(itm, 10) || 0;
        });
        day[1]-= 1;
        day= new Date(Date.UTC.apply(Date, day));
        var offsetString = s.slice(-5)
        var offset = parseInt(offsetString,10)/100;
        if (offsetString.slice(0,1)=="+") offset*=-1;
        day.setHours(day.getHours()+offset);
        return day.getTime();
    }
    if (noOffset(testIso)===1322118027000) {
        return noOffset;
    }
    return function(s){ // kennebec@SO + QTax@SO
        var day, tz,
//        rx = /^(\d{4}\-\d\d\-\d\d([tT][\d:\.]*)?)([zZ]|([+\-])(\d{4}))?$/,
            rx = /^(\d{4}\-\d\d\-\d\d([tT][\d:\.]*)?)([zZ]|([+\-])(\d\d):?(\d\d))?$/,

            p= rx.exec(s) || [];
        if(p[1]){
            day= p[1].split(/\D/).map(function(itm){
                return parseInt(itm, 10) || 0;
            });
            day[1]-= 1;
            day= new Date(Date.UTC.apply(Date, day));
            if(!day.getDate()) return NaN;
            if(p[5]){
                tz= parseInt(p[5], 10)/100*60;
                if(p[6]) tz += parseInt(p[6], 10);
                if(p[4]== "+") tz*= -1;
                if(tz) day.setUTCMinutes(day.getUTCMinutes()+ tz);
            }
            return day;
        }
        return NaN;
    }
})()