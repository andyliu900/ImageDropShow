package com.moping.imageshow.func;

public abstract class FunctionWithParamOnly<Param> extends Function {

    public FunctionWithParamOnly(String functionName) {
        super(functionName);
    }

    public abstract void function(Param param);
}
