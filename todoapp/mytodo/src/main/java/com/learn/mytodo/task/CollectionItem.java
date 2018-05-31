package com.learn.mytodo.task;

public class CollectionItem {

    public String title;
    public int nums;

    public CollectionItem(String s, int i) {
        title = s;
        nums = i;
    }

    public CollectionItem(String title) {
        this.title = title;
    }
}
