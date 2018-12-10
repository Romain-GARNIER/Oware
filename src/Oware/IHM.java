package Oware;

public class IHM {
    public static int level;

    public static void console(String line){
        System.out.println(line);
    }

    public static void log(String log, int lev){
        if(level >= lev){
            System.out.println(log);
        }
    }
}
