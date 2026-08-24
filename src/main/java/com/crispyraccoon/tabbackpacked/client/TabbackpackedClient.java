package com.crispyraccoon.tabbackpacked.client;

import com.crispyraccoon.tabbackpacked.Tabbackpacked;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

@Mod(value = Tabbackpacked.MOD_ID, dist = Dist.CLIENT)
public class TabbackpackedClient
{
    public TabbackpackedClient(ModContainer container)
    {
        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
    }
}
