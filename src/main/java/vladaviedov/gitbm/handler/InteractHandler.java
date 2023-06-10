package vladaviedov.gitbm.handler;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.Entity.RemovalReason;
import net.minecraft.world.entity.boss.EnderDragonPart;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import vladaviedov.gitbm.Constants;
import vladaviedov.gitbm.Registry;
import vladaviedov.gitbm.item.VanillaBucketOf;

@Mod.EventBusSubscriber
public class InteractHandler {

	@SubscribeEvent
	public static void onInteract(PlayerInteractEvent.EntityInteractSpecific event) {
		Player player = event.getEntity();
		InteractionHand hand = event.getHand();
		ItemStack item = player.getItemInHand(hand);
		Entity target = event.getTarget();

		// Cases to ignore event
		if (!(item.getItem() == Items.BUCKET) || player.isCrouching() || !target.isAlive()) {
			return;
		}

		// Sometimes the server does not recieve this event
		if (target.getType() == EntityType.ENDER_DRAGON) {
			EnderDragon dragon = ((EnderDragonPart) target).getParent();
			for (EnderDragonPart part : dragon.getSubEntities()) {
				part.remove(RemovalReason.DISCARDED);
			}
			target = dragon;
		}

		// Not a fan, but it works
		if (!(target instanceof Mob)) {
			return;
		}

		Item bucketOf = Registry.lookup(target.getType());
		prepareEnt(target);
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

	/**
	 * Prepare entity for "bucketing"
	 * @param ent entity
	 */
	private static void prepareEnt(Entity ent) {
		ent.ejectPassengers();
		ent.setDeltaMovement(0, 0, 0);
		ent.fallDistance = 0;
	}

	/**
	 * Serialize entity into an ItemStack (save NBT)
	 * @param item underlying item
	 * @param ent entity
	 * @return seralized ItemStack
	 */
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
