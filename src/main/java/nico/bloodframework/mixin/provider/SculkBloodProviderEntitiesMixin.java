package nico.bloodframework.mixin.provider;

import net.minecraft.entity.mob.WardenEntity;
import net.minecraft.util.Identifier;
import nico.bloodframework.api.EntityBloodTypeProvider;
import nico.bloodframework.data.DefaultBloodTypes;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(value = {
        WardenEntity.class
})
public abstract class SculkBloodProviderEntitiesMixin implements EntityBloodTypeProvider {
    @Override
    public Identifier bloodFramework$getDefaultBloodType() {
        return DefaultBloodTypes.SCULK;
    }
}
