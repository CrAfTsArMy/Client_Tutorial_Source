package de.craftsarmy.client.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import de.craftsarmy.client.Client;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;

public class WelcomeScreen extends Screen {

    public WelcomeScreen() {
        super(new TextComponent("Willkommen, " + Client.minecraft.getUser().getName()));
    }

    protected void init() {
        this.addRenderableWidget(new Button(this.width / 2 - 155, this.height / 4 + 120 + 12, 150, 20, new TranslatableComponent("gui.toTitle"), (p_96304_) ->
        {
            this.minecraft.setScreen(new TitleScreen());
        }));
        this.addRenderableWidget(new Button(this.width / 2 - 155 + 160, this.height / 4 + 120 + 12, 150, 20, new TranslatableComponent("menu.quit"), (p_96300_) ->
        {
            this.minecraft.stop();
        }));
    }

    public boolean shouldCloseOnEsc() {
        return false;
    }

    public void render(PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
        this.renderBackground(pPoseStack);
        drawCenteredString(pPoseStack, this.font, this.title, this.width / 2, this.height / 4 - 60 + 20, 16777215);
        drawString(pPoseStack, this.font, "Dies ist der YouTube Tutorial Client.", this.width / 2 - 140, this.height / 4 - 60 + 60 + 0, 10526880);
        super.render(pPoseStack, pMouseX, pMouseY, pPartialTick);
    }

}
