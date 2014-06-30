function initImages(data) {

    var images = data["images"]
    for (x in images) {

        var img = images[x]
        var path = "/api/images/getImage/" + img["path"]

        $("#images").append(
                "<div class='col-md-offset-1 col-md-10 col-sm-offset-1 col-sm-10 '>" +
                    "<a title='" + img["name"] + "' href='" + path + "'>" +
                        "<img class='img-thumbnail' src='" + path + "'/>" +
                    "</a>" +
                "</div>"
        )
    }

}



