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

public class StaticBeam {
	Player player;
	MainSpell main;
	double damage;
	Particle particle;
	public StaticBeam(Player player, MainSpell main)
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
	public void staticbeam(){
	    new BukkitRunnable() {
	    	
	        // Number of points to display, evenly spaced around the circle's radius
	        //int circlePoints = 3;
	        // How fast should the particles rotate around the center beam
	        int rotationSpeed = 20;
	        double radius = 2.5;
	        Location startLoc = player.getEyeLocation();
	        World world = startLoc.getWorld();
	        final Vector dir = player.getLocation().getDirection().normalize().multiply(1);
	        //final double pitch = (startLoc.getPitch() +90.0F) * 0.017453292F;
	        //final double yaw = -startLoc.getYaw() * 0.017453292F;
	        // Particle offset increment for each loop
	        double increment = (2 * Math.PI) / rotationSpeed;
	        double circlePointOffset = 0; // This is used to rotate the circle as the beam progresses
	        int beamLength = 30;
	        double radiusShrinkage = radius / (double) ((beamLength + 2) / 2);
	        
	        @Override
	        public void run() {
	        	
	            // We are going to draw the entire beam instantly instead of waiting for each tick
	        	
	            for (int count = beamLength; count > 1; count--) {
	            	for (Entity entity : world.getNearbyEntities(startLoc, 5, 5, 5)) {
	                    if (entity instanceof LivingEntity) {
	                        if (entity == player) {
	                            continue;
	                        }
	                        Vector particleMinVector = new Vector(
	                                startLoc.getX() - 0.25,
	                                startLoc.getY() - 0.25,
	                                startLoc.getZ() - 0.25);
	                        Vector particleMaxVector = new Vector(
	                                startLoc.getX() + 0.25,
	                                startLoc.getY() + 0.25,
	                                startLoc.getZ() + 0.25);
	                        if(entity.getBoundingBox().overlaps(particleMinVector,particleMaxVector)){
	                            world.spawnParticle(Particle.FLASH, startLoc, 0);
	                            world.playSound(startLoc,Sound.ENTITY_GENERIC_EXPLODE,2,1);
	                            entity.setVelocity(entity.getVelocity().add(startLoc.getDirection().normalize().multiply(1.5)));
	                            ((Damageable) entity).damage(damage,player);
	                            world.spawnParticle(Particle.EXPLOSION_HUGE, startLoc, 0);
	                            this.cancel();
	                            return;
	                        }
	                    }
	                }
	                /*for (int i = 0; i < circlePoints; i++) {
	                    double x = radius * Math.cos(2 * Math.PI * i / circlePoints + circlePointOffset);
	                    double z = radius * Math.sin(2 * Math.PI * i / circlePoints + circlePointOffset);

	                    Vector vec = new Vector(x, 0, z);
	                    rotateAroundAxisX(vec, pitch);
	                    rotateAroundAxisY(vec, yaw);

	                    startLoc.add(vec);
	                    world.spawnParticle(Particle.FLAME, startLoc, 0);
	                    startLoc.subtract(vec);
	                }*/
	                
	                // Always spawn a center particle in the same direction the player was facing.
	                startLoc.add(dir);
	                world.spawnParticle(particle, startLoc, 0);
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
	            
	        }
	        
	    }.runTaskTimer((Plugin)main, 0, 1);
	}
}
