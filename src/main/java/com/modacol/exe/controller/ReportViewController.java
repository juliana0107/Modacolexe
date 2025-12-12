package com.modacol.exe.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ReportViewController {

    @GetMapping("/reportes-view")
    public String mostrarReportes() {
        return "reportes/index";
    }
}