package com.moping.imageshow.func;

public abstract class FunctionWithParamAndResult<Result,Param> extends Function {

    public FunctionWithParamAndResult(String functionName) {
        super(functionName);
    }

    public abstract Result function(Param param);

}
