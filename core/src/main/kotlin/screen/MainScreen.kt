package screen

import ktx.log.debug
import ktx.log.logger
import kotlin.concurrent.thread
import kotlin.math.min

private val LOG = logger<MainScreen>()

private const val MAX_DELTA_TIME = 1 / 20f

class MainScreen : AbstractScreen() {

    override fun show() {
        LOG.debug { "Main screen shown" }

        thread {
            while(true) {
                Thread.sleep(50)
                Game.client.flush()
            }
        }
    }

    override fun render(delta: Float) {
        engine.update(min(MAX_DELTA_TIME, delta))
    }
}