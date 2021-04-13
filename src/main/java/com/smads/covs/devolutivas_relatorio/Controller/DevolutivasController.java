package com.smads.covs.devolutivas_relatorio.Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smads.covs.devolutivas_relatorio.Model.SasQuestions;
import com.smads.covs.devolutivas_relatorio.Model.SasServices;
import com.smads.covs.devolutivas_relatorio.Service.DevolutivasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "devolutivas")
public class DevolutivasController  {

    private final DevolutivasService devolutivasService;

    private ObjectMapper mapper = new ObjectMapper();

    @Autowired
    public DevolutivasController(DevolutivasService devolutivasService) {
        this.devolutivasService = devolutivasService;
    }

    @GetMapping
    public ResponseEntity getSas() throws UnsupportedEncodingException {
        return new ResponseEntity<>(devolutivasService.getSas(), HttpStatus.OK);
    }

    @GetMapping(path = "{sasName}")
    public ResponseEntity getSasMonthActivity(@PathVariable("sasName") String sasName) throws UnsupportedEncodingException{
        Object objSasName = devolutivasService.getSasMonthActivity(sasName);
        if(objSasName == null)
        {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        else{
            return new ResponseEntity<>(objSasName, HttpStatus.OK);
        }
    }

    @GetMapping(path = "{sasName}/{sasMonthActivity}")
    public ResponseEntity<ArrayList<SasServices>> getSasServices(@PathVariable("sasName") String sasName,
                                                            @PathVariable("sasMonthActivity") String sasMonthActivity)
            throws UnsupportedEncodingException{

        ArrayList<SasServices> lstSasServices = devolutivasService.getSasServices(sasName, sasMonthActivity);
        if(lstSasServices == null){
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        else {
            return new ResponseEntity<>(lstSasServices, HttpStatus.OK);
        }
    }

    @GetMapping(path = "{sasName}/{sasMonthActivity}/{token}/{attribute_4}")
    public ResponseEntity getSasServicesAnswers(@PathVariable("token") String token, @PathVariable("attribute_4")String type) throws UnsupportedEncodingException {
        return new ResponseEntity(devolutivasService.getSasServicesAnswers(token, type), HttpStatus.OK);
    }

    @GetMapping(path = "questions/{qtoken}")
    public ResponseEntity<ArrayList<SasQuestions>> getQuestions(@PathVariable("qtoken") String qtoken) throws UnsupportedEncodingException {
        ArrayList<SasQuestions> lstQuestions = devolutivasService.getQuestions(qtoken);
        if(lstQuestions == null){
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        else {
            return new ResponseEntity<>(lstQuestions, HttpStatus.OK);
        }
    }
}
