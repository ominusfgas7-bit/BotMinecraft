package com.example.fastclickmod;

import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Mouse;

import java.util.Random;

public class ClickMultiplierController {
    private final Random random = new Random();
    private volatile boolean enabled = false;
    private volatile int extraLeftClicks = 0;
    private volatile int extraRightClicks = 0;

    private boolean prevLeftPressed = false;
    private boolean prevRightPressed = false;

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) {
            return;
        }

        Minecraft mc = Minecraft.getMinecraft();
        if (mc.thePlayer == null || mc.currentScreen != null || !enabled) {
            prevLeftPressed = Mouse.isButtonDown(0);
            prevRightPressed = Mouse.isButtonDown(1);
            return;
        }

        boolean currentLeftPressed = Mouse.isButtonDown(0);
        boolean currentRightPressed = Mouse.isButtonDown(1);

        if (currentLeftPressed && !prevLeftPressed && extraLeftClicks > 0) {
            scheduleExtraClicks(0, extraLeftClicks);
        }

        if (currentRightPressed && !prevRightPressed && extraRightClicks > 0) {
            scheduleExtraClicks(1, extraRightClicks);
        }

        prevLeftPressed = currentLeftPressed;
        prevRightPressed = currentRightPressed;
    }

    private void scheduleExtraClicks(final int button, final int clickCount) {
        Thread clickThread = new Thread(() -> {
            Minecraft mc = Minecraft.getMinecraft();
            for (int i = 0; i < clickCount; i++) {
                int delayMs = 15 + random.nextInt(16);
                try {
                    Thread.sleep(delayMs);
                } catch (InterruptedException ignored) {
                    Thread.currentThread().interrupt();
                    return;
                }

                mc.addScheduledTask(() -> {
                    if (button == 0) {
                        mc.clickMouse();
                    } else {
                        mc.rightClickMouse();
                    }
                });
            }
        }, "FastClickMod-Button-" + button);

        clickThread.setDaemon(true);
        clickThread.start();
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public int getExtraLeftClicks() {
        return extraLeftClicks;
    }

    public void setExtraLeftClicks(int extraLeftClicks) {
        this.extraLeftClicks = clampClicks(extraLeftClicks);
    }

    public int getExtraRightClicks() {
        return extraRightClicks;
    }

    public void setExtraRightClicks(int extraRightClicks) {
        this.extraRightClicks = clampClicks(extraRightClicks);
    }

    private int clampClicks(int value) {
        return Math.max(0, Math.min(50, value));
    }
}
