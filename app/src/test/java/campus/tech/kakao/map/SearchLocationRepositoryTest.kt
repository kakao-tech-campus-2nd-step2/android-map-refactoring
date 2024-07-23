package campus.tech.kakao.map

import campus.tech.kakao.map.data.local_search.LocalSearchDocument
import campus.tech.kakao.map.data.local_search.LocalSearchMeta
import campus.tech.kakao.map.data.local_search.LocalSearchResponse
import campus.tech.kakao.map.data.local_search.LocalSearchSameName
import campus.tech.kakao.map.data.local_search.LocalSearchService
import campus.tech.kakao.map.data.local_search.SearchLocationRepositoryImpl
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.Assert.*
import retrofit2.Response

class SearchLocationRepositoryTest {
    // LocalSearchService Mock 객체
    private val mockLocalSearchService = mockk<LocalSearchService>()

    // SearchLocationRepository 객체
    private val repository = SearchLocationRepositoryImpl(mockLocalSearchService)

    @Test
    fun testSearchLocation() = runBlocking {
        // given
        val testDocuments = listOf(
            LocalSearchDocument(
                "address_name1", "category_group_code1", "category_group_name1",
                "category_name1", "distance1", "id1", "phone1", "place_name1",
                "place_url1", "road_address_name1", "1.0", "1.0"
            ),
            LocalSearchDocument(
                "address_name2", "category_group_code2", "category_group_name2",
                "category_name2", "distance2", "id2", "phone2", "place_name2",
                "place_url2", "road_address_name2", "2.0", "2.0"
            )
        )
        val testMeta = LocalSearchMeta(
            true, 1, LocalSearchSameName(
                "keyword", listOf("region"), "selected_region"
            ), 2
        )
        val mockResponse = Response.success(LocalSearchResponse(testDocuments, testMeta))
        coEvery { mockLocalSearchService.requestLocalSearch(query = any()) } returns mockResponse

        // when
        val result = repository.searchLocation("testCategory")

        // then
        assertEquals(2, result.size)
        assertEquals("place_name1", result[0].name)
        assertEquals("address_name1", result[0].address)
        assertEquals("category_group_name1", result[0].category)
        assertEquals(1.0, result[0].latitude, 1e-5)
        assertEquals(1.0, result[0].longitude, 1e-5)
        assertEquals("place_name2", result[1].name)
        assertEquals("address_name2", result[1].address)
        assertEquals("category_group_name2", result[1].category)
        assertEquals(2.0, result[1].latitude, 1e-5)
        assertEquals(2.0, result[1].longitude, 1e-5)
    }
}