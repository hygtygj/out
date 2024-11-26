package net.minecraft.block;

import java.util.List;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;

public class BlockRedSandstone extends Block {
   public static final PropertyEnum<BlockRedSandstone.EnumType> TYPE = PropertyEnum.create("type", BlockRedSandstone.EnumType.class);

   public BlockRedSandstone() {
      super(Material.rock, BlockSand.EnumType.RED_SAND.getMapColor());
      this.setDefaultState(this.blockState.getBaseState().withProperty(TYPE, BlockRedSandstone.EnumType.DEFAULT));
      this.setCreativeTab(CreativeTabs.tabBlock);
   }

   @Override
   public int damageDropped(IBlockState state) {
      return state.getValue(TYPE).getMetadata();
   }

   @Override
   public void getSubBlocks(Item itemIn, CreativeTabs tab, List<ItemStack> list) {
      BlockRedSandstone.EnumType[] var7;
      for (BlockRedSandstone.EnumType blockredsandstone$enumtype : var7 = BlockRedSandstone.EnumType.values()) {
         list.add(new ItemStack(itemIn, 1, blockredsandstone$enumtype.getMetadata()));
      }
   }

   @Override
   public IBlockState getStateFromMeta(int meta) {
      return this.getDefaultState().withProperty(TYPE, BlockRedSandstone.EnumType.byMetadata(meta));
   }

   @Override
   public int getMetaFromState(IBlockState state) {
      return state.getValue(TYPE).getMetadata();
   }

   @Override
   protected BlockState createBlockState() {
      return new BlockState(this, TYPE);
   }

   public static enum EnumType implements IStringSerializable {
      DEFAULT(0, "red_sandstone", "default"),
      CHISELED(1, "chiseled_red_sandstone", "chiseled"),
      SMOOTH(2, "smooth_red_sandstone", "smooth");

      private static final BlockRedSandstone.EnumType[] META_LOOKUP = new BlockRedSandstone.EnumType[values().length];
      private final int meta;
      private final String name;
      private final String unlocalizedName;

      static {
         BlockRedSandstone.EnumType[] var3;
         for (BlockRedSandstone.EnumType blockredsandstone$enumtype : var3 = values()) {
            META_LOOKUP[blockredsandstone$enumtype.getMetadata()] = blockredsandstone$enumtype;
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

      public static BlockRedSandstone.EnumType byMetadata(int meta) {
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
