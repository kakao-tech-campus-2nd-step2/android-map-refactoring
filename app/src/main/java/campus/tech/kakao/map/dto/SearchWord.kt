package campus.tech.kakao.map.dto

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = SearchWordContract.TABLE_NAME)
data class SearchWord(
	@PrimaryKey(autoGenerate = true) val id: Int,
	@ColumnInfo(name = SearchWordContract.COLUMN_NAME_NAME)val name: String,
	@ColumnInfo(name = SearchWordContract.COLUMN_NAME_ADDRESS)val address: String,
	@ColumnInfo(name = SearchWordContract.COLUMN_NAME_TYPE)val type: String){
	constructor(name: String, address: String, type: String) : this(0, name, address, type)
}
