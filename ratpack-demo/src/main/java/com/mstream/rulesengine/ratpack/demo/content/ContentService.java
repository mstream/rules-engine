package com.mstream.rulesengine.ratpack.demo.content;

import com.mstream.rulesengine.ratpack.demo.IdFixtures;
import ratpack.exec.Promise;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ContentService {

    public Promise<Boolean> isContentFreeToAir(Integer contentId) {
        return Promise.value(
                IdFixtures.CONTENT_FREE_TO_AIR.equals(contentId));
    }

}
