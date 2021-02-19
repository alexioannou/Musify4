package functional.pages

import geb.Page

class AddAlbumPage extends Page{
	static url = "http://localhost:8080/musify4/add"
	static at = { title.contains("Add Album")}
	
	static content = {
		titleField(wait: true) {$("#titleField")}
		artistField(wait: true) {$("#artistField")}
		genres(wait: true) {$("#genres")}
		createButton(wait: true) {$("#createButton")}
		cancelButton(wait: true) {$("#cancelButton")}
	}
	
	def selectGenre(def genre) {
		genres = [genre.id]
	}
}
