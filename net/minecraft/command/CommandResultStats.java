package net.minecraft.command;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.util.BlockPos;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class CommandResultStats {
   private static final int NUM_RESULT_TYPES = CommandResultStats.Type.values().length;
   private static final String[] STRING_RESULT_TYPES = new String[NUM_RESULT_TYPES];
   private String[] entitiesID = STRING_RESULT_TYPES;
   private String[] objectives = STRING_RESULT_TYPES;

   public void setCommandStatScore(final ICommandSender sender, CommandResultStats.Type resultTypeIn, int scorePoint) {
      String s = this.entitiesID[resultTypeIn.getTypeID()];
      if (s != null) {
         ICommandSender icommandsender = new ICommandSender() {
            @Override
            public String getName() {
               return sender.getName();
            }

            @Override
            public IChatComponent getDisplayName() {
               return sender.getDisplayName();
            }

            @Override
            public void addChatMessage(IChatComponent component) {
               sender.addChatMessage(component);
            }

            @Override
            public boolean canCommandSenderUseCommand(int permLevel, String commandName) {
               return true;
            }

            @Override
            public BlockPos getPosition() {
               return sender.getPosition();
            }

            @Override
            public Vec3 getPositionVector() {
               return sender.getPositionVector();
            }

            @Override
            public World getEntityWorld() {
               return sender.getEntityWorld();
            }

            @Override
            public Entity getCommandSenderEntity() {
               return sender.getCommandSenderEntity();
            }

            @Override
            public boolean sendCommandFeedback() {
               return sender.sendCommandFeedback();
            }

            @Override
            public void setCommandStat(CommandResultStats.Type type, int amount) {
               sender.setCommandStat(type, amount);
            }
         };

         String s1;
         try {
            s1 = CommandBase.getEntityName(icommandsender, s);
         } catch (EntityNotFoundException var11) {
            return;
         }

         String s2 = this.objectives[resultTypeIn.getTypeID()];
         if (s2 != null) {
            Scoreboard scoreboard = sender.getEntityWorld().getScoreboard();
            ScoreObjective scoreobjective = scoreboard.getObjective(s2);
            if (scoreobjective != null && scoreboard.entityHasObjective(s1, scoreobjective)) {
               Score score = scoreboard.getValueFromObjective(s1, scoreobjective);
               score.setScorePoints(scorePoint);
            }
         }
      }
   }

   public void readStatsFromNBT(NBTTagCompound tagcompound) {
      if (tagcompound.hasKey("CommandStats", 10)) {
         NBTTagCompound nbttagcompound = tagcompound.getCompoundTag("CommandStats");

         CommandResultStats.Type[] var6;
         for (CommandResultStats.Type commandresultstats$type : var6 = CommandResultStats.Type.values()) {
            String s = commandresultstats$type.getTypeName() + "Name";
            String s1 = commandresultstats$type.getTypeName() + "Objective";
            if (nbttagcompound.hasKey(s, 8) && nbttagcompound.hasKey(s1, 8)) {
               String s2 = nbttagcompound.getString(s);
               String s3 = nbttagcompound.getString(s1);
               setScoreBoardStat(this, commandresultstats$type, s2, s3);
            }
         }
      }
   }

   public void writeStatsToNBT(NBTTagCompound tagcompound) {
      NBTTagCompound nbttagcompound = new NBTTagCompound();

      CommandResultStats.Type[] var6;
      for (CommandResultStats.Type commandresultstats$type : var6 = CommandResultStats.Type.values()) {
         String s = this.entitiesID[commandresultstats$type.getTypeID()];
         String s1 = this.objectives[commandresultstats$type.getTypeID()];
         if (s != null && s1 != null) {
            nbttagcompound.setString(commandresultstats$type.getTypeName() + "Name", s);
            nbttagcompound.setString(commandresultstats$type.getTypeName() + "Objective", s1);
         }
      }

      if (!nbttagcompound.hasNoTags()) {
         tagcompound.setTag("CommandStats", nbttagcompound);
      }
   }

   public static void setScoreBoardStat(CommandResultStats stats, CommandResultStats.Type resultType, String entityID, String objectiveName) {
      if (entityID != null && entityID.length() != 0 && objectiveName != null && objectiveName.length() != 0) {
         if (stats.entitiesID == STRING_RESULT_TYPES || stats.objectives == STRING_RESULT_TYPES) {
            stats.entitiesID = new String[NUM_RESULT_TYPES];
            stats.objectives = new String[NUM_RESULT_TYPES];
         }

         stats.entitiesID[resultType.getTypeID()] = entityID;
         stats.objectives[resultType.getTypeID()] = objectiveName;
      } else {
         removeScoreBoardStat(stats, resultType);
      }
   }

   private static void removeScoreBoardStat(CommandResultStats resultStatsIn, CommandResultStats.Type resultTypeIn) {
      if (resultStatsIn.entitiesID != STRING_RESULT_TYPES && resultStatsIn.objectives != STRING_RESULT_TYPES) {
         resultStatsIn.entitiesID[resultTypeIn.getTypeID()] = null;
         resultStatsIn.objectives[resultTypeIn.getTypeID()] = null;
         boolean flag = true;

         CommandResultStats.Type[] var6;
         for (CommandResultStats.Type commandresultstats$type : var6 = CommandResultStats.Type.values()) {
            if (resultStatsIn.entitiesID[commandresultstats$type.getTypeID()] != null && resultStatsIn.objectives[commandresultstats$type.getTypeID()] != null) {
               flag = false;
               break;
            }
         }

         if (flag) {
            resultStatsIn.entitiesID = STRING_RESULT_TYPES;
            resultStatsIn.objectives = STRING_RESULT_TYPES;
         }
      }
   }

   public void addAllStats(CommandResultStats resultStatsIn) {
      CommandResultStats.Type[] var5;
      for (CommandResultStats.Type commandresultstats$type : var5 = CommandResultStats.Type.values()) {
         setScoreBoardStat(
            this,
            commandresultstats$type,
            resultStatsIn.entitiesID[commandresultstats$type.getTypeID()],
            resultStatsIn.objectives[commandresultstats$type.getTypeID()]
         );
      }
   }

   public static enum Type {
      SUCCESS_COUNT(0, "SuccessCount"),
      AFFECTED_BLOCKS(1, "AffectedBlocks"),
      AFFECTED_ENTITIES(2, "AffectedEntities"),
      AFFECTED_ITEMS(3, "AffectedItems"),
      QUERY_RESULT(4, "QueryResult");

      final int typeID;
      final String typeName;

      private Type(int id, String name) {
         this.typeID = id;
         this.typeName = name;
      }

      public int getTypeID() {
         return this.typeID;
      }

      public String getTypeName() {
         return this.typeName;
      }

      public static String[] getTypeNames() {
         String[] astring = new String[values().length];
         int i = 0;

         CommandResultStats.Type[] var5;
         for (CommandResultStats.Type commandresultstats$type : var5 = values()) {
            astring[i++] = commandresultstats$type.getTypeName();
         }

         return astring;
      }

      public static CommandResultStats.Type getTypeByName(String name) {
         CommandResultStats.Type[] var4;
         for (CommandResultStats.Type commandresultstats$type : var4 = values()) {
            if (commandresultstats$type.getTypeName().equals(name)) {
               return commandresultstats$type;
            }
         }

         return null;
      }
   }
}
