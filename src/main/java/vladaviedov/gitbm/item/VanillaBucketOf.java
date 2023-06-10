package vladaviedov.gitbm.item;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import vladaviedov.gitbm.Constants;

public class VanillaBucketOf extends BucketOf {

	// Stored entity type
	private final EntityType<?> _entType;

	public VanillaBucketOf(Properties props, EntityType<?> entType) {
		super(props);
		_entType = entType;
	}

	@Override
	public void placeEntity(Level world, ItemStack item, BlockPos pos, Player player) {
		if (item.getTagElement(Constants.DATA_TAG) != null) {
			super.placeEntity(world, item, pos, player);
		} else {
			_entType.spawn((ServerLevel) world, item, player, pos, MobSpawnType.BUCKET, false, false);
		}
	}

}
