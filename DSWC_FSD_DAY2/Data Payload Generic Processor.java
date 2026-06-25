// DataPayload.java
public abstract class DataPayload {
    public abstract String getRawContent();
}

// JsonPayload.java
public class JsonPayload extends DataPayload {
    private String jsonContent;
    
    public JsonPayload(String jsonContent) {
        this.jsonContent = jsonContent;
    }
    
    @Override
    public String getRawContent() {
        return jsonContent;
    }
}

// XmlPayload.java
public class XmlPayload extends DataPayload {
    private String xmlContent;
    
    public XmlPayload(String xmlContent) {
        this.xmlContent = xmlContent;
    }
    
    @Override
    public String getRawContent() {
        return xmlContent;
    }
}

// PipelineProcessor.java
public class PipelineProcessor<T extends DataPayload> {
    public void process(T payload) {
        System.out.println("Processing payload: " + payload.getRawContent());
        System.out.println("Payload type: " + payload.getClass().getSimpleName());
        System.out.println("Content length: " + payload.getRawContent().length());
    }
}

// GenericProcessorDemo.java
public class GenericProcessorDemo {
    public static void main(String[] args) {
        PipelineProcessor<DataPayload> processor = new PipelineProcessor<>();
        
        JsonPayload json = new JsonPayload("{\"user\": \"john\", \"action\": \"login\"}");
        XmlPayload xml = new XmlPayload("<user><name>john</name><action>login</action></user>");
        
        processor.process(json);
        System.out.println();
        processor.process(xml);
        
        System.out.println("\n=== Generic Type Safety Demonstration ===");
        
        PipelineProcessor<JsonPayload> jsonProcessor = new PipelineProcessor<>();
        jsonProcessor.process(new JsonPayload("{\"status\": \"success\"}"));
        
        PipelineProcessor<XmlPayload> xmlProcessor = new PipelineProcessor<>();
        xmlProcessor.process(new XmlPayload("<status>success</status>"));
        
        System.out.println("\nCompilation would fail if trying to pass String to PipelineProcessor:");
        System.out.println("// PipelineProcessor<String> invalid = new PipelineProcessor<>(); // Compile error");
    }
}