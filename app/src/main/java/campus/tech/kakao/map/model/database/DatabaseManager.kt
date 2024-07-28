package campus.tech.kakao.map.model.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import campus.tech.kakao.map.model.data.SavedSearch

class DatabaseManager(context: Context) {

    private val dbHelper = DatabaseHelper(context) //SQLiteOpenHelper객체 생성

    private val db: SQLiteDatabase = dbHelper.writableDatabase //open하고 실제 데이터베이스..?
    //예외 처리 try-catch로 처리하기

    //SavedSearch 테이블 삽입
    fun insertSavedsearch(id : Int, name: String) {
        val values = ContentValues().apply {
            put("id", id)
            put("name", name)
        }
        db.insert("SavedSearch", null, values)
    }

    //savedSearch 테이블에서 모두 다 가져오기
    fun getSavedSearches(): List<SavedSearch> {
        val result = mutableListOf<SavedSearch>()
        val cursor: Cursor = db.query(
            "SavedSearch",  //검색할 테이블
            arrayOf("id", "name"),   //반환할 열의 배열
            null, //필터링 where
            null, //위에 ?에 들어갈 매개변수
            null,
            null,
            null
        )

        with(cursor) {
            while (moveToNext()) {  //moveToNext() 메서드는 다음 레코드로 이동하고, 이동할 수 있으면 true를 반환
                val savedSearch = SavedSearch(
                    getInt(getColumnIndexOrThrow("id")),
                    getString(getColumnIndexOrThrow("name")),
                )
                result.add(savedSearch)
            }
            close()
        }

        return result
    }

    //id 값으로 제거
    fun deleteSavedSearch(id: Int) {
        db.delete("SavedSearch", "id = ?", arrayOf(id.toString()))
    }

    fun dropTable(){
        dbHelper.dropTable(db)
    }

    fun createTable() {
        dbHelper.onCreate(db)
    }

}