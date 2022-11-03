package org.atm.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.atm.util.Cbr.CbrStatement;
import org.atm.util.RequestCurrency;

import java.io.IOException;
import java.net.URISyntaxException;

public class CurrencyService {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static Double rubUsd;
    private static Double rubEur;

    private static CbrStatement mapJsonToPojo(String object) throws JsonProcessingException {
        return OBJECT_MAPPER.readValue(object, CbrStatement.class);
    }

    public static Double getRubUsd() {
        return rubUsd;
    }

    public static Double getRubEur() {
        return rubEur;
    }

    public static void rubRatioCheck() {
        try {
            CbrStatement cbr = mapJsonToPojo(RequestCurrency.response());
            rubUsd = cbr.getValute().getUsd().getValue();
            rubEur = cbr.getValute().getEur().getValue();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
