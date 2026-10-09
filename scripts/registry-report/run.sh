#!/usr/bin/env bash
# Sounds, particles and effects, potions, enchantments from 1.8 to 26.x: dumps every NMS revision's Spigot API (and
# 1.8's NMS particles), CraftBukkit's sound mappings and Mojang's sounds.json, then writes docs/Sound.md,
# Particle.md, Potion.md, Enchantment.md and the tables and constants of EranoAPI's EranoSound, EranoParticle,
# EranoEffect, EranoPotionEffect, EranoPotionType, EranoEnchantment.
#
# Needs: the spigot-api jars of every revision in ~/.m2 (BuildTools), the 1.8 Spigot server jars, a CraftBukkit git
# clone (BuildTools/CraftBukkit) and the internet (Mojang's assets, cached in the work folder).
set -euo pipefail
HERE="$(cd "$(dirname "$0")" && pwd)"
ROOT="$(cd "$HERE/../.." && pwd)"
WORK="${WORK:-$HERE/work}"
mkdir -p "$WORK/mojang"
cd "$WORK"
python3 "$HERE/dumpall.py" api.json
python3 "$HERE/soundkeys.py"
python3 "$HERE/mojang.py" mojang
python3 "$HERE/generate.py" "$ROOT"
