package com.snapadeal.controller;

import com.snapadeal.constants.SnapADealConstants;
import com.snapadeal.entity.*;
import com.snapadeal.entity.enums.Category;
import com.snapadeal.exceptions.BusinessProfileException;
import com.snapadeal.form.ChangePasswordForm;
import com.snapadeal.form.LoginForm;
import com.snapadeal.form.ProductIntakeForm;
import com.snapadeal.form.UpdateBusinessForm;
import com.snapadeal.services.ImageService;
import com.snapadeal.services.ProductListService;
import com.snapadeal.services.SnapADealServices;
import org.apache.poi.util.StringUtil;
import org.bouncycastle.ocsp.Req;
import org.springframework.beans.factory.annotation.Autowired;
import com.snapadeal.services.SnapADealServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
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
        snapADealServices.loadLocationToSession(pRequest);
        model.addAttribute("categories", Category.values());
//        List<Product> list = productListService.getProductsToDisplay();
        if(null!=pRequest.getParameter ( "latitude" )) {
            List<Product> productList = snapADealServices.getAllNearByProducts(Double.parseDouble ( pRequest.getParameter ( "latitude" )),Double.parseDouble ( pRequest.getParameter ( "longitude" )), 30);
            model.addAttribute("products", productList);
        }
        model.addAttribute("reservationOrder",new ReservationOrder());
        return "service-page";
    }

    @RequestMapping(value="/admin/products", method = RequestMethod.GET)
    public String businessPLPGET(Model model)
    {
        model.addAttribute("products",snapADealServices.getCurrentBusinessUser(true).getProductList());
        return "sadmin/products";
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
        List errors = new ArrayList();

        if(null == pRequest.getParameter("latitude") || "".equalsIgnoreCase(pRequest.getParameter("latitude")))
        {
            errors.add("Please enter a valid Store Location");
        }

        if (result.hasErrors() || errors.size()>0) {

            System.out.println("SnapADealController:addBusinessPOST() - Error --> "+result.toString());

            for(Object error: result.getAllErrors())
            {
                if(error instanceof FieldError)
                {
                    FieldError fieldError = (FieldError)error;

                    if("category".equalsIgnoreCase(fieldError.getField()))
                    {
                        errors.add("Please select a Category");
                    }
                    else {
                        errors.add(fieldError.getDefaultMessage());
                    }
                }
            }
            model.addAttribute("error", true);
            model.addAttribute("errors",errors);
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
            System.out.println("SnapADealController:businessLoginPOST() - Error --> "+result.toString());

            List errors = new ArrayList();

            for(Object error: result.getAllErrors())
            {
                if(error instanceof FieldError)
                {
                    FieldError fieldError = (FieldError)error;
                    errors.add(fieldError.getDefaultMessage());
                }
            }
            model.addAttribute("error", true);
            model.addAttribute("errors",errors);
            model.addAttribute("loginForm",loginForm);
            model.addAttribute("categories", Category.values());
            return "sadmin/business-login";
        }

        try {
            snapADealServices.loginBusinessAccount(loginForm);
            List<Product> businessProducts = snapADealServices.getCurrentBusinessUser(false).getProductList();
            if(businessProducts == null || businessProducts.isEmpty()) {
                model.addAttribute("productIntakeForm",new ProductIntakeForm());
                return "redirect:/admin/add-products";
            }
        } catch (BusinessProfileException e) {
            System.out.println("SnapADealController:businessLoginPOST() - BusinessProfileException --> "+e.getMessage());

            List errors = new ArrayList();

            errors.add(e.getMessage());

            model.addAttribute("error", true);
            model.addAttribute("errors",errors);
            model.addAttribute("loginForm",loginForm);
            model.addAttribute("categories", Category.values());
            return "sadmin/business-login";
        }

        return "redirect:/admin/products";
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
        List errors = new ArrayList();
        if(null==productIntakeForm.getPrimaryImage() || productIntakeForm.getPrimaryImage().isEmpty())
        {
            errors.add("Please upload a valid Product Image");
        }
        if (result.hasErrors() || errors.size()>0) {
            System.out.println("SnapADealController:addProductsPOST() - Error --> "+result.toString());

            for(Object error: result.getAllErrors())
            {
                if(error instanceof FieldError)
                {
                    FieldError fieldError = (FieldError)error;
                    errors.add(fieldError.getDefaultMessage());
                }
            }
            model.addAttribute("error", true);
            model.addAttribute("errors",errors);

            model.addAttribute("productIntakeForm",productIntakeForm);
            return "sadmin/add-products";
        }

        try {
            Product product = snapADealServices.convertFormToProduct(productIntakeForm);
            snapADealServices.createProduct(businessProfile, product);
        } catch (Exception e) {
            e.printStackTrace();

            errors.add(e.getMessage());

            model.addAttribute("error", true);
            model.addAttribute("errors",errors);

            model.addAttribute("productIntakeForm",productIntakeForm);
            return "sadmin/add-products";
        }

        return "redirect:/admin/products";
    }

    @RequestMapping(value="/admin/edit-product", method = RequestMethod.GET)
    public String editProductGET(Model model, @RequestParam("id") String productId)
    {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        //ROLE_ANONYMOUS
        if ("anonymousUser".equalsIgnoreCase(auth.getName())
                && (null == httpSession.getAttribute(USER_TYPE_KEY) || BUSINESSUSER.equalsIgnoreCase((String)httpSession.getAttribute(USER_TYPE_KEY))))
        {
            return "redirect:/admin/business-login";
        }

        if(null!=httpSession.getAttribute("error") && (boolean)httpSession.getAttribute("error"))
        {
            System.out.println("SnapADealController:editProductGET() - Error Scenario in Edit Product --> "+productId);

            model.addAttribute("error", true);
            model.addAttribute("errors", httpSession.getAttribute("errors"));
            model.addAttribute("productIntakeForm",httpSession.getAttribute("productIntakeForm"));

            httpSession.removeAttribute("error");
            httpSession.removeAttribute("errors");
            httpSession.removeAttribute("productIntakeForm");
            model.addAttribute("product",snapADealServices.findProductById(productId));

            return "sadmin/edit-product";
        }
        model.addAttribute("productIntakeForm",new ProductIntakeForm());
        model.addAttribute("product",snapADealServices.findProductById(productId));
        return "sadmin/edit-product";
    }

    @RequestMapping(value="/admin/edit-product", method = RequestMethod.POST)
    public String editProductPOST(@Valid @ModelAttribute("productIntakeForm") ProductIntakeForm productIntakeForm, BindingResult result,
                                  Model model, HttpServletRequest pRequest)
    {
        BusinessProfile businessProfile = snapADealServices.getCurrentBusinessUser(false);
        System.out.println("SnapADealController:editProductPOST() - Editing Product --> "+productIntakeForm.toString()+" for Business Profile --> "+businessProfile.getLogin());

        if (result.hasErrors()) {
            System.out.println("SnapADealController:editProductPOST() - Error --> "+result.toString());

            List errors = new ArrayList();

            for(Object error: result.getAllErrors())
            {
                if(error instanceof FieldError)
                {
                    FieldError fieldError = (FieldError)error;
                    errors.add(fieldError.getDefaultMessage());
                }
            }
            httpSession.setAttribute("error", true);
            httpSession.setAttribute("errors",errors);

            httpSession.setAttribute("productIntakeForm",productIntakeForm);
            return "redirect:/admin/edit-product?id="+productIntakeForm.getId();
        }

        try {
            Product product = snapADealServices.convertFormToProduct(productIntakeForm);
            snapADealServices.editProduct(product);
        } catch (Exception e) {
            e.printStackTrace();
            List errors = new ArrayList();

            errors.add(e.getMessage());

            httpSession.setAttribute("error", true);
            httpSession.setAttribute("errors",errors);
            httpSession.setAttribute("productIntakeForm",productIntakeForm);
            return "redirect:/admin/edit-product?id="+productIntakeForm.getId();
        }

        return "redirect:/admin/products";
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

    @RequestMapping(value="/reserve", method = RequestMethod.POST)
    @ResponseBody
    public Map reservePOST(@Valid @ModelAttribute("reservationOrder") ReservationOrder reservationOrder, BindingResult result,
                           Model model, HttpServletRequest pRequest)
    {
        Map responseMap = new HashMap();
        BusinessProfile businessProfile = snapADealServices.getCurrentBusinessUser(false);
        System.out.println("SnapADealController:reservePOST() - Creating Order for Product --> "+reservationOrder.getProductId());

        if (result.hasErrors()) {
            responseMap.put("error", true);
            System.out.println("SnapADealController:reservePOST() - Error --> "+result.toString());
            model.addAttribute("reservationOrder",reservationOrder);
            return responseMap;
        }

        try {
            reservationOrder = snapADealServices.placeOrder(reservationOrder);
        } catch (Exception e) {
            e.printStackTrace();
            responseMap.put("error", true);
            responseMap.put("reservationOrder",reservationOrder);
            return responseMap;
        }

        responseMap.put("reservationOrder",reservationOrder);
        responseMap.put("orderPlaced",true);

        return responseMap;
    }

    @RequestMapping(value="/admin/my-account", method = RequestMethod.GET)
    public String myAccount(Model model){

        model.addAttribute("categories", Category.values());
        model.addAttribute("businessProfile",snapADealServices.getCurrentBusinessUser(true));

        return "sadmin/business-profile";
    }

    @RequestMapping(value="/admin/update-password", method = RequestMethod.POST)
    public String changePassword(@Valid @ModelAttribute("changePasswordForm") ChangePasswordForm changePasswordForm, BindingResult result,
                                 Model model){

        //pre-check validations
        if (result.hasErrors()) {
            model.addAttribute("error", true);
            System.out.println("SnapADealController:editProductPOST() - Error --> "+result.toString());
            model.addAttribute("categories", Category.values());
            model.addAttribute("businessProfile",snapADealServices.getCurrentBusinessUser(false));
            return "sadmin/business-profile";
        }

        //post-validation checks
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        if(!StringUtils.isEmpty(changePasswordForm.getOldPassword()) &&
                !passwordEncoder.matches(changePasswordForm.getOldPassword(), snapADealServices.getCurrentBusinessUser(false).getPassword())){
            result.addError(new FieldError("changePasswordForm", "oldPassword", "Password is invalid."));
        }

        if(!changePasswordForm.getNewPassword().equals(changePasswordForm.getConfirmPassword())){
            result.addError(new ObjectError("changePasswordForm", "New Password and Confirm Password must match"));
        }

        //post-check additional validations
        if (result.hasErrors()) {
            model.addAttribute("error", true);
            System.out.println("SnapADealController:editProductPOST() - Error --> "+result.toString());
            model.addAttribute("categories", Category.values());
            model.addAttribute("businessProfile",snapADealServices.getCurrentBusinessUser(false));
            return "sadmin/business-profile";
        }

        BusinessProfile vBusinessProfile = new BusinessProfile();
        vBusinessProfile.setId(changePasswordForm.getProfileId());
        vBusinessProfile.setPassword(changePasswordForm.getNewPassword());

        try {
            snapADealServices.updatePassword(vBusinessProfile);
            model.addAttribute("status", "success");
            model.addAttribute("message", "Password Updated Successfully.");
        } catch (BusinessProfileException e) {
            e.printStackTrace();
            model.addAttribute("error", true);
            model.addAttribute("categories", Category.values());
            model.addAttribute("message", "Unable to upload Logo. Please try again later.");
            return "sadmin/business-profile";
        }

        return "redirect:/admin/my-account";
    }

    @RequestMapping(value="/admin/update-business", method = RequestMethod.POST)
    public String updateBusiness(@Valid @ModelAttribute("updateBusinessForm") UpdateBusinessForm updateBusinessForm, BindingResult result,
                                 Model model, HttpServletRequest pRequest, @RequestParam("logoImage") MultipartFile pLogoImage){

        if (result.hasErrors()) {
            model.addAttribute("error", true);
            System.out.println("SnapADealController:editProductPOST() - Error --> "+result.toString());
            model.addAttribute("categories", Category.values());
            model.addAttribute("businessProfile",snapADealServices.getCurrentBusinessUser(false));
            return "sadmin/business-profile";
        }

        BusinessProfile vBusinessProfile = new BusinessProfile();

        vBusinessProfile.setId(updateBusinessForm.getId());
        vBusinessProfile.setName(updateBusinessForm.getName());
        vBusinessProfile.setBusinessOwnerName(updateBusinessForm.getBusinessOwnerName());
        vBusinessProfile.setCategory(updateBusinessForm.getCategory());
        vBusinessProfile.setPhoneNumber(updateBusinessForm.getPhoneNumber());
        vBusinessProfile.setDescription(updateBusinessForm.getDescription());
        vBusinessProfile.setStoreAddress(updateBusinessForm.getStoreAddress());
        vBusinessProfile.setStoreHours(updateBusinessForm.getStoreHours());
        vBusinessProfile.setWebsite(updateBusinessForm.getWebsite());

        if(pRequest.getParameter("latitude") != null && pRequest.getParameter("longitude") != null){
            double lat = Double.parseDouble(pRequest.getParameter("latitude"));
            double lon = Double.parseDouble(pRequest.getParameter("longitude"));
            vBusinessProfile.setLocation(new GeoJsonPoint(lon, lat));
        }

        try {

            if (pLogoImage != null && !pLogoImage.isEmpty()) {
                Map<String, String> uploadResult = imageService.updateImageBytes(pLogoImage.getBytes());
                if (uploadResult != null && uploadResult.get("secure_url") != null) {
                    vBusinessProfile.setLogo(uploadResult.get("secure_url"));
                }
            }

            snapADealServices.updateBusinessProfile(vBusinessProfile);

        }catch(IOException e) {
            e.printStackTrace();
            model.addAttribute("warn", true);
            model.addAttribute("categories", Category.values());
            model.addAttribute("message", "Unable to upload Logo. Please try again later.");
            return "sadmin/business-profile";
        }


        return "redirect:/admin/my-account";
    }

}
