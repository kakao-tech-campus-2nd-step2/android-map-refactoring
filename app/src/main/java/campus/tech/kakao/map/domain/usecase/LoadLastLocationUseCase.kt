package campus.tech.kakao.map.domain.usecase

import campus.tech.kakao.map.domain.repository.LastLocationRepository

class LoadLastLocationUseCase(
    private val lastLocationRepository: LastLocationRepository
) {
    operator fun invoke() =
        lastLocationRepository.loadLocation()
}