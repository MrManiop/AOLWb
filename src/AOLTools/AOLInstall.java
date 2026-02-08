package AOLTools;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class AOLInstall
{
    //current version of AOL
    String aOLVersion = "";

    //Set path to cscript and installer
    private final Path powershell;
    private final Path installerPS;

    public AOLInstall () throws IOException
    {
        String systemRoot = System.getenv("SystemRoot");
        if (systemRoot == null) systemRoot = "C:\\Windows";

        Path sysnativeCmd = Paths.get(systemRoot, "Sysnative", "cmd.exe");
        String sysDir = Files.exists(sysnativeCmd)
                ? Paths.get(systemRoot, "Sysnative").toString()
                : Paths.get(systemRoot, "System32").toString();

        //creating directories

        createDir("C:\\LOGS");
        createDir("C:\\Signatures");
        createDir(Paths.get(systemRoot, "TPSA").toString());

        //path to powershell and installer
        this.powershell = Paths.get(
                sysDir,
                "WindowsPowerShell",
                "v1.0",
                "powershell.exe"
        );

        Path baseDir = Paths.get(System.getProperty("user.dir"));
        this.installerPS = baseDir.resolve("source").resolve("installer.ps1");
    }

    public static void createDir (String path) throws IOException
    {
        Files.createDirectories(Paths.get(path));
    }

    public String getAOLVersion() {
        return aOLVersion;
    }

    public void checkAOLVersion(String expectedAOLVersion) throws IOException {
        Path AOLtxt = Path.of("C:\\LOGS\\AOLVersion.txt");

        // Check if file exist
        if (!Files.exists(AOLtxt)) {
            System.out.println("AOL is not found");
            return;
        }

        //reading txt
        String savedVersion = Files.readString(AOLtxt, StandardCharsets.UTF_8).trim();

        // Incorrect version
        if (!savedVersion.equalsIgnoreCase(expectedAOLVersion)) {
            System.out.println(
                    "AOL is outdated\n" +
                            "Installed version: " + savedVersion + "\n" +
                            "Current version:      " + expectedAOLVersion
            );
            return;
        }

        // Correct version
        System.out.println(
                "AOL is current\n" +
                        "Version: " + savedVersion
        );
    }

    public void setAOLVersion(String aOLVersion) throws IOException {

        if (aOLVersion == null || aOLVersion.isBlank()) {
            throw new IllegalArgumentException("aOLVersion cannot be null or blank");
        }

        Path dir = Path.of("C:\\LOGS");
        Path AOLtxt = dir.resolve("AOLVersion.txt");

        // create C:\LOGS
        Files.createDirectories(dir);

        // create txt file
        Files.writeString(
                AOLtxt,
                aOLVersion,
                StandardCharsets.UTF_8,
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING
        );
    }

    // installing AOL
    public void startInstallator() throws IOException, InterruptedException {

        Path projectRoot = Paths.get(System.getProperty("user.dir"));
        Path installerPS = projectRoot.resolve("source").resolve("installer.ps1");

        if (!Files.exists(installerPS)) {
            throw new IOException("Installer not found: " + installerPS);
        }

        ProcessBuilder pb = new ProcessBuilder(
                powershell.toString(),          // powershell.exe
                "-NoProfile",
                "-ExecutionPolicy", "Bypass",
                "-File",
                installerPS.toString()           // installer.ps1
        );

        pb.inheritIO();

        Process process = pb.start();
        int exitCode = process.waitFor();



    }
}
