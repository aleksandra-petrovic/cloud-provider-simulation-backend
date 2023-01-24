package com.example.demo.services;

import com.example.demo.model.Machine;
import com.example.demo.model.Status;
import com.example.demo.model.User;
import com.example.demo.repositories.MachineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class MachineService {

    private MachineRepository machineRepository;
    private TaskScheduler taskScheduler;
    @Autowired
    public MachineService(MachineRepository machineRepository, TaskScheduler taskScheduler){
        this.machineRepository = machineRepository;
        this.taskScheduler = taskScheduler;
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
    @Async
    public void start(Long id){
        Machine machine = machineRepository.findByMachineId(id);

        try {
            Thread.sleep(10000);

            machine.setStatus(Status.RUNNING);
            this.machineRepository.save(machine);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Async
    public void stop(Long id){
        Machine machine = machineRepository.findByMachineId(id);

        try {
            Thread.sleep(10000);

            machine.setStatus(Status.STOPPED);
            this.machineRepository.save(machine);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Async
    public void restart(Long id){
        Machine machine = machineRepository.findByMachineId(id);

        try {
            Thread.sleep(5000);
            machine.setStatus(Status.STOPPED);
            this.machineRepository.save(machine);
            Thread.sleep(5000);
            machine.setStatus(Status.RUNNING);
            this.machineRepository.save(machine);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public String parse(Date date){
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String strDate = dateFormat.format(date);

        String month = strDate.substring(5,7);
        String day = strDate.substring(8,10);
        String hour = strDate.substring(11,13);
        String minute = strDate.substring(14,16);
        String second = strDate.substring(17,19);

        System.out.println("full date " + strDate);
        System.out.println("second " + second + " minute " + minute + " hour " + hour + " day " + day + " month " + month);

        return second + " " + minute + " " + hour + " " + day + " " + month + " *" ;
    }

    @Async
    public void scheduledStart(Long id, Date date){
        String expression = parse(date);

        CronTrigger cronTrigger = new CronTrigger(expression); //sec min hr day/month month day/week
        this.taskScheduler.schedule(() -> {
            System.out.println("starting machine...");
            this.start(id);
        }, cronTrigger);
    }

    @Async
    public void scheduledStop(Long id, Date date){
        String expression = parse(date);

        CronTrigger cronTrigger = new CronTrigger(expression); //sec min hr day/month month day/week
        this.taskScheduler.schedule(() -> {
            System.out.println("stopping machine...");
            this.stop(id);
        }, cronTrigger);
    }

    @Async
    public void scheduledRestart(Long id, Date date){
        String expression = parse(date);

        CronTrigger cronTrigger = new CronTrigger(expression); //sec min hr day/month month day/week
        this.taskScheduler.schedule(() -> {
            System.out.println("restarting machine...");
            this.restart(id);
        }, cronTrigger);
    }
}
