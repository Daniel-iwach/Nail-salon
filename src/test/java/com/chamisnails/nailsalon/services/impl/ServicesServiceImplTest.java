package com.chamisnails.nailsalon.services.impl;

import com.chamisnails.nailsalon.exceptions.ResourceNotFoundException;
import com.chamisnails.nailsalon.persistence.model.ServicesDocument;
import com.chamisnails.nailsalon.persistence.repository.IServiceRepository;
import com.chamisnails.nailsalon.presentation.dto.ServicesDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ServicesServiceImplTest {

    @InjectMocks
    private ServicesServiceImpl servicesService;

    @Mock
    private IServiceRepository serviceRepository;

    @Mock
    private ModelMapper modelMapper;

    @Test
    void getSevices_ShouldReturnMappedServicesList() {
        String serviceId = "s1";
        ServicesDocument document = new ServicesDocument();
        ServicesDTO dto = new ServicesDTO();

        when(serviceRepository.findById(serviceId)).thenReturn(Optional.of(document));
        when(modelMapper.map(document, ServicesDTO.class)).thenReturn(dto);

        List<ServicesDTO> result = servicesService.getSevices(List.of(serviceId));

        assertEquals(1, result.size());
        assertSame(dto, result.get(0));
    }

    @Test
    void getSevices_ShouldThrowExceptionWhenServiceNotFound() {
        String serviceId = "notFound";

        when(serviceRepository.findById(serviceId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> servicesService.getSevices(List.of(serviceId)));
    }

    @Test
    void getAllServices_ShouldReturnMappedList() {
        ServicesDocument doc1 = new ServicesDocument();
        ServicesDocument doc2 = new ServicesDocument();
        ServicesDTO dto1 = new ServicesDTO();
        ServicesDTO dto2 = new ServicesDTO();

        when(serviceRepository.findAll()).thenReturn(List.of(doc1, doc2));
        when(modelMapper.map(doc1, ServicesDTO.class)).thenReturn(dto1);
        when(modelMapper.map(doc2, ServicesDTO.class)).thenReturn(dto2);

        List<ServicesDTO> result = servicesService.getAllServices();

        assertEquals(2, result.size());
        assertTrue(result.containsAll(List.of(dto1, dto2)));
    }
}
