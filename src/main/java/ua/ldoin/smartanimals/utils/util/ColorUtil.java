package ua.ldoin.smartanimals.utils.util;

import lombok.experimental.UtilityClass;
import net.md_5.bungee.api.ChatColor;
import ua.ldoin.smartanimals.utils.util.version.VersionManager;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@UtilityClass
public class ColorUtil {

    public static String makeColor(String str) {

        Pattern unicode = Pattern.compile("\\\\u\\+[a-fA-F0-9]{4}");
        Matcher match = unicode.matcher(str);

        while (match.find()) {

            String code = str.substring(match.start(), match.end());
            str = str.replace(code, Character.toString((char) Integer.parseInt(code.replace("\\u+",""),16)));

            match = unicode.matcher(str);

        }

        if (VersionManager.isHigherThan(15))
            try {

                Pattern pattern = Pattern.compile("&#[a-fA-F0-9]{6}");
                match = pattern.matcher(str);

                while (match.find()) {

                    String color = str.substring(match.start(), match.end());

                    str = str.replace(color, ChatColor.class.getMethod("of", String.class).invoke(ChatColor.class, color.replace("&", "")) + "");
                    match = pattern.matcher(str);

                }
            } catch (Exception exception) {

                exception.printStackTrace();

            }

        return ChatColor.translateAlternateColorCodes('&', str);

    }
}