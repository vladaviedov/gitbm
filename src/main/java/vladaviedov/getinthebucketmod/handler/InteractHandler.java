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
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
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
        // instaceof to allow other buckets to work
        if (!isBucket(item) || player.isCrouching()) {
            return;
        }

        // Main hand is already doing the grab
        if (hand.equals(InteractionHand.OFF_HAND) && isBucket(player.getItemInHand(InteractionHand.MAIN_HAND))) {
            return;
        }

        // Can't bucket other people
        if (target.getType() == EntityType.PLAYER) {
            return;
        }

        Item bucketOf = Registry.lookup(target.getType());
        if (bucketOf == null) {
            return;
        }
        ItemStack itemStack = serializeEntToItem(bucketOf, target);

        // Play sound & Update stats
        target.playSound(SoundEvents.BUCKET_FILL_FISH, 1.0f, 1.0f);
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

    private static boolean isBucket(ItemStack item) {
        return item.getItem() == Items.BUCKET;
    }

    private static ItemStack serializeEntToItem(Item item, Entity ent) {
        ItemStack itemStack = new ItemStack(item);
        itemStack.addTagElement(Constants.DATA_TAG, ent.serializeNBT());
        itemStack.addTagElement(Constants.UUID_TAG, StringTag.valueOf(ent.getStringUUID()));
        if (!(item instanceof VanillaBucketOf)) {
            CompoundTag display = new CompoundTag();
            ListTag lore = new ListTag();

            StringTag name = StringTag.valueOf("\"" + ent.getName().getString() + "\"");
            lore.add(name);
            display.put("Lore", lore);
            itemStack.addTagElement("display", display);
        }

        return itemStack;
    }

}
