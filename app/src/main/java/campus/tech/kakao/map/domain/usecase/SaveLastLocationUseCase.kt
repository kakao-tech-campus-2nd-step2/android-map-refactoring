package campus.tech.kakao.map.domain.usecase

import campus.tech.kakao.map.domain.repository.LastLocationRepository

class SaveLastLocationUseCase(
    private val lastLocationRepository: LastLocationRepository
) {
    suspend operator fun invoke(latitude: Double, longitude: Double) =
        lastLocationRepository.saveLocation(latitude, longitude)
}