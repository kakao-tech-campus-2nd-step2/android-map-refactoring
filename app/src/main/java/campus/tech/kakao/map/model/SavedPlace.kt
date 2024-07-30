package campus.tech.kakao.map.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import campus.tech.kakao.map.db.PlaceContract

@Entity(tableName = PlaceContract.SavedPlaceEntry.TABLE_NAME)
data class SavedPlace(
    @PrimaryKey val name: String
)