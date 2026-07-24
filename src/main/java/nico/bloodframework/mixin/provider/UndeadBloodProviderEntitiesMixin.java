package nico.bloodframework.mixin.provider;

import net.minecraft.entity.mob.*;
import net.minecraft.util.Identifier;
import nico.bloodframework.api.EntityBloodTypeProvider;
import nico.bloodframework.data.DefaultBloodTypes;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(value = {
        ZombieEntity.class,
        ZombieHorseEntity.class,
        DrownedEntity.class,
        ZombieVillagerEntity.class,
        HuskEntity.class
})
public abstract class UndeadBloodProviderEntitiesMixin implements EntityBloodTypeProvider {
    @Override
    public Identifier bloodFramework$getDefaultBloodType() {
        return DefaultBloodTypes.ROTTEN;
    }
}
