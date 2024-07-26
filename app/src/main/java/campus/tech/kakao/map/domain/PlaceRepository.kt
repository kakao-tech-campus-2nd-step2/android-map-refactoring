package campus.tech.kakao.map.domain

import campus.tech.kakao.map.domain.VO.Place

interface PlaceRepository {
    suspend fun getCurrentFavorite() : List<Place>
    suspend fun getSimilarPlacesByName(name: String) : List<Place>
    suspend fun addFavorite(place : Place) : List<Place>
    suspend fun getPlaceById(id : Int): Place?
    suspend fun getFavoriteById(id: Int) : Place?
    suspend fun deleteFavorite(id : Int) : List<Place>
    suspend fun searchPlaceRemote(name : String) : List<Place>
    fun getPlaceByNameHTTP(name : String) : List<Place>


}