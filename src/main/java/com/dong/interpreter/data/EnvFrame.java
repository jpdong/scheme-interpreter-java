package com.dong.interpreter.data;

import com.dong.interpreter.Interpreter;
import com.dong.interpreter.Type;

import java.util.HashMap;
import java.util.List;

public class EnvFrame {
    public HashMap<String, Type> frameMap;
    EnvFrame nextFrame;

    public EnvFrame() {
        this.frameMap = new HashMap<>();
    }

    public EnvFrame(EnvFrame env) {
        this.frameMap = new HashMap<>();
        this.nextFrame = env;
    }

    public EnvFrame(Node parms, List arguments, EnvFrame env) {
        this.frameMap = new HashMap<>();
        nextFrame = env;
        updateEnv(parms, arguments);
    }

    public EnvFrame(Node field, EnvFrame envFrame) {
        this.frameMap = new HashMap<>();
        for (int i = 0; i < field.typeList.size(); i++) {
            Node node = (Node) field.typeList.get(i);
            Symbol symbol = (Symbol) node.typeList.get(0);
            String key = symbol.id;

        }
    }

    private void updateEnv(Node parms, List<Type> arguments) {
        String key;
        Type type;
        for (int i = 0; i < parms.typeList.size(); i++) {
            type = parms.typeList.get(i);
            key = ((Symbol)type).id;
            frameMap.put(key, arguments.get(i));
        }

    }

    public Type lookupSymbol(String id) {
        EnvFrame frame = this;
        while (frame != null) {
            if (frame.frameMap.containsKey(id)) {
                return frame.frameMap.get(id);
            }
            frame = frame.nextFrame;
        }
        return null;
    }

    public void put(String key, Type data) {
        this.frameMap.put(key, data);
    }
}
