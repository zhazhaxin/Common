package cn.lemon.jcourse.module.bbs;

import android.graphics.Bitmap;

import com.google.gson.Gson;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.alien95.util.ImageUtil;
import cn.alien95.util.Utils;
import cn.lemon.common.base.presenter.SuperPresenter;
import cn.lemon.common.net.ServiceResponse;
import cn.lemon.jcourse.config.Config;
import cn.lemon.jcourse.model.BBSModel;
import cn.lemon.jcourse.model.bean.Info;

/**
 * Created by linlongxin on 2016/9/16.
 */

public class PublishBBSPresenter extends SuperPresenter<PublishBBSActivity> {

    private List<String> picUrls = new ArrayList<>();
    private int picNum;

    public void dealPictures(List<String> paths) {
        picUrls.clear();
        if (paths.size() > 0) {
            getView().showLoadingDialog();
            //批量压缩图片
            ImageUtil.compress(paths, 350, 350, new ImageUtil.ListCallback() {
                @Override
                public void callback(List<Bitmap> bitmaps) {
                    getView().setBitmaps(bitmaps);
                }
            }, new ImageUtil.ListPathCallback() {
                @Override
                public void callback(List<File> files) {
                    picNum = files.size();
                    for (File pic : files) {
                        uploadPic(pic);
                    }
                }
            });
        }
    }

    //上传图片
    public void uploadPic(final File pic) {
        BBSModel.getInstance().uploadPicture(pic, new ServiceResponse<Info>() {
            @Override
            public void onNext(Info info) {
                picUrls.add(Config.CACEH_IAMGE + pic.getName());
                picNum--;
                if (picNum == 0) {
                    getView().dismissLoadingDialog();
                    Utils.Toast("上传成功");
                }
            }
        });
    }

    //发布BBS
    public void publishBBS(String title, String content) {
        Gson gson = new Gson();
        String json = gson.toJson(picUrls);
        BBSModel.getInstance().publishBBS(json, title, content, new ServiceResponse<Info>() {
            @Override
            public void onNext(Info info) {
                Utils.Toast("发布成功");
                getView().setResult(Config.RESULT_PUBLISH_BBS);
                getView().finish();
            }
        });
    }
}
