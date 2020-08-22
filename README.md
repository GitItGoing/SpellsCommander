# What is SpellsCommander?

SpellsCommander is a command line based spell creator plugin for Minecraft 1.15.2

This plugin enables Minecraft administrators, creatives and quest developers to create objects with spell-casting abilities using a powerful, simple and efficient interface. Unlike the GUI interfaces preferred by players, the development of spells is far more efficient with a command line.  For example, it is very useful to be able to cut and paste a spell configuration vs doing a series of clicks when testing. Command line is loved by Linux developers worldwide during the app creation process and it's now available for spell creation process as well.

# Key features

SpellsCommander allows for creating, editing and deleting spells using command line

The spell information is stored in a local SQL database, benefiting from the __[ACID](https://en.wikipedia.org/wiki/ACID)__ properties.  You'd want your saved spells to be available, consistent, isolated and durable, right?

The plugin comes with seven presets of spell effects including a static laserbeam, an 3D oscillating wave, a spiral cone and others. 

The plugin is compatible with all 1.15.2 particle effects.

This plugin comes with a Mana bar, allowing for control on the amount of spells castable at once.

# Are there examples of the plugin in use?

Of course!

__Here I create a spell using the Spiral effect and the Flame Particle__

![Example](https://github.com/GitItGoing/Spells/blob/master/Example1.gif?raw=true)

__Here are some keywords used to help develop your spell__

![Example2](https://github.com/GitItGoing/Spells/blob/master/Example2.gif?raw=true)
# How do I get the plugin on my server?

To start, you will need to install [MySQL](http://www.mysql.com). How to do that can be found here:

Windows:
https://dev.mysql.com/doc/mysql-installation-excerpt/5.7/en/windows-installation.html

Mac:
https://dev.mysql.com/doc/mysql-installation-excerpt/5.7/en/osx-installation.html

Linux:
https://dev.mysql.com/doc/mysql-installation-excerpt/5.7/en/linux-installation.html

AWS Instances(Such as EC2):

```bash
$ sudo yum install mysql-server  //installs mysql on ec2

$ service mysqld start //starts mysql

$ mysqladmin -u root password [your_new_pwd] //changes password so you can remember it (fill your_new_pwd with desired password)

$ mysql -u root -p //access mysql terminal
```

Once you have installed MySQL on your local machine, checkout and build the project.  You can also jump right in, putting the pre-built spell.jar file into your server's plugin folder. 

# Development Information

I developed the plugin on an AWS EC2 Instance, MacOS, and Windows. My preferred setup is the EC2 for the server and Mac for the development and test.

# Some things that could be added

* More presets
* More spell actions
* Use an AWS database, such as the [RDS](https://aws.amazon.com/rds/) that are managed by Amazon  (more expensive, but automatically backed up).




