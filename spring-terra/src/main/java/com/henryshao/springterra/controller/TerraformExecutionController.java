package com.henryshao.springterra.controller;

import com.henryshao.springterra.service.TerraformExecutionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@Slf4j
@RestController
public class TerraformExecutionController {

    private final TerraformExecutionService terraformExecutionService = new TerraformExecutionService();

    @GetMapping("terraform/provision")
    public void provisionAWS(){
        log.info("Provisioning from AWS");
        try {
            terraformExecutionService.runTerraform();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("terraform/destroy")
    public void destroyAWS(){
        log.info("Destroying resources");
        try {
            terraformExecutionService.destroyTerraform();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
