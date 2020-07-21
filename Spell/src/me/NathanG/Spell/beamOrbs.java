package me.NathanG.Spell;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class beamOrbs {
	Player player;
	MainSpell main;
	public beamOrbs(Player player, MainSpell main)
	{
		this.player = player;
		this.main = main;
	}
	public void orbs(){
	    new BukkitRunnable() {
	        // Number of points to display, evenly spaced around the circle's radius
	        int circlePoints = 3;
	        // How fast should the particles rotate around the center beam
	        int rotationSpeed = 20;
	        double radius = 2.5;
	        Location startLoc = player.getEyeLocation();
	        World world = startLoc.getWorld();
	        final Vector dir = player.getLocation().getDirection().normalize().multiply(1);
	        final double pitch = (startLoc.getPitch() +90.0F) * 0.017453292F;
	        final double yaw = -startLoc.getYaw() * 0.017453292F;
	        // Particle offset increment for each loop
	        double increment = (2 * Math.PI) / rotationSpeed;
	        double circlePointOffset = 0; // This is used to rotate the circle as the beam progresses
	        int beamLength = 60;
	        double radiusShrinkage = radius / (double) ((beamLength + 2) / 2);
	        @Override
	        public void run() {
	            beamLength--;
	            if(beamLength < 1){
	                this.cancel();
	            }
	            for (int i = 0; i < circlePoints; i++) {
	                double x =  radius * Math.cos(2 * Math.PI * i / circlePoints + circlePointOffset);
	                double z =  radius * Math.sin(2 * Math.PI * i / circlePoints + circlePointOffset);

	                Vector vec = new Vector(x, 0, z);
	                VectorUtils.rotateAroundAxisX(vec, pitch);
	                VectorUtils.rotateAroundAxisY(vec, yaw);

	                startLoc.add(vec);
	                world.spawnParticle(Particle.FLAME, startLoc, 0);
	                startLoc.subtract(vec);
	            }
	            // Always spawn a center particle in the same direction the player was facing.
	            startLoc.add(dir);
	            world.spawnParticle(Particle.FIREWORKS_SPARK, startLoc, 0);
	            startLoc.subtract(dir);

	            // Shrink each circle radius until it's just a point at the end of a long swirling cone
	            radius -= radiusShrinkage;
	            if (radius < 0) {
	                this.cancel();
	            }
	           
	            // Rotate the circle points each iteration, like rifling in a barrel
	            circlePointOffset += increment;
	            if (circlePointOffset >= (2 * Math.PI)) {
	                circlePointOffset = 0;
	            }
	            startLoc.add(dir);
	        }
	    }.runTaskTimer(main, 0, 3);
	}
}
