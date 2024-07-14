package me.flashyreese.mods.sodiumextra.mixin.sodium.resolution;

import com.mojang.blaze3d.platform.VideoMode;
import com.mojang.blaze3d.platform.Window;
import me.flashyreese.mods.sodiumextra.client.gui.options.control.SliderControlExtended;
import me.flashyreese.mods.sodiumextra.common.util.ControlValueFormatterExtended;
import me.jellysquid.mods.sodium.client.gui.SodiumGameOptionPages;
import me.jellysquid.mods.sodium.client.gui.options.OptionGroup;
import me.jellysquid.mods.sodium.client.gui.options.OptionImpact;
import me.jellysquid.mods.sodium.client.gui.options.OptionImpl;
import me.jellysquid.mods.sodium.client.gui.options.OptionPage;
import me.jellysquid.mods.sodium.client.gui.options.storage.MinecraftOptionsStorage;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;
import java.util.Optional;

@Mixin(value = SodiumGameOptionPages.class, remap = false)
public class MixinSodiumGameOptionPages {

    @Shadow
    @Final
    private static MinecraftOptionsStorage vanillaOpts;

    @Inject(method = "general", at = @At(value = "INVOKE", target = "Lme/jellysquid/mods/sodium/client/gui/options/OptionGroup;createBuilder()Lme/jellysquid/mods/sodium/client/gui/options/OptionGroup$Builder;", ordinal = 1, shift = At.Shift.BEFORE), locals = LocalCapture.CAPTURE_FAILSOFT, remap = false)
    private static void general(CallbackInfoReturnable<OptionPage> cir, List<OptionGroup> groups) {
        Window window = Minecraft.getInstance().getWindow();

        groups.add(OptionGroup.createBuilder()
                .add(OptionImpl.createBuilder(int.class, vanillaOpts)
                        .setName(Component.translatable("options.fullscreen.resolution"))
                        .setTooltip(Component.translatable("sodium-extra.option.resolution.tooltip"))
                        .setControl(option -> new SliderControlExtended(option, 0, window.findBestMonitor() != null ? window.findBestMonitor().getModeCount() : 0, 1, ControlValueFormatterExtended.resolution(), false))
                        .setBinding((options, value) -> {
                            if (window.findBestMonitor() != null) {
                                if (value == 0) {
                                    window.setPreferredFullscreenVideoMode(Optional.empty());
                                } else {
                                    window.setPreferredFullscreenVideoMode(Optional.of(window.findBestMonitor().getMode(value - 1)));
                                }
                            }
                            window.changeFullscreenVideoMode();
                        }, options -> {
                            if (window.findBestMonitor() == null) {
                                return 0;
                            } else {
                                Optional<VideoMode> optional = window.getPreferredFullscreenVideoMode();
                                return optional.map((videoMode) -> window.findBestMonitor().getVideoModeIndex(videoMode) + 1).orElse(0);
                            }
                        })
                        .setImpact(OptionImpact.HIGH)
                        .build())
                .build());
    }
}
