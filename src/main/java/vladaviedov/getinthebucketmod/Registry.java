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

    private static final Logger LOGGER = LogUtils.getLogger();
    public static Map<EntityType<?>, Item> itemLookup = new HashMap<>();

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        event.getRegistry().registerAll(
                makeItem(EntityType.BAT, "bucket_of_bat"),
                makeItem(EntityType.ZOMBIE, "bucket_of_zombie")
        );
    }

    public static Item makeItem(EntityType<?> type, String name) {
        Item item = new VanillaBucketOf(new Item.Properties()
                .stacksTo(1)
                .tab(Constants.CREATIVE_TAB), type)
                .setRegistryName(name);
        itemLookup.put(type, item);
        LOGGER.info("added new item: " + name);
        return item;
    }

}
