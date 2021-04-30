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

    static Color accent = new Color(233, 29, 99);
    static Color accent_light = lighten(accent, 30);
}
