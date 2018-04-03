package com.shing.stanfordcorenlpservice;

import edu.stanford.nlp.ie.util.RelationTriple;
import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.CoreQuote;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.simple.Document;
import edu.stanford.nlp.simple.Sentence;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

public class StanforecorenlpController {
    private static StanforecorenlpController instance = null;
    private static StanfordCoreNLP pipeline = null;
    private static String dummy_origin_text;
    private static String dummyKeyPhrase;

    public static StanforecorenlpController getInstance() {
        if (instance == null) {
            instance = new StanforecorenlpController();
        }
        return instance;
    }

    private StanforecorenlpController() {
        dummy_origin_text = "This is dummy text for initiation.";
        dummyKeyPhrase = "dummy";
    }

    public void initStanforecorenlp(boolean decision) {
        if (decision) {
            System.out.println("==========  START  ==========");
            isSelfSubject(dummy_origin_text, dummyKeyPhrase);
            isQuotationSentence(dummy_origin_text);

            System.out.println("<=========  INIT MESSAGE  =========>");
            System.out.println("INITIATION IS FINISHED");
            System.out.println("");
            System.out.println("==========  END  ==========");
        }
    }

    public boolean isSelfSubject(String text_para, String keyPhrase_para) {
        String text = text_para.toLowerCase();
        String keyPhrase = keyPhrase_para.toLowerCase();

        ArrayList<RelationTriple> rts = new ArrayList();
        Document doc = new Document(text);
        for (Sentence sentence : doc.sentences()) {
            Collection<RelationTriple> openie = sentence.openieTriples();
            for (RelationTriple r : openie) {
                rts.add(r);
            }
        }

        boolean result = false;
        if (rts.size() == 0) {
            return true;
        } else {
            for (RelationTriple r : rts) {
                if (r.objectGloss().equals(keyPhrase) && r.subjectGloss().equalsIgnoreCase("i"))
                    result = true;
                if (r.subjectGloss().contains("my") && r.subjectGloss().contains(keyPhrase))
                    result = true;
            }
        }

        return result;
    }

    public boolean isQuotationSentence(String text_para) {
        if (pipeline == null) {
            Properties props = new Properties();
            props.setProperty("annotators", "tokenize, ssplit, pos, lemma, ner, depparse, coref, quote");
            pipeline = new StanfordCoreNLP(props);
        }

        CoreDocument document = new CoreDocument(text_para);
        pipeline.annotate(document);

        List<CoreQuote> quotes = document.quotes();
        if (quotes.size() > 0)
            for (CoreQuote q : quotes)
                if (q.hasSpeaker && !q.speaker().get().equalsIgnoreCase("I"))
                    return true;
        return false;
    }
}
