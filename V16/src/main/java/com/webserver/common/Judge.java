package com.webserver.common;

import com.webserver.http.HttpServletRequest;

import java.util.List;

public class Judge {

    public static boolean isParError(List<String> strs, HttpServletRequest request) {
        for (String str : strs) {
            String val = request.getParameter(str);
            if (val == null || val.trim().isEmpty()) {
                return true;
            }
        }
        return false;
    }
}