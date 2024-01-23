package com.andrew;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class CrptApi {
    private final Semaphore semaphore;
    private final String apiUrl;

    public CrptApi(TimeUnit timeUnit, int requestLimit) {
        long intervalInSeconds = timeUnit.toSeconds(1);
        this.semaphore = new Semaphore(requestLimit);
        this.apiUrl = "https://ismp.crpt.ru/api/v3/lk/documents/create";
    }

    public void createDocument(Document document, String signature) {
        try {
            if (semaphore.tryAcquire()) {
                HttpClient httpClient = HttpClients.createDefault();
                HttpPost httpPost = new HttpPost(apiUrl);
                httpPost.setHeader("Content-Type", "application/json");

                ObjectMapper objectMapper = new ObjectMapper();
                String jsonDocument = objectMapper.writeValueAsString(document);

                StringEntity entity = new StringEntity(jsonDocument);
                httpPost.setEntity(entity);

                HttpResponse response = httpClient.execute(httpPost);

                // Обработка ответа

            } else {
                // Лимит запросов превышен, выполнение блокируется
                // Здесь можно добавить логику для ожидания или другие действия
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            semaphore.release();
        }
    }

    public static void main(String[] args) {
        CrptApi crptApi = new CrptApi(TimeUnit.SECONDS, 5);
        CrptApi.Document crptDocument = new CrptApi.Document();
        String signature = "exampleSignature";

        crptApi.createDocument(crptDocument, signature);
    }


    public static class Document {
        @JsonProperty("description")
        private Description description;
        @JsonProperty("doc_id")
        private String docId;
        @JsonProperty("doc_status")
        private String docStatus;
        @JsonProperty("doc_type")
        private String docType;
        @JsonProperty("importRequest")
        private boolean importRequest;
        @JsonProperty("owner_inn")
        private String ownerInn;
        @JsonProperty("participant_inn")
        private String participantInn;
        @JsonProperty("producer_inn")
        private String producerInn;
        @JsonProperty("production_date")
        private String productionDate;
        @JsonProperty("production_type")
        private String productionType;
        @JsonProperty("products")
        private Product[] products;
        @JsonProperty("reg_date")
        private String regDate;
        @JsonProperty("reg_number")
        private String regNumber;
        public static class Description {
            @JsonProperty("participantInn")
            private String participantInn;

        }

        public static class Product {
            @JsonProperty("certificate_document")
            private String certificateDocument;
            @JsonProperty("certificate_document_date")
            private String certificateDocumentDate;
            @JsonProperty("certificate_document_number")
            private String certificateDocumentNumber;
            @JsonProperty("owner_inn")
            private String ownerInn;
            @JsonProperty("producer_inn")
            private String producerInn;
            @JsonProperty("production_date")
            private String productionDate;
            @JsonProperty("tnved_code")
            private String tnvedCode;
            @JsonProperty("uit_code")
            private String uitCode;
            @JsonProperty("uitu_code")
            private String uituCode;
        }
    }
}
