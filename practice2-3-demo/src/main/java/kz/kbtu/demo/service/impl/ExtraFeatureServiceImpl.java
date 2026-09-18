package kz.kbtu.demo.service.impl;

import kz.kbtu.demo.service.ExtraFeatureService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(prefix = "app.feature", name = "extra-enabled", havingValue = "true")
public class ExtraFeatureServiceImpl implements ExtraFeatureService {

    @Override
    public String getFeatureMessage() {
        return "Дополнительная фича включена (app.feature.extra-enabled=true)";
    }
}
