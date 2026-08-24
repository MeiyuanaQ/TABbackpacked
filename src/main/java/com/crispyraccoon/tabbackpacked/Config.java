package com.crispyraccoon.tabbackpacked;

import net.neoforged.neoforge.common.ModConfigSpec;

public class Config
{
    public static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec.IntValue NAVIGATION_COOLDOWN_MS = BUILDER
            .comment("Minimum interval between backpack page switches, in milliseconds.")
            .defineInRange("navigationCooldownMs", 100, 1, 1000);

    public static final ModConfigSpec SPEC = BUILDER.build();
}
