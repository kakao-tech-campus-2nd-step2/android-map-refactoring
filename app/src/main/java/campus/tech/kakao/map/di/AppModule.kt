package campus.tech.kakao.map.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import campus.tech.kakao.map.data.model.Location
import campus.tech.kakao.map.data.repository.LocationSerializer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object AppModule {
    private val Context.dataStore: DataStore<Location> by dataStore(
        fileName = "location_data.pb",
        serializer = LocationSerializer,
    )

    @Provides
    @ViewModelScoped
    fun provideDataStore(
        @ApplicationContext context: Context,
    ): DataStore<Location> {
        return context.dataStore
    }
}
