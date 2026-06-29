package org.robbie.yaha.features.armor_stand

import at.petrak.hexcasting.api.casting.ParticleSpray
import at.petrak.hexcasting.api.casting.RenderedSpell
import at.petrak.hexcasting.api.casting.castables.SpellAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getEntity
import at.petrak.hexcasting.api.casting.getVec3
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.mishaps.MishapBadEntity
import at.petrak.hexcasting.api.misc.MediaConstants
import net.minecraft.entity.decoration.ArmorStandEntity
import net.minecraft.util.math.EulerAngle
import net.minecraft.util.math.Vec3d
import kotlin.math.PI

class OpStandPose(val part: Part) : SpellAction {
    override val argc = 2

    override fun execute(
        args: List<Iota>,
        env: CastingEnvironment
    ): SpellAction.Result {
        val armorStand = args.getEntity(0, argc)
        val angles = args.getVec3(1, argc)
        env.assertEntityInRange(armorStand)
        if (armorStand !is ArmorStandEntity) throw MishapBadEntity.of(armorStand, "yaha:armor_stand")
        return SpellAction.Result(
            Spell(armorStand, part, angles),
            MediaConstants.DUST_UNIT / 8,
            listOf(ParticleSpray.cloud(armorStand.pos, 1.0))
        )
    }

    private data class Spell(val armorStand: ArmorStandEntity, val part: Part, val angles: Vec3d) : RenderedSpell {
        override fun cast(env: CastingEnvironment) {
            val degreeAngles = angles.multiply(180.0 / PI)
            val eulerAngle = EulerAngle(
                degreeAngles.x.toFloat(),
                degreeAngles.y.toFloat(),
                degreeAngles.z.toFloat()
            )
            armorStand.apply {
                when (part) {
                    Part.HEAD -> headRotation = eulerAngle
                    Part.BODY -> bodyRotation = eulerAngle
                    Part.LEFT_ARM -> leftArmRotation = eulerAngle
                    Part.RIGHT_ARM -> rightArmRotation = eulerAngle
                    Part.LEFT_LEG -> leftLegRotation = eulerAngle
                    Part.RIGHT_LEG -> rightLegRotation = eulerAngle
                }
            }
        }
    }

    enum class Part {
        HEAD,
        BODY,
        LEFT_ARM,
        RIGHT_ARM,
        LEFT_LEG,
        RIGHT_LEG
    }
}