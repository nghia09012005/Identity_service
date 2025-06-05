package com.example.identity_service.service;

import com.example.identity_service.Exception.appException;
import com.example.identity_service.Exception.error;
import com.example.identity_service.dto.UserResponse.AuthenticationResponse;
import com.example.identity_service.dto.UserResponse.IntrospectResponse;
import com.example.identity_service.dto.request.AuthenticationRequest;
import com.example.identity_service.dto.request.IntrospectRequest;
import com.example.identity_service.dto.request.LogoutRequest;
import com.example.identity_service.dto.request.RefreshRequest;
import com.example.identity_service.entity.InvalidatedToken;
import com.example.identity_service.entity.Role;
import com.example.identity_service.entity.User;
import com.example.identity_service.repository.InvalidatedTokenRepository;
import com.example.identity_service.repository.userRepository;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cglib.core.Local;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Set;
import java.util.StringJoiner;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor// tao constructor vs param la cac final atribute
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationService {
    userRepository repo ;
    InvalidatedTokenRepository invalidatedTokenRepository;

    //dung annotation de lombok ko bo vao constructor
    @NonFinal
    @Value("${jwt.SignKey}") // chon annotation cua spring khong phai cua lombok
    protected  String SIGN_KEY;

    public IntrospectResponse confirm(IntrospectRequest request) throws JOSEException, ParseException {
        var Token = request.getToken();

        boolean isvalid = true  ;

        try{
            verifiedToken(Token);
        }
        catch (appException e){
            isvalid =false;
        }
        log.info("Token valid:" + isvalid);

        return IntrospectResponse.builder()
                .valid(isvalid) // het han chua
                .build();
    }

    public AuthenticationResponse isAuth(AuthenticationRequest request){
        // var = auto(c++)
        var us = repo.findByUsername(request.getUsername()).
                orElseThrow(()->new appException(error.UNCATEGORIZED_EXCEPTION));

        PasswordEncoder check = new BCryptPasswordEncoder(10);

        boolean valid =  check.matches(request.getPassword(), us.getPassword());


        if(!valid){
            throw new appException(error.UNAUTHENTICATED);
        }

        String token = this.GenerateToken(us);

        return AuthenticationResponse.builder()
                .isAuthen(true)
                .Token(token)
                .build();
    }

    public void Logout(LogoutRequest request) throws ParseException, JOSEException {
        String token = request.getToken();

            SignedJWT signedJWT =  verifiedToken(token);

            String jti =  signedJWT.getJWTClaimsSet().getJWTID();
            Date expirydate = signedJWT.getJWTClaimsSet().getExpirationTime();
            InvalidatedToken invalidatedToken = InvalidatedToken.builder()
                    .id(jti)
                    .expiryTime(expirydate)
                    .build();

            invalidatedTokenRepository.save(invalidatedToken);


    }

    /*
    frontend will call refresh so don't need to check expiry date of token
    */
    public AuthenticationResponse refreshToken(RefreshRequest request) throws ParseException, JOSEException {
        var token = request.getToken();
        // bo do invalid
        SignedJWT signedJWT = verifiedToken(token);
        var id = signedJWT.getJWTClaimsSet().getJWTID();
        var expiryDate = signedJWT.getJWTClaimsSet().getExpirationTime();
        InvalidatedToken invalidatedToken = InvalidatedToken.builder()
                .id(id)
                .expiryTime(expiryDate)
                .build();

        invalidatedTokenRepository.save(invalidatedToken);

        String username = signedJWT.getJWTClaimsSet().getSubject();
        User user = repo.findByUsername(username).orElseThrow(()->new appException(error.UNAUTHENTICATED));

        var newToken = GenerateToken(user);

        return AuthenticationResponse.builder()
                .Token(newToken)
                .isAuthen(true)
                .build();

    }

    private SignedJWT verifiedToken(String token) throws JOSEException, ParseException {
        JWSVerifier verifier = new MACVerifier(SIGN_KEY.getBytes());

        SignedJWT signedJWT = SignedJWT.parse(token);

        Date expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();

        var verified = signedJWT.verify(verifier);

        if (!(verified && expiryTime.after(new Date())))
            throw new appException(error.UNAUTHENTICATED);

        if (invalidatedTokenRepository
                .existsById(signedJWT.getJWTClaimsSet().getJWTID()))
            throw new appException(error.UNAUTHENTICATED);

        return signedJWT;
    }



    private String GenerateToken(User user){

        JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.HS512);// algo de ma hoa ( thuat toan ma hoa doi xung )
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getUsername())
                .issuer("NghiaTran.com") // domain cua ban
                .issueTime(new Date()) // thoi gian tao token
                .expirationTime(new Date(
                        Instant.now().plus(1, ChronoUnit.HOURS).toEpochMilli()
                )) // thoi han cua token ( gia tri hien tai + them 1 hour nua)
                .jwtID(UUID.randomUUID().toString())
                .claim("scope",this.BuildScope(user))
                .build();
        Payload payload = new Payload(jwtClaimsSet.toJSONObject());
        JWSObject jwsObject = new JWSObject(jwsHeader,payload);
        /*
         + ky de kem theo hash trong token
         + co 2 kieu
         + 1) (dg dung) ma hoa va gia ma giong nhau
         + 2) public key de giai ma private de ma hoa
         */
        try {
            jwsObject.sign(new MACSigner(SIGN_KEY.getBytes(StandardCharsets.UTF_8)));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            throw new appException(error.UNCATEGORIZED_EXCEPTION);
        }

    }
    private String BuildScope(User user){
        StringJoiner stringJoiner = new StringJoiner(" ");

        if(!CollectionUtils.isEmpty(user.getRoles())){
            user.getRoles().forEach(role -> {
                stringJoiner.add("ROLE_"+role.getName());
                if (!CollectionUtils.isEmpty(role.getPermissions())){
                    role.getPermissions().forEach(permission -> stringJoiner.add(permission.getName()));
                }
            });
        }
        return stringJoiner.toString();
    }

}
