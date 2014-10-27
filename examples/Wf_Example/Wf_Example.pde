import visualizer.library.*;
Visualizer visual;
Waveform wave;

void setup(){
  size(500,500);  
  visual = new Visualizer(this);
  visual.songSet("Pamgaea.mp3",4096);
  wave = new Waveform();
}
void draw(){
  background(0);
  wave.wf();
}
