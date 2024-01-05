package com.fpt.fptproducthunt.account.controller;

import com.fpt.fptproducthunt.account.repository.AccountRepository;
import com.fpt.fptproducthunt.account.service.AccountService;
import com.fpt.fptproducthunt.common.dto.ResponseObject;
import com.fpt.fptproducthunt.common.entity.Account;
import com.fpt.fptproducthunt.common.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RequestMapping(value = "api/v1/accounts")
@RestController
@CrossOrigin
@Tag(name = "Account APIs")
public class AccountController {
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private AccountService accountService;

    @Operation(summary = "Find account by username")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Query user successfully",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseObject.class)) }),
            @ApiResponse(responseCode = "403", description = "Don't have permission",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseObject.class)) })})
    @GetMapping("")
    public ResponseEntity<ResponseObject> findByUsername(String username) {
        Optional<Account> account = accountService.findByUsername(username);

        return account.isPresent() ?
                ResponseEntity.status(HttpStatus.OK).body(
                        new ResponseObject("OK", "Account found", account)
                ):
                ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                        new ResponseObject("FAILED", "Cannot find account with username = "+ username, "")
                );
    }

    @PostMapping("/{tokenDevice}")
    public ResponseEntity<ResponseObject> uploadDeviceToken(@PathVariable String tokenDevice,  @RequestHeader Map<String, String> headers) {
//        accountService.updateDeviceToken(userId, tokenDevice);

//        headers.forEach((key, value) -> {
//            System.out.println(String.format("Header '%s' = %s", key, value));
//        });

        String accessToken = headers.get("authorization").substring(7);
        System.out.println(accessToken);
        String[] chunks = accessToken.split("\\.");

        Base64.Decoder decoder = Base64.getMimeDecoder();
        String payload = new String(decoder.decode(chunks[1]), StandardCharsets.UTF_8);
        JSONObject payload_data = new JSONObject(payload);
        String id = payload_data.getString("jti");

        accountService.updateDeviceToken(UUID.fromString(id), tokenDevice);

        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK", "Token posted", "")
        );
    }

}
