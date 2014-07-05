/**
 * Created by basti on 6/15/14.
 */


function loadTracks(date, callbacks){
    $.getJSON("/api/getDayTourInformation/" + date,
        function (data) {
            console.log(data)
            $.each( callbacks, function( key, val ) {
                callbacks[key](data);
            });
        });
}

