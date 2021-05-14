package com.model.gpu;

import com.google.gson.Gson;
import com.model.Thing;

public class Gpu extends Thing {

    private final String model;
    private final String tflops;

    public Gpu(String model, String tflops) {
        this.model = model;
        this.tflops = tflops;
    }

    public static Gpu fromString(String json) {
        return new Gson().fromJson(json, Gpu.class);
    }

    @Override
    public String toScs() {
        return """
                %s <- concept_gpu;
                => nrel_main_idtf: [%s](*<- lang_ru;;*);
                => nrel_main_idtf: [%s](*<- lang_en;;*);
                => nrel_tflops: [%s];;
                """.formatted(model.replace(" ", "_")
                .replace("-", "_")
                .replace("™", "")
                .replace("®", ""), model, model, tflops);

//        return model.replace(" ", "_")
//                .replace("-", "_")
//                .replace("™", "")
//                .replace("®", "") + " <- concept_gpu;\n" +
//                "=> nrel_main_idtf: [" + model + "](*<- lang_ru;;*);\n" +
//                "=> nrel_main_idtf: [" + model + "](*<- lang_en;;*);\n" +
//                "=> nrel_tflops: [" + tflops + "];;\n";
    }

    @Override
    public String getName() {
        return model.replace(" ", "_").replace("-", "_").replace("™", "")
                .replace("®", "");
    }
}
