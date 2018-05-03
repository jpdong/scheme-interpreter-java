package com.dong.interpreter;

public class Tool {
    public static void print(String s) {
        System.out.print(s);
    }

    public static void print(SchemeObject object) {
        switch (object.type) {
            case NUM:
                System.out.print(object.data.num.value);
                break;
                default:
                    break;

        }

    }
}
