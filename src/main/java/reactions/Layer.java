package reactions;

import global.I;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

public class Layer extends ArrayList<I.Show> implements I.Show{
    public static HashMap<String,Layer> byName = new HashMap<>();
    public static Layer ALL = new Layer("ALL"); //keep the layer a single list(not really a const)
    public String name; //background

    public Layer(String name) { //constructor
        this.name = name;
        if(!name.equals("ALL")) {ALL.add(this);}
        byName.put(name,this);
    }

    @Override
    public void show(Graphics g) {
        System.out.println("LS: " + name + size());
        for(I.Show item: this) {
            item.show(g);
        }
    }

    //nukes layers before undo
    public static void nuke() {
        for(I.Show layer: ALL) {
            ((Layer) layer).clear();
        }

    }
    public static void createAll(String[] a) {
        for(String s: a) {new Layer(s);}
    }
}
