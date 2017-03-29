package de.guntram.mcmod.entitylogger;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = EntityLogger.MODID, 
        version = EntityLogger.VERSION,
	clientSideOnly = true, 
	// guiFactory = "de.guntram.mcmod.entitylogger.config.XXGuiFactory",
	acceptedMinecraftVersions = "[1.11.2]"
)

public class EntityLogger
{
    static final String MODID="entitylogger";
    static final String VERSION="1.0";
    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @EventHandler
    public void preInit(final FMLPreInitializationEvent event) {
        ConfigurationHandler confHandler = ConfigurationHandler.getInstance();
        confHandler.load(event.getSuggestedConfigurationFile());
        MinecraftForge.EVENT_BUS.register(confHandler);
    }
}
