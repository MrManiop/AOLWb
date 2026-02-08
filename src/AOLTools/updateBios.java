package AOLTools;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.stream.Stream;

public class updateBios {

    //Aktualizuje BIOS

    public static void BIOSUpdate() throws IOException, InterruptedException{

        //BIOS path
        Path baseBios = Paths.get("C:\\projects\\AOLWb\\BIOS");

        //Getting pc info
        ComputerInfo.Info computerData = ComputerInfo.getComputerInfo();

        // building path
        Path sourceBIOSdir = baseBios
                .resolve(computerData.manufacturer)
                .resolve(computerData.model)
                .normalize();

        runExe(sourceBIOSdir);

    }

    public static int runExe(Path dir) throws IOException, InterruptedException {

        Optional<Path> exeOpt;
        try (Stream<Path> files = Files.list(dir)) {
            exeOpt = files
                    .filter(p -> p.toString().toLowerCase().endsWith(".exe"))
                    .findFirst();
        }

        if (exeOpt.isEmpty()) {
            throw new IOException("Exe not found in " + dir);
        }

        Path exe = exeOpt.get();
        System.out.println("Starting: " + exe);

        ProcessBuilder pb = new ProcessBuilder(exe.toString());
        pb.directory(dir.toFile());   // katalog roboczy = folder exe
        pb.inheritIO();               // pokazuje output w konsoli

        Process p = pb.start();
        return p.waitFor();
    }
}
