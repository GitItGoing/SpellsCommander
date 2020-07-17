package me.NathanG.Spell;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class ConeSpell {
	public static final Vector rotateAroundAxisX(Vector v, double angle) {
        double cos = Math.cos(angle);
        double sin = Math.sin(angle);
        double y = v.getY() * cos - v.getZ() * sin;
        double z = v.getY() * sin + v.getZ() * cos;
        return v.setY(y).setZ(z);
    }

    public static final Vector rotateAroundAxisY(Vector v, double angle) {
        double cos = Math.cos(angle);
        double sin = Math.sin(angle);
        double x = v.getX() * cos + v.getZ() * sin;
        double z = v.getX() * -sin + v.getZ() * cos;
        return v.setX(x).setZ(z);
    }
	public void particleTutorial(Player player){
	    new BukkitRunnable() {
	        // Number of points in each circle
	        int circlePoints = 10;
	        // radius of the circle
	        double radius = 2;
	        // Starting location for the first circle will be the player's eye location
	        Location playerLoc = player.getEyeLocation();
	        // We need world for the spawnParticle function
	        World world = playerLoc.getWorld();
	        // This is the direction the player is looking, normalized to a length (speed) of 1.
	        final Vector dir = player.getLocation().getDirection().normalize();
	        // We need the pitch in radians for the rotate axis function
	        // We also add 90 degrees to compensate for the non-standard use of pitch degrees in Minecraft.
	        final double pitch = (playerLoc.getPitch() + 90.0F) * 0.017453292F;
	        // The yaw is also converted to radians here, but we need to negate it for the function to work properly
	        final double yaw = -playerLoc.getYaw() * 0.017453292F;
	        // This is the distance between each point around the circumference of the circle.
	        double increment = (2 * Math.PI) / circlePoints;
	        // This is used to rotate the circle as the beam progresses
	        double circlePointOffset = 0;
	        // Max length of the beam..for now
	        int beamLength = 30;
	        // This is the amount we will shrink the circle radius with each loop
	        double radiusShrinkage = radius / (double) ((beamLength + 2) / 2);
	        @Override
	        public void run() {
	            beamLength--;
	            if(beamLength < 1){
	                this.cancel();
	                return;
	            }
	            // We need to loop to get all of the points on the circle every loop
	            for (int i = 0; i < circlePoints; i++) {
	                // Angle on the circle + the offset for rotating each loop
	                double angle = i * increment + circlePointOffset;
	                double x = radius * Math.cos(angle);
	                double z = radius * Math.sin(angle);
	                // Convert that to a 3D Vector where the height is always 0
	                Vector vec = new Vector(x, 0, z);
	                // Now rotate the circle point so it's properly aligned no matter where the player is looking:
	                rotateAroundAxisX(vec, pitch);
	                rotateAroundAxisY(vec, yaw);
	                playerLoc.add(vec);
	                world.spawnParticle(Particle.FLAME, playerLoc, 0); // Reminder to self - the "data" option for a (particle, location, data) is speed, not count!!
	                
	                playerLoc.subtract(vec);
	            }
	            circlePointOffset += increment / 3;
	            
	            if (circlePointOffset >= increment) {
	                circlePointOffset = 0;
	            }
	            radius -= radiusShrinkage;
	            if (radius < 0) {
	                this.cancel();
	                return;
	            }
	            playerLoc.add(dir);
	        }
	    }.runTaskTimer((Plugin)this, 0, 1);
	}
}
