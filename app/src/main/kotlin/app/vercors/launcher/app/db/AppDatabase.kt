package app.vercors.launcher.app.db

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import app.vercors.launcher.account.data.AccountDao
import app.vercors.launcher.account.data.AccountEntity
import app.vercors.launcher.core.storage.Storage
import app.vercors.launcher.instance.data.InstanceDao
import app.vercors.launcher.instance.data.InstanceEntity
import app.vercors.launcher.project.data.local.ProjectDao
import app.vercors.launcher.project.data.local.ProjectEntity
import kotlinx.io.files.Path
import org.koin.core.annotation.Single

@Database(
    version = 1,
    entities = [InstanceEntity::class, AccountEntity::class, ProjectEntity::class]
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun accountDao(): AccountDao
    abstract fun instanceDao(): InstanceDao
    abstract fun projectDao(): ProjectDao
}

@Single
fun provideAppDatabase(storage: Storage): AppDatabase = Room
    .databaseBuilder<AppDatabase>(Path(storage.state.value.path, "launcher.db").toString())
    .setDriver(BundledSQLiteDriver())
    .build()

@Single
fun provideAccountDao(db: AppDatabase): AccountDao = db.accountDao()

@Single
fun provideInstanceDao(db: AppDatabase): InstanceDao = db.instanceDao()

@Single
fun provideProjectDao(db: AppDatabase): ProjectDao = db.projectDao()