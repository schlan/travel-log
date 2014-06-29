/**
 * Created by basti on 6/14/14.
 */

var initialRange;

function initFlot(data) {
    var tracks = data["tracks"]

    var dataSet = []
    for (x in tracks) {
        var track = tracks[x];
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
        }
    }

    var plot = $.plot($("#chart"), dataSet, options);


    var range = Array(plot.getAxes().xaxis.min, plot.getAxes().xaxis.max)


    $('<div class="button" style="right:20px;bottom:30px">zoom out</div>').appendTo($("#chart")).click(function (e) {
        plot.setSelection({ xaxis: { from: range[0], to: range[1]}});

    });

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

        for(x in tracks){
            var track = tracks[x];
            for(y in track["trackPoints"]){
               var trackPt = track["trackPoints"][y];
               if(Math.abs(new Date(Date.parse(trackPt["datetime"])).getTime() - selectedDate) < bestDiff){
                    bestDiff = Math.abs(new Date(Date.parse(trackPt["datetime"])).getTime() - selectedDate);
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

        var date = trkPt["datetime"]
        var elevation = trkPt["elevation"]

        var date = new Date(Date.parse(date))
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
