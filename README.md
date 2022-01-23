

# GameAutoMusicPlayer #

#### A game auto music player for almost every game which can play music with keyboard

### Very Important things: 

This program play music automatically by simulate key action of the keyboard , which is ***not*** allowed in some games.

Some games may ***ban*** you if you use this program,so make sure that your game allows to use tools like this, and 

**USE AT YOUR OWN RISK**.



### Modules:

  AutoMusicPlayerMain : The main program.

  MouseMappingMaker:  It will be used in the future. ~~~NEVER~~~

### How to use:
1. Drag the music file you want to play to the program window. (This program now supports .mid file and .txt file)
2. Choose the file type.
3. Choose the key map you want to use.
4. Change the tune of the music (by click on the text field and press up key and down key),or just click the auto tune button ,it will make the music sound better.
5. Click the start button and then click your game window as soon as possible.

### Features

1. You can change the tune of the music before playing.
2. You can change the key map( _keyboard layout_ ) freely ,which means you can use this program to play music in almost any game which you can play music with your keyboard
3. You can change the speed of playing the music.
4. You can mute the tracks which you don't want to play.
5. You can make the music sound better in your game by clicking the "Auto tune" button,without modifying the music file.

### How to make a key map

1. Create a folder called keyMaps in the program's directory(if you run the program,the folder will be created automatically)

2. Create a file called somename.txt

3. Open the file, and write the note and its corresponding key in the following form: ```note name+octave:key```     

   For example:

   ```
   C4:a
   D4:s
   ```
For some key which can't be expressed by a single character, see [special keys and names](/docs/SpecialKeysAndNames.md)

4. Save the file and restart the program,then you can see your key map.  

   
### Tips:

1. If you change you play speed during playing the music, you need to click pause and resume or restart playing the music.
2. If you can't find your key map,check if there are mistakes in your key map file.

3. The colon in the key map file ***must*** be ":" , instead of "："

