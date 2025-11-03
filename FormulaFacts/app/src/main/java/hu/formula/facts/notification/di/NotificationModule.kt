package hu.formula.facts.notification.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import hu.formula.facts.notification.scheduler.AlarmScheduler
import hu.formula.facts.notification.scheduler.NotificationAlarmScheduler
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NotificationModule {
    @Singleton
    @Provides
    fun provideScheduler(
        @ApplicationContext app: Context
    ): AlarmScheduler = NotificationAlarmScheduler(app)
}
