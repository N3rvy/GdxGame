package packets.client

import Game
import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Entity
import ecs.components.synchronization.setField
import ktx.ashley.entity
import packets.ClientPacket
import packets.ComponentData

class PacketCreateEntity(
    val components: Array<ComponentData>,
) : ClientPacket {
    override fun execute() {
        val engine = Game.engine

        engine.entity {
            // Code from ktx.ashley.engines (using this because i can not use generics)
            components.forEach { comp ->
                val component = try {
                    engine.createComponent(comp.type)
                        ?: throw NullPointerException("The component of ${comp.type} type is null.")
                } catch (exception: Throwable) {
                    exception.printStackTrace()
                    return
                }.apply {
                    comp.fields.forEach { field ->
                        this.setField(field.key, field.value)
                    }
                }
                entity.add(component)
            }
        }
    }

    companion object {
        fun create(configure: MutableList<ComponentData>.() -> Unit): PacketCreateEntity {
            val conf = mutableListOf<ComponentData>()
            conf.configure()
            return PacketCreateEntity(conf.toTypedArray())
        }

        fun fromEntity(entity: Entity): PacketCreateEntity {
            return create {
                entity.components.forEach {
                    add(ComponentData.fromComponent(it))
                }
            }
        }
    }
}

inline fun <reified T : Component> MutableList<ComponentData>.with(configure: T.() -> Unit = {}) =
    add(ComponentData.create(configure))
