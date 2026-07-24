package nico.bloodframework.mixin.provider;

import net.minecraft.entity.mob.*;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.util.Identifier;
import nico.bloodframework.api.EntityBloodTypeProvider;
import nico.bloodframework.data.DefaultBloodTypes;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(value = {
        SkeletonEntity.class,
        SkeletonHorseEntity.class,
        WitherSkeletonEntity.class,
        StrayEntity.class,
        VexEntity.class,
        IronGolemEntity.class
})
public abstract class NoneBloodProviderEntitiesMixin implements EntityBloodTypeProvider {
    @Override
    public Identifier bloodFramework$getDefaultBloodType() {
        return DefaultBloodTypes.NONE;
    }
}
