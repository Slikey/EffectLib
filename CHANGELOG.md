# CHANGELOG

# 6.3

 - Bleed effect now uses a "material" rather than "color"

# 6.2

 - Some specific internal FX fixes (please let me know if any effects look weird now)
 - Add "rotation" and "orient" parameters to image effects
 - Add parameters to TornadoEffect to reduce lag
 - Add orient parameter to Cylinder effect

# 6.1

 - Fix redstone particles with no color specified (color is required for 1.13, defaults to red now)
 - Fix offset/relativeOffset used via API 
 - Bring back legacy particle name lookup (Fixes MagicSpells compatibility)

# 6.0

 - Plugins that interact directly with Effect properties may need to be rebuilt! Configuration-based plugins should be OK.
 - Added 1.13 Support
 - ParticleEffect has been deprecated, use the Spigot Particle API enum instead
 - EffectManager.display helper method available to handle colors, items and blocks in a version-independent way
 - Effect.materialData changed from Byte to byte

# 5.9

 - Effects marked disappearWithOriginEntity or disappearWithTargetEntity will now disappear
   if their target goes invalid.
 - Allow underscore_style and dash_style parameters in configuration-based effects
 - Add EffectManager.registerEffectClass for registering short names for custom effects
 - Add "wholeCircle" option to Circle effect

# 5.8
 
 - Add additional functions for use in equations: min, max, select
   - Min and max take two parameters and should be pretty self-explanatory.
   - The select function takes 4 parameters. 
     - The first parameter is a test value, call it p0
     - The second parameter is returned if p0 < 0
     - The third parameter is returned if p0 == 0
     - The fourth parameter is returned if p0 > 0
 - Modified effect equations can now use a second parameter, "i", representing total # of iterations
 - Add x,y,z location equations to Modified effect, for moving the entire effect around.
 - Add Plot effect, mainly intended to be used for testing equations, not really for final effects
 - Fix particle offset x/y/z being an integer
 - ShieldEffect radius changed to floating point
 - Fix ImageEffect using a non-relative file path. 

# 5.7

 - Expanded EquationTransform support.
 - Added "transparency" option to ColoredImageEffect and ImageEffect
 - ImageEffect and ColoredImageEffect semi-merged, default particle for ImageEffect is now redstone
 - Optimize image effects, particularly with animations, to avoid constantly reloading the file
 - Image effects can now load files from a URL
 - Cache parsed equations for performance
 - Add ModifiedEffect, which allows you to parameterized any existing effect.

# 5.6

 - Fix inconsistent position of Circle effect when using updateLocations: false

# 5.5

 - Try to make Effect instances reusable (by calling start() again when they are finished)
 - Optimizations (thanks, XakepSDK!)
 - Fix builtin transforms, default package was wrong
 - Added setTargetPlayer API

# 5.4

 - Memory use optimizations
 - Fix potential CME in EffectManager.cancel
 - Fix potential errors when playing FX while shutting down
 - Remove EntityManager. If you were using it, sorry, please open an Issue.

# 5.3

 - Add direct yaw and pitch overrides to Effect

# 5.2

 - Add support for 1.10 and  1.11 particles (FALLING_DUST, TOTEM, SPIT)

# 5.1

 - Some updates to EquationEffect, may or may not change behavior (sorry!)
 - Fixes for the item_crack particle effect disconnecting clients

## 5.0

 - Add support for 1.10
 - Update to 1.9 sound names, now only fully compatible with 1.9 and up

## 4.3

 - Add cycle parameter to EquationEffect

## 4.2

 - Fix API, add back Effect.setLocation, setEntity, 
   setTargetLocation, setTargetEntity
 - Add EquationEffect, for making custom effects using math equations

## 4.1

 - Add new particles from 1.9

## 4.0

 - 1.9 Compatibility
 - Add relativeOffset parameter (thanks, SexyToad!)
 - Dropped support for 1.7. It's no longer easy/possible to have backwards compatibility
   due to 1.9 dropping the old signature of getOnlinePlayers.

## 3.9

 - Add relativeOffset parameter (thanks, SexyToad!)
 - Dropped support for 1.7. It's no longer easy/possible to have backwards compatibility
   due to 1.9 dropping the old signature of getOnlinePlayers.

## 3.8

 - Add material and materialData parameters to all effects, for customizing
   block_crack and item_crack particle types
 - Add support for particle counts and offsets

## 3.7

 - Turn Effect.autoOrient off by default
 - Add updateLocations and updateDirections parameters, can be used to turn off Entity location tracking
 - Changed Cone and Vortex effects to better react to changes in direction
 - Fix BigBang (can't spawn Fireworks async!)
 - Config-driven effect classes may omit the "Effect" portion of the class name when using builtin effects
 - Effect "type" (instant, repeating, delayed) can now be set via configs
 
## 3.6
 
 - Fix LineEffect, broken in 3.5
 - Add "duration" parameter, a value in ms that can convert to period/iterations for you.

## 3.5

 - Add KCauldron support (Thank you, tpnils1!)
 - Add setParticleRange method to set default particle visibility range
 - Particles now use the "long range" flag when range is greater that 16 blocks
   - The old range of 256 seemed way too high, particles appear to vanish after 16 blocks if not long-range
 - Add generalized parameter support for Configuration-driven effects
   You can put variable names that start with "$" in the configuration, e.g. "$radius"
   Then when starting an effect, you can set $radius to a value in the parameterMap
   to automatically replace the variables in the config.
 - Add a DynamicLocation class to encapsulate an Entity or Location.
   This can be used to create a virtual entity, by controlling the location while the effect is runnning.

## 3.4

 - Effects now track an Entity relative to the given starting Location
 - Added some simpler versions of EffectManager.start()
 - Removed references to Apache Commons classes

## 3.0

 - Update particle names to match current ParticleEffect lib- please check code and configs!
 - Added colorizeable spell_mob, spell_mob_ambient and redstone particles
 - Full 1.8 support
 - Better error handling, config options for turning on/off log messages

## 2.1

 - Add MC 1.8 particle FX (will work even on Spigot protocol hack- avoid on Craftbukkit 1.7!)
 - Add displayTo methods for showing a particle to only one player.

## 2.0

 - Refactored Effect system. Most Effect class names have changed!
 - Combined Entity and Location Effects.

## 1.8

 - Last backward-compatible version of EffectLib.
 