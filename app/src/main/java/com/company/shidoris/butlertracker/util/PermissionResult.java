package com.company.shidoris.butlertracker.util;

/**
 * Created by isaac on 11/24/17.
 */

public class PermissionResult {

    int requestCode;
    String permissions[];
    int grantResults[];

    public PermissionResult(int requestCode, String[] permissions, int[] grantResults) {
        this.requestCode = requestCode;
        this.permissions = permissions;
        this.grantResults = grantResults;
    }
}
