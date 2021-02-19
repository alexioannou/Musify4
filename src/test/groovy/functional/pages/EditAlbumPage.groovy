package functional.pages

import geb.Page

class EditAlbumPage extends Page{

    static url = "http://localhost:8080/musify4/edit"
    static at = { title.contains("Edit Album") }

    static content = {
        titleField(wait:true) {$("#titleField")}
        artistField(wait:true) {$("#artistField")}
        genres(wait:true) {$("#genres")}
        confirmButton(wait:true) {$("#confirmButton")}
        cancelButton(wait:true) {$("#cancelButton")}
    }

    def selectGenre(def genre) {
        genres = [genre.id]
    }
}
