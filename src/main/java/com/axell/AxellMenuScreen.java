package com.axell;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

/** Blue AXELL Client V4 menu. Open with the menu key (default Right Shift). */
public class AxellMenuScreen extends Screen {
    private static final int PANEL_WIDTH = 240;
    private static final int PANEL_HEIGHT = 230;
    private static final int BUTTON_WIDTH = 200;
    private static final int BUTTON_HEIGHT = 20;
    private static final int BUTTON_SPACING = 24;

    private ButtonWidget hudButton;
    private ButtonWidget fullbrightButton;
    private ButtonWidget waypointHudButton;

    public AxellMenuScreen() {
        super(Text.literal(AxellClient.NAME));
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

    @Override
    protected void init() {
        int x = this.width / 2 - BUTTON_WIDTH / 2;
        int y = this.height / 2 - 80;

        hudButton = addDrawableChild(ButtonWidget.builder(toggleLabel("HUD", AxellClient.hud), b -> {
            AxellClient.setHud(!AxellClient.hud);
            refresh();
        }).dimensions(x, y, BUTTON_WIDTH, BUTTON_HEIGHT).build());

        y += BUTTON_SPACING;
        fullbrightButton = addDrawableChild(ButtonWidget.builder(toggleLabel("Fullbright", AxellClient.fullbright), b -> {
            AxellClient.setFullbright(MinecraftClient.getInstance(), !AxellClient.fullbright);
            refresh();
        }).dimensions(x, y, BUTTON_WIDTH, BUTTON_HEIGHT).build());

        y += BUTTON_SPACING;
        waypointHudButton = addDrawableChild(ButtonWidget.builder(toggleLabel("Waypoint HUD", AxellClient.waypointHud), b -> {
            AxellClient.setWaypointHud(!AxellClient.waypointHud);
            refresh();
        }).dimensions(x, y, BUTTON_WIDTH, BUTTON_HEIGHT).build());

        y += BUTTON_SPACING;
        addDrawableChild(ButtonWidget.builder(Text.literal("Set Waypoint Here"), b -> {
            AxellClient.setWaypointHere(MinecraftClient.getInstance());
            refresh();
        }).dimensions(x, y, BUTTON_WIDTH, BUTTON_HEIGHT).build());

        y += BUTTON_SPACING;
        addDrawableChild(ButtonWidget.builder(Text.literal("Clear Waypoint"), b -> {
            AxellClient.clearWaypoint();
            refresh();
        }).dimensions(x, y, BUTTON_WIDTH, BUTTON_HEIGHT).build());

        y += BUTTON_SPACING + 8;
        addDrawableChild(ButtonWidget.builder(Text.literal("Done"), b -> this.close())
                .dimensions(x, y, BUTTON_WIDTH, BUTTON_HEIGHT).build());
    }

    private void refresh() {
        hudButton.setMessage(toggleLabel("HUD", AxellClient.hud));
        fullbrightButton.setMessage(toggleLabel("Fullbright", AxellClient.fullbright));
        waypointHudButton.setMessage(toggleLabel("Waypoint HUD", AxellClient.waypointHud));
    }

    private static Text toggleLabel(String name, boolean on) {
        return Text.literal(name + ": " + (on ? "ON" : "OFF"));
    }

    @Override
    public void render(DrawContext ctx, int mouseX, int mouseY, float delta) {
        // Dark blue backdrop, blue border, navy panel
        ctx.fill(0, 0, this.width, this.height, 0x99000814);
        int left = this.width / 2 - PANEL_WIDTH / 2;
        int top = this.height / 2 - PANEL_HEIGHT / 2;
        ctx.fill(left - 3, top - 3, left + PANEL_WIDTH + 3, top + PANEL_HEIGHT + 3, AxellClient.ACCENT);
        ctx.fill(left, top, left + PANEL_WIDTH, top + PANEL_HEIGHT, AxellClient.PANEL);

        ctx.drawCenteredTextWithShadow(this.textRenderer, Text.literal("AXELL Client V4"),
                this.width / 2, top + 10, AxellClient.TITLE);
        ctx.drawCenteredTextWithShadow(this.textRenderer, Text.literal("Keys: Options > Controls > AXELL Client V4"),
                this.width / 2, top + PANEL_HEIGHT - 14, 0xFF60A5FA);

        super.render(ctx, mouseX, mouseY, delta);
    }
                                           }
