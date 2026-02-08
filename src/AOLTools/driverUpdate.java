package AOLTools;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;

public class driverUpdate {
    /**
     * Updates drivers following structure :
     * drivers/<manufacturer>/<model>
     */

    public static void checkDrivers(){
        //Driver path
        Path baseDriversDir = Paths.get("C:\\projects\\AOLWb\\Drivers");

        //getting pc info
        ComputerInfo.Info computerData = ComputerInfo.getComputerInfo();

        // building path
        Path sourceDriverDir = baseDriversDir
                .resolve(computerData.manufacturer)
                .resolve(computerData.model)
                .normalize();

        if (Files.exists(sourceDriverDir)){
            System.out.println("Drivers available");
        }
        else {
            System.out.println("DRIVERS ARE NOT AVAIlABLE");
        }

    }

    public static void updateDrivers() {

        //Driver path
        Path baseDriversDir = Paths.get("C:\\projects\\AOLWb\\Drivers");

        ComputerInfo.Info computerData = ComputerInfo.getComputerInfo();

        Path sourceDriverDir = baseDriversDir
                .resolve(computerData.manufacturer)
                .resolve(computerData.model)
                .normalize();

        try {
            int code = runPnputil(sourceDriverDir);
        } catch (IOException e) {
            System.err.println("IO error cmd: " + e.getMessage());
            e.printStackTrace();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Interrupted: " + e.getMessage());
            e.printStackTrace();
        }
    }
    public static void updateDriversManual(Path targetDir) throws IOException{
        try {
            int code = runPnputil(targetDir);
        } catch (IOException e) {
            System.err.println("IO error: " + e.getMessage());
            e.printStackTrace();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); //
            System.err.println("Interrupted: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void exportDriversManual(Path targetDir) throws IOException{
        AOLInstall.createDir(targetDir.toString());
        try {
            int code = runExport(targetDir);
        } catch (IOException e) {
            System.err.println("IO error: " + e.getMessage());
            e.printStackTrace();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Interrupted: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void exportDriversAuto() throws IOException{
        //Driver path
        Path baseDriversDir = Paths.get("C:\\projects\\AOLWb\\Drivers");


        ComputerInfo.Info computerData = ComputerInfo.getComputerInfo();


        Path targetDriverDir = baseDriversDir
                .resolve(computerData.manufacturer)
                .resolve(computerData.model)
                .normalize();

        AOLInstall.createDir(targetDriverDir.toString());

        try {
            int code = runExport(targetDriverDir);
        } catch (IOException e) {
            System.err.println("IO error: " + e.getMessage());
            e.printStackTrace();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Interrupted: " + e.getMessage());
            e.printStackTrace();
        }


    }

    public static int runPnputil(Path sourceDriverDir) throws IOException, InterruptedException {
        String infPattern ="\"" + sourceDriverDir.toString() + ("\\*.inf\"");
        String command = "pnputil /add-driver " + infPattern +(" /subdirs /install");
        System.out.println(command);

        ProcessBuilder pb = new ProcessBuilder(
                "pnputil",
                "/add-driver",
                infPattern,
                "/subdirs",
                "/install"
        );

        pb.inheritIO();
        Process p = pb.start();
        return p.waitFor();
    }
    public static int runExport(Path targetDriverDir) throws IOException, InterruptedException  {
        String infPattern ="*";
        String command = "pnputil /export-driver " + infPattern + targetDriverDir.toString();
        System.out.println(command);

        ProcessBuilder pb = new ProcessBuilder(
                "pnputil",
                "/export-driver",
                infPattern,
                targetDriverDir.toString()
        );

        pb.inheritIO();
        Process p = pb.start();
        return p.waitFor();
        }
    }
