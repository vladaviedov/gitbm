package vladaviedov.getinthebucketmod.item;

import com.mojang.logging.LogUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import vladaviedov.getinthebucketmod.Constants;

import java.util.UUID;

public class BucketOf extends Item {

    private static final Logger LOGGER = LogUtils.getLogger();

    public BucketOf(Properties props) {
        super(props);
    }

    @Override
    @NotNull
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        ItemStack heldItem = player.getItemInHand(hand);

        BlockHitResult trace = getPlayerPOVHitResult(world, player, ClipContext.Fluid.ANY);
        BlockPos targetPos = trace.getBlockPos();

        if (!player.mayInteract(world, targetPos)) {
            return new InteractionResultHolder<>(InteractionResult.FAIL, heldItem);
        }

        player.awardStat(Stats.ITEM_USED.get(this));
        player.playSound(SoundEvents.BUCKET_EMPTY_FISH, 1.0f, 1.0f);

        // Only run the spawn server side
        if (world.isClientSide()) {
            return new InteractionResultHolder<>(InteractionResult.PASS, heldItem);
        }

        // Find correct position
        BlockState blockState = world.getBlockState(targetPos);
        if (!blockState.getCollisionShape(world, targetPos).isEmpty()) {
            targetPos = targetPos.offset(trace.getDirection().getNormal());
        }

        placeEntity(world, heldItem, targetPos, player);
        if (player.isCreative()) {
            return new InteractionResultHolder<>(InteractionResult.SUCCESS, heldItem);
        }

        ItemStack bucket = new ItemStack(Items.BUCKET);
        heldItem.shrink(1);
        if (heldItem.isEmpty()) {
            return new InteractionResultHolder<>(InteractionResult.SUCCESS, bucket);
        }

        if (!player.getInventory().add(bucket)) {
            player.drop(bucket, false);
        }

        return new InteractionResultHolder<>(InteractionResult.PASS, heldItem);
    }

    public void placeEntity(Level world, ItemStack item, BlockPos pos, Player player) {
        ServerLevel server = (ServerLevel) world;
        CompoundTag entityData = item.getTagElement(Constants.DATA_TAG);
        if (entityData == null) {
            LOGGER.error("Data tag is null");
            return;
        }

        EntityType.loadEntityRecursive(entityData, server, e -> {
            e.setPos(pos.getX() + 0.5f, pos.getY(), pos.getZ() + 0.5f);
            while (!server.tryAddFreshEntityWithPassengers(e)) {
                e.setUUID(UUID.randomUUID());
            }
            return e;
        });
    }

}
