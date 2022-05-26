package vladaviedov.getinthebucketmod;

import com.mojang.logging.LogUtils;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.slf4j.Logger;
import vladaviedov.getinthebucketmod.item.VanillaBucketOf;

import java.util.HashMap;
import java.util.Map;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class Registry {

    private static final Logger logger = LogUtils.getLogger();
    private static final Map<EntityType<?>, Item> itemLookup = new HashMap<>();

    /**
     * Register items
     * @param event register event
     */
    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        event.getRegistry().registerAll(
                // Passive mobs
                makeItem(EntityType.AXOLOTL, "bucket_of_axolotl"), /* no texture */
                makeItem(EntityType.BAT, "bucket_of_bat"),
                makeItem(EntityType.CAT, "bucket_of_cat"),
                makeItem(EntityType.CHICKEN, "bucket_of_chicken"),
                makeItem(EntityType.COW, "bucket_of_cow"),
                makeItem(EntityType.DONKEY, "bucket_of_donkey"),
                makeItem(EntityType.FOX, "bucket_of_fox"),
                makeItem(EntityType.GIANT, "bucket_of_giant"),
                makeItem(EntityType.GLOW_SQUID, "bucket_of_glow_squid"), /* no texture */
                makeItem(EntityType.HORSE, "bucket_of_horse"),
                makeItem(EntityType.MOOSHROOM, "bucket_of_mooshroom"),
                makeItem(EntityType.MULE, "bucket_of_mule"),
                makeItem(EntityType.OCELOT, "bucket_of_ocelot"),
                makeItem(EntityType.PANDA, "bucket_of_panda"),
                makeItem(EntityType.PARROT, "bucket_of_parrot"),
                makeItem(EntityType.PIG, "bucket_of_pig"),
                makeItem(EntityType.RABBIT, "bucket_of_rabbit"),
                makeItem(EntityType.SHEEP, "bucket_of_sheep"),
                makeItem(EntityType.SKELETON_HORSE, "bucket_of_skeleton_horse"),
                makeItem(EntityType.SNOW_GOLEM, "bucket_of_snow_golem"),
                makeItem(EntityType.SQUID, "bucket_of_squid"),
                makeItem(EntityType.STRIDER, "bucket_of_strider"),
                makeItem(EntityType.TURTLE, "bucket_of_turtle"),
                makeItem(EntityType.VILLAGER, "bucket_of_villager"),
                makeItem(EntityType.ZOMBIE_HORSE, "bucket_of_zombie_horse")
        );
    }

    /**
     * Make a registry entry for vanilla buckets
     * @param type entity type
     * @param name registry name
     * @return item registry entry
     */
    private static Item makeItem(EntityType<?> type, String name) {
        Item item = new VanillaBucketOf(new Item.Properties()
                .stacksTo(1)
                .tab(Constants.CREATIVE_TAB), type)
                .setRegistryName(name);
        itemLookup.put(type, item);
        logger.info("added new item: " + name);
        return item;
    }

    /**
     * Lookup item object by entity type
     * @param t entity type
     * @return item
     */
    public static Item lookup(EntityType<?> t) {
        return itemLookup.get(t);
    }

}
