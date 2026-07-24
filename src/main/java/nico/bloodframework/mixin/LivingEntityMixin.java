package nico.bloodframework.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import nico.bloodframework.api.EntityBloodTypeProvider;
import nico.bloodframework.data.BloodInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity implements EntityBloodTypeProvider {

    @Unique
    private Identifier bloodType;

    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    public void blood_framework$init(EntityType<?> entityType, World world, CallbackInfo ci) {
        this.bloodType = bloodFramework$getDefaultBloodType();
    }

    @Inject(method = "writeCustomDataToNbt", at = @At("TAIL"))
    public void blood_framework$writeNbt(NbtCompound nbt, CallbackInfo ci) {
        nbt.putString("blood_type", bloodType.toString());
    }

    @Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
    public void blood_framework$readNbt(NbtCompound nbt, CallbackInfo ci) {
        if(nbt.contains("blood_type")) {
            bloodType = Identifier.tryParse(nbt.getString("blood_type"));
        } else {
            bloodType = bloodFramework$getDefaultBloodType();
        }
    }

    @Override
    public Identifier bloodFramework$getBloodType() {
        return this.bloodType;
    }

    @Override
    public void bloodFramework$setBloodType(Identifier id) {
        this.bloodType = id;
    }

    @Override
    public BloodInstance bloodFramework$createBloodInstance(World world) {
        return new BloodInstance(
                getUuid(),
                bloodFramework$getBloodType(),
                world.getTime(),
                getType(),
                getDisplayName()
        );
    }
}
