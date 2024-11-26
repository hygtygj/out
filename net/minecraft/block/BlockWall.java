package net.minecraft.block;

import java.util.List;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.StatCollector;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockWall extends Block {
   public static final PropertyBool UP = PropertyBool.create("up");
   public static final PropertyBool NORTH = PropertyBool.create("north");
   public static final PropertyBool EAST = PropertyBool.create("east");
   public static final PropertyBool SOUTH = PropertyBool.create("south");
   public static final PropertyBool WEST = PropertyBool.create("west");
   public static final PropertyEnum<BlockWall.EnumType> VARIANT = PropertyEnum.create("variant", BlockWall.EnumType.class);

   public BlockWall(Block modelBlock) {
      super(modelBlock.blockMaterial);
      this.setDefaultState(
         this.blockState
            .getBaseState()
            .withProperty(UP, false)
            .withProperty(NORTH, false)
            .withProperty(EAST, false)
            .withProperty(SOUTH, false)
            .withProperty(WEST, false)
            .withProperty(VARIANT, BlockWall.EnumType.NORMAL)
      );
      this.setHardness(modelBlock.blockHardness);
      this.setResistance(modelBlock.blockResistance / 3.0F);
      this.setStepSound(modelBlock.stepSound);
      this.setCreativeTab(CreativeTabs.tabBlock);
   }

   @Override
   public String getLocalizedName() {
      return StatCollector.translateToLocal(this.getUnlocalizedName() + "." + BlockWall.EnumType.NORMAL.getUnlocalizedName() + ".name");
   }

   @Override
   public boolean isFullCube() {
      return false;
   }

   @Override
   public boolean isPassable(IBlockAccess worldIn, BlockPos pos) {
      return false;
   }

   @Override
   public boolean isOpaqueCube() {
      return false;
   }

   @Override
   public void setBlockBoundsBasedOnState(IBlockAccess worldIn, BlockPos pos) {
      boolean flag = this.canConnectTo(worldIn, pos.north());
      boolean flag1 = this.canConnectTo(worldIn, pos.south());
      boolean flag2 = this.canConnectTo(worldIn, pos.west());
      boolean flag3 = this.canConnectTo(worldIn, pos.east());
      float f = 0.25F;
      float f1 = 0.75F;
      float f2 = 0.25F;
      float f3 = 0.75F;
      float f4 = 1.0F;
      if (flag) {
         f2 = 0.0F;
      }

      if (flag1) {
         f3 = 1.0F;
      }

      if (flag2) {
         f = 0.0F;
      }

      if (flag3) {
         f1 = 1.0F;
      }

      if (flag && flag1 && !flag2 && !flag3) {
         f4 = 0.8125F;
         f = 0.3125F;
         f1 = 0.6875F;
      } else if (!flag && !flag1 && flag2 && flag3) {
         f4 = 0.8125F;
         f2 = 0.3125F;
         f3 = 0.6875F;
      }

      this.setBlockBounds(f, 0.0F, f2, f1, f4, f3);
   }

   @Override
   public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state) {
      this.setBlockBoundsBasedOnState(worldIn, pos);
      this.maxY = 1.5;
      return super.getCollisionBoundingBox(worldIn, pos, state);
   }

   public boolean canConnectTo(IBlockAccess worldIn, BlockPos pos) {
      Block block = worldIn.getBlockState(pos).getBlock();
      return block == Blocks.barrier
         ? false
         : (
            block != this && !(block instanceof BlockFenceGate)
               ? (block.blockMaterial.isOpaque() && block.isFullCube() ? block.blockMaterial != Material.gourd : false)
               : true
         );
   }

   @Override
   public void getSubBlocks(Item itemIn, CreativeTabs tab, List<ItemStack> list) {
      BlockWall.EnumType[] var7;
      for (BlockWall.EnumType blockwall$enumtype : var7 = BlockWall.EnumType.values()) {
         list.add(new ItemStack(itemIn, 1, blockwall$enumtype.getMetadata()));
      }
   }

   @Override
   public int damageDropped(IBlockState state) {
      return state.getValue(VARIANT).getMetadata();
   }

   @Override
   public boolean shouldSideBeRendered(IBlockAccess worldIn, BlockPos pos, EnumFacing side) {
      return side == EnumFacing.DOWN ? super.shouldSideBeRendered(worldIn, pos, side) : true;
   }

   @Override
   public IBlockState getStateFromMeta(int meta) {
      return this.getDefaultState().withProperty(VARIANT, BlockWall.EnumType.byMetadata(meta));
   }

   @Override
   public int getMetaFromState(IBlockState state) {
      return state.getValue(VARIANT).getMetadata();
   }

   @Override
   public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
      return state.withProperty(UP, !worldIn.isAirBlock(pos.up()))
         .withProperty(NORTH, this.canConnectTo(worldIn, pos.north()))
         .withProperty(EAST, this.canConnectTo(worldIn, pos.east()))
         .withProperty(SOUTH, this.canConnectTo(worldIn, pos.south()))
         .withProperty(WEST, this.canConnectTo(worldIn, pos.west()));
   }

   @Override
   protected BlockState createBlockState() {
      return new BlockState(this, UP, NORTH, EAST, WEST, SOUTH, VARIANT);
   }

   public static enum EnumType implements IStringSerializable {
      NORMAL(0, "cobblestone", "normal"),
      MOSSY(1, "mossy_cobblestone", "mossy");

      private static final BlockWall.EnumType[] META_LOOKUP = new BlockWall.EnumType[values().length];
      private final int meta;
      private final String name;
      private String unlocalizedName;

      static {
         BlockWall.EnumType[] var3;
         for (BlockWall.EnumType blockwall$enumtype : var3 = values()) {
            META_LOOKUP[blockwall$enumtype.getMetadata()] = blockwall$enumtype;
         }
      }

      private EnumType(int meta, String name, String unlocalizedName) {
         this.meta = meta;
         this.name = name;
         this.unlocalizedName = unlocalizedName;
      }

      public int getMetadata() {
         return this.meta;
      }

      @Override
      public String toString() {
         return this.name;
      }

      public static BlockWall.EnumType byMetadata(int meta) {
         if (meta < 0 || meta >= META_LOOKUP.length) {
            meta = 0;
         }

         return META_LOOKUP[meta];
      }

      @Override
      public String getName() {
         return this.name;
      }

      public String getUnlocalizedName() {
         return this.unlocalizedName;
      }
   }
}
