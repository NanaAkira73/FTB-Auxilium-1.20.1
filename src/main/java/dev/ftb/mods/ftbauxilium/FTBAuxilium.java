package dev.ftb.mods.ftbauxilium;

import com.mojang.logging.LogUtils;
import dev.ftb.mods.ftbauxilium.integration.FTBAuxiliumKubeJSPlugin;
import net.minecraftforge.fml.common.Mod;
import org.slf4j.Logger;

@Mod(FTBAuxilium.MOD_ID)
public class FTBAuxilium {
    public static final String MOD_ID = "ftbauxilium";
    public static final Logger LOGGER = LogUtils.getLogger();

    public FTBAuxilium() {
        LOGGER.info("FTB Auxilium loaded");
        FTBAuxiliumKubeJSPlugin.init();
    }
}