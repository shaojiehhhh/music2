package music;

import java.awt.*;

public class Stem extends Duration{
    public Staff staff;
    public Head.List heads = new Head.List();
    public boolean isUp = true;

    public Stem(Staff staff, boolean up){
        super();
        this.staff = staff;
        isUp = up;
        //TODO: ADD REACTION
    }

    public int x(){Head h = firstHead(); return h.time.x + (isUp?h.W():0);}
    public Head firstHead(){return heads.get(isUp ? heads.size() - 1 : 0);}
    public Head lastHead(){return  heads.get(isUp ? 0 : heads.size() - 1);}
    public int yFirstHead(){Head h = firstHead(); return h.staff.yLine(h.line);}

    public int yBeamEnd(){
        Head h = lastHead();
        int line = h.line;
        line += (isUp? -7:7); // default is one octave from last head on the beam
        int flagInc = nFlag > 2 ? 2*(nFlag-2) :0; // if more than 2 flags we adjust stem end..
        line += (isUp? -flagInc : flagInc); // .. but direction of adjustment depends on up or down stem
        if((isUp && line > 4) || (!isUp && line < 4)){line = 4;} // meet center line if we must
        return h.staff.yLine(line);
    }

    public void deleteStem(){deleteMass();} // only call if list of heads is empty

    public void setWrongSides() {

    }

    @Override
    public void show(Graphics g) {
        if(nFlag >= -1 && heads.size() > 0){
            int x = x(), h = staff.h(), yH = yFirstHead(), yB = yBeamEnd();
            g.drawLine(x,yH,x,yB);
        }
    }
}
