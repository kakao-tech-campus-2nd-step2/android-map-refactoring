package campus.tech.kakao.map.data.model

import androidx.room.Entity
import com.google.gson.annotations.SerializedName

data class MetaEntity(
    @SerializedName("total_count")
    val totalCount: Int,
    @SerializedName("pageable_count")
    val pageableCount: Int,
    @SerializedName("is_end")
    val isEnd: Boolean,
)