package com.henryshao.springterra.service;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

@Slf4j
public class TerraformExecutionService {

    public TerraformExecutionService() {
    }

    public void runTerraform() throws IOException, InterruptedException {
        log.info("Provisioning resources based on configuration folder");
        // Set the directory where the Terraform configuration files are located
        String terraformDir = "./config";

        // Initialize the Terraform configuration
        log.info("  terraform init");
        ProcessBuilder initProcessBuilder = new ProcessBuilder("terraform", "init")
                .directory(new File(terraformDir));
        Process initProcess = initProcessBuilder.start();
        printProcessOutput(initProcess);

        // Plan the Terraform configuration and save the plan to a file
        log.info("  terraform plan -out=terraform.out");
        ProcessBuilder planProcessBuilder = new ProcessBuilder("terraform", "plan", "-out=terraform.out")
                .directory(new File(terraformDir));
        Process planProcess = planProcessBuilder.start();
        printProcessOutput(planProcess);

        // Apply the Terraform configuration from the saved plan
        log.info("  terraform apply terraform.out");
        ProcessBuilder applyProcessBuilder = new ProcessBuilder("terraform", "apply", "terraform.out")
                .directory(new File(terraformDir));
        Process applyProcess = applyProcessBuilder.start();
        printProcessOutput(applyProcess);
    }

    public void destroyTerraform() throws IOException, InterruptedException {
        log.info("Destroying resources based on last application");

        String terraformDir = "./config";

        log.info("  terraform apply terraform.out");
        ProcessBuilder applyProcessBuilder = new ProcessBuilder("terraform", "apply", "terraform.out")
                .directory(new File(terraformDir));
        Process applyProcess = applyProcessBuilder.start();
        printProcessOutput(applyProcess);
        log.info("  terraform destroy -auto-approve");
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