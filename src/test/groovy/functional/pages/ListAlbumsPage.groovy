package functional.pages

import geb.Module
import geb.Page

class ListAlbumsPage extends Page {

    static url = "http://localhost:8080/musify4/listAlbums"
    static at = { title.contains("Album List") }

    static content = {
        
        
        
        
        tableBody(wait: true) { $("#myTable tbody").children().moduleList(AlbumListRow) }
        
        
        form(wait: true) { $("#myForm") }
        titleField(wait: true) { $("#titleField") }
        artistField(wait: true) { $("#artistField") }
        genreField(wait: true) { $("#genreField") }
        searchButton(wait: true) { $("#searchButton") }
        clearButton(wait: true) { $("#clearButton") }
        newButton(wait: true) { $("#newButton") }
    }

    boolean containsAlbum(def album) {
        boolean foundTitle, foundArtist, foundGenres
        foundTitle = foundArtist = foundGenres = true
        if (!tableBody.find { it.titleCell.text() == album.title }) {
            foundTitle = false
        }
        if (!tableBody.find { it.artistCell.text() == album.artist }) {
            foundArtist = false
        }
        album.genres.each { gen ->
            if (!tableBody.find { it.genresCell.text().contains(gen.name) }) {
                foundGenres = false
            }
        }
        return foundTitle && foundArtist && foundGenres
    }

    def getRowOf(def album) {
        return tableBody.find{row ->
            row.titleCell.text() == album.title && row.artistCell.text() == album.artist && row.genresCell.text().contains(album.genres.first().name)
        }
    }
}

class AlbumListRow extends Module {
    static content = {
        artistCell(wait:true) {$("#artistCell")}
        titleCell(wait:true) {$("#titleCell")}
        genresCell(wait:true) {$("#genresCell")}
        editCell(wait:true) {$("#editCell")}
        deleteCell(wait:true) {$("#deleteCell")}
    }
}
