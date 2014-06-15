/**
 * Created by basti on 6/14/14.
 */

function initFlot(data) {

    var dataSet = []
    for(x in data) {
        var track = data[x];
        dataSet.push(getDataSet(track))
    }

    var options = {

        series: {
            shadowSize: 5

        },
        xaxis: {
            mode: "time"
        },
        selection: {
            mode: "x"
        },
        grid: {
            hoverable: true
        }

    }

    var plot = $.plot($("#chart"), dataSet, options);

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
}

function getDataSet(track){

    var points = [];
    console.log(track)
    for(i in track["trackPoints"]){
        var trkPt = track["trackPoints"][i]

        var date = trkPt["datetime"]
        var elevation = trkPt["elevation"]
        points.push(Array(new Date(Date.parse(date)).getTime(), elevation))
    }

    var dataSet = {
        label: "Elevation",
            data: points,
        color: getRandomColor(),
        lines: { show: true }
    }

    return dataSet;
}
