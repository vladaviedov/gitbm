package vladaviedov.gitbm;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;

@Mod(Constants.MOD_ID)
public class Main {

	public Main() {
		Registry.registerItems();
		MinecraftForge.EVENT_BUS.register(this);
	}

}
