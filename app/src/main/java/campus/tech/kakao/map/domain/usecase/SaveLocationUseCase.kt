package campus.tech.kakao.map.domain.usecase

import campus.tech.kakao.map.domain.model.LocationDomain
import campus.tech.kakao.map.data.repository.LocationRepository
import javax.inject.Inject

class SaveLocationUseCase @Inject constructor(
    private val locationRepository: LocationRepository,
) {
    suspend operator fun invoke(location: LocationDomain) {
        locationRepository.saveLocation(location)
    }
}
