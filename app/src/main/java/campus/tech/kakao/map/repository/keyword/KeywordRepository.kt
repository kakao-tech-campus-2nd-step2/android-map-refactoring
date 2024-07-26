package campus.tech.kakao.map.repository.keyword

interface KeywordRepository {
    fun update(keyword: String)
    fun read(): List<String>
    fun delete(keyword: String)
    fun close()
}
