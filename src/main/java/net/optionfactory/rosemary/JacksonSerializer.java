package net.optionfactory.rosemary;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.Writer;
import org.thymeleaf.standard.serializer.IStandardJavaScriptSerializer;

public class JacksonSerializer implements IStandardJavaScriptSerializer {

    private final ObjectMapper jackson;

    public JacksonSerializer(ObjectMapper jackson) {
        this.jackson = jackson;
    }

    @Override
    public void serializeValue(Object object, Writer writer) {
        try {
            jackson.writeValue(writer, object);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

}
