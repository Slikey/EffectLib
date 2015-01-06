<?php

// Use this to migrate a configuration file from pre-3.0 effect names

if (PHP_SAPI !== 'cli')
{
    die('Must be run from the terminal.');
}
if (count($argv) != 3)
{
    die("Usage: migrate.php <input_file> <output_file>\n");
}

$inputFile = $argv[1];
$outputFile = $argv[2];

$effectTranslation = array(
    'huge_explosion' => 'explosion_huge',
    'large_explode' => 'explosion_large',
    'bubble' => 'water_bubble',
    'suspend' => 'suspended',
    'depth_suspend' => 'suspended_depth',
    'magic_crit' => 'crit_magic',
    'smoke' => 'smoke_normal',
    'mob_spell' => 'spell_mob',
    'mob_spell_ambient' => 'spell_mob_ambient',
    'instant_spell' => 'spell_instant',
    'witch_magic' => 'spell_witch',
    'explode' => 'explosion_normal',
    'splash' => 'water_splash',
    'wake' => 'water_wake',
    'large_smoke' => 'smoke_large',
    'red_dust' => 'redstone',
    'snowball_poof' => 'snowball',
    'angry_villager' => 'villager_angry',
    'happy_villager' => 'villager_happy',
    'droplet' => 'water_drop',
    'take' => 'item_take',
    'icon_crack' => 'item_crack',
    'tile_crack' => 'block_dust',
);

$contents = file_get_contents($inputFile);
foreach ($effectTranslation as $from => $to)
{
    $contents = str_replace($from, $to, $contents);
}

file_put_contents($outputFile, $contents);