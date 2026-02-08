
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;
import AOLTools.*;

public class Main {

    public static void main(String[] args) throws IOException {
        Scanner sc = new Scanner(System.in);
        AOLInstall aolcheck = new AOLInstall();

        aolcheck.checkAOLVersion(aolcheck.getAOLVersion());
        driverUpdate.checkDrivers();

        System.out.println("");
        System.out.println("Choose option:");
        System.out.println("1) Full -  AOL + drivers + change netbios +  BIOS");
        System.out.println("2) Only AOL");
        System.out.println("3) Drivers update(Auto)");
        System.out.println("4) Drivers update(Manual)");
        System.out.println("5) Drivers Export(Auto)");
        System.out.println("6) Drivers export(Manual)");
        System.out.println("7) Change netbios");
        System.out.println("8) BIOS Update");
        System.out.print("> ");

        String choice = sc.nextLine().trim();

        try {
            switch (choice) {
                case "1" -> {


                    // Drivers

                    System.out.println("Start driver update");
                    driverUpdate.updateDrivers();
                    System.out.println("Stop driver update");


                    // AOL
                    System.out.print("Start AOL");
                    AOLInstall aol = new AOLInstall();
                    aol.setAOLVersion(aol.getAOLVersion());
                    aol.startInstallator();
                    System.out.print("AOL finished");
                    System.out.println("");

                    //Change netbios
                    renamePC.renameComputer();

                    //BIOS update
                    updateBios.BIOSUpdate();

                }

                //only aol
                case "2" -> {
                    AOLInstall aol = new AOLInstall();
                    aol.setAOLVersion(aol.getAOLVersion());
                    aol.startInstallator();

                }

                // 3) update Auto
                case "3" -> {
                    driverUpdate.updateDrivers();
                }
                //update manual
                case "4" ->{
                    System.out.println("");
                    System.out.println("Provide the FULL path from which the drivers should be taken");

                    Scanner edm = new Scanner(System.in);
                    Path man = Paths.get(edm.nextLine());

                    driverUpdate.updateDriversManual(man);
                }
                //export  auto
                case "5" ->{
                    driverUpdate.exportDriversAuto();
                }
                //export manual
                case "6" ->{
                    System.out.println("");
                    System.out.println("Provide the FULL path where drivers should be exported");

                    Scanner edm = new Scanner(System.in);
                    Path man = Paths.get(edm.nextLine());
                    driverUpdate.exportDriversManual(man);
                }
                //Rename
                case "7" ->{
                    renamePC.renameComputer();
                }
                //BiosUpdate
                case "8" ->{
                    updateBios.BIOSUpdate();
                }
                default -> System.out.println("Incorrect choice");
            }
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
}
