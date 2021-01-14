EffectLib - Manage your effects the nice way.
=========

# Note - Outdated!

Development of this plugin/lib has been moved to http://github.com/elBukkit/EffectLib/ with much respect and appreciation to Slikey!

This move was mainly done so the Maven group id could be changed to something that can be verified for submission to Maven central.

# Introduction

You have no idea what an vector or matrix is, but you want to give your users some nice effects with particles? No problem. this library comes with a load of effects for you. It handles rotation, text-parsing, and creation of 3D objects with particles in Minecraft.

  - Text-Parsing
  - 3D cubes, spheres, stars and others
  - 2D arcs, lines and many more!
  - Entity effects that enhance every game 

Find more information on [BukkitForums] [forum] or [BukkitDev] [dev]!

Note that **this library** is no standalone plugin! You have to **create yourself a plugin** to run the effects!

# How to Shade

It is recommended to shade this plugin into yours. This way users of your plugin do not need to install EffectLib separately.

This easy to do with Maven. First, add the elMakers repository:

```
    <repositories>
        <repository>
            <id>github</id>
            <url>https://maven.pkg.github.com/Slikey/EffectLib</url>
        </repository>
<!-- Use this repository if you need snapshot builds -->
        <repository>
            <id>elMakers</id>
            <url>http://maven.elmakers.com/repository/</url>
        </repository>
    </repositories>
```

Then add the EffectLib dependency:

```
        <dependency>
            <groupId>de.slikey</groupId>
            <artifactId>EffectLib</artifactId>
            <version>6.3</version>
            <scope>compile</scope>
        </dependency>

```

Note the "compile" scope!

Then finally add the Maven shade plugin:

```
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-shade-plugin</artifactId>
                <version>3.0.0</version>
                <configuration>
                    <minimizeJar>false</minimizeJar>
                    <dependencyReducedPomLocation>${project.build.directory}/dependency-reduced-pom.xml</dependencyReducedPomLocation>
                    <relocations>
                        <relocation>
                            <pattern>de.slikey</pattern>
                            <shadedPattern>com.your.own.package.slikey</shadedPattern>
                        </relocation>
                    </relocations>
                </configuration>
                <executions>
                    <execution>
                        <phase>package</phase>
                        <goals>
                            <goal>shade</goal>
                        </goals>
                    </execution>
                </executions>
            </plugin>
        </plugins>
```

Make sure to change the "shadedPattern" to match the base package of your own plugin

# Support

Come visit the Magic Discord if you'd like EffectLib help: https://discord.gg/fWJ3W3kMjG

# License

MIT

**Free Software, Hell Yeah!**

[dev]:http://dev.bukkit.org/bukkit-plugins/effectlib/
[forum]:http://forums.bukkit.org/threads/effectlib-manage-your-effects-the-nice-way-text-in-particles.259879/

    
