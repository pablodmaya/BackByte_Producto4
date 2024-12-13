package com.backbyte.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SucursalesController {

    @GetMapping("/madrid")
    public String madrid() {
        return "city/madrid";
    }

    @GetMapping("/user/madrid")
    public String userMadrid() {
        return "user/city/madridUser";
    }

    @GetMapping("/admin/madrid")
    public String adminMadrid() {
        return "admin/city/madridAdmin";
    }


    // Otros mapeos para diferentes sucursales
    @GetMapping("/barcelona")
    public String barcelona() {
        return "city/barcelona";
    }

    @GetMapping("/user/barcelona")
    public String userBarcelona() {
        return "user/city/barcelonaUser";
    }

    @GetMapping("/admin/barcelona")
    public String adminBarcelona() {
        return "admin/city/barcelonaAdmin";
    }


    // Otros mapeos para diferentes sucursales
    @GetMapping("/bilbao")
    public String bilbao() {
        return "city/bilbao";
    }

    @GetMapping("/user/bilbao")
    public String userBilbao() {
        return "user/city/bilbaoUser";
    }

    @GetMapping("/admin/bilbao")
    public String adminBilbao() {
        return "admin/city/bilbaoAdmin";
    }


    // Otros mapeos para diferentes sucursales
    @GetMapping("/sevilla")
    public String sevilla() {
        return "city/sevilla";
    }

    @GetMapping("/user/sevilla")
    public String userSevilla() {
        return "user/city/sevillaUser";
    }

    @GetMapping("/admin/sevilla")
    public String adminSevila() {
        return "admin/city/sevillaAdmin";
    }


    // Otros mapeos para diferentes sucursales
    @GetMapping("/valencia")
    public String valencia() {
        return "city/valencia";
    }

    @GetMapping("/user/valencia")
    public String userValencia() {
        return "user/city/valenciaUser";
    }

    @GetMapping("/admin/valencia")
    public String adminValencia() {
        return "admin/city/valenciaAdmin";
    }



}