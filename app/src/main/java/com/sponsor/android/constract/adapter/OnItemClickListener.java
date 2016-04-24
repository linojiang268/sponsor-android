package com.sponsor.android.constract.adapter;

import android.view.View;

public abstract class OnItemClickListener<E> implements View.OnClickListener{
    private E model;
    private int position;

    public OnItemClickListener(int p, E m){
        this.model = m;
        this.position = p;
    }


    @Override
    public void onClick(View view) {
        onItemClick(position, model);
    }

    public abstract void onItemClick(int position, E e);
}
