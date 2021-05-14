package com.translator.web.cpu;

import com.model.Thing;
import com.model.cpu.Cpu;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class AmdCpuWeb {
    public static List<Thing> fetchCpus() throws IOException {
        Connection connection = Jsoup.connect("https://www.amd.com/en/products/specifications/processors");
        Document document = connection.get();
        Elements tr = document.getElementsByTag("tr");
        return tr
                .stream()
                .map(element -> new Cpu(
                        element.getElementsByAttributeValue("headers", "view-name-table-column").text(),
                        element.getElementsByAttributeValue("headers", "view-field-cpu-clock-speed-table-column").text(),
                        element.getElementsByAttributeValue("headers", "view-field-thread-count-table-column").text())
                ).collect(Collectors.toList());
    }
}
