package com.example.lib_common.utils;

import android.os.Handler;
import android.os.Looper;
import android.widget.ImageView;

import com.example.lib_common.R;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.io.File;
import java.util.Map;

/**
 * Created by acer on 2018/3/21.
 */

public class Xutils {
    private volatile static Xutils instance;
    private Handler handler;
    private ImageOptions options;

    private Xutils() {
        handler = new Handler(Looper.getMainLooper());
    }

    /**
     * 单例模式
     *
     * @return
     */
    public static Xutils getInstance() {
        if (instance == null) {
            synchronized (Xutils.class) {
                if (instance == null) {
                    instance = new Xutils();
                }
            }
        }
        return instance;
    }

    /**
     * 异步get请求
     *
     * @param url：请求的网址
     * @param maps：请求时需要的参数
     * @param callBack
     */
    public void get(String url, Map<String, String> maps, final XCallBack callBack) {
        RequestParams params = new RequestParams(url);
        if (maps != null && !maps.isEmpty()) {
            for (Map.Entry<String, String> entry : maps.entrySet()) {
                params.addQueryStringParameter(entry.getKey(), entry.getValue());
            }
        }
        x.http().get(params, new Callback.CommonCallback<String>() {

            @Override
            public void onSuccess(String result) {
                onSuccessResponse(result, callBack);
                //请求成功后的回调方法
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                ex.printStackTrace();
                //请求异常后的回调方法
            }

            @Override
            public void onCancelled(CancelledException cex) {
                //主动调用取消请求的回调方法
            }

            @Override
            public void onFinished() {
                //请求完成后的回调方法（无论失败还是成功）
            }
        });

    }

