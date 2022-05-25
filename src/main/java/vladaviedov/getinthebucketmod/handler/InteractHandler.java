package vladaviedov.getinthebucketmod.handler;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.core.jmx.Server;
import vladaviedov.getinthebucketmod.Constants;
import vladaviedov.getinthebucketmod.Registry;
import vladaviedov.getinthebucketmod.item.VanillaBucketOf;

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

        Item bucketOf = Registry.itemLookup.get(target.getType());
        if (bucketOf == null) {
            return;
        }
        target.playSound(SoundEvents.BUCKET_FILL_FISH, 1.0f, 1.0f);

        // Save NBT
        ItemStack itemStack = new ItemStack(bucketOf);
        itemStack.addTagElement(Constants.DATA_TAG, target.serializeNBT());
        itemStack.addTagElement(Constants.UUID_TAG, StringTag.valueOf(target.getStringUUID()));
        if (!(bucketOf instanceof VanillaBucketOf)) {
            CompoundTag display = new CompoundTag();
            ListTag lore = new ListTag();

            StringTag name = StringTag.valueOf("\"" + target.getName().getString());
            lore.add(name);
            display.put("Lore", lore);
            itemStack.addTagElement("display", display);
        }

        // Update stats
        if (!target.getLevel().isClientSide()) {
            CriteriaTriggers.FILLED_BUCKET.trigger((ServerPlayer) player, itemStack);
        }

        // Gamemode check
        if (!player.isCreative()) {
            item.shrink(1);
            if (item.isEmpty()) {
                player.setItemInHand(hand, itemStack);
            } else if (!player.getInventory().add(itemStack)) {
                player.drop(itemStack, false);
            }
        }

        // Remove entity
        target.remove(Entity.RemovalReason.DISCARDED);
        event.setResult(Event.Result.ALLOW);
    }

}
