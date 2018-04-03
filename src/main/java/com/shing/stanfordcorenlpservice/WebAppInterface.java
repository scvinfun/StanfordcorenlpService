package com.shing.stanfordcorenlpservice;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WebAppInterface {
    @PostMapping("stanfordcorenlpService")
    public boolean stanfordcorenlpService(@RequestParam("text") String text, @RequestParam("keyPhrase") String keyPhrase) {
        return (!StanforecorenlpController.getInstance().isSelfSubject(text, keyPhrase) || StanforecorenlpController.getInstance().isQuotationSentence(text));
    }
}
