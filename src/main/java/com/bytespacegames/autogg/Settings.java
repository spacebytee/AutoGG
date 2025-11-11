package com.bytespacegames.autogg;

import net.minecraft.client.Minecraft;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

public class Settings {
    private static final String DEFAULT_MESSAGE = "gg";
    private static final double DEFAULT_DELAY = 0;
    public double delay;
    public String message;

    public static Settings INSTANCE;
    public Settings() {
        INSTANCE = this;
        load();
    }
    public void load() {
        Path path = Paths.get(Minecraft.getInstance().gameDirectory.getAbsolutePath() + "/config/spaceautogg.config");
        Properties props = new Properties();
        if (Files.exists(path)) {
            try (InputStream in = Files.newInputStream(path)) {
                props.load(in);
                message = props.getProperty("message", DEFAULT_MESSAGE);
                delay = Double.parseDouble(props.getProperty("delay", DEFAULT_DELAY + ""));
                return;
            } catch (IOException | NumberFormatException e) {
                e.printStackTrace();
            }
        }
        delay = DEFAULT_DELAY;
        message = DEFAULT_MESSAGE;
    }

    public void save() {
        new Thread(() -> {
            Path path = Paths.get(Minecraft.getInstance().gameDirectory.getAbsolutePath() + "/config/spaceautogg.config");
            Properties props = new Properties();
            props.setProperty("message", message);
            props.setProperty("delay", String.valueOf(delay));

            try (OutputStream out = Files.newOutputStream(path)) {
                props.store(out, "AutoGG Settings");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }, "Settings-Save-Thread").start();
    }
}
