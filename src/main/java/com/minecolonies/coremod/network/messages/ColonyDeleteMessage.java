package com.minecolonies.coremod.network.messages;

import com.minecolonies.coremod.colony.ColonyView;
import com.minecolonies.coremod.colony.IColony;
import com.minecolonies.coremod.colony.ColonyManager;
import io.netty.buffer.ByteBuf;
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
            ColonyManager.deleteColony(message.colonyId);
        }
    }
}
