package com.youshon.routefr;


import android.content.Context;

import com.route.fr.annotation.ClassName;
import com.route.fr.annotation.Key;
import com.route.fr.annotation.RequestCode;

import java.util.ArrayList;

/**
 * @author hiphonezhu@gmail.com
 * @version [Android-BaseLine, 16/10/21 12:23]
 */

public interface IntentService {
    @ClassName("com.youshon.routefr.SecondActivity")
    @RequestCode(1)
    void intent2ActivityDemo2(@Key("context")Context context,
                              @Key("platform") String platform,
                              @Key("month")Integer month,
                              @Key("list")ArrayList<Integer> list);
}
