package nico.bloodframework.api;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import nico.bloodframework.data.BloodInstance;

public interface BloodContainerItem {
    String NBT_KEY = "BloodInstance";

    static BloodInstance getBloodInstance(ItemStack stack) {
        if (!hasBloodInstance(stack)) return null;

        return BloodInstance.fromNbt(stack.getNbt().getCompound(NBT_KEY));
    }

    static ItemStack setBloodInstance(BloodInstance bloodInstance, ItemStack stack) {
        if(stack.getItem() instanceof BloodContainerItem containerItem && !containerItem.canBeFilledWithBlood(stack)) return stack;

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

    static boolean hasBloodInstance(ItemStack stack) {
        return stack.hasNbt() && stack.getNbt() != null && stack.getNbt().contains(NBT_KEY);
    }

    default boolean canBeFilledWithBlood(ItemStack stack) {
        return true;
    }
}
