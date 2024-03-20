/*
 * Copyright (c) 2024 skyecodes
 *
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package app.vercors.common

/**
 * A sealed interface to represent a resource that is loaded from another source. It can have 4 different states:
 * - [NotLoaded]: the resource has not been fetched yet
 * - [Loading]: the resource is being fetched; when applicable, a progress value is available, as well as a partial result
 * - [Loaded]: the resource has been successfully loaded; contains the result
 * - [Errored]: the resource failed to load; contains an exception
 */
sealed interface Resource<T> {
    class NotLoaded<T> : Resource<T>
    data class Loading<T>(val progress: Float, val partialResult: T?) : Resource<T>

    @JvmInline
    value class Loaded<T>(val result: T) : Resource<T>

    @JvmInline
    value class Errored<T>(val exception: Exception) : Resource<T>
}