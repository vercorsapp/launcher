/*
 * Copyright (c) 2024-2025 skyecodes
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

pluginManagement {
    repositories {
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
        google()
        gradlePluginPortal()
        mavenCentral()
    }
}

rootProject.name = "vercors"

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
include(
    "launcher:app",
    "launcher:core:serialization",
    "launcher:core:network",
    "launcher:core:storage",
    "launcher:core:config",
    "launcher:core:domain",
    "launcher:core:presentation",
    "launcher:core:resources",
    "launcher:core:meta",
    "launcher:feature:account:data",
    "launcher:feature:account:domain",
    "launcher:feature:account:presentation",
    "launcher:feature:game:data",
    "launcher:feature:game:domain",
    "launcher:feature:game:presentation",
    "launcher:feature:home:data",
    "launcher:feature:home:domain",
    "launcher:feature:home:presentation",
    "launcher:feature:instance:data",
    "launcher:feature:instance:domain",
    "launcher:feature:instance:presentation",
    "launcher:feature:project:data",
    "launcher:feature:project:domain",
    "launcher:feature:project:presentation",
    "launcher:feature:settings:data",
    "launcher:feature:settings:domain",
    "launcher:feature:settings:presentation",
    "launcher:feature:setup:data",
    "launcher:feature:setup:domain",
    "launcher:feature:setup:presentation",
    "meta:backend",
    "meta:api",
    "lib:loader:fabric",
    "lib:loader:fabric-like",
    "lib:loader:forge",
    "lib:loader:neoforge",
    "lib:loader:quilt",
    "lib:minecraft",
    "lib:platform:curseforge",
    "lib:platform:modrinth",
)
