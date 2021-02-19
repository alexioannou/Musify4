package functional

import functional.pages.AddAlbumPage
import functional.pages.EditAlbumPage
import functional.pages.ListAlbumsPage
import geb.spock.GebSpec
import grails.testing.mixin.integration.Integration
import org.junit.Assert

@Integration
class Musify4FunctionalSpec extends GebSpec{

    def musify4TestToolkitService


    def sampleTitle = "sampleTitle"
    def sampleArtist = "sampleArtist"
    def sampleGenre1 = [id:12345, name:"GenOne"]
    def sampleGenre2 = [id:23456, name:"GenTwo"]
    def sampleGenre3 = [id:34567, name:"GenThree"]
    def sampleGenre4 = [id:45678, name:"GenFour"]
    def sampleGenre5 = [id:56789, name:"GenFive"]
    def sampleGenre6 = [id:67890, name:"GenSix"]
    def sampleGenreIds1 = [1, 2, 3]
    def sampleAlbum1 = [id:1111111, title:"sampleTitle1", artist:"sampleArtist1", genres:[sampleGenre1, sampleGenre2]]
    def sampleAlbum2 = [id:2222222, title:"sampleTitle2", artist:"sampleArtist2", genres:[sampleGenre3, sampleGenre4, sampleGenre5]]
    def sampleAlbum3 = [id:3333333, title:"sampletitle3", artist:"sampleArtist3", genres:[sampleGenre6]]

    def setup() {
    }

    def cleanup() {
    }

    /**
     * Go to the listAlbums View and make sure all Albums are showing properly
     */
    def testViewAlbums() {
        given:
            musify4TestToolkitService.persistAlbumAlongWithGenres(sampleAlbum1)
            musify4TestToolkitService.persistAlbumAlongWithGenres(sampleAlbum2)
        when:
            ListAlbumsPage listAlbumsPage = to(ListAlbumsPage)
            Thread.sleep(5000)
        then:
            assert listAlbumsPage.containsAlbum(sampleAlbum1)
            assert listAlbumsPage.containsAlbum(sampleAlbum2)
        cleanup:
            musify4TestToolkitService.cleanupAlbumWithGenres(sampleAlbum1)
            musify4TestToolkitService.cleanupAlbumWithGenres(sampleAlbum2)
    }

    /**
     * Edit an Album in the edit View and make sure the changes were reflected in the Database
     */
    def testEditAlbum() {
        given:
            def album = musify4TestToolkitService.persistAlbumAlongWithGenres(sampleAlbum1)
            musify4TestToolkitService.persistGenre(sampleGenre6)
        when:
            ListAlbumsPage listAlbumsPage = to(ListAlbumsPage)
            listAlbumsPage.getRowOf(sampleAlbum1).editCell.click()
        then:
            assert isAt(EditAlbumPage)
        when:
            EditAlbumPage editAlbumPage = at(EditAlbumPage)
            editAlbumPage.titleField.value(sampleTitle)
            editAlbumPage.artistField.value(sampleArtist)
            editAlbumPage.selectGenre(sampleGenre6)
            Thread.sleep(5000)
            editAlbumPage.confirmButton.click()
            album = musify4TestToolkitService.fetchAlbumAlongWithGenreIds(album.id)
        then:
            Assert.assertEquals(sampleTitle, album.title)
            Assert.assertEquals(sampleArtist, album.artist)
            assert album.genres.contains(sampleGenre6.id)
        cleanup:
            musify4TestToolkitService.cleanupAlbumWithGenres(sampleAlbum1)
            musify4TestToolkitService.cleanupGenre(sampleGenre6)
    }
    
    /**
     * Go to listAlbums View and delete an album
     */
    def testDeleteAlbum() {
        given:
            def album = musify4TestToolkitService.persistAlbumAlongWithGenres(sampleAlbum1)
        when:
            ListAlbumsPage listAlbumsPage = to(ListAlbumsPage)
            Thread.sleep(5000)
            listAlbumsPage.getRowOf(sampleAlbum1).deleteCell.click()
        then:
            assert musify4TestToolkitService.fetchAlbum(album.id) == null
        cleanup:
            musify4TestToolkitService.cleanupGenre(sampleGenre1)
            musify4TestToolkitService.cleanupGenre(sampleGenre2)
    }
    
    /**
     * Create an album using the add View
     */
    def testCreateAlbum() {
        given:
            musify4TestToolkitService.persistGenre(sampleGenre1)
        when:
            ListAlbumsPage listAlbumsPage = to(ListAlbumsPage)
            listAlbumsPage.newButton.click()
        then:
            assert isAt(AddAlbumPage) //Unnecessary as at() below checks for that as well, but this is clearer
        when:
            AddAlbumPage addAlbumPage = at(AddAlbumPage)
            addAlbumPage.titleField.value(sampleTitle)
            addAlbumPage.artistField.value(sampleArtist)
            addAlbumPage.selectGenre(sampleGenre1)
            Thread.sleep(5000)
            addAlbumPage.createButton.click()
        then:
            assert isAt(ListAlbumsPage)
        when:
            def album = musify4TestToolkitService.searchAlbumAlongWithGenres(sampleTitle, sampleArtist, sampleGenre1)
        then:
            Assert.assertNotEquals(null, album)
        cleanup:

            musify4TestToolkitService.cleanupAlbumWithGenres(album)
    }
    
    /**
     * Do a Search and make sure only the correct albums are visible,
     * then clears the results and makes sure all albums are visible
     * @return
     */
    def testSearchAlbums() {
        given:
            musify4TestToolkitService.persistAlbumAlongWithGenres(sampleAlbum1)
            musify4TestToolkitService.persistAlbumAlongWithGenres(sampleAlbum2)
        when:
            ListAlbumsPage listAlbumsPage = to(ListAlbumsPage)
            Thread.sleep(5000)
            listAlbumsPage.titleField.value(sampleAlbum1.title)
            listAlbumsPage.artistField.value(sampleAlbum1.artist)
            listAlbumsPage.genreField.value(sampleAlbum1.genres.first().name)
            listAlbumsPage.searchButton.click()
            Thread.sleep(5000)
        then:
            assert listAlbumsPage.containsAlbum(sampleAlbum1)
            assert !listAlbumsPage.containsAlbum(sampleAlbum2)
        when:
            listAlbumsPage.clearButton.click()
            Thread.sleep(5000)
        then:
            assert listAlbumsPage.containsAlbum(sampleAlbum1)
            assert listAlbumsPage.containsAlbum(sampleAlbum2)
        cleanup:
            musify4TestToolkitService.cleanupAlbumWithGenres(sampleAlbum1)
            musify4TestToolkitService.cleanupAlbumWithGenres(sampleAlbum2)
    }
}