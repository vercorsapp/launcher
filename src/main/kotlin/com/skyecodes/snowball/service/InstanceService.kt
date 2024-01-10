package com.skyecodes.snowball.service

import com.skyecodes.snowball.data.app.Instance
import kotlinx.coroutines.CoroutineScope

interface InstanceService {
    suspend fun fetchInstances(scope: CoroutineScope): List<Instance>
}