package nico.bloodframework.api;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import nico.bloodframework.data.BloodInstance;

public interface BloodContainerItem {
    String NBT_KEY = "BloodInstance";

    static BloodInstance getBloodInstance(ItemStack stack) {
        if(!(stack.getItem() instanceof BloodContainerItem bloodContainerItem)) return null;
        return ((BloodContainerItem)stack.getItem()).readBloodInstance(stack);
    }

    static ItemStack setBloodInstance(BloodInstance bloodInstance, ItemStack stack) {
        return ((BloodContainerItem)stack.getItem()).writeBloodInstance(bloodInstance, stack);
    }

    static boolean hasBloodInstance(ItemStack stack) {
        return ((BloodContainerItem)stack.getItem()).containsBloodInstance(stack);
    }

    default boolean canFillWithBlood(ItemStack stack) {
        return !hasBloodInstance(stack);
    }

    default BloodInstance readBloodInstance(ItemStack stack) {
        if (!hasBloodInstance(stack)) return null;

        return BloodInstance.fromNbt(stack.getNbt().getCompound(NBT_KEY));
    }

    default ItemStack writeBloodInstance(BloodInstance bloodInstance, ItemStack stack) {
        if(stack.getItem() instanceof BloodContainerItem containerItem && !containerItem.canFillWithBlood(stack) && bloodInstance != null) return stack;

        if (bloodInstance == null) {
            stack.getOrCreateNbt().remove(NBT_KEY);

            if (stack.getItem() instanceof BloodContainerTransformItem transformItem) {
                ItemStack returnStack = new ItemStack(transformItem.whenEmptiedItem(), stack.getCount());
                returnStack.setNbt(stack.getNbt());
                return returnStack;
            }

            return stack;
        }
        NbtCompound tag = bloodInstance.toNbt();
        stack.getOrCreateNbt().put(NBT_KEY, tag);

        if (stack.getItem() instanceof BloodContainerTransformItem transformItem) {
            ItemStack returnStack = new ItemStack(transformItem.whenFilledItem(), stack.getCount());
            returnStack.setNbt(stack.getNbt());
            return returnStack;
        }

        return stack;
    }

    default boolean containsBloodInstance(ItemStack stack) {
        return stack.hasNbt() && stack.getNbt() != null && stack.getNbt().contains(NBT_KEY);
    }
}
