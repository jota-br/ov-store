package ostro.veda.service.events.interfaces;

import ostro.veda.service.events.EventPayload;

import java.lang.reflect.InvocationTargetException;

public interface EventService {

    void handleEvent(EventPayload payload)
            throws InvocationTargetException, IllegalAccessException, NoSuchMethodException, ClassNotFoundException;
}
