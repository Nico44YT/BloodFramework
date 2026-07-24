package nico.bloodframework.api;

import net.minecraft.item.Item;

public interface BloodContainerTransformItem {
    Item whenFilledItem();

    Item whenEmptiedItem();
}
