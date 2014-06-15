/**
 * Created by basti on 6/14/14.
 */

function initMap(data) {
    var map = new OpenLayers.Map("map",
        {
            projection: new OpenLayers.Projection("EPSG:4326"),
            displayProjection: new OpenLayers.Projection("EPSG:4326")
        }
    );

    var ol = new OpenLayers.Layer.OSM();

    var gm = new OpenLayers.Layer.Google(
        "Google Satellite",
        {type: google.maps.MapTypeId.SATELLITE, numZoomLevels: 22}
    );

    var gmap = new OpenLayers.Layer.Google(
        "Google Streets", // the default
        {numZoomLevels: 20}
    );

    map.addLayer(ol);
    map.addLayer(gm);
    map.addLayer(gmap);


    for(x in data){
        var track = data[x];
        var layer = getLineLayer(track, map);
        map.addLayer(layer);
        map.addControl(new OpenLayers.Control.DrawFeature(layer, OpenLayers.Handler.Path));
    }

    map.addControl(new OpenLayers.Control.LayerSwitcher());
}


function getLineLayer(track, map){
    var lineLayer = new OpenLayers.Layer.Vector(track["name"]);

    var points = [];

    for(i in track["trackPoints"]){


        var trkPt = track["trackPoints"][i];
        var lon = trkPt['latitude'];
        var lat = trkPt['longitude'];
        points.push(new OpenLayers.Geometry.Point(lat,lon).transform(new OpenLayers.Projection("EPSG:4326"), map.getProjectionObject()))

    }

    var line = new OpenLayers.Geometry.LineString(points);

    var style = {
        strokeColor: approveTrackColor(track["metadata"]["displaycolor"]),
        strokeOpacity: 0.7,
        strokeWidth: 5
    };

    var lineFeature = new OpenLayers.Feature.Vector(line, null, style);
    lineLayer.addFeatures([ lineFeature ]);
    map.zoomToExtent(lineLayer.getDataExtent())
    return lineLayer
}


//var availableColors = Array("#000000", "#8B0000", "#006400", "#DAA520", "#00008B", "#8B008B", "#008B8B", "#D3D3D3", "#A9A9A9", "#FF0000", "#08000", "#FFFF00", "#0000FF", "#FF00FF", "#00FFFF", "#FFFFFF")
var availableColors = Array("#000000", "#8B0000", "#006400", "#A9A9A9", "#FF0000", "#08000", "#0000FF")
var usedColors = Array()

function approveTrackColor(color){

    if(color == "null"){
        var color = getRandomColor();
        while($.inArray(color, usedColors)){
            color = getRandomColor();
        }
        usedColors.push[color]
        return color;
    }else{
        //TODO
        return getRandomColor();
    }
}

function getRandomColor(){
    return availableColors[Math.floor(Math.random()*availableColors.length)]
}