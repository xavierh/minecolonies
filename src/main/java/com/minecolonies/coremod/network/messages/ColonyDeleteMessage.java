package com.minecolonies.coremod.network.messages;

import com.minecolonies.coremod.blocks.ModBlocks;
import com.minecolonies.coremod.colony.ColonyView;
import com.minecolonies.coremod.colony.IColony;
import com.minecolonies.coremod.colony.ColonyManager;
import com.minecolonies.coremod.util.LanguageHandler;
import io.netty.buffer.ByteBuf;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Delete/Abandon a colony.
 * @author xavierh
 */
public class ColonyDeleteMessage  extends AbstractMessage<ColonyDeleteMessage, IMessage>
{

    private int colonyId;

    /**
     * Empty constructor used when registering the message.
     */
    public ColonyDeleteMessage()
    {
        super();
    }

    /**
     * Creates an object for the delete message for colony.
     *
     * @param colony      colony the player want to abandon.
     */
    public ColonyDeleteMessage(@NotNull final ColonyView colony)
    {
        this.colonyId = colony.getID();
    }

    @Override
    public void fromBytes(@NotNull final ByteBuf buf)
    {
        colonyId = buf.readInt();
    }

    @Override
    public void toBytes(@NotNull final ByteBuf buf)
    {
        buf.writeInt(colonyId);
    }

    @Override
    public void messageOnServerThread(final ColonyDeleteMessage message, final EntityPlayerMP player)
    {
        final IColony iColony = ColonyManager.getIColonyByOwner(player.world, player);
        if (iColony.getID() == message.colonyId)
        {
            //delete the colony
            ColonyManager.deleteColony(message.colonyId);
            //give a TH item to the player
            EntityItem entity = player.dropItem(new ItemStack(ModBlocks.blockHutTownHall, 1), false);
            entity.setNoPickupDelay();
            LanguageHandler.sendPlayerMessage(player, "com.minecolonies.coremod.gui.townHall.abandon.warning");
        }
    }
}
