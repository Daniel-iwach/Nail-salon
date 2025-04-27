package com.chamisnails.nailsalon.config;

import com.chamisnails.nailsalon.persistence.model.*;
import com.chamisnails.nailsalon.persistence.repository.IAppointmentRepository;
import com.chamisnails.nailsalon.persistence.repository.IDateRepository;
import com.chamisnails.nailsalon.persistence.repository.IServiceRepository;
import com.chamisnails.nailsalon.persistence.repository.IUserRepository;
import com.chamisnails.nailsalon.services.interfaces.IDateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.YearMonth;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Configuration
public class DataLoader {

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private IDateService dateService;

    @Autowired
    IDateRepository dateRepository;

    @Autowired
    private IServiceRepository serviceRepository;

    @Autowired
    private IAppointmentRepository appointmentRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Bean
    public ApplicationRunner initializer() {
        return args -> {
            if (userRepository.count() == 0) {
                UserDocument admin = new UserDocument();
                admin.setUsername("admin");
                admin.setFirstName("admin");
                admin.setLastName("user");
                admin.setEmail("admin@example.com");
                admin.setPassword(passwordEncoder.encode("admin123"));
                admin.setRol(Set.of(ERol.ADMIN));
                admin.setRegistrationDate(new Date());

                userRepository.save(admin);


                UserDocument user1 = new UserDocument();
                user1.setUsername("user1");
                user1.setFirstName("user");
                user1.setLastName("one");
                user1.setEmail("user1@example.com");
                user1.setPassword(passwordEncoder.encode("user123"));
                user1.setRol(Set.of(ERol.USER));
                user1.setRegistrationDate(new Date());

                userRepository.save(user1);


                UserDocument user2 = new UserDocument();
                user2.setUsername("user2");
                user2.setFirstName("user");
                user2.setLastName("two");
                user2.setEmail("user2@example.com");
                user2.setPassword(passwordEncoder.encode("user123"));
                user2.setRol(Set.of(ERol.ADMIN));
                user2.setRegistrationDate(new Date());

                userRepository.save(user2);
            }
            if (serviceRepository.count()==0){
                ServicesDocument service1=new ServicesDocument(null,"Manicura tradicional",11000);
                ServicesDocument service2=new ServicesDocument(null,"Manicura semipermanente",16000);
                ServicesDocument service3=new ServicesDocument(null,"Manicura semipermanente French",22500);
                ServicesDocument service4=new ServicesDocument(null,"Kapping con Esmaltado Semipermanente",24000);
                ServicesDocument service5=new ServicesDocument(null,"Uñas Esculpidas",33000);

                serviceRepository.save(service1);
                serviceRepository.save(service2);
                serviceRepository.save(service3);
                serviceRepository.save(service4);
                serviceRepository.save(service5);
            }
            if (dateRepository.count()==0){
                dateService.loadDatesOfTheMonth(YearMonth.of(2025,5));
            }
            if (appointmentRepository.count()==0){
                //APPOINTMENTS USER 1
                String idUser1=userRepository.findByUsername("user1").get().getId();
                String idDateUser1=dateRepository.findAll().get(10).getId();
                String idServiceUser1=serviceRepository.findAll().get(2).getId();
                AppointmentDocument app1=new AppointmentDocument(null,idUser1,idDateUser1, EState.BUSY, List.of(idServiceUser1));
                dateService.changeStatusById(idDateUser1,EState.BUSY);
                appointmentRepository.save(app1);

                idDateUser1=dateRepository.findAll().get(20).getId();
                idServiceUser1=serviceRepository.findAll().get(3).getId();
                AppointmentDocument app2=new AppointmentDocument(null,idUser1,idDateUser1, EState.BUSY, List.of(idServiceUser1));
                dateService.changeStatusById(idDateUser1,EState.BUSY);
                appointmentRepository.save(app2);

                //APPOINTMENTS USER 2
                String idUser2=userRepository.findByUsername("user2").get().getId();
                String idDateUser2=dateRepository.findAll().get(11).getId();
                String idServiceUser2=serviceRepository.findAll().get(4).getId();
                AppointmentDocument app3=new AppointmentDocument(null,idUser2,idDateUser2, EState.BUSY, List.of(idServiceUser2));
                dateService.changeStatusById(idDateUser2,EState.BUSY);
                appointmentRepository.save(app3);

            }
        };
    }
}
