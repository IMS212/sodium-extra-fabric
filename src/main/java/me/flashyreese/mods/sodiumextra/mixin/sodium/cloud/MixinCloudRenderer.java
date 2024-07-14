package me.flashyreese.mods.sodiumextra.mixin.sodium.cloud;

import me.flashyreese.mods.sodiumextra.client.SodiumExtraClientMod;
import me.jellysquid.mods.sodium.client.render.immediate.CloudRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(CloudRenderer.class)
public class MixinCloudRenderer {
    @ModifyVariable(method = "render", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/client/Options;getEffectiveRenderDistance()I"), index = 21, name = "renderDistance")
    public int modifyCloudRenderDistance(int original) {
        return original * SodiumExtraClientMod.options().extraSettings.cloudDistance / 100;
    }
}