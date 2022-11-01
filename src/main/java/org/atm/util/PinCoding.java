package org.atm.util;

import java.util.Base64;
//todo : можно заморочиться с кодировкой когда-нить
public class PinCoding {
    public String codePin(String pin) {
        return Base64.getEncoder().encodeToString(pin.getBytes());
    }
}
