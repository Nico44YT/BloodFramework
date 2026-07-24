package nico.bloodframework.mixin.provider;

import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.mob.EndermanEntity;
import net.minecraft.entity.mob.EndermiteEntity;
import net.minecraft.entity.mob.ShulkerEntity;
import net.minecraft.util.Identifier;
import nico.bloodframework.api.EntityBloodTypeProvider;
import nico.bloodframework.data.DefaultBloodTypes;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(value = {
        EndermanEntity.class,
        EnderDragonEntity.class,
        EndermiteEntity.class,
        ShulkerEntity.class
})
public abstract class EnderEntityBloodProviderEntitiesMixin implements EntityBloodTypeProvider {
    @Override
    public Identifier bloodFramework$getDefaultBloodType() {
        return DefaultBloodTypes.ENDER;
    }
}
