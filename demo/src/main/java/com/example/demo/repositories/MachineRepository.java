package com.example.demo.repositories;

import com.example.demo.model.Machine;
import com.example.demo.model.Status;
import com.example.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface MachineRepository extends JpaRepository<Machine,Long> {
    public Machine findByMachineId(Long id);

    public List<Machine> findMachinesByUser(User user);

    public List<Machine> findMachinesByUserAndNameContainingIgnoreCase(User user, String name);

    public List<Machine> findMachinesByUserAndStatus(User user, Status status);

    public List<Machine> findMachinesByUserAndDateCreatedAfter(User user,Date date);
    public List<Machine> findMachinesByUserAndDateCreatedBefore(User user, Date date);
}
