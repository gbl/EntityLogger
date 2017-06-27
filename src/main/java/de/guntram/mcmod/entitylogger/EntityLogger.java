package de.guntram.mcmod.entitylogger;

import java.io.File;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.TreeSet;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Mod(modid = EntityLogger.MODID, 
        version = EntityLogger.VERSION,
	clientSideOnly = true, 
	guiFactory = "de.guntram.mcmod.entitylogger.GuiFactory",
	acceptedMinecraftVersions = "[1.12]"
)

public class EntityLogger
{
    static final String MODID="entitylogger";
    static final String VERSION="1.0";
    
    long lastLogged;
    boolean connected=false;
    ConfigurationHandler confHandler;
    File logFile;
    DailyLogWriter logger;
    
    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @EventHandler
    public void preInit(final FMLPreInitializationEvent event) {
        confHandler = ConfigurationHandler.getInstance();
        confHandler.load(event.getSuggestedConfigurationFile());
        MinecraftForge.EVENT_BUS.register(confHandler);
        logger=new DailyLogWriter(event.getSuggestedConfigurationFile().getParentFile().getParent()+
                "/logs/"+MODID+"."+Minecraft.getMinecraft().getSession().getUsername()+"-%d.log");
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onConnectedToServerEvent(FMLNetworkEvent.ClientConnectedToServerEvent event) {
        connected=true;
        lastLogged=System.currentTimeMillis()+5000;
    }
    
    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onDisconnectFromServerEvent(FMLNetworkEvent.ClientDisconnectionFromServerEvent event) {
        if (connected)
            logger.closeWriter();
        connected=false;
    }
    
    @SubscribeEvent
    public void onCLientTick(final ClientTickEvent event) {
        long now;
        if (!confHandler.logatall || !connected || (now=System.currentTimeMillis()) < lastLogged+1000)
            return;
        PrintWriter writer=logger.getWriter();
        if (writer==null)
            return;

        WorldClient world = Minecraft.getMinecraft().world;
        writer.print(Long.toString(now));
        writer.append(':');
        if (world==null) {
            writer.println(" no world");
            writer.flush();
            return;
        }
        List<Entity> entities = world.loadedEntityList;
        writer.append(Integer.toString(entities.size()));
        writer.append(" entities total. ");
        
        HashMap<Integer, String>strings=new HashMap<Integer, String>();
        int villagers=0;

        for (Entity e:entities) {
            if (e.posX>=confHandler.x1 && e.posX <= confHandler.x2+1
            &&  e.posY>=confHandler.y1 && e.posY <= confHandler.y2+1
            &&  e.posZ>=confHandler.z1 && e.posZ <= confHandler.z2+1
            ) {
                strings.put(e.getEntityId(), String.format("\t%6.2f %6.2f %6.2f %-40s", e.posX, e.posY, e.posZ, e.getClass().getSimpleName()));
                if (e instanceof EntityVillager)
                    villagers++;
            }
        }

        TreeSet<Integer> indexes = new TreeSet();
        indexes.addAll(strings.keySet());
        writer.print(indexes.size());
        writer.print(" in area, ");
        writer.print(villagers);
        writer.print(" villagers, ");
        for (Integer index:indexes)
            writer.append(strings.get(index));
        writer.append('\n');
        writer.flush();
        lastLogged=now;
    }
}
