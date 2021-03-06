package ru.sber.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.sber.json.requests.HeadRequest;
import ru.sber.json.response.HeadResponse;
import ru.sber.json.response.ResponseJson;
import ru.sber.task1.TransformNotation;
import ru.sber.task2.FileCalculator;
import ru.sber.task3.Post;

import java.io.File;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@RestController
@RequestMapping(path = {"/api"})
public class NetworkController {


    @GetMapping("/test")
    public ResponseEntity<Object> getEmpty() {
        logger.info("/test");
        return ResponseEntity.status(HttpStatus.OK).build();
    }


//    4. Реализовав все 3 задачи (или те, что удалось) сделать сервис, который бы запускал работу для каждой задачи.
//Реализовать клиент серверное приложение на спринге (можно спрингбут) с контроллерами, которые вызывают
// сервисы работы решенных задач.
//Контролеры должны принимать POST запросы с параметрами в урле и при необходимости телом запроса в виде json
//Урлы должны быть вида /task/{taskID }
//Где taskID – от 1 до 3, а необходимые параметры указывать в теле запроса.
//Для тестирования можно пользоваться рест клиентом POSTMAN, соответствующей фичей в IntellijIdea
// либо другим удобным инструментом
//Задача 1 должна возвращать ответ,
//Задача 2 должна возвращать путь до файла, в дополнении можно вывести данные из файла в json, если их не много (не обязательно)
//Задача 3 должна вернуть ОК и запускать процесс. Для доп. Задания можно реализовать рест контролер для запросов статуса задачи,
// с выводом кто в каком статусе на данный момент времени.


    @PostMapping(value = "/task/{taskID}")
    public ResponseEntity<Object> sendMailHeader(@PathVariable("taskID") int taskID, @RequestBody(required = false) HeadRequest headJson) throws Exception {
        logger.info("taskId: "+taskID);
        switch (taskID){
            case (1):
                logger.info("case 1");
                String calc = new TransformNotation(headJson.getRequestJson().getNotation()
                        , headJson.getRequestJson().getNumber()).calc();
                ResponseJson responseJson = new ResponseJson();
                responseJson.setTransformNumber(calc);
                HeadResponse response = new HeadResponse(responseJson);
                return ResponseEntity.status(HttpStatus.OK).body(response);
            case (2):
                logger.info("case 2");
                File fileOut = new File("./fileOut");
                File fileIn = new File("./fileIn");
                new FileCalculator().readWriteFromInFile(fileIn, fileOut);
                ResponseJson responseJson2 = new ResponseJson();
                responseJson2.setFilePath(fileOut.getCanonicalPath());
                HeadResponse response2 = new HeadResponse(responseJson2);
                return ResponseEntity.status(HttpStatus.OK).body(response2);
            case (3):
                logger.info("case 3");
                Post post = new Post("Post №1", new AtomicInteger(50), 20, 25,
                        3, 5, 500, 600, 5000,
                        6000, 5, 80,
                        75, 6000, 4000, 20,
                        25, 5000, 7000);
                Post anotherPost = new Post("Post №2", new AtomicInteger(55), 23, 27,
                        2, 4, 450, 550, 6000,
                        5000, 7, 79, 72,
                        6500, 4500, 19, 23,
                        4000, 6500);
                post.setAnotherPost(anotherPost);
                anotherPost.setAnotherPost(post);
                post.start();
                anotherPost.start();
                return ResponseEntity.status(HttpStatus.OK).build();
        }
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}