package com;

import com.fileService.FileService;
import com.model.Thing;
import com.translator.web.cpu.AmdCpuWeb;
import com.translator.web.cpu.IntelCpuWeb;
import com.translator.web.gpu.AmdGpuWeb;
import com.translator.web.gpu.NvidiaGpuWeb;
import org.apache.commons.cli.*;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        Options options = new Options();

        Option cpuOption =
                new Option("c", "cpu", false, "");
        cpuOption.setRequired(false);
        options.addOption(cpuOption);

        Option gpuOption =
                new Option("g", "gpu", false, "");
        gpuOption.setRequired(false);
        options.addOption(gpuOption);

        Option webOption =
                new Option("w", "web", false, "");
        gpuOption.setRequired(false);
        options.addOption(webOption);

        Option localOption =
                new Option("l", "local", false, "");
        localOption.setRequired(false);
        options.addOption(localOption);

        Option inputDirOption =
                new Option("", "input-dir", true, "");
        inputDirOption.setRequired(false);
        options.addOption(inputDirOption);

        Option outputDirOption =
                new Option("", "output-dir", true, "");
        outputDirOption.setRequired(true);
        options.addOption(outputDirOption);

        Option brandOption =
                new Option("", "brand", true, "");
        brandOption.setRequired(true);
        options.addOption(brandOption);

        Option generationOption =
                new Option("", "generation", true, "");
        generationOption.setRequired(false);
        options.addOption(generationOption);

        Option url =
                new Option("", "url", true, "");
        url.setRequired(false);
        options.addOption(url);

        CommandLine cmd = null;
        try {
            cmd = new DefaultParser().parse(options, args);
        } catch (ParseException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }

        try {
            if (cmd.hasOption("local")) {
                if (cmd.hasOption("input-dir")) {
                    if (cmd.hasOption("output-dir")) {
                        String inputDir = cmd.getOptionValue("input-dir");
                        String outputDir = cmd.getOptionValue("output-dir");
                        List<Thing> things = FileService.read(inputDir);
                        FileService.write(things, outputDir);
                    }
                }
            } else if (cmd.hasOption("cpu")) {
                if (cmd.hasOption("web")) {
                    if (cmd.hasOption("brand")) {
                        if (cmd.hasOption("output-dir")) {
                            String cpuBrand = cmd.getOptionValue("brand");
                            String outputDir = cmd.getOptionValue("output-dir");
//                            List<Thing> things = switch (cpuBrand) {
//                                case "intel" -> IntelCpuWeb.fetchCpus();
//                                case "amd" -> AmdCpuWeb.fetchCpus();
//                                default -> throw new RuntimeException("Unknown brand");
//                            };
                            List<Thing> things = null;
                            if (cpuBrand.equals("intel")) {
                                if (cmd.hasOption("url")) {
                                    String url1 = cmd.getOptionValue("url");
                                    things = new ArrayList<>();
                                    things.add(IntelCpuWeb.fetchCpu(0, url1));
                                } else {
                                    things = IntelCpuWeb.fetchCpus();
                                }
                            } else if (cpuBrand.equals("amd")) {
                                things = AmdCpuWeb.fetchCpus();
                            } else {
                                throw new RuntimeException("Unknown brand");
                            }
                            FileService.write(things, outputDir);
                        }
                    }
                }
            } else if (cmd.hasOption("gpu")) {
                if (cmd.hasOption("web")) {
                    if (cmd.hasOption("brand")) {
                        if (cmd.hasOption("output-dir")) {
                            String gpuBrand = cmd.getOptionValue("brand");
                            String outputDir = cmd.getOptionValue("output-dir");
                            List<Thing> things = null;
                            if (gpuBrand.equals("nvidia")) {
                                if (cmd.hasOption("generation")) {
                                    String generation = cmd.getOptionValue("generation");
                                    things = NvidiaGpuWeb.fetchGpus(
                                            "https://www.techpowerup.com/gpu-specs/?mfgr=NVIDIA&generation=",
                                            generation
                                    );
                                } else if (cmd.hasOption("url")) {
                                    String url_ = cmd.getOptionValue("url");
                                    things = new ArrayList<>();
                                    things.add(NvidiaGpuWeb.fetchGpu(url_));
                                }
                            } else if (gpuBrand.equals("amd")) {
                                things = AmdGpuWeb.fetchGpus();
                            } else {
                                throw new RuntimeException("Unknown brand");
                            }
                            FileService.write(things, outputDir);
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Unable to fetch: " + e.getMessage());
            System.exit(1);
        }
    }
}
