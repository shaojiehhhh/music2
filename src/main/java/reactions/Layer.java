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
        if(!name.equals("All")) {ALL.add(this);}
        byName.put(name,this);
    }

    @Override
    public void show(Graphics g) {
        for(I.Show item: this) {
            item.show(g);
        }
    }
}
