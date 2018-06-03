package com.snapadeal.controller;

import com.snapadeal.entity.BusinessProfile;
import com.snapadeal.entity.enums.Category;
import com.snapadeal.exceptions.BusinessProfileException;
import com.snapadeal.form.LoginForm;
import com.snapadeal.services.SnapADealServices;
import org.springframework.beans.factory.annotation.Autowired;
import com.snapadeal.services.SnapADealServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Controller
public class SnapADealController
{
    @Autowired
    private SnapADealServices snapADealServices;

    @RequestMapping(value = "/")
    public String index() {
        snapADealServices.findLocationByMiles ();
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

    @RequestMapping(value="/contact-us", method = RequestMethod.GET)
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

    @RequestMapping(value="/signup", method = RequestMethod.POST)
    public String signUpPOST(Model model)
    {
        return "redirect:/";
    }

    @RequestMapping(value="/services", method = RequestMethod.GET)
    public String servicesGet(Model model)
    {
        return "service-page";
    }

    @RequestMapping(value="/admin/add-business", method = RequestMethod.GET)
    public String addBusiness(Model model)
    {
        model.addAttribute("categories", Category.values());
        model.addAttribute("businessProfile",new BusinessProfile());
        return "sadmin/add-business";
    }

    @RequestMapping(value="/admin/add-business", method = RequestMethod.POST)
    public String addBusinessPOST(@Valid @ModelAttribute("businessProfile") BusinessProfile businessProfile, BindingResult result,
                                  Model model, HttpServletRequest pRequest)
    {
        System.out.println("SnapADealController:addBusinessPOST() - Creating Business Profile For --> "+businessProfile.getLogin());

        if (result.hasErrors()) {
            model.addAttribute("error", true);
            System.out.println("SnapADealController:addBusinessPOST() - Error --> "+result.toString());
            model.addAttribute("businessProfile",businessProfile);
            model.addAttribute("categories", Category.values());
            return "sadmin/add-business";
        }

        if(pRequest.getParameter("latitude") != null && pRequest.getParameter("longitude") != null && businessProfile != null){
            double lat = Double.parseDouble(pRequest.getParameter("latitude"));
            double lon = Double.parseDouble(pRequest.getParameter("longitude"));
            businessProfile.setLocation(new GeoJsonPoint(lat, lon));
        }

        try {
            snapADealServices.createBusinessAccount(businessProfile);
        } catch (BusinessProfileException e) {
            e.printStackTrace();
            model.addAttribute("error", true);
            model.addAttribute("businessProfile",businessProfile);
            model.addAttribute("categories", Category.values());
            return "sadmin/add-business";
        }

        return "redirect:/admin/add-products";
    }

    @RequestMapping(value="/admin/business-login", method = RequestMethod.GET)
    public String businessLoginGET(Model model)
    {
        model.addAttribute("categories", Category.values());
        model.addAttribute("loginForm",new LoginForm());
        return "sadmin/business-login";
    }

    @RequestMapping(value="/admin/business-login", method = RequestMethod.POST)
    public String businessLoginPOST(@Valid @ModelAttribute("loginForm") LoginForm loginForm, BindingResult result,
                                    Model model, HttpServletRequest pRequest)
    {
        System.out.println("SnapADealController:addBusinessPOST() - Creating Business Profile For --> "+loginForm.getLogin());

        if (result.hasErrors()) {
            model.addAttribute("error", true);
            System.out.println("SnapADealController:addBusinessPOST() - Error --> "+result.toString());
            model.addAttribute("loginForm",loginForm);
            model.addAttribute("categories", Category.values());
            return "sadmin/business-login";
        }

        try {
            snapADealServices.loginBusinessAccount(loginForm);
        } catch (BusinessProfileException e) {
            e.printStackTrace();
            model.addAttribute("error", true);
            model.addAttribute("loginForm",loginForm);
            model.addAttribute("categories", Category.values());
            return "sadmin/business-login";
        }

        return "redirect:/admin/add-products";
    }

    @RequestMapping(value="/admin/add-products", method = RequestMethod.GET)
    public String addProductsGET(Model model)
    {
        model.addAttribute("categories", Category.values());
        return "sadmin/add-products";
    }

    public SnapADealServices getSnapADealServices ( ) {
        return snapADealServices;
    }

    public void setSnapADealServices ( SnapADealServices snapADealServices ) {
        this.snapADealServices = snapADealServices;
    }
}
