package util;

import model.RouteResult;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class CsvExporter {
    public static void exportBenchmarks(String filePath, List<BenchmarkRecord> records) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write("algorithm,nodes,edges,failureRate,congestionLevel,runtimeMs,totalCost,hopCount,rerouteCount,success,path\n");
            for (BenchmarkRecord record : records) {
                RouteResult r = record.getResult();
                writer.write(String.format(
                        "%s,%d,%d,%.3f,%.3f,%d,%.2f,%d,%d,%b,\"%s\"\n",
                        record.getAlgorithm(),
                        record.getNodes(),
                        record.getEdges(),
                        record.getFailureRate(),
                        record.getCongestionLevel(),
                        r.getRuntimeMs(),
                        r.getTotalCost(),
                        Math.max(0, r.getPath().size() - 1),
                        r.getRerouteCount(),
                        r.isSuccess(),
                        r.getPath().toString()
                ));
            }
        }
    }
}
