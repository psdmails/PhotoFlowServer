package com.psdutta.hmi.photogalaxy;

import com.facebook.imagepipeline.image.EncodedImage;
import com.facebook.imagepipeline.producers.Consumer;
import com.facebook.imagepipeline.producers.FetchState;
import com.facebook.imagepipeline.producers.ProducerContext;

public class FetchRequest extends FetchState {

    public FetchRequest(Consumer<EncodedImage> consumer, ProducerContext context) {
        super(consumer, context);
    }

}
