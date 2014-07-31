function initImages(data){
    initImagesGeneral(data["images"])
}

function initImagesForOverview(data){
    initImagesGeneral(data["newestImages"])
}


$(document).ready(function ($) {
    $(document).delegate('*[data-toggle="lightbox"]', 'click', function(event) {
        event.preventDefault();
        $(this).ekkoLightbox();
    });
})

function initImagesGeneral(images) {

    var markup = "<div class='tour-gallery'>"
    for (x in images) {

        var img = images[x]
        var path = "/api/images/getImage/" + img["path"]

        markup += "<div class='col-lg-offset-1 col-lg-10 col-md-4 col-sm-4 col-xs-6' style='padding-left: 5px;padding-right: 5px;'>"
        markup += "<div class='thumbnail'>"

        markup += "<a data-title='" + img["name"] + "' href='"+path+"' data-parent='.tour-gallery' data-toggle='lightbox' data-gallery='multiimages' data-type='image' >"
        markup += "<img class='img-responsive' src='" + path + "'/>"
        markup += "</a>"

        markup += "<span class='caption'>"
        markup += "<span style='float:left;padding-left: 5px;'><b>" + img["name"] + "</b></span>"

        var imgLat = img["location"]["latitude"]
        var imgLon = img["location"]["longitude"]

        if(imgLat != -1 && imgLon != -1){
            markup += "<span style='float:right;padding-right: 5px;'><a class='glyphicon glyphicon-map-marker' href='#map' onclick='zoomToPosition(" + imgLat + "," + imgLon + ")''></a></span>"
        }
        markup += "</span></div></div>"
    }

    markup += "</div>"


    $(".tourimg").append(markup)


}



