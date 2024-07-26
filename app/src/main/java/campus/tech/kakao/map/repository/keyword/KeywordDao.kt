package campus.tech.kakao.map.repository.keyword

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase

class KeywordDao(context: Context) {

    private val dbHelper = KeywordDbHelper(context)
    private val db: SQLiteDatabase = dbHelper.writableDatabase

    fun update(keyword: String) {
        ContentValues().apply {
            put(KeywordContract.RECENT_KEYWORD, keyword)
            db.insert(KeywordContract.TABLE_NAME, null, this)
        }
    }

    fun read(): List<String> {
        val keywords = mutableListOf<String>()
        val projection = arrayOf(KeywordContract.RECENT_KEYWORD)
        val cursor = db.query(
            KeywordContract.TABLE_NAME,
            projection,
            null,
            null,
            null,
            null,
            null
        )

        with(cursor) {
            while (moveToNext()) {
                keywords.add(getString(getColumnIndexOrThrow(KeywordContract.RECENT_KEYWORD)))
            }
        }
        return keywords
    }

    fun delete(keyword: String) {
        db.delete(
            KeywordContract.TABLE_NAME,
            "${KeywordContract.RECENT_KEYWORD} = ?",
            arrayOf(keyword)
        )
    }

    fun close() {
        db.close()
    }
}