    /**
     * 异步post请求
     *
     * @param url
     * @param maps
     * @param callback
     */
    public void post(String url, Map<String, String> maps, final XCallBack callback) {
        RequestParams params = new RequestParams(url);
        if (maps != null && !maps.isEmpty()) {
            for (Map.Entry<String, String> entry : maps.entrySet()) {
                params.addBodyParameter(entry.getKey(), entry.getValue());
            }
        }

        x.http().post(params, new Callback.CommonCallback<String>() {

            @Override
            public void onSuccess(String result) {
                onSuccessResponse(result, callback);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }


    /**
     * 带缓存数据的异步 get请求
     *
     * @param url
     * @param maps
     * @param pnewCache：//true: 信任缓存数据, 不再发起网络请求; false不信任缓存数据
     * @param callback
     */
    public void getCache(String url, Map<String, String> maps, final boolean pnewCache, final XCallBack callback) {

        RequestParams params = new RequestParams(url);
        // 默认缓存存活时间, 单位:毫秒（如果服务器没有返回有效的max-age或Expires则参考）
        //params.setCacheMaxAge(1000 * 60);
        if (maps != null && !maps.isEmpty()) {
            for (Map.Entry<String, String> entry : maps.entrySet()) {
                params.addQueryStringParameter(entry.getKey(), entry.getValue());
            }
        }
        x.http().get(params, new Callback.CacheCallback<String>() {
            @Override
            public void onSuccess(String result) {
                onSuccessResponse(result, callback);
                //如果服务返回304或onCache选择了信任缓存,这时result为null
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {}

            @Override
            public void onCancelled(CancelledException cex) {}

            @Override
            public void onFinished() {}

            @Override
            public boolean onCache(String result) { //得到缓存数据, 缓存过期后不会进入
                boolean newCache = pnewCache;
                if (newCache) {
                    newCache = !newCache;
                }
                if (!newCache) {
                    newCache = !newCache;
                    onSuccessResponse(result, callback);
                }
                return newCache;//true: 信任缓存数据, 不再发起网络请求; false不信任缓存数据
            }
        });
    }

    /**
     * 带缓存数据的异步 post请求
     *
     * @param url
     * @param maps
     * @param pnewCache
     * @param callback
     */
    public void postCache(String url, Map<String, String> maps, final boolean pnewCache, final XCallBack callback) {
        RequestParams params = new RequestParams(url);
        if (maps != null && !maps.isEmpty()) {
            for (Map.Entry<String, String> entry : maps.entrySet()) {
                params.addBodyParameter(entry.getKey(), entry.getValue());
            }
        }

        x.http().post(params, new Callback.CacheCallback<String>() {
            @Override
            public void onSuccess(String result) {
                onSuccessResponse(result, callback);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {}

            @Override
            public void onCancelled(CancelledException cex) {}


            @Override
            public void onFinished() {}

            @Override
            public boolean onCache(String result) {
                boolean newCache = pnewCache;
                if (newCache) {
                    newCache = !newCache;
                }
                if (!newCache) {
                    newCache = !newCache;
                    onSuccessResponse(result, callback);
                }
                return newCache;
            }
        });
    }


    /**
     * 正常图片显示
     *
     * @param iv
     * @param url
     * @param option
     *  通过ImageOptions.Builder().set方法设置图片的属性
     *  ImageOptions imageOptions= new ImageOptions.Builder().setFadeIn(true).build(); 淡入效果
     *  ImageOptions.Builder()的一些其他属性：
     *  .setCircular(true) //设置图片显示为圆形
     *  .setSquare(true) //设置图片显示为正方形
     *  .setCrop(true).setSize(200,200) //设置大小
     *  .setAnimation(animation) //设置动画
     *  .setFailureDrawable(Drawable failureDrawable) //设置加载失败的动画
     *  .setFailureDrawableId(int failureDrawable) //以资源id设置加载失败的动画
     *  .setLoadingDrawable(Drawable loadingDrawable) //设置加载中的动画
     *  .setLoadingDrawableId(int loadingDrawable) //以资源id设置加载中的动画
     *  .setIgnoreGif(false) //忽略Gif图片
     *  .setParamsBuilder(ParamsBuilder paramsBuilder) //在网络请求中添加一些参数
     *  .setRaduis(int raduis) //设置拐角弧度
     *  .setUseMemCache(true) //设置使用MemCache，默认true
     */
    public void bindCommonImage(ImageView iv, String url, boolean option) {
        if (option) {
            options = new ImageOptions.Builder()
                    .setImageScaleType(ImageView.ScaleType.CENTER_CROP)// 图片缩放模式
                    .setLoadingDrawableId(R.mipmap.ic_launcher) // 下载中显示的图片
                    .setFailureDrawableId(R.mipmap.ic_launcher)  // 下载失败显示的图片
                    .build();

            x.image().bind(iv, url, options);
        } else {
            x.image().bind(iv, url);
        }
    }

    /**
     * 圆形图片显示
     *
     * @param iv
     * @param url
     * @param option
     */
    public void bindCircularImage(ImageView iv, String url, boolean option) {
        if (option) {
            options = new ImageOptions.Builder()
                    .setLoadingDrawableId(R.mipmap.ic_launcher)
                    .setFailureDrawableId(R.mipmap.ic_launcher)
                    .setCircular(true).build();
            x.image().bind(iv, url, options);
        } else {
            x.image().bind(iv, url);
        }
    }


    /**
     * 文件上传
     *
     * @param url
     * @param maps
     * @param file
     * @param callback
     */
    public void upLoadFile(String url, Map<String, String> maps, Map<String, File> file, final XCallBack callback) {
        RequestParams params = new RequestParams(url);
        if (maps != null && !maps.isEmpty()) {
            for (Map.Entry<String, String> entry : maps.entrySet()) {
                params.addBodyParameter(entry.getKey(), entry.getValue());
            }
        }
        if (file != null) {
            for (Map.Entry<String, File> entry : file.entrySet()) {
                params.addBodyParameter(entry.getKey(), entry.getValue().getAbsoluteFile());
            }
        }
        // 有上传文件时使用multipart表单, 否则上传原始文件流.
        params.setMultipart(true);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                onSuccessResponse(result, callback);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {}

            @Override
            public void onCancelled(CancelledException cex) {}

            @Override
            public void onFinished() {}
        });

    }


    /**
     * 文件下载
     *
     * @param url
     * @param maps
     * @param callBack
     */
    public void downLoadFile(String url, Map<String, String> maps, final XDownLoadCallBack callBack,String path) {

        RequestParams params = new RequestParams(url);
        if (maps != null && !maps.isEmpty()) {
            for (Map.Entry<String, String> entry : maps.entrySet()) {
                params.addBodyParameter(entry.getKey(), entry.getValue());
            }
        }
        params.setAutoRename(true);// 断点续传
        params.setSaveFilePath(path);//path是文件路径
        x.http().post(params, new Callback.ProgressCallback<File>() {
            @Override
            public void onSuccess(final File result) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (callBack != null) {
                            callBack.onResponse(result);
                        }
                    }
                });
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {}

            @Override
            public void onCancelled(CancelledException cex) {}

            @Override
            public void onFinished() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (callBack != null) {
                            callBack.onFinished();
                        }
                    }
                });
            }

            @Override
            public void onWaiting() {}

            @Override
            public void onStarted() {}

            @Override
            public void onLoading(final long total, final long current, final boolean isDownloading) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (callBack != null) {
                            callBack.onLoading(total, current, isDownloading);
                        }
                    }
                });
            }
        });

    }


    /**
     * 异步get请求返回结果,json字符串
     *
     * @param result
     * @param callBack
     */
    private void onSuccessResponse(final String result, final XCallBack callBack) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (callBack != null) {
                    callBack.onResponse(result);
                }
            }
        });
    }


    public interface XCallBack {
        void onResponse(String result);
    }


    public interface XDownLoadCallBack extends XCallBack {

        void onResponse(File result);

        void onLoading(long total, long current, boolean isDownloading);

        void onFinished();
    }


}
