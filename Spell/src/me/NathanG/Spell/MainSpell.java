package me.NathanG.Spell;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class MainSpell extends JavaPlugin implements Listener{
	Player player;
	ConeSpell conespell = new ConeSpell();
	public void onEnable()
	{
		this.getServer().getPluginManager().registerEvents(this, this);
	}
	public void onDisable()
	{
		
	}
	@EventHandler (priority = EventPriority.MONITOR)
    // You can name the function anything you want. "clickAction" is appropriate here.
    public void clickAction(PlayerInteractEvent event) {
        // The player that triggered the event
        player = event.getPlayer();
        // If the player left-clicked the air, and has an end rod in their hand
        if(event.getAction() == Action.LEFT_CLICK_AIR && player.getInventory().getItemInMainHand().getType() == Material.END_ROD){
            // Display the particle beam
            particleBeam(player);
        }
        else if(event.getAction() == Action.RIGHT_CLICK_AIR && player.getInventory().getItemInMainHand().getType() == Material.END_ROD)
        {
        	spiral(player);
        }
        else if(event.getAction() == Action.RIGHT_CLICK_AIR && player.getInventory().getItemInMainHand().getType() == Material.BLAZE_ROD)
        {
        	particleTutorial(player);
        }
        else if(event.getAction() == Action.LEFT_CLICK_AIR && player.getInventory().getItemInMainHand().getType() == Material.BLAZE_ROD)
        {
        	beamOrbs(player);
        }
        else if(event.getAction() == Action.LEFT_CLICK_AIR)
        {
        	staticbeam();
        }
    }
	public void particleBeam(Player player){
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
	    Vector vecOffset = dir.clone().multiply(2);

	    new BukkitRunnable(){
	        int maxBeamLength = 100; // Max beam length
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
	                        ((Damageable) entity).damage(100,player);
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
	            world.spawnParticle(Particle.FIREWORKS_SPARK, particleLoc, 0);
	        }
        }.runTaskTimer((Plugin) this, 0, 1);
        // 0 is the delay in ticks before starting this task
        // 1 is the how often to repeat the run() function, in ticks (20 ticks are in one second)
	}
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
	public void spiral(Player player){
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
	@EventHandler
	public void particleTutorial(Player player){
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
	                rotateAroundAxisX(vec, pitch);
	                rotateAroundAxisY(vec, yaw);
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
	    }.runTaskTimer((Plugin)this, 0, 1);
	}
	public void beamOrbs(Player player){
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
	                rotateAroundAxisX(vec, pitch);
	                rotateAroundAxisY(vec, yaw);

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
	    }.runTaskTimer((Plugin)this, 0, 3);
	}
	public void staticbeam(){
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
	                            ((Damageable) entity).damage(100,player);
	                            this.cancel();
	                            return;
	                        }
	                    }
	                }
	                for (int i = 0; i < circlePoints; i++) {
	                    double x = radius * Math.cos(2 * Math.PI * i / circlePoints + circlePointOffset);
	                    double z = radius * Math.sin(2 * Math.PI * i / circlePoints + circlePointOffset);

	                    Vector vec = new Vector(x, 0, z);
	                    rotateAroundAxisX(vec, pitch);
	                    rotateAroundAxisY(vec, yaw);

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
	            
	        }
	        
	    }.runTaskTimer((Plugin)this, 0, 1);
	}
	public void angelWings()
	{
		
	}
 
}

