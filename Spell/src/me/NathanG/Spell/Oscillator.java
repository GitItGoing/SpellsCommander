package me.NathanG.Spell;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class Oscillator {
	Player player;
	MainSpell main;
	public Oscillator(Player player, MainSpell main)
	{
		this.player = player;
		this.main = main;
	}
	public void oscillation(){
	    new BukkitRunnable() {
	        // Number of points on each circle to show a particle
	        int circlePoints = 6;
	        // Maximum radius before shrinking again.
	        double maxRadius = 1.5;
	        Location playerLoc = player.getEyeLocation();
	        World world = playerLoc.getWorld();
	        // Get the player's looking direction and multiply it by 0.5
	        // 0.5 is the number of blocks each new ring will be away from the previous ring
	        final Vector dir = player.getLocation().getDirection().normalize().multiply(0.5);
	        final double pitch = (playerLoc.getPitch() + 90.0F) * 0.017453292F; // Need these in radians, not degrees or the circle flattens out sometimes
	        final double yaw = -playerLoc.getYaw() * 0.017453292F; // Need these in radians, not degrees or the circle flattens out sometimes
	        double increment = (2 * Math.PI) / circlePoints;
	        // This will be the maximum number of circles in one oscillation before repeating
	        int maxCircles = 12;
	        // This is used to calculate the radius for each loop
	        double t = 0;
	        double circlePointOffset = 0; // This is used to rotate the circle as the beam progresses
	        // Max beam length
	        int beamLength = 100;
	        @Override
	        public void run() {
	            beamLength--;
	            if(beamLength < 1){
	                this.cancel();
	                return;
	            }
	            // This calculates the radius for the current circle/ring in the pattern
	            double radius = Math.sin(t) * maxRadius;
	            for (int i = 0; i < circlePoints; i++) {
	                double angle = i * increment + circlePointOffset; // Angle on the circle
	                double x = radius * Math.cos(angle);
	                double z = radius * Math.sin(angle);
	                Vector vec = new Vector(x, 0, z);
	                VectorUtils.rotateAroundAxisX(vec, pitch);
	                VectorUtils.rotateAroundAxisY(vec, yaw);
	                playerLoc.add(vec);
	                world.spawnParticle(Particle.FIREWORKS_SPARK, playerLoc, 0); // Reminder to self - the "data" option for a (particle, location, data) is speed, not count!!
	                playerLoc.subtract(vec);
	            }
	            circlePointOffset += increment / 3; // Rotate the circle points each iteration, like rifling in a barrel
	            if (circlePointOffset >= increment) {
	                circlePointOffset = 0;
	            }
	            t += Math.PI / maxCircles; // Oscillation effect
	            if (t > Math.PI * 2) {
	                t = 0;
	            }
	            playerLoc.add(dir);
	        }
	    }.runTaskTimer(main, 0, 1);
	}
}
