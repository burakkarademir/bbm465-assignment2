package com.company;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Process {
    private Argument argument = null;
    private Registry registry = null;

    public Process(String[] args) {
        Argument argument = new Argument(args);
        argument.fillMonitoredPath(args);
        argument.fillRegistryPath(args);
        argument.fillLogPath(args);
        argument.fillHashArgument(args);
        argument.fillKeys(args);
        argument.fillIntervalTime(args);
        this.argument = argument;
    }


    public void run() {
        Argument argument = this.argument;
        if (argument.getType().equals("stop")) {
            System.exit(0);
        } else if (argument.getType().equals("start")) {
            //Start process add sh
            File file = new File(argument.getRegisteryFile());
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            this.registry = new Registry(argument);
            registry.createRegistryFile(new File(argument.getMonitoredPath()), argument);
            Sign sign = new Sign(argument);
        } else if (argument.getType().equals("cronJob")) {
            //run cron job
        }

    }

    public Argument getArgument() {
        return argument;
    }

    public void setArgument(Argument argument) {
        this.argument = argument;
    }

    private void addSystemTask(Argument argument) {
        try {

            String path = Main.class.getProtectionDomain().getCodeSource().getLocation().getPath();
            String runCron = "*/"+argument.getIntervalTime()+" * * * * cd " + path + ";integrity -p " + argument.getMonitoredPath() + " -r " + argument.getRegisteryFile() +
                    " -l " + argument.getLogFile() + " -h " + argument.getHashFunction() + " -k " + argument.getPublicKey();
            byte[] ar = runCron.getBytes();
            Files.write(Paths.get("run.sh"), ar);
            String runPath = path + "/run.sh";
            String[] shExec = {"sh", "crontab -e;", runPath};
            Runtime.getRuntime().exec(shExec);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void removeSystemTask(Argument argument){
        try {
            String path = Main.class.getProtectionDomain().getCodeSource().getLocation().getPath();
            String killCron = "crontab -r";
            byte[] ar = killCron.getBytes();
            Files.write(Paths.get("stop.sh"), ar);
            String stopPath = path + "/stop.sh";
            String[] shExec = {"sh", stopPath};
            Runtime.getRuntime().exec(shExec);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void verifyFile(Argument argument)throws InterruptedException {
        Verify verify = new Verify();
        verify.verify(argument);
    }
}
