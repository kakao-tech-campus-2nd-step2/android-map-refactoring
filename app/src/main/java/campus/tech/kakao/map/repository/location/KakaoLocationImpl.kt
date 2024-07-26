package campus.tech.kakao.map.repository.location

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import campus.tech.kakao.map.model.kakaolocal.LocalUiModel
import campus.tech.kakao.map.model.kakaolocal.KakaoLocalResponse
import campus.tech.kakao.map.network.KakaoLocalRetrofitBuilder
import campus.tech.kakao.map.network.KakaoLocalService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class KakaoLocationImpl(private val kakaoLocalService: KakaoLocalService) : LocationRepository {
    private val _items = MutableLiveData<List<LocalUiModel>>()
    val items: LiveData<List<LocalUiModel>>
        get() = _items

    override fun search(keyword: String) {
        val call = kakaoLocalService.searchKeyword(query = keyword)
        call.enqueue(object : Callback<KakaoLocalResponse> {
            override fun onResponse(
                call: Call<KakaoLocalResponse>,
                response: Response<KakaoLocalResponse>
            ) {
                if (response.isSuccessful) {
                    val body = response.body()
                    _items.postValue(body?.toUiModel())
                }
            }

            override fun onFailure(call: Call<KakaoLocalResponse>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }

    fun KakaoLocalResponse.toUiModel(): List<LocalUiModel> {
        return documents.map {
            LocalUiModel(
                it.placeName,
                it.addressName,
                it.categoryGroupName,
                it.y.toDouble(),
                it.x.toDouble()
            )
        }
    }

    companion object {
        fun getInstance(): KakaoLocationImpl {
            val retrofit = KakaoLocalRetrofitBuilder.instance
            val service = retrofit.create(KakaoLocalService::class.java)

            return KakaoLocationImpl(service)
        }
    }
}
