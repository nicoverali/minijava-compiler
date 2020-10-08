package util;

import org.junit.jupiter.api.DisplayNameGenerator;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * This name generator has been extracted from:
 * <a href="https://helpercode.com/2020/08/27/better-tests-names-using-junits-display-names-generators/">Better tests names using junits display names genrators</a>
 */
public class RoyOsheroveTestNameGenerator extends DisplayNameGenerator.Standard{
    private String splitToParts(String input) {
        try {
            List<String> stringParts = getTestNameParts(input);

            if (stringParts.size() == 1) {
                return input;
            }

            if (stringParts.size() == 2) {
                return String.format("(%s): Always %s", stringParts.get(0), stringParts.get(1));
            }

            if (stringParts.size() == 3) {
                return String.format("(%s): %s -> %s", stringParts.get(0), stringParts.get(1), stringParts.get(2));
            }

        } catch (Exception exc) {
            System.console().writer().println("Failed parsing test name");
        }

        return input;
    }

    private List<String> getTestNameParts(String input) {
        List<String> stringParts = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < input.length(); ++i) {
            char ch = input.charAt(i);
            if (ch == '(') {
                break;
            }
            if (ch == '_') {
                stringParts.add(sb.toString());
                sb.setLength(0);
            } else if (Character.isUpperCase(ch) && stringParts.size() > 0) {
                if (sb.length() > 0) {
                    sb.append(" ");
                }
                sb.append(Character.toLowerCase(ch));
            } else {
                sb.append(ch);
            }
        }

        stringParts.add(sb.toString());
        return stringParts;
    }

    @Override
    public String generateDisplayNameForMethod(Class<?> testClass, Method testMethod) {
        return splitToParts(
                super.generateDisplayNameForMethod(testClass, testMethod)
        );
    }

}