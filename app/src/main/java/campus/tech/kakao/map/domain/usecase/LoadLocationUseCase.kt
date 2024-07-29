package campus.tech.kakao.map.domain.usecase

import campus.tech.kakao.map.domain.model.LocationDomain
import campus.tech.kakao.map.domain.repository.LocationRepository
import javax.inject.Inject

class LoadLocationUseCase @Inject constructor(
    private val locationRepository: LocationRepository,
) {
    suspend operator fun invoke(): LocationDomain {
        return locationRepository.loadLocation()
    }
}
