package com.example.fastclickmod;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.client.ClientCommandHandler;

@Mod(modid = FastClickMod.MOD_ID, name = FastClickMod.MOD_NAME, version = FastClickMod.VERSION, acceptedMinecraftVersions = "[1.8.9]")
public class FastClickMod {
    public static final String MOD_ID = "fastclickmod";
    public static final String MOD_NAME = "Fast Click Mod";
    public static final String VERSION = "1.0.0";

    @Mod.Instance(MOD_ID)
    public static FastClickMod INSTANCE;

    private final ClickMultiplierController clickMultiplierController = new ClickMultiplierController();

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(clickMultiplierController);
        ClientCommandHandler.instance.registerCommand(new FastClickCommand(clickMultiplierController));
    }
}
