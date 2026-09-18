package kz.kbtu.demo.config;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app")
public class AppProperties {
    private String name = "Practice Demo";
    private String greetingLanguage = "en";
    private int maxGreetings = 3;
    private final Feature feature = new Feature();
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getGreetingLanguage() {
        return greetingLanguage;
    }
    public void setGreetingLanguage(String greetingLanguage) {
        this.greetingLanguage = greetingLanguage;
    }
    public int getMaxGreetings() {
        return maxGreetings;
    }
    public void setMaxGreetings(int maxGreetings) {
        this.maxGreetings = maxGreetings;
    }
    public Feature getFeature() {
        return feature;
    }
    public static class Feature {
        private boolean extraEnabled = false;
        public boolean isExtraEnabled() {
            return extraEnabled;
        }
        public void setExtraEnabled(boolean extraEnabled) {
            this.extraEnabled = extraEnabled;
        }
    }
}
