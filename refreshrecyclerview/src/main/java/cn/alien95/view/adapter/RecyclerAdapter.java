package cn.alien95.view.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * Created by llxal on 2015/12/19.
 */
public abstract class RecyclerAdapter<T> extends RecyclerView.Adapter<BaseViewHolder<T>> {

    private static final String TAG = "RecyclerAdapter";

    public static final int HEADER_TYPE = 111;

    public static final int FOOTER_TYPE = 222;

    public static final int NO_MORE_TYPE = 333;

    private boolean isHasHeader = false;

    private boolean isHasFooter = false;

    private boolean isStopAdapterLoadData = false;  //配合StopMore()使用

    private List<T> mData = new ArrayList<>();

    private View mHeaderView;

    private View mFooterView;

    private Context mContext;

    public RecyclerAdapter(Context context) {
        mContext = context;
    }

    public RecyclerAdapter(Context context, T[] mData) {
        this(context, Arrays.asList(mData));
    }

    public RecyclerAdapter(Context context, List<T> mData) {
        mContext = context;
        this.mData = mData;
        notifyDataSetChanged();
    }

    @Override
    public BaseViewHolder<T> onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == HEADER_TYPE) {
            return new BaseViewHolder(mHeaderView);
        } else if (viewType == FOOTER_TYPE) {
            return new BaseViewHolder(mFooterView);
        } else if (viewType == NO_MORE_TYPE) {
            return new BaseViewHolder(mFooterView);
        } else
            return onCreateBaseViewHolder(parent, viewType);
    }


    public abstract BaseViewHolder<T> onCreateBaseViewHolder(ViewGroup parent, int viewType);

    @Override
    public void onBindViewHolder(BaseViewHolder<T> holder, int position) {
        if (!isHasHeader && !isHasFooter) {
            holder.setData(mData.get(position));
        } else if (isHasHeader && !isHasFooter) {
            if (position != 0) {
                holder.setData(mData.get(position - 1));
            }
        } else if (!isHasHeader && isHasFooter) {
            if (position != mData.size()) {
                holder.setData(mData.get(position));
            }
        } else {
            if (position != 0 && position != mData.size() + 1) {
                holder.setData(mData.get(position - 1));
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (isHasHeader && isHasFooter) {
            if (position == 0) {
                return HEADER_TYPE;
            } else if (position == mData.size() + 1) {
                if (isStopAdapterLoadData) {
                    return NO_MORE_TYPE;
                }
                return FOOTER_TYPE;
            } else {
                return 0;
            }

        } else if (isHasHeader && !isHasFooter) {
            if (position == 0) {
                return HEADER_TYPE;
            } else {
                return 0;
            }
        } else if (!isHasHeader && isHasFooter) {
            if (position == mData.size()) {
                if (isStopAdapterLoadData) {
                    return NO_MORE_TYPE;
                }
                return FOOTER_TYPE;
            } else
                return 0;
        } else {
            return super.getItemViewType(position);
        }

    }

    @Override
    public int getItemCount() {
        int count = mData.size();
        if (isHasHeader)
            count++;
        if (isHasFooter) {
            count++;
        }
        return count;
    }

    /**
     * 添加一个实例
     *
     * @param object
     */
    public void add(T object) {
        if (!isStopAdapterLoadData) {
            mData.add(object);
            notifyDataSetChanged();
        }
    }

    /**
     * 插入数据
     *
     * @param object   实例
     * @param position 插入的位置
     */
    public void insert(T object, int position) {
        if (!isStopAdapterLoadData) {
            mData.add(position, object);
            notifyDataSetChanged();
        }
    }

    /**
     * 添加一组数据
     *
     * @param list
     */
    public void addAll(List<T> list) {
        if (!isStopAdapterLoadData) {
            mData.addAll(list);
            notifyDataSetChanged();
        }
    }

    /**
     * 添加一组数据
     *
     * @param objects
     */
    public void addAll(T[] objects) {
        if (!isStopAdapterLoadData) {
            mData.addAll(Arrays.asList(objects));
            notifyDataSetChanged();
        }

    }

    /**
     * 替换某个数据
     *
     * @param object   实例
     * @param position 替换数据的位置
     */
    public void replace(T object, int position) {
        if (!isStopAdapterLoadData) {
            mData.set(position, object);
            notifyDataSetChanged();
        }
    }

    /**
     * 删除数据
     *
     * @param object
     */
    public void remove(T object) {
        mData.remove(object);
        notifyDataSetChanged();
    }

    /**
     * 删除数据通过位置
     *
     * @param position
     */
    public void remove(int position) {
        mData.remove(position);
        notifyItemRemoved(position);
    }

    /**
     * 清空数据
     */
    public void clear() {
        mData.clear();
        notifyDataSetChanged();
    }

    /**
     * 设置一个Header
     *
     * @param header
     */
    public void setHeader(View header) {
        isHasHeader = true;
        mHeaderView = header;
    }

    /**
     * 设置一个Header
     *
     * @param res 设置的布局的ID
     */
    public void setHeader(@LayoutRes int res) {
        isHasHeader = true;
        mHeaderView = LayoutInflater.from(mContext).inflate(res, null);
    }

    /**
     * 设置一个Footer
     *
     * @param footer
     */
    public void setFooter(View footer) {
        isHasFooter = true;
        mFooterView = footer;
        notifyDataSetChanged();
    }

    /**
     * 设置一个Footer
     *
     * @param res 设置的布局的ID
     */
    public void setFooter(@LayoutRes int res) {
        isHasFooter = true;
        mFooterView = LayoutInflater.from(mContext).inflate(res, null);
    }

    /**
     * 移除Header
     */
    public void removeHeader() {
        if (isHasHeader) {
            isHasHeader = false;
        }
    }

    /**
     * 移除Footer
     */
    public void removeFooter() {
        if (isHasFooter) {
            isHasFooter = false;
        }
    }

    /**
     * 停止Adapter加载数据
     */
    public void stopAdapterLoadData() {
        isStopAdapterLoadData = true;
    }

    /**
     * 打开Adapter加载数据
     */
    public void openAdapterLoadData() {
        isStopAdapterLoadData = false;
    }
}
