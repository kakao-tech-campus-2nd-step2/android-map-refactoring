package campus.tech.kakao.map.di

import android.content.Context
import campus.tech.kakao.map.utility.BottomSheetHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.scopes.ActivityScoped

@Module
@InstallIn(ActivityComponent::class)
object BottomSheetModule {
    @Provides
    @ActivityScoped
    fun singletonBottomSheet(@ActivityContext context: Context): BottomSheetHelper {
        return BottomSheetHelper(context)
    }
}