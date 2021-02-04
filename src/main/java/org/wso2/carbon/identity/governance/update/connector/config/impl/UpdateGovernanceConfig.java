package org.wso2.carbon.identity.governance.update.connector.config.impl;


import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;
import org.wso2.carbon.identity.event.IdentityEventException;
import org.wso2.carbon.identity.governance.IdentityGovernanceException;
import org.wso2.carbon.identity.governance.IdentityGovernanceService;
import org.wso2.carbon.identity.governance.update.connector.config.impl.internal.ConnectorConfigUpdateComponent;

public class UpdateGovernanceConfig {
    public void init(){
        //JSON parser object to parse read file
        JSONParser jsonParser = new JSONParser();
        //Write JSON file
        try (FileReader reader = new FileReader("repository/resources/conf/update_config.json")) {

            //Read JSON file
            Object obj = jsonParser.parse(reader);
            JSONArray configList = (JSONArray) obj;
            configList.forEach( emp -> parseConfig( (JSONObject) emp ) );

        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }
    private void parseConfig(JSONObject config){
        String tenantDomain = (String) config.get("tenant_domain");
        String key = (String) config.get("property");
        String value = (String) config.get("value");
        try {
            updateConnectorConfig(key, value, tenantDomain);
        } catch (IdentityEventException e) {
            e.printStackTrace();
        }
    }

    private void updateConnectorConfig(String key, String value, String tenantDomain) throws IdentityEventException {
        try {
            IdentityGovernanceService identityGovernanceService = ConnectorConfigUpdateComponent
                    .getIdentityGovernanceService();
            if (identityGovernanceService != null) {
                Map<String,String> stringStringMap = new HashMap<>();
                stringStringMap.put(key, value);
                identityGovernanceService.updateConfiguration(tenantDomain, stringStringMap);
            }
        } catch (IdentityGovernanceException e) {
            throw new IdentityEventException("Error while getting connector configurations for property :" + key, e);
        }
    }
}
