##轻量级组件路由框架
将路由跳转协议注解，在编译期创建协议类
例如app中定义的协议接口IntentService,那么对应会创建IntentServiceImpl



```
   public interface IntentService {
       @ClassName("com.youshon.routefr.SecondActivity")
       @RequestCode(1)
       void intent2ActivityDemo2(@Key("context")Context context,
                                 @Key("platform") String platform,
                                 @Key("month")Integer month,
                                 @Key("list")ArrayList<Integer> list);
   }



    public class IntentServiceImpl {

   
  
   public class IntentServiceImpl {

     public static void intent2ActivityDemo2(Context context, String platform, Integer month,
         ArrayList<Integer> list) {
       Intent intent = new Intent(context,com.youshon.routefr.SecondActivity.class);
       intent.putExtra("platform",platform);
       intent.putExtra("month",month);
       intent.putIntegerArrayListExtra("list",list);
       ((android.app.Activity)context).startActivityForResult(intent,1);
     }
   }
```



build后调用方可以直接敲入如下代码:
```IntentServiceImpl.intent2ActivityDemo2(...)```



