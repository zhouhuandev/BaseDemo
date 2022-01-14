package debug

import com.hzsoft.lib.base.module.ModuleApplication
import com.hzsoft.lib.common.URL_BASE
import com.hzsoft.lib.net.config.NetConfig

class MainApplication : ModuleApplication() {
    override fun onCreate() {
        super.onCreate()
        initNet()
    }

    private fun initNet() {
        val config = NetConfig.Builder()
            .setBaseUrl(URL_BASE)
            .build()
        config.initContext(this)
    }
}
