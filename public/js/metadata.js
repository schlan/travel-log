/**
 * Created by basti on 6/15/14.
 */


function initMetadataForSummary(data){
    var metadata = data["summarizedMetaDataByActivity"]
    var niceMetaData = Array()

    // Merge Data
    for(x in metadata){
        var at = x
        var m = metadata[x]

        var nm = Array()
        nm["Name"] = at

        var formatted = makeMetaDataLookNice(m)
        for (attr in formatted) { nm[attr] = formatted[attr]; }

        niceMetaData[at] = nm
    }
    renderMetaDataToTable(niceMetaData)
}

function initMetadata(data){
    var niceMetaData = Array()

    // Merge Data
    for(x in data["tracks"]){
        var track = data["tracks"][x]
        var nm = Array()
        nm["Name"] = track["name"]

        var formatted = makeMetaDataLookNice(track["metadata"])
        for (attr in formatted) { nm[attr] = formatted[attr]; }

        nm["Acivity"] = track["activity"]
        niceMetaData[track["trackId"]] = nm
    }
    if(Object.keys(niceMetaData).length > 1) {
        var sum = Array()
        sum["Name"] = "Sum"
        var formatted = makeMetaDataLookNice(data["summarizedMetadata"])
        for (attr in formatted) {
            sum[attr] = formatted[attr];
        }
        niceMetaData["1565165ad"] = sum
    }
    var cols = ((data["tracks"].length > 1) ? data["tracks"].length  + 2 : data["tracks"].length + 1)
    renderMetaDataToTable(niceMetaData, cols)

}

function renderMetaDataToTable(niceMetaData, cols){

    // Get All Keys
    var keys = Array()
    for(x in niceMetaData){
        var meta = niceMetaData[x]
        for(y in meta){
            if($.inArray(y, keys) == -1) {
                keys.push(y)
            }
        }
    }

    var colWidth = 100 / cols

    var table = "<thead>" + getMetaDataTableRow("Name", niceMetaData,"th", colWidth) + "</thead>"

    keys.splice(keys.indexOf("Name"),1)

    for(key in keys){
        table += getMetaDataTableRow(keys[key],niceMetaData, "td", colWidth)
    }

    $('#accordion').append($(
            "<div class='table-responsive'>" +
            "<table class='table table-condensed table-hover'>" +
            table +
            "</table>"+
            "</div>"
    ));
}

function getMetaDataTableRow(key,niceMetaData, celltype, colWidth){
    var row = "<tr><" + celltype +" style='width:" +colWidth + "%'>" + key +"</"+ celltype + "></th>"

    for(x in niceMetaData){
        var value = niceMetaData[x][key]
        row += "<" + celltype +" class='text-right' style='width:" +colWidth + "%'>" + value +"</"+ celltype + ">"
    }

    return row + "</tr>"
}

function getMetaDataTabHtml(name, metadata, i){
    return '<div class="panel panel-default">' +
    '<div class="panel-heading">' +
    '<h4 class="panel-title">' +
    '<a data-toggle="collapse" data-parent="#accordion" href="#collapse' + i + '">' +
    name +
    '</a>'+
    '</h4>' +
    '</div>'+
    '<div id="collapse' + i + '" class="panel-collapse collapse">' +
    '<div class="panel-body">' +
    getMetaDataAsHtml(makeMetaDataLookNice(metadata)) +
    '</div>' +
    '</div>' +
    '</div>'
}

function getMetaDataAsHtml(metadata){
    var table = '<table class="table table-condensed table-hover">'

    for(x in metadata){
        if(metadata[x] == null) continue
        table += '<tr><td>'+ x + '</td><td style="text-align:right;">' + metadata[x] + '</td></tr>'
    }

    table += '</table>'
    return table
}

function makeMetaDataLookNice(metadata){
    var newMetaData = Array()
    for(x in metadata){
        if(metadata[x] == null) continue
        switch (x){
            case "description":
                newMetaData["Description"] = metadata[x]
                break
            case "distance":
                newMetaData["Distance"] = makeALengthLookNice(metadata[x], true)
                break
            case "timerTime":
                newMetaData["Timer time"] = makeATimeSpanLookNice(metadata[x])
                break
            case "totalElapsedTime":
                newMetaData["Total elapsed time"] = makeATimeSpanLookNice(metadata[x])
                break
            case "movingTime":
                newMetaData["Moving time"] = makeATimeSpanLookNice(metadata[x])
                break
            case "stoppedTime":
                newMetaData["Stopped time"] = makeATimeSpanLookNice(metadata[x])
                break
            case "movingSpeed":
                newMetaData["Moving avg"] = makeSpeedLookNice(metadata[x], true)
                break
            case "maxSpeed":
                newMetaData["Max. speed"] = makeSpeedLookNice(metadata[x], true)
                break
            case "maxElevation":
                newMetaData["Max. elevation"] = makeALengthLookNice(metadata[x], false)
                break
            case "minElevation":
                newMetaData["Min. elevation"] = makeALengthLookNice(metadata[x], false)
                break
            case "ascent":
                newMetaData["Ascent"] = makeALengthLookNice(metadata[x], false)
                break
            case "descent":
                newMetaData["Descent"] = makeALengthLookNice(metadata[x], false)
                break
            case "avgAscentRate":
                newMetaData["Avg. ascent rate"] = makeSpeedLookNice(metadata[x], false)
                break
            case "maxAscentRate":
                newMetaData["Max. ascent rate"] = makeSpeedLookNice(metadata[x], false)
                break
            case "avgDescentRate":
                newMetaData["Avg. descent rate"] = makeSpeedLookNice(metadata[x], false)
                break
            case "maxDescentRate":
                newMetaData["Max. descent rate"] = makeSpeedLookNice(metadata[x], false)
                break
            case "calories":
                newMetaData["Calories"] = metadata[x]
                break
            case "activity":
                newMetaData["Moving type"] = metadata[x]
                break
        }
    }
    return newMetaData
}



function makeSpeedLookNice(speed, convert) {
    if (convert == true) {
        return round((speed * 3.6), -2) + "&thinsp;km/h"
    } else {
        return speed + "&thinsp;m/s"
    }
}

function makeCaloriesLookNice(calories){
    return calories + "&thinsp;m/s"
}

function makeALengthLookNice(length, convert){
    if(length >= 1000 && convert == true){
        return round((length/1000),-2) + "&thinsp;km"
    }else{
        return length + "&thinsp;m"
    }
}

function makeATimeSpanLookNice(timeInSeconds){
    var hours = (timeInSeconds - (timeInSeconds%3600))/3600
    var leftOverSeconds = (timeInSeconds - (3600 * hours))
    var minutes = (leftOverSeconds - (leftOverSeconds%60))/60
    var seconds = (leftOverSeconds - (60*minutes))
    return hours + ":" + numberToTwoDigits(minutes) + ":" + numberToTwoDigits(seconds)
}

function numberToTwoDigits(number){
    if(number < 10){
        return "0" + number
    }else{
       return number
    }
}

function round(x, n){
    return Math.round(x * 100) / 100
}