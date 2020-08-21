package me.NathanG.Spell;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class RadialWave extends SpellMethods{
	Player player = super.player;
	MainSpell main = super.main;
	double damage = super.damage; 
	Particle particle = super.particle; //Single Particle
	int mana = super.mana;
	double cooldown = super.cooldown;
	String acts = super.acts;
	public RadialWave(Player player, MainSpell main, double damage, Particle particle, int mana, double cooldown, String acts)
	{
		super(player, main, damage, particle, mana, cooldown, acts);
	}
	public double getMana()
	{
		return mana;
	}
	public void spell(){
		World world = player.getWorld();
		new BukkitRunnable(){
			double t = Math.PI/4;
			Location loc = player.getLocation();
			public void run(){
				t = t + 0.1*Math.PI;
				for (double theta = 0; theta <= 2*Math.PI; theta = theta + Math.PI/32){
					double x = t*Math.cos(theta);
					double y = 2*Math.exp(-0.1*t) * Math.sin(t) + 1.5;
					double z = t*Math.sin(theta);
					loc.add(x,y,z);
					world.spawnParticle(particle, loc, 1, 0, 0, 0, 0);
					loc.subtract(x,y,z);
					
					theta = theta + Math.PI/64;
					
					x = t*Math.cos(theta);
					y = 2*Math.exp(-0.1*t) * Math.sin(t) + 1.5;
					z = t*Math.sin(theta);
					loc.add(x,y,z);
					world.spawnParticle(Particle.SPELL_WITCH, loc, 1, 0, 0, 0);
					loc.subtract(x,y,z);
				}
				if (t > 20){
					this.cancel();
				}
			}
						
		}.runTaskTimer(main, 0, 1);
	}
}
