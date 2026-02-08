package AOLTools;

import java.io.IOException;

public class renamePC {


    public static int renameComputer()
            throws IOException, InterruptedException {

        ComputerInfo.serialNumber pcSerial = ComputerInfo.getComputerSN();
        String newName = "OPL" + pcSerial.serialnumber;


        String command =
                "Rename-Computer -NewName '" + newName + "' -Force";

        ProcessBuilder pb = new ProcessBuilder(
                "powershell.exe",
                "-NoProfile",
                "-ExecutionPolicy", "Bypass",
                "-Command", command
        );

        pb.inheritIO();

        Process p = pb.start();
        return p.waitFor();
    }
}
