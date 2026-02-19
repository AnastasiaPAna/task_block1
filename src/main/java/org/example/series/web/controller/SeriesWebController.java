package org.example.series.web.controller;

import org.example.series.core.service.SeriesService;
import org.example.series.core.service.StatisticsService;
import org.example.series.web.mapper.SeriesViewMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;



/**
 * MVC controller serving HTML pages for the simple web UI.
 */
@Controller

public class SeriesWebController {

    private final SeriesService seriesService;

    public SeriesWebController(SeriesService seriesService) {
        this.seriesService = seriesService;
    }

    @GetMapping("/")
    public String home(Model model) {

        model.addAttribute("series",
                SeriesViewMapper.toViewModelList(seriesService.findAll()));

        return "index";
    }

    @GetMapping("/series")
    public String allSeries(Model model) {

        model.addAttribute("series",
                SeriesViewMapper.toViewModelList(seriesService.findAll()));

        return "series-list";
    }

    @GetMapping("/top")
    public String top(Model model) {

        var topSeries = StatisticsService.topNByRating(
                seriesService.findAll(),
                5
        );

        model.addAttribute("series",
                SeriesViewMapper.toViewModelList(topSeries));

        model.addAttribute("n", 5);

        return "series-list";
    }

    @GetMapping("/statistics")
    public String statistics(
            @RequestParam(required = false) String attribute,
            Model model) {

        if (attribute != null && !attribute.isBlank()) {
            model.addAttribute("stats",
                    StatisticsService.countByAttribute(
                            seriesService.findAll(),
                            attribute
                    ));
        }

        return "statistics";
    }
}
