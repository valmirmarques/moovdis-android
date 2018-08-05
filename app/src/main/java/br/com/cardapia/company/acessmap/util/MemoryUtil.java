package br.com.cardapia.company.acessmap.util;

import android.util.Log;

/**
 * Created by tvtios-01 on 14/04/18.
 */

public class MemoryUtil {

    public static final int MAX_MEMORY_FREE = 50;

    public static int getAvaiableMemory() {
        final Runtime runtime = Runtime.getRuntime();
        final long usedMemInMB = (runtime.totalMemory() - runtime.freeMemory()) / 1048576L;
        final long maxHeapSizeInMB = runtime.maxMemory() / 1048576L;
        final long availHeapSizeInMB = maxHeapSizeInMB - usedMemInMB;
        return (int) availHeapSizeInMB;
    }

    /**
     * Verifica se tem memoria Livre
     * @return
     */
    public static boolean hasMemoryFree () {
        boolean hasMemoryFree = getAvaiableMemory() >= MAX_MEMORY_FREE;
        if (!hasMemoryFree) {
            Log.i("MemoryUtil", "Forced invoking Garbaged Collector");
            System.gc();
        }
        return hasMemoryFree;
    }

}
