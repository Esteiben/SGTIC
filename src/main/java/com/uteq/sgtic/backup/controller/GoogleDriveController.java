package com.uteq.sgtic.backup.controller;

import com.uteq.sgtic.backup.dto.BackupMessageDTO;
import com.uteq.sgtic.backup.dto.DriveAccountResponseDTO;
import com.uteq.sgtic.backup.dto.DriveAuthUrlResponseDTO;
import com.uteq.sgtic.backup.service.GoogleDriveAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/drive")
@RequiredArgsConstructor
public class GoogleDriveController {

    private final GoogleDriveAuthService googleDriveAuthService;

    @Value("${backup.drive.success-redirect:}")
    private String successRedirect;

    @Value("${backup.drive.failure-redirect:}")
    private String failureRedirect;

    @GetMapping("/oauth/url")
    public ResponseEntity<DriveAuthUrlResponseDTO> getAuthorizationUrl(@RequestParam Integer userId) {
        return ResponseEntity.ok(googleDriveAuthService.buildAuthorizationUrl(userId));
    }

    @GetMapping(value = "/oauth/callback", produces = MediaType.TEXT_HTML_VALUE)
    public ResponseEntity<String> callback(@RequestParam(required = false) String code,
                                           @RequestParam(required = false) String state,
                                           @RequestParam(required = false) String error) {
        try {
            if (error != null) {
                throw new IllegalStateException("Google devolvió error: " + error);
            }

            DriveAccountResponseDTO account = googleDriveAuthService.handleCallback(code, state);

            String html = """
                    <html>
                      <head><title>Google Drive vinculado</title></head>
                      <body style="font-family: Arial, sans-serif; padding: 24px;">
                        <h2>Cuenta de Google Drive vinculada correctamente</h2>
                        <p>Correo: %s</p>
                        <script>
                          if (window.opener) {
                            window.opener.postMessage({ type: 'GOOGLE_DRIVE_LINKED' }, '*');
                          }
                        </script>
                      </body>
                    </html>
                    """.formatted(account.getGoogleEmail() != null ? account.getGoogleEmail() : "(sin correo)");

            return ResponseEntity.ok(html);

        } catch (Exception e) {
            String html = """
                    <html>
                      <head><title>Error de vinculación</title></head>
                      <body style="font-family: Arial, sans-serif; padding: 24px;">
                        <h2>Error al vincular Google Drive</h2>
                        <p>%s</p>
                      </body>
                    </html>
                    """.formatted(e.getMessage());

            return ResponseEntity.badRequest().body(html);
        }
    }

    @GetMapping("/account/active")
    public ResponseEntity<?> getActiveAccount() {
        try {
            return ResponseEntity.ok(googleDriveAuthService.getActiveAccount());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new BackupMessageDTO(e.getMessage()));
        }
    }

    @DeleteMapping("/account/{id}")
    public ResponseEntity<?> unlink(@PathVariable Long id, @RequestParam Integer userId) {
        try {
            googleDriveAuthService.unlinkAccount(id, userId);
            return ResponseEntity.ok(new BackupMessageDTO("Cuenta de Google Drive desvinculada correctamente."));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new BackupMessageDTO(e.getMessage()));
        }
    }
}