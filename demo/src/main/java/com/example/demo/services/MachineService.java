package com.example.demo.services;

import com.example.demo.model.Machine;
import com.example.demo.model.Status;
import com.example.demo.model.User;
import com.example.demo.repositories.MachineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class MachineService {

    MachineRepository machineRepository;
    @Autowired
    public MachineService(MachineRepository machineRepository){
        this.machineRepository = machineRepository;
    }
    public Machine findById(Long id) { return this.machineRepository.findByMachineId(id); }

    public Machine create(User user, String name) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        Machine machine = new Machine();
        machine.setName(name);
        machine.setUser(user);
        machine.setStatus(Status.STOPPED);
        machine.setActive(true);
        machine.setDateCreated(sdf.parse(sdf.format(new Date())));
        return this.machineRepository.save(machine);
    }

    public void destroy(Long id){
        Machine machine = machineRepository.findByMachineId(id);
        machine.setActive(false);
        machineRepository.save(machine);
    }

    public List<Machine> search(User user, String name, List<String> status, Date dateFrom, Date dateTo){
        List<Machine> machines = machineRepository.findMachinesByUser(user);
        if(!name.equals(null)){
           machines.retainAll(machineRepository.findMachinesByUserAndNameContainingIgnoreCase(user,name));
        }

        if(!status.equals(null) && !status.isEmpty()){
            for(String s: status){
                machines.retainAll(machineRepository.findMachinesByUserAndStatus(user, Status.valueOf(s)));
            }
        }

        if(!dateFrom.equals(null)){
            machines.retainAll(machineRepository.findMachinesByUserAndDateCreatedAfter(user,dateFrom));
        }

        if(!dateTo.equals(null)){
            machines.retainAll(machineRepository.findMachinesByUserAndDateCreatedBefore(user, dateTo));
        }

        return machines;
    }
}
