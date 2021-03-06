package me.jonathing.minecraft.packupdater.client;

import me.jonathing.minecraft.packupdater.PackUpdater;
import me.jonathing.minecraft.packupdater.config.ConfigVariables;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiConfirmOpenLink;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.event.ClickEvent;

import javax.annotation.Nullable;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Iterator;
import java.util.List;

public class UpdateGUI extends GuiScreen
{
    private final GuiScreen parent;
    private GuiButton continueAnyway;
    private GuiButton goToCurseForge;

    public UpdateGUI(@Nullable GuiScreen parent)
    {
        this.parent = parent;
    }

    @Override
    public void initGui()
    {
        ScaledResolution sr = new ScaledResolution(this.mc);

        this.continueAnyway = new GuiButton(10, ((sr.getScaledWidth() / 2) - 90), 190, 180, 20, "Menu Principal");
        this.goToCurseForge = new GuiButton(11, ((sr.getScaledWidth() / 2) - 90), 160, 180, 20, "Baixar Atualizacao!");

        this.buttonList.add(this.continueAnyway);
        this.buttonList.add(this.goToCurseForge);

        super.initGui();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        String title = String.format("Atualizacao disponivel para %s!", ConfigVariables.modpackName);
        String[] description;

        description = (String.format("Voce tem a v%s, baixe e instale a nova versao v%s\n\n", ConfigVariables.currentVersion, PackUpdater.getNewVersion())
                + "Antes, lembre-se de salvar sua pasta JourneyMap em um local seguro e de\n"
                + "recoloca-la na pasta do modpack apos instalar a atualizacao!\n\n\n").split("\n");

        ScaledResolution screenRes = new ScaledResolution(this.mc);

        this.drawDefaultBackground();
        this.drawString(this.fontRenderer, title, (screenRes.getScaledWidth() / 2) - (this.fontRenderer.getStringWidth(title) / 2), 50, 0xFFFFFF);

        for (int i = 0; i < description.length; i++)
        {
            String str = description[i];

            this.drawString(this.fontRenderer, str, (screenRes.getScaledWidth() / 2) - (this.fontRenderer.getStringWidth(str) / 2), 70 + (i * 10), 0xFFFFFF);
        }

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException
    {
        if (button.id == this.continueAnyway.id)
        {
            ClientEventHandler.showGUI = false;

            this.mc.displayGuiScreen(this.parent);
        }

        if (button.id == this.goToCurseForge.id)
        {
            try
            {
                openWebLink(new URI(ConfigVariables.modpackUrl));
            }
            catch (URISyntaxException e)
            {
                PackUpdater.LOGGER.error("Unable to open the webpage! Maybe check your URL in config/packupdater.cfg?");
                e.printStackTrace();
            }
        }

        super.actionPerformed(button);
    }

    private void openWebLink(URI url)
    {
        try
        {
            Class<?> oclass = Class.forName("java.awt.Desktop");
            Object object = oclass.getMethod("getDesktop").invoke(null);
            oclass.getMethod("browse", URI.class).invoke(object, url);
        }
        catch (Throwable throwable)
        {
            Throwable e = throwable.getCause();
            PackUpdater.LOGGER.error("Couldn't open link: {}", e == null ? "<UNKNOWN>" : e.getMessage());
        }
    }
}
