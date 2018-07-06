package com.moping.imageshow.func;

import android.text.TextUtils;

import java.util.HashMap;

public class FunctionManager {

    private static FunctionManager instance;
    private HashMap<String, FunctionNoParamNoResult> mFunctionNoParamNoResault;
    private HashMap<String, FunctionWithParamAndResult> mFunctionWithParamAndResult;
    private HashMap<String, FunctionWithParamOnly> mFunctionWithParamOnly;
    private HashMap<String, FunctionWithResultOnly> mFunctionWithResultOnly;

    private FunctionManager() {
        mFunctionNoParamNoResault = new HashMap<>();
        mFunctionWithParamAndResult = new HashMap<>();
        mFunctionWithParamOnly = new HashMap<>();
        mFunctionWithResultOnly = new HashMap<>();
    }

    public static FunctionManager getInstance() {
        if (instance == null) {
            synchronized (FunctionManager.class) {
                if (instance == null) {
                    instance = new FunctionManager();
                }
            }
        }

        return instance;
    }

    /**
     * 添加无参无返回值方法
     * @param function
     * @return
     */
    public FunctionManager addFunction(FunctionNoParamNoResult function) {
        mFunctionNoParamNoResault.put(function.mFunctionName, function);
        return this;
    }

    /**
     * 调用无参无返回值的接口方法
     * @param functionName
     */
    public void invokeFunc(String functionName) {
        if (TextUtils.isEmpty(functionName) == true) {
            return;
        }

        if (mFunctionNoParamNoResault != null) {
            FunctionNoParamNoResult f = mFunctionNoParamNoResault.get(functionName);
            if (f != null) {
                f.function();
            } else {
                try {
                    throw new FunctionException("Has no this function" + functionName);
                } catch (FunctionException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 添加无参有返回值方法
     * @param function
     * @return
     */
    public FunctionManager addFunction(FunctionWithResultOnly function) {
        mFunctionWithResultOnly.put(function.mFunctionName, function);
        return this;
    }

    /**
     * 调用无参有返回值的接口方法
     * @param functionName
     */
    public <Result> Result invokeFunc(String functionName, Class<Result> clz) {
        if (TextUtils.isEmpty(functionName) == true) {
            return null;
        }

        if (mFunctionWithResultOnly != null) {
            FunctionWithResultOnly f = mFunctionWithResultOnly.get(functionName);
            if (f != null) {
                if (clz != null) {
                    return clz.cast(f.function());
                } else {
                    return (Result)f.function();
                }
            } else {
                try {
                    throw new FunctionException("Has no this function" + functionName);
                } catch (FunctionException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * 添加有参无返回值方法
     * @param function
     * @return
     */
    public FunctionManager addFunction(FunctionWithParamOnly function) {
        mFunctionWithParamOnly.put(function.mFunctionName, function);
        return this;
    }

    /**
     * 调用有参无返回值的接口方法
     * @param functionName
     * @param data
     * @param <Param>
     */
    public <Param> void invokeFunc(String functionName, Param data) {
        if (TextUtils.isEmpty(functionName) == true) {
            return;
        }
        if (mFunctionWithParamOnly != null) {
            FunctionWithParamOnly f = mFunctionWithParamOnly.get(functionName);
            if (f != null) {
                f.function(data);
            } else {
                try {
                    throw new FunctionException("Has no this function" + functionName);
                } catch (FunctionException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 添加有参有返回值方法
     * @param function
     * @return
     */
    public FunctionManager addFunction(FunctionWithParamAndResult function) {
        mFunctionWithParamAndResult.put(function.mFunctionName, function);
        return this;
    }

    /**
     *
     * @param functionName
     * @param clz
     * @param data
     * @param <Result>
     * @param <Param>
     * @return
     */
    public <Result, Param> Result invokeFunc(String functionName, Class<Result> clz, Param data) {
        if (TextUtils.isEmpty(functionName) == true) {
            return null;
        }
        if (mFunctionWithParamAndResult != null) {
            FunctionWithParamAndResult f = mFunctionWithParamAndResult.get(functionName);
            if (f != null) {
                if (clz != null) {
                    return clz.cast(f.function(data));
                } else {
                    return (Result)f.function(data);
                }
            } else {
                try {
                    throw new FunctionException("Has no this function" + functionName);
                } catch (FunctionException e) {
                    e.printStackTrace();
                }
            }
        }

        return null;
    }



}
