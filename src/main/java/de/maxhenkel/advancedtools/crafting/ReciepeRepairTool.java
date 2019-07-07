package de.maxhenkel.advancedtools.crafting;

import de.maxhenkel.advancedtools.Main;
import de.maxhenkel.advancedtools.items.tools.AbstractTool;
import de.maxhenkel.advancedtools.items.tools.StackUtils;
import de.maxhenkel.advancedtools.items.tools.AdvancedToolMaterial;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.SpecialRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class ReciepeRepairTool extends SpecialRecipe {

    public ReciepeRepairTool(ResourceLocation idIn) {
        super(idIn);
    }

    @Override
    public boolean matches(CraftingInventory inv, World worldIn) {
        return !getCraftingResult(inv).equals(ItemStack.EMPTY);
    }

    @Override
    public ItemStack getCraftingResult(CraftingInventory inv) {
        ItemStack tool = null;
        List<ItemStack> otherItems = new ArrayList<>();
        for (int i = 0; i < inv.getSizeInventory(); i++) {
            ItemStack stack = inv.getStackInSlot(i);

            if (stack.getItem() instanceof AbstractTool) {
                if (tool != null) {
                    return ItemStack.EMPTY;
                }
                tool = stack;
            } else {
                if (!StackUtils.isEmpty(stack)) {
                    otherItems.add(stack);
                }
            }
        }

        if (tool == null) {
            return ItemStack.EMPTY;
        }


        AdvancedToolMaterial fMaterial = null;
        int fCount = 0;

        for (AdvancedToolMaterial mat : AdvancedToolMaterial.getAll()) {
            AdvancedToolMaterial material = null;
            int count = 0;
            for (ItemStack stack : otherItems) {
                if (mat.getMatcher().isMaterial(stack)) {
                    if (material != null) {
                        if (!material.equals(mat)) {
                            return ItemStack.EMPTY;
                        }
                    }
                    material = mat;
                    count++;
                } else {
                    break;
                }
            }
            if (material != null) {
                fMaterial = material;
                fCount = count;
                break;
            }
        }

        if (fMaterial == null) {
            return ItemStack.EMPTY;
        }

        return ((AbstractTool) tool.getItem()).repair(tool, fMaterial, fCount);
    }

    @Override
    public boolean canFit(int width, int height) {
        return width >= 2 && height >= 2;
    }

    @Override
    public ItemStack getRecipeOutput() {
        return ItemStack.EMPTY; //TODO maybe fix
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return Main.CRAFTING_REPAIR_TOOL;
    }

}
