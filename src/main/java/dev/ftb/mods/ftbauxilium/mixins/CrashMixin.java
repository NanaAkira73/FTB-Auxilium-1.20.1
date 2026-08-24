package dev.ftb.mods.ftbauxilium.mixins;

import dev.ftb.mods.ftbauxilium.ApiManager;
import net.minecraft.CrashReport;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.io.File;

@Mixin(CrashReport.class)
public class CrashMixin {
    @Inject(method = "saveToFile", at = @At("HEAD"))
    private void onSaveToFile(File file, CallbackInfoReturnable<Boolean> cir) {
        String path = file.getAbsoluteFile().toString();
        if (path.contains("crash-")) {
            ApiManager.INSTANCE.sendCrashReport(path);
        }
    }
}