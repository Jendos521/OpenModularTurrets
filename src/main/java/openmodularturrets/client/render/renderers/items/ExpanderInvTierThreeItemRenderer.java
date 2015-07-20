package openmodularturrets.client.render.renderers.items;

import net.minecraft.item.ItemStack;
import net.minecraftforge.client.IItemRenderer;
import openmodularturrets.client.render.models.ModelExpander;
import openmodularturrets.client.render.renderers.blockitem.ExpanderInvTierThreeRenderer;
import openmodularturrets.tileentity.expander.ExpanderInvTierThreeTileEntity;
import org.lwjgl.opengl.GL11;

public class ExpanderInvTierThreeItemRenderer implements IItemRenderer {

    private final ExpanderInvTierThreeRenderer expanderInvTierThreeRenderer;
    private final ExpanderInvTierThreeTileEntity expanderInvTierThreeTileEntity;
    private final ModelExpander model;

    public ExpanderInvTierThreeItemRenderer(ExpanderInvTierThreeRenderer expanderInvTierThreeRenderer, ExpanderInvTierThreeTileEntity expanderInvTierThreeTileEntity) {
        this.expanderInvTierThreeRenderer = expanderInvTierThreeRenderer;
        this.expanderInvTierThreeTileEntity = expanderInvTierThreeTileEntity;
        this.model = new ModelExpander();
    }

    @Override
    public boolean handleRenderType(ItemStack item, ItemRenderType type) {
        return true;
    }

    @Override
    public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
        return true;
    }

    @Override
    public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
        GL11.glPushMatrix();
        GL11.glTranslated(-0.5, -0.5, -0.5);
        this.expanderInvTierThreeRenderer.renderTileEntityAt(this.expanderInvTierThreeTileEntity, 0.0D, 0.0D, 0.0D,
                0.0F);
        GL11.glPopMatrix();
    }
}