package com.dong.interpreter;

public class SchemeObject {

    public SchemeType type;
    public SchemeData data;


    public boolean equals(SchemeObject obj) {
        return this.type == obj.type && this.data.symbol.value.equals(obj.data.symbol.value);
    }

    public static enum SchemeType {
    THE_EMPTY_LIST, BOOL, SYMBOL, NUM,
    CHARACTER, STRING, PAIR, PRIMITIVE_PROC,
    COMPOUND_PROC, INPUT_PORT, OUTPUT_PORT,
    EOF_OBJECT
    }

    class Bool {
        public int value;
    }

    class Symbol{
        String value;
    }
    class Num{
        long value;
    }
    class Characters{
        String value;
    }
    class Strings{
        String value;
    }
    class Pair{
        SchemeObject car;
        SchemeObject cdr;
    }
    class PrimitiveProc{
        Function fu;
        SchemeObject arguments;
    }
    class CompoundProc{
         SchemeObject parameters;
         SchemeObject body;
         SchemeObject env;
    }

    class SchemeData {
        Bool bool;
        Symbol symbol;
        Num num;
        Characters characters;
        Strings strings;
        Pair pair;
        PrimitiveProc primitiveProc;
        CompoundProc compoundProc;
    }
}
