package campus.tech.kakao.map.model.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

//데이터베이스 생성 및 업그레이드를 관리
class DatabaseHelper(context : Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, 1) {

    //테이블 생성
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(TABLE_CREATE) //Place저장 테이블
        db.execSQL(SAVED_SEARCH_CREATE) //최근 검색 테이블
    }

    //테이블 업그레이드
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        db.execSQL("DROP TABLE IF EXISTS $SAVED_SEARCH_TABLE_NAME")
        onCreate(db)
    }

    //테이블 삭제
    fun dropTable(db: SQLiteDatabase) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        db.execSQL("DROP TABLE IF EXISTS $SAVED_SEARCH_TABLE_NAME")
    }

    companion object {  //companion object는 거의 클래스 최하단에 위치하기 권장, companion object (동반객체)
        private const val DATABASE_NAME = "places.db"
        private const val TABLE_NAME = "places"
        private const val SAVED_SEARCH_TABLE_NAME = "SavedSearch"
        private const val COLUMN_ID = "id"
        private const val COLUMN_NAME = "name"
        private const val COLUMN_ADDRESS = "address"
        private const val COLUMN_KIND = "kind"

        //Place 테이블 생성 쿼리
        private const val TABLE_CREATE =
            "CREATE TABLE IF NOT EXISTS $TABLE_NAME ($COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "$COLUMN_NAME TEXT, $COLUMN_ADDRESS TEXT, $COLUMN_KIND TEXT)"

        //SavedSearch 테이블 생성 쿼리
        private const val SAVED_SEARCH_CREATE =
            "CREATE TABLE IF NOT EXISTS $SAVED_SEARCH_TABLE_NAME ($COLUMN_ID INTEGER PRIMARY KEY, " +
                    "$COLUMN_NAME TEXT)"
    }

}


