package vladaviedov.getinthebucketmod;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;

@Mod(Constants.MOD_ID)
public class Main
{

	public Main()
	{
		MinecraftForge.EVENT_BUS.register(this);
	}

}
