package com.chamisnails.nailsalon.services.impl;

import com.chamisnails.nailsalon.exceptions.ResourceNotFoundException;
import com.chamisnails.nailsalon.persistence.model.ServicesDocument;
import com.chamisnails.nailsalon.persistence.repository.IServiceRepository;
import com.chamisnails.nailsalon.presentation.dto.ServicesDTO;
import com.chamisnails.nailsalon.services.interfaces.IServicesService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Service class for managing services in the nailsalon.
 * Provides methods to retrieve services either by a list of IDs or all services available.
 */
@Service
public class ServicesServiceImpl implements IServicesService {

    @Autowired
    private IServiceRepository serviceRepository;
    @Autowired
    private ModelMapper modelMapper;

    /**
     * Retrieves a list of services by their IDs.
     *
     * @param services a list of service IDs to fetch
     * @return a list of ServicesDTOs corresponding to the provided IDs
     * @throws ResourceNotFoundException if a service with the provided ID is not found
     */
    @Override
    public List<ServicesDTO> getSevices(List<String> services) {
        List<ServicesDTO> list = new ArrayList<>();
        for (String id : services) {
            // Retrieves a service by its ID
            ServicesDocument servicesDocument = serviceRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Service with ID '" + id + "' not found."));
            // Maps the entity to a DTO and adds it to the list
            ServicesDTO servicesDTO = modelMapper.map(servicesDocument, ServicesDTO.class);
            list.add(servicesDTO);
        }
        return list;
    }

    /**
     * Retrieves all available services.
     *
     * @return a list of all services as ServicesDTOs
     */
    @Override
    public List<ServicesDTO> getAllServices() {
        // Retrieves all services and maps them to ServicesDTO
        return serviceRepository.findAll().stream()
                .map(servicesDocument -> modelMapper.map(servicesDocument, ServicesDTO.class))
                .toList();
    }
}
