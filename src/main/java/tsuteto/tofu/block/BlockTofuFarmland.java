package tsuteto.tofu.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.util.ForgeDirection;
import tsuteto.tofu.material.TcMaterial;

import java.util.Random;

public class BlockTofuFarmland extends TcBlock
{
    @SideOnly(Side.CLIENT)
    private IIcon field_149824_a;
    @SideOnly(Side.CLIENT)
    private IIcon field_149823_b;

    protected BlockTofuFarmland()
    {
        super(TcMaterial.tofu);
        this.setTickRandomly(true);
        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.9375F, 1.0F);
        this.setLightOpacity(255);
    }

    @Override
    public boolean canSustainPlant(IBlockAccess world, int x, int y, int z, ForgeDirection direction, IPlantable plantable)
    {
        Block plant = plantable.getPlant(world, x, y + 1, z);
        if (plant == TcBlocks.soybean)
        {
            return true;
        }
        else
        {
            return super.canSustainPlant(world, x, y, z, direction, plantable);
        }
    }

    public boolean isFertile(World world, int x, int y, int z)
    {
        return world.getBlockMetadata(x, y, z) > 0;
    }

    /**
     * Returns a bounding box from the pool of bounding boxes (this means this box can change after the pool has been
     * cleared to be reused)
     */
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World p_149668_1_, int p_149668_2_, int p_149668_3_, int p_149668_4_)
    {
        return AxisAlignedBB.getBoundingBox((double)(p_149668_2_), (double)(p_149668_3_), (double)(p_149668_4_), (double)(p_149668_2_ + 1), (double)(p_149668_3_ + 1), (double)(p_149668_4_ + 1));
    }

    /**
     * Is this block (a) opaque and (b) a full 1m cube?  This determines whether or not to render the shared face of two
     * adjacent blocks and also whether the player can attach torches, redstone wire, etc to this block.
     */
    public boolean isOpaqueCube()
    {
        return false;
    }

    /**
     * If this block doesn't render as an ordinary block it will return False (examples: signs, buttons, stairs, etc)
     */
    public boolean renderAsNormalBlock()
    {
        return false;
    }

    /**
     * Gets the block's texture. Args: side, meta
     */
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int p_149691_1_, int p_149691_2_)
    {
        return p_149691_1_ == 1 ? (p_149691_2_ > 0 ? this.field_149824_a : this.field_149823_b) : TcBlocks.tofuMomen.getBlockTextureFromSide(p_149691_1_);
    }

    /**
     * Ticks the block if it's been scheduled
     */
    public void updateTick(World p_149674_1_, int p_149674_2_, int p_149674_3_, int p_149674_4_, Random p_149674_5_)
    {
        if (!this.func_149821_m(p_149674_1_, p_149674_2_, p_149674_3_, p_149674_4_) && !p_149674_1_.canLightningStrikeAt(p_149674_2_, p_149674_3_ + 1, p_149674_4_))
        {
            int l = p_149674_1_.getBlockMetadata(p_149674_2_, p_149674_3_, p_149674_4_);

            if (l > 0)
            {
                p_149674_1_.setBlockMetadataWithNotify(p_149674_2_, p_149674_3_, p_149674_4_, l - 1, 2);
            }
            else if (!this.func_149822_e(p_149674_1_, p_149674_2_, p_149674_3_, p_149674_4_))
            {
                p_149674_1_.setBlock(p_149674_2_, p_149674_3_, p_149674_4_, TcBlocks.tofuMomen);
            }
        }
        else
        {
            p_149674_1_.setBlockMetadataWithNotify(p_149674_2_, p_149674_3_, p_149674_4_, 7, 2);
        }
    }

    /**
     * Block's chance to react to an entity falling on it.
     */
    public void onFallenUpon(World p_149746_1_, int p_149746_2_, int p_149746_3_, int p_149746_4_, Entity p_149746_5_, float p_149746_6_)
    {
        if (!p_149746_1_.isRemote && p_149746_1_.rand.nextFloat() < p_149746_6_ - 0.5F)
        {
            if (!(p_149746_5_ instanceof EntityPlayer) && !p_149746_1_.getGameRules().getGameRuleBooleanValue("mobGriefing"))
            {
                return;
            }

            p_149746_1_.setBlock(p_149746_2_, p_149746_3_, p_149746_4_, TcBlocks.tofuMomen);
        }
    }

    private boolean func_149822_e(World p_149822_1_, int p_149822_2_, int p_149822_3_, int p_149822_4_)
    {
        byte b0 = 0;

        for (int l = p_149822_2_ - b0; l <= p_149822_2_ + b0; ++l)
        {
            for (int i1 = p_149822_4_ - b0; i1 <= p_149822_4_ + b0; ++i1)
            {
                Block block = p_149822_1_.getBlock(l, p_149822_3_ + 1, i1);

                if (block instanceof IPlantable && canSustainPlant(p_149822_1_, p_149822_2_, p_149822_3_, p_149822_4_, ForgeDirection.UP, (IPlantable)block))
                {
                    return true;
                }
            }
        }

        return false;
    }

    private boolean func_149821_m(World p_149821_1_, int p_149821_2_, int p_149821_3_, int p_149821_4_)
    {
        for (int l = p_149821_2_ - 4; l <= p_149821_2_ + 4; ++l)
        {
            for (int i1 = p_149821_3_; i1 <= p_149821_3_ + 1; ++i1)
            {
                for (int j1 = p_149821_4_ - 4; j1 <= p_149821_4_ + 4; ++j1)
                {
                    if (p_149821_1_.getBlock(l, i1, j1).getMaterial() == Material.water)
                    {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    /**
     * Lets the block know when one of its neighbor changes. Doesn't know which neighbor changed (coordinates passed are
     * their own) Args: x, y, z, neighbor Block
     */
    public void onNeighborBlockChange(World p_149695_1_, int p_149695_2_, int p_149695_3_, int p_149695_4_, Block p_149695_5_)
    {
        super.onNeighborBlockChange(p_149695_1_, p_149695_2_, p_149695_3_, p_149695_4_, p_149695_5_);
        Material material = p_149695_1_.getBlock(p_149695_2_, p_149695_3_ + 1, p_149695_4_).getMaterial();

        if (material.isSolid())
        {
            p_149695_1_.setBlock(p_149695_2_, p_149695_3_, p_149695_4_, TcBlocks.tofuMomen);
        }
    }

    public Item getItemDropped(int p_149650_1_, Random p_149650_2_, int p_149650_3_)
    {
        return TcBlocks.tofuMomen.getItemDropped(0, p_149650_2_, p_149650_3_);
    }

    /**
     * Gets an item for the block being called on. Args: world, x, y, z
     */
    @SideOnly(Side.CLIENT)
    public Item getItem(World p_149694_1_, int p_149694_2_, int p_149694_3_, int p_149694_4_)
    {
        return Item.getItemFromBlock(TcBlocks.tofuMomen);
    }

    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister p_149651_1_)
    {
        this.field_149824_a = p_149651_1_.registerIcon(this.getTextureName() + "_wet");
        this.field_149823_b = p_149651_1_.registerIcon(this.getTextureName() + "_dry");
    }
}