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

    public EnvFrame(Node parms, List arguments, EnvFrame env) {
        this.frameMap = new HashMap<>();
        nextFrame = env;
        updateEnv(parms, arguments);
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
}
