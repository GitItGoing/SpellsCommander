package me.NathanG.Spell;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import me.NathanG.Spell.TableGenerator;
import me.NathanG.Spell.TableGenerator.Alignment;
import me.NathanG.Spell.TableGenerator.Receiver;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

@SuppressWarnings("unused")
public class MainSpell extends JavaPlugin implements Listener{
	String firstKeyword;
	int editArg;
	private Connection connection;
	ItemStack testwand;
	String effect;
	Player player;
	List<String> args;
	List<String> spellconfigs;
	boolean isFinished = false;
	boolean isParticleSet = false;
	double damage;
	Particle particle;
	String name;
	String creator;
	String doc;
	String id;
	String proj;
	String mana;
	String cooldwn;
	String acts;
	String parts;
	String fx;
	boolean inEditor = false;
	int testWandId;
	ParticleBeam beam;
	Spiral spiral;
	RadialWave wave;
	StaticBeam staticbeam;
	private String host, database, username, password;
    private int port;
	public void onEnable()
	{
		this.getServer().getPluginManager().registerEvents(this, this);
		spellconfigs = new ArrayList<String>();
		spellconfigs.add("spiral");
		spellconfigs.add("static");
		spellconfigs.add("beam");
		spellconfigs.add("radialwave");
		host = "localhost";
        port = 3306;
        database = "spells";
        username = "root";
        password = "druidmcmysql";    
        try {    
            shipToSQL();          
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
	}
	public void onDisable()
	{
		
	}
	public void cleanUp()
	{
		testwand = null;
		effect = "";
		player = null;
		args = new ArrayList<String>();
		spellconfigs = new ArrayList<String>();
		isParticleSet = false;
		damage = 0;
		Particle particle;
		String name;
		int testWandId;
	}
	public void shipToSQL() throws SQLException, ClassNotFoundException 
	{
		
	    if (connection != null && !connection.isClosed()) 
	    {
	    	BukkitRunnable runnable = new BukkitRunnable() 
	    	{
			   @Override
			   public void run()
			   {
				   if(firstKeyword == "create")
				   {
						Date date = Date.valueOf(LocalDate.now());String sql = "insert into allspells(SpellName, Creator, DateOfCreation, Projectile, Mana, Cooldown, Actions, Particles, Effect)"
						+ "VALUES(?,?,?,?,?,?,?,?,?);";
						PreparedStatement st;
						try 
						{
							System.out.println("SQL Input: " + name + " " + player.getName() + " " + date + " " + String.valueOf(particle));
							st = connection.prepareStatement(sql);
							st.setString(1, name);
							st.setString(2, player.getName());
							st.setDate(3, date);
							st.setString(4, "Invisible");
							st.setInt(5, 0);
							st.setInt(6, 0);
							st.setString(7, "damage " + damage);
							st.setString(8, String.valueOf(particle));
							st.setString(9, effect);
							st.executeUpdate();
						}
						catch (SQLException e) 
						{
							e.printStackTrace();
						}
					}
				   else if(firstKeyword == "edit")
				   {
						
						try {
							String sql = "insert into allspells(SpellName, Creator, DateOfCreation, Projectile, Mana, Cooldown, Actions, Particles, Effect)"
									+ "VALUES(?,?,?,?,?,?,?,?,?);";
							PreparedStatement st;
							Date date = Date.valueOf(LocalDate.now());
							System.out.println("SQL Input: " + name + " " + player.getName() + " " + date + " " + String.valueOf(particle));
							st = connection.prepareStatement(sql);
							st.setString(1, name);
							st.setString(2, player.getName());
							st.setDate(3, date);
							st.setString(4, "Invisible");
							st.setInt(5, 0);
							st.setInt(6, 0);
							st.setString(7, "damage " + damage);
							st.setString(8, String.valueOf(particle));
							st.setString(9, effect);
							st.executeUpdate();
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
							
				   }
				   else if(firstKeyword == "remove")
				   {
						
						try {
							PreparedStatement rmst = connection.prepareStatement("DELETE FROM allspells WHERE ID = ?;");
							rmst.setInt(1, editArg);
							rmst.executeUpdate(); 
							player.sendMessage("Removed Spell from Database");
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							player.sendMessage("Error removing.");
						}
						
				   }
			   }
	    	};		 
	    	runnable.runTaskAsynchronously(this);
	    }
	    else
	    {
	    	System.out.println("Failed");
	    }
	    synchronized (this) 
	    {
	        if (connection != null && !connection.isClosed()) {
	            return;
	        }
	        Class.forName("com.mysql.jdbc.Driver");
	        connection = DriverManager.getConnection("jdbc:mysql://" + this.host + ":" + this.port + "/" + this.database, this.username, this.password);
	    }
	}
	public void spellMaker()
	{
		if(effect.equalsIgnoreCase("Spiral"))
		{
			spiral = new Spiral(player, this);
			spiral.setDamage(damage);
        	spiral.setParticle(particle);
			isFinished = false;
		}
		if(effect.equalsIgnoreCase("Static"))
		{
			staticbeam = new StaticBeam(player, this);
			staticbeam.setDamage(damage);
        	staticbeam.setParticle(particle);
			isFinished = false;
		}
		if(effect.equalsIgnoreCase("RadialWave"))
		{
			wave = new RadialWave(player, this);
			wave.setDamage(damage);
        	wave.setParticle(particle);
			isFinished = false;
		}
		if(effect.equalsIgnoreCase("Beam"))
		{
			beam = new ParticleBeam(player, this);
        	beam.setDamage(damage);
        	beam.setParticle(particle);
			isFinished = false;   
		}
	}
	@EventHandler (priority = EventPriority.MONITOR)
    // You can name the function anything you want. "clickAction" is appropriate here.
    public void clickAction(PlayerInteractEvent event) {
        player = event.getPlayer();
    	if(isFinished && event.getAction() == Action.LEFT_CLICK_AIR)
    	{
    		if(isFinished)
    		{
    			giveItem();
    			System.out.println(isFinished);
    			System.out.println(damage);
            	System.out.println("Main Spell: " + particle);
            	spellMaker();
    		}		
    	}
    	if(checker() && !isFinished)
    	{
    		if(effect.equalsIgnoreCase("Spiral"))
    		{
    			spiral.spiral();
    		}
    		if(effect.equalsIgnoreCase("Static"))
    		{
    			staticbeam.staticbeam();
    		}
    		if(effect.equalsIgnoreCase("RadialWave"))
    		{
    			wave.blank();
    		}
    		if(effect.equalsIgnoreCase("Beam"))
    		{
    			beam.particleBeam();
    		}
        }
    }
	public double canBeParsed(String toParse)
	{
		double toReturn = 0;
		try{
			toReturn = Double.parseDouble(toParse);
	    }
	    catch(Exception e){
	    	return toReturn;
	    }
		return toReturn;
	}
	@EventHandler
	public void parser(AsyncPlayerChatEvent event)
	{
		String message = event.getMessage();
		String str[] = message.split(" ");
		args = Arrays.asList(str);
		if(args.get(0).contains("create"))
		{
			firstKeyword = "create";
			printCreate();
		}
		if(args.get(0).contains("particles"))
		{
			player.sendMessage("ASH, BARRIER, BLOCK_CRACK, BLOCK_DUST, BUBBLE_COLUMN_UP, BUBBLE_POP, CAMPFIRE_COSY_SMOKE, CAMPFIRE_SIGNAL_SMOKE, CLOUD, COMPOSTER, CRIMSON_SPORE, CRIT, CRIT_MAGIC, CURRENT_DOWN, DAMAGE_INDICATOR, DOLPHIN, DRAGON_BREATH, DRIP_LAVA, DRIP_WATER, DRIPPING_HONEY, DRIPPING_OBSIDIAN_TEAR, ENCHANTMENT_TABLE, END_ROD, EXPLOSION_HUGE, EXPLOSION_LARGE, EXPLOSION_NORMAL, FALLING_DUST, FALLING_HONEY, FALLING_LAVA, FALLING_NECTAR, FALLING_OBSIDIAN_TEAR, FALLING_WATER, FIREWORKS_SPARK, FLAME, FLASH, HEART, ITEM_CRACK, LANDING_HONEY, LANDING_LAVA, LANDING_OBSIDIAN_TEAR, LAVA, LEGACY_BLOCK_CRACK, LEGACY_BLOCK_DUST, LEGACY_FALLING_DUST, MOB_APPEARANCE, NAUTILUS, NOTE, PORTAL, REDSTONE, REVERSE_PORTAL, SLIME, SMOKE_LARGE, SMOKE_NORMAL, SNEEZE, SNOW_SHOVEL, SNOWBALL, SOUL, SOUL_FIRE_FLAME, SPELL, SPELL_INSTANT, SPELL_MOB, SPELL_MOB_AMBIENT, SPELL_WITCH, SPIT, SQUID_INK, SUSPENDED, SUSPENDED_DEPTH, SWEEP_ATTACK, TOTEM, TOWN_AURA, VILLAGER_ANGRY, VILLAGER_HAPPY, WARPED_SPORE, WATER_BUBBLE, WATER_DROP, WATER_SPLASH, WATER_WAKE, WHITE_ASH".toLowerCase());
		}
		if(args.get(0).contains("edit"))
		{
			if(args.get(0).contains("edit") && canBeParsed(args.get(1)) != 0 && canBeParsed(args.get(1)) != 0.0)
			{
				firstKeyword = "edit";
				printCreate();
				getFromSQL();
				editArg = Integer.parseInt(args.get(1));
			}
			else
			{
				player.sendMessage("Usage is: edit <id> or edit <spellname>");
			}
		}
		if(args.get(0).contains("remove") || args.get(0).contains("delete"))
		{
			if((args.get(0).contains("remove") || args.get(0).contains("delete")) && canBeParsed(args.get(1)) != 0 && canBeParsed(args.get(1)) != 0.0)
			{
				firstKeyword = "remove";
				try {
					shipToSQL();
				} catch (Exception e) {
					e.printStackTrace();
				}
				editArg = Integer.parseInt(args.get(1));
			}
			else
			{
				player.sendMessage("Usage is: remove <id> or edit <spellname>");
			}
		}
		if(args.get(0).contains("showall"))
		{

			firstKeyword = "getall";
			try {
				System.out.println("Here");
				getFromSQL();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		event.setCancelled(true);
		//----- Within Editor -----
		
		for(int i = 0; i<args.size(); i++)
		{
			
			if(args.get(i).contains("name") && inEditor == true)
			{
				player.sendMessage("");
				player.sendMessage("New Spells name is: " + args.get(i+1));
				name = args.get(1);
			}
			if(args.get(i).contains("particle") && inEditor == true)
			{
				player.sendMessage("");
				System.out.println(i);
				if(isParticle(i))
					player.sendMessage("New Spells particle is: Particle." + args.get(i+1).toUpperCase());
				else
					System.out.println("No particle!");
			}
			if((args.get(i).contains("damage") || args.get(0).contains("dmg")) && inEditor == true)
			{
				player.sendMessage("");
				player.sendMessage("New Spells damage is: " + args.get(i+1));
				damage = Double.parseDouble(args.get(i+1));
			}
			if(args.get(i).contains("effect"))
			{
				player.sendMessage("");
				for(int j = 0; j<spellconfigs.size(); j++)
				{
					if(args.get(i+1).equals(spellconfigs.get(j)))
					{
						effect = spellconfigs.get(j);
						player.sendMessage("New Spells effect is: " + args.get(i+1));
					}
				}
				if(effect.equals(""))
				{
					player.sendMessage("Input invalid.");
				}
			}
			if((args.get(0).contains("close") || args.get(0).contains("exit") || (args.get(0).contains("leave") || args.get(0).contains("out"))) && inEditor == true)
			{
				player.sendMessage("");
				player.sendMessage("Leaving editor.");
				inEditor = false;
				isFinished = true;
			}
			if((args.get(0).contains("specs") && inEditor == true))
			{
				player.sendMessage("Specs of spell are: name > " + name + " dmg > " + damage);
			}
			if(args.get(0).contains("finish") && inEditor == true)
			{
				player.sendMessage("");
				player.sendMessage("Finishing Spell. ");
				
				System.out.println(damage);
				System.out.println(particle);
				System.out.println(effect);
				try {
					shipToSQL();
				} catch (Exception e) 
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				inEditor = false;
				isFinished = true;
			}
		}
		
		event.setCancelled(true);
	}
	public List<String> getFromSQL()
	{
		System.out.println("Here in getFromSQL");
		if(!firstKeyword.equalsIgnoreCase("getall"))
		{
			System.out.println("Here in firs if");
			List<String> allInput = new ArrayList<String>();
			try 
			{
				 String sql = "SELECT * FROM allspells WHERE ID = ?;"; 
				 PreparedStatement st;
				 st = connection.prepareStatement(sql);
				 if(String.valueOf(args.get(1)).length() != 0)
					 st.setString(1, String.valueOf(args.get(1)));
				 else
					 player.sendMessage("Something went wrong.");
				 ResultSet result = st.executeQuery();
		         result.next();
	        	 name = result.getString("SpellName");
	        	 creator = result.getString("Creator");
	        	 doc = result.getString("DateOfCreation");
	        	 id = result.getString("ID");
	        	 proj = result.getString("Projectile");
	        	 mana = result.getString("Mana");
	        	 cooldwn = result.getString("Cooldown");
	        	 acts = result.getString("Actions");
	        	 parts = result.getString("Particles");
	        	 fx = result.getString("Effect");
	        	 player.sendMessage(name);
		         player.sendMessage(creator);
		         player.sendMessage(doc);
		         player.sendMessage(id);
		         player.sendMessage(proj);
		         player.sendMessage(mana);
		         player.sendMessage(cooldwn);
		         player.sendMessage(acts);
		         player.sendMessage(parts);
		         player.sendMessage(fx);
		         allInput.add(name);allInput.add(creator);allInput.add(doc);allInput.add(id);allInput.add(proj);
		         allInput.add(mana);allInput.add(cooldwn);allInput.add(acts);allInput.add(parts);allInput.add(fx);
	        	 for(int i = 0;  i<allInput.size(); i++)
	        	 {
	        		//String[] msg =  allInput.get(i);
	        		List<String> split;
	        		String[] msg = allInput.get(i).split(" ");    			
	    			split = Arrays.asList(msg);
	    			System.out.println(split);
	    			setActionValues(split);
	        	 }
			 } 
			catch(SQLException e) 
			{
			     e.printStackTrace();
			}
		}
		else if(firstKeyword.equalsIgnoreCase("getall"))
		{
			System.out.println("Here in getall");
			try {
				String sql = "select * from allspells;"; 
				PreparedStatement st;
				st = connection.prepareStatement(sql);
				ResultSet result = st.executeQuery();
				System.out.println("Here before while");
				for(int k=0; k < 100; k++)
				{
					player.sendMessage("");
				}
				TableGenerator tg = new TableGenerator(Alignment.LEFT, Alignment.LEFT, Alignment.LEFT, Alignment.LEFT);
				while(result.next())
				{
					System.out.println("Here in while");
					tg.addRow("§b" + result.getString("SpellName"), "§b" + result.getString("Creator"), "§b" + result.getString("DateOfCreation"), "§b" + result.getString("ID")); 
				}
				for (String line : tg.generate(Receiver.CLIENT, false, false)) {
		         	   player.sendMessage(line);
		        }
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println("Problem");
			}
		}
		else
		{
			System.out.println("Didnt pass either case");
		}
		return null;
	}
	public String setActionValues(List<String> list)
	{
		//Checking actions
		for(int i = 0; i<list.size(); i++)
		{
			if(list.get(i).equalsIgnoreCase("damage"))
			{
				player.sendMessage(list.get(i+1));
				damage = Double.parseDouble(list.get(i+1));
				System.out.println(damage);
				player.sendMessage(String.valueOf(damage));
			    return String.valueOf(damage);
			}
		}
		return "Empty";
	}
	public boolean isParticle(int i)
	{
		if(args.get(i+1).contains("barrier"))
		{
			particle = Particle.BARRIER;
			isParticleSet = true;
		}
		else if(args.get(i+1).contains("cozy"))
		{
			particle = Particle.CAMPFIRE_COSY_SMOKE;
			isParticleSet = true;
		}
		else if(args.get(i+1).contains("signal"))
		{
			particle = Particle.CAMPFIRE_SIGNAL_SMOKE;
			isParticleSet = true;
		}
		else if(args.get(i+1).contains("cloud"))
		{
			particle = Particle.CLOUD;
			isParticleSet = true;
		}
		else if(args.get(i+1).contains("composter"))
		{
			particle = Particle.COMPOSTER;
			isParticleSet = true;
		}
		else if(args.get(i+1).contains("crit_magic"))
		{
			particle = Particle.CRIT_MAGIC;
			isParticleSet = true;
		}
		else if(args.get(i+1).contains("crit"))
		{
			particle = Particle.CRIT;
			isParticleSet = true;
		}
		else if(args.get(i+1).contains("damage_indicator"))
		{
			particle = Particle.DAMAGE_INDICATOR;
			isParticleSet = true;
		}
		else if(args.get(i+1).contains("dolphin"))
		{
			particle = Particle.DOLPHIN;
			isParticleSet = true;
		}
		else if(args.get(i+1).contains("dragon"))
		{
			particle = Particle.DRAGON_BREATH;
			isParticleSet = true;
		}
		else if(args.get(i+1).contains("drip_lava"))
		{
			particle = Particle.DRIP_LAVA;
			isParticleSet = true;
		}
		else if(args.get(i+1).contains("drip_water"))
		{
			particle = Particle.DRIP_WATER;
			isParticleSet = true;
		}
		else if(args.get(i+1).contains("dripping_honey"))
		{
			particle = Particle.DRIPPING_HONEY;
			isParticleSet = true;
		}
		else if(args.get(i+1).contains("enchantment"))
		{
			particle = Particle.ENCHANTMENT_TABLE;
			isParticleSet = true;
		}
		else if(args.get(i+1).contains("end_rod"))
		{
			particle = Particle.END_ROD;
			isParticleSet = true;
		}
		else if(args.get(i+1).contains("explosion_huge"))
		{
			particle = Particle.EXPLOSION_HUGE;
			isParticleSet = true;
		}
		else if(args.get(i+1).contains("explosion_large"))
		{
			particle = Particle.EXPLOSION_LARGE;
			isParticleSet = true;
		}
		else if(args.get(i+1).contains("explosion"))
		{
			particle = Particle.EXPLOSION_NORMAL;
			isParticleSet = true;
		}
		else if(args.get(i+1).contains("falling_dustr"))
		{
			particle = Particle.FALLING_DUST;
			isParticleSet = true;
		}
		else if(args.get(i+1).contains("falling_honey"))
		{
			particle = Particle.FALLING_HONEY;
			isParticleSet = true;
		}
		else if(args.get(i+1).contains("falling_lava"))
		{
			particle = Particle.FALLING_LAVA;
			isParticleSet = true;
		}
		else if(args.get(i+1).contains("falling_nectar"))
		{
			particle = Particle.FALLING_NECTAR;
			isParticleSet = true;
		}
		else if(args.get(i+1).contains("falling_water"))
		{
			particle = Particle.FALLING_WATER;
			isParticleSet = true;
		}
		else if(args.get(i+1).contains("firework"))
		{
			particle = Particle.FIREWORKS_SPARK;
			isParticleSet = true;
		}
		else if(args.get(i+1).contains("flame"))
		{
			particle = Particle.FLAME;
			System.out.println(particle);
			isParticleSet = true;
		}
		else if(args.get(i+1).contains("flash"))
		{
			particle = Particle.FLASH;
			isParticleSet = true;
		}
		else if(args.get(i+1).contains("heart"))
		{
			particle = Particle.HEART;
			isParticleSet = true;
		}
		else if(args.get(i+1).contains("item_crack"))
		{
			particle = Particle.ITEM_CRACK;
			isParticleSet = true;
		}
		else if(args.get(i+1).contains("landing_honey"))
		{
			particle = Particle.LANDING_HONEY;
			isParticleSet = true;
		}
		else if(args.get(i+1).contains("landing_lava"))
		{
			particle = Particle.LANDING_LAVA;
			isParticleSet = true;
		}
		else if(args.get(i+1).contains("lava"))
		{
			particle = Particle.LAVA;
			isParticleSet = true;
		}
		else if(args.get(i+1).contains("nautilus"))
		{
			particle = Particle.NAUTILUS;
			isParticleSet = true;
		}
		else if(args.get(i+1).contains("note"))
		{
			particle = Particle.NOTE;
			isParticleSet = true;
		}
		else if(args.get(i+1).contains("portal"))
		{
			particle = Particle.PORTAL;
			isParticleSet = true;
		}
		else if(args.get(i+1).contains("redstone"))
		{
			particle = Particle.REDSTONE;
			isParticleSet = true;
		}
		else if(args.get(i+1).contains("slime"))
		{
			particle = Particle.SLIME;
			isParticleSet = true;
		}
		else if(args.get(i+1).contains("smoke_large"))
		{
			particle = Particle.SMOKE_LARGE;
			isParticleSet = true;
		}
		else if(args.get(i+1).contains("smoke_normal"))
		{
			particle = Particle.SMOKE_NORMAL;
			isParticleSet = true;
		}
		else if(args.get(i+1).contains("sneeze"))
		{
			particle = Particle.SNEEZE;
			isParticleSet = true;
		}
		else if(args.get(i+1).contains("snow_shovel"))
		{
			particle = Particle.SNOW_SHOVEL;
			isParticleSet = true;
		}
		else if(args.get(i+1).contains("spell_instant"))
		{
			particle = Particle.SPELL_INSTANT;
			isParticleSet = true;
		}
		else if(args.get(i+1).contains("spell_mob"))
		{
			particle = Particle.SPELL_MOB;
			isParticleSet = true;
		}
		else if(args.get(i+1).contains("spell_mob_ambient"))
		{
			particle = Particle.SPELL_MOB_AMBIENT;
			isParticleSet = true;
		}
		else if(args.get(i+1).contains("spell_witch"))
		{
			particle = Particle.SPELL_WITCH;
			isParticleSet = true;
		}
		else if(args.get(i+1).contains("spell"))
		{
			particle = Particle.SPELL;
			isParticleSet = true;
		}
		else if(args.get(i+1).contains("spit"))
		{
			particle = Particle.SPIT;
			isParticleSet = true;
		}
		else if(args.get(i+1).contains("squid") || args.get(i+1).contains("ink"))
		{
			particle = Particle.SQUID_INK;
			isParticleSet = true;
		}
		else if(args.get(i+1).contains("suspended_depth"))
		{
			particle = Particle.SUSPENDED_DEPTH;
			isParticleSet = true;
		}
		else if(args.get(i+1).contains("suspend"))
		{
			particle = Particle.SUSPENDED;
			isParticleSet = true;
		}
		else if(args.get(i+1).contains("sweep"))
		{
			particle = Particle.SWEEP_ATTACK;
			isParticleSet = true;
		}
		else if(args.get(i+1).contains("totem"))
		{
			particle = Particle.TOTEM;
			isParticleSet = true;
		}
		else if(args.get(i+1).contains("happy"))
		{
			particle = Particle.VILLAGER_HAPPY;
			isParticleSet = true;
		}
		else if(args.get(i+1).contains("angry"))
		{
			particle = Particle.VILLAGER_ANGRY;
			isParticleSet = true;
		}
		else if(args.get(i+1).contains("bubble"))
		{
			particle = Particle.WATER_BUBBLE;
			isParticleSet = true;
		}
		else if(args.get(i+1).contains("drop"))
		{
			particle = Particle.WATER_DROP;
			isParticleSet = true;
		}
		else if(args.get(i+1).contains("splash"))
		{
			particle = Particle.WATER_SPLASH;
			isParticleSet = true;
		}
		else if(args.get(i+1).contains("wake"))
		{
			particle = Particle.WATER_WAKE;
			isParticleSet = true;
		}
		else
		{
			return false;
		}
		return true;
	}
	public void printCreate()
	{
		for(int k=0; k < 100; k++)
		{
			player.sendMessage("");
		}
		player.sendMessage("all valid arguments");
		player.sendMessage("name");
		player.sendMessage("particle");
		player.sendMessage("damage");
		player.sendMessage("effect");
		player.sendMessage("exit");
		player.sendMessage("finish");
	}
	public void giveItem()
	{
		if(player.getInventory().firstEmpty()==-1)
		{
			//Inv is full
			World world = player.getWorld();
			world.dropItemNaturally(player.getLocation(), getItem());
			player.sendMessage(ChatColor.GOLD + "TestWand for newly created spell dropped near you");
		}
		player.getInventory().addItem(getItem());
		player.sendMessage(ChatColor.GOLD + "TestWand for newly created spell in your inventory");
	}
	public ItemStack getItem()
	{
		
		testWandId = new Random().nextInt((1000000 - 1) + 1) + 1;
		testwand = new ItemStack(Material.STICK);
		ItemMeta testWandMeta = testwand.getItemMeta();
		testWandMeta.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "TestWand " + player.getName() + " " + testWandId);
		List<String> lore = new ArrayList<String>();
		lore.add("");
		lore.add(ChatColor.AQUA + "Item used for testing newly created spell");
		testWandMeta.setLore(lore);
		testwand.setItemMeta(testWandMeta);
		return testwand;
	}
	public boolean checker()
	{
		if(player.getInventory().getItemInMainHand().getType() == Material.STICK)
		{
			if(player.getInventory().getItemInMainHand().getItemMeta().getDisplayName().contains(player.getName()))
			{
				System.out.println("Passed Checks");
				return true;
			}
		}
		System.out.println("Failed Checks");
		return false;
	}
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {

    	//arguments = args;
    	//command = label;
    	if(sender instanceof Player)
    	{
    		player = (Player)sender;
        	if(label.equalsIgnoreCase("spell") ) 
        	{
        		//initiate console
        		inEditor = true;
        		for(int i=0; i < 100; i ++)
        		{
        			player.sendMessage("");
        		}
        	}
			
        	player.sendMessage(ChatColor.AQUA + "" + ChatColor.STRIKETHROUGH + "" + ChatColor.BOLD + "---------------------------------------------");
            player.sendMessage(ChatColor.AQUA + "                            Spell Terminal          ");
            player.sendMessage(ChatColor.AQUA + "" + ChatColor.STRIKETHROUGH + "" + ChatColor.BOLD + "---------------------------------------------");
            player.sendMessage("");
            player.sendMessage("Welcome to Spell Terminal. Here are the KEYWORDS");
            player.sendMessage("");
            sendMessage("create", "Type create to start creating a spell");
            sendMessage("edit", "Type edit to start editing a spell");
            sendMessage("showall","Type showall to get all created spells");
            sendMessage("particles","Type particles to display all valid particle effects");
            
            
            
    	}
    	return true;
    		
    }
	public void sendMessage(String desc, String info)
	{
		TextComponent message = new TextComponent(desc);
		message.setColor(ChatColor.BLUE);
		message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
				new ComponentBuilder(info).color(ChatColor.AQUA).create()));
		player.spigot().sendMessage(message);	
	}
	//----------------------------------------------
	public Player getPlayer()
	{
		return player;
	}
	
}

