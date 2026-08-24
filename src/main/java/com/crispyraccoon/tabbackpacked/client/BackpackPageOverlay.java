package com.crispyraccoon.tabbackpacked.client;

import com.crispyraccoon.tabbackpacked.Tabbackpacked;
import com.mrcrayfish.backpacked.client.gui.screen.inventory.BackpackScreen;
import com.mrcrayfish.backpacked.client.gui.screen.widget.TitleWidget;
import com.mrcrayfish.backpacked.client.gui.screen.widget.popup.PopupMenuHandler;
import com.mrcrayfish.backpacked.common.Pagination;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ScreenEvent;

@EventBusSubscriber(modid = Tabbackpacked.MOD_ID, value = Dist.CLIENT)
public class BackpackPageOverlay
{
    private static final int TITLE_LABEL_WIDTH = 110;
    private static final int TITLE_PADDING = 5;
    private static final int TITLE_SHIFT_OWNER = 6;
    private static final int PAGE_GAP = 4;

    @SubscribeEvent
    public static void onRenderScreenPre(ScreenEvent.Render.Pre event)
    {
        Screen screen = event.getScreen();
        if(!(screen instanceof BackpackScreen backpackScreen))
            return;

        TitleWidget titleWidget = findTitleWidget(screen);
        if(titleWidget == null)
            return;

        int originalShift = backpackScreen.getMenu().isOwner() ? TITLE_SHIFT_OWNER : 0;
        if(shouldShowPage(screen))
        {
            titleWidget.setShift(computeShift(backpackScreen, Minecraft.getInstance().font));
        }
        else
        {
            titleWidget.setShift(originalShift);
        }
    }

    @SubscribeEvent
    public static void onRenderScreenPost(ScreenEvent.Render.Post event)
    {
        Screen screen = event.getScreen();
        if(!(screen instanceof BackpackScreen backpackScreen))
            return;
        if(!shouldShowPage(screen))
            return;

        TitleWidget titleWidget = findTitleWidget(screen);
        if(titleWidget == null)
            return;

        Font font = Minecraft.getInstance().font;
        int nameWidth = visibleTitleWidth(font, screen.getTitle());
        int shift = computeShift(backpackScreen, font);
        int titleX = titleWidget.getX() + (TITLE_LABEL_WIDTH - nameWidth) / 2 + shift;
        int pageX = titleX + nameWidth + PAGE_GAP;
        int pageY = titleWidget.getY() + 1;

        GuiGraphics graphics = event.getGuiGraphics();
        graphics.drawString(font, pageComponent(backpackScreen.getMenu().getPagination()), pageX, pageY, 0xFF61503D, false);
    }

    private static boolean shouldShowPage(Screen screen)
    {
        if(!(screen instanceof BackpackScreen backpackScreen))
            return false;
        if(screen instanceof PopupMenuHandler popup && popup.hasPopupMenu())
            return false;
        return backpackScreen.getMenu().getPagination().totalPages() > 1;
    }

    private static TitleWidget findTitleWidget(Screen screen)
    {
        for(Renderable renderable : screen.renderables)
        {
            if(renderable instanceof TitleWidget titleWidget)
                return titleWidget;
        }
        return null;
    }

    private static int computeShift(BackpackScreen screen, Font font)
    {
        int pageWidth = font.width(pageComponent(screen.getMenu().getPagination()));
        int originalShift = screen.getMenu().isOwner() ? TITLE_SHIFT_OWNER : 0;
        return originalShift - (pageWidth + PAGE_GAP) / 2;
    }

    private static int visibleTitleWidth(Font font, Component title)
    {
        int maxWidth = TITLE_LABEL_WIDTH - TITLE_PADDING * 2;
        int width = font.width(title);
        if(width > maxWidth)
        {
            return font.width(font.substrByWidth(title, maxWidth - font.width("..."))) + font.width("...");
        }
        return width;
    }

    private static Component pageComponent(Pagination pagination)
    {
        return Component.literal("(" + pagination.currentPage() + "/" + pagination.totalPages() + ")");
    }
}
