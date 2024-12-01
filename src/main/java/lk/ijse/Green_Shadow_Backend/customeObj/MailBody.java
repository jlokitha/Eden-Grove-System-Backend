package lk.ijse.Green_Shadow_Backend.customeObj;

import lombok.Builder;

import java.util.Map;

@Builder
public record MailBody(String to, String subject, String templateName, Map<String, String> replacements) {}