package com.example.lobbymenu;

import com.mojang.logging.LogUtils;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(LobbyMenuMod.MOD_ID)
@Mod.EventBusSubscriber(modid = LobbyMenuMod.MOD_ID, value = Dist.CLIENT)
public class LobbyMenuMod {

    public static final String MOD_ID = "lobbymenu";
    public static final Logger LOGGER = LogUtils.getLogger();

    public LobbyMenuMod() {
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();

        // Регистрация клавиши
        modBus.addListener(KeyHandler::onRegisterKeys);

        // Клиентские события (тик для отслеживания нажатий)
        MinecraftForge.EVENT_BUS.register(new KeyHandler());
    }
}