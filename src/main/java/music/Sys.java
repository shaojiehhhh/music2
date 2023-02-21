package music;

import reactions.Mass;

import java.awt.*;
import java.util.ArrayList;

import static music.MusicEd.PAGE;

public class Sys extends Mass {
    public ArrayList<Staff> staffs = new ArrayList<>();
    public Page page = PAGE;
    public int iSys;
    public Sys.Fmt fmt;

    public Sys(int iSys, Sys.Fmt fmt) {
        super("BACK");
        this.iSys = iSys;
        this.fmt = fmt;
    }
    
    public int yTop() {return page.sysTop(iSys);}

    @Override
    public void show(Graphics g) {
        int y = yTop(), x = page.margins.left;
        g.drawLine(x, y, x, y+ fmt.height());
    }

    //---------------------------Sys.Fmt--------------------------
    public static class Fmt extends ArrayList<Staff.Fmt>{
        public ArrayList<Integer> staffOffset =  new ArrayList<>();

        public int height() {
            int last = size() -1;
            return staffOffset.get(last) + get(last).height();
        }

        public void showAt(Graphics g, int y) {
            for(int i = 0; i < size(); i++) {
                get(i).showAt(g, y+staffOffset.get(i)); //get(i) is StaffFmt
            }
        }
    }
}
