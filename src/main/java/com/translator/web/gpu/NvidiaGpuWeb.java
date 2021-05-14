package com.translator.web.gpu;

import com.model.Thing;
import com.model.gpu.Gpu;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class NvidiaGpuWeb {
    public static Thing fetchGpu(String concreteGraphicsCardUri) {
        Connection connection = Jsoup.connect(concreteGraphicsCardUri);

        try {
            Document document = connection.get();
            String gpuName = document.getElementsByAttributeValue("class", "gpudb-name").text();

            Elements sections = document.getElementsByTag("section");
            Element theoreticalPerformance = sections
                    .stream()
                    .filter(section -> section
                            .getElementsByTag("h2").text()
                            .contains("Theoretical Performance"))
                    .collect(Collectors.toList()).get(0);
            Elements dl = theoreticalPerformance.getElementsByTag("dl");
            Element dt = dl
                    .stream()
                    .filter(e -> e
                            .getElementsByTag("dt")
                            .text().contains("FP32"))
                    .collect(Collectors.toList()).get(0);
            String tflops = dt.getElementsByTag("dd").text();
            int i = tflops.indexOf(" ");
            tflops = tflops.substring(0, i);

            return new Gpu(gpuName, tflops);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //uriBase must be equal to https://www.techpowerup.com/gpu-specs/?mfgr=NVIDIA&generation=
    //generation must be like "GeForce 30"
    public static List<Thing> fetchGpus(String uriBase, String generation) {
        String url = uriBase + generation.replace(" ", "%20");
        Connection connection = Jsoup.connect(url);

        try {
            Document document = connection.get();
            Elements cards = document.getElementsByAttributeValue("class", "vendor-NVIDIA");
            List<Thing> collect = cards
                    .stream()
                    .map(c -> "https://www.techpowerup.com" + c
                            .getElementsByTag("a")
                            .get(0).attributes().get("href"))
                    .map(NvidiaGpuWeb::fetchGpu)
                    .collect(Collectors.toList());
            return collect;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
