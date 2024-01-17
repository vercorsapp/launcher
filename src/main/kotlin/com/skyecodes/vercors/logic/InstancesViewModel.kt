package com.skyecodes.vercors.logic

import com.skyecodes.vercors.data.model.app.Instance
import kotlinx.coroutines.flow.StateFlow
import moe.tlaster.precompose.viewmodel.ViewModel

class InstancesViewModel(
    val instances: StateFlow<List<Instance>?>
) : ViewModel()