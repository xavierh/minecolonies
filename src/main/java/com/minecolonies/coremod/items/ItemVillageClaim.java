package com.minecolonies.coremod.items;

import com.minecolonies.coremod.achievements.ModAchievements;
import com.minecolonies.coremod.blocks.ModBlocks;
import com.minecolonies.coremod.colony.Colony;
import com.minecolonies.coremod.colony.buildings.BuildingTownHall;
import com.minecolonies.coremod.colony.IColony;
import com.minecolonies.coremod.colony.ColonyManager;
import com.minecolonies.coremod.configuration.Configurations;
import com.minecolonies.coremod.creativetab.ModCreativeTabs;
import com.minecolonies.coremod.lib.Constants;
import com.minecolonies.coremod.util.LanguageHandler;
import com.minecolonies.coremod.util.Log;
import com.minecolonies.coremod.util.BlockPosUtil;
import com.minecolonies.coremod.util.StructureWrapper;
import net.minecraft.block.BlockChest;
import net.minecraft.init.Blocks;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Mirror;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Class to handle the placement of the village claim.
 */
public class ItemVillageClaim extends AbstractItemMinecolonies
{
    public ItemVillageClaim()
    {
        super("blockvillageclaim");

        super.setCreativeTab(ModCreativeTabs.MINECOLONIES);
    }

    @Override
    public EnumActionResult onItemUse(
            final EntityPlayer player,
            final World worldIn,
            final BlockPos pos,
            final EnumHand hand,
            final EnumFacing facing,
            final float hitX,
            final float hitY,
            final float hitZ)
    {
        Log.getLogger().info("onItemUse");
        if(worldIn == null || player == null)
        {
            return EnumActionResult.FAIL;
        }

        final ItemStack stack = player.getHeldItem(hand);
        if (worldIn.isRemote || stack.getCount() == 0)
        {
            return EnumActionResult.FAIL;
        }

        Log.getLogger().info("onItemUse claim?");
        if (canClaimVillage(player, worldIn, pos))
        {
            Log.getLogger().info("onItemUse claim ok");
            //todo ?
            buildClaim(player, worldIn, pos);
            Log.getLogger().info("onItemUse claim done");
            return EnumActionResult.SUCCESS;
        }

        return EnumActionResult.SUCCESS;
    }


    /**
     * check that the player can claim the village.
     *
     * The player should have a townhall level 1 at least
     * Should be in a village radius.
     * Should not be in a colony.
     * Should be in tax radius.
     * Should not be already claimed
     * Should not have other players guard near by
     * @return true when we can claim the village
     */
    private static boolean canClaimVillage(@NotNull final EntityPlayer player, @NotNull final World worldIn, @NotNull final BlockPos pos)
    {
        // Check that The player has a colony
        final IColony iColony = ColonyManager.getIColonyByOwner(worldIn, player);
        final Colony colony = iColony == null ? null : ColonyManager.getColony(iColony.getID());

        if (colony == null)
        {
            LanguageHandler.sendPlayerMessage(player, "com.minecolonies.coremod.error.villageClaimWithoutColony");
            return false;
        }

        // check for a village nearby
        net.minecraft.village.Village village = worldIn.getVillageCollection().getNearestVillage(pos, 32);
        if (village == null || village.getVillageRadius() * village.getVillageRadius() > BlockPosUtil.getDistanceSquared(village.getCenter(), pos))
        {
            LanguageHandler.sendPlayerMessage(player, "com.minecolonies.coremod.error.villageClaimNoVillage");
            return false;
        }

        //Check that we are not in a colony
        if (ColonyManager.isCoordinateInAnyColony(worldIn, pos))
        {
            LanguageHandler.sendPlayerMessage(player, "com.minecolonies.coremod.error.villageClaimInColony");
            return false;
        }

        // Check that we are in tax radius
        final BuildingTownHall townHall = colony.getTownHall();
        if (townHall != null && townHall.getTaxRadius() * townHall.getTaxRadius() > BlockPosUtil.getDistanceSquared(colony.getCenter(), pos))
        {
            LanguageHandler.sendPlayerMessage(player, "com.minecolonies.coremod.error.villageClaimTooFar");
            return false;
        }

        // check that we don't have guard around
        // TODO
        return true;
    }

    //Temporary for testing
    private static void buildClaim(@NotNull final EntityPlayer player, @NotNull final World worldIn, @NotNull final BlockPos pos)
    {
        BlockPos currentPosition = pos.up().up();
        worldIn.setBlockState(currentPosition, Blocks.OAK_FENCE.getDefaultState());
        currentPosition = pos.up();
        worldIn.setBlockState(currentPosition, Blocks.OAK_FENCE.getDefaultState());
        currentPosition = pos.up();
        worldIn.setBlockState(currentPosition, Blocks.GLOWSTONE.getDefaultState());
        currentPosition = pos.north();
        worldIn.setBlockState(currentPosition, Blocks.WALL_BANNER.getDefaultState());
        currentPosition = pos.south(2);
        worldIn.setBlockState(currentPosition, Blocks.WALL_BANNER.getDefaultState());
        currentPosition = pos.north();
        currentPosition = pos.east();
        worldIn.setBlockState(currentPosition, Blocks.WALL_BANNER.getDefaultState());
        currentPosition = pos.west(2);
        worldIn.setBlockState(currentPosition, Blocks.WALL_BANNER.getDefaultState());



    }
}
