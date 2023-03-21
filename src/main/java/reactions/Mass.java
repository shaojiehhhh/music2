package reactions;

import global.I;
import graphicsLib.G;

public abstract class Mass extends Reaction.List implements I.Show{
    public Layer layer;

    //constructor
    public Mass(String layerName) {
        layer = Layer.byName.get(layerName);
        if(layer != null) {layer.add(this);}
        else {System.out.println("Bad Layer Name: " + layerName);}
    }
    public void deleteMass() {
        clearAll(); //clear all reaction from this list and byShape;
        layer.remove(this); //remove itself from list
    }

    public boolean equals(Object o) {return this == o;}

    private int hashCode = G.rnd(10000000);
    public int hashCode() {return hashCode;}

}
