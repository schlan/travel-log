/**
 * Created by basti on 6/15/14.
 */


function loadTracks(date, callbacks){
    $.getJSON("/json/getTrack/" + date,
        function (data) {
            for(i in callbacks){
                callbacks[i](data);
            }
        });
}

