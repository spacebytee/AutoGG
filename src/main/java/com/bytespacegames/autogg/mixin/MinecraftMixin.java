package com.bytespacegames.autogg.mixin;

import com.bytespacegames.autogg.AutoGG;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class MinecraftMixin {
    @Inject(method="tick", at = @At("HEAD"))
    public void mixin$tick(CallbackInfo ci) {
        if (AutoGG.INSTANCE != null) AutoGG.INSTANCE.onTick();
    }
}
