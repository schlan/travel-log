/**
 * Created by basti on 6/14/14.
 */
var map, marker, mapslayer,cloudmadeLayer, linesLayer = {};

function initMapForSummary(data) {
    initLeaflet()

    var tracks = data["condensedTracks"]

    if(tracks.length > 0) {

        var lastKnownPos = data["lastKnownPosition"]
        //console.log(lastKnownPos)
        if (lastKnownPos != null) {
            var icon = L.AwesomeMarkers.icon({
                icon: 'glyphicon glyphicon-asterisk',
                markerColor: 'red'
            })

            var lastPos = L.marker([lastKnownPos["latitude"], lastKnownPos["longitude"]], {
                'icon': icon,
                'riseOnHover': true
            })
            lastPos.addTo(map)
        }

        var trackOverview = Array()

        for (x in tracks) {
            var track = tracks[x]
            var line = getPolyLine(track)
            //line.on("click", function(){ window.location.href = ""  + track["id"]; })//TODO
            trackOverview.push(line)
        }

        var tracks = L.featureGroup(trackOverview)
        map.fitBounds(tracks.getBounds());
        tracks.addTo(map)

        linesLayer["Tracks"] = tracks
        linesLayer["Last known Position"] = lastPos


        var imageLayer = getImageMarkerGroup(data["newestImages"])
        imageLayer.addTo(map)
        linesLayer["Images"] = imageLayer

        L.control.layers(mapslayer, linesLayer).addTo(map)

    }

    L.control.scale().addTo(map)
    cloudmadeLayer.addTo(map)
}

function initMap(data) {
    initLeaflet()

    for (x in data["tracks"]) {
        var track = data["tracks"][x]
        var polyline = getPolyLine(track)

        polyline.addTo(map)
        map.fitBounds(polyline.getBounds());

        linesLayer[track['name'].substring(0,15) + "..."] = polyline
    }

    var imageLayer = getImageMarkerGroup(data["images"])
    imageLayer.addTo(map)
    linesLayer["Images"] = imageLayer

    L.control.layers(mapslayer, linesLayer).addTo(map)
    L.control.scale().addTo(map)
    cloudmadeLayer.addTo(map)

}

function initLeaflet(){
    $("#map").fadeIn( 1000 )
    map = L.map('map', {fullscreenControl: true});

    cloudmadeLayer = L.tileLayer('http://{s}.tile.osm.org/{z}/{x}/{y}.png', {
        attribution: '&copy; <a href="http://osm.org/copyright">OpenStreetMap</a> contributors'
    })

    var cycleMap = L.tileLayer('http://{s}.tile.opencyclemap.org/transport/{z}/{x}/{y}.png', {
        attribution: 'Tiles courtesy of <a href="http://www.opencyclemap.org/"" target="_blank">Andy Allan</a>'
    })

    var googleLayerRoad = new L.Google('ROADMAP', {featureType: 'all'});
    var googleLayerSat = new L.Google('SATELLITE', {featureType: 'all'});
    var googleLayerHybrid = new L.Google('HYBRID', {featureType: 'all'});
    var googleLayerTerrain = new L.Google('TERRAIN', {featureType: 'all'});

    mapslayer = {
        'OpenStreetMap':cloudmadeLayer,
        'OpenCycleMap':cycleMap,
        'Google Satellite': googleLayerSat,
        'Google Road': googleLayerRoad,
        'Google Hybrid': googleLayerHybrid,
        'Google Terrain': googleLayerTerrain
    }

    var OpenWeatherMap_CloudsClassic = L.tileLayer('http://{s}.tile.openweathermap.org/map/clouds_cls/{z}/{x}/{y}.png', {
        attribution: 'Map data &copy; <a href="http://openweathermap.org">OpenWeatherMap</a>',
        opacity: 0.5
    });
    var OpenWeatherMap_PrecipitationClassic = L.tileLayer('http://{s}.tile.openweathermap.org/map/precipitation_cls/{z}/{x}/{y}.png', {
        attribution: 'Map data &copy; <a href="http://openweathermap.org">OpenWeatherMap</a>',
        opacity: 0.5
    });
    var OpenWeatherMap_Wind = L.tileLayer('http://{s}.tile.openweathermap.org/map/wind/{z}/{x}/{y}.png', {
        attribution: 'Map data &copy; <a href="http://openweathermap.org">OpenWeatherMap</a>',
        opacity: 0.5
    });
    var OpenWeatherMap_Temperature = L.tileLayer('http://{s}.tile.openweathermap.org/map/temp/{z}/{x}/{y}.png', {
        attribution: 'Map data &copy; <a href="http://openweathermap.org">OpenWeatherMap</a>',
        opacity: 0.5
    });


    linesLayer["Clouds"] = OpenWeatherMap_CloudsClassic
    linesLayer["Precipitation"] = OpenWeatherMap_PrecipitationClassic
    linesLayer["Wind"] = OpenWeatherMap_Wind
    linesLayer["Temperature"] = OpenWeatherMap_Temperature



    marker = L.marker([0, 0],{
        clickable: true
    })

    marker.addTo(map)

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

function zoomToPosition(lon, lat){
    map.setView([lon,lat],16)
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

    var icon =  L.AwesomeMarkers.icon({
        icon: 'glyphicon glyphicon-picture',
        markerColor: 'darkred'
    })

    var sortedImages = Array()

    for(x in images){
        var image = images[x]
        var key = image["location"]["latitude"]  + "\&" + image["location"]["longitude"]
        if (typeof sortedImages[key] === "undefined") {
            sortedImages[key] = [image]
        }else{
            sortedImages[key].push(image)
        }
    }

    for(x in sortedImages){

        var location = sortedImages[x]

        var indicator = ""
        var slides = ""
        var i = 0

        for(y in location){
            var image = location[y]
            var path = "/api/images/getImage/" + image["path"]

            if(i == 0){
                indicator +=  "<li data-target='#carousel-example-generic' class='active' data-slide-to='"+ i + "'></li>"
                slides +=   "<div class='item active'>"
            }else{
                indicator +=  "<li data-target='#carousel-example-generic' data-slide-to='"+ i + "'></li>"
                slides +=   "<div class='item'>"
            }

            slides +=       "<img src='"+ path + "' style='height:200px;' ></img>" +
                            "<div class='carousel-caption'></div>" +
                        "</div>"
            i++

        }
        var popupMarkUp =   "<div id='carousel-example-generic' class='carousel slide' data-ride='carousel' style='width:300px;'>"

        popupMarkUp +=      "<ol class='carousel-indicators'>" + indicator + "</ol>"

        popupMarkUp +=      "<div class='carousel-inner'>" + slides + "</div>"

        popupMarkUp += "<a class='left carousel-control' href='#carousel-example-generic' role='button' data-slide='prev'>" +
                            "<span class='glyphicon glyphicon-chevron-left'></span>" +
                       "</a>" +
                       "<a class='right carousel-control' href='#carousel-example-generic' role='button' data-slide='next'>" +
                            "<span class='glyphicon glyphicon-chevron-right'></span>" +
                       "</a>"

        popupMarkUp += "</div>"

        var marker = L.marker(x.split("\&"),{
            'icon' : icon,
            'riseOnHover':true
        })

        marker.bindPopup(popupMarkUp)
        markers.push(marker)

    }

    return L.layerGroup(markers)
}


