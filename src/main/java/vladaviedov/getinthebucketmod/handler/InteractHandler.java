package vladaviedov.getinthebucketmod.handler;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import vladaviedov.getinthebucketmod.Registry;

@Mod.EventBusSubscriber
public class InteractHandler {

    @SubscribeEvent
    public static void onInteract(PlayerInteractEvent.EntityInteractSpecific event) {
        Player player = event.getPlayer();
        InteractionHand hand = event.getHand();
        ItemStack item = player.getItemInHand(hand);
        Entity target = event.getTarget();

        // Ignore if not bucket or player is crouching
        if (item.getItem() != Items.BUCKET || player.isCrouching()) {
            return;
        }

        // Can't bucket other people
        if (target.getType() == EntityType.PLAYER) {
            return;
        }

        target.playSound(SoundEvents.BUCKET_FILL_FISH, 1.0f, 1.0f);
//        ItemStack bucketOf = new ItemStack(Registry.itemLookup.get(target.getType()));

    }

}
