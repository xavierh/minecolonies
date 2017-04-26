package com.minecolonies.coremod.villages;

import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.common.registry.VillagerRegistry;
import java.util.Random;
import java.util.List;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.StructureVillagePieces;
import net.minecraft.world.gen.structure.StructureVillagePieces.PieceWeight;
import net.minecraft.world.gen.structure.StructureVillagePieces.Village;
import net.minecraft.util.math.MathHelper;

import com.minecolonies.coremod.villages.ComponentVillageControlPoint;
import com.minecolonies.coremod.util.Log;

public class VillageHandlerControlPoint implements VillagerRegistry.IVillageCreationHandler
{


     public StructureVillagePieces.PieceWeight getVillagePieceWeight(Random random, int size)
     {
            Log.getLogger().info("VillageHandlerControlPoint.getVillagePieceWeight()");
           return new StructureVillagePieces.PieceWeight(ComponentVillageControlPoint.class, 4, MathHelper.getInt(random, 0, 10));
     }

     public Class<ComponentVillageControlPoint> getComponentClass() {
            Log.getLogger().info("VillageHandlerControlPoint.getComponentClass()");
           return ComponentVillageControlPoint.class;
     }

     public Village buildComponent(StructureVillagePieces.PieceWeight villagePiece, StructureVillagePieces.Start startPiece, List<StructureComponent> pieces, Random random, int p1,
                               int p2, int p3, EnumFacing facing, int p5)
     {
            Log.getLogger().info("VillageHandlerControlPoint.buildComponent()");
         return ComponentVillageControlPoint.buildComponent(startPiece, pieces, random, p1, p2, p3, facing, p5);
     }

     public static void init()
     {
       /* Log.getLogger().info("VillageHandlerControlPoint.init()");
        VillageHandlerControlPoint villageHandler = new VillageHandlerControlPoint();
        VillagerRegistry.instance().registerVillageCreationHandler(villageHandler);*/
     }


}
