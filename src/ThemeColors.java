import java.awt.*;

class ThemeColors{
    // A class that stores a color theme.

    // A utility function that lightens a color by adding values to R, G, and B channels.
    static Color lighten(Color color, int amount){
        int r = color.getRed();
        int g = color.getGreen();
        int b = color.getBlue();
        int a = color.getAlpha();

        r = Integer.min(r + amount, 255);
        g = Integer.min(g + amount, 255);
        b = Integer.min(b + amount, 255);

        return new Color(r, g, b, a);
    }

    // A utility function that darkens a color by adding values to R, G, and B channels.
    static Color darken(Color color, int amount){
        int r = color.getRed();
        int g = color.getGreen();
        int b = color.getBlue();
        int a = color.getAlpha();

        r = Integer.max(r - amount, 0);
        g = Integer.max(g - amount, 0);
        b = Integer.max(b - amount, 0);

        return new Color(r, g, b, a);
    }
    static Color accent = new Color(233, 29, 99);
    static Color accent_light = lighten(accent, 30);
    static Color time_block_color = new Color(217,210,233);
    static Color time_block_drop = new Color(252,229,205);
    static Color background = new Color(255, 255, 255);
    static Color urgent_important_bg = new Color(244,204,204);
    static Color urgent_unimportant_bg = new Color(252,229,205);
    static Color non_urgent_important_bg = new Color(217,234,211);
    static Color non_urgent_unimportant_bg = new Color(201,218,248);
    static Color text_warning = new Color(240, 50, 50);
    static Color text = new Color(0, 0, 0);
    static Color transparent = new Color(0,0,0,0);

}
