package com.dong.interpreter.data;





public class SchemeData {

    public Bool bool;
    public Symbol symbol;
    public Num num;
    Characters characters;
    Strings strings;
    public Pair pair;
    public PrimitiveProc primitiveProc;
    CompoundProc compoundProc;

    public SchemeData(SchemeType.Type type) {
        switch(type){
            case NUM:
                num  = new Num();
                break;
            case SYMBOL:
                symbol  = new Symbol();
                break;
            case BOOL:
                bool  = new Bool();
                break;
            case PAIR:
                pair  = new Pair();
                break;
            case PRIMITIVE_PROC:
                primitiveProc  = new PrimitiveProc();
                break;

            default:
                break;
        }
    }
}
