package com.chamisnails.nailsalon.services.interfaces;

import com.chamisnails.nailsalon.presentation.dto.ServicesDTO;

import java.util.List;

public interface IServicesService {
    List<ServicesDTO>getSevices(List<String> services);
    List<ServicesDTO>getAllServices();
}
