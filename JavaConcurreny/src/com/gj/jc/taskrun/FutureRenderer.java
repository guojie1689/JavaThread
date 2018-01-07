package com.gj.jc.taskrun;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class FutureRenderer {

    private final static ExecutorService executorService = Executors.newCachedThreadPool();


    static void renderPage(List<ImageInfo> imageInfoList) throws ExecutionException {

        Callable<List<ImageData>> callable = new Callable<List<ImageData>>() {
            @Override
            public List<ImageData> call() throws Exception {
                List<ImageData> imageDataList = new ArrayList<>();

                for (ImageInfo info : imageInfoList) {
                    imageDataList.add(new ImageData(info.url.getBytes()));

                    System.out.println("render call -- ");

                    Thread.sleep(1000);
                }

                return imageDataList;
            }
        };

        Future<List<ImageData>> future = executorService.submit(callable);

        renderText();

        try {
            List<ImageData> imageDataLIst = future.get();

            for (int i = 0; i < imageDataLIst.size(); i++) {
                System.out.println("renderImage --- :" + new String(imageDataLIst.get(i).data));
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (ExecutionException e) {
            throw e;
        }


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
