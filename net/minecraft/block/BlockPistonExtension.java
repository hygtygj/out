package net.minecraft.block;

import java.util.List;
import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockPistonExtension extends Block {
   public static final PropertyDirection FACING = PropertyDirection.create("facing");
   public static final PropertyEnum<BlockPistonExtension.EnumPistonType> TYPE = PropertyEnum.create("type", BlockPistonExtension.EnumPistonType.class);
   public static final PropertyBool SHORT = PropertyBool.create("short");

   public BlockPistonExtension() {
      super(Material.piston);
      this.setDefaultState(
         this.blockState
            .getBaseState()
            .withProperty(FACING, EnumFacing.NORTH)
            .withProperty(TYPE, BlockPistonExtension.EnumPistonType.DEFAULT)
            .withProperty(SHORT, false)
      );
      this.setStepSound(soundTypePiston);
      this.setHardness(0.5F);
   }

   @Override
   public void onBlockHarvested(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player) {
      if (player.capabilities.isCreativeMode) {
         EnumFacing enumfacing = state.getValue(FACING);
         if (enumfacing != null) {
            BlockPos blockpos = pos.offset(enumfacing.getOpposite());
            Block block = worldIn.getBlockState(blockpos).getBlock();
            if (block == Blocks.piston || block == Blocks.sticky_piston) {
               worldIn.setBlockToAir(blockpos);
            }
         }
      }

      super.onBlockHarvested(worldIn, pos, state, player);
   }

   @Override
   public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
      super.breakBlock(worldIn, pos, state);
      EnumFacing enumfacing = state.getValue(FACING).getOpposite();
      pos = pos.offset(enumfacing);
      IBlockState iblockstate = worldIn.getBlockState(pos);
      if ((iblockstate.getBlock() == Blocks.piston || iblockstate.getBlock() == Blocks.sticky_piston) && iblockstate.getValue(BlockPistonBase.EXTENDED)) {
         iblockstate.getBlock().dropBlockAsItem(worldIn, pos, iblockstate, 0);
         worldIn.setBlockToAir(pos);
      }
   }

   @Override
   public boolean isOpaqueCube() {
      return false;
   }

   @Override
   public boolean isFullCube() {
      return false;
   }

   @Override
   public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
      return false;
   }

   @Override
   public boolean canPlaceBlockOnSide(World worldIn, BlockPos pos, EnumFacing side) {
      return false;
   }

   @Override
   public int quantityDropped(Random random) {
      return 0;
   }

   @Override
   public void addCollisionBoxesToList(World worldIn, BlockPos pos, IBlockState state, AxisAlignedBB mask, List<AxisAlignedBB> list, Entity collidingEntity) {
      this.applyHeadBounds(state);
      super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
      this.applyCoreBounds(state);
      super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
      this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
   }

   private void applyCoreBounds(IBlockState state) {
      float f = 0.25F;
      float f1 = 0.375F;
      float f2 = 0.625F;
      float f3 = 0.25F;
      float f4 = 0.75F;
      switch ((EnumFacing)state.getValue(FACING)) {
         case DOWN:
            this.setBlockBounds(0.375F, 0.25F, 0.375F, 0.625F, 1.0F, 0.625F);
            break;
         case UP:
            this.setBlockBounds(0.375F, 0.0F, 0.375F, 0.625F, 0.75F, 0.625F);
            break;
         case NORTH:
            this.setBlockBounds(0.25F, 0.375F, 0.25F, 0.75F, 0.625F, 1.0F);
            break;
         case SOUTH:
            this.setBlockBounds(0.25F, 0.375F, 0.0F, 0.75F, 0.625F, 0.75F);
            break;
         case WEST:
            this.setBlockBounds(0.375F, 0.25F, 0.25F, 0.625F, 0.75F, 1.0F);
            break;
         case EAST:
            this.setBlockBounds(0.0F, 0.375F, 0.25F, 0.75F, 0.625F, 0.75F);
      }
   }

   @Override
   public void setBlockBoundsBasedOnState(IBlockAccess worldIn, BlockPos pos) {
      this.applyHeadBounds(worldIn.getBlockState(pos));
   }

   public void applyHeadBounds(IBlockState state) {
      float f = 0.25F;
      EnumFacing enumfacing = state.getValue(FACING);
      if (enumfacing != null) {
         switch (enumfacing) {
            case DOWN:
               this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.25F, 1.0F);
               break;
            case UP:
               this.setBlockBounds(0.0F, 0.75F, 0.0F, 1.0F, 1.0F, 1.0F);
               break;
            case NORTH:
               this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.25F);
               break;
            case SOUTH:
               this.setBlockBounds(0.0F, 0.0F, 0.75F, 1.0F, 1.0F, 1.0F);
               break;
            case WEST:
               this.setBlockBounds(0.0F, 0.0F, 0.0F, 0.25F, 1.0F, 1.0F);
               break;
            case EAST:
               this.setBlockBounds(0.75F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
         }
      }
   }

   @Override
   public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock) {
      EnumFacing enumfacing = state.getValue(FACING);
      BlockPos blockpos = pos.offset(enumfacing.getOpposite());
      IBlockState iblockstate = worldIn.getBlockState(blockpos);
      if (iblockstate.getBlock() != Blocks.piston && iblockstate.getBlock() != Blocks.sticky_piston) {
         worldIn.setBlockToAir(pos);
      } else {
         iblockstate.getBlock().onNeighborBlockChange(worldIn, blockpos, iblockstate, neighborBlock);
      }
   }

   @Override
   public boolean shouldSideBeRendered(IBlockAccess worldIn, BlockPos pos, EnumFacing side) {
      return true;
   }

   public static EnumFacing getFacing(int meta) {
      int i = meta & 7;
      return i > 5 ? null : EnumFacing.getFront(i);
   }

   @Override
   public Item getItem(World worldIn, BlockPos pos) {
      return worldIn.getBlockState(pos).getValue(TYPE) == BlockPistonExtension.EnumPistonType.STICKY
         ? Item.getItemFromBlock(Blocks.sticky_piston)
         : Item.getItemFromBlock(Blocks.piston);
   }

   @Override
   public IBlockState getStateFromMeta(int meta) {
      return this.getDefaultState()
         .withProperty(FACING, getFacing(meta))
         .withProperty(TYPE, (meta & 8) > 0 ? BlockPistonExtension.EnumPistonType.STICKY : BlockPistonExtension.EnumPistonType.DEFAULT);
   }

   @Override
   public int getMetaFromState(IBlockState state) {
      int i = 0;
      i |= state.getValue(FACING).getIndex();
      if (state.getValue(TYPE) == BlockPistonExtension.EnumPistonType.STICKY) {
         i |= 8;
      }

      return i;
   }

   @Override
   protected BlockState createBlockState() {
      return new BlockState(this, FACING, TYPE, SHORT);
   }

   public static enum EnumPistonType implements IStringSerializable {
      DEFAULT("normal"),
      STICKY("sticky");

      private final String VARIANT;

      private EnumPistonType(String name) {
         this.VARIANT = name;
      }

      @Override
      public String toString() {
         return this.VARIANT;
      }

      @Override
      public String getName() {
         return this.VARIANT;
      }
   }
}
