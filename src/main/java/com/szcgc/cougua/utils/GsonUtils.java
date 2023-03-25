package com.szcgc.cougua.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * gson相关的
 */
public class GsonUtils {
    public static final Gson gson = new GsonBuilder().disableHtmlEscaping().create();
}
