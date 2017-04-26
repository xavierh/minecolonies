package com.minecolonies.coremod.colony;

import com.minecolonies.coremod.util.BlockPosUtil;
import com.minecolonies.coremod.util.Log;
import net.minecraft.util.math.BlockPos;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


public class Village
{
    private static final String TAG_POSITION = "position";
    private static final String TAG_RADIUS = "radius";
    private static final String TAG_VILLAGERS = "villagers";
    private static final String TAG_COLONY = "colony";
    private static final int COLLECT_TAX = 1000;
    

    /**
     * last known position for the village.
     * NOTE: The position of a village can change over time as this is calculated
     * from the average positions of the doors
     */
    @NotNull
    private BlockPos position;

    /**
     * Last known number of villagers in thie villages.
     */
    private int numVillagers;

    /**
     * Last known radius of the village.
     */
    private int radius;

    /**
     * colony which claim this village.
     */
    @Nullable
    private IColony colony;

    /**
     * colony which claim this village.
     */
    private int taxCollected;
    private int tickBeforeCollect;

    public Village(@NotNull final BlockPos position, final int numVillagers, final int radius)
    {
        this.position = position;
        this.numVillagers = numVillagers;
        this.radius = radius;
        this.taxCollected = 0;
        this.tickBeforeCollect = COLLECT_TAX;
    }


    /**
     * set the position of the village.
     *
     * @param position of the village
     */
    public void setPosition(@NotNull final BlockPos position)
    {
        this.position = position;
    }

    /**
     * get the position of the village.
     *
     * @return position of the village
     */
    public BlockPos getPosition()
    {
        return this.position;
    }

    /**
     * set the number of villager.
     *
     * @param number of villagers
     */
    public void setNumVillagers(final int numVillagers)
    {
        this.numVillagers = numVillagers;
    }

    /**
     * get the number of villagers in the village.
     *
     * @return the number of villagers
     */
    public int getNumVillagers()
    {
        return this.numVillagers;
    }

    /**
     * set the radius of the village.
     *
     * @param radius of the village
     */
    public void setRadius(final int radius)
    {
        this.radius = radius;
    }

    /**
     * get the radius of the village.
     *
     * @return the radius of the village
     */
    public int getRadius()
    {
        return this.radius;
    }

    /**
     * Check whether or not this is the same village
     */
    public boolean isSameVillage(@NotNull final BlockPos position)
    {
        if (this.position.equals(position))
        {
            return true;
        }

        //location might have changed
        final long squaredDistance = BlockPosUtil.getDistanceSquared2D(this.position, position);
        return squaredDistance <= (this.radius * this.radius);
    }

    /**
     * Whether or not a colony can claim this village.
     *
     * @return true if the colony can claim this villages
     */
    public boolean canClaim(final IColony c)
    {
        //TODO
        // check if we are to far from the colony ?
        // check that it is not already clamied ?
        return true;
    }

    public boolean isClaimed()
    {
        Log.getLogger().info("village owner = " + colony);
        return colony != null;
    }

    /**
     * Claim the village.
     *
     * @param colony which is claiming this village, use Null to unclaim
     */
    public void claim(final IColony c)
    {
        colony = c;
    }

    /**
     * Write village to NBT data for saving.
     *
     * @param compound NBT-Tag.
     */
    public void writeToNBT(@NotNull final NBTTagCompound compound)
    {
        BlockPosUtil.writeToNBT(compound, TAG_POSITION, position);
        compound.setInteger(TAG_RADIUS, radius);
        compound.setInteger(TAG_VILLAGERS, numVillagers);
        if (colony != null)
        {
            compound.setInteger(TAG_COLONY, colony.getID());
        }
    }

    /**
     * Load a saved village.
     *
     * @param compound The NBT compound containing the village's data.
     * @return loaded village.
     */
    @NotNull
    public static Village loadVillage(@NotNull final NBTTagCompound compound)
    {
        @NotNull final BlockPos position = BlockPosUtil.readFromNBT(compound, TAG_POSITION);
        final int radius = compound.getInteger(TAG_RADIUS);
        final int numVillagers = compound.getInteger(TAG_VILLAGERS);
        final Village village = new Village(position, radius, numVillagers);
        final Colony colony = ColonyManager.getColony(compound.getInteger(TAG_COLONY));
        if (colony != null)
        {
            village.colony = colony;
        }
        return village;
    }


    /**
     * Any per-server-tick logic should be performed here.
     *
     * @param event {@link net.minecraftforge.fml.common.gameevent.TickEvent.ServerTickEvent}
     */
    public void onServerTick(@NotNull final TickEvent.ServerTickEvent event)
    {
        tickBeforeCollect--;
        Log.getLogger().info("Village: tickBeforeCollect " + tickBeforeCollect);
        if (tickBeforeCollect < 0)
        {
            taxCollected += numVillagers;
            tickBeforeCollect=COLLECT_TAX;
            Log.getLogger().info("Village ["+position+"] has collected " + taxCollected);
        }

    }
}
