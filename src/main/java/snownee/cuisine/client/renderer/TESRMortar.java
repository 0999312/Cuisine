package snownee.cuisine.client.renderer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import snownee.cuisine.tiles.TileMortar;

import java.util.List;

public class TESRMortar extends TileEntitySpecialRenderer<TileMortar>
{

    @Override
    public void render(TileMortar tile, double x, double y, double z, float partialTicks, int destroyStage, float alpha)
    {
        super.render(tile, x, y, z, partialTicks, destroyStage, alpha);
        Minecraft mc = Minecraft.getMinecraft();
        if (y > mc.player.eyeHeight)
        {
            return;
        }

        List<ItemStack> contents = tile.stacks.getStacks();
        if (contents.isEmpty())
        {
            return; // Skip rendering when there is nothing to render.
        }

        // Render the contents "inside" mortar, if any.
        GlStateManager.pushMatrix();

        GlStateManager.translate(x + 0.5F, y, z + 0.5F);

        double offsetY = 0.125;
        double scale = 1.0;
        for (ItemStack stack : contents)
        {
            offsetY = renderItem(stack, tile.getWorld(), offsetY, scale);
        }

        GlStateManager.popMatrix();
    }

    private double renderItem(ItemStack itemStack, World world, double offsetY, double scale)
    {
        if (!itemStack.isEmpty())
        {
            RenderItem renderItem = Minecraft.getMinecraft().getRenderItem();
            IBakedModel model = renderItem.getItemModelWithOverrides(itemStack, world, null);

            RenderHelper.enableStandardItemLighting();
            GlStateManager.enableLighting();

            GlStateManager.pushMatrix();

            if (model.isGui3d())
            {
                // Block
                GlStateManager.translate(0, offsetY, 0);
                GlStateManager.scale(.2f * scale, .2f * scale, .2f * scale);
                GlStateManager.rotate((float) (offsetY * 360), 0, 1, 0);
                offsetY += 0.15;
            }
            else
            {
                // Item
                GlStateManager.translate(offsetY * 5 % 0.1 - 0.05, offsetY - 0.05, offsetY * 3 % 0.1 - 0.05);
                GlStateManager.scale(.4f * scale, .4f * scale, .4f * scale);
                GlStateManager.rotate(270, 1, 0.15F, 0);
                offsetY += 0.03;
            }

            renderItem.renderItem(itemStack, ItemCameraTransforms.TransformType.NONE);

            GlStateManager.popMatrix();
        }

        return offsetY;
    }
}
