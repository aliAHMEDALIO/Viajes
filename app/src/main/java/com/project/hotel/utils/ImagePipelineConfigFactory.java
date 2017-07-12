/*

    Copyright 2017, The Android Open Source Project

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

    IF there any question ,Please contact me at :
    m.elbehiry44@gmail.com

*/


package com.project.hotel.utils;





import android.content.Context;
import android.os.Build;
import android.os.Environment;

import com.facebook.cache.disk.DiskCacheConfig;
import com.facebook.common.internal.Supplier;
import com.facebook.common.util.ByteConstants;
import com.facebook.imagepipeline.cache.MemoryCacheParams;
import com.facebook.imagepipeline.core.ImagePipelineConfig;

import java.io.File;

/**
 * Creates ImagePipeline configuration for the sample app
 */
public class ImagePipelineConfigFactory {
    private static final String IMAGE_PIPELINE_CACHE_DIR = "imagepipeline_cache";

    private static ImagePipelineConfig sImagePipelineConfig;
//    private static ImagePipelineConfig sOkHttpImagePipelineConfig;

    private static final int MAX_HEAP_SIZE = (int) Runtime.getRuntime().maxMemory();

    public static final int MAX_DISK_CACHE_SIZE = 300 * ByteConstants.MB;
    public static final int MAX_MEMORY_CACHE_SIZE = MAX_HEAP_SIZE ;

    /**
     * Creates config using android http stack as network backend.
     */
    public static ImagePipelineConfig getImagePipelineConfig(Context context) {
        if (sImagePipelineConfig == null) {
            ImagePipelineConfig.Builder configBuilder = ImagePipelineConfig.newBuilder(context);
            configureCaches(configBuilder, context);
            sImagePipelineConfig = configBuilder.build();
        }
        return sImagePipelineConfig;
    }

    /**
     * Creates config using OkHttp as network backed.
     */
/*  public static ImagePipelineConfig getOkHttpImagePipelineConfig(Context context) {
    if (sOkHttpImagePipelineConfig == null) {
      OkHttpClient okHttpClient = new OkHttpClient();
      ImagePipelineConfig.Builder configBuilder =
        OkHttpImagePipelineConfigFactory.newBuilder(context, okHttpClient);
      configureCaches(configBuilder, context);
      sOkHttpImagePipelineConfig = configBuilder.build();
    }
    return sOkHttpImagePipelineConfig;
  }*/

    /**
     * Configures disk and memory cache not to exceed common limits
     */
    private static void configureCaches(ImagePipelineConfig.Builder configBuilder, Context context) {
        final MemoryCacheParams bitmapCacheParams = new MemoryCacheParams(
                MAX_MEMORY_CACHE_SIZE, // Max total size of elements in the cache
                Integer.MAX_VALUE,                     // Max entries in the cache
                MAX_MEMORY_CACHE_SIZE, // Max total size of elements in eviction queue
                Integer.MAX_VALUE,                     // Max length of eviction queue
                Integer.MAX_VALUE);                    // Max cache entry size
        configBuilder
                .setBitmapMemoryCacheParamsSupplier(
                        new Supplier<MemoryCacheParams>() {
                            public MemoryCacheParams get() {
                                return bitmapCacheParams;
                            }
                        })
                .setMainDiskCacheConfig(DiskCacheConfig.newBuilder(context)
                        .setBaseDirectoryPath(getExternalCacheDir(context))
                        .setBaseDirectoryName(IMAGE_PIPELINE_CACHE_DIR)
                        .setMaxCacheSize(MAX_DISK_CACHE_SIZE)
                        .build());
    }

    public static File getExternalCacheDir(final Context context) {
        if (hasExternalCacheDir())
            return context.getExternalCacheDir();

        // Before Froyo we need to construct the external cache dir ourselves
        final String cacheDir = "/Android/data/" + context.getPackageName() + "/cache/";
        return createFile(Environment.getExternalStorageDirectory().getPath() + cacheDir, "");
    }

    public static boolean hasExternalCacheDir() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO;
    }

    public static File createFile(String folderPath, String fileName) {
        File destDir = new File(folderPath);
        if (!destDir.exists()) {
            destDir.mkdirs();
        }
        return new File(folderPath, fileName);
    }


}
