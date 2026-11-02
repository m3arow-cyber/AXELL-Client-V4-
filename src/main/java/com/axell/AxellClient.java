package com.axell;

import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.world.phys.Vec3;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

/**
 * AXELL Client V4 - client-side utility mod.
 * Features: blue menu, HUD, fullbright, zoom, waypoint marker.
 * Every feature has its own rebindable key (Options > Controls > AXELL Client V4).
 */
public class AxellClient implements ClientModInitializer {
    public static final String NAME = "AXELL Client V4";

    // Blue theme (ARGB)
    public static final int ACCENT = 0xFF3B82F6;
    public static final int PANEL = 0xE00A1F44;
    public static final int TITLE = 0xFF93C5FD;
    private static final int HUD_BG = 0xB00A1F44;
    private static final int TEXT_WHITE = 0xFFFFFFFF;

    private static final int ZOOM_FOV = 30;

    private static final KeyMapping.Category CATEGORY =
            KeyMapping.Category.register(ResourceLocation.fromNamespaceAndPath("axell", "main"));

    // Feature state
    public static boolean hud = true;
    public static boolean fullbright = false;
    public static boolean waypointHud = true;
    public static BlockPos waypoint = null;

    private static Double savedGamma = null;
    private static Integer savedFov = null;

    private static KeyMapping menuKey;
    private static KeyMapping hudKey;
    private static KeyMapping fullbrightKey;
    private static KeyMapping zoomKey;
    private static KeyMapping waypointKey;

    @Override
    public void onInitializeClient() {
        menuKey = register("key.axell.menu", GLFW.GLFW_KEY_RIGHT_SHIFT);
        hudKey = register("key.axell.hud", GLFW.GLFW_KEY_K);
        fullbrightKey = register("key.axell.fullbright", GLFW.GLFW_KEY_L);
        zoomKey = register("key.axell.zoom", GLFW.GLFW_KEY_Z);
        waypointKey = register("key.axell.waypoint", GLFW.GLFW_KEY_N);

        ClientTickEvents.END_CLIENT_TICK.register(AxellClient::onTick);
        HudRenderCallback.EVENT.register((ctx, tickCounter) -> renderHud(ctx));
        ClientLifecycleEvents.CLIENT_STOPPING.register(mc -> setFullbright(mc, false));
    }

    private static KeyMapping register(String translationKey, int defaultKey) {
        return KeyBindingHelper.registerKeyBinding(
                new KeyMapping(translationKey, InputConstants.Type.KEYSYM, defaultKey, CATEGORY));
    }

    private static void onTick(Minecraft mc) {
        while (menuKey.consumeClick()) {
            if (mc.screen == null) {
                mc.setScreen(new AxellMenuScreen());
            }
        }
        while (hudKey.consumeClick()) {
            setHud(!hud);
        }
        while (fullbrightKey.consumeClick()) {
            setFullbright(mc, !fullbright);
        }
        while (waypointKey.consumeClick()) {
            setWaypointHere(mc);
        }

        // Zoom: active only while the key is held and no screen is open
        boolean zooming = zoomKey.isDown() && mc.screen == null;
        if (zooming && savedFov == null) {
            savedFov = mc.options.fov().get();
            mc.options.fov().set(ZOOM_FOV);
        } else if (!zooming && savedFov != null) {
            mc.options.fov().set(savedFov);
            savedFov = null;
        }
    }

    // ---- Feature setters (used by the keys and the menu) ----

    public static void setHud(boolean on) {
        hud = on;
    }

    public static void setWaypointHud(boolean on) {
        waypointHud = on;
    }

    public static void setFullbright(Minecraft mc, boolean on) {
        if (on == fullbright) {
            return;
        }
        if (on) {
            savedGamma = mc.options.gamma().get();
            mc.options.gamma().set(1.0);
        } else if (savedGamma != null) {
            mc.options.gamma().set(savedGamma);
            savedGamma = null;
        }
        fullbright = on;
    }

    public static void setWaypointHere(Minecraft mc) {
        if (mc.player != null) {
            waypoint = mc.player.blockPosition();
        }
    }

    public static void clearWaypoint() {
        waypoint = null;
    }

    // ---- HUD ----

    private static void renderHud(GuiGraphics ctx) {
        Minecraft mc = Minecraft.getInstance();
        if (!hud || mc.options.hideGui || mc.player == null || mc.font == null) {
            return;
        }

        List<String> lines = new ArrayList<>();
        lines.add(NAME);
        lines.add("FPS: " + mc.getFps());
        lines.add(String.format("XYZ: %.1f %.1f %.1f",
                mc.player.getX(), mc.player.getY(), mc.player.getZ()));
        if (waypoint != null && waypointHud) {
            double dist = mc.player.position().distanceTo(Vec3.atCenterOf(waypoint));
            lines.add(String.format("Waypoint: %d %d %d (%.0f m)",
                    waypoint.getX(), waypoint.getY(), waypoint.getZ(), dist));
        }

        int width = 0;
        for (String line : lines) {
            width = Math.max(width, mc.font.width(line));
        }
        int height = lines.size() * 10 + 6;

        ctx.fill(4, 4, 14 + width, 4 + height, HUD_BG);
        ctx.fill(4, 4, 6, 4 + height, ACCENT);

        int y = 7;
        for (int i = 0; i < lines.size(); i++) {
            int color = (i == 0) ? TITLE : TEXT_WHITE;
            ctx.drawString(mc.font, lines.get(i), 10, y, color, true);
            y += 10;
        }
    }
                                     }
