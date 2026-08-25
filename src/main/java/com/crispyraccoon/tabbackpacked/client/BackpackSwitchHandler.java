package com.crispyraccoon.tabbackpacked.client;

import com.crispyraccoon.tabbackpacked.Config;
import com.crispyraccoon.tabbackpacked.Tabbackpacked;
import com.mrcrayfish.backpacked.client.gui.screen.inventory.BackpackScreen;
import com.mrcrayfish.backpacked.client.gui.screen.widget.popup.PopupMenuHandler;
import com.mrcrayfish.backpacked.common.Pagination;
import com.mrcrayfish.backpacked.platform.ClientServices;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHandler;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.world.inventory.Slot;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ScreenEvent;

@EventBusSubscriber(modid = Tabbackpacked.MOD_ID, value = Dist.CLIENT)
public class BackpackSwitchHandler
{
    private static long lastNavigationTime = Long.MIN_VALUE;
    private static boolean pendingMouseRestore;
    private static double pendingMouseX;
    private static double pendingMouseY;

    @SubscribeEvent
    public static void onKeyPressed(ScreenEvent.KeyPressed.Pre event)
    {
        Screen screen = event.getScreen();
        if(!(screen instanceof BackpackScreen backpackScreen))
            return;
        if(!(screen instanceof PopupMenuHandler popup) || popup.hasPopupMenu())
            return;

        Pagination pagination = backpackScreen.getMenu().getPagination();
        if(pagination.totalPages() <= 1)
            return;

        int key = event.getKeyCode();
        int scanCode = event.getScanCode();
        if(Keys.KEY_PREVIOUS_BACKPACK.matches(key, scanCode) && pagination.currentPage() > 1 && canNavigate())
        {
            lastNavigationTime = Util.getMillis();
            captureMouse();
            pagination.previousPage();
            event.setCanceled(true);
        }
        else if(Keys.KEY_NEXT_BACKPACK.matches(key, scanCode) && pagination.currentPage() < pagination.totalPages() && canNavigate())
        {
            lastNavigationTime = Util.getMillis();
            captureMouse();
            pagination.nextPage();
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onMouseScrolled(ScreenEvent.MouseScrolled.Pre event)
    {
        Screen screen = event.getScreen();
        if(!(screen instanceof BackpackScreen backpackScreen))
            return;
        if(!(screen instanceof PopupMenuHandler popup) || popup.hasPopupMenu())
            return;

        Pagination pagination = backpackScreen.getMenu().getPagination();
        if(pagination.totalPages() <= 1)
            return;

        if(!isMouseInsideInventoryUi(backpackScreen, event.getMouseX(), event.getMouseY()))
            return;

        Slot hoveredSlot = backpackScreen.getSlotUnderMouse();
        if(hoveredSlot != null && hoveredSlot.hasItem())
            return;

        double scrollDelta = event.getScrollDeltaY();
        if(scrollDelta > 0 && pagination.currentPage() > 1 && canNavigate())
        {
            lastNavigationTime = Util.getMillis();
            captureMouse();
            pagination.previousPage();
            event.setCanceled(true);
        }
        else if(scrollDelta < 0 && pagination.currentPage() < pagination.totalPages() && canNavigate())
        {
            lastNavigationTime = Util.getMillis();
            captureMouse();
            pagination.nextPage();
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onScreenInitPost(ScreenEvent.Init.Post event)
    {
        if(pendingMouseRestore && event.getScreen() instanceof BackpackScreen)
        {
            ClientServices.CLIENT.setMousePos(pendingMouseX, pendingMouseY);
            pendingMouseRestore = false;
        }
    }

    private static void captureMouse()
    {
        MouseHandler handler = Minecraft.getInstance().mouseHandler;
        pendingMouseX = handler.xpos();
        pendingMouseY = handler.ypos();
        pendingMouseRestore = true;
    }

    private static boolean isMouseInsideInventoryUi(BackpackScreen screen, double mouseX, double mouseY)
    {
        int guiLeft = screen.getGuiLeft();
        int guiTop = screen.getGuiTop();
        return mouseX >= guiLeft && mouseX < guiLeft + screen.getXSize()
            && mouseY >= guiTop && mouseY < guiTop + screen.getYSize();
    }

    private static boolean canNavigate()
    {
        long now = Util.getMillis();
        int cooldownMs = Config.NAVIGATION_COOLDOWN_MS.getAsInt();
        return lastNavigationTime == Long.MIN_VALUE || now - lastNavigationTime >= cooldownMs;
    }
}
