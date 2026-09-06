BloodFrame work was originally designed to work with a group of private mods for 1.20.1 (could work for different version, couldn't be bothered to try)
and that is the reason it's tailored specifically for them, 
that could explain some design decisions.

If you wanna fork if for some ~~godawful~~ reason, provide a link to this repository.

Regardless it's available to the public under the GNU GPL-3.0 license.

## Maven
````gradle
repositories {
    maven {
        url 'https://jitpack.io'
    }
}

dependencies {
    modImplementation 'com.github.Nico44YT:BloodFramework:<version_tag>'
    include 'com.github.Nico44YT:BloodFramework:<version_tag>'
}
````

## Custom Types

Data Structure:
````
resources
└ data
  └ <mod_id>
    └ blood_type
      ├ custom_type_1.json
      └ custom_type_2.json
````

Custom blood type definition:

`custom_type_1.json`
````json
{
  "name": "blood_type.mod_id.custom_type_1.name",
  "texture": "mod_id:textures/blood_type/custom_type_1.png",
  "color": "0xFF_FF_FF_FF"
}
````

The texture field can have an optional tint value:
````json
{
  "name": "blood_type.mod_id.custom_type_1.name",
  "texture": {
    "file": "mod_id:textures/blood_type/custom_type_1.png",
    "tint": "0xFF_FF_FF_FF"
  },
  "color": "0xFF_FF_FF_FF"
}
````

The color and tint field can both either be a hexadecimal value or a simple integer
`````json
"color": "0xFF_FF_FF_FF",
"color": 4294967295
`````


Add a custom blood texture map like these:

<style="image-rendering: pixelated;" img src="/src/main/resources/assets/blood_framework/textures/blood_type/living.png" alt="drawing" width="75"/>
<style="image-rendering: pixelated;" img src="/src/main/resources/assets/blood_framework/textures/blood_type/rotten.png" alt="drawing" width="75"/>

## Using Blood Types in Code

`````java
// To put a BloodInstance to an ItemStack
BloodContainerItem.setBloodInstance(instance, stack);

// To retrieve a BloodInstance from an ItemStack
BloodInstance instance = BloodContainerItem.getBloodInstance(stack);

boolean contains = BloodContainerInstance.hasBloodInstance(stack);
`````

### Simple Syringe Item

````java
public class SyringeItem extends Item implements BloodContainerItem {
    public SyringeItem(Settings settings) {
        super(settings);
    }

    // BloodFramework uses mixin injected interfaces, 
    // that makes method calls like entity.bloodFramework$getBloodType() possible
    @Override
    public ActionResult useOnEntity(ItemStack stack, PlayerEntity user, LivingEntity entity, Hand hand) {
        if (this.canBeFilledWithBlood(stack) ||
                entity.bloodFramework$getBloodType().equals(DefaultBloodTypes.NONE)
        ) {
            return super.useOnEntity(stack, user, entity, hand);
        }

        BloodInstance bloodInstance = entity.bloodFramework$createBloodInstance(world);
        stack = BloodContainerItem.setBloodInstance(bloodInstance, stack);
        entity.damage(world.getDamageSources().magic(), 1);
        user.setStackInHand(hand, stack);

        return ActionResult.SUCCESS;
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        super.appendTooltip(stack, world, tooltip, context);

        if (BloodContainerItem.hasBloodInstance(stack)) {
            BloodInstance instance = BloodContainerItem.getBloodInstance(stack);
            tooltip.add(instance.getDisplayText());
        } else {
            tooltip.add(Text.literal("Empty"));
        }
    }
}
````

### Item Changing Syringe Item

````java
public class SyringeItem extends Item implements BloodContainerItem, BloodContainerTransformItem {
    public SyringeItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnEntity(ItemStack stack, PlayerEntity user, LivingEntity entity, Hand hand) {
        //...//
        return ActionResult.SUCCESS;
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        //...//
    }

    @Override
    public Item whenFilledItem() {
        return ModItems.BLOOD_SYRINGE;
    }
    
    @Override
    public Item whenEmptiedItem() {
        return ModItems.EMPTY_SYRINGE;
    }
    
    @Override
    public boolean canBeFilledWithBlood(ItemStack stack) {
        return this == ModItems.EMPTY_SYRINGE && super.canBeFilledWithBlood(stack);
    }
}
````

A BloodInstance is a record containing information like:
- The entities UUID
- Blood type as an Identifier
- The time game time when the blood was drawn
- The EntityType of the entity
- A Text object for displaying, e.g. "Blood from XY with type XY"

<img src="https://github.com/Nico44YT/nico44yt.github.io/blob/main/very_important_images/cat.webp" alt="cat" width="128"/>

###### _Remember! Reality's an illusion, the universe is a hologram, buy gold! Byeeee!_
