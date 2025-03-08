package ostro.veda.service.events;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import ostro.veda.common.dto.Dto;
import ostro.veda.common.util.ApplicationContextProvider;
import ostro.veda.common.util.MainService;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@Slf4j
@Component
public class EventServiceImpl implements EventService {

    @Override
    @EventListener
    public void handleEvent(@NonNull EventPayload payload)
            throws InvocationTargetException, IllegalAccessException, NoSuchMethodException, ClassNotFoundException {

        log.info("EventServiceImpl() Started from source -> {}", payload.getSource());
        Class<?> payloadClass = payload.getDto().getClass();
        MainService annotation = payloadClass.getAnnotation(MainService.class);

        if (annotation == null) {
            log.info("handleEvent() -> payload Class {} isn't supported", payloadClass);
            return;
        }

        try {

            log.info("Annotated Class {} found", annotation);
            String serviceClassName = annotation.getServiceClass();
            Class<?> serviceClass = Class.forName(serviceClassName);
            Object service = ApplicationContextProvider.getContext().getBean(serviceClass);

            Dto dto = payload.getDto();
            Method methodCall = service.getClass().getMethod(payload.getMethodName(), dto.getClass());

            methodCall.invoke(service, dto);

        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {

            log.warn(e.getMessage());
            throw e;

        }
    }
}
