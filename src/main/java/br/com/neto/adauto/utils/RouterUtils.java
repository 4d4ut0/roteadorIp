package br.com.neto.adauto.utils;

import br.com.neto.adauto.model.Redirection;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class RouterUtils {

    private static Integer formatMaskCIDRNotation(String mask) {
        Integer cidr = 0;
        if (mask.contains(".")) {
            String[] parts = mask.split(Pattern.quote("."));
            for (String part : parts) {
                if (part.contains("255")) {
                    cidr += 8;
                } else {
                	Integer partInt = Integer.valueOf(part);
                	String binary = Integer.toBinaryString(partInt);
                	String[] binaryInParts = binary.split("");
                	for(String b : binaryInParts) {
                		cidr += Integer.valueOf(b);
                	}
                }
            }
        } else {
            cidr = Integer.parseInt(mask);
        }
        return cidr;
    }

    public static String formatMaskCIDRNotation(Integer numberInCIDNotation) {
    	return numberInCIDNotation.toString();
    }

    public static List<Redirection> formattingRoutingTableFromCommand(String[] routersForRedirection) {
        List<Redirection> redirections = new ArrayList<Redirection>();
        for (String redirection : routersForRedirection) {
            if (redirection.contains("/")) {
                Redirection redirectionCreated = formattingRoutingTable(redirection);
                if (redirectionCreated != null) {
                    redirections.add(redirectionCreated);
                }
            }
        }
        return redirections;
    }

    public static Redirection formattingRoutingTable(String router) {
        String[] parts = router.split("/");
        if (parts.length != 4) {
            return null;
        }
        Redirection redirection = new Redirection();
        redirection.setDestiny(parts[0]);
        if(parts[1].contains(".")) {
            redirection.setMask(RouterUtils.formatMaskCIDRNotation(parts[1]));
        } else {
        	redirection.setMask(Integer.valueOf(parts[1]));
        }
        redirection.setGateway(parts[2]);
        redirection.setInterfaceOutput(Integer.valueOf(parts[3]));
        return redirection;
    }
}
