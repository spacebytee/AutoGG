package com.bytespacegames.autogg;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class AutoGGScreen extends Screen {
    private EditBox textField;
    private DelaySlider delaySlider;

    protected AutoGGScreen() {
        super(Component.literal("AutoGG"));
    }
    protected void init() {
        int centerX = this.width / 2;
        int centerY = this.height / 2;
        textField = new EditBox(this.font, centerX - 75, centerY - 25, 150, 20, Component.literal("AutoGG message"));
        textField.setValue(Settings.INSTANCE.message);
        this.addRenderableWidget(textField);
        delaySlider = new DelaySlider(centerX - 75, centerY + 17, 150, 20, Settings.INSTANCE.delay/5d, 0, 5);
        this.addRenderableWidget(delaySlider);
    }
    public void render(GuiGraphics guiGraphics, int i, int j, float f) {
        int centerY = this.height / 2;
        int centerX = this.width / 2;
        super.render(guiGraphics, i, j, f);
        guiGraphics.drawCenteredString(Minecraft.getInstance().font, Component.literal("AutoGG Settings"), centerX, centerY-55, 0xFFFFFFFF);
        guiGraphics.drawString(this.font, Component.literal("GG Message"), centerX - 75, centerY - 38, 0xFFFFFFFF, true);
        guiGraphics.drawString(this.font, Component.literal("GG Delay (seconds)"), centerX - 75, centerY + 4, 0xFFFFFFFF,true);
    }
    public void onClose() {
        Settings.INSTANCE.message = textField.getValue();
        Settings.INSTANCE.save();
        super.onClose();
    }
    public boolean isPauseScreen() {
        return false;
    }
    private static class DelaySlider extends AbstractSliderButton {
        private final double min;
        private final double max;

        public DelaySlider(int x, int y, int width, int height, double value, double min, double max) {
            super(x, y, width, height, Component.literal(""), 0);
            this.min = min;
            this.max = max;
            this.value = value;
            this.updateMessage();
        }

        @Override
        protected void updateMessage() {
            this.setMessage(Component.literal(getValue() + "s"));
        }
        @Override
        protected void applyValue() {
            Settings.INSTANCE.delay = getValue();
        }
        public double getValue() {
            return Math.round((min + (max - min) * this.value) * 10d) / 10d;
        }
    }
}
