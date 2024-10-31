package app.vercors.launcher.app.db

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import app.vercors.launcher.account.data.dao.AccountDao
import app.vercors.launcher.account.data.model.AccountEntity
import app.vercors.launcher.core.data.storage.Storage
import app.vercors.launcher.instance.data.dao.InstanceDao
import app.vercors.launcher.instance.data.model.InstanceEntity
import app.vercors.launcher.project.data.local.dao.ProjectDao
import app.vercors.launcher.project.data.local.model.ProjectEntity
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
fun provideAppDatabase(): AppDatabase = Room
    .databaseBuilder<AppDatabase>(Path(Storage.state.value.path, "launcher.db").toString())
    .setDriver(BundledSQLiteDriver())
    .build()

@Single
fun provideAccountDao(db: AppDatabase): AccountDao = db.accountDao()

@Single
fun provideInstanceDao(db: AppDatabase): InstanceDao = db.instanceDao()

@Single
fun provideProjectDao(db: AppDatabase): ProjectDao = db.projectDao()