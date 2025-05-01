package com.cpl.jumpstart.controller;


import com.cpl.jumpstart.Exception.EmailAlreadyExistException;
import com.cpl.jumpstart.Exception.OutletNotActiveException;
import com.cpl.jumpstart.Exception.RoleNotFoundException;
import com.cpl.jumpstart.Exception.UserNotActiveException;
import com.cpl.jumpstart.dto.request.RegistrationRequest;
import com.cpl.jumpstart.dto.response.CurrentUser;
import com.cpl.jumpstart.dto.response.MessageResponse;
import com.cpl.jumpstart.entity.constraint.UserAppRole;
import com.cpl.jumpstart.services.JwtService;
import com.cpl.jumpstart.dto.request.AuthenticationRequest;
import com.cpl.jumpstart.dto.response.AuthenticationResponse;
import com.cpl.jumpstart.entity.Token;
import com.cpl.jumpstart.entity.UserApp;
import com.cpl.jumpstart.entity.constraint.TokenType;
import com.cpl.jumpstart.repositories.TokenRepository;
import com.cpl.jumpstart.services.TokenService;
import com.cpl.jumpstart.services.UserAppServices;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @Autowired
    private UserAppServices userAppService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private TokenService tokensService;
    @Autowired
    private TokenRepository tokensRepository;

    @Autowired
    private JwtService jwtService;


    @PostMapping
    public ResponseEntity<AuthenticationResponse> login(@RequestBody AuthenticationRequest loginRequest) {

        AuthenticationResponse authenticationResponse = new AuthenticationResponse();

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

            UserDetails principals = userAppService.loadUserByUsername(loginRequest.getEmail());
            UserApp users = userAppService.findUserByEmail(principals.getUsername());

            String token = jwtService.generateToken(principals);

            Token jwtToken = Token.builder()
                    .user(users)
                    .token(token)
                    .tokenType(TokenType.BEARER)
                    .revoked(false)
                    .expired(false)
                    .build();

            tokensService.revokeAllUserTokens(users);
            tokensRepository.save(jwtToken);
            authenticationResponse.setToken(token);
            authenticationResponse.setRole(users.getUserRole().name());
            return ResponseEntity.ok(authenticationResponse);

        } catch (BadCredentialsException e) {
            authenticationResponse.setErrorType("NOT_FOUND");
            authenticationResponse.setErrorMessage("Invalid username or password !");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(authenticationResponse);
        } catch (UserNotActiveException e){
            authenticationResponse.setErrorType("NOT_ACTIVE");
            authenticationResponse.setErrorMessage("Outlet is inactive");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(authenticationResponse);
        } catch (OutletNotActiveException e){
            authenticationResponse.setErrorType("NOT_ACTIVE");
            authenticationResponse.setErrorMessage("You don't have an access to any outlet");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(authenticationResponse);
        }

//        catch (UserNotActiveException e) {
//            authenticationResponse.setErrorType("NOT_ACTIVE");
//            authenticationResponse.setErrorMessage("Your account still awaiting for admin approval");
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(authenticationResponse);
//        }

    }


}
