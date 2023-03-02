package com.henryshao.springterra.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class TerraformExecutionService {

    public TerraformExecutionService() {
    }

    public void runTerraform() throws IOException, InterruptedException {
        System.out.println("Provisioning");
        // Set the directory where the Terraform configuration files are located
        String terraformDir = "./config";

        // Initialize the Terraform configuration
        ProcessBuilder initProcessBuilder = new ProcessBuilder("terraform", "init")
                .directory(new File(terraformDir));
        Process initProcess = initProcessBuilder.start();
        printProcessOutput(initProcess);

        // Plan the Terraform configuration and save the plan to a file
        ProcessBuilder planProcessBuilder = new ProcessBuilder("terraform", "plan", "-out=terraform.out")
                .directory(new File(terraformDir));
        Process planProcess = planProcessBuilder.start();
        printProcessOutput(planProcess);

        // Apply the Terraform configuration from the saved plan
        ProcessBuilder applyProcessBuilder = new ProcessBuilder("terraform", "apply", "terraform.out")
                .directory(new File(terraformDir));
        Process applyProcess = applyProcessBuilder.start();
        printProcessOutput(applyProcess);
    }

    public void destroyTerraform() throws IOException, InterruptedException {
        System.out.println("Destroying");

        String terraformDir = "./config";

        ProcessBuilder applyProcessBuilder = new ProcessBuilder("terraform", "apply", "terraform.out")
                .directory(new File(terraformDir));
        Process applyProcess = applyProcessBuilder.start();
        printProcessOutput(applyProcess);

        ProcessBuilder destroyProcessBuilder = new ProcessBuilder("terraform", "destroy", "-auto-approve")
                .directory(new File(terraformDir));
        Process destroyProcess = destroyProcessBuilder.start();
        printProcessOutput(destroyProcess);
    }

    private static void printProcessOutput(Process process) throws IOException, InterruptedException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
        }
        process.waitFor();
    }
}