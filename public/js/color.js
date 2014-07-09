/**
 * Created by basti on 6/18/14.
 */

var availableColors = Array("#00008B","#FF0000", "#008000","#000000", "#8B0000", "#006400", "#A9A9A9")
var usedColors = Array()

function initColors(data) {

    for (x in data["tracks"]) {
        var trackId = data["tracks"][x]["trackId"];
        var color = availableColors[(x % availableColors.length)]
        usedColors[trackId] = color;
    }
}

function getColorForTrackId(trackId) {
    return usedColors[trackId]
}
