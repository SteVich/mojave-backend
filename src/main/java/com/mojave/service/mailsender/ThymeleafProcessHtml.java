package com.mojave.service.mailsender;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import java.util.Map;

@RequiredArgsConstructor
@Component
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class ThymeleafProcessHtml {

    SpringTemplateEngine templateEngine;

    public String getProcessedHtml(Map<String, Object> model, String templateName) {
        String template = null;

        Context context = new Context();

        if (model != null) {
            model.forEach(context::setVariable);
            template = templateEngine.process(templateName, context);
        }

        return template;
    }
}
