package de.craftsarmy.client.gui.screens;

import com.mojang.blaze3d.vertex.PoseStack;
import de.craftsarmy.Variables;
import de.craftsarmy.client.Client;
import de.craftsarmy.craftscore.core.Core;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.multiplayer.resolver.ServerAddress;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import org.jetbrains.annotations.NotNull;

public class DiscordScreen extends Screen {

    private EditBox stateEdit;
    private EditBox detailsEdit;
    private final Screen last;
    public DiscordRpcStatus status;

    public DiscordScreen(Screen last) {
        super(new TextComponent("Discord Einstellungen"));
        this.last = last;
    }

    @Override
    public void tick() {
        stateEdit.tick();
        detailsEdit.tick();
    }

    @Override
    protected void init() {
        if (Client.isInitialized()) {
            // Load / Prepare Config
            String state = "State";
            String details = "Details";
            if (!Client.clientConfig.contains("discord.rpc")) {
                Client.clientConfig.set("discord.rpc.state", "State");
                Client.clientConfig.set("discord.rpc.details", "Details");
                Client.clientConfig.set("discord.rpc.status", "");
                Client.clientConfig.save(Client.clientConfigFile);
                status = DiscordRpcStatus.DEFAULT;
            } else {
                state = Client.clientConfig.getString("discord.rpc.state");
                details = Client.clientConfig.getString("discord.rpc.details");
                status = DiscordRpcStatus.parse(Client.clientConfig.getString("discord.rpc.status"));
            }

            Client.minecraft.keyboardHandler.setSendRepeatsToGui(true);

            // State Edit Field
            this.stateEdit = new EditBox(this.font, this.width / 2 - 100, 66, 200, 20, new TextComponent("RPC State Text"));
            this.stateEdit.setFocus(true);
            this.stateEdit.setMaxLength(40);
            this.stateEdit.setValue(state);
            this.addWidget(this.stateEdit);

            // Details Edit Field
            this.detailsEdit = new EditBox(this.font, this.width / 2 - 100, 106, 200, 20, new TextComponent("RPC Details Text"));
            this.detailsEdit.setMaxLength(40);
            this.detailsEdit.setValue(details);
            this.addWidget(this.detailsEdit);

            // Change from Defautl to Custom CycleButton
            this.addRenderableWidget(
                    CycleButton.builder(DiscordRpcStatus::getName).a(DiscordRpcStatus.values()).withInitialValue(status).create(
                            this.width / 2 - 100,
                            this.height / 4 + 72,
                            200,
                            20,
                            new TextComponent("Anzeige Modus"),
                            (p_169299_, p_169300_) -> {
                                status = p_169300_;
                                if (status.equals(DiscordRpcStatus.DEFAULT)) {
                                    stateEdit.active = false;
                                    detailsEdit.active = false;
                                } else {
                                    stateEdit.active = true;
                                    detailsEdit.active = true;
                                }
                            }
                    )
            );

            // Save Button
            this.addRenderableWidget(
                    new Button(
                            this.width / 2 - 100,
                            this.height / 4 + 96 + 18,
                            200,
                            20,
                            CommonComponents.GUI_DONE,
                            p_96030_ -> this.onSave()
                    )
            );

            // Cancel Button
            this.addRenderableWidget(
                    new Button(
                            this.width / 2 - 100,
                            this.height / 4 + 120 + 18,
                            200,
                            20,
                            CommonComponents.GUI_CANCEL,
                            p_169297_ -> Client.minecraft.setScreen(last)
                    )
            );
        }
    }

    public void onSave() {
        Client.clientConfig.set("discord.rpc.state", stateEdit.getValue());
        Client.clientConfig.set("discord.rpc.details", detailsEdit.getValue());
        Client.clientConfig.set("discord.rpc.status", status.getTypeText());
        Client.clientConfig.save(Client.clientConfigFile);
        Client.minecraft.setScreen(last);
        if (Client.clientConfig.contains("discord.rpc") &&
                DiscordScreen.DiscordRpcStatus.parse(Client.clientConfig.getString("discord.rpc.status")).equals(DiscordScreen.DiscordRpcStatus.CUSTOM))
            Core.instance().getDiscordRPC().update(Client.clientConfig.getString("discord.rpc.state"), Client.clientConfig.getString("discord.rpc.details"), "", "");
        else if (Client.minecraft.isLocalServer())
            Core.instance().getDiscordRPC().update("Playing ratatata\n", "Singleplayer", "", "");
        else if (Variables.server) {
            ServerAddress address = Client.serverAddress;
            Core.instance().getDiscordRPC().update("Playing on " + address.getHost() + (address.getPort() == 25565 ? "" : ":" + address.getPort()), "Multiplayer", "", "");
        }
    }

    @Override
    public void resize(@NotNull Minecraft pMinecraft, int pWidth, int pHeight) {
        String s = stateEdit.getValue();
        String d = detailsEdit.getValue();
        init(pMinecraft, pWidth, pHeight);
        stateEdit.setValue(s);
        detailsEdit.setValue(d);
    }

    @Override
    public void removed() {
        Client.minecraft.keyboardHandler.setSendRepeatsToGui(false);
    }

    @Override
    public void onClose() {
        Client.minecraft.setScreen(last);
    }

    @Override
    public void render(PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
        renderBackground(pPoseStack);
        drawCenteredString(pPoseStack, font, title, width / 2, 17, 16777215);
        drawString(pPoseStack, font, new TextComponent("RPC State Text:"), this.width / 2 - 100, 53, 10526880);
        drawString(pPoseStack, font, new TextComponent("RPC Details Text:"), this.width / 2 - 100, 94, 10526880);
        this.stateEdit.render(pPoseStack, pMouseX, pMouseY, pPartialTick);
        this.detailsEdit.render(pPoseStack, pMouseX, pMouseY, pPartialTick);
        super.render(pPoseStack, pMouseX, pMouseY, pPartialTick);
    }

    public enum DiscordRpcStatus {

        DEFAULT("Default", "default"),
        CUSTOM("Benutzerdefiniert", "custom");

        private final Component name;
        private final String typeText;

        DiscordRpcStatus(String p_105399_, String p_105400_) {
            this.name = new TextComponent(p_105399_);
            this.typeText = p_105400_;
        }

        public Component getName() {
            return name;
        }

        public String getTypeText() {
            return typeText;
        }

        public static DiscordRpcStatus parse(String p_105401_) {
            for (DiscordRpcStatus status : DiscordRpcStatus.values())
                if (status.typeText.trim().equalsIgnoreCase(p_105401_.trim()))
                    return status;
            return null;
        }

    }

}
