package rtg.world.gen.terrain.vanilla;

import rtg.util.noise.CellNoise;
import rtg.util.noise.OpenSimplexNoise;
import rtg.world.gen.terrain.GroundEffect;
import rtg.world.gen.terrain.TerrainBase;

public class TerrainVanillaSavanna extends TerrainBase {

    private GroundEffect groundEffect = new GroundEffect(4f);

    public TerrainVanillaSavanna() {

    }

    @Override
    public float generateNoise(OpenSimplexNoise simplex, CellNoise cell, int x, int y, float border, float river) {
        //return terrainPlains(x, y, simplex, river, 160f, 10f, 60f, 100f, 66f);
        return riverized(65f + groundEffect.added(simplex, cell, x, y), river);
    }
}
