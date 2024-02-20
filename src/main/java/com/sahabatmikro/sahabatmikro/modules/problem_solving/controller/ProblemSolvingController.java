package com.sahabatmikro.sahabatmikro.modules.problem_solving.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ProblemSolvingController {

    /**
     * TASK : Write a function that takes a list of integers as input and returns the sum of all
     * even numbers in the list.
     * @param number
     * @return
     */
    @GetMapping(
            path = "/api/problem-solving"
    )
    public String sumEvenNumber(@RequestBody List<Integer> number){
        int total = 0;

        for (Integer priority : number) {
            if (priority % 2 == 0) total += priority;
        }

        return "Hasil : " + total;
    }
}
