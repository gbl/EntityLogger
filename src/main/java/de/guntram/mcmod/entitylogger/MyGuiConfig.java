package de.guntram.mcmod.entitylogger;

import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import static net.minecraftforge.common.config.Configuration.CATEGORY_CLIENT;
import net.minecraftforge.fml.client.config.GuiConfig;

public class MyGuiConfig extends GuiConfig {
    public MyGuiConfig(GuiScreen parent) {
        super(parent,
                new ConfigElement(ConfigurationHandler.getConfig().getCategory(CATEGORY_CLIENT)).getChildElements(),
                EntityLogger.MODID,
                false,
                false,
                "EntityLogger config");
    }
}
