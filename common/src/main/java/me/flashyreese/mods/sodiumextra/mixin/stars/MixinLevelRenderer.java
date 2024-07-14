package me.flashyreese.mods.sodiumextra.mixin.stars;

import me.flashyreese.mods.sodiumextra.client.SodiumExtraClientMod;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.LevelRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(LevelRenderer.class)
public class MixinLevelRenderer {
    @Redirect(
            method = "renderSky",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/multiplayer/ClientLevel;getStarBrightness(F)F"
            )
    )
    public float redirectGetStarBrightness(ClientLevel instance, float f) {
        if (SodiumExtraClientMod.options().detailSettings.stars) {
            return instance.getStarBrightness(f);
        } else {
            return 0.0f;
        }
    }
}
