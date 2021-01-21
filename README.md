# rd-131655 (Pre-Classic)
Development phase: May 10 - May 13, 2009

[![Demo](.assets/demo.gif)](https://www.youtube.com/watch?v=UMpv5kZ9-rE)
<br>Cave game tech test video released on May 13, 2009.

## Accuracy
The original version was never released to the public.
The copy is based on the very first video.
Due to the similarity of the upcoming released versions,
it is possible to recreate the unreleased version.

#### Recreated features:
- Cave generation
- 8x8x8 Chunks

## References
- [Minecraft Wiki - Java_Edition_pre-Classic_rd-131655](https://minecraft.gamepedia.com/Java_Edition_pre-Classic_rd-131655)
- [Cave game tech test - Nizzotch on YouTube](https://www.youtube.com/watch?v=UMpv5kZ9-rE)
- [IRC logs](https://minecraft.gamepedia.com/Java_Edition_pre-Classic_rd-131655#lwjgl_logs) to recreate the unreleased features:
  - ``[18:55:45] <Notch_> the chunk size is 16x16 now..``
  - ``[18:56:47] <Notch_> 8x8 chunks are SLOWER``
  - ``[20:02:58] <Notch_> it takes like 20 seconds to grow a 256*256*64 map, though. :-\``

## Setup
1. Clone the project
2. Set the VM option ``-Dorg.lwjgl.librarypath="<path_to_project>/run/natives"``
3. Set the working directory to ``./run``