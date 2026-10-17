# Icon generation guide

This document explains how to generate PNG launcher icons from the vector adaptive icon assets included in the project.

Prerequisites
- inkscape or librsvg (rsvg-convert) to render vector drawable XML to PNG.
- ImageMagick (convert) to compose the foreground over the background and create rounded-corner square icons.

Run the script
1. From the project root, run:

   cd android-player/tools
   chmod +x generate_icons.sh
   ./generate_icons.sh

2. The script will render the vector drawable app/src/main/res/drawable/ic_play.xml at multiple sizes and put output PNGs into res/mipmap-*/ic_launcher.png.

Notes
- If you don't have inkscape or ImageMagick, you can use Android Studio's Image Asset tool (Tools -> Image Asset) to create launcher icons using the drawable/ic_play vector and the background color @color/ic_launcher_background.
- The script attempts to create rounded-square icons similar to the provided reference; you can tweak corner radius in the draw command (the 24,24 values in the script) to change rounding.
