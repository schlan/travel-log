/**
 * Created by basti on 6/14/14.
 */
var map, marker;

function initMap(data) {

    var googleLayerRoad = new L.Google('ROADMAP', {featureType: 'all'});
    var googleLayerSat = new L.Google('SATELLITE', {featureType: 'all'});
    var googleLayerHybrid = new L.Google('HYBRID', {featureType: 'all'});
    var googleLayerTerrain = new L.Google('TERRAIN', {featureType: 'all'});

    var cloudmadeLayer = L.tileLayer('http://{s}.tile.osm.org/{z}/{x}/{y}.png', {
        attribution: '&copy; <a href="http://osm.org/copyright">OpenStreetMap</a> contributors'
    })

    var cycleMap = L.tileLayer('http://{s}.tile2.opencyclemap.org/transport/{z}/{x}/{y}.png', {
        attribution: 'Tiles courtesy of <a href="http://www.opencyclemap.org/"" target="_blank">Andy Allan</a>'
    });

    map = L.map('map', {});

    var mapsLayer = {
        'OSM':cloudmadeLayer,
        'Open Cycle Map':cycleMap,
        'Google Satellite': googleLayerSat,
        'Google Road': googleLayerRoad,
        'Google Hybrid': googleLayerHybrid,
        'Google Terrain': googleLayerTerrain
    }

    marker = L.marker([0, 0],{
        clickable: true
    })

    marker.addTo(map)

    var linesLayer = {}

    for (x in data["tracks"]) {
        var track = data["tracks"][x]
        var polyline = getPolyLine(track)

        polyline.addTo(map)
        map.fitBounds(polyline.getBounds());

        linesLayer[track['name']] = polyline
    }

    var imageLayer = getImageMarkerGroup(data["images"])
    imageLayer.addTo(map)
    linesLayer["Images"] = imageLayer

    L.control.layers(mapsLayer, linesLayer).addTo(map)
    L.control.scale().addTo(map)
    cloudmadeLayer.addTo(map)

}


function moveMarkerTo(lon, lat, elevation, datetime) {
    marker.closePopup()
    marker.setLatLng({lat: lat, lng: lon})
    marker.bindPopup(
        "<table class='table table-condensed'>"+
        "<tr><th>Datetime:</th><td>" + datetime + "</td></tr>" +
        "<tr><th>Elevation:</th><td>" + elevation + "m</td></tr>" +
        "<tr><th>Position:</th><td>" + lat +"/" + lon + "</td></tr>" +
        "</table>"
    )
}

function getPolyLine(track) {

    var points = []

    for (i in track["trackPoints"]) {
        var trkPt = track["trackPoints"][i]
        var lat = trkPt['latitude']
        var lon = trkPt['longitude']
        points.push(L.latLng(lat, lon))
    }

    var color = getColorForTrackId(track["trackId"])
    return L.polyline(points, {color: color})
}

function getImageMarkerGroup(images){
    var markers = Array()
    for(x in images){
        var image = images[x]
        var location = image["location"]

        var marker = L.marker([location["latitude"], location["longitude"]])
        var path = "/api/images/getImage/" + image["path"]
        marker.bindPopup(
            "<a href='"+ path +"'>" +
                "<img src='"+ path + "' class='img-responsive' width='250px'/>" +
            "</a>"
        )
        markers.push(marker)
    }
    return L.layerGroup(markers)
}


