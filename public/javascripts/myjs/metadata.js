/**
 * Created by basti on 6/15/14.
 */


function initMetadata(data){
    var index = 0
    for(i in data["tracks"]){
        var track = data["tracks"][i]
        var metadata = track["metadata"];
        $('#accordion').append($(
           getMetaDataTabHtml(track['name'], metadata, i)
        ));
        index = i
    }

    $('#accordion').append($(
        getMetaDataTabHtml("Sum", data["summarizedMetadata"], index+1)
    ));
}

function getMetaDataTabHtml(name, metadata, i){
    return '<div class="panel panel-default">' +
    '<div class="panel-heading">' +
    '<h4 class="panel-title">' +
    '<a data-toggle="collapse" data-parent="#accordion" href="#collapse' + i + '">' +
    name +
    '</a>'+
    '</h4>' +
    '</div>'+
    '<div id="collapse' + i + '" class="panel-collapse collapse">' +
    '<div class="panel-body">' +
    getMetaDataAsHtml(metadata) +
    '</div>' +
    '</div>' +
    '</div>'
}

function getMetaDataAsHtml(metadata){
    var table = '<table class="table table-condensed table-hover"><tr><th>Key</th><th style="text-align:right;">Value</th></tr>'

    for(x in metadata){
        if(metadata[x] == null) continue
        table += '<tr><td>'+ x + '</td><td style="text-align:right;">' + metadata[x] + '</td></tr>'
    }

    table += '</table>'
    return table
}