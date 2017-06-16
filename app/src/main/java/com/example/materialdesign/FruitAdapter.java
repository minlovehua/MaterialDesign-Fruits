package com.example.materialdesign;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import java.util.List;

/**
 * Created by 媚敏 on 2017/6/14.
 */

/**
 * 15.为RecyclerView准备一个适配器，让这个适配器继承自RecyclerView.Adapter,并将泛型指定为FruitAdapter.ViewHolder
 */
public class FruitAdapter extends RecyclerView.Adapter<FruitAdapter.ViewHolder> {

    private Context mContext;
    private List<Fruit> mFruitList;

    static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        ImageView fruitImage;
        TextView fruitName;
        public ViewHolder(View view) {
            super(view);
            cardView = (CardView) view;
            fruitImage = (ImageView) view.findViewById(R.id.fruit_image);
            fruitName = (TextView) view.findViewById(R.id.fruit_name);
        }
    }
    public FruitAdapter(List<Fruit> fruitList) {
        mFruitList = fruitList;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.fruit_item, parent, false);


        //19(11).处理RecycleView的点击事件，否则打不开FruitActivity
        final ViewHolder holder = new ViewHolder(view);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            //给CardView注册一个点击事件监听器，在点击事件中获取水果名和水果图片的资源id，再把它传入Intent中，
            // 最后调用startActivity()方法启动FruitActivity
            @Override
            public void onClick(View v) {
                int position=holder.getAdapterPosition();
                Fruit fruit=mFruitList.get(position);
                Intent intent=new Intent(mContext,FruitActivity.class);
                intent.putExtra(FruitActivity.FRUIT_NAME,fruit.getName());
                intent.putExtra(FruitActivity.FRUIT_IMAGE_ID,fruit.getImageId());
                mContext.startActivity(intent);
            }
        });
        //19(11)
        return holder;

        //15.return new ViewHolder(view);
    }




    /**
     * 使用Glide加载图片:
     * (原因：网上的高像素照片不经压缩就直接展示会引起内存溢出。Glide在内部做了许多复杂的逻辑操作，其中包括图片的压缩，我们只需要按照Glide的标准法去加载图片，无需担心内存会溢出)
     * 调用Glide.with()方法并传入一个Context、Activity或Fragment参数
     * 调用load()方法加载图片，传入一个URL地址、一个本地路径或者一个资源id
     * 调用into()方法将图片设置到具体的某个ImageView中
     */
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Fruit fruit = mFruitList.get(position);
        holder.fruitName.setText(fruit.getName());
        Glide.with(mContext).load(fruit.getImageId()).into(holder.fruitImage);
    }

    @Override
    public int getItemCount() {
        return mFruitList.size();
    }


}
