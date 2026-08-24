package com.crispyraccoon.tabbackpacked.client;

import com.crispyraccoon.tabbackpacked.Tabbackpacked;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;

@EventBusSubscriber(modid = Tabbackpacked.MOD_ID, value = Dist.CLIENT)
public class ClientRegistry
{
    @SubscribeEvent
    public static void onRegisterKeyMappings(RegisterKeyMappingsEvent event)
    {
        event.register(Keys.KEY_PREVIOUS_BACKPACK);
        event.register(Keys.KEY_NEXT_BACKPACK);
    }
}
