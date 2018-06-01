package com.learn.mytodo.collection;

import java.util.UUID;

public class CollectionItem {

    public String id;
    public String title;
    public int nums;
    public String createAt;

    public CollectionItem(String s, int i) {
        this.id = UUID.randomUUID().toString();
        title = s;
        nums = i;
    }

    public CollectionItem(String title) {
        this.id = UUID.randomUUID().toString();
        this.title = title;
    }

    public CollectionItem(String id, String title, String createAt) {
        this.id = id;
        this.title = title;
        this.createAt = createAt;
    }
}
