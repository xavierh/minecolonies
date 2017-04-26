package com.minecolonies.coremod.colony;

import com.minecolonies.coremod.colony.Village;
import com.minecolonies.coremod.util.Log;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.Constants.NBT;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraft.util.math.BlockPos;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.ArrayList;

public final class VillageManager
{
    /**
     * The tag of the villages.
     */
    private static final String TAG_VILLAGES = "villages";

    /**
     * List of known villages
     * Note: we keep a list of villages as Minecraft does not keep the full list
     * And we need to store additionnal informations.
     */
    @NotNull
    private static final List<Village> villages = new ArrayList<Village>();

    private VillageManager()
    {
        //Hides default constructor.
    }



    /**
     * Write villages to NBT data for saving.
     *
     * @param compound NBT-Tag.
     */
    public static void writeToNBT(@NotNull final NBTTagCompound compound)
    {
        @NotNull final NBTTagList villageTagList = new NBTTagList();
        for (@NotNull final Village village : villages)
        {
            @NotNull final NBTTagCompound villageTagCompound = new NBTTagCompound();
            village.writeToNBT(villageTagCompound);
            villageTagList.appendTag(villageTagCompound);
        }
        compound.setTag(TAG_VILLAGES, villageTagList);
    }

    /**
     * Read Colonies from saved NBT data.
     *
     * @param compound NBT Tag.
     */
    public static void readFromNBT(@NotNull final NBTTagCompound compound)
    {
        final NBTTagList villageTags = compound.getTagList(TAG_VILLAGES, NBT.TAG_COMPOUND);
        for (int i = 0; i < villageTags.tagCount(); ++i)
        {
            @NotNull final Village village = Village.loadVillage(villageTags.getCompoundTagAt(i));
            villages.add(village);
        }

        Log.getLogger().info(String.format("Loaded %d villages", villages.size()));
    }

    /**
     * On server tick, tick every Village.
     * NOTE: Review this for performance.
     *
     * @param event {@link net.minecraftforge.fml.common.gameevent.TickEvent.ServerTickEvent}
     */
    public static void onServerTick(@NotNull final TickEvent.ServerTickEvent event)
    {
//        Log.getLogger().info("Villages tick: " + getPosition() + " tick");
        Log.getLogger().info("Villages tick");
        /* TODO update villagers number?
        if ((event.world.getWorldTime() % UPDATEVILLAGE ) == 0)
        {

        }
        */
        //getColonies(event.world).forEach(c -> c.onWorldTick(event));

        for (@NotNull final Village v : villages)
        {
            v.onServerTick(event);
        }
    }

    /**
     * get the village using approximated coordinates.
     *
     * return the village, null if none
     */
    @Nullable
    public static Village getVillage(@NotNull final BlockPos position)
    {
        for (@NotNull final Village v : villages)
        {
             if (v.isSameVillage(position))
             {
                 return v;
             }
        }
        return null;
    }

    public static void  addVillage(@NotNull final Village village)
    {
        villages.add(village);
        ColonyManager.markDirty();
    }
/*
    public static Village createVillage(@NotNull PlockPos position, int numVillager, int radius)
    {
    }
*/

}
