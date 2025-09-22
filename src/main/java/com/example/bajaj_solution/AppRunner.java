package com.example.bajajsolution;

import org.springframework.boot.CommandLineRunner;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class AppRunner implements CommandLineRunner {

    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Application starting... Beginning the process.");

        // Step 1 & 2: Generate Webhook and get access token
        String generateUrl = "https://bfhldevapigw.healthrx.co.in/hiring/generateWebhook/JAVA";
        
        WebhookRequest initialRequest = new WebhookRequest();
        initialRequest.setName("Deexant Bharti"); 
        initialRequest.setRegNo("2211201261");
        initialRequest.setEmail("deexantbharti4321@gmail.com"); 

        try {
            WebhookResponse webhookResponse = restTemplate.postForObject(generateUrl, initialRequest, WebhookResponse.class);

            if (webhookResponse == null || webhookResponse.getWebhookUrl() == null) {
                System.err.println("Failed to get webhook URL and token.");
                return;
            }

            String webhookUrl = webhookResponse.getWebhookUrl();
            String accessToken = webhookResponse.getAccessToken();
            
            System.out.println("Successfully received Webhook URL: " + webhookUrl);

            // Step 3 & 4: Prepare and submit the SQL query
            String sqlQuery = "SELECT p.AMOUNT AS SALARY, CONCAT(e.FIRST_NAME, ' ', e.LAST_NAME) AS NAME, TIMESTAMPDIFF(YEAR, e.DOB, CURDATE()) AS AGE, d.DEPARTMENT_NAME FROM PAYMENTS p JOIN EMPLOYEE e ON p.EMP_ID = e.EMP_ID JOIN DEPARTMENT d ON e.DEPARTMENT = d.DEPARTMENT_ID WHERE p.AMOUNT = (SELECT MAX(AMOUNT) FROM PAYMENTS WHERE DAY(PAYMENT_TIME) <> 1);";

            SolutionRequest solutionRequest = new SolutionRequest();
            solutionRequest.setFinalQuery(sqlQuery);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", accessToken);

            HttpEntity<SolutionRequest> entity = new HttpEntity<>(solutionRequest, headers);

            restTemplate.exchange(webhookUrl, HttpMethod.POST, entity, String.class);
            System.out.println("Successfully submitted the final SQL query.");

        } catch (Exception e) {
            System.err.println("An error occurred: " + e.getMessage());
        }
        
        System.out.println("Process finished.");
    }
}