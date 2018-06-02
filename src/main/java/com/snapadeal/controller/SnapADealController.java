package com.snapadeal.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class SnapADealController
{
    @RequestMapping(value = "/")
    public String index() {
        return "service-page";
    }

    @RequestMapping(value="/login", method = RequestMethod.GET)
    public String userLoginGet(Model model)
    {
        return "login";
    }

    @RequestMapping(value="/faq", method = RequestMethod.GET)
    public String faqGet(Model model)
    {
        return "faq";
    }

    @RequestMapping(value="/contactus", method = RequestMethod.GET)
    public String contactUsGet(Model model)
    {
        return "contact-us";
    }

    @RequestMapping(value="/products", method = RequestMethod.GET)
    public String productsGet(Model model)
    {
        return "product-page";
    }

    @RequestMapping(value="/signup", method = RequestMethod.GET)
    public String signUpGet(Model model)
    {
        return "registration";
    }

    @RequestMapping(value="/signUp", method = RequestMethod.POST)
    public String signUpPOST(Model model)
    {
        return "redirect:/";
    }

    @RequestMapping(value="/services", method = RequestMethod.GET)
    public String servicesGet(Model model)
    {
        return "service-page";
    }

    @RequestMapping(value="/admin/addbusiness", method = RequestMethod.GET)
    public String addBusiness(Model model)
    {
        return "sadmin/add-business";
    }
}
