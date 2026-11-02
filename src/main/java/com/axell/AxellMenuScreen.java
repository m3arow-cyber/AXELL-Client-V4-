package com.axell;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

/** Blue AXELL Client V4 menu. Open with the menu key (default Right Shift). */
public class AxellMenuScreen extends Screen {
    private static final int PANEL_WIDTH = 240;
    private static final int PANEL_HEIGHT = 230;
    private static final int BUTTON_WIDTH = 200;
    private static final int BUTTON_HEIGHT = 20;
    private static final int BUTTON_SPACING = 24;

    private Button hudButton;
    private Button fullbrightButton;
    private Button waypointHudButton;

    public AxellMenuScreen() {
        super(Component.literal(AxellClient.NAME));
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

    @Override
    protected void init() {
        int x = this.width / 2 - BUTTON_WIDTH / 2;
        int y = this.height / 2 - 80;

        hudButton = addRenderableWidget(Button.builder(toggleLabel("HUD", AxellClient.hud), b -> {
            AxellClient.setHud(!AxellClient.hud);
            refresh();
        }).bounds(x, y, BUTTON_WIDTH, BUTTON_HEIGHT).build());

        y += BUTTON_SPACING;
        fullbrightButton = addRenderableWidget(Button.builder(toggleLabel("Fullbright", AxellClient.fullbright), b -> {
            AxellClient.setFullbright(Minecraft.getInstance(), !AxellClient.fullbright);
            refresh();
        }).bounds(x, y, BUTTON_WIDTH, BUTTON_HEIGHT).build());

        y += BUTTON_SPACING;
        waypointHudButton = addRenderableWidget(Button.builder(toggleLabel("Waypoint HUD", AxellClient.waypointHud), b -> {
            AxellClient.setWaypointHud(!AxellClient.waypointHud);
            refresh();
        }).bounds(x, y, BUTTON_WIDTH, BUTTON_HEIGHT).build());

        y += BUTTON_SPACING;
        addRenderableWidget(Button.builder(Component.literal("Set Waypoint Here"), b -> {
            AxellClient.setWaypointHere(Minecraft.getInstance());
            refresh();
        }).bounds(x, y, BUTTON_WIDTH, BUTTON_HEIGHT).build());

        y += BUTTON_SPACING;
        addRenderableWidget(Button.builder(Component.literal("Clear Waypoint"), b -> {
            AxellClient.clearWaypoint();
            refresh();
        }).bounds(x, y, BUTTON_WIDTH, BUTTON_HEIGHT).build());

        y += BUTTON_SPACING + 8;
        addRenderableWidget(Button.builder(Component.literal("Done"), b -> this.onClose())
                .bounds(x, y, BUTTON_WIDTH, BUTTON_HEIGHT).build());
    }

    private void refresh() {
        hudButton.setMessage(toggleLabel("HUD", AxellClient.hud));
        fullbrightButton.setMessage(toggleLabel("Fullbright", AxellClient.fullbright));
        waypointHudButton.setMessage(toggleLabel("Waypoint HUD", AxellClient.waypointHud));
    }

    private static Component toggleLabel(String name, boolean on) {
        return Component.literal(name + ": " + (on ? "ON" : "OFF"));
    }

    @Override
    public void render(GuiGraphics ctx, int mouseX, int mouseY, float delta) {
        // Dark blue backdrop, blue border, navy panel
        ctx.fill(0, 0, this.width, this.height, 0x99000814);
        int left = this.width / 2 - PANEL_WIDTH / 2;
        int top = this.height / 2 - PANEL_HEIGHT / 2;
        ctx.fill(left - 3, top - 3, left + PANEL_WIDTH + 3, top + PANEL_HEIGHT + 3, AxellClient.ACCENT);
        ctx.fill(left, top, left + PANEL_WIDTH, top + PANEL_HEIGHT, AxellClient.PANEL);

        ctx.drawCenteredString(this.font, Component.literal("AXELL Client V4"),
                this.width / 2, top + 10, AxellClient.TITLE);
        ctx.drawCenteredString(this.font, Component.literal("Keys: Options > Controls > AXELL Client V4"),
                this.width / 2, top + PANEL_HEIGHT - 14, 0xFF60A5FA);

        super.render(ctx, mouseX, mouseY, delta);
    }
}
