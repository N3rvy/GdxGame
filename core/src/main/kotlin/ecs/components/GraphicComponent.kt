package ecs.components

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.TextureRegion
import ecs.EntityComponent
import ktx.ashley.get
import ktx.ashley.mapperFor

class GraphicComponent : EntityComponent {
    val sprite = Sprite()

    override fun reset() {
        sprite.texture = null
        sprite.setColor(1f, 1f, 1f, 1f)
    }

    fun setSpriteRegion(region: TextureRegion) {
        sprite.setRegion(region)
    }

    companion object {
        val mapper = mapperFor<GraphicComponent>()
    }
}

fun Entity.obtainGraphic() = this[GraphicComponent.mapper]
    ?: throw KotlinNullPointerException("No GraphicComponent given for entity $this")
