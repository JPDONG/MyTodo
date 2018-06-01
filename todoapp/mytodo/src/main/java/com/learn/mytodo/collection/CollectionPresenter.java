package com.learn.mytodo.collection;

import android.content.Context;

import com.learn.mytodo.data.source.CollectionRepository;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.schedulers.Schedulers;

public class CollectionPresenter {

    CollectionRepository mCollectionRepository;

    public CollectionPresenter(Context context) {
        mCollectionRepository = CollectionRepository.getInstance(context);
    }

    public Observable<Boolean> save(CollectionItem item) {
        return Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter emitter) throws Exception {
                emitter.onNext(mCollectionRepository.save(item));
                emitter.onComplete();
            }
        })
                .subscribeOn(Schedulers.io());
    }

    public Observable<List<CollectionItem>> getCollections() {
        return Observable.create(new ObservableOnSubscribe<List<CollectionItem>>() {
            @Override
            public void subscribe(ObservableEmitter<List<CollectionItem>> emitter) throws Exception {
                emitter.onNext(mCollectionRepository.getCollections());
                emitter.onComplete();
            }
        })
                .subscribeOn(Schedulers.io());
    }
}
