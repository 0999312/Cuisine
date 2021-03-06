package snownee.cuisine.plugins.top;

import mcjty.theoneprobe.api.*;
import mcjty.theoneprobe.apiimpl.styles.ProgressStyle;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.client.MinecraftForgeClient;
import snownee.cuisine.Cuisine;
import snownee.cuisine.CuisineRegistry;
import snownee.cuisine.blocks.BlockFirePit;
import snownee.cuisine.tiles.TileBasinHeatable;
import snownee.cuisine.tiles.TileFirePit;
import snownee.cuisine.util.I18nUtil;

import java.text.MessageFormat;

public class CuisineMachineProvider implements IProbeInfoProvider
{

    @Override
    public String getID()
    {
        return Cuisine.MODID + ":machine";
    }

    @Override
    public void addProbeInfo(ProbeMode mode, IProbeInfo probeInfo, EntityPlayer player, World world, IBlockState blockState, IProbeHitData data)
    {
        if (mode == ProbeMode.DEBUG && blockState.getBlock() instanceof BlockFirePit)
        {
            TileEntity tile = world.getTileEntity(data.getPos());
            if (tile instanceof TileFirePit)
            {
                TileFirePit tileFirePit = (TileFirePit) tile;
                probeInfo.text(TextStyleClass.LABEL + I18nUtil.translate("gui.burnTime", tileFirePit.heatHandler.getBurnTime()));
                probeInfo.text(TextStyleClass.LABEL + I18nUtil.translate("gui.heat", tileFirePit.heatHandler.getHeat()));
                // probeInfo.text(TextStyleClass.LABEL + I18nUtil.translate("gui.water_amount", tileWok.getWaterAmount()));
                // probeInfo.text(TextStyleClass.LABEL + I18nUtil.translate("gui.oil_amount", tileWok.getOilAmount()));
            }
        }
        else if (mode == ProbeMode.EXTENDED || mode == ProbeMode.DEBUG)
        {
            if (blockState.getBlock() == CuisineRegistry.EARTHEN_BASIN || blockState.getBlock() == CuisineRegistry.EARTHEN_BASIN_COLORED)
            {
                TileEntity tile = world.getTileEntity(data.getPos());
                if (tile instanceof TileBasinHeatable)
                {
                    TileBasinHeatable tileBasinHeatable = (TileBasinHeatable) tile;
                    MessageFormat formatter = new MessageFormat(I18nUtil.translate("gui.progress"), MinecraftForgeClient.getLocale());
                    if (tileBasinHeatable.isWorking())
                    {
                        int max = tileBasinHeatable.getMaxHeatingTick();
                        probeInfo.text(TextStyleClass.INFO + formatter.format(new Object[] { 2 })); // "Using TheOneProbe"
                        probeInfo.progress(max - tileBasinHeatable.getCurrentHeatingTick(), max, new ProgressStyle().showText(false));
                    }
                    else
                    {
                        probeInfo.text(TextStyleClass.INFO + formatter.format(new Object[] { -1 })); // "suspended"
                    }
                }
            }
        }

    }

}
