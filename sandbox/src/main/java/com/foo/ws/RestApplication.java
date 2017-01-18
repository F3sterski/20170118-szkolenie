package com.foo.ws;

import ws.utils.jersey.EndpointLoggingListener;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.Set;

@ApplicationPath("/api")
public class RestApplication extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> s = new HashSet<>();
        s.add(HelloResource.class);
        s.add(HelloMessageText.class);
        s.add(EndpointLoggingListener.class);
        return s;
    }

    public static class HelloMessageText implements MessageBodyWriter<HelloResource.HelloMessage> {

        @Override
        public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
            return type == HelloResource.HelloMessage.class;
        }

        @Override
        public long getSize(HelloResource.HelloMessage message, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
            return 0;
        }

        @Override
        public void writeTo(HelloResource.HelloMessage message,
                            Class<?> type, Type genericType, Annotation[] annotations,
                            MediaType mediaType,
                            MultivaluedMap<String, Object> httpHeaders,
                            OutputStream entityStream) throws IOException, WebApplicationException {
            StringBuffer sb = new StringBuffer();
            sb.append("Hello Message: ").append(message.message).append("\n");

            entityStream.write(sb.toString().getBytes());
        }
    }

}