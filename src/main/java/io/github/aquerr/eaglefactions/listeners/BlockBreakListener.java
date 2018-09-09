package io.github.aquerr.eaglefactions.listeners;

import io.github.aquerr.eaglefactions.EagleFactions;
import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.data.Transaction;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.Order;
import org.spongepowered.api.event.block.ChangeBlockEvent;
import org.spongepowered.api.event.cause.EventContextKeys;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.util.ArrayList;
import java.util.List;

public class BlockBreakListener extends AbstractListener
{
    public BlockBreakListener(EagleFactions plugin)
    {
        super(plugin);
    }

    @Listener(order = Order.EARLY)
    public void onBlockBreak(ChangeBlockEvent.Pre event)
    {
        if(event.getContext().containsKey(EventContextKeys.PLAYER_BREAK))
        {
            List<Location<World>> locationList = new ArrayList<>(event.getLocations());
            for(Location<World> location : locationList)
            {
                BlockState blockState = location.getBlock();
                if(blockState.getType() == BlockTypes.FLOWING_WATER)
                {
                    return;
                }

                if(event.getContext().containsKey(EventContextKeys.OWNER))
                {
                    Player player = (Player) event.getContext().get(EventContextKeys.OWNER).get();
                    if(!EagleFactions.AdminList.contains(player.getUniqueId()))
                    {
                        World world = player.getWorld();

                        if(!super.getPlugin().getProtectionManager().canBreak(location, world, player))
                            event.setCancelled(true);
                    }
                }
                else
                {
                    if(blockState.getType() == BlockTypes.FLOWING_WATER)
                    {
                        return;
                    }

                    if(!super.getPlugin().getProtectionManager().canBreak(location, location.getExtent()))
                        event.setCancelled(true);
                }
            }
        }
    }

    @Listener(order = Order.EARLY)
    public void onBlockBreak(ChangeBlockEvent.Break event)
    {
        if(event.getContext().containsKey(EventContextKeys.OWNER) && event.getContext().get(EventContextKeys.OWNER).isPresent())
        {
            Player player = (Player) event.getContext().get(EventContextKeys.OWNER).get();

            if(!EagleFactions.AdminList.contains(player.getUniqueId()))
            {
                for(Transaction<BlockSnapshot> transaction : event.getTransactions())
                {
                    if(!super.getPlugin().getProtectionManager().canBreak(transaction.getFinal().getLocation().get(), player.getWorld(), player))
                        event.setCancelled(true);
                }
            }
        }
        else
        {
            for (Transaction<BlockSnapshot> transaction : event.getTransactions())
            {
                if(transaction.getOriginal().getState().getType() == BlockTypes.FLOWING_WATER)
                {
                    return;
                }

                if(!super.getPlugin().getProtectionManager().canBreak(transaction.getFinal().getLocation().get(), transaction.getFinal().getLocation().get().getExtent()))
                    event.setCancelled(true);
            }
        }
    }
}
