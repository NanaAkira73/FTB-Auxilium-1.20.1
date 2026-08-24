package dev.ftb.mods.ftbauxilium.forge;

import dev.ftb.mods.ftbauxilium.FTBAuxilium;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(FTBAuxilium.MOD_ID)
public class FTBAuxiliumForge {
    public FTBAuxiliumForge() {
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
            new FTBAuxilium();
        });
    }
}