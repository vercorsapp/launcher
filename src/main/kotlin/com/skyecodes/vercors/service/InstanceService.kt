package com.skyecodes.vercors.service

import com.skyecodes.vercors.data.app.Instance
import com.skyecodes.vercors.data.app.Loader
import com.skyecodes.vercors.data.mojang.MojangVersionManifest
import kotlinx.coroutines.CoroutineScope

interface InstanceService {
    suspend fun loadInstances(scope: CoroutineScope): List<Instance>

    suspend fun createInstance(
        instanceName: String,
        version: MojangVersionManifest.Version,
        loader: Loader?,
        loaderVersion: String?
    )
}