package com.example.demo.bootstrap;

import com.example.demo.model.*;
import com.example.demo.repositories.ErrorMsgRepository;
import com.example.demo.repositories.MachineRepository;
import com.example.demo.repositories.PermissionRepository;
import com.example.demo.repositories.UserRepository;
import com.example.demo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

@Component
public class BootstrapData implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PermissionRepository permissionRepository;
    private final MachineRepository machineRepository;
    private final ErrorMsgRepository errorMsgRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public BootstrapData(UserRepository userRepository,PermissionRepository permissionRepository, MachineRepository machineRepository, ErrorMsgRepository errorMsgRepository,PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.permissionRepository = permissionRepository;
        this.machineRepository = machineRepository;
        this.errorMsgRepository = errorMsgRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {

        System.out.println("Loading Data...");

        String[] FIRST_NAME_LIST = {"John-James", "Justine", "Ahsan", "Leja", "Jad", "Vernon", "Cara", "Eddison", "Eira", "Emily"};
        String[] LAST_NAME_LIST = {"Booker", "Summers", "Reyes", "Rahman", "Crane", "Cairns", "Hebert", "Bradshaw", "Shannon", "Phillips"};
        String[] PERMISSION_LIST = {"can_read_users", "can_update_users", "can_delete_users", "can_create_users",
                                    "can_search_machines", "can_start_machines", "can_stop_machines", "can_restart_machines",
                                    "can_create_machines", "can_destroy_machines"};

        Random random = new Random();
        List<Permission> permissions = new ArrayList<>();
        for (int i = 0 ; i < PERMISSION_LIST.length ; i++){
            Permission permission = new Permission();
            permission.setPermission(PERMISSION_LIST[i]);

            permissions.add(permission);
        }
        permissionRepository.saveAll(permissions);

        List<User> users = new ArrayList<>();
        for (int i = 1; i < 29; i++) {
            User user = new User();
            user.setName(FIRST_NAME_LIST[random.nextInt(FIRST_NAME_LIST.length)]);
            user.setSurname(LAST_NAME_LIST[random.nextInt(LAST_NAME_LIST.length)]);
            user.setEmail("user" + i + "@gmail.com");
            user.setPassword(this.passwordEncoder.encode("user" + i));

            if(i == 1){
                for (int j = 0; j < permissions.size() ; j++) {
                    user.getPermissions().add(permissions.get(j));
                }
            }else {
                List<Integer> list = new ArrayList<>();
                for (int k = 0; k < permissions.size(); k++) {
                    list.add(k);
                }

                for (int j = 0; j < random.nextInt(11); j++) {
                    int rand = random.nextInt(list.size());
                    user.getPermissions().add(permissions.get(list.get(rand)));
                    list.remove(rand);
                }

                if (user.getPermissions().contains(permissions.get(1)) || user.getPermissions().contains(permissions.get(2)) || user.getPermissions().contains(permissions.get(3))) {
                    if (!user.getPermissions().contains(permissions.get(0))) {
                        user.getPermissions().add(permissions.get(0));
                    }
                }
            }
            users.add(user);
        }
        userRepository.saveAll(users);

        List<Machine> machines = new ArrayList<>();
        for(int i=1 ; i<11 ; i++){
//            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

            Machine machine = new Machine();
            machine.setName("machine" + i);
            machine.setUser(userRepository.findByUserId(Long.valueOf(i)));
            machine.setStatus(Status.STOPPED);
            machine.setActive(true);
//            machine.setDateCreated(sdf.parse(sdf.format(new Date())));
            machine.setDateCreated(new Date());

            machines.add(machine);
        }

        machineRepository.saveAll(machines);

        ErrorMsg errorMsg = new ErrorMsg();
        errorMsg.setDate(new Date());
        errorMsg.setOperation("example operation");
        errorMsg.setDescription("example description");
        errorMsg.setMachineId(Long.valueOf(3));

        errorMsgRepository.save(errorMsg);

        System.out.println("Data loaded!");
    }
}
