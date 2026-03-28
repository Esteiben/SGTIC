package com.uteq.sgtic.services;

import com.azure.storage.blob
        .BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.azure.storage.blob.models.BlobHttpHeaders;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
public class AzureStorageConfig {

    @Value("${azure.storage.connection-string}")
    private String connectionString;

    @Value("${azure.storage.container-name}")
    private String containerName;

    public String subirDocumento(MultipartFile archivo) throws IOException {
        if (archivo == null || archivo.isEmpty()) {
            return null;
        }

        String nombreOriginal = archivo.getOriginalFilename();
        String extension = "";

        if (nombreOriginal != null && nombreOriginal.contains(".")) {
            extension = nombreOriginal.substring(nombreOriginal.lastIndexOf("."));
        }

        String nombreUnico = UUID.randomUUID() + extension;

        BlobServiceClient blobServiceClient = new BlobServiceClientBuilder()
                .connectionString(connectionString)
                .buildClient();

        BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient(containerName);

        if (!containerClient.exists()) {
            containerClient.create();
        }

        BlobClient blobClient = containerClient.getBlobClient(nombreUnico);

        blobClient.upload(archivo.getInputStream(), archivo.getSize(), true);

        BlobHttpHeaders headers = new BlobHttpHeaders();
        headers.setContentType(
                archivo.getContentType() != null && !archivo.getContentType().isBlank()
                        ? archivo.getContentType()
                        : "application/octet-stream"
        );

        blobClient.setHttpHeaders(headers);

        return blobClient.getBlobUrl();
    }
}