package reactions;

import global.I;

public abstract class Mass extends Reaction.List implements I.Show{
    public Layer layer;

    //constructor
    public Mass(String layerName) {
        layer = Layer.byName.get(layerName);
        if(layer != null) {layer.add(this);}
        else {System.out.println("Bad Layer Name: " + layerName);}
    }
    public void delete() {
        clearAll(); //clear all reaction from this list and byShape;
        layer.remove(this); //remove itself from list
    }

}
