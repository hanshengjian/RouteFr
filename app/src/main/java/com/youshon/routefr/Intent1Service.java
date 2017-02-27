package com.youshon.routefr;

import android.content.Context;

import com.route.fr.annotation.ClassName;
import com.route.fr.annotation.Key;
import com.route.fr.annotation.RequestCode;

import java.util.ArrayList;

/**
 * Created by hansj on 2017/2/24.
 */

public interface Intent1Service {
    @ClassName("com.youshon.routefr.SecondActivity")
    @RequestCode(1)
    void intent3(@Key("context")Context context,
                              @Key("platform") String platform,
                              @Key("student") Student student,
                              @Key("list")ArrayList<String> list);
}
