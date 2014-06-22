/**
 * Created by basti on 6/14/14.
 */
var map, marker;

function initMap(data) {

    map = new OpenLayers.Map("map",
        {
            projection: new OpenLayers.Projection("EPSG:4326"),
            displayProjection: new OpenLayers.Projection("EPSG:4326")
        }
    );

    var ol = new OpenLayers.Layer.OSM();


    var googleSat = new OpenLayers.Layer.Google(
        "Google Satellite",
        {type: google.maps.MapTypeId.SATELLITE, numZoomLevels: 22}
    );

    var googleStreet = new OpenLayers.Layer.Google(
        "Google Streets",
        {numZoomLevels: 22}
    );

    var googleHybrid = new OpenLayers.Layer.Google(
        "Google Hybrid",
        {type: google.maps.MapTypeId.HYBRID, numZoomLevels: 20}
    );

    var googlePyhs = new OpenLayers.Layer.Google(
        "Google Physical",
        {type: google.maps.MapTypeId.TERRAIN}
    );

    var markers = new OpenLayers.Layer.Markers("Markers");
    map.addLayer(markers);


    map.addLayer(ol);
    map.addLayer(googleStreet);
    map.addLayer(googleSat);
    map.addLayer(googleHybrid);
    map.addLayer(googlePyhs);

    map.addControl(new OpenLayers.Control.LayerSwitcher());
    map.addControl(new OpenLayers.Control.PanZoomBar());
    map.addControl(new OpenLayers.Control.KeyboardDefaults());
    map.addControl(new OpenLayers.Control.MousePosition());

    marker = new OpenLayers.Marker(new OpenLayers.LonLat(0, 0).transform(new OpenLayers.Projection("EPSG:4326"), map.getProjectionObject()));
    marker.map = map;
    markers.addMarker(marker);

    for (x in data["tracks"]) {
        var track = data["tracks"][x];
        var layer = getLineLayer(track, map);
        map.addLayer(layer);
        map.addControl(new OpenLayers.Control.DrawFeature(layer, OpenLayers.Handler.Path));
    }
}


function moveMarkerTo(lon, lat) {
    var px = map.getLayerPxFromViewPortPx(map.getPixelFromLonLat(new OpenLayers.LonLat(lon, lat).transform(new OpenLayers.Projection("EPSG:4326"), map.getProjectionObject())));
    marker.moveTo(px)
}

function getLineLayer(track, map) {
    var lineLayer = new OpenLayers.Layer.Vector(track["name"]);

    var points = [];

    for (i in track["trackPoints"]) {

        var trkPt = track["trackPoints"][i];
        var lat = trkPt['latitude'];
        var lon = trkPt['longitude'];

        points.push(new OpenLayers.Geometry.Point(lon, lat).transform(new OpenLayers.Projection("EPSG:4326"), map.getProjectionObject()))
    }

    var line = new OpenLayers.Geometry.LineString(points);

    var style = {
        strokeColor: getColorForTrackId(track["trackId"]),
        strokeOpacity: 0.7,
        strokeWidth: 5
    };

    var lineFeature = new OpenLayers.Feature.Vector(line, null, style);
    lineLayer.addFeatures([ lineFeature ]);
    map.zoomToExtent(lineLayer.getDataExtent())
    return lineLayer
}


