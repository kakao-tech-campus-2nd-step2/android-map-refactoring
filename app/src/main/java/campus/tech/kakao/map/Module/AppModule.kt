package campus.tech.kakao.map.Module

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import javax.inject.Singleton;
import android.content.Context;
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.net.PlacesClient;
import dagger.hilt.android.qualifiers.ApplicationContext

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun providePlacesClient(@ApplicationContext context: Context): PlacesClient {
        Places.initialize(context, "AIzaSyCUncz7v8nwT3m5OHasVJTep1e1549yAKM")
        return Places.createClient(context)
    }
}
