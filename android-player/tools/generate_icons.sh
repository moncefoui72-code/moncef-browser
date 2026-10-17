#!/usr/bin/env bash
# generate_icons.sh
# Usage: ./generate_icons.sh
# This script generates launcher PNGs for various mipmap densities from the vector drawable (ic_play.xml).
# Requires: inkscape (or rsvg-convert) and ImageMagick (convert).

set -e
ROOT_DIR="$(cd "$(dirname "$0")/.." && pwd)"
DRAWABLE="$ROOT_DIR/app/src/main/res/drawable/ic_play.xml"
OUT_DIR="$ROOT_DIR/app/src/main/res"

# sizes in px for the foreground layer (based on a 108dp design at mdpi)
# We'll render at sizes appropriate for each density bucket.
declare -A sizes
sizes[ldpi]=54
sizes[mdpi]=72
sizes[hdpi]=108
sizes[xhdpi]=144
sizes[xxhdpi]=216
sizes[xxxhdpi]=324

echo "Generating launcher icons from $DRAWABLE"

for density in "ldpi" "mdpi" "hdpi" "xhdpi" "xxhdpi" "xxxhdpi"; do
  size=${sizes[$density]}
  out_dir="$OUT_DIR/mipmap-$density"
  mkdir -p "$out_dir"
  fg_png="$out_dir/ic_play_fg.png"
  launcher_png="$out_dir/ic_launcher.png"

  echo "- Rendering foreground ($density) ${size}x${size} -> $fg_png"
  if command -v inkscape >/dev/null 2>&1; then
    inkscape --export-type=png --export-width=$size --export-height=$size "$DRAWABLE" -o "$fg_png"
  elif command -v rsvg-convert >/dev/null 2>&1; then
    rsvg-convert -w $size -h $size "$DRAWABLE" -o "$fg_png"
  else
    echo "Error: inkscape or rsvg-convert is required to render SVG to PNG. Please install one and re-run." >&2
    exit 1
  fi

  # Compose the adaptive icon: background color + rounded square + foreground centered
  bg_color="#0A5AB6"
  canvas_size=$((size))
  temp_bg="$out_dir/bg.png"

  if command -v convert >/dev/null 2>&1; then
    # Create rounded rectangle background
    convert -size ${canvas_size}x${canvas_size} xc:none -fill "$bg_color" -draw "roundrectangle 0,0 $canvas_size,$canvas_size 24,24" "$temp_bg"
    # Composite foreground centered
    convert "$temp_bg" "$fg_png" -gravity center -composite "$launcher_png"
    rm -f "$temp_bg"
  else
    echo "Warning: ImageMagick 'convert' not found. Foreground PNG created but adaptive launcher PNG not composed. Install ImageMagick to compose final icons." >&2
    cp "$fg_png" "$launcher_png"
  fi

  echo "  -> Generated $launcher_png"
done

echo "Done. Generated mipmap folders under $OUT_DIR (open Android Studio -> Tools -> Image Asset if you want to refine)."
