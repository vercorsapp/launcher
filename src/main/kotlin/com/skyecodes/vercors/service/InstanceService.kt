package com.skyecodes.vercors.service

import com.skyecodes.vercors.data.app.Instance
import kotlinx.coroutines.CoroutineScope

interface InstanceService {
    suspend fun fetchInstances(scope: CoroutineScope): List<Instance>
}