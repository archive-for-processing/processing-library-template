package visualizer.library;

import processing.core.PApplet;

public class Waveform {

	public Waveform(){
		
	}
	public void wf(){
		Visualizer.Parent.stroke(0,255,0);
		for(int i = 0; i < Visualizer.song.bufferSize() - 1;i++){
			PApplet.println(Visualizer.song.left.get(i));
			float x1 = PApplet.map(i, 0, Visualizer.song.bufferSize(), 0, Visualizer.width);
			float x2 = PApplet.map(i+1, 0, Visualizer.song.bufferSize(), 0, Visualizer.width);
			Visualizer.Parent.line(x1, 50 + Visualizer.song.left.get(i) * 50, x2, 50 + Visualizer.song.left.get(i + 1) * 50);
			Visualizer.Parent.line(x1, 150 + Visualizer.song.right.get(i) * 50, x2, 150 + Visualizer.song.right.get(i + 1) * 50);
		}
	}
	public void wf(int y){
		Visualizer.Parent.stroke(0,255,0);
		for(int i = 0; i < Visualizer.song.bufferSize() - 1;i++){
			PApplet.println(Visualizer.song.left.get(i));
			float x1 = PApplet.map(i, 0, Visualizer.song.bufferSize(), 0, Visualizer.width);
			float x2 = PApplet.map(i+1, 0, Visualizer.song.bufferSize(), 0, Visualizer.width);
			Visualizer.Parent.line(x1, 50 + Visualizer.song.left.get(i) * 50, x2, 50 + Visualizer.song.left.get(i + 1) * 50);
			Visualizer.Parent.line(x1, 150 + Visualizer.song.right.get(i) * 50, x2, 150 + Visualizer.song.right.get(i + 1) * 50);
		}
	}
	
}
