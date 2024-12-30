/*
 * Copyright (c) 2024 skyecodes
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

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