package com.henryshao.springterra.controller;

import com.henryshao.springterra.service.TerraformExecutionService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class TerraformExecutionController {

    private final TerraformExecutionService terraformExecutionService = new TerraformExecutionService();

    @GetMapping("terraform/provision")
    public void provisionAWS(){
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
        try {
            terraformExecutionService.destroyTerraform();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
