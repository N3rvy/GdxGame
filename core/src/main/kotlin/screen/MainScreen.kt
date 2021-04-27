package screen

import AnimationProvider
import GameAnimation
import Game
import ecs.components.*
import ecs.createEntityWithId
import ktx.ashley.with
import ktx.log.debug
import ktx.log.logger
import kotlin.math.min

private val LOG = logger<MainScreen>()

private const val MAX_DELTA_TIME = 1 / 20f

class MainScreen(game: Game) : AbstractScreen(game) {

    override fun show() {
        LOG.debug { "Main screen shown" }

        engine.createEntityWithId {
            with<TransformComponent> {
                setInitialPosition(8f, 4.5f, 0f)
                size.set(2.5f, 2.5f)
            }
            with<GraphicComponent>()
            with<PlayerComponent>()
            with<AnimationComponent> {
                animation = AnimationProvider.getAnimation(GameAnimation.PLAYER_IDLE)
            }
            with<FlipComponent>()
            with<MoveComponent>()
        }
    }

    override fun render(delta: Float) {
        engine.update(min(MAX_DELTA_TIME, delta))
    }

    override fun dispose() {
        super.dispose()
    }
}