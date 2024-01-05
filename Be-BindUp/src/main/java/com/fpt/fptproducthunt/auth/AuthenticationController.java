package com.fpt.fptproducthunt.auth;

import com.fpt.fptproducthunt.account.service.AccountService;
import com.fpt.fptproducthunt.auth.dto.UserInfoDTO;
import com.fpt.fptproducthunt.common.dto.ResponseObject;
import com.fpt.fptproducthunt.common.entity.User;
import com.fpt.fptproducthunt.user.service.UserService;
import com.fpt.fptproducthunt.utility.BindUpUtility;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import org.json.JSONObject;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@CrossOrigin
@Tag(name = "Authentication")
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final UserService userService;
    private final AccountService accountService;

    @Operation(summary = "Register")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Register successfully",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseObject.class)) }),
            @ApiResponse(responseCode = "403", description = "Don't have permission",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseObject.class)) })})
    @PostMapping("/new")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authenticationService.register(request));
    }
    @Operation(summary = "Login")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login successfully",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseObject.class)) }),
            @ApiResponse(responseCode = "403", description = "Don't have permission",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseObject.class)) })})
    @PostMapping("")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }

    @PostMapping("/google")
    public ResponseEntity<AuthenticationResponse> getGoogleAccessToken(@RequestBody String googleAccessToken) {
//        googleAccessToken = new String(googleAccessToken, "UTF-8");
//        System.out.println(googleAccessToken.charAt(22));
        String[] chunks = googleAccessToken.split("\\.");

        Base64.Decoder decoder = Base64.getMimeDecoder();

//        System.out.println(chunks[0]);

        String header = new String(decoder.decode(chunks[0]), StandardCharsets.UTF_8);
        String payload = new String(decoder.decode(chunks[1]), StandardCharsets.UTF_8);
//        String signature = new String(decoder.decode(chunks[2]), StandardCharsets.UTF_8);
//        JSONObject header_data = new JSONObject(header);

        JSONObject payload_data = new JSONObject(payload);
        String name = payload_data.getString("name");
        String avatar = payload_data.getString("picture");
        String email = payload_data.getString("email");

//        System.out.println(payload);

        User user = userService.findByEmail(email);
//        System.out.println(user.getEmail());

        if (user != null) {
            return ResponseEntity.ok(authenticationService.authenticate(user));
        } else {
            String username = email.substring(0, email.indexOf("@")) + BindUpUtility.getUniqueId();
            String password = BindUpUtility.getPassword();

            RegisterRequest googleRegisterRequest = new RegisterRequest(name, username, 0, email, password, null);
            return ResponseEntity.ok(authenticationService.register(googleRegisterRequest));
        }
    }

    @GetMapping("")
    public ResponseEntity<UserInfoDTO> decodeToken(@RequestParam String accessToken) {
        String[] chunks = accessToken.split("\\.");

        Base64.Decoder decoder = Base64.getMimeDecoder();

        String header = new String(decoder.decode(chunks[0]), StandardCharsets.UTF_8);
        String payload = new String(decoder.decode(chunks[1]), StandardCharsets.UTF_8);

        JSONObject payload_data = new JSONObject(payload);
        String username = payload_data.getString("sub");
        String id = payload_data.getString("jti");

        return ResponseEntity.status(HttpStatus.OK).body(new UserInfoDTO(id, username));
    }
}
