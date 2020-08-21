package me.NathanG.Spell;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class DeathStarBeam extends SpellMethods{
	Player player = super.player;
	MainSpell main = super.main;
	double damage = super.damage; 
	Particle particle = super.particle; //Single Particle
	int mana = super.mana;
	double cooldown = super.cooldown;
	String acts = super.acts;
	public DeathStarBeam(Player player, MainSpell main, double damage, Particle particle, int mana, double cooldown, String acts)
	{
		super(player, main, damage, particle, mana, cooldown, acts);
	}
	public double getMana()
	{
		return mana;
	}
	public void spell()
	{
	    new BukkitRunnable() {	
	        // Number of points in each circle
	        int circlePoints = 10;
	        // radius of the circle
	        double radius = 5;
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
	        // Max length of the beam..for now
	        double length = 30;
	        @Override
	        public void run() {
	            length--;
	            if(length < 1){
	                this.cancel();
	                return;
	            }
	            // We need to loop to get all of the points on the circle every loop
	            for (int i = 0; i < circlePoints; i++) {
	                double angle = i * increment;
	                double x = radius * Math.cos(angle);
	                double z = radius * Math.sin(angle);
	                // Convert that to a 3D Vector where the height is always 0
	                Vector vec = new Vector(x, 0, z);
	                // Now rotate the circle point so it's properly aligned no matter where the player is looking:
	                VectorUtils.rotateAroundAxisX(vec, pitch);
	                VectorUtils.rotateAroundAxisY(vec, yaw);
	                // Add that vector to the player's current location
	                playerLoc.add(vec);
	                // Display the particle
	                world.spawnParticle(Particle.FLAME, playerLoc, 0, 0, 0, 0); // Reminder to self - the "data" option for a (particle, location, data) is speed, not count!!
	                // Since add() modifies the original variable, we have to subtract() it so the next calculation starts from the same location as this one.
	                playerLoc.subtract(vec);
	            }
	            double radiusShrinkage = radius / (double) ((length) / 5);
	            radius -= radiusShrinkage;
                if (radius < 0) {
                    this.cancel();
                    return;
                }
	            /* We multiplied this by 1 already (using normalize()), ensuring the beam will
	               travel one block away from the player each loop.
	             */
	            
	            playerLoc.add(dir);
	            
	        }
	        
	    }.runTaskTimer(main, 0, 1);
	    StaticBeam staticbeam = new StaticBeam(player, main, damage, particle, mana, cooldown, acts);
	    staticbeam.spell();
	}
}
