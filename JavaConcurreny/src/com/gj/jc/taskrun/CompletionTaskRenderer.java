package com.gj.jc.taskrun;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class CompletionTaskRenderer {

    private final static ExecutorService executorService = Executors.newCachedThreadPool();

    static void renderPage(List<ImageInfo> imageInfoList) throws ExecutionException, InterruptedException {

        CompletionService<ImageData> completionService = new ExecutorCompletionService<>(executorService);

        for (ImageInfo info : imageInfoList) {
            completionService.submit(new Callable<ImageData>() {
                @Override
                public ImageData call() throws Exception {
                    Thread.sleep(2000);

                    return new ImageData(info.url.getBytes());
                }
            });
        }


        renderText();

        for (int i = 0; i < imageInfoList.size(); i++) {
            Future<ImageData> imageDataFuture = completionService.take();

            ImageData imageData = imageDataFuture.get();

            renderImage(imageData);
        }

    }

    private static void renderImage(ImageData imageData) {
        System.out.println("renderImage --- " + new String(imageData.data));
    }

    private static void renderText() {
        System.out.println("renderText ---");
    }

    public static void main(String[] args) {

        List<ImageInfo> imageInfos = new ArrayList<>();

        imageInfos.add(new ImageInfo("1"));
        imageInfos.add(new ImageInfo("2"));
        imageInfos.add(new ImageInfo("3"));

        try {
            renderPage(imageInfos);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    static class ImageInfo {
        public String url;

        public ImageInfo(String url) {
            this.url = url;
        }
    }

    static class ImageData {
        public byte[] data;

        public ImageData(byte[] data) {
            this.data = data;
        }
    }

}
