package tsuteto.tofu.util;

import com.google.common.collect.Lists;
import cpw.mods.fml.relauncher.ReflectionHelper;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraftforge.oredict.OreDictionary;
import org.apache.logging.log4j.Level;
import tsuteto.tofu.Settings;
import tsuteto.tofu.TofuCraftCore;

import java.util.Iterator;
import java.util.List;

/**
 * Utilities for items
 */
public class ItemUtils
{
    /**
     * Unites items registered with each name in OreDictionary into a name
     * @param name
     * @param targets
     */
    public static void uniteOreItems(String name, String... targets)
    {
        List<ItemStack> itemsRegistered = Lists.newArrayList();

        for (String target : targets)
        {
            List<ItemStack> stacks = OreDictionary.getOres(target);
            for (ItemStack stack : stacks)
            {
                if (!containsItemStackList(stack, itemsRegistered))
                {
                    OreDictionary.registerOre(name, stack);
                    itemsRegistered.add(stack);
                }
            }
        }
    }

    /**
     * Get CreativeTabs list of a specific item/block
     * @param item
     * @return
     */
    public static CreativeTabs[] getCreativeTabs(Item item)
    {
        switch (Settings.creativeTab) {
        case ORIGINAL:
            return new CreativeTabs[]{ TofuCraftCore.tabTofuCraft };
        case SORTED:
            return new CreativeTabs[]{ item.getCreativeTab() };
        case BOTH:
        default:
            return new CreativeTabs[]{ item.getCreativeTab(), TofuCraftCore.tabTofuCraft };
        }
    }

    public static void tweakToolAttackDamage(ItemTool tool)
    {
        Float dmg = ReflectionHelper.getPrivateValue(ItemTool.class, tool, "field_77865_bY", "damageVsEntity");

        if (dmg < 0.0F)
        {
            ReflectionHelper.setPrivateValue(ItemTool.class, tool, 0.0F, "field_77865_bY", "damageVsEntity");
        }
    }

    public static ItemStack getItemStack(Object in)
    {
        if (in instanceof Item)
        {
            return new ItemStack((Item)in);
        }
        else if (in instanceof Block)
        {
            return new ItemStack((Block)in);
        }
        else if (in instanceof ItemStack)
        {
            return (ItemStack)in;
        }
        else
        {
            return null;
        }
    }

    public static boolean containsItemStackList(ItemStack stack, List<ItemStack> list)
    {
        for (ItemStack entry : list)
        {
            if (entry.isItemEqual(stack))
            {
                return true;
            }
        }
        return false;
    }

    /**
     * Get a specific item from IC2 by a field name, NOT unlocalized name
     * @param name
     * @return
     */
    public static Item getIc2Item(String name)
    {
        try
        {
            Class Ic2Items = Class.forName("ic2.core.Ic2Items");

            Object itemstack = Ic2Items.getField(name).get(null);

            if (itemstack instanceof ItemStack)
            {
                Item item = ((ItemStack) itemstack).getItem();
                return item;
            }
            else
            {
                return null;
            }
        }
        catch (Exception e)
        {
            ModLog.log(Level.WARN, e, "Failed to get IC2 item '" + name + "'");
            return null;
        }
    }

    /**
     * Get a specific item from external mods by unlocalized name
     * @param name
     * @return
     */
    public static Item getExternalModItem(String name)
    {
        return (Item)Item.itemRegistry.getObject(name);
    }

    /**
     * Get a specific item from external mods by matching regex with unlocalized name
     * @param subregex
     * @return
     */
    public static Item getExternalModItemWithRegex(String subregex)
    {
        String regex = "item\\." + subregex;
        Iterator<Item> itr = Item.itemRegistry.iterator();
        while (itr.hasNext())
        {
            Item item = itr.next();
            if (item.getUnlocalizedName() != null && item.getUnlocalizedName().matches(regex))
            {
                return item;
            }
        }
        ModLog.log(Level.WARN, "Failed to get external mod item with /" + regex + "/");
        return null;
    }

    /**
     * Get a specific block from external mods by matching regex with unlocalized name
     * @param subregex
     * @return
     */
    public static Block getExternalModBlockWithRegex(String subregex)
    {
        String regex = "tile\\." + subregex;
        Iterator<Block> itr = Block.blockRegistry.iterator();
        while (itr.hasNext())
        {
            Block block = itr.next();
            if (block.getUnlocalizedName() != null && block.getUnlocalizedName().matches(regex))
            {
                return block;
            }
        }
        ModLog.log(Level.WARN, "Failed to get external mod block with /" + regex + "/");
        return null;
    }
}