package rtg.world.biome.realistic.biomesoplenty;

import java.util.Random;

import biomesoplenty.api.biome.BOPBiomes;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.ChunkPrimer;
import rtg.api.config.BiomeConfig;
import rtg.api.util.noise.SimplexNoise;
import rtg.api.world.RTGWorld;
import rtg.api.world.surface.SurfaceBase;
import rtg.api.world.terrain.TerrainBase;


public class RealisticBiomeBOPOriginBeach extends RealisticBiomeBOPBase {

    public static Biome biome = BOPBiomes.origin_beach.orNull();
    public static Biome river = Biomes.RIVER;

    public RealisticBiomeBOPOriginBeach() {

        super(biome);
    }

    @Override
    public void initConfig() {

    }

    @Override
    public TerrainBase initTerrain() {

        return new TerrainBOPOriginBeach();
    }

    @Override
    public SurfaceBase initSurface() {

        return new SurfaceBOPOriginBeach(getConfig(), biome.topBlock, biome.fillerBlock, biome.topBlock, biome.fillerBlock, 1);
    }

    public static class TerrainBOPOriginBeach extends TerrainBase {

        public TerrainBOPOriginBeach() {

        }

        @Override
        public float generateNoise(RTGWorld rtgWorld, int x, int y, float border, float river) {

            return terrainBeach(x, y, rtgWorld, river, 60f);
        }
    }

    public static class SurfaceBOPOriginBeach extends SurfaceBase {

        private IBlockState cliffBlock1;
        private IBlockState cliffBlock2;
        private int cliffType;

        public SurfaceBOPOriginBeach(BiomeConfig config, IBlockState top, IBlockState filler, IBlockState cliff1, IBlockState cliff2, int cliff) {

            super(config, top, filler);

            cliffBlock1 = cliff1;
            cliffBlock2 = cliff2;
            cliffType = cliff;
        }

        @Override
        public void paintTerrain(ChunkPrimer primer, int i, int j, int x, int z, int depth, RTGWorld rtgWorld, float[] noise, float river, Biome[] base) {

            Random rand = rtgWorld.rand();
            SimplexNoise simplex = rtgWorld.simplexInstance(0);
            float c = TerrainBase.calcCliff(x, z, noise);
            boolean cliff = c > 1.3f;
            boolean dirt = false;

            for (int k = 255; k > -1; k--) {
                Block b = primer.getBlockState(x, k, z).getBlock();
                if (b == Blocks.AIR) {
                    depth = -1;
                }
                else if (b == Blocks.STONE) {
                    depth++;

                    if (cliff) {
                        if (cliffType == 1) {
                            if (depth < 6) {
                                primer.setBlockState(x, k, z, cliffBlock1.getBlock().getDefaultState());
                            }
                        }
                        else {
                            if (depth > -1 && depth < 2) {
                                primer.setBlockState(x, k, z, rand.nextInt(3) == 0 ? cliffBlock2 : cliffBlock1);
                            }
                            else if (depth < 10) {
                                primer.setBlockState(x, k, z, cliffBlock1);
                            }
                        }
                    }
                    else if (depth < 6) {
                        if (depth == 0 && k > 61) {
                            if (simplex.noise2f(i / 12f, j / 12f) > -0.3f + ((k - 61f) / 15f)) {
                                dirt = true;
                                primer.setBlockState(x, k, z, topBlock);
                            }
                            else {
                                primer.setBlockState(x, k, z, Blocks.SAND.getDefaultState());
                            }
                        }
                        else if (depth < 4) {
                            if (dirt) {
                                primer.setBlockState(x, k, z, fillerBlock);
                            }
                            else {
                                primer.setBlockState(x, k, z, Blocks.SAND.getDefaultState());
                            }
                        }
                        else if (!dirt) {
                            primer.setBlockState(x, k, z, Blocks.SANDSTONE.getDefaultState());
                        }
                    }
                }
            }
        }
    }
}
