package nico.bloodframework.api;

import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import nico.bloodframework.data.BloodInstance;
import nico.bloodframework.data.DefaultBloodTypes;

public interface EntityBloodTypeProvider {
    default Identifier bloodFramework$getDefaultBloodType() {
        return DefaultBloodTypes.LIVING;
    }

    default Identifier bloodFramework$getBloodType() {
        return null;
    }

    default void bloodFramework$setBloodType(Identifier id) {

    }

    default BloodInstance bloodFramework$createBloodInstance(World world) {
        return null;
    }
}
