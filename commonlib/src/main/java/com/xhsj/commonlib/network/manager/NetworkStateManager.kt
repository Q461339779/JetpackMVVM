package com.xhsj.commonlib.network.manager

import me.hgj.jetpackmvvm.callback.livedata.event.EventLiveData

class NetworkStateManager private constructor() {
    val mNetworkstateCallback = EventLiveData<NetState>()

    companion object {
        val intance: NetworkStateManager by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            NetworkStateManager()

        }
    }
}