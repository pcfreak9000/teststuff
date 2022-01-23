package de.pcfreak9000.teststuff;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;

public class White {
    static {
        Pixmap p = new Pixmap(1, 1, Format.RGB565);
        p.setColor(Color.WHITE);
        p.drawPixel(0, 0);
        WHITE = new Texture(p);
        p.dispose();
    }
    public static final Texture WHITE;
    
    public static void dispose() {
        WHITE.dispose();
    }
}
