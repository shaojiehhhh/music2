package music;

import global.UC;
import reactions.Gesture;
import reactions.Mass;
import reactions.Reaction;

import java.awt.*;
import java.util.ArrayList;

import static music.MusicEd.PAGE;

public class Page extends Mass {
    public Margin margins = new Margin();
    public Sys.Fmt sysFmt;
    public int sysGap, nSys;
    public ArrayList<Sys> sysList = new ArrayList<>();

    //constructor
    public Page(Sys.Fmt sysFmt) {
        super("BACK");
        this.sysFmt = sysFmt;
        addReaction(new Reaction("E-E") {// adding a new Staff (goes into existing system)
            @Override
            public int bid(Gesture g) {
                int y = g.vs.yM();
                if(y <= PAGE.margins.top + sysFmt.height() +30) return UC.noBid;//force space between staffs
                return 50;
            }

            @Override
            public void act(Gesture g) {
                int y = g.vs.yM();
                PAGE.addNewStaff(y - PAGE.margins.top);
            }
        });

        addReaction(new Reaction("E-W") { //adding a new system
            @Override
            public int bid(Gesture g) {
                int y = g.vs.yM();
                int yBot = PAGE.sysTop(nSys);
                if(y <= yBot) return UC.noBid; //return noBid if drawn above existing system
                return 50;
            }

            @Override
            public void act(Gesture g) {
                int y = g.vs.yM();
                if(PAGE.nSys == 1) {
                    PAGE.sysGap = y - PAGE.sysTop(1);
                }
                PAGE.addNewSys();
            }
        });
    }

    public void addNewSys() {
        sysList.add(new Sys(nSys, sysFmt));
        nSys ++;
    }

    public void addNewStaff(int yOff) { //y offset
        Staff.Fmt sf = new Staff.Fmt();
        int n = sysFmt.size();
        sysFmt.add(sf); sysFmt.staffOffset.add(yOff);
        for(int i = 0; i < nSys; i++) {
            sysList.get(i).addStaff(new Staff(n, sf));
        }
    }

    @Override
    public void show(Graphics g) {
        g.setColor(Color.black);
        for(int i = 0; i < nSys; i++) {
            sysFmt.showAt(g, sysTop(i));
        }
    }

    public int sysTop(int iSys) {return margins.top + iSys * (sysFmt.height() + sysGap);}

    //-------------------------Margin-----------------------------------
    public static class Margin {
        private static final int M = 50;
        public int top = M, left = M;
        public int bot = UC.MAIN_WINDOW_HEIGHT - M;
        public int right = UC.MAIN_WINDOW_WIDTH - M;
    }

}
