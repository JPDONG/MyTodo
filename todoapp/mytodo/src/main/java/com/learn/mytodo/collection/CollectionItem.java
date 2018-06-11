package com.learn.mytodo.collection;

import java.util.UUID;

public class CollectionItem {

    public String id;
    public String title;
    public int nums;
    public String createAt;

    public CollectionItem(String title, int nums) {
        this.id = UUID.randomUUID().toString();
        title = title;
        nums = nums;
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

    public CollectionItem(String id, String title, String createAt, int nums) {
        this.id = id;
        this.title = title;
        this.createAt = createAt;
        this.nums = nums;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public int getNums() {
        return nums;
    }
}
