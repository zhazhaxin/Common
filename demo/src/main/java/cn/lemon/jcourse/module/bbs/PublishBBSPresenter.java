package cn.lemon.jcourse.module.bbs;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.google.gson.Gson;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.lemon.util.ImageUtil;
import cn.lemon.util.Utils;
import cn.lemon.common.base.presenter.SuperPresenter;
import cn.lemon.common.net.ServiceResponse;
import cn.lemon.jcourse.config.Config;
import cn.lemon.jcourse.model.BBSModel;
import cn.lemon.jcourse.model.bean.Info;
import cn.lemon.jcourse.model.bean.Picture;

/**
 * Created by linlongxin on 2016/9/16.
 */

public class PublishBBSPresenter extends SuperPresenter<PublishBBSActivity> {

    private List<Picture> pics = new ArrayList<>();
    private int picNum;

    public void dealPictures(List<String> paths) {
        pics.clear();
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
        BitmapFactory.Options op = new BitmapFactory.Options();
        op.inJustDecodeBounds = true;  //不分配内存设置
        BitmapFactory.decodeFile(pic.getPath(), op);
        int width = op.outWidth;
        int height = op.outHeight;
        BBSModel.getInstance().uploadPicture(pic, width, height, new ServiceResponse<Picture>() {
            @Override
            public void onNext(Picture picture) {
                pics.add(picture);
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
        String json = gson.toJson(pics);
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
