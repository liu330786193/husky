package com.lyl.husky.core.util.concurrent;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BlockUtils {

    public static void waitingShortTime(){
        sleep(100L);
    }

    public static void sleep(final long millis){
        try {
            Thread.sleep(millis);
        } catch (final InterruptedException ex){
            Thread.currentThread().interrupt();
        }
    }

}
