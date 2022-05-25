package vladaviedov.getinthebucketmod;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import vladaviedov.getinthebucketmod.item.ItemList;
import vladaviedov.getinthebucketmod.item.VanillaBucketOf;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class Registry {

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        ItemList.bucket_of_bat = new VanillaBucketOf(new Item.Properties().tab(CreativeModeTab.TAB_MISC).stacksTo(1), EntityType.BAT).setRegistryName("bucket_of_bat");
        event.getRegistry().register(ItemList.bucket_of_bat);
    }

}
