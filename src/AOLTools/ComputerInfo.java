package AOLTools;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class ComputerInfo {

    public static class Info {
        public final String manufacturer;
        public final String model;

        public Info(String manufacturer, String model) {
            this.manufacturer = manufacturer;
            this.model = model;
        }
    }
    public static class serialNumber{
        public final String serialnumber;

        public serialNumber(String serialnumber){
            this.serialnumber = serialnumber;
        }
    }

    public static  serialNumber getComputerSN(){
        try{
            String pcserialnumber = execPs(
                    "Get-CimInstance Win32_BIOS | Select-Object -ExpandProperty SerialNumber"
            );
            return new serialNumber(pcserialnumber);
        }
        catch (Exception e){
            System.out.println("serial number not found");
            return new serialNumber("null");
        }

    }

    public static Info getComputerInfo() {
        try {
            String manufacturer = execPs(
                    "Get-CimInstance Win32_ComputerSystem | " +
                            "Select-Object -ExpandProperty Manufacturer"
            );


            String model;

            if (manufacturer != null && manufacturer.toLowerCase().contains("lenovo")) {
                model = execPs(
                        "Get-CimInstance Win32_ComputerSystemProduct | " +
                                "Select-Object -ExpandProperty Version"
                );
            } else {
                model = execPs(
                        "Get-CimInstance Win32_ComputerSystem | " +
                                "Select-Object -ExpandProperty Model"
                );
            }

            return new Info(
                    normalize(manufacturer),
                    normalize(model)
            );

        } catch (Exception e) {
            return new Info("[brak danych]", "[brak danych]");
        }
    }

    private static String execPs(String command) throws Exception {
        Process process = new ProcessBuilder(
                "powershell",
                "-NoProfile",
                "-Command",
                command
        ).start();

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8))) {
            return reader.readLine();
        }
    }
    private static String normalize(String s) {
        if (s == null) return "[brak danych]";
        return s.trim();
    }
}
