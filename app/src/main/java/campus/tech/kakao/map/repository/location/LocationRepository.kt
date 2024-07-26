package campus.tech.kakao.map.repository.location

interface LocationRepository {
    fun search(keyword: String)
}
