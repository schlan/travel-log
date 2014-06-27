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

    map = L.map('map', {
        center: [39.73, -104.99],
        zoom: 10

    });

    var mapsLayer = {
        'OSM':cloudmadeLayer,
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

    L.control.layers(mapsLayer, linesLayer).addTo(map)
    L.control.scale().addTo(map)
    cloudmadeLayer.addTo(map)

}


function moveMarkerTo(lon, lat, elevation, datetime) {
    marker.closePopup()
    marker.setLatLng({lat: lat, lng: lon})
    marker.bindPopup(
        "<b>Datetime:  </b>" + new Date(Date.parse(datetime)) + "<br />" +
        "<b>Elevation:  </b>" + elevation + "m<br />" +
        "<b>Position: </b>" + lat +"/" + lon + "<br />"
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


