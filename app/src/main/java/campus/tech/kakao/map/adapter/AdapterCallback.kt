package campus.tech.kakao.map.adapter

import campus.tech.kakao.map.dto.Document
import campus.tech.kakao.map.dto.SearchWord

interface AdapterCallback {
	fun onPlaceClicked(document: Document)
	fun onWordDeleted(searchWord: SearchWord)
	fun onWordSearched(searchWord: SearchWord)
}