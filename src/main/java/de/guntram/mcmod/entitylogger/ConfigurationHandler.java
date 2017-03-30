package de.guntram.mcmod.entitylogger;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import java.io.File;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.config.Configuration;

public class ConfigurationHandler {

    private static ConfigurationHandler instance;

    private Configuration config;
    private String configFileName;
    public int x1, x2, y1, y2, z1, z2;
    public boolean logatall;

    public static ConfigurationHandler getInstance() {
        if (instance==null)
            instance=new ConfigurationHandler();
        return instance;
    }

    public void load(final File configFile) {
        if (config == null) {
            config = new Configuration(configFile);
            configFileName=configFile.getPath();
            loadConfig();
        }
    }

    @SubscribeEvent
    public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
        // System.out.println("OnConfigChanged for "+event.getModID());
        if (event.getModID().equalsIgnoreCase(EntityLogger.MODID)) {
            loadConfig();
        }
    }
    
    private void loadConfig() {
        logatall=config.getBoolean("logatall."+Minecraft.getMinecraft().getSession().getUsername(),
                Configuration.CATEGORY_CLIENT, logatall, "Log for "+Minecraft.getMinecraft().getSession().getUsername());
        x1=config.getInt("x1", Configuration.CATEGORY_CLIENT, x1, Integer.MIN_VALUE, Integer.MAX_VALUE, "XPos 1");
        x2=config.getInt("x2", Configuration.CATEGORY_CLIENT, x2, Integer.MIN_VALUE, Integer.MAX_VALUE, "XPos 2");
        y1=config.getInt("y1", Configuration.CATEGORY_CLIENT, y1, Integer.MIN_VALUE, Integer.MAX_VALUE, "YPos 1");
        y2=config.getInt("y2", Configuration.CATEGORY_CLIENT, y2, Integer.MIN_VALUE, Integer.MAX_VALUE, "YPos 2");
        z1=config.getInt("z1", Configuration.CATEGORY_CLIENT, z1, Integer.MIN_VALUE, Integer.MAX_VALUE, "ZPos 1");
        z2=config.getInt("z2", Configuration.CATEGORY_CLIENT, z2, Integer.MIN_VALUE, Integer.MAX_VALUE, "ZPos 2");
        if (config.hasChanged())
            config.save();
    }
    
    public static Configuration getConfig() {
        return getInstance().config;
    }
    
    public static String getConfigFileName() {
        return getInstance().configFileName;
    }
}
