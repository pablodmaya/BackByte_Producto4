package com.backbyte.service;

import com.backbyte.models.Alquiler;
import com.backbyte.repository.AlquilerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AlquilerService {

    @Autowired
    private AlquilerRepository alquilerRepository;

    public List<Alquiler> getAlquiler() {
        return alquilerRepository.findAll();
    }
}
