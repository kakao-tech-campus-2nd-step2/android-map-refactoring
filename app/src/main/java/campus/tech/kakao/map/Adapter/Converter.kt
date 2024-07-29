package campus.tech.kakao.map.Adapter

import androidx.databinding.InverseMethod

object Converter {

    @InverseMethod("stringToSearchQuery")
    @JvmStatic
    fun searchQueryToString(value: String): String {
        return value
    }

    @JvmStatic
    fun stringToSearchQuery(value: String): String {
        return value
    }
}
