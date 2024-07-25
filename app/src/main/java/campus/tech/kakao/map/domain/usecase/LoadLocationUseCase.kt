package campus.tech.kakao.map.domain.usecase

import campus.tech.kakao.map.domain.model.Location
import campus.tech.kakao.map.data.repository.LocationRepository
import javax.inject.Inject

class LoadLocationUseCase @Inject constructor(
    private val locationRepository: LocationRepository,
) {
    suspend operator fun invoke(): Location {
        return locationRepository.loadLocation()
    }
}
