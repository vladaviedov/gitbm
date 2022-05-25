package vladaviedov.getinthebucketmod.item;

import net.minecraft.world.entity.EntityType;

public class ItemData {

    public EntityType<?> entType;
    public String regName;

    public ItemData setEntType(EntityType<?> type) {
        entType = type;
        return this;
    }

    public ItemData setRegName(String name) {
        regName = name;
        return this;
    }

}
