package com.example.bajajsolution;

public class WebhookResponse {
    private String webhookUrl;
    private String accessToken;

    // Getters and Setters
    public String getWebhookUrl() { return webhookUrl; }
    public void setWebhookUrl(String webhookUrl) { this.webhookUrl = webhookUrl; }
    public String getAccessToken() { return accessToken; }
    public void setAccessToken(String accessToken) { this.accessToken = accessToken; }
}