package me.NathanG.Spell;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class ParticleBeam {
	Player player;
	MainSpell main;
	double damage;
	Particle particle;
	public ParticleBeam(Player player, MainSpell main)
	{
		this.player = player;
		this.main = main;	
	}
	public void setDamage(double dmg)
	{
		damage = dmg;
	}
	public void setParticle(Particle particle)
	{
		this.particle = particle;
	}
	public void particleBeam()
	{
	    // Player's eye location is the starting location for the particle
	    Location startLoc = player.getEyeLocation();

	    // We need to clone() this location, because we will add() to it later.
	    Location particleLoc = startLoc.clone();

	    World world = startLoc.getWorld(); // We need this later to show the particle

	    // dir is the Vector direction (offset from 0,0,0) the player is facing in 3D space
	    Vector dir = startLoc.getDirection();

	    /* vecOffset is used to determine where the next particle should appear
	    We are taking the direction and multiplying it by 0.5 to make it appear 1/2 block
	      in its continuing Vector direction.
	    NOTE: We have to clone() because multiply() modifies the original variable!
	    For a straight beam, we only need to calculate this once, as the direction does not change.
	    */
	    Vector vecOffset = dir.clone().multiply(0.5);

	    new BukkitRunnable(){
	        int maxBeamLength = 30; // Max beam length
	        int beamLength = 0; // Current beam length

	        // The run() function runs every X number of ticks - see below
	        public void run(){
	            // Search for any entities near the particle's current location
	            for (Entity entity : world.getNearbyEntities(particleLoc, 5, 5, 5)) {
	                // We only care about living entities. Any others will be ignored.
	                if (entity instanceof LivingEntity) {
	                    // Ignore player that initiated the shot
	                    if (entity == player) {
	                        continue;
	                    }

	                    /* Define the bounding box of the particle.
	                    We will use 0.25 here, since the particle is moving 0.5 blocks each time.
	                    That means the particle won't miss very small entities like chickens or bats,
	                      as the particle bounding box covers 1/2 of the movement distance.
	                     */
	                    Vector particleMinVector = new Vector(
	                            particleLoc.getX() - 0.25,
	                            particleLoc.getY() - 0.25,
	                            particleLoc.getZ() - 0.25);
	                    Vector particleMaxVector = new Vector(
	                            particleLoc.getX() + 0.25,
	                            particleLoc.getY() + 0.25,
	                            particleLoc.getZ() + 0.25);

	                    // Now use a spigot API call to determine if the particle is inside the entity's hitbox
	                    if(entity.getBoundingBox().overlaps(particleMinVector,particleMaxVector)){
	                        // We have a hit!
	                        // Display a flash at the location of the particle
	                        world.spawnParticle(Particle.FLASH, particleLoc, 0);
	                        // Play an explosion sound at the particle location
	                        world.playSound(particleLoc,Sound.ENTITY_GENERIC_EXPLODE,2,1);

	                        // Knock-back the entity in the same direction from where the particle is coming.
	                        entity.setVelocity(entity.getVelocity().add(particleLoc.getDirection().normalize().multiply(1.5)));

	                        // Damage the target, using the shooter as the damager
	                        ((Damageable) entity).damage(damage,player);
	                        // Cancel the particle beam
	                        this.cancel();
	                        // We must return here, otherwise the code below will display one more particle.
	                        return;
	                    }
	                }
	            }

	            beamLength ++; // This is the distance between each particle

	            // Kill this task if the beam length is max
	            if(beamLength >= maxBeamLength){
	                world.spawnParticle(Particle.FLASH, particleLoc, 0);
	                this.cancel();
	                return;
	            }

	            // Now we add the direction vector offset to the particle's current location
	            particleLoc.add(vecOffset);

	            // Display the particle in the new location
	            //System.out.println(particle);
	            world.spawnParticle(particle, particleLoc, 0);
	        }
	    }.runTaskTimer((Plugin)main,0, 1);
	    // 0 is the delay in ticks before starting this task
	    // 1 is the how often to repeat the run() function, in ticks (20 ticks are in one second)
	}
}
