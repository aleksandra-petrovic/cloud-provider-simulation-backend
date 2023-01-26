package com.example.demo.services;

import com.example.demo.model.ErrorMsg;
import com.example.demo.model.Machine;
import com.example.demo.repositories.ErrorMsgRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ErrorMsgService {

    private ErrorMsgRepository errorMsgRepository;

    @Autowired
    public ErrorMsgService(ErrorMsgRepository errorMsgRepository) {
        this.errorMsgRepository = errorMsgRepository;
    }
    public void log(Long machineId, String operation, String description) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        ErrorMsg errorMsg = new ErrorMsg();
        errorMsg.setDate(sdf.parse(sdf.format(new Date())));
        errorMsg.setMachineId(machineId);
        errorMsg.setOperation(operation);
        errorMsg.setDescription(description);
        this.errorMsgRepository.save(errorMsg);
    }

    public List<ErrorMsg> getAll(List<Machine> machines){
        List<ErrorMsg> errorMsgs = new ArrayList<>();
        for(Machine m: machines){
            errorMsgs.addAll(this.errorMsgRepository.findErrorMsgsByMachineId(m.getMachineId()));
        }
        return errorMsgs;
    }

}
