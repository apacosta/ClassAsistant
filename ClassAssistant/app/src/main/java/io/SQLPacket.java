package io;

import java.util.ArrayList;

/**
 * Created by asmateus on 16/04/17.
 */

public class SQLPacket {

    public static final int TYPE_INT = 0;
    public static final int TYPE_LONG = 1;
    public static final int TYPE_STRING = 2;

    public String cmd = "";
    public ArrayList<String> fields = new ArrayList<>();
    public  ArrayList<Integer> types = new ArrayList<>();
}
