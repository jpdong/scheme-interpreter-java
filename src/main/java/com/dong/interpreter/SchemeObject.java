package com.dong.interpreter;

import com.dong.interpreter.data.SchemeData;
import com.dong.interpreter.data.SchemeType;

public class SchemeObject {

    public SchemeType.Type type;
    public SchemeData data;

    public SchemeObject() {

    }

    public boolean equalSymbol(SchemeObject object) {
        if (object == null || object.data == null || object.data.symbol == null) {
            return false;
        } else {
            return this.data.symbol.value.equals(object.data.symbol.value);
        }
    }















}
