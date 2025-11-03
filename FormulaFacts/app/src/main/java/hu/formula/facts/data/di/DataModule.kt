package hu.formula.facts.data.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import hu.formula.facts.data.datasource.FormulaOneRepository
import hu.formula.facts.data.datasource.FormulaOneRepositoryImpl
import hu.formula.facts.data.room.FormulaOneDatabase
import hu.formula.facts.data.room.repository.constructor.ConstructorLocalRepository
import hu.formula.facts.data.room.repository.constructor.ConstructorLocalRepositoryImpl
import hu.formula.facts.data.room.repository.driver.DriverLocalRepository
import hu.formula.facts.data.room.repository.driver.DriverLocalRepositoryImpl
import hu.formula.facts.data.room.repository.grandPrix.GrandPrixLocalRepository
import hu.formula.facts.data.room.repository.grandPrix.GrandPrixLocalRepositoryImpl
import hu.formula.facts.data.room.repository.season.SeasonLocalRepository
import hu.formula.facts.data.room.repository.season.SeasonLocalRepositoryImpl
import hu.formula.facts.data.settings.Settings
import hu.formula.facts.data.settings.SettingsImpl
import hu.formula.facts.network.f1.FormulaService
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {
    @Singleton
    @Provides
    fun provideDataStore(
        @ApplicationContext app: Context
    ): Settings = SettingsImpl(app)

    @Singleton
    @Provides
    fun provideFormulaRepository(
        driverLocalRepository: DriverLocalRepository,
        constructorLocalRepository: ConstructorLocalRepository,
        grandPrixLocalRepository: GrandPrixLocalRepository,
        seasonLocalRepository: SeasonLocalRepository,
        formulaService: FormulaService,
        @ApplicationContext app: Context
    ): FormulaOneRepository = FormulaOneRepositoryImpl(
        driverLocalRepository = driverLocalRepository,
        constructorLocalRepository = constructorLocalRepository,
        grandPrixLocalRepository = grandPrixLocalRepository,
        seasonLocalRepository = seasonLocalRepository,
        formulaService = formulaService,
        context = app
    )

    @Singleton
    @Provides
    fun provideFormulaDatabase(
        @ApplicationContext app: Context
    ): FormulaOneDatabase = Room.databaseBuilder(
        app,
        FormulaOneDatabase::class.java,
        "formula_database"
    ).fallbackToDestructiveMigration(false).build()

    @Singleton
    @Provides
    fun provideSeasonLocalRepository(
        db: FormulaOneDatabase,
    ): SeasonLocalRepository = SeasonLocalRepositoryImpl(
        seasonDao = db.seasonDao,
        driverDao = db.driverDao,
        constructorDao = db.constructorDao,
        standingDao = db.standingDao,
        raceDao = db.raceDao
    )

    @Singleton
    @Provides
    fun provideGrandPrixLocalRepository(
        db: FormulaOneDatabase,
    ): GrandPrixLocalRepository = GrandPrixLocalRepositoryImpl(
        raceDao = db.raceDao,
        driverDao = db.driverDao,
        constructorDao = db.constructorDao,
    )

    @Singleton
    @Provides
    fun provideDriverLocalRepository(
        db: FormulaOneDatabase,
    ): DriverLocalRepository = DriverLocalRepositoryImpl(
        driverDao = db.driverDao,
        standingDao = db.standingDao,
        seasonDao = db.seasonDao,
    )

    @Singleton
    @Provides
    fun provideConstructorLocalRepository(
        db: FormulaOneDatabase,
    ): ConstructorLocalRepository = ConstructorLocalRepositoryImpl(
        constructorDao = db.constructorDao,
        standingDao = db.standingDao,
        seasonDao = db.seasonDao,
    )
}
