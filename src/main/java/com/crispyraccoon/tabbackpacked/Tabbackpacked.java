package com.crispyraccoon.tabbackpacked;

import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;

@Mod(Tabbackpacked.MOD_ID)
public class Tabbackpacked
{
    public static final String MOD_ID = "tabbackpacked";

    public Tabbackpacked(ModContainer container)
    {
        container.registerConfig(ModConfig.Type.CLIENT, Config.SPEC);
    }
}
