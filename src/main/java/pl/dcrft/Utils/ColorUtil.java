package pl.dcrft.Utils;

import org.bukkit.ChatColor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ColorUtil {

    public static String reformatRGB(String message) {

        message = message.replaceAll("(?i)\\&(x|#)([0-9A-F])([0-9A-F])([0-9A-F])([0-9A-F])([0-9A-F])([0-9A-F])", "&x&$2&$3&$4&$5&$6&$7");

        StringBuilder transformedMessage = new StringBuilder();
        char lastChar = 'a';

        for (char c : message.toCharArray()) {

            if (lastChar == '&') {
                if (String.valueOf(c).matches("(?i)([0-9A-FX])")) {
                    c = Character.toLowerCase(c);
                }
            }

            transformedMessage.append(c);
            lastChar = c;
        }

        return transformedMessage.toString();

    }
    public static String colorize (String toColorize){
        return ChatColor.translateAlternateColorCodes('&', ColorUtil.reformatRGB(toColorize));
    }

}