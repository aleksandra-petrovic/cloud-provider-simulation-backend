package com.example.demo.services;

import com.example.demo.model.ErrorMsg;
import com.example.demo.model.Machine;
import com.example.demo.model.Status;
import com.example.demo.model.User;
import com.example.demo.repositories.MachineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.OptimisticLockingFailureException;
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

    private ErrorMsgService errorMsgService;
    @Autowired
    public MachineService(MachineRepository machineRepository, TaskScheduler taskScheduler, ErrorMsgService errorMsgService){
        this.machineRepository = machineRepository;
        this.taskScheduler = taskScheduler;
        this.errorMsgService = errorMsgService;
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
        if(name != null){
           machines.retainAll(machineRepository.findMachinesByUserAndNameContainingIgnoreCase(user,name));
        }

        if(status != null && !status.isEmpty()){
            for(String s: status){
                machines.retainAll(machineRepository.findMachinesByUserAndStatus(user, Status.valueOf(s)));
            }
        }

        if(dateFrom != null){
            machines.retainAll(machineRepository.findMachinesByUserAndDateCreatedAfter(user,dateFrom));
        }

        if(dateTo != null){
            machines.retainAll(machineRepository.findMachinesByUserAndDateCreatedBefore(user, dateTo));
        }

        return machines;
    }

    public List<ErrorMsg> getErrors(User user){
        List<Machine> machines = machineRepository.findMachinesByUser(user);
        return this.errorMsgService.getAll(machines);
    }

    @Async
    public void update(Long id, String operation) throws ParseException {
        Machine machine = machineRepository.findByMachineId(id);

        if(operation.equals("start")){
            if(!this.findById(id).getStatus().equals(Status.RUNNING)) {
                try {
                    Thread.sleep(10000);
                    machine.setStatus(Status.RUNNING);
                    this.machineRepository.save(machine);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } catch (OptimisticLockingFailureException exception) {
                    //exception.printStackTrace();
                    System.out.println("konflikt verzija");
                    this.errorMsgService.log(id, operation, "Conflict of versions. Operation " + operation +
                            " couldn't finish because antoher operation interrupted. ");
                }
            }else{
                this.errorMsgService.log(id, operation, "You can't " + operation + " machine if it's running.");
            }
        }

        if(operation.equals("stop")){
            if(!this.findById(id).getStatus().equals(Status.STOPPED)) {
                try {
                    Thread.sleep(10000);
                    machine.setStatus(Status.STOPPED);
                    this.machineRepository.save(machine);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } catch (OptimisticLockingFailureException exception) {
                    //exception.printStackTrace();
                    System.out.println("konflikt verzija");
                    this.errorMsgService.log(id, operation, "Conflict of versions. Operation " + operation +
                            " couldn't finish because antoher operation interrupted. ");
                }
            }else{
                this.errorMsgService.log(id, operation, "You can't " + operation + " machine if it's stopped.");
            }
        }

        if(operation.equals("restart")) {
            if(!this.findById(id).getStatus().equals(Status.STOPPED)) {
                try {
                    Thread.sleep(5000);
                    machine.setStatus(Status.STOPPED);
                    this.machineRepository.save(machine);
                    Thread.sleep(5000);
                    machine = machineRepository.findByMachineId(id);
                    machine.setStatus(Status.RUNNING);
                    this.machineRepository.save(machine);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } catch (OptimisticLockingFailureException exception) {
                    //exception.printStackTrace();
                    System.out.println("konflikt verzija");
                    this.errorMsgService.log(id, operation, "Conflict of versions. Operation " + operation +
                                                                " couldn't finish because antoher operation interrupted. ");
                }
            }else{
                this.errorMsgService.log(id, operation, "You can't " + operation + " machine if it's stopped.");
            }
        }
    }

    @Async
    public void scheduledUpdate(Long id, String operation, Date date){
        this.taskScheduler.schedule(() -> {
            System.out.println("updating machine status...");
            try {
                this.update(id,operation);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }, date);
    }

//    @Async
//    public void start(Long id){
//        Machine machine = machineRepository.findByMachineId(id);
//
//        try {
//            Thread.sleep(10000);
//
//            machine.setStatus(Status.RUNNING);
//            this.machineRepository.save(machine);
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        } catch (OptimisticLockingFailureException exception){
//            //exception.printStackTrace();
//            System.out.println("konflikt verzija");
//        }
//    }
//
//    @Async
//    public void stop(Long id){
//        Machine machine = machineRepository.findByMachineId(id);
//
//        try {
//            Thread.sleep(10000);
//
//            machine.setStatus(Status.STOPPED);
//            this.machineRepository.save(machine);
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }catch (OptimisticLockingFailureException exception){
//            //exception.printStackTrace();
//            System.out.println("konflikt verzija");
//        }
//    }
//
//    @Async
//    public void restart(Long id){
//        Machine machine = machineRepository.findByMachineId(id);
//
//        try {
//            Thread.sleep(5000);
//            machine.setStatus(Status.STOPPED);
//            this.machineRepository.save(machine);
//            Thread.sleep(5000);
//            machine = machineRepository.findByMachineId(id);
//            machine.setStatus(Status.RUNNING);
//            this.machineRepository.save(machine);
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }catch (OptimisticLockingFailureException exception){
//            //exception.printStackTrace();
//            System.out.println("konflikt verzija");
//        }
//    }
//
//    @Async
//    public void scheduledStart(Long id, Date date){
//        this.taskScheduler.schedule(() -> {
//            System.out.println("starting machine...");
//            this.start(id);
//        }, date);
//    }
//
//    @Async
//    public void scheduledStop(Long id, Date date){
//        this.taskScheduler.schedule(() -> {
//            System.out.println("stopping machine...");
//            this.stop(id);
//        }, date);
//    }
//
//    @Async
//    public void scheduledRestart(Long id, Date date){
//        this.taskScheduler.schedule(() -> {
//            System.out.println("restarting machine...");
//            this.restart(id);
//        }, date);
//    }
}
