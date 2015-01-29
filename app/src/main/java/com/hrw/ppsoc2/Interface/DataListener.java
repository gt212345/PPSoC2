package com.hrw.ppsoc2.Interface;

import java.util.ArrayList;

/**
 * Created by Wu on 2015/1/28.
 */
public interface DataListener {
    void doAfterDataReceived(byte[] input,ArrayList<Integer> data,int position);
}
