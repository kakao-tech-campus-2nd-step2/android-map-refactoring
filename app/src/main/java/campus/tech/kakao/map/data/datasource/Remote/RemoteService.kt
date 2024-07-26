package campus.tech.kakao.map.data.datasource.Remote

import campus.tech.kakao.map.data.datasource.Remote.Response.Document

interface RemoteService {
    fun getPlaceResponse(query: String): List<Document>
}