/**
 * Created by basti on 6/15/14.
 */


function initMetadata(data){
    var index = 0
    for(i in data){
        var track = data[i]
        var metadata = track["metadata"];
        $('#accordion').append($(
           getMetaDataTabHtml(track['name'], metadata, i)
        ));
        index = i
    }

    $('#accordion').append($(
        getMetaDataTabHtml("Sum", sumMetaData(data), index+1)
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


function sumMetaData(data){

    var sum = Array()

    for(i in data){
        var meta = data[i]["metadata"];

        for(key in meta){

            if(meta[key] == null) continue

            if(key in sum){
                sum[key] = sum[key] + meta[key]
            }else{
                sum[key] = meta[key]
            }
        }
    }
    return sum
}

function getMetaDataAsHtml(metadata){
    var table = '<table class="table table-condensed table-hover"><tr><th>Key</th><th>Value</th></tr>'

    for(x in metadata){
        if(metadata[x] == null) continue
        table += '<tr><td>'+ x + '</td><td>' + metadata[x] + '</td></tr>'
    }

    table += '</table>'
    return table
}