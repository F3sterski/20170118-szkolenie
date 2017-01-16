package ws.utils.resteasy;

import io.github.lukehutch.fastclasspathscanner.FastClasspathScanner;
import io.github.lukehutch.fastclasspathscanner.scanner.ScanResult;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.ws.rs.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@WebListener
public class EndpointListingListener implements ServletContextListener {

    private static Logger logger = Logger.getLogger(EndpointListingListener.class.getName());

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        FastClasspathScanner scanner = new FastClasspathScanner();
        ScanResult scan = scanner.scan();
        //TODO: @ApplicationPath
        //TODO: inny scanner: https://gist.github.com/2880038?
        scan.getNamesOfClassesWithAnnotation(Path.class).stream()
                .forEach(s -> {
                    try {
                        Class<?> resourceClass = Class.forName(s);
                        List<ResourceDescription> descriptions = ResourceDescription.fromBoundResourceInvokers(resourceClass.getDeclaredMethods());
                        StringBuilder sb = new StringBuilder("\nAll endpoints for RestEasy application\n");
                        descriptions.stream().forEach(desc -> {
                            sb.append("\t").append("Base path: ").append(desc.basePath).append("\n");
                            desc.calls.stream().forEach(call -> {
                                sb.append("\t - ").append(call.toString()).append("\n");
                            });
                        });
                        logger.info(sb.toString());
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }


    private static final class MethodDescription {
        private String method;
        private String fullPath;
        private List<String> produces;
        private List<String> consumes;

        public MethodDescription(String method, String fullPath, List<String> produces, List<String> consumes) {
            super();
            this.method = method;
            this.fullPath = fullPath;
            this.produces = produces;
            this.consumes = consumes;
        }

        @Override
        public String toString() {
            return method +
                    " " + fullPath +
                    ", produces='" + produces + '\'' +
                    ", consumes='" + consumes + '\'' +
                    '}';
        }
    }

    private static final class ResourceDescription {
        private String basePath;
        private List<MethodDescription> calls;

        public ResourceDescription(String basePath) {
            this.basePath = basePath;
            this.calls = new ArrayList<>();
        }

        private static String mostPreferredOrNull(String[] mediaTypes) {
            if (mediaTypes == null || mediaTypes.length < 1) {
                return "";
            } else {
                return mediaTypes[0].toString();
            }
        }

        public static List<ResourceDescription> fromBoundResourceInvokers(Method[] methods) {
            Map<String, ResourceDescription> descriptions = new HashMap<>();

            for (Method method : methods) {
                String basePath = method.getDeclaringClass().getAnnotation(Path.class).value();
                if (!descriptions.containsKey(basePath)) {
                    descriptions.put(basePath, new ResourceDescription(basePath));
                }

                String subPath = "";
                for (Annotation annotation : method.getDeclaredAnnotations()) {
                    if (annotation.annotationType().equals(Path.class)) {
                        subPath = ((Path) annotation).value();
                        break;
                    }
                }

                descriptions.get(basePath).addMethod(basePath + subPath, method);
            }

            return new LinkedList<>(descriptions.values());
        }

        public void addMethod(String path, Method method) {
            List<String> produces = Stream.of(method.getDeclaredAnnotation(Produces.class),
                    method.getDeclaringClass().getDeclaredAnnotation(Produces.class))
                    .filter(a -> a != null)
                    .map(a -> mostPreferredOrNull(a.value()))
                    .collect(Collectors.toList());
            List<String> consumes = Stream.of(method.getDeclaredAnnotation(Consumes.class),
                    method.getDeclaringClass().getDeclaredAnnotation(Consumes.class))
                    .filter(a -> a != null)
                    .map(a -> mostPreferredOrNull(a.value()))
                    .collect(Collectors.toList());

            if (method.getDeclaredAnnotation(GET.class) != null) {
                calls.add(new MethodDescription("GET", path, produces, consumes));
            }
            if (method.getDeclaredAnnotation(POST.class) != null) {
                calls.add(new MethodDescription("POST", path, produces, consumes));
            }
            if (method.getDeclaredAnnotation(PUT.class) != null) {
                calls.add(new MethodDescription("PUT", path, produces, consumes));
            }
            if (method.getDeclaredAnnotation(DELETE.class) != null) {
                calls.add(new MethodDescription("DELETE", path, produces, consumes));
            }
        }

        @Override
        public String toString() {
            return "ResourceDescription{" +
                    "basePath='" + basePath + '\'' +
                    ", calls=" + calls +
                    '}';
        }
    }

//    private static final class ResourceDescription {
//        private String basePath;
//        private List<MethodDescription> calls;
//
//        public ResourceDescription(String basePath) {
//            this.basePath = basePath;
//            this.calls = new ArrayList<>();
//        }
//
//        private static String mostPreferredOrNull(MediaType[] preferred) {
//            if (preferred == null || preferred.length == 0) {
//                return null;
//            } else {
//                return preferred[0].toString();
//            }
//        }
//
//        public static List<ResourceDescription> fromBoundResourceInvokers(Set<Map.Entry<String, List<ResourceInvoker>>> bound) {
//            Map<String, ResourceDescription> descriptions = new HashMap<>();
//
//            for (Map.Entry<String, List<ResourceInvoker>> entry : bound) {
//                ResourceMethod aMethod = (ResourceMethod) entry.getValue().get(0);
//                String basePath = aMethod.getMethod().getDeclaringClass().getAnnotation(Path.class).value();
//
//                if (!descriptions.containsKey(basePath)) {
//                    descriptions.put(basePath, new ResourceDescription(basePath));
//                }
//
//                for (ResourceInvoker invoker : entry.getValue()) {
//                    ResourceMethod method = (ResourceMethod) invoker;
//                    descriptions.get(basePath).addMethod(basePath, method);
//                }
//            }
//
//            return new LinkedList<>(descriptions.values());
//        }
//
//        public void addMethod(String path, ResourceMethod method) {
//            String produces = mostPreferredOrNull(method.getProduces());
//            String consumes = mostPreferredOrNull(method.getConsumes());
//
//            for (String verb : method.getHttpMethods()) {
//                calls.add(new MethodDescription(verb, path, produces, consumes));
//            }
//        }
//    }
}
