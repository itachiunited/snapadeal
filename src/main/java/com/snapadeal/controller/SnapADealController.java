package com.snapadeal.controller;

import com.snapadeal.constants.SnapADealConstants;
import com.snapadeal.entity.BusinessProfile;
import com.snapadeal.entity.Location;
import com.snapadeal.entity.Product;
import com.snapadeal.entity.enums.Category;
import com.snapadeal.exceptions.BusinessProfileException;
import com.snapadeal.form.LoginForm;
import com.snapadeal.form.ProductIntakeForm;
import com.snapadeal.services.ImageService;
import com.snapadeal.services.ProductListService;
import com.snapadeal.services.SnapADealServices;
import org.bouncycastle.ocsp.Req;
import org.springframework.beans.factory.annotation.Autowired;
import com.snapadeal.services.SnapADealServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Controller
public class SnapADealController implements SnapADealConstants
{
    @Autowired
    private SnapADealServices snapADealServices;

    @Autowired
    private HttpSession httpSession;

    @Autowired
    private ProductListService productListService;

    @Autowired
    private ImageService imageService;


    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index(Model model,HttpServletRequest pRequest) {
        if(null!=pRequest.getParameter ( "latitude" )) {
            httpSession.setAttribute ( "latitude" , pRequest.getParameter ( "latitude" ) );
            httpSession.setAttribute ( "longitude" , pRequest.getParameter ( "longitude" ) );
            double            latitude  = Double.parseDouble ( pRequest.getParameter ( "latitude" ) );
            double            longitude = Double.parseDouble ( pRequest.getParameter ( "longitude" ) );
            Point             dus       = new Point ( longitude , latitude );
            List < Location > locations = snapADealServices.findLocationByMiles ( dus );
            if ( null != locations && locations.size()>0) {
                Location location = locations.get ( 0 );
                System.out.println ( location );
                httpSession.setAttribute ( "zipCode" , location.getId ( ) );
            }
        }
        model.addAttribute("categories", Category.values());
//        List<Product> list = productListService.getProductsToDisplay();
        if(null!=pRequest.getParameter ( "latitude" )) {
            List<Product> productList = snapADealServices.getAllNearByProducts(Double.parseDouble ( pRequest.getParameter ( "latitude" )),Double.parseDouble ( pRequest.getParameter ( "longitude" )), 25);
            model.addAttribute("products", productList);
        }
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


    @RequestMapping(value="/admin/add-business", method = RequestMethod.GET)
    public String addBusiness(Model model)
    {
        model.addAttribute("categories", Category.values());
        model.addAttribute("businessProfile",new BusinessProfile());
        return "sadmin/add-business";
    }

    @RequestMapping(value="/admin/add-business", method = RequestMethod.POST)
    public String addBusinessPOST(@Valid @ModelAttribute("businessProfile") BusinessProfile businessProfile, BindingResult result,
                                  Model model, HttpServletRequest pRequest, @RequestParam("logoImage") MultipartFile pLogoImage)
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
            businessProfile.setLocation(new GeoJsonPoint(lon, lat));
        }

        try {

            if(pLogoImage != null && !pLogoImage.isEmpty()){
                Map<String,String> uploadResult = imageService.updateImageBytes(pLogoImage.getBytes());
                if(uploadResult != null && uploadResult.get("secure_url") != null){
                    businessProfile.setLogo(uploadResult.get("secure_url"));
                }
            }

            snapADealServices.createBusinessAccount(businessProfile);
        } catch (BusinessProfileException e) {
            e.printStackTrace();
            model.addAttribute("error", true);
            model.addAttribute("businessProfile",businessProfile);
            model.addAttribute("categories", Category.values());
            return "sadmin/add-business";
        } catch (IOException e) {
            e.printStackTrace();
            model.addAttribute("warn", true);
            model.addAttribute("businessProfile",businessProfile);
            model.addAttribute("categories", Category.values());
            model.addAttribute("message", "Unable to upload Logo. Please try again later.");
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
        System.out.println("SnapADealController:businessLoginPOST() - Business Login for --> "+loginForm.getLogin());

        if (result.hasErrors()) {
            model.addAttribute("error", true);
            System.out.println("SnapADealController:businessLoginPOST() - Error --> "+result.toString());
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
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        //ROLE_ANONYMOUS
        if ("anonymousUser".equalsIgnoreCase(auth.getName())
                && (null == httpSession.getAttribute(USER_TYPE_KEY) || BUSINESSUSER.equalsIgnoreCase((String)httpSession.getAttribute(USER_TYPE_KEY))))
        {
            return "redirect:/admin/business-login";
        }
        model.addAttribute("productIntakeForm",new ProductIntakeForm());
        return "sadmin/add-products";
    }

    @RequestMapping(value="/admin/add-products", method = RequestMethod.POST)
    public String addProductsPOST(@Valid @ModelAttribute("productIntakeForm") ProductIntakeForm productIntakeForm, BindingResult result,
                                    Model model, HttpServletRequest pRequest)
    {
        BusinessProfile businessProfile = snapADealServices.getCurrentBusinessUser(false);
        System.out.println("SnapADealController:addProductsPOST() - Creating Product --> "+productIntakeForm.getName()+" for Business Profile --> "+businessProfile.getLogin());

        if (result.hasErrors()) {
            model.addAttribute("error", true);
            System.out.println("SnapADealController:addProductsPOST() - Error --> "+result.toString());
            model.addAttribute("productIntakeForm",productIntakeForm);
            return "sadmin/add-products";
        }

        try {
            Product product = snapADealServices.convertFormToProduct(productIntakeForm);
            snapADealServices.createProduct(businessProfile, product);
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", true);
            model.addAttribute("productIntakeForm",productIntakeForm);
            return "sadmin/add-products";
        }

        return "redirect:/products";
    }

    public SnapADealServices getSnapADealServices ( ) {
        return snapADealServices;
    }

    public void setSnapADealServices ( SnapADealServices snapADealServices ) {
        this.snapADealServices = snapADealServices;
    }

    public ProductListService getProductListService() {
        return productListService;
    }

    public void setProductListService(ProductListService productListService) {
        this.productListService = productListService;
    }


}
