package com.thekusch.emoji_mashup;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.appindexing.FirebaseAppIndex;
import com.google.firebase.appindexing.FirebaseAppIndexingInvalidArgumentException;
import com.google.firebase.appindexing.Indexable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class UploadSticker {
    private static final String TAG = "AppIndexingUtil";
    private static final String FAILED_TO_INSTALL_STICKERS = "Failed to install stickers";

    public static void setStickers(final Context context, FirebaseAppIndex firebaseAppIndex) {
        try {
            List<Indexable> stickers = getIndexableStickers();
            Indexable stickerPack = getIndexableStickerPack(stickers);

            List<Indexable> indexables = new ArrayList<>(stickers);
            indexables.add(stickerPack);

            Task<Void> task = firebaseAppIndex.update(
                    indexables.toArray(new Indexable[0]));

            task.addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(context, "Successfully added stickers", Toast.LENGTH_SHORT)
                            .show();
                }
            });

            task.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d(TAG, FAILED_TO_INSTALL_STICKERS, e);
                    Toast.makeText(context, FAILED_TO_INSTALL_STICKERS, Toast.LENGTH_SHORT)
                            .show();
                }
            });
        } catch (IOException | FirebaseAppIndexingInvalidArgumentException e) {
            Log.e(TAG, "Unable to set stickers", e);
        }
    }

    private static Indexable getIndexableStickerPack(List<Indexable> stickers)
            throws IOException, FirebaseAppIndexingInvalidArgumentException {
        Log.d("SIZE",""+StaticMember.Companion.getUri().size());
        Indexable.Builder stickerPackBuilder = new Indexable.Builder("StickerPack")
                .setName(StaticMember.packName)
                .setUrl("custom_emoji://sticker/pack/"+StaticMember.packName)
                .setImage(StaticMember.Companion.getUri().get(0))
                .put("hasSticker",stickers.toArray(new Indexable[0]));

        return stickerPackBuilder.build();
    }

    private static List<Indexable> getIndexableStickers() throws IOException,
            FirebaseAppIndexingInvalidArgumentException {
        List<Indexable> indexableStickers = new ArrayList<>();

        for(int i=0; i<StaticMember.Companion.getUri().size(); i++){
            Indexable.Builder indexableStickerBuilder = new Indexable.Builder("StickerPack")
                    .setName("$i")
                    .setUrl("custom_emoji://sticker/"+i)
                    .setImage(StaticMember.Companion.getUri().get(i))
                    .setKeywords(StaticMember.packName)
                    .put("partOf",new Indexable.Builder("StickerPack")
                            .setName(StaticMember.packName)
                            .build());
            indexableStickers.add(indexableStickerBuilder.build());
        }

        return indexableStickers;
    }
}
