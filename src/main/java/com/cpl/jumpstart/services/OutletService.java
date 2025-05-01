package com.cpl.jumpstart.services;

import com.cpl.jumpstart.entity.Outlet;
import com.cpl.jumpstart.entity.UserApp;
import com.cpl.jumpstart.repositories.CustomerRepository;
import com.cpl.jumpstart.repositories.OutletRepository;
import com.cpl.jumpstart.utils.CountryConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OutletService {

    @Autowired
    private OutletRepository outletRepo;

    @Autowired
    private CustomerRepository customerRepo;
    @Autowired
    private UserAppServices userAppServices;


    public Long getLastSavedSupplierId() {
        return outletRepo.findMaxId();
    }

    public void addNewOutlet(Outlet outlet, Long userId){

        UserApp userApp = userAppServices.findById(userId);

        Long outletCode = getLastSavedSupplierId();
        if(outletCode == null) {
            outletCode = 1L;
        } else {
            outletCode += 1;
        }
        outlet.setOutletCode("OUTLET-JP-" + outletCode);
        outlet.setUserApp(userApp);
        outletRepo.save(outlet);
    }

    public Outlet findById(Long outletId){
        return outletRepo.findById(outletId).orElseThrow(() ->
                new RuntimeException(String.format("Outlet with id '%s' not found", outletId))
        );
    }


    public Outlet findByOutletName(String outletName){
        return outletRepo.findByOutletName(outletName);
    }

    public void updateOutlet(Long outletId, Outlet updatedOutlet, String staffId){
        UserApp staff = userAppServices.findById(Long.parseLong(staffId));
        updatedOutlet.setUserApp(staff);
        outletRepo.save(updatedOutlet);
    }


    public Outlet findByUser(UserApp userApp){

        return outletRepo.findByUserApp(userApp).orElseThrow(() -> new RuntimeException(
                String.format("Outlet with userId %s not found !", userApp.getUserId())
        ));

    }


}
