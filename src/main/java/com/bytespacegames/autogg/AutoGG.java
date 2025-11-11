package com.bytespacegames.autogg;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.api.ClientModInitializer;

import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.minecraft.client.Minecraft;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public class AutoGG implements ClientModInitializer {
	public static final String MOD_ID = "autogg";
	public static AutoGG INSTANCE;
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	private Minecraft mc;
	private boolean awaitingAutoGG = false;
	private boolean shouldOpenGui = false;
	private TimerUtils ggTimer = new TimerUtils();
	@Override
	public void onInitializeClient() {
		INSTANCE = this;
		new Settings();
		new RegexManager();
		mc = Minecraft.getInstance();

		ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
			LiteralArgumentBuilder cmd = ClientCommandManager.literal("autogg")
					.executes(this::openGui);
			dispatcher.register(cmd);
		});
	}
	public int openGui(CommandContext commandContext) {
		shouldOpenGui = true;
		return 1;
	}
	public void onTick() {
		if (mc == null) return;
		boolean validServer = mc.getCurrentServer() != null && !mc.getCurrentServer().ip.isEmpty();
		if (awaitingAutoGG && validServer && ggTimer.hasTimeElapsed((long) (Settings.INSTANCE.delay * 1000d), true)) {
			awaitingAutoGG = false;
			RegexManager.ServerConfig c = RegexManager.INSTANCE.getConfigByIP(mc.getCurrentServer().ip.trim());
			sendChatMessage(c.getPrefix() + Settings.INSTANCE.message);
		}
		if (shouldOpenGui && mc.level != null && mc.screen == null) {
			shouldOpenGui = false;
			mc.execute(() -> mc.setScreen(new AutoGGScreen()));
		}

	}
	private void sendChatMessage(String s) {
		if (this.mc.player == null) {
			(new Exception("poo")).printStackTrace();
			return;
		}
		if (s.length() > 200) {
			s = s.substring(0, 200);
		}
		if (s.startsWith("/")) {
			mc.player.connection.sendCommand(s.substring(1));
		} else {
			mc.player.connection.sendChat(s);
		}
	}
	public void handleChat(String s) {
		if (mc.getCurrentServer() == null) return;
		if (mc.getCurrentServer().ip.isEmpty()) return;
		RegexManager.ServerConfig c = RegexManager.INSTANCE.getConfigByIP(mc.getCurrentServer().ip.trim());
		if (c == null) return;
		boolean matched = false;
		for (RegexManager.Regex r : c.getList()) {
			if (s.matches(r.getMatch())) {
				if (r.getType() != 0) {
					return;
				}
				matched = true;
				break;
			}
		}
		if (matched) {
			if (Settings.INSTANCE.delay > 0) {
				ggTimer.reset();
				awaitingAutoGG = true;
				return;
			}
			sendChatMessage(c.getPrefix() + Settings.INSTANCE.message);
		}
	}
}