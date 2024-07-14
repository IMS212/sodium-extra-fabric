package me.flashyreese.mods.sodiumextra.mixin.sky;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexBuffer;
import me.flashyreese.mods.sodiumextra.client.SodiumExtraClientMod;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.ShaderInstance;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LevelRenderer.class)
public class MixinLevelRenderer {
    @Redirect(
            method = "renderSky",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/mojang/blaze3d/vertex/VertexBuffer;drawWithShader(Lorg/joml/Matrix4f;Lorg/joml/Matrix4f;Lnet/minecraft/client/renderer/ShaderInstance;)V",
                    ordinal = 0
            )
    )
    public void redirectSetSkyShader(VertexBuffer instance, Matrix4f viewMatrix, Matrix4f projectionMatrix, ShaderInstance program) {
        if (SodiumExtraClientMod.options().detailSettings.sky) {
            instance.drawWithShader(viewMatrix, projectionMatrix, program);
        }
    }

    @Inject(method = "renderEndSky", at = @At(value = "HEAD"), cancellable = true)
    public void preRenderEndSky(PoseStack stack, CallbackInfo ci) {
        if (!SodiumExtraClientMod.options().detailSettings.sky) {
            ci.cancel();
        }
    }
}
