package com.moping.imageshow.func;

public abstract class FunctionWithResultOnly<Result> extends Function {

    public FunctionWithResultOnly(String functionName) {
        super(functionName);
    }

    public abstract Result function();
}
