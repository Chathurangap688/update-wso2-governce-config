package org.wso2.carbon.identity.governance.update.connector.config.impl.internal;
import org.osgi.framework.BundleContext;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.wso2.carbon.identity.governance.IdentityGovernanceService;
import org.wso2.carbon.identity.governance.update.connector.config.impl.UpdateGovernanceConfig;

@Component(
        name = "identity.governance.update.connector.config.component",
        immediate = true
)
public class ConnectorConfigUpdateComponent {
    private static IdentityGovernanceService identityGovernanceService;

    public static IdentityGovernanceService getIdentityGovernanceService() {

        return identityGovernanceService;
    }

    @Activate
    protected void activate(ComponentContext context) {
        BundleContext bundleContext = context.getBundleContext();
        try {
            UpdateGovernanceConfig updateGovernanceConfig = new UpdateGovernanceConfig();
            updateGovernanceConfig.init();
        } catch (Throwable throwable) {
            System.out.println("error " + throwable);
        }
    }

    @Reference(
            name = "IdentityGovernanceService",
            service = IdentityGovernanceService.class,
            cardinality = ReferenceCardinality.MANDATORY,
            policy = ReferencePolicy.DYNAMIC,
            unbind = "unsetIdentityGovernanceService"
    )
    protected void setIdentityGovernanceService(IdentityGovernanceService identityGovernanceService) {

        ConnectorConfigUpdateComponent.identityGovernanceService = identityGovernanceService;
    }

    protected void unsetIdentityGovernanceService(IdentityGovernanceService identityGovernanceService) {

        ConnectorConfigUpdateComponent.identityGovernanceService = null;
    }
}
