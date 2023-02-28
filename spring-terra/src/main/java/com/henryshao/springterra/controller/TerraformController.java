package com.henryshao.springterra.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

@RestController
public class TerraformController {

    @GetMapping("terraform/provision")
    public void provisionAWS(){
        try {
            runTerraform();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("terraform/destroy")
    public void destroyAWS(){
        try {
            destroyTerraform();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void runTerraform() throws IOException, InterruptedException {
        System.out.println("Provisioning");
        // Set the directory where the Terraform configuration files are located
        String terraformDir = "C:/Users/Henry Work/Documents/Development/tf-vpc";

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

        String terraformDir = "C:/Users/Henry Work/Documents/Development/tf-vpc";

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
