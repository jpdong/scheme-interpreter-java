package com.dong.interpreter.data;

public class SchemeType {

    public static enum Type {
        THE_EMPTY_LIST, BOOL, SYMBOL, NUM,
        CHARACTER, STRING, PAIR, PRIMITIVE_PROC,
        COMPOUND_PROC, INPUT_PORT, OUTPUT_PORT,
        EOF_OBJECT
    }
}
