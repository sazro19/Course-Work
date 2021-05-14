package com.model.cpu;

import com.google.gson.Gson;
import com.model.Thing;

public class Cpu extends Thing {
    private final String model;
    private final String frequency;
    private final String threads;

    public Cpu(String model, String frequency, String threads) {
        this.model = model;
        this.frequency = frequency;
        this.threads = threads;
    }

    public static Cpu fromString(String json) {
        return new Gson().fromJson(json, Cpu.class);
    }

    @Override
    public String toScs() {
        return """
                %s <- concept_cpu;
                => nrel_main_idtf: [%s](*<- lang_ru;;*);
                => nrel_main_idtf: [%s](*<- lang_en;;*);
                => nrel_logical_cores_count: [%s];
                => nrel_clock_mhz: [%s];;
                """
                .formatted(
                        model
                                .replace(" ", "_")
                                .replace("-", "_")
                                .replace("™", "")
                                .replace("®", ""),
                        model,
                        model,
                        threads,
                        frequency
                );
//        return model.replace(" ", "_")
//                .replace("-", "_")
//                .replace("™", "")
//                .replace("®", "") + " <- concept_cpu;\n" +
//                "=> nrel_main_idtf: [" + model + "](*<- lang_ru;;*);\n" +
//                "=> nrel_main_idtf: [" + model + "](*<- lang_en;;*);\n" +
//                "=> nrel_logical_cores_count: [" + threads + "];\n" +
//                "=> nrel_clock_mhz: [" + frequency + "];;";
    }

    @Override
    public String getName() {
        return model.replace(" ", "_").replace("-", "_").replace("™", "")
                .replace("®", "");
    }
}
