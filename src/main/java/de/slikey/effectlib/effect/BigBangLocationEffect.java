package de.slikey.effectlib.effect;

import de.slikey.effectlib.EffectManager;
import de.slikey.effectlib.EffectType;
import de.slikey.effectlib.util.RandomUtils;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Builder;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.util.Vector;

@Deprecated
public class BigBangLocationEffect extends LocationEffect {

    public FireworkEffect firework;
    public int intensity = 2;
    public float radius = 2;
    public int explosions = 10;
    public int soundInterval = 5;
    protected int step = 0;

    public BigBangLocationEffect(EffectManager effectManager, Location location) {
        super(effectManager, location);
        type = EffectType.REPEATING;
        period = 2;
        iterations = 400;

        Builder b = FireworkEffect.builder();
        b.withColor(Color.RED).withColor(Color.ORANGE).withColor(Color.BLACK);
        b.withFade(Color.BLACK);
        b.trail(true);
        firework = b.build();
    }

    @Override
    public void onRun() {
        for (int i = 0; i < explosions; i++) {
            Vector v = RandomUtils.getRandomVector().multiply(radius);
            detonate(v);
            if (step % soundInterval == 0)
                location.getWorld().playSound(location, Sound.EXPLODE, 100, 1);
        }
        step++;
    }

    protected void detonate(Vector v) {
        final Firework fw = (Firework) location.getWorld().spawnEntity(location.add(v), EntityType.FIREWORK);
        location.subtract(v);
        FireworkMeta meta = fw.getFireworkMeta();
        meta.setPower(0);
        for (int i = 0; i < intensity; i++) {
            meta.addEffect(firework);
        }
        fw.setFireworkMeta(meta);
        fw.detonate();
    }
}
