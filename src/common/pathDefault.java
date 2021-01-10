package common;

import javax.swing.plaf.SliderUI;

public class pathDefault {
    public static String SLASH = "\\";
    private static String RESOURCES = "src" + SLASH + "resources";

    public static String getProjectPath(){
        return System.getProperty("user.dir") + SLASH;
    }

    public static String getResourcesPath(){
        return getProjectPath() + RESOURCES + SLASH;
    }

    public static void main(String[] args) {
        System.out.println(pathDefault.getProjectPath());
        System.out.println(getResourcesPath());
    }
}
