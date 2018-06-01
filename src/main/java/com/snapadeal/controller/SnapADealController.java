package com.snapadeal.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class SnapADealController
{
    @RequestMapping(value="/login", method = RequestMethod.GET)
    public String userLoginGet(Model model)
    {
        return "login";
    }
}
