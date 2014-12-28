/**
 * Created by basti on 6/15/14.
 */


function loadJsonDataAndCallback(url, callbacks, spinner){

    $.getJSON(url,
        function (data) {

            for(var x = 0; x < spinner['length']; x++){
                if(spinner[x] !== "undefined" && spinner[x].style !== "undefined")
                    spinner[x].style.display = "none"
            }

            $.each( callbacks, function( key, val ) {
                callbacks[key](data)
            });
        });
}

function loadTracks(date, callbacks, spinner){
    loadJsonDataAndCallback('/api/getDayTourInformation/' + date, callbacks, spinner)
}

function loadTripSummary(trip, callbacks, spinner){
    loadJsonDataAndCallback('/api/getTripInformation/' + trip, callbacks, spinner)
}

