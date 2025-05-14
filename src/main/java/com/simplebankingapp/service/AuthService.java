package com.simplebankingapp.service;

import com.simplebankingapp.entity.ApiRole;
import com.simplebankingapp.entity.User;
import com.simplebankingapp.entity.UserRole;
import com.simplebankingapp.repository.ApiRoleRepository;
import com.simplebankingapp.repository.UserRepository;
import com.simplebankingapp.repository.UserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    UserRoleRepository userRoleRepository;

    @Autowired
    ApiRoleRepository apiRoleRepository;



    public boolean isAuthorized(String apiUri, String httpMethod, long userId){

        ApiRole apiRole = apiRoleRepository.findByUriAndHttpMethod(apiUri,httpMethod);
        String apiPermissions = apiRole.getRole();
        UserRole userRole = userRoleRepository.findByUserId(userId);
        if(userRole==null || apiRole==null)
            return false;
        String thisUserRole = userRole.getRole();
        String[] apiRoles = apiPermissions.split(",");
        boolean match = false;
        for(String apiRoleVal:apiRoles){
            if(apiRoleVal.equals(thisUserRole)){
                match = true;
            }
        }
        return match;
    }


}
