package com.crispyraccoon.tabbackpacked.client;

import net.minecraft.client.KeyMapping;
import org.lwjgl.glfw.GLFW;

/**
 * 键位定义(与 Backpacked 的 Keys 类同构)。
 * 分类复用 "key.categories.backpacked":设置界面中与 Backpacked 的
 * "打开背包(B)"/"打开管理(V)" 同组显示。
 */
public class Keys
{
    public static final KeyMapping KEY_PREVIOUS_BACKPACK = new KeyMapping("key.tabbackpacked.previous", GLFW.GLFW_KEY_Z, "key.categories.backpacked");
    public static final KeyMapping KEY_NEXT_BACKPACK = new KeyMapping("key.tabbackpacked.next", GLFW.GLFW_KEY_C, "key.categories.backpacked");
}
