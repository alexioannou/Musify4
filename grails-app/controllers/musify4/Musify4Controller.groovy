package musify4

import grails.converters.JSON


class Musify4Controller {

    def musify4Service

    def index() {
        redirect (action: "listAlbums")
    }

    def add() {
        def myGenres = musify4Service.fetchAllGenres()
        [genres : myGenres]
    }

    def create() {
        musify4Service.createAlbum(params.title, params.artist, params.genres)
        redirect (action: "listAlbums")
    }

    def edit() {
        def allGenres = musify4Service.fetchAllGenres()
        def myAlbum = musify4Service.fetchSingleAlbum(params.id.toInteger())
        [album: myAlbum, genres : allGenres]
    }

    def update() {
        musify4Service.updateAlbum(params.id.toInteger(), params.title, params.artist, params.genres)
        redirect (action: "listAlbums")
    }

    def listAlbums() {
        def myAlbums = musify4Service.listAlbums()
        [albums : myAlbums]
    }

    def search() {
        def myJson
        if(params.title || params.artist || params.genre)
        {
            myJson = musify4Service.searchAlbumsAlongWithGenres(params.title, params.artist, params.genre)
        }
        else
            myJson = musify4Service.listAlbums()
        response.setContentType("application/json")
        render myJson as JSON
    }

    def delete() {
        musify4Service.deleteAlbum(params.id.toInteger())
        redirect (action: "listAlbums")
    }

    def test() {
        render "*gesture* This is not the Action you are looking for"
    }
}
