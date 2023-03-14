package music;

import reactions.Mass;

import java.awt.*;

public class Head extends Mass {
    public Staff staff;
    int line;
    public Time time;

    public Head(Staff staff, int x, int y) {
        super("NOTE");
        this.staff = staff;
        time = staff.sys.getTime(x);
        line = staff.lineOfY(y);
        System.out.println("Line: "+line);
    }

    @Override
    public void show(Graphics g) {
        int H = staff.h();
        Glyph.HEAD_Q.showAt(g,H,time.x, staff.yTop()+line*H);
    }
}
