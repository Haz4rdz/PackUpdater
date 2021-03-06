package me.jonathing.minecraft.packupdater.client;

import me.jonathing.minecraft.packupdater.PackUpdater;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod.EventBusSubscriber(Side.CLIENT)
public class ClientEventHandler
{
    public static boolean showGUI = PackUpdater.doesNeedUpdate();

    @SubscribeEvent
    public static void onOpenGui(final GuiOpenEvent event)
    {
        if (event.getGui() instanceof GuiMainMenu && showGUI)
        {
            event.setGui(new UpdateGUI(event.getGui()));
        }
    }
}
