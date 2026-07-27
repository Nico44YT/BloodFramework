package nico.bloodframework.data;

import net.minecraft.entity.EntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.UUID;

public class BloodInstance {
    private final UUID entityUuid;
    private final Identifier bloodType;
    private final long time;
    private final EntityType<?> entityType;
    private final Text displayText;

    public BloodInstance(UUID entityUuid, Identifier bloodType, long time, EntityType<?> entityType, Text displayText) {
        this.entityUuid = entityUuid;
        this.bloodType = bloodType;
        this.time = time;
        this.entityType = entityType;
        this.displayText = displayText;
    }

    public static BloodInstance fromNbt(NbtCompound compound) {
        UUID entityUuid = compound.getUuid("entityUuid");
        Identifier id = Identifier.tryParse(compound.getString("bloodType"));
        long time = compound.getLong("time");
        EntityType<?> type = EntityType.fromNbt(compound.getCompound("entityType")).orElse(null);
        Text displayText = Text.Serializer.fromJson(compound.getString("displayText"));

        return new BloodInstance(entityUuid, id, time, type, displayText);
    }

    public NbtCompound toNbt() {
        NbtCompound container = new NbtCompound();
        container.putUuid("entityUuid", entityUuid);
        container.putString("bloodType", bloodType.toString());
        container.putLong("time", time);
        NbtCompound entityTypeNbt = new NbtCompound();
        entityTypeNbt.putString("id", EntityType.getId(entityType).toString());
        container.put("entityType", entityTypeNbt);
        container.putString("displayText", Text.Serializer.toJson(displayText));

        return container;
    }

    public UUID getEntityUuid() {
        return entityUuid;
    }

    public Identifier getBloodType() {
        return bloodType;
    }

    public int getColor() {
        return BloodTypeDataLoader.getData().get(getBloodType()).color();
    }

    public long getTime() {
        return time;
    }

    public EntityType<?> getEntityType() {
        return entityType;
    }

    public Identifier getTexture() {
        return BloodTypeDataLoader.getData().get(getBloodType()).texture();
    }

    public Text getDisplayText() {
        return displayText;
    }
}
