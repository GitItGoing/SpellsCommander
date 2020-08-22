# What is SpellsCommander?

SpellsCommander is a command line based spell creator plugin for Minecraft 1.15.2

This project was created because I believe that a command line interface is far easier to test, use, and allows for copy and paste of spells from other users.

During development process it was extremely useful to cut and paste a spell configuration versus doing a series of clicks. The interface is much like a Linux prompt which is loved by developers worldwide during the app creation process.

# What are some features of SpellsCommander?

SpellsCommander allows for creating, editing and deleting spells thanks to a local MySQL Database for spells. 

The plugin comes with 7 presets of spell effects including a static laserbeam, an 3D oscillating wave, a spiral cone and others. Along with compatibility with all 1.15.2 particle effects

This plugin comes with a Mana bar, allowing for control on the amount of spells castable at once

# Are there examples of the plugin in use?

Of course!

__Here I create a spell using the Spiral effect and the Flame Particle__

![Example](https://github.com/GitItGoing/Spells/blob/master/Example1.gif?raw=true)

__Here are some keywords used to help develop your spell__

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

```bash
$ sudo yum install mysql-server  //installs mysql on ec2

$ service mysqld start //starts mysql

$ mysqladmin -u root password [your_new_pwd] //changes password so you can remember it (fill your_new_pwd with desired password)

$ mysql -u root -p //access mysql terminal
```

Once you have installed MySQL on your local machine, download the project from here (github), extract it, and __Copy the Spell.jar file into your server's plugin folder.__ No other action is required

# Development Information

This project was developed for and tested on an AWS EC2 Instance, MacOS, and Windows.

# Some things that could be added

* More presets
* More spell actions
* Implementation of a web SQL so the configurator doesn't have to set up MySQL locally





