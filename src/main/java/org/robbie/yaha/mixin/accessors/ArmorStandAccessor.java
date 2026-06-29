package org.robbie.yaha.mixin.accessors;

import net.minecraft.entity.decoration.ArmorStandEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(ArmorStandEntity.class)
public interface ArmorStandAccessor {
    @Invoker("setSmall")
    void yaha_setSmall(boolean small);
}
