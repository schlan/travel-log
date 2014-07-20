/**
 * Created by basti on 6/15/14.
 */


function loadJsonDataAndCallback(url, callbacks){
    $.getJSON(url,
        function (data) {
            console.log(data)
            $.each( callbacks, function( key, val ) {
                callbacks[key](data);
            });
        });
}

function loadTracks(date, callbacks){
    loadJsonDataAndCallback('/api/getDayTourInformation/' + date, callbacks)
}

function loadTripSummary(trip, callbacks){
    loadJsonDataAndCallback('/api/getTripInformation/' + trip, callbacks)
}

