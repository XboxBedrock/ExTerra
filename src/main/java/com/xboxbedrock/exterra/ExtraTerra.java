package com.xboxbedrock.exterra;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.apache.logging.log4j.Logger;

import com.xboxbedrock.exterra.proxy.CommonProxy;

import net.minecraft.client.Minecraft;
import net.minecraft.client.main.Main;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import com.xboxbedrock.exterra.region.Airocean;
import com.xboxbedrock.exterra.region.ProjectionTransform;
import com.xboxbedrock.exterra.region.ConformalEstimate;
import com.xboxbedrock.exterra.region.GeographicProjection;
import com.xboxbedrock.exterra.region.InvertableVectorField;
import com.xboxbedrock.exterra.region.InvertedOrientation;
import com.xboxbedrock.exterra.region.ModifiedAirocean;
import com.xboxbedrock.exterra.region.UprightOrientation;
import com.xboxbedrock.exterra.region.GeographicProjection.Orientation;
import com.xboxbedrock.exterra.region.ScaleProjection;






@Mod(modid = ExtraTerra.MODID, name = ExtraTerra.NAME, version = ExtraTerra.VERSION)
public class ExtraTerra
{
    public static final String MODID = "exterra";
    public static final String NAME = "ExtraTerra";
    public static final String VERSION = "1.0";
    public static final String ACCEPTED_VERSIONS = "[1.12.2]";
    public static final String CLIENT_PROXY_CLASS = "com.xboxbedrock.exterra.proxy.ClientProxy";
    public static final String COMMON_PROXY_CLASS = "com.xboxbedrock.exterra.proxy.CommonProxy";
    public static final double SCALE = 7318261.522857145;
    
    

    GeographicProjection projection = new ModifiedAirocean();
    GeographicProjection uprightProj = GeographicProjection.orientProjection(projection, GeographicProjection.Orientation.upright);
    ScaleProjection scaleProj = new ScaleProjection(uprightProj, SCALE, SCALE);
    @Mod.Instance
    public static ExtraTerra instance;

    @SidedProxy(clientSide = CLIENT_PROXY_CLASS, serverSide = COMMON_PROXY_CLASS)
    public static CommonProxy proxy;


    

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
    	System.out.println("isClient: " + FMLCommonHandler.instance().getEffectiveSide().isClient());
        if(FMLCommonHandler.instance().getEffectiveSide().isClient())
            MinecraftForge.EVENT_BUS.register(this);
        
    }
    @EventHandler
    public void PostInit(FMLPostInitializationEvent event)
    {
    }

    @SideOnly(value=Side.CLIENT)
    @SubscribeEvent
    public void renderOverlayEvent(RenderGameOverlayEvent.Text event) {
        EntityPlayer player = Minecraft.getMinecraft().player;
        double PositionX = player.posX;
        double PositionY = player.posY;
        double[] terracoords = scaleProj.toGeo(PositionX, PositionY);
        if(event.getType() == RenderGameOverlayEvent.ElementType.DEBUG)
        {
            event.getLeft().add("Long: " + terracoords[0] + "Lat: " + terracoords[1]);
        }
       
        //event.getLeft().add("Long: " + terracoords[0] + "Lat" + terracoords[1]);

        
    }
    @SubscribeEvent
    public void event(RenderGameOverlayEvent.Text event){
        if(Minecraft.getMinecraft().gameSettings.showDebugInfo){
            EntityPlayer player = Minecraft.getMinecraft().player;
            double PositionX = player.posX;
            double PositionY = player.posY;
            double[] terracoords = scaleProj.toGeo(PositionX, PositionY);
            List<String> left = event.getLeft();
            if(!left.isEmpty()){
                event.getLeft().add("");
            }
            event.getLeft().add("Long: " + terracoords[0] + " Lat: " + terracoords[1]);
           
        }
    }
    @SideOnly(value=Side.CLIENT)
    @SubscribeEvent
    public void renderOverlayEvent1(RenderGameOverlayEvent.Text event) {
        EntityPlayer player = Minecraft.getMinecraft().player;
        if(player.capabilities.isCreativeMode)
            return;

        
        Iterator<String> it = event.getLeft().listIterator();
        while (it.hasNext()) {
            String value = it.next();
            if (value != null && (value.startsWith("XYZ:") || value.startsWith("Chunk:") || value.startsWith("Block:") || value.startsWith("Facing:") || value.startsWith("Looking at:")))
                it.remove();
        }
    }
}


