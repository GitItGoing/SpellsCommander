# What is SpellsCommander?

Spells is a Command Line based Spell creator plugin for Minecraft 1.15.2

This project was created because I believe that a command line interface is far easier to test, use, and allows for copy and paste of spells from other users

# What are some features of SpellsCommander?

Spells allows for creating, editing and deleting spells thanks to a local MySQL Database for spells. 

The plugin comes with 7 presets of spell effects including a static laserbeam, and oscillating wave, a spiral cone and others. Along with compatibility with all 1.15.2 particle effects

This plugin comes with a Mana bar, allowing for control on the amount of spells castable at once

# Are there examples of the plugin in use?

Of course!

__Here I create a spell using the Spiral effect and the Flame Particle__

![Example](https://github.com/GitItGoing/Spells/blob/master/Example1.gif?raw=true)

__Here is where to find reference for all the particles and effects__

![Example2](https://github.com/GitItGoing/Spells/blob/master/Example2.gif?raw=true)
# How do I get the plugin on my server?

To start, you will need to install MySQL. How to do that can be found here:

Windows:
https://dev.mysql.com/doc/mysql-installation-excerpt/5.7/en/windows-installation.html

Mac:
https://dev.mysql.com/doc/mysql-installation-excerpt/5.7/en/osx-installation.html

Linux:
https://dev.mysql.com/doc/mysql-installation-excerpt/5.7/en/linux-installation.html

AWS Instances(Such as EC2):

__sudo yum install mysql-server__   //installs mysql on ec2

__service mysqld start__  //starts mysql

__mysqladmin -u root password [your_new_pwd]__ //changes password so you can remember it (fill your_new_pwd with desired password)

__mysql -u root -p__ //access mysql terminal

Once you have installed MySQL on your local machine, download the project from here (github), extract it, and __Copy the Spell.jar file into your server's plugin folder.__ No other action is required

# Development Information

This project was developed for and tested on an AWS EC2 Instance, MacOS, and Windows.

# Some things that could be added

* More presets
* More spell actions
* Implementation of a web SQL so the configurator doesn't have to set up MySQL locally





