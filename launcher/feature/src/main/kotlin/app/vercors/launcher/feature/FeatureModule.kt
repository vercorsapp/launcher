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

package app.vercors.launcher.feature

import app.vercors.launcher.account.AccountFeatureModule
import app.vercors.launcher.account.SettingsFeatureModule
import app.vercors.launcher.game.GameFeatureModule
import app.vercors.launcher.home.HomeFeatureModule
import app.vercors.launcher.instance.InstanceFeatureModule
import app.vercors.launcher.project.ProjectFeatureModule
import app.vercors.launcher.setup.SetupFeatureModule
import org.koin.core.annotation.Module

@Module(
    includes = [
        AccountFeatureModule::class,
        GameFeatureModule::class,
        HomeFeatureModule::class,
        InstanceFeatureModule::class,
        ProjectFeatureModule::class,
        SettingsFeatureModule::class,
        SetupFeatureModule::class,
    ]
)
class FeatureModule