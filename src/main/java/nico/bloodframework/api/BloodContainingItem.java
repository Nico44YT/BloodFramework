package nico.bloodframework.api;

import net.minecraft.item.Item;

public abstract class BloodContainingItem extends Item implements BloodContainerItem {

    public BloodContainingItem(Settings settings) {
        super(settings);
    }
}
