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

package app.vercors.instance

import app.vercors.common.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

/**
 * Repository that manages the instances
 */
interface InstanceRepository {
    /**
     * Contains the loading state of the instances (loading, loaded or not loaded).
     */
    val loadingState: StateFlow<Resource<List<Instance>>>

    /**
     * Contains the state of the instances (its value if loaded or loading, or null if not loaded).
     */
    val state: StateFlow<List<Instance>?>

    /**
     * Contains the current instances.
     * @throws NullPointerException if the instances are not loaded
     */
    val current: List<Instance>

    /**
     * Loads the instances and automatically updates the state when done.
     */
    fun loadInstances(): Flow<InstanceResult>

    /**
     * Finds an instance from loaded instances, or returns null if no such instance is loaded
     * @param id the id of the instance to find
     */
    fun findInstance(id: String): Instance?

    /**
     * Updates the instance data and automatically saves when done.
     * @param id the id of the instance to update
     * @param updater the instance data updater
     */
    suspend fun updateInstanceData(id: String, updater: (InstanceData) -> InstanceData)

    /**
     * Updates the instance status.
     * @param id the id of the instance to update
     * @param updater the instance status updater
     */
    fun updateInstanceStatus(id: String, updater: (InstanceStatus) -> InstanceStatus)

    /**
     * Creates the instance and automatically saves when done.
     * @param instance the instance to create
     * @return the id of the created instance
     */
    suspend fun createInstance(instance: InstanceData): String
}