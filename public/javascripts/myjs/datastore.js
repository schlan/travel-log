/**
 * Created by basti on 6/15/14.
 */


function loadTracks(date, callbacks){
    $.getJSON("/api/getDayTourInformation/" + date,
        function (data) {
            console.log(data)
            for(i in callbacks){
                callbacks[i](data);
            }
        });
}

