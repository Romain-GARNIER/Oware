package Oware;

import java.util.Scanner;

public class IHM {
    public static int level;
    public static Scanner sc = new Scanner(System.in);

    public static void console(String line){
        System.out.println(line);
    }

    public static void log(String log, int lev){
        if(level >= lev){
            System.out.println(log);
        }
    }

    public static int consoleNextInt(){
        return sc.nextInt();
    }

    public static String consoleNext(){
        return sc.next();
    }
}
