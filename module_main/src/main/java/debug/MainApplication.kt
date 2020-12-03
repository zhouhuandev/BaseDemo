package debug

import com.hzsoft.lib.common.BaseApplication
import com.hzsoft.lib.common.URL_BASE
import com.hzsoft.lib.net.config.NetConfig

/**
 * Description: <><br>
 * Author:      mxdl<br>
 * Date:        2018/12/27<br>
 * Version:     V1.0.0<br>
 * Update:     <br>
 */
class MainApplication : BaseApplication() {
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
