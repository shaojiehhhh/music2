package music;

import reactions.Mass;

import java.awt.*;

import static music.MusicEd.PAGE;

public class Staff extends Mass {
    public Sys sys; //the staff lives in system
    public int iStaff; //index of staff in system
    public Staff.Fmt fmt; //staff format

    public Staff(int iStaff, Staff.Fmt sf) {
        super("BACK");
        this.iStaff = iStaff;
        this.fmt = sf;
    }

    @Override
    public void show(Graphics g) {}

    //-------------------Staff.Fmt-----------------------
    public static class Fmt {
        public int nLine = 5, H = 8;

        public int height() {
            return 2 * H * (nLine -1);
        }

        public void showAt(Graphics g, int y) {
            int left = PAGE.margins.left, right = PAGE.margins.right;
            for(int i  = 0; i < nLine; i ++) {
                g.drawLine(left, y+2*H*i, right, y+2*H*i);
            }
        }
    }
}
