package campus.tech.kakao.map.data

import campus.tech.kakao.map.data.datasource.Local.Entity.FavoriteEntity
import campus.tech.kakao.map.data.datasource.Local.Entity.PlaceEntity
import campus.tech.kakao.map.data.datasource.Local.Entity.toVO
import campus.tech.kakao.map.data.datasource.Local.DB.RoomDB
import campus.tech.kakao.map.data.datasource.Remote.RemoteService
import campus.tech.kakao.map.data.datasource.Remote.Response.toVO
import campus.tech.kakao.map.data.datasource.Remote.RetrofitService
import campus.tech.kakao.map.domain.PlaceRepository
import campus.tech.kakao.map.domain.vo.Place
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PlaceRepositoryImpl @Inject constructor(
    private val roomDB: RoomDB,
    private val retrofitService: RetrofitService,
    private val httpUrlConnect: RemoteService
) : PlaceRepository {

    override suspend fun getCurrentFavorite() : List<Place> = withContext(Dispatchers.IO){
        roomDB.favoriteDao().getCurrentFavorite().map { it.toVO() }
    }

    override suspend fun getSimilarPlacesByName(name: String) : List<Place> = withContext(Dispatchers.IO){
        roomDB.placeDao().getSimilarPlacesByName(name).map { it.toVO() }
    }

    override suspend fun getPlaceById(id : Int): Place? = withContext(Dispatchers.IO){
        roomDB.placeDao().getPlaceById(id)?.toVO()
    }

    override suspend fun getFavoriteById(id: Int) : Place? = withContext(Dispatchers.IO){
        roomDB.favoriteDao().getFavoriteById(id)?.toVO()
    }

    override suspend fun addFavorite(place : Place) : List<Place> = withContext(Dispatchers.IO){
        roomDB.favoriteDao().addFavorite(place.toFavoriteEntity())
        getCurrentFavorite()
    }

    override suspend fun deleteFavorite(id : Int) : List<Place> = withContext(Dispatchers.IO){
        roomDB.favoriteDao().deleteFavorite(id)
        getCurrentFavorite()
    }

    override suspend fun searchPlaceRemote(name: String) : List<Place> = withContext(Dispatchers.IO){
        getPlaceByNameRemote(name)
    }

    override fun getPlaceByNameHTTP(name : String) : List<Place> =
        httpUrlConnect.getPlaceResponse(name).map{ it.toVO()}


    private suspend fun getPlaceByNameRemote(name: String): List<Place> =
        withContext(Dispatchers.IO) {
            val pageCount = getPageCount(name)
            var placeList: MutableList<Place> = mutableListOf<Place>()

            for (page in 1..pageCount) {
                val req = retrofitService.requestProducts(query = name, page = page).execute()

                when (req.code()) {
                    200 -> {
                        req.body()?.documents?.forEach {
                            placeList.add(
                                it.toVO()
                            )
                        }
                    }
                    else -> {}
                }

            }
            placeList
        }

    private fun getPageCount(name: String): Int {
        val req = retrofitService.requestProducts(query = name).execute()

        when (req.code()) {
            200 -> return minOf(
                MAX_PAGE, req.body()?.meta?.pageableCount ?: 1
            )

            else -> return 0
        }
    }

    private fun Place.toPlaceEntity() = PlaceEntity(
        this.id,
        this.name,
        this.address,
        this.category,
        this.x,
        this.y
    )

    private fun Place.toFavoriteEntity() = FavoriteEntity(
        this.id,
        this.name,
        this.address,
        this.category,
        this.x,
        this.y
    )
    companion object {
        const val MAX_PAGE = 2
    }
}