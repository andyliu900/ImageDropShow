package com.moping.imageshow.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.moping.imageshow.R;
import com.moping.imageshow.view.RotateTextView;

import java.util.List;

public class FolderAdapter extends RecyclerView.Adapter<FolderAdapter.ViewHolder> {

    private int[] mFolderRes = {R.mipmap.folder_one, R.mipmap.folder_two, R.mipmap.folder_three,
            R.mipmap.folder_four, R.mipmap.folder_five,R.mipmap.folder_six};
    private int[] mFolderSelectedRes = {R.mipmap.folder_one_press, R.mipmap.folder_two_press, R.mipmap.folder_three_press,
            R.mipmap.folder_four_press, R.mipmap.folder_five_press, R.mipmap.folder_six_press};
    private List<String> mFolderNames;
    private int currentSelectedFolderIndes = -1;
    private OnFolderClickListener mListener;

    public void setOnFolderClickListener(OnFolderClickListener listener) {
        mListener = listener;
    }

    public void setFolderNames(List<String> folderNames) {
        this.mFolderNames = folderNames;
    }

    public void setCurrentSelectedFolderIndes(int index) {
        currentSelectedFolderIndes = index;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_folder, parent, false);
        FolderAdapter.ViewHolder viewHolder = new FolderAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        if (currentSelectedFolderIndes == position) {
            holder.folder_btn.setImageResource(mFolderSelectedRes[position]);
        } else {
            holder.folder_btn.setImageResource(mFolderRes[position]);
        }
        holder.folder_tv.setText(mFolderNames.get(position));
        holder.folder_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null) {
                    mListener.folderClick(position);
                }
            }
        });
        holder.folder_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null) {
                    mListener.folderClick(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mFolderNames == null ? 0 : mFolderNames.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageButton folder_btn;
        RotateTextView folder_tv;

        public ViewHolder(View itemView) {
            super(itemView);

            folder_btn = (ImageButton)itemView.findViewById(R.id.folder_btn);
            folder_tv = (RotateTextView) itemView.findViewById(R.id.folder_tv);
        }
    }

    public interface OnFolderClickListener {
        void folderClick(int position);
    }

}
