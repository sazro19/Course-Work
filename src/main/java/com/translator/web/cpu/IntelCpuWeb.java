package com.translator.web.cpu;

import com.model.Thing;
import com.model.cpu.Cpu;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class IntelCpuWeb {

    public static Thing fetchCpu(int i, String url) {
        Connection connection = Jsoup.connect(url);
        try {
            Document document = connection.get();
            Elements cpuTitle = document.getElementsByAttributeValue("class", "product-family-title-text");
            String title = cpuTitle.stream().findFirst().get().getElementsByAttributeValue("class", "h1").text();
            if (!title.contains("Processor")) {
                return null;
            }
            String threadCount = document.getElementsByAttributeValue("data-key", "ThreadCount").text();
            String cpuClock = document.getElementsByAttributeValue("data-key", "ClockSpeedMax").text();
            System.out.println(i + " " + title + " " + threadCount + " " + cpuClock);
            return new Cpu(
                    title
                            .replace("®", "")
                            .replace("™", "")
                            .replace("-", "_")
                            .replace(" ", "_"),
                    cpuClock
                            .replace(" GHz", ""),
                    threadCount
            );
        } catch (IOException e) {
//            System.err.println(e.getMessage());
        }
        return null;
    }

    public static List<Thing> fetchCpus() {
        List<Thing> result = new ArrayList<>();
        for (int i = 212256 - 1; i < 999999; i++) {
            Thing thing = fetchCpu(i, "https://ark.intel.com/content/www/us/en/ark/products/" + i + "/C.html");
            if (thing != null) {
                result.add(thing);
            }
        }
        return result;
    }
}
