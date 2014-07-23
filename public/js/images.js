function initImages(data) {

    var images = data["images"]

    var markup = ""

    for (x in images) {

        var img = images[x]
        var path = "/api/images/getImage/" + img["path"]


        markup += "<div class='col-md-offset-2 col-md-8 col-sm-offset-2 col-sm-8'>"
        markup += "<div class='thumbnail'>"
        markup += "<a title='" + img["name"] + "' href='" + path + "'>"
        markup += "<img class='img-thumbnail' src='" + path + "'/>"
        markup += "</a>"
        markup += "<span class='caption'>"

        markup += "<span><b>" + img["name"] + "</b></span>"

        markup += "</span></div></div>"
//
//        $("#images").append(
//                "<div class='col-md-offset-1 col-md-10 col-sm-offset-1 col-sm-10 '>" +
//                    "<a title='" + img["name"] + "' href='" + path + "'>" +
//                        "<img class='img-thumbnail' src='" + path + "'/>" +
//                    "</a>" +
//                "</div>"
//        )
    }


    $("#images").append(markup)

}



