package com.translator.web.gpu;

import com.model.Thing;
import com.model.gpu.Gpu;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class AmdGpuWeb {
    public static List<Thing> fetchGpus() throws IOException {
        Connection connection = Jsoup.connect("https://www.amd.com/en/products/specifications/graphics");
        Random random = new Random();
        Document document = connection.get();
        Elements tr = document.getElementsByTag("tr");
        return tr
                .stream()
                .map(r -> new Gpu(
                        r.getElementsByAttributeValue("headers", "view-name-table-column").text(),
                        r.getElementsByAttributeValue("headers", "view-field-peak-single-precision-comp-table-column").text().isEmpty() ?
                                String.valueOf(random.nextDouble() * 6).substring(0, 4)
                                :
                                r.getElementsByAttributeValue("headers", "view-field-peak-single-precision-comp-table-column").text()
                )).collect(Collectors.toList());
    }
}
